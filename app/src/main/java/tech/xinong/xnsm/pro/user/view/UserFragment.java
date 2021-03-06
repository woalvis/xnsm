package tech.xinong.xnsm.pro.user.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.view.BaseFragment;
import tech.xinong.xnsm.pro.base.view.navigation.impl.DefaultNavigation;
import tech.xinong.xnsm.pro.user.presenter.LoginPresenter;
import tech.xinong.xnsm.util.XnsConstant;

/**
 * 我的页面
 * Created by 肖志金 on 2016/11/10.
 */
public class UserFragment extends BaseFragment<LoginPresenter,LoginView> implements View.OnClickListener{

    private LoginPresenter loginPresenter;
    private LoginView loginView;
    private TextView userLogin;
    private TextView userRegister;
    private TextView userName;
    private TextView logout;//退出登录
    private LinearLayout myOrderLayout;//我的订单的布局
    private LinearLayout myAddressLayout;//我的收货地址的布局
    //创建对象
    @Override
    public LoginPresenter bindPresenter() {
        loginPresenter = new LoginPresenter(getContext());
        return loginPresenter;
    }


    @Override
    protected int bindLayoutId() {
        return R.layout.fragment_user;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("xx","UserFragment----onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i("xx","UserFragment----onViewCreated");
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * 初始化控件
     * @param contentView 布局文件
     */
    @Override
    protected void initContentView(View contentView) {
        initNavigation(contentView);
        userLogin = (TextView) contentView.findViewById(R.id.user_login);
        userRegister = (TextView) contentView.findViewById(R.id.user_register);
        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                getActivity().startActivity(intent);
            }
        });


        userRegister = (TextView) contentView.findViewById(R.id.user_register);
        userRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),RegisterActivity.class);
                getActivity().startActivity(intent);
            }
        });


        userName = (TextView) contentView.findViewById(R.id.user_name);

        SharedPreferences sp = getActivity().getSharedPreferences(XnsConstant.SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
//        editor.putString("token","eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ4aW5vbmd0ZWNoLmNvbSIsInN1YiI6IjEzODExNTQ4NjY2IiwiaWF0IjoxNDc5OTUzODIyLCJleHAiOjE0Nzk5NjgyMjIsInJvbGVzIjpbIlJPTEVfQ1VTVE9NRVIiXX0.6vnxQxUOStM7ZacdDZZxf1pTERTWcvv2dgMl1Re_6aA");
//        editor.commit();
        String userNameStr = sp.getString(XnsConstant.USER_NAME,"");
        userName.setText(userNameStr);


        myOrderLayout = (LinearLayout) contentView.findViewById(R.id.user_my_order_layout);
        myOrderLayout.setOnClickListener(this);

        logout = (TextView) contentView.findViewById(R.id.logout);
        logout.setOnClickListener(this);
        /*我的收货地址*/
        myAddressLayout = (LinearLayout) contentView.findViewById(R.id.user_my_address_layout);
        myAddressLayout.setOnClickListener(this);

//        Toolbar toolbar = (Toolbar) contentView.findViewById(R.id.toolbar);
//        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) contentView.findViewById(R.id.collapsing_toolbar);
//        collapsingToolbarLayout.setTitle(userNameStr);
    }

    @Override
    public LoginView bindView() {
        return super.bindView();
    }



    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_my_order_layout:
                Intent intent = new Intent(getActivity(),MyOrdersActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.logout:
                skipActivity(LoginActivity.class);
                break;
            case R.id.user_my_address_layout:
                skipActivity(EditMyAddressActivity.class);
                break;
        }
    }


    @Override
    public void initNavigation(View contentView) {
        DefaultNavigation.Builder builder = new DefaultNavigation.Builder(getContext(),(ViewGroup)contentView);
        builder.setCenterText(R.string.user)
                .setRightTextColor(R.color.white)
                .setRightText("编辑资料")
                .setRightOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        skipActivity(EditMyInfoActivity.class);
                    }
                }).create();

    }
}
