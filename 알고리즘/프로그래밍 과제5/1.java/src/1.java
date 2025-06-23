//22311978 김예원

import java.util.*;

class Solution {
    public String solution(String number, int k) {
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < number.length(); i++) {
            char now = number.charAt(i);

            while (!stack.isEmpty() && k > 0 && stack.peek() < now) {
                stack.pop();
                k--;
            }
            stack.push(now);
        }

        while (k > 0) {
            stack.pop();
            k--;
        }

        StringBuilder answer = new StringBuilder();
        for (char c : stack) {
            answer.append(c);
        }
        return answer.toString();
    }
}