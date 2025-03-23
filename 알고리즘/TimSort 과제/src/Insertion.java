package src;

import static src.AbstractSort.*;

public class Insertion {
    public static void sort(Comparable[] a, int start, int end) {
        int N = a.length;
        for (int i = start; i < end; i++){
            for (int j = i; j > 0 && less(a[j], a[j-1]); j--){
                exch(a, j, j-1);
            }
        }
        assert isSorted(a);
    }
}

