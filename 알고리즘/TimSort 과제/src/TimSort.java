package src;

import java.util.ArrayList;
import java.util.List;

import static src.GallopMerge.mergeRuns;
import static src.TimSortUtil.*;

public class TimSort extends AbstractSort {
    public static void timSort(Comparable[] a) {
        if (a.length <= 1) return;

        int minRun = minRunLength(a.length);
        List<int[]> runStack = new ArrayList<>();
        Comparable[] aux = new Comparable[a.length];

        int start = 0;
        while (start < a.length) {
            int end = getRun(a, start);
            if (end - start < minRun) {
                int newEnd = Math.min(a.length, start + minRun);
                Insertion.sort(a, start, newEnd);
                end = newEnd;
            }
            runStack.add(new int[]{start, end});
            mergeRuns(a, runStack);
            start = end;
        }
        mergeAll(a, runStack);
    }
}
