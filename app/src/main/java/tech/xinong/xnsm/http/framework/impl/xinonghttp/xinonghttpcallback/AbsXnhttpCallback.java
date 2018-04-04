package tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback;
import android.content.Context;

import okhttp3.Call;
import okhttp3.Response;
import tech.xinong.xnsm.pro.base.view.BaseActivity;
import tech.xinong.xnsm.util.L;

/**
 * Created by xiao on 2016/11/23.
 */

public abstract class AbsXnHttpCallback implements XnHttpCallback{

    private Context mContext;

    protected AbsXnHttpCallback(Context context){
        mContext = context;
    }

    @Override
    public void onHttpError(Call call, Response response, Exception e) {
        L.e(e.toString());
        ((BaseActivity)mContext).cancelProgress();
    }

    @Override
    public void onError(String info) {
        L.e(info);
        ((BaseActivity)mContext).cancelProgress();
    }
}
