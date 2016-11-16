package tech.xinong.xnsm.pro.user.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.pro.base.view.BaseFragment;
import tech.xinong.xnsm.pro.user.presenter.LoginPresenter;
import tech.xinong.xnsm.pro.user.presenter.LoginView;

/**
 * 我的页面
 * Created by 肖志金 on 2016/11/10.
 */
public class UserFragment extends BaseFragment<LoginPresenter,LoginView> {

    private LoginPresenter loginPresenter;
    private LoginView loginView;
    private TextView userLogin;
    private TextView userRegister;
    //创建对象
    @Override
    public LoginPresenter bindPresenter() {
        loginPresenter = new LoginPresenter(getContext());

        return loginPresenter;
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
    }

    @Override
    public LoginView bindView() {
        return super.bindView();
    }

    @Override
    protected int bindLayoutId() {
        return R.layout.fragment_user;
    }

    @Override
    public void onStart() {
        Log.i("xx","UserFragment----onStart");
        super.onStart();
    }
}
