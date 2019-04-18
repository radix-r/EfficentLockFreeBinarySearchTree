import org.deuce.Atomic;




// This code is contributed by Ankur Narain Verma
// Modified by Chinh Le and Ross Wagner to incorporate STMP using Deuce

public class STMBST {

    class Node {
        int key;
        Node left, right;

        public Node(int item) {
            key = item;
            left = right = null;
        }
    }

    Node root;

    STMBST() {
        root = new Node(Integer.MAX_VALUE);
    }

    @Atomic
    public Node contains(Node root, int key)
    {
        if (root==null || root.key==key)
            return root;

        if (root.key > key)
            return contains(root.left, key);

        return contains(root.right, key);
    }

    void insert(int key) {
        root = insertRec(root, key);
    }

    Node insertRec(Node root, int key) {

        /* If the tree is empty, return a new node */
        if (root == null) {
            return insertNode(key);
        }

        /* Otherwise, recur down the tree */
        if (key < root.key)
            root.left = insertRec(root.left, key);
        else if (key > root.key)
            root.right = insertRec(root.right, key);

        /* return the (unchanged) node pointer */
        return root;
    }

    @Atomic
    Node insertNode(int key) {
        return new Node(key);
    }

    void inorder()  {
        inorderRec(root);
    }

    void inorderRec(Node root) {
        if (root != null) {
            inorderRec(root.left);
            System.out.println(root.key);
            inorderRec(root.right);
        }
    }

    @Atomic
    void removeKey(int key)
    {
        root = removeRec(root, key);
    }

    Node removeRec(Node root, int key)
    {
        /* If the tree is empty */
        if (root == null)  return root;

        /* Otherwise, recur down the tree */
        if (key < root.key)
            root.left = removeRec(root.left, key);
        else if (key > root.key)
            root.right = removeRec(root.right, key);

            // if key is same as root's key, then This is the node
            // to be removed
        else
        {
            // node with only one child or no child
            if (root.left == null)
                return root.right;
            else if (root.right == null)
                return root.left;

            // node with two children: Get the inorder successor (smallest
            // in the right subtree)
            root.key = minValue(root.right);

            // remove the inorder successor
            root.right = removeRec(root.right, root.key);
        }

        return root;
    }

    int minValue(Node root)
    {
        int minv = root.key;
        while (root.left != null)
        {
            minv = root.left.key;
            root = root.left;
        }
        return minv;
    }

    // Driver Program to test above functions
    public static void main(String[] args) {
        STMBST tree = new STMBST();

        tree.insert(50);
        tree.insert(30);
        tree.insert(20);
        tree.insert(40);
        tree.insert(70);
        tree.insert(60);
        tree.insert(80);

        tree.inorder();
    }
}


