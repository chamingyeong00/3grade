//22312072 차민경

import java.util.*;

public class HW2 {

    static List<List<Integer>> result = new ArrayList<>();
    static List<Integer> current = new ArrayList<>();

    public static void combine(int start, int n, int k) {
        if (current.size() == k) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = start; i <= n; i++) {
            current.add(i);
            combine(i + 1, n, k);
            current.remove(current.size() - 1);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("입력: 정수 n과 k를 입력? ");
        int n = sc.nextInt();
        int k = sc.nextInt();

        combine(1, n, k);

        System.out.print("출력: ");
        for (List<Integer> subset : result) {
            System.out.print(subset + " ");
        }
    }
}