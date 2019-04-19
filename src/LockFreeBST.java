import java.lang.Integer;
import java.lang.ref.Reference;
import java.sql.Ref;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.AtomicStampedReference;


/*  BitWise Stamped
    flagged, marked, threaded
0    000
1    001
2    010
3    011
4    100
5    101
6    110
7    111
*/


public class LockFreeBST  {

    private final int THREAD = 1;
    private final int MARK = 2;
    private final int FLAG = 4;

    //private Node[] root;
    private AtomicReferenceArray<Node> root;
    private Node rootMin;
    private Node rootMax;

    // Declare infite global Nodes here for Contains (page 5 near bottom paragraph)
    // root[0] = -oo root[1] = oo
    // root[0] is the left child of root[1]
    LockFreeBST() {
        rootMin = new Node(Integer.MIN_VALUE);
        rootMax = new Node(Integer.MAX_VALUE);

        rootMin.setLeftChild(rootMin, THREAD);
        rootMin.setRightChild(rootMax, THREAD);
        rootMin.setBackLink(rootMax, 0);
        rootMin.setPreLink(null, 0);


        rootMax.setLeftChild(rootMin, 0);
        rootMax.setRightChild(null, THREAD);
        rootMax.setBackLink(null, 0);
        rootMax.setPreLink(null, 0);

        root = new AtomicReferenceArray<Node>(2);
        root.getAndSet(0,rootMin);
        root.getAndSet(1,rootMax);
    }

    boolean add(int key) {
        //  prev = &root[1]; curr = &root[0];
        Node pre = root.get(1);
        Node curr = root.get(0);



        /* Initializing a new node with supplied key and left-link threaded and pointing to itself.*/
        Node node = new Node(key);
        node.setLeftChild(node, THREAD);

        while(true) {
            // use array to pass by ref
            Node[] preCurr = new Node[]{pre,curr};

            int dir = locate(preCurr,key);
            pre = preCurr[0];
            curr = preCurr[1];


            //  if (dir = 2) then key exists in the BST return false;
            if (dir == 2) {
                return false;
            }

            else {
                // Some temporary node R get the childs based on dir reference and all of the flag, mark, threaded bits
                //  (R, ∗, ∗, ∗) = curr->child[dir];
                int [] linkStamp = new int[1];
                Node R = curr.getChild(dir, linkStamp);

                // /* The located link is threaded. Set the right-link of the adding node to copy this value */
                //  nodechild[1] = (R, 0, 0, 1);
                node.setRightChild(R, THREAD);
                //  nodebackLink = curr;

                node.setBackLink(curr, 0);

                //  result = CAS(currchild[dir], (R, 0, 0, 1), (node, 0, 0, 0)); //  // Try inserting the new node.
                // childCAS takes in the direction(left or right child), initialRef, initialStamp, and newRef and newStamp
                // and preformes atomic comparedAndSet
                boolean result = curr.childCAS(dir, R, linkStamp[0], node, 0);

                // if result then return true;
                if (result) {
                    return true;
                }

                else {
                    /* If the CAS fails, check if the link has been marked, flagged or a new node has been inserted.
                     If marked or flagged, first help cleaning. */
                    // (newR, f, m, t) = currchild[dir];
                    int [] newLinkStamp = new int[1];
                    Node newR = curr.getChild(dir, newLinkStamp);

                    // compare references
                    if (newR == R) {
                        Node newCurr = pre;

                        // Bitwise comparision xx
                        if( newLinkStamp[0] == MARK ) {
                            cleanMarked(curr, dir);
                        }

                        else if ( newLinkStamp[0] == FLAG ) {
                            cleanFlagged(curr, R, pre, true);
                        }

                        curr = newCurr;
                        pre = newCurr.getBackLink(new int[1]);
                    }

                }
            }
        }
    }

