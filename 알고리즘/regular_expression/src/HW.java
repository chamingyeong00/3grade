//22312072 차민경

class Solution {
    public int solution(String dartResult) {
        // 라운드당 점수를 담을 배열
        int[] score = new int[3];
        // toCharArray() : String -> char 배열
        char[] arr = dartResult.toCharArray();
        // score 인덱스
        int idx = -1;

        for (int i = 0; i < arr.length; i++) {
            // 숫자인 경우
            if (arr[i] == '1' && arr[i + 1] == '0') {
                idx++;
                // '10'의 경우 10점으로 처리하여 score 배열에 저장
                score[idx] = 10;
                i++; // 다음 문자도 처리했으므로 인덱스 증가
                continue;
            } else if (arr[i] >= '0' && arr[i] <= '9') {
                idx++;
                // 숫자인 경우 해당 값을 정수로 변환하여 score 배열에 저장
                score[idx] = Integer.parseInt(String.valueOf(arr[i]));
                continue;
            }

            // 문자인 경우
            switch (arr[i]) {
                case 'D':
                    // 'D'는 해당 라운드의 점수를 2제곱하여 score 배열에 저장
                    score[idx] = (int) Math.pow(score[idx], 2);
                    break;
                case 'T':
                    // 'T'는 해당 라운드의 점수를 3제곱하여 score 배열에 저장
                    score[idx] = (int) Math.pow(score[idx], 3);
                    break;
                case '*':
                    // '*'는 해당 라운드의 점수를 2배로 만들고, 이전 라운드의 점수도 2배로 만듦
                    score[idx] *= 2;
                    // 첫 번째 라운드인 경우에는 이전 라운드가 없으므로 조건 판단
                    if (idx - 1 >= 0) {
                        score[idx - 1] *= 2;
                    }
                    break;
                case '#':
                    // '#'는 해당 라운드의 점수를 음수로 만듦
                    score[idx] *= -1;
            }
        }

        // 각 라운드의 점수를 모두 합하여 총 점수를 반환
        return score[0] + score[1] + score[2];
    }
}
출처: https://ittrue.tistory.com/473 [IT is True:티스토리]