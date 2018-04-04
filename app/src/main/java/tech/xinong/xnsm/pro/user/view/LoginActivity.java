package tech.xinong.xnsm.pro.user.view;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.MainActivity;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.buy.model.ProductDTO;
import tech.xinong.xnsm.pro.user.model.Customer;
import tech.xinong.xnsm.pro.user.presenter.LoginPresenter;
import tech.xinong.xnsm.util.RegexpUtils;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.XnsConstant;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;

@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity<LoginPresenter> implements View.OnClickListener,LoginView {
    private EditText etPhoneNum;
    private EditText etPassword;
    private long mLastPressBack;
    private Button login;
    /*导航栏标题显示*/
    @ViewInject(R.id.tv_center)
    private TextView tvCenter;
    @ViewInject(R.id.tv_register)
    private TextView tv_register;
    /*导航栏右侧文字*/
    @ViewInject(R.id.tv_right)
    private TextView tvRight;
    @ViewInject(R.id.tv_left)
    private TextView tvLeft;
    /*新型EditText的布局-----用于输入用户名*/
    @ViewInject(R.id.text_input_username)
    private TextInputLayout textInputUsername;
    /*新型EditText的布局-----用于输入密码*/
    @ViewInject(R.id.text_input_password)
    private TextInputLayout textInputPassword;
    /*导航栏的布局*/

    private Customer customer;


    @Override
    public void initWidget() {
    //    initNavigation();
        super.initWidget();
        login = this.findViewById(R.id.login_bt_login);
        login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        textInputUsername.setHint(getResources().getString(R.string.phone_num_hint));
        etPhoneNum = textInputUsername.getEditText();
        textInputPassword.setHint(getResources().getString(R.string.input_password));
        etPassword = textInputPassword.getEditText();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public LoginPresenter bindPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_bt_login:
                String phoneNum = etPhoneNum.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if (check(phoneNum,password)){
                    login(phoneNum, password);
                }
                break;
            case R.id.tv_register:
                skipActivity(RegisterActivity.class);
                break;
            default:
                break;
        }

    }

    private boolean check(String phoneNum,String password) {
        if (TextUtils.isEmpty(phoneNum)){
            T.showShort(mContext,"请输入手机号");
            return false;
        }else {
            if (TextUtils.isEmpty(password)){
                T.showShort(mContext,"请输入密码");
                return false;
            }else{
               if (!RegexpUtils.pPhone.matcher(phoneNum).matches()){
                   T.showShort(mContext,"请输入正确的手机号");
                   return false;
               }
            }
        }
        return true;
    }


//    private void initNavigation() {
//        tvCenter.setVisibility(View.VISIBLE);
//        tvCenter.setText("登录");
//        tvRight.setVisibility(View.VISIBLE);
//        tvRight.setText("注册");
//        tvRight.setTextColor(Color.WHITE);
//        tvRight.setVisibility(View.VISIBLE);
//        //tvLeft.setText("<");
//        //tvLeft.setVisibility(View.VISIBLE);
//        //tvLeft.setTextColor(Color.WHITE);
//        tvLeft.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        tvRight.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                skipActivity(RegisterActivity.class);
//            }
//        });
//    }


    /**
     * 登录
     */
    private void login(final String name, final String password) {
        getPresenter().login(name,password,editor);
    }


    @Override
    public void onLoginSuccess() {
        cancelProgress();
        T.showShort(mContext, "登陆成功");
        initCurrentInfo();
    }


    private void initCurrentInfo() {
        updateUserInfo(new OnUpdateUserInfoComplete() {
            @Override
            public void onComplete(Customer customer) {
                editor.putString(XnsConstant.HEADIMG, TextUtils.isEmpty(customer.getHeadImg())?"":customer.getHeadImg());
                editor.putString(XnsConstant.CUSTOMERID, customer.getId());
                editor.putString(XnsConstant.COVER_IMG,TextUtils.isEmpty(customer.getCoverImg())?"":customer.getCoverImg());
                editor.putString(XnsConstant.DISTRICT,TextUtils.isEmpty(customer.getDistrict())?"":customer.getDistrict());
                editor.putString(XnsConstant.FULL_NAME,TextUtils.isEmpty(customer.getFullName())?"":customer.getFullName());
                editor.putString(XnsConstant.STREET,TextUtils.isEmpty(customer.getStreet())?"":customer.getStreet());
                editor.putString(XnsConstant.PROVINCE,TextUtils.isEmpty(customer.getProvince())?"":customer.getProvince());
                editor.putString(XnsConstant.CITY, TextUtils.isEmpty(customer.getCity())?"":customer.getCity());
                XinongHttpCommend.getInstance(mContext).favs(new AbsXnHttpCallback(mContext) {
                    @Override
                    public void onSuccess(String info, String result) {
                        JSONObject jb = JSON.parseObject(result);
                        List<ProductDTO> productDTOS = JSONObject.parseArray(jb.getString("products"),ProductDTO.class);
                        if (productDTOS!=null&&productDTOS.size()>0){
                            StringBuilder sbIds = new StringBuilder();
                            StringBuilder sbNames = new StringBuilder();
                            for (ProductDTO p : productDTOS){
                                sbIds.append(p.getId()+",");
                                sbNames.append(p.getName()+",");
                            }
                            editor.putString(XnsConstant.FAVNAME,sbNames.toString());
                            editor.putString(XnsConstant.FAvID,sbIds.toString());
                            editor.commit();
                        }


                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        LoginActivity.this.startActivity(intent);
                        LoginActivity.this.finish();

                    }
                });
            }
        });


    }

    @Override
    public void onLoginFiled() {
        cancelProgress();
        T.showShort(mContext, "用户名或密码输入不正确，请重新输入");
        etPassword.setText("");
    }



//    /**
//     * 捕捉back,实现二次点击回退键再推出程序
//     */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            long currentTime = System.currentTimeMillis();
//            if (currentTime - mLastPressBack > 2000) {
//                T.showShort(this, "再按一次退出");
//                mLastPressBack = currentTime;
//            } else {
//                ActivityCollector.finishAll();
//                this.finish();
//            }
//        }
//        return true;
//    }

    @Override
    public String setToolBarTitle() {
        return "喜农市登录";
    }

    @Override
    public void initData() {

    }
}
