package tech.xinong.xnsm.http.framework.impl.xinonghttp.xinonghttpcallback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by xiao on 2016/11/23.
 */

public abstract class AbsXnHttpCallback implements XnHttpCallback{

    @Override
    public void onHttpError(Call call, Response response, Exception e) {
        if (response.code()==401){

        }
    }

    @Override
    public void onError(String info) {

    }
}
