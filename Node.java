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
}
