import java.lang.Integer;


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

    private Node[] root;
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

        root = new Node[2];
        root[0] = rootMin;
        root[1] = rootMax;
    }

    boolean Add(int key) {
        //  prev = &root[1]; curr = &root[0];
        Node prev = root[0];
        Node curr = root[1];

        /* Initializing a new node with supplied key and left-link threaded and pointing to itself.*/
        Node node = new Node(key);
        node.setLeftChild(node, THREAD);

        while(true) {

            int dir = locate(prev, curr, key);

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
                // and performes atomic comparedAndSet
                // If the reference of the curr child pointer was modified before the execution of this CAS then that means
                // another thread must have added another node or a remove marked the threaded link
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
                        Node newCurr = prev;

                        // Bitwise comparision xx
                        if( newLinkStamp[0] == MARK ) {
                            cleanMarked(curr, dir);
                        }

                        else if ( newLinkStamp[0] == FLAG ) {
                            cleanFlagged(curr, R, prev, true);
                        }

                        curr = newCurr;
                        prev = newCurr.getBackLink();
                    }

                }
            }
        }
    }

    public void cleanFlagged(Node prev, Node curr, Node back, boolean isThread) {
        if (isThread ) {
            // // Cleaning a flagged order-link
            while(true) {
                // // To mark the right-child link
                // (next, f, m, t) = currchild[1]; get the right childs address and assocaited values


                // if (marked)  break;
                // else if (flagged) {


                // if (back = next) then
                // // If back is getting deleted then move back.
                // back = back->backLink;

                // backNode = currbackLink;
                // CleanFlag(curr, next, backN ode, t);
                // if (back = next) {
                // pDir =cmp(prevk, backN odek);
                // prev = backchild[pDir];
                // }
                //}

                // else {
                // if (currpreLink 6= prev) then currpreLink = prev;
                // result = CAS(currchild[1], (next, 0, 0, t), (next, 0, 1, t)) ;
                // if result then break;
                // }
            }

            // CleanMark(curr, 1);

        }

        else {
            // (right, rF, rM, rT ) = currchild[1];
            // if (rM ) then
            // (lef t, lF, lM, lT ) = currchild[0];
            // preN ode = currpreLink;
            // if (lef t 6= preN ode) then // A category 3 node
            // tryMark(curr, 0);
            // CleanMark(curr, 0);
            // else
            // pDir =cmp(currk,prevk);
            // if (lef t = curr) then
            // CAS(prevchild[pDir], (curr , f, 0, 0), (right, 0, 0, rT ));
            // if (!rT ) then CAS(rightbackLink, (curr, 0, 0, 0), (prev, 0, 0, 0));
            // else
            // CAS(preN odechild[1], (curr, 1, 0, 1), (right, 0, 0, rT ));
            // if (!rT ) then CAS(rightbackLink, (curr, 0, 0, 0), (prev, 0, 0, 0));
            // CAS(prevchild[pDir], (curr, 1, 0, 0), (preN ode, 0, 0, rT ));
            // CAS(preN odebackLink, (curr, 0, 0, 0), (prev, 0, 0, 0));
            // else if (rt and rF ) then
            // // The node is moving to replace its successor
            // delN ode = right;
            // while true do
            // parent = delN odebackLink;
            // pDir =cmp(currk, prevk);
            // (∗, pF, pM, pT ) = parentchild[pDir];
            // if (pM ) then CleanMark(parent, pDir);
            // else if (pF ) then break;
            // else if (CAS(parentchild[pDir], (curr, 0, 0, 0), (curr, 1, 0, 0))) then
            // break;
            // backN ode = parentbackLink;
            // CleanFlag(parent, curr, backN ode, true);
            // // Try marking the child link.
            // // Cleaning a flagged parent-link
            // // The node is to be deleted itself
            // // This is a category 1 or 2 node
            // // A category 1 node
            // // A category 2 node
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
                Node preNode = curr.getBackLink();
                if (preNode.equals(left)){
                    Node parent = curr.getBackLink();
                    // get which direction the child is
                    int pDir;
                    if (parent.getChild(0, new int[1]).equals(curr)){
                        pDir = 0;
                    } else {
                        pDir=1;
                    }



                    Node back = parent.getBackLink();
                    tryFlag(parent,curr,back,true);
                    if (parent.getChild(pDir,new int[1]).equals(curr)){
                        cleanFlagged(parent,curr,back,true);
                        break;
                    }
                    // pick up at line 132 in paper

                }
            }
        }
    }

    /**
     * comparison to map results to indexes
     * 2 := equal, 1:= greater than, 0 := less than
     * */
    private int cmp(int i, int j){
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
    public boolean Contains(int key) {
        return false;
    }

    /**
     *
     * */
    public int locate(Node prev, Node curr, int key){
        while (true){
            int dir = cmp(key,curr.k);

            if (dir == 2){
                // key found
                return dir;
            }
            else{
                int[] nextStamp = new int[1];
                Node next = curr.getChild(dir,nextStamp);

                int m = nextStamp[0] & MARK;
                int t = nextStamp[0] & THREAD;


                if (m == MARK && dir == 1){
                    // remove the node?
                    Node newPrev = prev.getBackLink();
                    cleanMarked(curr,dir);
                    prev= newPrev;
                    int pDir = cmp(key,prev.k);
                    int[] s = new int[1];
                    curr = prev.getChild(pDir,s);
                    continue;
                }
                if(t == THREAD){
                    // check stoping criterion
                    int nextE = curr.k;
                    if (dir == 0 || key < nextE){
                        return dir;
                    }
                    else{
                        prev = curr;
                        curr = next;
                    }
                }


            }
        }



    }



    public boolean remove(int key) {

        boolean result = false;

        /* Initialize the location variables as before. */
        //  Node prev = &root[1];
        //  Node curr = &root[0];

        //  int  dir = locate(prev, curr, k− e);????????

        //  (next, f, ∗, t) = currchild[dir];

        //  if (k 6= nextk) then return false;

        //  else

        // // flag the order-link
        // result = tryFlag(curr, next, prev, true);
        // if (prevchild[dir].ref = curr) then
        // CleanFlag(curr, next, prev, true);


        return result;
    }

    public boolean tryFlag(Node prev, Node curr, Node backLink, boolean inThread) {

    //  while true do
    //  pDir = cmp(currk, prevk) & 1;
    //  // If cmp returns 2 then curr is the left link of prev; so pDir is changed to 0.
    // // Try atomically flagging the parent link.
    //  t = isThread;
    //  result = CAS(prevchild[pDir], (curr, 0, 0, t), (curr, 1, 0, t));
    //  if result then return true;
    //  else
    // /* The CAS fails, check if the link has been marked, flagged or the curr node got deleted. If flagged, return false; if
    // marked, first clean it; else just proceed */
    //  (newR, f, m, t) = prevchild[pDir];
    //  if (newR = curr) then
    //  if f then return false;
    //  else if m then
    //  cleanMarked(prev, pDir);
    // prev = back;
    // pDir =cmp(currk, prevk);
    // newCurr = (prevchild[newP Dir])·ref ; locate(prev, newCurr, currk);
    // if (newCurr 6=curr ) then
    // return false;
    //  back = prevbackLink;

        return false;
    }

    public void tryMark(Node curr, int dir) {
    // while true do
    // back = currbackLink;
    // (next, f, m, t) = currchild[dir];
    // if (m == 1) then break;
    // else if (f == 1) then
    // if (t == 0) then
    // CleanFlag(curr, next, back, false); continue;
    // else if ((t == 1) and (dir == 1)) then
    // CleanFlag(curr, next, back, true); continue;
    // result = CAS(currchild[dir], (next, 0, 0, t), (next, 0, 1, t));
    // if result then break;

    }






}
