package tech.xinong.xnsm.pro.test;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.views.NavigationHorizontalScrollView;
import tech.xinong.xnsm.views.entity.Navigation;

/**
 * Created by xiao on 2017/11/21.
 */
@ContentView(R.layout.test4)
public class Test4 extends Activity {

    private Context context;
    private List<Navigation> navs = buildNavigation();
    private NavigationHorizontalScrollView mHorizontalScrollView;
    private TextView tv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test4);
        context=this;
        tv=(TextView)findViewById(R.id.tv);
        tv.setText("You clicked "+navs.get(0).getTitle());

        mHorizontalScrollView=(NavigationHorizontalScrollView)findViewById(R.id.horizontal_scrollview);
        mHorizontalScrollView.setImageView((ImageView) findViewById(R.id.iv_pre),(ImageView) findViewById(R.id.iv_next));

        mHorizontalScrollView.setOnItemClickListener(new NavigationHorizontalScrollView.OnItemClickListener() {

            @Override
            public void click(int position) {
                // TODO Auto-generated method stub
                tv.setText("You clicked "+navs.get(position).getTitle());
            }
        });

        mHorizontalScrollView.setAdapter(new NavigationAdapter());

    }

    private List<Navigation> buildNavigation() {
        List<Navigation> navigations = new ArrayList<Navigation>();
        navigations.add(new Navigation(0, "url", "首页"));
        navigations.add(new Navigation(1, "url", "新闻"));
        navigations.add(new Navigation(2, "url", "科技"));
        navigations.add(new Navigation(3, "url", "设置"));
        navigations.add(new Navigation(4, "url", "朋友"));
        navigations.add(new Navigation(5, "url", "测试长标题"));
        navigations.add(new Navigation(7, "url", "测试"));
        navigations.add(new Navigation(6, "url", "我的宝贝"));
        return navigations;
    }


    class NavigationAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return navs.size();
        }

        @Override
        public Object getItem(int position) {
            return navs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_tag, null);
            }
            ((TextView) convertView).setText(navs.get(position).getTitle());
            return convertView;
        }

    }

}