    public void cleanFlagged(Node prev, Node curr, Node back, boolean isThread) {
        if (isThread) {
            // // Cleaning a flagged order-link
            while(true) {
                // To mark the right-child link
                int [] linkStamp = new int[1];
                Node next = curr.getChild(1, linkStamp);


                if ( (linkStamp[0]&MARK) == MARK)  break;

                else if ( (linkStamp[0]&FLAG) == FLAG) {
                    // Help cleaning if right-child is flagge
                    if (back == next) {
                        // If back is getting deleted then move back.
                        back = back.getBackLink(new int[1]);
                    }

                    Node backNode = curr.getBackLink(new int[1]);
                    cleanFlagged(curr, next, backNode, ((linkStamp[0]&THREAD) == THREAD) );

                    if (back == next) {
                        int pDir = cmp(prev.k, backNode.k);
                        prev = back.getChild(pDir, new int[1]);
                    }
                }

                else {
                    if (curr.getPreLink(new int[1]) != prev) curr.setPreLink(prev, 0); // I Worry about setting stamp to 0
                    // Try marking the child link.
                    boolean result = curr.childCAS(1, next, linkStamp[0]&THREAD, next, (linkStamp[0]&THREAD)+MARK);
                    if(result) break;
                }
            }

            cleanMarked(curr, 1);

        }

        else {
            // Cleaning a flagged parent-link
            int[] rightStamp = new int[1];
            int[] leftStamp = new int[1];
            Node left;
            Node preNode;
            Node right = curr.getChild(1, rightStamp);

            // The node is to be deleted itself
            if ((rightStamp[0]&MARK) == MARK ) {
                left = curr.getChild(0, leftStamp);
                preNode = curr.getPreLink(new int[1]);

                // A category 3 node
                if (left != preNode){
                    tryMark(curr, 0);
                    cleanMarked(curr, 0);
                }

                // This is a category 1 or 2 node
                else {
                    int pDir = cmp(curr.k,prev.k);

                    // A category 1 node
                    if (left == curr) {
                        prev.childCAS(pDir, curr, FLAG, right, rightStamp[0]&THREAD);
                        if( (rightStamp[0]&THREAD) != THREAD) right.casBacklink(curr, 0, prev, 0);
                    }

                    // A category 2 node
                    else {
                        preNode.childCAS(1, curr, FLAG+THREAD, right, rightStamp[0]&THREAD);
                        if((rightStamp[0]&THREAD) != THREAD) right.casBacklink(curr, 0, prev, 0);
                        prev.childCAS(pDir, curr, FLAG, preNode, rightStamp[0]&THREAD);
                        preNode.casBacklink(curr, 0, prev, 0);
                    }
                }
            }

            // The node is moving to replace its successor
            else if ( (rightStamp[0]&THREAD) == THREAD && (rightStamp[0]&FLAG) == FLAG ) {
                Node delNode = right;
                Node parent;

                while(true) {
                    int[] parentStamp = new int[1];
                    parent = delNode.getBackLink(new int[1]);
                    int pDir = cmp(curr.k, prev.k);

                    Node temp = parent.getChild(pDir, parentStamp);

                    if ( (parentStamp[0]&MARK) == MARK) cleanMarked(parent, pDir);

                    else if ((parentStamp[0]&FLAG) == FLAG ) break;

                    else if (parent.childCAS(pDir, curr, 0, curr, FLAG)) break;
                }

                Node backNode = parent.getBackLink(new int[1]);
                cleanFlagged(parent, curr, backNode, true);
            }
        }
    }

