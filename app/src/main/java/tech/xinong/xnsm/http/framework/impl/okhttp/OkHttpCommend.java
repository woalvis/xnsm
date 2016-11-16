package tech.xinong.xnsm.http.framework.impl.okhttp;


import java.util.HashMap;

import tech.xinong.xnsm.http.framework.IHttpCommand;
import tech.xinong.xnsm.http.framework.IRequestParam;

/**
 * Created by xzj on 16/6/11.
 */
public class OkHttpCommend  implements IHttpCommand<HashMap<String,Object>> {
    @Override
    public String execute(String url, IRequestParam<HashMap<String, Object>> requestParam) {
        //OKHttp代码是不是可以

        return null;
    }
}
