public class Node <T>{


private T k;

//left=child[0]; right=child[1];
private Node<T>[] child = new Node[2];

private Node<T> backLink;
private Node<T> preLink;

Node(T key) {
    this.k = key;
}

public void setLeftChild(Node<T> node) {
    this.child[0] = node;
}

public void setRightChild(Node<T> node) {
    this.child[1] = node;
}

//
// // Global variable with the fixed value of members.
// Node root[2] = {{−∞, {(&root[0], 0, 0, 1), (&root[1], 0, 0, 1)}, &root[1], null},
// {∞, {(&root[0], 0, 0, 0), (null, 0, 0, 1)}, null, null}};

}