    public void cleanMarked(Node curr, int markDir) {
        int[] lStamp = new int[1];
        Node left = curr.getChild(0,lStamp);

        int[] rStamp = new int[1];
        Node right = curr.getChild(1,rStamp);

        if (markDir == 1){
            /* the node is getting itself. if it is category 1 or 2 node, flag the incoming parent link; if it is a category 3
                node, flag the incoming parent link of its predecessor*/

            while ( true ){
                int[] preStamp = new int[1];
                Node preNode = curr.getPreLink(preStamp);
                if (preNode.equals(left)){
                    Node parent = curr.getBackLink(new int[1]);
                    // get which direction the child is
                    int pDir;
                    if (parent.getChild(0, new int[1]).equals(curr)){
                        pDir = 0;
                    } else {
                        pDir=1;
                    }



                    Node back = parent.getBackLink(new int[1]);
                    tryFlag(parent,curr,back,true);
                    if (parent.getChild(pDir,new int[1]).equals(curr)){
                        cleanFlagged(parent,curr,back,true);
                        break;
                    }


                } else{
                    // Cat 3 node
                    // get parent
                    int[] pDir = new int[1];
                    int[] curBackStamp = new int[1];
                    Node parent = curr.getBackLink(curBackStamp,pDir);
                    int[] preBackStamp = new int[1];
                    Node preParent = preNode.getBackLink(preBackStamp);
                    int[] linkStamp = new int[1];
                    preParent.getChild(1,linkStamp);
                    int[] preParBackStamp = new int[1];
                    Node backNode = preParent.getBackLink(preParBackStamp);
                    // check if link marked
                    if((linkStamp[0]&MARK) == MARK){
                        // if marked clean
                        cleanMarked(preParent,1);
                    }
                    // Check if flagged
                    else if((linkStamp[0]&FLAG)==FLAG){

                        cleanFlagged(preParent,preNode,backNode,true);
                        break;
                    } // else try flag parent of order-node
                    else if(parent.childCAS(pDir[0],curr,0,curr,FLAG)){

                    }

                }
            }
        } else{
            // node is getting deleted or moved to replace its successor
            if((rStamp[0]&MARK)==MARK){
                // getting deleted. clean its left marked link
                int[] preStamp = new int[1];
                Node preNode = curr.getPreLink(preStamp);
                tryMark(preNode,0);
                cleanMarked(preNode,0);
            } else if ((rStamp[0]&(THREAD+FLAG))==(THREAD+FLAG)){
                // move to replace successor. Change links accordingly
                Node delNode = right;
                // parent of node to delete ?
                Node delNodePa = delNode.getBackLink(new int[1]);
                int[] currDir = new int[1];
                Node preParent = curr.getBackLink(currDir);
                int pDir = cmp(delNode.k,delNodePa.k);

                // get left and right children of delNode
                int[] delNodeLStamp = new int[1];
                Node delNodeL = delNode.getChild(0,delNodeLStamp);

                int[] delNodeRStamp = new int[1];
                Node delNodeR = delNode.getChild(1,delNodeRStamp);

                int[] currStamp = new int[1];
                preParent.getChild(currDir[0],currStamp);

                // expect curr, *00
                preParent.childCAS(1,curr,currStamp[0]&FLAG,left,lStamp[0]&(FLAG+THREAD));

                if((lStamp[0]&THREAD)==0){
                    left.casBacklink(curr,0,preParent,0);
                }

                curr.childCAS(0,left,(MARK+(lStamp[0]&THREAD)), delNode,0);

                delNode.casBacklink(delNode,0,curr,0);

                curr.childCAS(1,right,FLAG+THREAD,delNodeR,delNodeRStamp[0]&THREAD);

                if((delNodeRStamp[0]&THREAD) != THREAD){
                    delNodeR.casBacklink(delNode,0,curr,0);
                }
                delNodePa.childCAS(pDir,delNode,FLAG,curr,0);
                curr.casBacklink(preParent,0,delNodePa,0);
            }
        }
    }

    /**
     * comparison to map results to indexes
     * 2 := equal, 1:= greater than, 0 := less than
     * */
    public static int cmp(int i, int j){
        int dir = Integer.compare(i,j);

        // remap dir to indexes
        switch (dir){
            case -1:
                dir = 0;
                break;
            case 0:
                dir = 2;
                break;
        }
        return dir;
    }

    public boolean contains(int key) {
        Node pre = root.get(1);
        Node curr = root.get(0);
        Node[] preCurr = new Node[]{pre,curr};
        // int dir = locate(prev,curr,key);
        int dir = locate(preCurr,key);

        if(dir == 2){
            return true;
        }else {
            return false;
        }
    }

    /**
     * Returns the node whose threaded right pointer would point to the node with the value k
     * */
    public Node findPred(int key, int[] direction1, int[] direction2){
        /* Initialize the location variables as before. */
        Node prev = root.get(1);
        Node curr = root.get(0);


        while (true){
            int dir = cmp(key,curr.k);

            if(dir == 2){
                // target found go left
                dir = 0;
            }
            direction1[0]=dir;
            int[] nextStamp = new int[1];
            Node next = curr.getChild(dir,nextStamp);

            int m = nextStamp[0] & MARK;
            int t = nextStamp[0] & THREAD;


            if (m == MARK && dir == 1){
                // remove the node?
                Node newPrev = prev.getBackLink(new int[1]);
                cleanMarked(curr,dir);
                prev= newPrev;
                int pDir = cmp(key,prev.k);
                int[] s = new int[1];
                curr = prev.getChild(pDir,s);
                continue;
            }
            if(t == THREAD){
                // check stopping criterion
                int nextE = next.k;
                if (dir == 0 || key <= nextE){
                    return curr;
                }
                /*
                else{
                    prev = curr;
                    curr = next;
                }*/
            }
            // continue search
            direction2[0] = direction1[0];
            prev = curr;
            curr = next;



        }

    }

