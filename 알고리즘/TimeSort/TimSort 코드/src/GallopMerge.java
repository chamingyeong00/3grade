package src;

import java.util.List;

public class GallopMerge {

    public static int gallopRight(Comparable key, Comparable[] array, int base, int lenOfA) {
        int lo = 0;
        int hi = 1;

        if (base >= array.length) {
            return 0;
        }
        if (base + lenOfA > array.length) {
            lenOfA = array.length - base;
        }

        if (key.compareTo(array[base]) < 0) {
            return 0;
        }
        else{
            int maxLen = lenOfA;
            while (hi < maxLen && array[base + hi].compareTo(key) <= 0) {
                lo = hi;
                hi = (hi << 1) + 1;
                if (hi <= 0) {
                    hi = maxLen;
                }
            }
            if (hi > maxLen) {
                hi = maxLen;
            }
            lo++;
            while (lo < hi) {
                int mid = lo + ((hi - lo) >>> 1);
                if (key.compareTo(array[base + mid]) < 0) {
                    hi = mid;
                } else {
                    lo = mid + 1;
                }
            }
            return hi;
        }
    }


    public static int gallopLeft(Comparable key, Comparable[] array, int base, int lenOfB) {
        int lo = 0;
        int hi = 1;

        if (base + lenOfB - 1 >= array.length) {
            lenOfB = array.length - base;
        }

        if (key.compareTo(array[base + lenOfB - 1]) > 0) {
            return lenOfB;
        }
        else{
            int startPointOfRun = base + lenOfB - 1;
            int maxLen = lenOfB;
            while (hi < maxLen && key.compareTo(array[startPointOfRun - hi]) <= 0) {
                lo = hi;
                hi = (hi << 1) + 1;
                if (hi <= 0) {
                    hi = maxLen;
                }
            }
            if (hi > maxLen) {
                hi = maxLen;
            }
            int temp = lo;
            lo = lenOfB - 1 - hi;
            hi = lenOfB - 1 - temp;
            lo++;
            while (lo < hi) {
                int mid = lo + ((hi - lo) >>> 1);
                if (key.compareTo(array[base + mid]) < 0) {
                    hi = mid;
                } else {
                    lo = mid + 1;
                }
            }
            return hi;
        }
    }

    public static void mergeLo(Comparable[] a, int start1, int length1, int start2, int length2) {

        Comparable[] temp = new Comparable[length1];
        System.arraycopy(a, start1, temp, 0, length1);

        int insertIdx = start1;
        int runBIdx = start2;
        int tempIdx = 0;
        int leftRemain = length1;
        int rightRemain = length2;

        while (leftRemain != 0 && rightRemain != 0) {
            if (a[runBIdx].compareTo(temp[tempIdx]) < 0) {
                a[insertIdx++] = a[runBIdx++];
                rightRemain--;
            } else {
                a[insertIdx++] = temp[tempIdx++];
                leftRemain--;
            }
        }
        if (leftRemain != 0) {
            System.arraycopy(temp, tempIdx, a, insertIdx, leftRemain);
        }
        else {
            System.arraycopy(a, runBIdx, a, insertIdx, rightRemain);
        }
    }

    public static void mergeHi(Comparable[] a, int start1, int length1, int start2, int length2) {
        Comparable[] temp = new Comparable[length2];
        System.arraycopy(a, start2, temp, 0, length2);

        int insertIdx = start2 + length2 - 1;
        int runAIdx = start1 + length1 - 1;
        int tempIdx = length2 - 1;
        int leftRemain = length1;
        int rightRemain = length2;

        while (leftRemain != 0 && rightRemain != 0) {
            if (a[runAIdx].compareTo(temp[tempIdx]) > 0) {
                a[insertIdx--] = a[runAIdx--];
                leftRemain--;
            } else {
                a[insertIdx--] = temp[tempIdx--];
                rightRemain--;
            }
        }
        if (rightRemain != 0) {
            System.arraycopy(temp, 0, a, start1, rightRemain);
        } else {
            System.arraycopy(a, start1, a, start1, leftRemain);
        }
    }

    public static void mergeRuns(Comparable[] a, List<int[]> runStack) {
        while (runStack.size() > 1) {
            int[] X = runStack.get(runStack.size() - 1);
            int[] Y = runStack.get(runStack.size() - 2);

            merge(a, Y[0], Y[1], X[0], X[1]);

            runStack.remove(runStack.size() - 1);
            runStack.remove(runStack.size() - 1);
            runStack.add(new int[]{Y[0], X[1]});
        }
    }

    public static void merge(Comparable[] a, int start1, int length1, int start2, int length2) {
        int lo = gallopRight(a[start2], a, start1, length1);

        if (length1 == lo) {
            return;
        }
        start1 += lo;
        length1 -= lo;

        if (start1 + length1 - 1 >= a.length) {
            return;
        }
        int hi = gallopLeft(a[start1 + length1 - 1], a, start2, length2);

        if (hi == 0) {
            return;
        }
        length2 = hi;
        if (length1 <= length2) {
            mergeLo(a, start1, length1, start2, length2);
        } else {
            mergeHi(a, start1, length1, start2, length2);
        }
    }
}
