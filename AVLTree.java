package avltree;

import static java.lang.Math.pow;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 *
 * @author Tikhoglo
 * @param <V> the type of node values
 */
public class AVLTree<V> {

    private Node root;
    private int size;

    private class Node<V> implements Comparable<Node> {

        private Integer key;
        private V value;
        private Node left;
        private Node right;
        private Node parent;

        public Node() {

        }

        public Node(Integer key, V value) {
            this.key = key;
            this.value = value;
        }

        public Node(Integer key) {
            this.key = key;
        }

        @Override
        public int compareTo(Node o) {
            return this.key.compareTo(o.key);
        }

        public String toString() {
            return this.key + ": " + this.value;
        }
    };

    public int getSize() {
        return this.size;
    }

    public AVLTree() {
    }

    /**
     * @param key the key for searching in the tree
     */
    public V search(Integer key) {
        return searchAt(this.root, key);
    }

    private V searchAt(Node node, Integer key) throws NoSuchElementException {
        V foundValue;

        if (node == null) {
            throw new NoSuchElementException("No element with such key found");
        } else if (key.compareTo(node.key) == 0) {
            foundValue = (V) node.value;
        } else if (key.compareTo(node.key) < 0) {
            foundValue = searchAt(node.left, key);
        } else {
            foundValue = searchAt(node.right, key);
        }

        return foundValue;
    }

    public void insert(Integer key, V value) {
        Node node = new Node(key, value);

        insertAt(this.root, node);

        balance(node);
    }

    private void insertAt(Node x, Node y) {
        if (x == null) {
            this.root = y;
            this.size++;
        } else if (x.compareTo(y) == 0) {
            x.value = y.value;
        } else if (x.compareTo(y) < 0) {
            if (x.right != null) {
                insertAt(x.right, y);
            } else {
                x.right = y;
                y.parent = x;
                this.size++;
            }
        } else if (x.compareTo(y) > 0) {
            if (x.left != null) {
                insertAt(x.left, y);
            } else {
                x.left = y;
                y.parent = x;
                this.size++;
            }
        }
    }

    public void remove(Integer k) {
        removeAt(this.root, k);
    }

    /**
     * Finds the node to remove
     */
    private void removeAt(Node n, Integer k) throws NoSuchElementException {
        if (n == null) {
            throw new NoSuchElementException("Unexisting element can`t be removed");
        } else if (n.key.compareTo(k) == 0) {
            remioveFound(n);
        } else if (n.key.compareTo(k) < 0) {
            removeAt(n.right, k);
        } else if (n.key.compareTo(k) > 0) {
            removeAt(n.left, k);
        }
    }

    private void remioveFound(Node n) {
        if (n == this.root) {
            if (n.right != null) {
                Node replacement = minimum(n.right);
                n.key = replacement.key;
                n.value = replacement.value;
                removeAt(n.right, replacement.key);
            } else if (n.right == null) {
                this.root = n.left;
                this.size--;
                return;
            }
        } else if (n.left == null || n.right == null) {
            if (n.left == null) {
                if (n.parent.right == n) {
                    n.parent.right = n.right;
                } else if (n.parent.left == n) {
                    n.parent.left = n.right;
                }
            }
            if (n.right == null) {
                if (n.parent.right == n) {
                    n.parent.right = n.left;
                } else if (n.parent.left == n) {
                    n.parent.left = n.left;
                }
            }
            this.size--;
        } else {
            Node replacement = minimum(n.right);
            n.key = replacement.key;
            n.value = replacement.value;
            removeAt(n.right, replacement.key);
        }
        
        balance(n);
    }

    private void balance(Node a) {

        while (true) {
            int balance = height(a.left) - height(a.right);

            if (balance == -2) {
                if (height(a.right.left) <= height(a.right.right)) {
                    rotateLeftMinor(a);
                    break;
                } else {
                    rotateLeftMajor(a);
                    break;
                }
            } else if (balance == 2) {
                if (height(a.left.right) <= height(a.left.left)) {
                    rotateRightMinor(a);
                    break;
                } else {
                    rotateRightMajor(a);
                    break;
                }
            }
            if (a.parent == null){
                this.root = a;
                return;
            }
            a = a.parent;
        }
    }

