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

public class Node {

    private int k;

    //left=child[0]; right=child[1];
    private AtomicStampedReference<Node>[] child = new AtomicStampedReference[2];
    private AtomicStampedReference<Node> backLink;
    private AtomicStampedReference<Node> preLink;

    Node(int key) {
        this.k = key;
    }

    public void setLeftChild(Node leftChild, int bit) {
        this.child[0].set(leftChild, bit);
    }

    public void setRightChild(Node rightChild, int bit) {
        this.child[1].set(rightChild, bit);
    }

    public void setBackLink(Node backLink, int bit) {
        this.backLink.set(backLink, bit);
    }

    public void setPreLink(Node preLink, int bit) {
        this.preLink.set(null, bit);
    }

    // Returns the Node reference of left or right child based on dir, 0 = left,
    // 1 = right. and also put the stamped value into linkStamp in one atomic step
    public Node getChild(int dir, int[] linkStamp ) {
        return this.child[dir].get(linkStamp);
    }

    // Takes in the direction(left or right child), initialRef, initialStamp, and newRef and newStamp
    // and performes atomic comparedAndSet
    public boolean childCAS(int dir, Node initialRef, int initialStamp, Node newRef, int newStamp) {
        return this.child[dir].compareAndSet(initialRef, newRef, initialStamp, newStamp);
    }

    public Node getBackLink() {
        return this.backLink.getReference();
    }
}