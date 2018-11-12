package main;

import main.bqs.Queue_Node;

import java.util.*;
import java.util.function.BiFunction;

public class BSTSimple<Key extends Comparable<Key>, Value> implements BSTdetail<Key, Value> {
    @Override
    public Boolean contains(Key key) {
        return get(key) != null;
    }

    @Override
    public void putAll(Map<Key, Value> map) {
        // CONSIDER optionally randomize the input
        for (Map.Entry<Key, Value> entry : map.entrySet()) put(entry.getKey(), entry.getValue());
    }

    @Override
    public int size() {
        return root != null ? root.count : 0;
    }

    @Override
    public void inOrderTraverse(BiFunction<Key, Value, Void> f) {
        doTraverse(0, root, f);
    }

    @Override
    public Value get(Key key) {
        return get(root, key);
    }

    @Override
    public Value put(Key key, Value value) {
        NodeValue nodeValue = put(root, key, value);
        if (root == null) root = nodeValue.node;
        if (nodeValue.value==null) root.count++;
        return nodeValue.value;
    }
    @Override
    public void delete(Key key) {
        root = delete(root, key);
    }

    @Override
    public void deleteMin() {
        root = deleteMin(root);
    }

    @Override
    public Set<Key> keySet() {
        return null;
    }

    public BSTSimple() {
    }

    public BSTSimple(Map<Key, Value> map) {
        this();
        putAll(map);
    }

    Node root = null;



    private Value get(Node node, Key key) {
        if (node == null) return null;
        int cf = key.compareTo(node.key);
        if (cf < 0) return get(node.smaller, key);
        else if (cf > 0) return get(node.larger, key);
        else return node.value;
    }

    /**
     * Method to put the key/value pair into the subtree whose root is node.
     *
     * @param node  the root of a subtree
     * @param key   the key to insert
     * @param value the value to associate with the key
     * @return a tuple of Node and Value: Node is the
     */
    private NodeValue put(Node node, Key key, Value value) {
        // If node is null, then we return the newly constructed Node, and value=null
        if (node == null) return new NodeValue(new Node(key, value), null);
        int cf = key.compareTo(node.key);
        if (cf == 0) {
            // If keys match, then we return the node and its value
            NodeValue result = new NodeValue(node, node.value);
            node.value = value;
            return result;
        } else if (cf < 0) {
            // if key is less than node's key, we recursively invoke put in the smaller subtree
            NodeValue result = put(node.smaller, key, value);
            if (node.smaller == null)
                node.smaller = result.node;
            if (result.value==null)
                result.node.count++;
            return result;
        } else {
            // if key is greater than node's key, we recursively invoke put in the larger subtree
            NodeValue result = put(node.larger, key, value);
            if (node.larger == null)
                node.larger = result.node;
            if (result.value==null)
                result.node.count++;
            return result;
        }
    }

    private Node delete(Node x, Key key) {
        // TO IMPLEMENT
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.smaller = delete(x.smaller, key);
        else if (cmp > 0) x.larger = delete(x.larger, key);
        else {
            if (x.larger == null) return x.smaller;
            if (x.smaller == null) return x.larger;

            Node t = x;
            x = min(t.larger);
            x.larger = deleteMin(t.larger);
            x.smaller = t.smaller;
        }
        x.count = size(x.smaller) + size(x.larger) + 1;
        return x;
    }

    private Node deleteMin(Node x) {
        if (x.smaller == null) return x.larger;
        x.smaller = deleteMin(x.smaller);
        x.count = 1 + size(x.smaller) + size(x.larger);
        return x;
    }

    private int size(Node x) {
        if(x==null){
            return 0;
        }else{
            return x.count;
        }
    }

    private Node min(Node x) {
        if (x == null) throw new RuntimeException("min not implemented for null");
        else if (x.smaller == null) return x;
        else return min(x.smaller);
    }

    /**
     * Do a generic traverse of the binary tree starting with node
     * @param q determines when the function f is invoked ( lt 0: pre, ==0: in, gt 0: post)
     * @param node the node
     * @param f the function to be invoked
     */
    private void doTraverse(int q, Node node, BiFunction<Key, Value, Void> f) {
        if (node == null) return;
        if (q<0) f.apply(node.key, node.value);
        doTraverse(q, node.smaller, f);
        if (q==0) f.apply(node.key, node.value);
        doTraverse(q, node.larger, f);
        if (q>0) f.apply(node.key, node.value);
    }

    private class NodeValue {
        private final Node node;
        private final Value value;

        NodeValue(Node node, Value value) {
            this.node = node;
            this.value = value;
        }

        @Override
        public String toString() {
            return node + "<->" + value;
        }
    }

    class Node {
        Node(Key key, Value value) {
            this.key = key;
            this.value = value;
        }