    //height of the subtree, whose root is x
    private int height(Node x) {
        if (x == null) {
            return -1;
        } else if (x.left == null && x.right == null) {
            return 0;
        } else if (x.right == null && x.left != null) {
            return 1 + height(x.left);
        } else if (x.left == null && x.right != null) {
            return 1 + height(x.right);
        } else {
            return 1 + Math.max(height(x.left), height(x.right));
        }
    }

    private void rotateLeftMinor(Node a) {
        Node b = a.right;
        if (b.left != null) {
            b.left.parent = a;
        }
        a.right = b.left;
        b.left = a;
        b.parent = a.parent;
        a.parent = b;
        if (b.parent != null) {
            if (a == b.parent.left) {
                b.parent.left = b;
            } else if (a == b.parent.right) {
                b.parent.right = b;
            }
        }
    }

    private void rotateLeftMajor(Node a) {
        rotateRightMinor(a.right);
        rotateLeftMinor(a);
    }

    private void rotateRightMajor(Node a) {
        rotateLeftMinor(a.left);
        rotateRightMinor(a);
    }

    private void rotateRightMinor(Node a) {
        Node b = a.left;
        if (b.right != null) {
            b.right.parent = a;
        }
        a.left = b.right;
        b.right = a;
        b.parent = a.parent;
        a.parent = b;
        if (b.parent != null) {
            if (a == b.parent.left) {
                b.parent.left = b;
            } else if (a == b.parent.right) {
                b.parent.right = b;
            }
        }
    }

    //finds the minimal value of the subtree whose root is root
    private Node minimum(Node node) {
        if (node.left == null) {
            return node;
        } else {
            return minimum(node.left);
        }
    }

    //finds the minimal value of the subtree whose root is root
    private Node maximum(Node node) {
        if (node.right == null) {
            return node;
        } else {
            return maximum(node.right);
        }
    }

    public List<V> inOrder() {
        List<V> list = new ArrayList<>();
        recursiveInOrder(this.root, list);
        return list;
    }

    private void recursiveInOrder(Node n, List<V> list) {
        if (n == null) {
            return;
        }
        recursiveInOrder(n.left, list);
        list.add((V) n.value);
        recursiveInOrder(n.right, list);
    }

    public List<V> preOrder() {
        List<V> list = new ArrayList<>();
        recursivePreOrder(this.root, list);
        return list;
    }

    private void recursivePreOrder(Node n, List<V> list) {
        if (n == null) {
            return;
        }
        list.add((V) n.value);
        recursivePreOrder(n.left, list);
        recursivePreOrder(n.right, list);
    }

    public List<V> postOrder() {
        List<V> list = new ArrayList<>();
        recursivePostOrder(this.root, list);
        return list;
    }

    private void recursivePostOrder(Node n, List<V> list) {
        if (n == null) {
            return;
        }
        recursivePostOrder(n.left, list);
        recursivePostOrder(n.right, list);
        list.add((V) n.value);
    }

    /**
     * Prints rows of AVL tree (in a user-readable way)
     *
     * @param rows sets the number of rows for output absence of node is marked
     * as null: 0
     */
    public void printRows(int rows) {
        Queue<Node> q = new LinkedList<>();
        if (this.root == null) {
            q.offer(new Node(null, 0));
        } else {
            q.offer(this.root);
        }
        int rowCounter = 1;
        int nodesCounter = 0;
        while (!q.isEmpty()) {
            Node current = q.poll();
            nodesCounter++;
            System.out.print(current.key + ": " + current.value + "    ");
            if (rowCounter < rows) {
                if (current.left != null) {
                    q.offer(current.left);
                } else {
                    q.offer(new Node(null, 0));
                }
                if (current.right != null) {
                    q.offer(current.right);
                } else {
                    q.offer(new Node(null, 0));
                }
            }

            if (nodesCounter == pow(2, rowCounter) - 1) {
                System.out.println("");
                rowCounter++;
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Menu menu = new Menu();
        Scanner s = new Scanner(System.in);
        while(true){
            System.out.println("Enter a command: ");
            menu.insertCommand(s.nextLine());
        }
    }
}
