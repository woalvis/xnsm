package tech.xinong.xnsm.util;

import java.text.DecimalFormat;

/**
 * Created by xiao on 2016/11/29.
 */

public class NumUtil {

    /**
     * 检查一个字符串数字是否是正数（包含小数）
     * 如果是则返回double型保留两位小数的结果
     * 如果不是则返回-1；
     */
    public static double isPositiveNumber(String string){

        String resultNum = "";
        if (string.matches("[0-9]+.*[0-9]*")) {
            try {
                DecimalFormat df   = new DecimalFormat("######0.00");
                resultNum = df.format(Double.parseDouble(string));

            }catch (Exception e){
                return -1;
            }

        }else {
            return -1;
        }


        return Double.parseDouble(resultNum);
    }

    public static void main(String[] args){

        String[] ss = {"adsfhjkkq","-0.2","-1","0","879.32ejujkjadk","123","123.512300"};

        for (String s : ss){
            System.out.println(s+"----->"+isPositiveNumber(s));
        }


    }
}
