package juc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Solution {
    public static void main(String[] args){
        Solution s = new Solution();
        List<Integer> nums = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5));
        Collections.sort(nums);
        int size = nums.size();
        int res = nums.get(size -1);
        for(int i = size - 2 ; i >= 0 ; i--){
            int t = res - nums.get(i);
            if(res < t){
                res = t;
            }else {
                break;
            }
        }

        System.out.println(res);
    }




}
