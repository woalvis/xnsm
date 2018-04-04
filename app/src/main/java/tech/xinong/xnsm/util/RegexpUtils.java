package tech.xinong.xnsm.util;

import java.util.regex.Pattern;

/**
 * Created by xiao on 2018/1/21.
 */

public class RegexpUtils {
    public static Pattern pPhone = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

    public static Pattern pTel = Pattern.compile("[0-9-()（）]{7,18}");

    public static Pattern pIdCard = Pattern.compile("^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$");
    // /^\d{6}(18|19|20)?\d{2}(0[1-9]|1[012])(0[1-9]|[12]\d|3[01])\d{3}(\d|[xX])$/
}
