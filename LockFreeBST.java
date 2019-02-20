public class LockFreeBST <T> {

    private Node<T>[] root;

    // Declare infite global Nodes here for Contains (page 5 near bottom paragraph)
    // root[0] = -oo root[1] = oo
    // root[0] is the left child of root[1]
    LockFreeBST() {
        T min; T max;

        root = new Node[2];
        root[0] = new Node<T>(min); //temp
        root[1] =  new Node<T>(max); //temp
    }

    public int Locate(Node<T> prev, Node<T> curr, T key){
        return 0;
    }

    public boolean Contains(T key) {
        return false;
    }

    public boolean Remove(T key) {

        boolean result = false;

        /* Initialize the location variables as before. */
        //  Node<T> prev = &root[1];
        //  Node<T> curr = &root[0];

        //  int  dir = Locate(prev, curr, k− e);????????

        //  (next, f, ∗, t) = currchild[dir];

        //  if (k 6= nextk) then return false;

        //  else

        // // flag the order-link
        // result = TryFlag(curr, next, prev, true);
        // if (prevchild[dir].ref = curr) then
        // CleanFlag(curr, next, prev, true);


        return result;
    }

    public boolean TryFlag(Node<T> prev, Node<T> curr, Node<T> backLink, boolean inThread) {

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
    //  CleanMarked(prev, pDir);
    // prev = back;
    // pDir =cmp(currk, prevk);
    // newCurr = (prevchild[newP Dir])·ref ; Locate(prev, newCurr, currk);
    // if (newCurr 6=curr ) then
    // return false;
    //  back = prevbackLink;

        return false;
    }

    public void TryMark(Node<T> curr, int dir) {
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

    public void CleanFlag(Node<T> prev, Node<T> curr, Node<T> backLink, boolean isThread) {

    }

    public void CleanMark(Node<T> curr, int markDir) {

    }


    boolean Add(T key) {
    //  prev = &root[1]; curr = &root[0];
        Node<T> prev = root[0];
        Node<T> curr = root[1];

        // /* Initializing a new node with supplied key and left-link threaded and pointing to itself.*/
        //  nodechild[0] = (node, 0, 0, 1);
        Node<T> node = new Node<T>(key);

        while(true) {

            int dir = Locate(prev, curr, key);

            //  if (dir = 2) then // key exists in the BST return false;
            if (dir == 2) {
                return false;
            }

            else {
                //  (R, ∗, ∗, ∗) = currchild[dir];

                // /* The located link is threaded. Set the right-link of the adding node to copy this value */

                //  nodechild[1] = (R, 0, 0, 1);

                //  nodebackLink = curr;

                //  result = CAS(currchild[dir], (R, 0, 0, 1), (node, 0, 0, 0));
                boolean result = true;

                //  // Try inserting the new node.

                // if result then return true;
                if (result) {
                    return true;
                }

                else {
                    // /* If the CAS fails, check if the link has been marked, flagged or a new node has been inserted.
                    //  If marked or
                    // flagged, first help cleaning. */
                    // (newR, f, m, t) = currchild[dir];
                    // if (newR = R) then
                    // newCurr = prev;
                    // if m then CleanMarked(curr, dir);
                    // else if f then
                    // CleanFlagged(curr, R, prev, true);
                    // curr = newCurr;
                    // prev = newCurrbackLink
                }
            }
        }
    }

}
