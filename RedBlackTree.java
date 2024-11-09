class RBTNode {
    char key;
    RBTNode right;
    RBTNode left;
    RBTNode parent;
    public Boolean isRed;

    public RBTNode(char key) {
        this.key = key;
        this.isRed = true; // New nodes are red by default
        this.left = RBT.NIL;
        this.right = RBT.NIL;
        this.parent = RBT.NIL;
    }

    public void setLeft(RBTNode left) {
        this.left = left;
    }

    public void setRight(RBTNode right) {
        this.right = right;
    }

    public void setParent(RBTNode parent) {
        this.parent = parent;
    }

    public void setRed(Boolean isRed) {
        this.isRed = isRed;
    }

    public RBTNode getRight() {
        return right;
    }

    public RBTNode getLeft() {
        return left;
    }

    public RBTNode getParent() {
        return parent;
    }

    public Boolean isRED() {
        return isRed;
    }

    public char getKey() {
        return key;
    }
}

class RBT {
    public static final RBTNode NIL = new RBTNode((char) 0); // Sentinel NIL node
    public RBTNode root;

    public RBT() {
        NIL.isRed = false; // NIL node is always black
        root = NIL;
    }

    private RBTNode rotateLeft(RBTNode node) {
        RBTNode temp = node.getRight();
        node.right = temp.left;

        if (temp.left != NIL) {
            temp.left.parent = node;
        }
        temp.parent = node.parent;

        if (node.parent == NIL) {
            root = temp;
        } else if (node == node.parent.left) {
            node.parent.left = temp;
        } else {
            node.parent.right = temp;
        }
        temp.left = node;
        node.parent = temp;
        return temp;
    }

    private RBTNode rotateRight(RBTNode node) {
        RBTNode temp = node.getLeft();
        node.left = temp.right;

        if (temp.right != NIL) {
            temp.right.parent = node;
        }
        temp.parent = node.parent;

        if (node.parent == NIL) {
            root = temp;
        } else if (node == node.parent.right) {
            node.parent.right = temp;
        } else {
            node.parent.left = temp;
        }
        temp.right = node;
        node.parent = temp;
        return temp;
    }

    public void insert(char key) {
        RBTNode newNode = new RBTNode(key);
        newNode.left = NIL;
        newNode.right = NIL;
        root = insertRec(root, newNode);
        fixUpInsert(newNode);
    }

    private RBTNode insertRec(RBTNode root, RBTNode node) {
        if (root == NIL) {
            return node;
        }
        if (node.key < root.key) {
            root.left = insertRec(root.left, node);
            root.left.parent = root;
        } else{
            root.right = insertRec(root.right, node);
            root.right.parent = root;
        }
        return root;
    }
    
    private void fixUpInsert(RBTNode node) {
        while (node != root && node.parent != NIL && node.parent.isRed) {
            RBTNode parent = node.parent;
            RBTNode grandParent = parent.parent;

            if (grandParent == NIL) break;

            if (parent == grandParent.left) {
                RBTNode uncle = grandParent.right;

                if (uncle != NIL && uncle.isRed) {
                    grandParent.isRed = true;
                    parent.isRed = false;
                    uncle.isRed = false;
                    node = grandParent;
                } else {
                    if (node == parent.right) {
                        node = parent;
                        rotateLeft(node);
                    }
                    parent.isRed = false;
                    grandParent.isRed = true;
                    rotateRight(grandParent);
                }
            } else {
                RBTNode uncle = grandParent.left;

                if (uncle != NIL && uncle.isRed) {
                    grandParent.isRed = true;
                    parent.isRed = false;
                    uncle.isRed = false;
                    node = grandParent;
                } else {
                    if (node == parent.left) {
                        node = parent;
                        rotateRight(node);
                    }
                    parent.isRed = false;
                    grandParent.isRed = true;
                    rotateLeft(grandParent);
                }
            }
        }
        root.isRed = false;
    }

    public RBTNode search(char key){
        return searchRec(root, key);
      }
    
      private RBTNode searchRec(RBTNode node, char key){
        if (node == null || node.key == key) {
          return node;
        }
    
        if (key < node.key) {
          return searchRec(node.left, key);
        }
        else{
          return searchRec(node.right, key);
        }
      }

    public void inOrder(RBTNode node) {
        if (node != null && node != NIL) {
            inOrder(node.left);
            System.out.print(node.key + " ");
            inOrder(node.right);
        }
    }

    public void preorder(RBTNode node) {
        if (node != null && node != NIL) {
            System.out.print(node.key + " ");
            preorder(node.left);
            preorder(node.right);
        }
    }

