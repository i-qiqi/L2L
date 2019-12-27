package array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MinSubArrayLen {
    public static  void  main(String[] args){
        int[] nums = new int[]{2,3,1,2,4,3};
        int s = 7;

        int i = 0;
        int j = -1;
        int total = 0;
        int res = nums.length+1;
        int[] resList = null;
        for(;i < nums.length;){
            if(j < nums.length - 1 && total < s){
                ++j;
                total += nums[j];
            }else{
                total -= nums[i];
                ++i;
            }
            if(total >= s){
                res = Math.min(res , j-i+1);
                resList = Arrays.copyOfRange(nums , i , j+1);
            }

        }

        if(res == nums.length+1){
            res = 0;
        }

        System.out.println("res = "+res + " , list = "+resList);

    }
}
