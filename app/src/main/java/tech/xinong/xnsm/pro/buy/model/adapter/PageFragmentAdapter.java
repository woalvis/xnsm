package tech.xinong.xnsm.pro.buy.model.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;
import tech.xinong.xnsm.pro.base.model.FragmentInfo;

/**
 * Created by xiao on 2017/11/23.
 */

public class PageFragmentAdapter extends FragmentPagerAdapter {
    private List<FragmentInfo> infos;

    public PageFragmentAdapter(FragmentManager fm, List<FragmentInfo> infos) {
        super(fm);
        this.infos = infos;
    }

    @Override
    public Fragment getItem(int position) {
        return  infos == null ? null : infos.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return infos == null ? 0 : infos.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return infos == null ? "" : infos.get(position).getTitle();
    }
}
