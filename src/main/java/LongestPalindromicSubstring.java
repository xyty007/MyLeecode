package main.java;

/**
 * Given a string s, find the longest palindromic substring in s. You may assume that the maximum length of s is 1000.
 * <p>
 * Example:
 * Input: "babad"
 * Output: "bab"
 * Note: "aba" is also a valid answer.
 * Example:
 * Input: "cbbd"
 * Output: "bb"
 */

public class LongestPalindromicSubstring {

    //solution 1
    public String longestPalindrome(String s) {

        int head = 0;
        int tail = 0;

        for (int i = 0; i < s.length(); i++) {
            int j;
            if (i + 1 < s.length() && s.charAt(i) == s.charAt(i + 1)) {
                for (j = 0; i + 1 + j < s.length() && i - j >= 0; j++) {
                    if (s.charAt(i - j) != s.charAt(i + 1 + j)) {
                        break;
                    }
                }
                if (tail - head < 2 * (j - 1) + 1) {
                    head = i - (j - 1);
                    tail = i + 1 + (j - 1);
                }
            }
            for (j = 0; i + j < s.length() && i - j >= 0; j++) {
                if (s.charAt(i - j) != s.charAt(i + j)) {
                    break;
                }
            }
            if (tail - head < 2 * (j - 1)) {
                head = i - (j - 1);
                tail = i + (j - 1);
            }
        }
        return s.substring(head, tail + 1);
    }

    public static void main(String args[]) {
        LongestPalindromicSubstring longestPalindromicSubstring = new LongestPalindromicSubstring();
        System.out.println(longestPalindromicSubstring.longestPalindrome("babad"));
    }
}
