package src;

public class Main {
    public static void main(String[] args) {
        Integer[] a = {
                5, 3, 2, 8, 10, 15, 4, 7, 6, 1, 9
        };

        TimSort.timSort(a);
        TimSort.show(a);
    }
}
