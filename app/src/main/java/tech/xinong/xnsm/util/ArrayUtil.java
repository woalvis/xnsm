package tech.xinong.xnsm.util;

import java.util.Arrays;

/**
 * Created by xiao on 2017/11/16.
 */

public class ArrayUtil {
    //使用循环判断数组内是否包含某元素
    public static boolean c(String[] arr,String targetValue){
        if (arr==null||arr.length==0){
            return false;
        }

        for(String s:arr){
            if(s.equals(targetValue))
                return true;
        }
        return false;
    }

    public static String[] d(String[] arr,String targetValue){
        if (arr!=null&&arr.length!=0){

            if (c(arr,targetValue)){
                int position = Arrays.binarySearch(arr, targetValue);
                arr[position] = arr[arr.length-1];
                // arr = (String[]) Arrays.copyOf(arr, arr.length-1);
                //System.out.println(Arrays.toString(arr));
                return Arrays.copyOf(arr, arr.length-1);
            }
        }
        return arr;
    }

}
