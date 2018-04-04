package tech.xinong.xnsm.pro.user.model;

import android.content.Context;

import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.model.BaseModel;

/**
 * Created by xiao on 2017/1/10.
 */

public class RegisterModel extends BaseModel{
    public RegisterModel(Context context) {
        super(context);
    }

    public void registerUser(String cellphone,String pwd,String verificationCode,AbsXnHttpCallback callback){
        Register register = new Register();
        register.setCellphone(cellphone);
        register.setVerificationCode(verificationCode);
        register.setPassword(pwd);
        register.setConfirmPassword(pwd);
        XinongHttpCommend.getInstance(getContext()).registerUser(register, callback);
    }



    /**
     * 注册信息包装类
     */
    public class Register {
        String cellphone;
        String verificationCode;
        String password;
        String confirmPassword;

        public String getConfirmPassword() {
            return confirmPassword;
        }

        public void setConfirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Register() {
        }

        public String getCellphone() {
            return cellphone;
        }

        public void setCellphone(String cellphone) {
            this.cellphone = cellphone;
        }

        public String getVerificationCode() {
            return verificationCode;
        }

        public void setVerificationCode(String verificationCode) {
            this.verificationCode = verificationCode;
        }
    }
}
