package tech.xinong.xnsm.pro.home.view.navigation;

import android.content.Context;
import android.view.ViewGroup;

import tech.xinong.xnsm.pro.base.view.navigation.impl.AbsNavigation;

/**
 * Created by xiao on 2016/11/17.
 */

public class HomeNavigation extends AbsNavigation<HomeNavigation.Builder.HomeNavigationParams> {
    public HomeNavigation(Builder.HomeNavigationParams params) {
        super(params);
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    public static class Builder extends AbsNavigation.Builder{

        @Override
        public AbsNavigation create() {
            return null;
        }
        public static class HomeNavigationParams extends AbsNavigation.Builder.NavigationParams{
            public HomeNavigationParams(Context context, ViewGroup parent){
                super(context,parent);
            }
        }
    }
}
