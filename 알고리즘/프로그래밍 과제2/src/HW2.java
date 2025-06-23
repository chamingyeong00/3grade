import java.io.File;
import java.io.IOException;
import java.util.*;

class Node<K, V> {
    K key;
    V value;
    int N;
    Node<K, V> left, right, parent;

    public Node(K key, V val) {
        this.key = key;
        this.value = val;
        this.N = 1;
    }
}

class BST<K extends Comparable<K>, V> {
    protected Node<K, V> root;

    public int size() {
        return (root != null) ? root.N : 0;
    }

    protected Node<K, V> treeSearch(K key) {
        Node<K, V> x = root;
        while (true) {
            int cmp = key.compareTo(x.key);
            if (cmp == 0) return x;
            else if (cmp < 0) {
                if (x.left == null) return x;
                else x = x.left;
            } else {
                if (x.right == null) return x;
                else x = x.right;
            }
        }
    }

    public V get(K key) {
        if (root == null) return null;
        Node<K, V> x = treeSearch(key);
        if (key.equals(x.key))
            return x.value;
        else
            return null;
    }

    public void put(K key, V val) {
        if (root == null) {
            root = new Node<>(key, val);
            return;
        }
        Node<K, V> x = treeSearch(key);
        int cmp = key.compareTo(x.key);
        if (cmp == 0) x.value = val;
        else {
            Node<K, V> newNode = new Node<>(key, val);
            if (cmp < 0) x.left = newNode;
            else x.right = newNode;
            newNode.parent = x;
            rebalanceInsert(newNode);
        }
    }

    protected void rebalanceInsert(Node<K, V> x) {
        resetSize(x.parent, 1);
    }

    private void resetSize(Node<K, V> x, int value) {
        for (; x != null; x = x.parent)
            x.N += value;
    }

    public Iterable<K> keys() {
        if (root == null) return null;
        ArrayList<K> keyList = new ArrayList<K>(size());
        inorder(root, keyList);
        return keyList;
    }

    private void inorder(Node<K, V> x, ArrayList<K> keyList) {
        if (x != null) {
            inorder(x.left, keyList);
            keyList.add(x.key);
            inorder(x.right, keyList);
        }
    }
}

class HW2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in, "UTF-8");
        BST<String, Integer> bfs1 = null;
        BST<String, Integer> bfs2 = null;
        int cnt1 = 0;
        int cnt2 = 0;
        int intersection = 0;
        int union = 0;
        int frequency1;
        int frequency2;
        double similarity;

        try {
            System.out.print("첫번째 파일 이름? ");
            String file1 = sc.nextLine();
            bfs1 = processFile(file1);

            System.out.print("두번째 파일 이름? ");
            String file2 = sc.nextLine();
            bfs2 = processFile(file2);

            for (String key : bfs1.keys()) {
                cnt1 += bfs1.get(key);
            }
            for (String key : bfs2.keys()) {
                cnt2 += bfs2.get(key);
            }

            System.out.println("파일 " + file1 + "의 Shingle의 수 = " + cnt1);
            System.out.println("파일 " + file2 + "의 Shingle의 수 = " + cnt2);

            for (String key : bfs1.keys()) {
                frequency1 = bfs1.get(key);
                if (bfs2.get(key) != null) {
                    frequency2 = bfs2.get(key);
                } else {
                    frequency2 = 0;
                }

                intersection += Math.min(frequency1, frequency2);
                union += Math.max(frequency1, frequency2);
            }

            for (String key : bfs2.keys()) {
                if (bfs1.get(key) == null) {
                    union += bfs2.get(key);
                }
            }

            System.out.println("두 파일에서 공통된 shingle의 수 = " + intersection);
            if (union == 0) {
                similarity = 0.0;
            } else {
                similarity = (double) intersection / union;
            }
            System.out.println(file1 + "과 " + file2 + "의 유사도 = " + similarity);
        } catch (IOException e) {
            System.out.println("error" + e);
        }
        sc.close();
    }

    public static BST<String, Integer> processFile(String fname) throws IOException {
        BST<String, Integer> bst = new BST<>();
        Scanner sc = new Scanner(new File(fname), "UTF-8");
        String str = "";
        while (sc.hasNextLine()) {
            str += sc.nextLine() + " ";
        }
        sc.close();

        StringTokenize;r st = new StringTokenizer(str, " \t\n=;,<>()");
        ArrayList<String> words = new ArrayList<>();
        while (st.hasMoreTokens()) {
            words.add(st.nextToken());
        }

        for (int i = 0; i <= words.size() - 5; i++) {
            String shingle = "";
            for (int j = 0; j < 5; j++) {
                shingle += words.get(i + j);
                if (j <= 3) {
                    shingle += " ";
                }
            }

            Integer frequency = bst.get(shingle);
            if (frequency == null)
                bst.put(shingle, 1);
            else
                bst.put(shingle, frequency + 1);
        }
        return bst;
    }
}
