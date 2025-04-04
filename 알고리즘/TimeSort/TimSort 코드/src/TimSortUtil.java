package src;

import java.util.*;

import static src.AbstractSort.*;

public class TimSortUtil {
    public static int minRunLength(int n) {
        int r = 0;
        while (n >= 4) {
            r |= (n & 1);
            n >>= 1;
        }
        return n + r;
    }

    public static int getRun(Comparable[] a, int start) {
        if (start >= a.length - 1)
            return a.length;

        boolean isDescending = less(a[start + 1], a[start]);
        int prev = start, curr = start + 1;

        while (curr < a.length - 1 && less(a[curr + 1], a[curr]) == isDescending) {
            prev = curr;
            curr++;
        }
        if (isDescending) {
            reverse(a, start, curr);
        }
        return curr + 1;
    }

    public static void reverse(Comparable[] a, int start, int end) {
        while (start < end) {
            exch(a, start++, end--);
        }
    }


    public static void mergeAll(Comparable[] a, List<int[]> runStack) {
        while (runStack.size() > 1) {
            int[] X = runStack.remove(runStack.size() - 1);
            int[] Y = runStack.remove(runStack.size() - 1);
            Merge.merge(a, Y[0], X[0] - 1, X[1] - 1);
            runStack.add(new int[]{Y[0], X[1]});
        }
    }
}