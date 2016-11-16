package tech.xinong.xnsm.http.framework.impl;


import tech.xinong.xnsm.http.framework.IHttpCommand;
import tech.xinong.xnsm.http.framework.IRequestParam;

/**
 * Created by xzj on 16/6/2.
 */
public abstract class HttpCommand<T> implements IHttpCommand<T> {

    @Override
    public String execute(String url, IRequestParam<T> requestParam) {
        return null;
    }
}
