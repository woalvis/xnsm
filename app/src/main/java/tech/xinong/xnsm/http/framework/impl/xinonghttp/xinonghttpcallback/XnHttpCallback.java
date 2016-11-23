package tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by xiao on 2016/11/22.
 */

public interface XnHttpCallback {
    void onSuccess(String info,String result);

    void onError(Call call, Response response, Exception e);
}
