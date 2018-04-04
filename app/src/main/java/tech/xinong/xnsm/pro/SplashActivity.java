package tech.xinong.xnsm.pro;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.request.BaseRequest;
import com.mylhyl.circledialog.CircleDialog;
import com.vondear.rxtools.RxAppTool;

import java.io.File;
import java.util.List;
import java.util.Properties;

import okhttp3.Call;
import okhttp3.Response;
import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.utils.HttpConstant;
import tech.xinong.xnsm.pro.base.Util.ProperTies;
import tech.xinong.xnsm.pro.base.application.XnsApplication;
import tech.xinong.xnsm.pro.buy.model.ProductDTO;
import tech.xinong.xnsm.pro.user.model.Customer;
import tech.xinong.xnsm.util.FileUtil;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.XnsConstant;

public class SplashActivity extends AppCompatActivity {
    private Context mContext;
    private ImageView splash_title;
    private ImageView splash_mask;
    private ImageView splash_logo;
    private ImageView splash_mask_hidden;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor editor;
    private String favString;
    private String update_url = "";
    private CircleDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mContext = this;
        //checkForUpdate();
        init();
    }


    void checkForUpdate() {
        String checkUpdateUrl = "http://api.fir.im/apps/latest/" + XnsConstant.FIR_APP_ID + "?api_token=" + XnsConstant.FIR_APP_TOKEN;
        XinongHttpCommend.getInstance(this).checkUpdate(checkUpdateUrl, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                JSONObject versionJsonObj = JSONObject.parseObject(s);
                int firVersionCode = Integer.parseInt(versionJsonObj.getString("version"));
                //FIR上当前的versionName
                String firVersionName = versionJsonObj.getString("versionShort");
                update_url = versionJsonObj.getString("installUrl");
                String changelog = versionJsonObj.getString("changelog");
                PackageManager pm = SplashActivity.this.getPackageManager();
                try {
                    PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
                    if (pi != null) {
                        int currentVersionCode = pi.versionCode;
                        String currentVersionName = pi.versionName;
                        //   if(currentVersionCode == firVersionCode && currentVersionName.equals(firVersionName)){
                        if (currentVersionCode >= firVersionCode) {
                            init();
                        } else {
                            showChangeLog(changelog);
                        }
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void init() {
        splash_logo = findViewById(R.id.splash_logo);
        splash_title = findViewById(R.id.splash_title);
        splash_mask = findViewById(R.id.splash_mask);
        splash_mask_hidden = findViewById(R.id.splash_mask_hidden);
        mSharedPreferences = getSharedPreferences(XnsConstant.SP_NAME, MODE_PRIVATE);
        editor = mSharedPreferences.edit();
        final Animation mask = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left_mask);

        splash_mask_hidden.setAnimation(mask);

        mask.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                splash_mask_hidden.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        ObjectAnimator objectAnimatorLogo = ObjectAnimator.ofFloat(splash_logo, "alpha", 0.0f, 1.0f);
        ObjectAnimator objectAnimatorTitle = ObjectAnimator.ofFloat(splash_title, "alpha", 0.0f, 1.0f);
        objectAnimatorLogo.setDuration(3000);
        objectAnimatorTitle.setDuration(3000);
        objectAnimatorLogo.start();
        objectAnimatorTitle.start();

        objectAnimatorLogo.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (mSharedPreferences.getBoolean("first", true)) {
                    if (getFavs() != null && getFavs().length > 0) {
                        toServer();
                    } else {
                        skipMain();
                    }
                } else {
                    skipMain();
                }
            }
        });
    }

    private void toServer() {
        XinongHttpCommend.getInstance(this).nameToProduct(favString, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                JSONObject jsonObject = JSON.parseObject(s);
                int code = jsonObject.getInteger("c");
                String info = jsonObject.getString("i");
                String result = jsonObject.getString("r");
                if (code == 0) {
                    List<ProductDTO> productDTOS = JSON.parseArray(result, ProductDTO.class);
                    StringBuilder ids = new StringBuilder();
                    for (ProductDTO productDTO : productDTOS) {
                        ids.append(productDTO.getId() + ",");
                    }

                    editor.putString("ids", ids.substring(0, ids.length() - 1));
                    editor.commit();
                    skipMain();
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
            }

            @Override
            public void onBefore(BaseRequest request) {
                super.onBefore(request);
            }
        });
    }


    private void skipMain() {
        final String userName = mSharedPreferences.getString(XnsConstant.USER_NAME, "");
        final String passWord = mSharedPreferences.getString(XnsConstant.PASSWORD, "");
        if (!TextUtils.isEmpty(userName)) {
            XinongHttpCommend.getInstance(mContext).login(userName, passWord, new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    String token = response.header(HttpConstant.HTTP_HEADER_TOKEN);
                    HttpHeaders headers = new HttpHeaders();
                    headers.put(HttpConstant.HTTP_HEADER_TOKEN, token);
                    //将token放入到header
                    OkGo.getInstance().addCommonHeaders(headers);
                 /*登陆成功后将用户名密码，还有得到的token存入文件中*/
                    editor.putString(XnsConstant.USER_NAME, userName);
                    editor.putString(XnsConstant.PASSWORD, passWord);
                    editor.putString(XnsConstant.TOKEN, token);
                    editor.commit();
                    T.showShort(mContext,"自动登录成功");
                    updateUserInfo();
                }
            });
        } else {
            Intent intent;
            if(mSharedPreferences.getBoolean("first",true)){
                editor.putBoolean("first", false);
                editor.commit();
                intent = new Intent(SplashActivity.this, GuideActivity.class);
            }
            else {
                intent = new Intent(SplashActivity.this, MainActivity.class);
            }
            startActivity(intent);
            SplashActivity.this.finish();
        }


    }


    public String[] getFavs() {
        Properties proper = ProperTies.getProperties(this);
        favString = proper.getProperty(XnsConstant.FAVS);
        if (!TextUtils.isEmpty(favString))
            favString = proper.getProperty(XnsConstant.FAVS).replace("，", ",");
        if (TextUtils.isEmpty(favString)) {
            return null;
        } else {
            mSharedPreferences = getSharedPreferences(XnsConstant.SP_NAME, MODE_PRIVATE);
            editor = mSharedPreferences.edit();
            editor.putString(XnsConstant.FAVS, favString);
            editor.commit();
            return favString.split(",");
        }
    }


    void showChangeLog(String changelog) {


        showDoneDialog("更新详情", changelog, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadAPK();
            }
        });

    }


    public void showDoneDialog(String titleText, String contentTExt, View.OnClickListener listener) {
        new CircleDialog.Builder(this)
                .setTitle(titleText)
                .setText(contentTExt)
                .setPositive("确定", listener)
                .show();
    }

    /**
     * 下载安装文件
     */
    void downloadAPK() {
        builder = new CircleDialog.Builder(this);
        builder.setCancelable(false).setCanceledOnTouchOutside(false)
                .setTitle("下载")
                //.setProgressText("已经下载")
                .setProgressText("已经下载%s了")
//                        .setProgressDrawable(R.drawable.bg_progress_h)
                .setNegative("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .show();


        String[] allowedContentTypes = new String[]{"application/vnd.android.package-archive", "*"};
        OkGo.get(update_url)//
                .tag(this)//
                .execute(new FileCallback(FileUtil.initPath(), "xns.apk") {  //文件下载时，可以指定下载的文件目录和文件名
                    @Override
                    public void onSuccess(File file, Call call, Response response) {
                        // file 即为文件数据，文件保存在指定目录
                        install(file);
                    }

                    @Override
                    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        //这里回调下载进度(该回调在主线程,可以直接更新ui)
                        //currentSize totalSize以字节byte为单位
                        builder.setProgress((int)totalSize,(int)currentSize).create();
                    }
                });
    }


    void install(File file) {

        RxAppTool.installApp(this,file);

//        String str = "/xns.apk"; //APK的名字
//        String fileName = FileUtil.initPath() + str; //我们上面说到路径
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            //判断版本是否在7.0以上
//            Uri apkUri =
//                    FileProvider.getUriForFile(this,
//                            "tech.xinong.xnsm" + ".fileprovider",
//                            file);
//            //添加这一句表示对目标应用临时授权该Uri所代表的文件
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
//        } else {
//            intent.setDataAndType(Uri.fromFile(file),
//                    "application/vnd.android.package-archive");
//        }
//


//        File tmp = file;
//        if (!tmp.exists())
//            return;
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setDataAndType(Uri.fromFile(tmp), "application/vnd.android.package-archive");
//        startActivity(intent);
//        finish();
    }



    private void updateUserInfo(){
        XinongHttpCommend.getInstance(mContext).getCurrentInfo(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                Customer customer = JSONObject.parseObject(JSON.parseObject(s).getString("r"),Customer.class);
                editor.putString(XnsConstant.CUSTOMERID,customer.getId());
                editor.putString(XnsConstant.LOGIN_NAME,customer.getLoginName());
                editor.putString(XnsConstant.HEADIMG,customer.getHeadImg());
                editor.putString(XnsConstant.COVER_IMG,customer.getCoverImg());
                editor.putString(XnsConstant.FULL_NAME,customer.getFullName());
                editor.putString(XnsConstant.DISTRICT,customer.getDistrict());
                editor.putString(XnsConstant.PROVINCE,customer.getProvince());
                editor.putString(XnsConstant.TAGS,customer.getTags());
                editor.putString(XnsConstant.STREET,customer.getStreet());
                editor.commit();
                XinongHttpCommend.getInstance(mContext).favs(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        JSONObject jb = JSON.parseObject(JSON.parseObject(s).getString("r"));

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


                        Intent intent;
                        if (TextUtils.isEmpty(XnsApplication.mInstance.token)) {
                            intent = new Intent(SplashActivity.this, GuideActivity.class);
                        } else {
                            intent = new Intent(SplashActivity.this, MainActivity.class);
                        }
                        startActivity(intent);
                        SplashActivity.this.finish();
                    }
                });
            }
        });
    }
}