        final Key key;
        Value value;
        Node smaller = null;
        Node larger = null;
        int count = 0;
        int depth;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("Node: " + key + ":" + value);
            if (smaller != null) sb.append(", smaller: " + smaller.key);
            if (larger != null) sb.append(", larger: " + larger.key);
            return sb.toString();
        }

    }

    private Node makeNode(Key key, Value value) {
        return new Node(key, value);
    }

    private Node getRoot() {
        return root;
    }

    private void setRoot(Node node) {
        if(root==null){
            root = node;
            root.count++;
        }else
            root = node;
    }
    private void show(Node node, StringBuffer sb, int indent) {
        if (node == null) return;
        for (int i = 0; i < indent; i++) sb.append("  ");
        sb.append(node.key);
        sb.append(": ");
        sb.append(node.value);
        sb.append("\n");
        if (node.smaller != null) {
            for (int i = 0; i <= indent; i++) sb.append("  ");
            sb.append("smaller: ");
            show(node.smaller, sb, indent + 1);
        }
        if (node.larger != null) {
            for (int i = 0; i <= indent; i++) sb.append("  ");
            sb.append("larger: ");
            show(node.larger, sb, indent + 1);
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        show(root, sb, 0);
        return sb.toString();
    }

    /**
     * Calculate height of the binary tree
     */
    public int height() {
        return height(root);
    }
    private int height(Node x) {
        if (x == null) return 0;
        return 1 + Math.max(height(x.smaller), height(x.larger));
    }
    /**
     * Calculate average Internal Path Length
     */
    private int averageInternalPathLength(){

        if (root == null) {
            return 0;
        }

        int sum = 0;
        int size = 0;

        Queue_Node<Node> queue = new Queue_Node<>();
        root.depth = 0;
        queue.enqueue(root);

        while (!queue.isEmpty()) {
            Node current = queue.dequeue();

            if(current.smaller == null && current.larger == null){
                sum += current.depth;
                size += 1;
            }
            if (current.smaller != null) {
                current.smaller.depth = current.depth + 1;
                queue.enqueue(current.smaller);
            }
            if (current.larger != null) {
                current.larger.depth = current.depth + 1;
                queue.enqueue(current.larger);
            }

        }

        return (sum/size);
    }
    /**
     * Select method
     */
    public Key select(int k) {
        if (k < 0 || k >= size()) {
            throw new IllegalArgumentException("argument to select() is invalid: " + k);
        }
        Node x = select(root, k);
        return x.key;
    }

    // Return key of rank k.
    private Node select(Node x, int k) {
        if (x == null) return null;
        int t = size(x.smaller);
        if      (t > k) return select(x.smaller,  k);
        else if (t < k) return select(x.larger, k-t-1);
        else            return x;
    }
//    public int calculateHeight(int N){
//        Random random = new Random();
//        int totalHeight = 0;
//        int nextNum = random.nextInt(N);
//        for(int i = 0; i < N; i++){
//            System.out.println(select(nextNum));
//            delete(select(nextNum));
//            put(select(nextNum),new Integer(i));
//            totalHeight +=height();
//        }
//        return totalHeight/N+1;
//    }

    public static double calculateAveragePathLength(int N){
        BSTSimple<Integer,Integer> bst= new BSTSimple<Integer,Integer>();
        Random random = new Random();
        while(bst.size()!= N){
            bst.put(random.nextInt(2*N),random.nextInt(2*N));
        }
        for(int i = 0; i< N*N; i++){
            int temp = (int)(Math.random()*N*2);
            bst.delete(temp);//delete
            bst.put(temp,temp);//insert
        }

        return bst.averageInternalPathLength();
    }

    public static int calculateMaxPathLength(int N){
        BSTSimple<Integer,Integer> bst= new BSTSimple<Integer,Integer>();
        Random random = new Random();
        while(bst.size()!= N){
            bst.put(random.nextInt(2*N),random.nextInt(2*N));
        }
        int height = bst.height(bst.getRoot());

        for(int i = 1; i<N*N;i++){
            int temp = (int)(Math.random()*N*2);
            bst.delete(temp); //delete
            bst.put(temp,temp); //insert
        }
        return bst.height(bst.getRoot());

    }

    public static double log2(int n) {
        return (Math.log(n) / Math.log(2));
    }

    public static void main(String[] args) {
        for(int N = 100; N<=5000; N+=100) {
            int maxPathHeight = calculateMaxPathLength(N);
            double square = Math.sqrt(N);
            double log2 = log2(N);
            double averageHeight = calculateAveragePathLength(N);

            System.out.println("N= "+N + "  Max Height= " + maxPathHeight + "  Average Height= " + averageHeight + " " +
                    "log2N= " + log2 + "  sqrtN= " + square);
        }
    }

}
