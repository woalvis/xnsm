package tech.xinong.xnsm.http.framework.impl.system;


import java.util.HashMap;

import tech.xinong.xnsm.http.framework.IHttpCommand;
import tech.xinong.xnsm.http.framework.IRequestParam;
import tech.xinong.xnsm.http.framework.utils.HttpUtils;


/**
 * Created by xzj on 16/5/28.
 */
public class SystemHttpCommand implements IHttpCommand<HashMap<String,Object>> {

    @Override
    public String execute(String url, IRequestParam<HashMap<String, Object>> requestParam) {
        try {
            return HttpUtils.post(url,requestParam.getRequestParam());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
