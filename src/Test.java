import java.util.concurrent.atomic.AtomicStampedReference;

public class Test {
    public static void main(String [] args) {
        Node n = new Node(1);
        Node n2 = new Node(2);

        n.setLeftChild(n2, 7);
        int[] stamp = new int[1];

        System.out.printf("n:%s, n2:%s\n", n, n2);
        System.out.printf("n:%d, n2:%d\n", n.k, n2.k);
        System.out.printf("n:%s, n2:%s\n", n, n.getChild(0, stamp));
        System.out.printf("n:%s, n2:%s\n", n, n.child[0]);
        System.out.printf("stamp: %d\n", stamp[0]);

    }
}
