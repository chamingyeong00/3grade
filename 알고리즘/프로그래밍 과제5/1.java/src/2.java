//22311978 김예원

import java.util.Arrays;

class Solution {
    public int solution(int[][] routes) {
        int cameras = 0;
        Arrays.sort(routes, (a, b) -> a[1] - b[1]);
        int lastCamera = -30001;

        for (int i = 0; i < routes.length; i++) {
            int start = routes[i][0];
            int end = routes[i][1];

            if (start <= lastCamera) {
                continue;
            }

            cameras++;
            lastCamera = end;
        }

        return cameras;
    }
}