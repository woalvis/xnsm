package tech.xinong.xnsm.pro.base.model;

import android.support.v4.app.Fragment;

/**
 * Created by xiao on 2017/11/23.
 */

public class FragmentInfo {
    private  String title;

    private Fragment fragment;

    public FragmentInfo(String title, Fragment fragment) {
        this.title = title;
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
