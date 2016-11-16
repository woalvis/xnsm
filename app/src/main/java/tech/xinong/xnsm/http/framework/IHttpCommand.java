package tech.xinong.xnsm.http.framework;

/**
 * 执行网络请求命令接口
 * @param <T>
 */
public interface IHttpCommand<T> {
	String execute(String url, IRequestParam<T> requestParam);
}
