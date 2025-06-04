import java.io.*;
import java.util.*;

public class HW2 {

    // Binary Search Tree Node
    static class TreeNode {
        String key;
        int count;
        TreeNode left, right;

        TreeNode(String key) {
            this.key = key;
            this.count = 1;
            this.left = this.right = null;
        }
    }

    // Binary Search Tree
    static class BST {
        private TreeNode root;

        public BST() {
            this.root = null;
        }

        // Insert a key into the BST
        public void insert(String key) {
            root = insertRec(root, key);
        }

        // Helper function to insert recursively
        private TreeNode insertRec(TreeNode root, String key) {
            if (root == null) {
                return new TreeNode(key);
            }

            int cmp = key.compareTo(root.key);
            if (cmp < 0) {
                root.left = insertRec(root.left, key);
            } else if (cmp > 0) {
                root.right = insertRec(root.right, key);
            } else {
                root.count++;
            }

            return root;
        }

        // Find the intersection of two BSTs and return the count of common keys
        public int intersectionCount(BST other) {
            return intersectionCountRec(root, other.root);
        }

        // Helper function to find intersection recursively
        private int intersectionCountRec(TreeNode node1, TreeNode node2) {
            if (node1 == null || node2 == null) return 0;

            int count = 0;

            if (node1.key.equals(node2.key)) {
                count += Math.min(node1.count, node2.count);
                count += intersectionCountRec(node1.left, node2.left);
                count += intersectionCountRec(node1.right, node2.right);
            } else if (node1.key.compareTo(node2.key) < 0) {
                count += intersectionCountRec(node1.right, node2);
            } else {
                count += intersectionCountRec(node1, node2.right);
            }

            return count;
        }

        // Count the number of unique keys in the BST
        public int size() {
            return sizeRec(root);
        }

        // Helper function to count the number of unique keys
        private int sizeRec(TreeNode root) {
            if (root == null) return 0;
            return 1 + sizeRec(root.left) + sizeRec(root.right);
        }
    }

    // Tokenize a string and return the shingles
    public static List<String> getShingles(String text) {
        StringTokenizer tokenizer = new StringTokenizer(text, " \t\n=;,<>()");
        List<String> shingles = new ArrayList<>();
        Queue<String> window = new LinkedList<>();

        while (tokenizer.hasMoreTokens()) {
            window.add(tokenizer.nextToken());
            if (window.size() > 5) {
                window.poll();
            }
            if (window.size() == 5) {
                shingles.add(String.join(" ", window));
            }
        }

        return shingles;
    }

    // Read a file and return the shingles of the file
    public static List<String> readFileAndExtractShingles(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        StringBuilder content = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            content.append(line).append(" ");
        }

        return getShingles(content.toString());
    }

    // Calculate the similarity between two BSTs
    public static double calculateSimilarity(BST bst1, BST bst2) {
        int intersection = bst1.intersectionCount(bst2);
        int union = bst1.size() + bst2.size() - intersection;
        return (double) intersection / union;
    }

    public static void main(String[] args) throws IOException {
        // 파일 이름을 입력받고, 각각의 파일을 처리
        String file1 = "h.java";
        String file2 = "t.java";

        List<String> shingles1 = readFileAndExtractShingles(file1);
        List<String> shingles2 = readFileAndExtractShingles(file2);

        BST bst1 = new BST();
        BST bst2 = new BST();

        // 파일1의 shingles를 BST에 삽입
        for (String shingle : shingles1) {
            bst1.insert(shingle);
        }

        // 파일2의 shingles를 BST에 삽입
        for (String shingle : shingles2) {
            bst2.insert(shingle);
        }

        // 유사도 계산
        double similarity = calculateSimilarity(bst1, bst2);

        // 결과 출력
        System.out.println("파일 " + file1 + "의 Shingle의 수 = " + shingles1.size());
        System.out.println("파일 " + file2 + "의 Shingle의 수 = " + shingles2.size());
        System.out.println("두 파일에서 공통된 shingle의 수 = " + bst1.intersectionCount(bst2));
        System.out.println(file1 + "과 " + file2 + "의 유사도 = " + similarity);
    }
}