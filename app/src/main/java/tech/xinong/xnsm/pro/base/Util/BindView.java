package tech.xinong.xnsm.pro.base.Util;

/**
 * Created by xiao on 2016/9/1.
 */
public @interface BindView {
    int id();
    boolean click() default false;
}
