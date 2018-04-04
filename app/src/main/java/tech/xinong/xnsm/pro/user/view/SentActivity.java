package tech.xinong.xnsm.pro.user.view;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanzhenjie.album.Album;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.publish.model.adapter.ImageShowAdapter;
import tech.xinong.xnsm.pro.user.model.SentCreator;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;
import tech.xinong.xnsm.views.GridViewForScrollView;

/**
 * Created by xiao on 2018/1/16.
 */
@ContentView(R.layout.activity_sent)
public class SentActivity extends BaseActivity {
    @ViewInject(R.id.ll_logistics)
    private LinearLayout ll_logistics;
    @ViewInject(R.id.ll_logistic_no)
    private LinearLayout ll_logistic_no;
    @ViewInject(R.id.ll_com_person)
    private LinearLayout ll_com_person;
    @ViewInject(R.id.tv_order_no)
    private TextView tv_order_no;
    @ViewInject(R.id.tv_logistics)
    private TextView tv_logistics;
    @ViewInject(R.id.tv_com_person)
    private TextView tv_com_person;
    @ViewInject(R.id.et_com_person)
    private EditText et_com_person;
    @ViewInject(R.id.tv_logistic_no)
    private TextView tv_logistic_no;
    @ViewInject(R.id.et_logistic_no)
    private EditText et_logistic_no;
    private LogisticCom logisticCom;
    @ViewInject(R.id.tv_submit)
    private TextView tv_submit;
    @ViewInject(R.id.gv_goods_photos)
    private GridViewForScrollView gv_goods_photos;
    private String orderNo;
    private ImageShowAdapter adapter;
    public static final int REQ_CODE_SELECT_PHOTOS = 0x1009;//选择图片
    private List<File> fileList;
    private ArrayList<String> photoList;
    private ArrayList<String> uploadFileStrList;
    private String orderId;

    @Override
    public void initData() {
        orderNo = intent.getStringExtra("orderNo");
        orderId = intent.getStringExtra("orderId");
        fileList = new ArrayList<>();
        photoList = new ArrayList<>();
        uploadFileStrList = new ArrayList<>();
        tv_order_no.setText(orderNo);

        adapter = new ImageShowAdapter(mContext);
        adapter.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Album.startAlbum(SentActivity.this, REQ_CODE_SELECT_PHOTOS
                        , 3                                                        // 指定选择数量。
                        , ContextCompat.getColor(mContext, R.color.colorPrimary)        // 指定Toolbar的颜色。
                        , ContextCompat.getColor(mContext, R.color.colorPrimaryDark));  // 指定状态栏的颜色。
            }
        });
        gv_goods_photos.setAdapter(adapter);

        ll_logistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogBotton("物流方式", "物流/快递发货,货运零担发货", new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ll_com_person.setVisibility(View.VISIBLE);
                        ll_logistic_no.setVisibility(View.VISIBLE);
                        boolean isCom = position == 0 ? true : false;
                        logisticCom = new LogisticCom(isCom);
                        tv_com_person.setText(logisticCom.getCom());
                        et_com_person.setHint(logisticCom.getComHint());
                        tv_logistics.setText(logisticCom.getTitleHint());
                        tv_logistic_no.setText(logisticCom.getLogisticNo());
                        et_logistic_no.setHint(logisticCom.getLogisticNoHint());
                    }
                });
            }
        });


        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()){
                    SentCreator sentCreator = new SentCreator();
                    sentCreator.setMode(logisticCom.getCom());
                    sentCreator.setCompany(et_com_person.getText().toString().trim());
                    sentCreator.setNumber(et_logistic_no.getText().toString().trim());
                    sentCreator.setFiles(fileList);
                    showProgress("正在上传");
                    XinongHttpCommend.getInstance(mContext).sent(orderId,sentCreator, new AbsXnHttpCallback(mContext) {
                        @Override
                        public void onSuccess(String info, String result) {
                            T.showShort(mContext,"发货成功");
                            cancelProgress();
                            Intent intent = new Intent(mContext,MyOrdersActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                }


            }
        });
    }

    private boolean check() {
        if (TextUtils.isEmpty(et_com_person.getText().toString().trim())){
            T.showShort(mContext,"请您检查输入，不能为空");
            return false;
        }
        if (TextUtils.isEmpty(et_logistic_no.getText().toString().trim())){
            T.showShort(mContext,"请您检查输入，不能为空");
            return false;
        }
        if (fileList.size()==0){
            T.showShort(mContext,"请上传凭证，以避免纠纷");
            return false;
        }

        return true;
    }


    @Override
    public String setToolBarTitle() {
        return "确认发货";
    }


    private class LogisticCom {

        private String com;
        private String logisticNo;
        private String titleHint;
        private String comHint;
        private String logisticNoHint;

        public LogisticCom(boolean isCom) {
            if (isCom) {
                com = "物流公司";
                logisticNo = "物流单号";
                titleHint = "物流/快递发货";
                comHint = "请输入";
                logisticNoHint = "请输入";
            } else {
                com = "承运公司/个人";
                logisticNo = "车牌号码";
                titleHint = "货运零担发货";
                comHint = "请输入物流公司/司机姓名电话>";
                logisticNoHint = "请输入";
            }
        }

        public String getCom() {
            return com;
        }

        public String getLogisticNo() {
            return logisticNo;
        }

        public String getTitleHint() {
            return titleHint;
        }

        public String getComHint() {
            return comHint;
        }

        public String getLogisticNoHint() {
            return logisticNoHint;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK&&requestCode == REQ_CODE_SELECT_PHOTOS){
            List<String> strs = Album.parseResult(data);
            if (strs != null && strs.size() > 0) {
                for (String str : strs) {
                    if (!photoList.contains(str)) {
                        if (photoList.size()>=3){
                            T.showShort(mContext,"只能上传三张照片");
                        }else {
                            photoList.add(str);
                        }

                    }
                    if (!uploadFileStrList.contains(str)) {
                        if (fileList.size()==3){
                            T.showShort(mContext,"只能上传三张照片");
                        }else {
                            fileList.add(new File(str));
                            uploadFileStrList.add(str);
                        }

                    }
                }
                adapter.setPhotoList(photoList);
        }

            super.onActivityResult(requestCode, resultCode, data);
        }
    }



}