    public void postorder(RBTNode node) {
        if (node != null && node != NIL) {
            postorder(node.left);
            postorder(node.right);
            System.out.print(node.key + " ");
        }
    }

    public void printTree() {
        printTree(root, 0);
    }

    private void printTree(RBTNode node, int level) {
        if (node != NIL) {
            printTree(node.getRight(), level + 1);

            String color = node.isRed ? "[R]" : "[B]";
            System.out.printf("%" + (level * 4 + 2) + "s%n", node.getKey() + color);
            printTree(node.getLeft(), level + 1);
        }
    }

    public void delete(char key) {
        RBTNode nodeToDelete = searchRec(root, key);
        if (nodeToDelete == NIL) {
            System.out.println("Node not found");
            return;
        }

        RBTNode y = nodeToDelete;
        RBTNode x;
        boolean originalColor = y.isRed;

        if (nodeToDelete.left == NIL) {
            x = nodeToDelete.right;
            RBTransplant(nodeToDelete, nodeToDelete.right);
        } else if (nodeToDelete.right == NIL) {
            x = nodeToDelete.left;
            RBTransplant(nodeToDelete, nodeToDelete.left);
        } else {
            y = minimum(nodeToDelete.right);
            originalColor = y.isRed;
            x = y.right;

            if (y.parent == nodeToDelete) {
                x.parent = y;
            } else {
                RBTransplant(y, y.right);
                y.right = nodeToDelete.right;
                y.right.parent = y;
            }

            RBTransplant(nodeToDelete, y);
            y.left = nodeToDelete.left;
            y.left.parent = y;
            y.isRed = nodeToDelete.isRed;
        }

        if (!originalColor) {
            fixUpDelete(x);
        }
    }

    private void RBTransplant(RBTNode u, RBTNode v) {
        if (u.parent == NIL) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        v.parent = u.parent;
    }

    private void fixUpDelete(RBTNode x) {
        while (x != root && !x.isRed) {
            if (x == x.parent.left) {
                RBTNode w = x.parent.right;
                if (w.isRed) {
                    w.isRed = false;
                    x.parent.isRed = true;
                    rotateLeft(x.parent);
                    w = x.parent.right;
                }
                if (!w.left.isRed && !w.right.isRed) {
                    w.isRed = true;
                    x = x.parent;
                } else {
                    if (!w.right.isRed) {
                        w.left.isRed = false;
                        w.isRed = true;
                        rotateRight(w);
                        w = x.parent.right;
                    }
                    w.isRed = x.parent.isRed;
                    x.parent.isRed = false;
                    w.right.isRed = false;
                    rotateLeft(x.parent);
                    x = root;
                }
            } else {
                RBTNode w = x.parent.left;
                if (w.isRed) {
                    w.isRed = false;
                    x.parent.isRed = true;
                    rotateRight(x.parent);
                    w = x.parent.left;
                }
                if (!w.right.isRed && !w.left.isRed) {
                    w.isRed = true;
                    x = x.parent;
                } else {
                    if (!w.left.isRed) {
                        w.right.isRed = false;
                        w.isRed = true;
                        rotateLeft(w);
                        w = x.parent.left;
                    }
                    w.isRed = x.parent.isRed;
                    x.parent.isRed = false;
                    w.left.isRed = false;
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        x.isRed = false;
    }

    private RBTNode minimum (RBTNode node) {
        while (node.left != NIL) {
            node = node.left;
        }
        return node;
    }
}

public class RedBlackTree {
    public static void main(String[] args) {
        RBT rbt = new RBT();
        char[] keys = {'A', 'K', 'Z', 'A'};

        System.out.println();
        System.out.println("Inserting Values To Red-Black-Tree");
        for (char key : keys) {
            System.out.print(key + " ");
            rbt.insert(key);
        }
        System.out.println();


        System.out.println("");
        
        char searchKey = 'A';
        RBTNode foundNode = rbt.search(searchKey);
        if (foundNode != null) {
          System.out.println(searchKey + "' ditemukan dalam tree.");
        } else {
          System.out.println(searchKey + "' tidak ditemukan dalam tree.");
        }
    
        System.out.println("\nPre-order Traversal: ");
        rbt.preorder(rbt.root);
        System.out.println("\nIn-Order Traversal: ");
        rbt.inOrder(rbt.root);
        System.out.println("\nPost-Order Traversal: ");
        rbt.postorder(rbt.root);

        System.out.println("\nRed Black Tree Structure: ");
        rbt.printTree();

        System.out.println("");

        rbt.delete('A');
        rbt.printTree();
        System.out.println();
    }
}
