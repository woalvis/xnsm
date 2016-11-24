package tech.xinong.xnsm.util.ioc.iocproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiao on 2016/9/5.
 */
public class ListenerInvercationHandler implements InvocationHandler {
    private Object targetObj;
    private Map<String,Method> methodMap;
    public ListenerInvercationHandler(Object targetObj){
        this.targetObj = targetObj;
        this.methodMap = new HashMap<String,Method>();
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        // TODO Auto-generated method stub
        if(targetObj!=null){
            //执行OnclickListener中的onClick方法（其实就是执行回调方法）
            method = methodMap.get(method.getName());
            if(method!=null){
                //执行onClick方法
                method.invoke(targetObj, args);
            }
        }
        return null;
    }

    public void addMethod(String name,Method method){
        this.methodMap.put(name, method);
    }
}