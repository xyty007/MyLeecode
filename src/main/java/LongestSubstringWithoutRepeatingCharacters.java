package main.java;

/**
 * 3. Longest Substring Without Repeating Characters
 * Given a string, find the length of the longest substring without repeating characters.
 * <p>
 * Examples:
 * Given "abcabcbb", the answer is "abc", which the length is 3.
 * Given "bbbbb", the answer is "b", with the length of 1.
 * Given "pwwkew", the answer is "wke", with the length of 3. Note that the answer must be a substring, "pwke" is a subsequence and not a substring.
 */

public class LongestSubstringWithoutRepeatingCharacters {

    public int lengthOfLongestSubstring(String s) {

        if (s.length() == 0) {
            return 0;
        }

        int[] history = new int[256];
        int maxlength = 0;
        int expired = -1;

        for (int i = 0; i < history.length; i++) {
            history[i] = -1;
        }

        for (int i = 0; i < s.length(); i++) {
            char temp = s.charAt(i);
            if (history[temp ] > -1) {
                if (history[temp ] > expired) {
                    int tempMaxlength = (i-1)-(expired+1)+1;
                    maxlength = maxlength<tempMaxlength?tempMaxlength:maxlength;
                    expired = history[temp ];
                }
            }
            history[temp ] = i;
        }

        int remainingLength = (s.length()-1)-(expired+1)+1;
        maxlength = maxlength<remainingLength?remainingLength:maxlength;

        return maxlength;
    }

    public static void main(String args[]){
        LongestSubstringWithoutRepeatingCharacters longestSubstringWithoutRepeatingCharacters = new LongestSubstringWithoutRepeatingCharacters();
        System.out.println(longestSubstringWithoutRepeatingCharacters.lengthOfLongestSubstring("abcae"));
    }
}
