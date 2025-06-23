//22312072 차민경

class Solution {
    public int solution(int[][] triangle) {
        int n = triangle.length;
        for (int floor = n - 1; floor > 0; floor--) {
            for (int i = 0; i < floor; i++) {
                triangle[floor - 1][i] += Math.max(triangle[floor][i], triangle[floor][i + 1]);
            }
        }

        return triangle[0][0];
    }
}