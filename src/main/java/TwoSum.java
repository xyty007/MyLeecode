package main.java;

import java.util.Arrays;

/**
 * Given an array of integers, return indices of the two numbers such that they add up to a specific target.
 * You may assume that each input would have exactly one solution, and you may not use the same element twice.
 *
 * Example:
 * Given nums = [2, 7, 11, 15], target = 9,
 *
 * Because nums[0] + nums[1] = 2 + 7 = 9,
 * return [0, 1].
 *
 */

public class TwoSum {
    public int[] twoSum(int[] nums, int target) {
        int[] result = new int[2];
        outer:
        for(int i=0; i<nums.length; i++){
            for(int j=i+1; j<nums.length; j++){
                if (nums[i] + nums[j]==target){
                    result[0]=i;
                    result[1]=j;
                    break outer;
                }
            }
        }
        return result;
    }

    public static void main(String args[]){
        TwoSum twoSum = new TwoSum();
        int[] nums = {1,2,4,8,16};
        System.out.println(Arrays.toString(twoSum.twoSum(nums,9)));
    }
}
