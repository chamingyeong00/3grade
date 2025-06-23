//22312072 차민경

class Solution {
    public boolean zip(int[][] arr, int x, int y, int size, int val) {
        for (int i = x; i < x + size; i++)
            for (int j = y; j < y + size; j++)
                if (arr[i][j] != val)
                    return false;
        return true;
    }
    public void quad(int[][] arr, int x, int y, int size, int[] answer){
        if (zip(arr, x, y, size, arr[x][y])){
            if(arr[x][y] == 1)
                answer[1]++;
            else
                answer[0]++;
            return ;
        }
        int half = size / 2;
        quad(arr, x, y, half, answer);
        quad(arr, x, y + half, half, answer);
        quad(arr, x + half, y, half, answer);
        quad(arr, x + half, y + half, half, answer);
    }
    public int[] solution(int[][] arr) {
        int[] answer = new int[2];
        quad(arr, 0, 0, arr.length, answer);
        return answer;
    }
}

