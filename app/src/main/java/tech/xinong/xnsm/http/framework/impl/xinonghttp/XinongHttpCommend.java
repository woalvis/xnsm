package tech.xinong.xnsm.http.framework.impl.xinonghttp;


import okhttp3.OkHttpClient;
import tech.xinong.xnsm.http.framework.IHttpCommand;
import tech.xinong.xnsm.http.framework.IRequestParam;
import tech.xinong.xnsm.http.framework.impl.RequestParam;

/**
 * Created by Dream on 16/6/11.
 */
public class XinongHttpCommend implements IHttpCommand<RequestParam> {
    private OkHttpClient mOkHttpClient;

    @Override
    public String execute(String url, IRequestParam<RequestParam> requestParam) {

        return null;
    }

//    private void getAsynHttp() {
//        mOkHttpClient=new OkHttpClient();
//        Request.Builder requestBuilder = new Request.Builder().url("http://www.baidu.com");
//        //可以省略，默认是GET请求
//        requestBuilder.method("GET",null);
//        Request request = requestBuilder.build();
//        Call mcall= mOkHttpClient.newCall(request);
//        mcall.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (null != response.cacheResponse()) {
//                    String str = response.cacheResponse().toString();
//                    Log.i("wangshu", "cache---" + str);
//                } else {
//                    response.body().string();
//                    String str = response.networkResponse().toString();
//                    Log.i("wangshu", "network---" + str);
//                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getApplicationContext(), "请求成功", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
//    }
}
