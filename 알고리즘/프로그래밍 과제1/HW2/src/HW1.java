// 22312072 차민경
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HW1 {
    static double start_x;
    static double start_y;
    static int k;
    static int length;
    static PointDistance[] points;

    static class PointDistance {
        double x, y, distance;

        public PointDistance(double x, double y, double distance) {
            this.x = x;
            this.y = y;
            this.distance = distance;
        }
    }

    static class makeQueue {
        private List<PointDistance> heap;
        private int k;

        public makeQueue(int k) {
            this.heap = new ArrayList<>();
            this.k = k;
        }

        public void add(PointDistance point) {
            heap.add(point);
            makeNode(heap.size() - 1);
        }

        private void makeNode(int index) {
            PointDistance point = heap.get(index);

            while (index > 0) {
                int parentIdx = (index - 1) / 2;
                PointDistance parent = heap.get(parentIdx);

                if (point.distance >= parent.distance) break;

                heap.set(index, parent);
                heap.set(parentIdx, point);
                index = parentIdx;
            }
        }

        public int size() {
            return heap.size();
        }

        public List<PointDistance> getnearPoints() {
            return heap;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        init();
        findnearHeap();
    }

    private static void findnearHeap() {
        long start = System.currentTimeMillis();
        makeQueue maxHeap = new makeQueue(length);
        for (PointDistance point : points) {
            maxHeap.add(point);
        }
        long end = System.currentTimeMillis();
        System.out.println("k = "+ k + "일 때의 실행시간 = " + (end - start) + "ms");
        List<PointDistance> closest = maxHeap.getnearPoints();
        for (int i = 0; i < k; i++) {
            PointDistance p = closest.get(i);
            System.out.println(i + ": (" + p.x + ", " + p.y + ")    거리 = " + p.distance);
        }
    }

    private static void init() {
        Scanner sc = new Scanner(System.in);
        System.out.println("입력 파일 이름? ");
        String fname = sc.nextLine();

        try {
            sc = new Scanner(new File(fname));
            start_x = sc.nextDouble();
            start_y = sc.nextDouble();
            k = sc.nextInt();
            length = sc.nextInt();
            if (k == -1) {
                k = length;
            }
            points = new PointDistance[length];

            for (int i = 0; i < length; i++) {
                if (sc.hasNextDouble()) {
                    double x = sc.nextDouble();
                    double y = sc.nextDouble();
                    double distance = Math.sqrt(Math.pow(x - start_x, 2) + Math.pow(y - start_y, 2));
                    points[i] = new PointDistance(x, y, distance);
                }
                else {
                    System.out.println("오류 발생 : k가 주요소 좌표의 개수보다 큼 ");
                    return;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("오류 발생 : 파일을 찾을 수 없음 ");
        }
        if (sc != null) sc.close();
    }
}