    /**
     * Locate returns 2 if key is found otherwise 0 or 1 if the last visited link is left or right respectively.
     * */
    public int locate(Node[] prevCurr, int key){
        while (true){
            int dir = cmp(key,prevCurr[1].k);

            if (dir == 2){
                // key found
                return dir;
            }
            else{
                int[] nextStamp = new int[1];
                Node next = prevCurr[1].getChild(dir,nextStamp);

                int m = nextStamp[0] & MARK;
                int t = nextStamp[0] & THREAD;


                if (m == MARK && dir == 1){
                    // remove the node?
                    Node newPrev = prevCurr[0].getBackLink(new int[1]);
                    cleanMarked(prevCurr[1],dir);
                    prevCurr[0]= newPrev;
                    int pDir = cmp(key,prevCurr[0].k);
                    int[] s = new int[1];
                    prevCurr[1] = prevCurr[0].getChild(pDir,s);
                    continue;
                }
                if(t == THREAD){
                    // check stopping criterion
                    int nextE = next.k;
                    if (dir == 0 || key < nextE){
                        return dir;
                    }
                    /*
                    else{
                        prev = curr;
                        curr = next;
                    }*/
                }
                // continue search
                prevCurr[0] = prevCurr[1];
                prevCurr[1] = next;


            }
        }



    }



    public boolean remove(int key) {

        boolean result = false;

        /* Initialize the location variables as before. */
        Node pre = root.get(1);
        Node curr = root.get(0);

        // find largest node less than k? I think so
        int[] dir1 = new int[1];
        int[] dir2 = new int[]{-1};
        Node pred = findPred(key, dir1,dir2);

        Node[] preCurr = new Node[]{pre,curr};
        int  d = locate(preCurr, pred.k); // wtf ????????
        pre = preCurr[0];
        curr = preCurr[1];

        //  (next, f, ∗, t) = currchild[dir];
        int[] nextStamp = new int[1];

        Node next = curr.getChild(dir1[0],nextStamp);



        if (key != next.k) {
            return false;
        }else{
            // flag the order-link
            result = tryFlag(curr, next, pre, true);
            if (pre.getChild(dir2[0],new int[1])==curr){
                // if nodes still in same place attempt removal
                cleanFlagged(curr, next, pre, true);
            }

        }




        return result;
    }

    public boolean tryFlag(Node prev, Node curr, Node back, boolean isThread) {

        while(true){
            // If cmp returns 2 then curr is the left link of prev; so pDir is changed to 0.
            int pDir = cmp(curr.k, prev.k) & 1;

            // Try atomically flagging the parent link.
            int t = isThread ? 1 : 0;
            boolean result = prev.childCAS(pDir, curr, t, curr, FLAG+t);

            if(result) return true;

            else {
                // /* The CAS fails, check if the link has been marked, flagged or the curr node got deleted. If flagged, return false; if
                // marked, first clean it; else just proceed */
                int [] newRStamp = new int[1];
                Node newR = prev.getChild(pDir, newRStamp);

                 if (newR == curr) {
                    if( (newRStamp[0]&FLAG) == FLAG ) return false;

                    else if ( (newRStamp[0]&MARK) == MARK ) {
                         cleanMarked(prev, pDir);
                    }

                    prev = back;
                    pDir = cmp(curr.k, prev.k);


                    Node newCurr = prev.getChild(pDir,new int[1]);
                    Node[] preCurr = new Node[]{prev,newCurr};
                    locate(preCurr, curr.k);
                    prev = preCurr[0];
                    newCurr = preCurr[1];

                    if (newCurr != curr ) return false;

                    back = prev.getBackLink(new int[1]);
                 }
            }
        }
    }

    public void tryMark(Node curr, int dir) {
        while(true) {
            Node back = curr.getBackLink(new int[1]);
            int[] nextStamp = new int[1];
            Node next = curr.getChild(dir, nextStamp);

            if ((nextStamp[0]&MARK) == MARK) break;

            else if ((nextStamp[0]&FLAG) == FLAG) {
                if ((nextStamp[0]&THREAD) == 0) {
                    cleanFlagged(curr, next, back, false); continue;
                }

                else if (((nextStamp[0]&THREAD) == THREAD) && (dir == 1)) {
                    cleanFlagged(curr, next, back, true); continue;
                }

            }

            // Try atomically marking the child link.
            boolean result = curr.childCAS(dir, next, nextStamp[0]&THREAD, next, MARK+(nextStamp[0]&THREAD));
            if (result) break;
        }
    }
}
