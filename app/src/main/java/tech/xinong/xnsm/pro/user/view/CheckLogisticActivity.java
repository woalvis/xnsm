package tech.xinong.xnsm.pro.user.view;

import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;

import tech.xinong.xnsm.R;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.XinongHttpCommend;
import tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback.AbsXnHttpCallback;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.pro.base.view.adapter.CommonAdapter;
import tech.xinong.xnsm.pro.base.view.adapter.CommonViewHolder;
import tech.xinong.xnsm.pro.buy.model.ListingDocDTO;
import tech.xinong.xnsm.pro.user.model.LogisticCom;
import tech.xinong.xnsm.pro.user.model.LogisticsModel;
import tech.xinong.xnsm.util.ImageUtil;
import tech.xinong.xnsm.util.T;
import tech.xinong.xnsm.util.ioc.ContentView;
import tech.xinong.xnsm.util.ioc.ViewInject;

/**
 * Created by xiao on 2018/1/17.
 */
@ContentView(R.layout.activity_check_logistic)
public class CheckLogisticActivity extends BaseActivity {
    @ViewInject(R.id.tv_order_no)
    private TextView tv_order_no;
    @ViewInject(R.id.tv_logistics_mode)
    private TextView tv_logistics_mode;
    @ViewInject(R.id.tv_logistic_com)
    private TextView tv_logistic_com;
    @ViewInject(R.id.tv_logistic_com_show)
    private TextView tv_logistic_com_show;
    @ViewInject(R.id.tv_logistic_no)
    private TextView tv_logistic_no;
    @ViewInject(R.id.tv_logistic_no_show)
    private TextView tv_logistic_no_show;
    @ViewInject(R.id.gv_pic)
    private GridView gv_pic;
    private String orderId;
    private String orderNo;
    @Override
    public void initData() {
        orderId = intent.getStringExtra("orderId");
        orderNo = intent.getStringExtra("orderNo");
        tv_order_no.setText(orderNo);
        XinongHttpCommend.getInstance(mContext).logistics(orderId,
                new AbsXnHttpCallback(mContext) {
                    @Override
                    public void onSuccess(String info, String result) {
                        if (TextUtils.isEmpty(result)){
                            T.showShort(mContext,"物流信息获取失败");
                        }else {
                            LogisticsModel logisticsModel = JSONObject.parseObject(result,LogisticsModel.class);
                            initPage(logisticsModel);
                        }
                    }
                });
    }

    private void initPage(LogisticsModel logisticsModel) {
        LogisticCom logisticCom = new LogisticCom(logisticsModel.getMode().equals("物流公司"));
        tv_logistics_mode.setText(logisticCom.getTitleHint());
        tv_logistic_com.setText(logisticCom.getCom());
        tv_logistic_com_show.setText(logisticsModel.getCompany());
        tv_logistic_no.setText(logisticCom.getLogisticNo());
        tv_logistic_no_show.setText(logisticsModel.getNumber());
        gv_pic.setAdapter(new CommonAdapter<ListingDocDTO>(mContext,R.layout.item_img,logisticsModel.getDocs()) {
            @Override
            protected void fillItemData(CommonViewHolder viewHolder, int position, final ListingDocDTO item) {
                viewHolder.getView(R.id.img_del).setVisibility(View.GONE);
                SimpleDraweeView img = (SimpleDraweeView) viewHolder.getView(R.id.img);
                img.setImageURI(ImageUtil.getImgUrl(item.getDocName()));
                viewHolder.setOnClickListener(R.id.img, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showImg(ImageUtil.getImgUrl(item.getDocName()));
                    }
                });
            }
        });

    }

    @Override
    public String setToolBarTitle() {
        return "物流详情";
    }
}
