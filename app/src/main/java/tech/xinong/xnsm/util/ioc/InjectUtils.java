package tech.xinong.xnsm.util.ioc;

import android.annotation.SuppressLint;
import android.content.Context;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import tech.xinong.xnsm.R;

@SuppressLint("UseSparseArrays")
public class InjectUtils {

	public final static String METHOD_SET_CONTENT_VIEW = "setContentView";
	public final static String METHOD_FIND_VIEW_BYID = "findViewById";

	// 注入activity（加载布局、初始化空间、设置监听）
	public static void inject(Context c) {
		injectContentView(c);
		Map<Integer, Object> map  = injectView(c);
		injectMyView(c);
		injectEvent(c, map);
		injectEvent(c,injectMyView(c));
	}

	private static Map<Integer, Object> injectMyView(Context c) {
		// TODO Auto-generated method stub
		Map<Integer, Object> viewMap = new HashMap<Integer, Object>();
		Class<? extends Context> clazz = c.getClass();
		Field[] fs = clazz.getDeclaredFields();
		for (Field field : fs) {
			field.setAccessible(true);
			MyViewInject myViewInject = field.getAnnotation(MyViewInject.class);
			if(myViewInject!=null){
				String fieldName = field.getName();
				try {
					int value = myViewInject.value();
					int viewId = R.id.class.getField(fieldName).getInt(null);
					Method method = clazz.getMethod(METHOD_FIND_VIEW_BYID, int.class);
					Object view = method.invoke(c, viewId);
					field.set(c, view);
					viewMap.put(value, view);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return viewMap;
	}

	/**
	 * 注入监听
	 * @param c
	 *            当前activity
	 */
	private static  Map<Integer, Object> injectEvent(Context c, Map<Integer, Object> viewMap) {
		Class<? extends Context> clazz = c.getClass();
		Method[] methods = clazz.getMethods();
		Map<Integer, Object> map = viewMap;
		System.out.println(map);
		try {
			for (Method method : methods) {
				Annotation[] annotations = method.getAnnotations();
//				if (annotations == null || annotations.length == 0)
//					continue;
				for (Annotation a : annotations) {
					if (a != null) {
						Class<? extends Annotation> annotationType = a
								.annotationType();
						BaseEvent baseEvent = annotationType
								.getAnnotation(BaseEvent.class);
						if (baseEvent != null) {
							// 获取listener方法名称
							String listenerSetter = baseEvent.listenerSetter();
							// 获取listener类型
							Class<?> listenerType = baseEvent.listenerType();
							// 获取回调方法名称
							String callBackMethod = baseEvent.callBackMethod();
							// 获取OnClick注解类上的value方法
							Method valueMethod = annotationType
									.getDeclaredMethod("value");
							// 获取注解
							int[] ids = (int[]) valueMethod.invoke(a);
							// 给我们的Activity设置代理对象
							ListenerInvercationHandler invercationHandler = new ListenerInvercationHandler(
									c);
							// 回调方法OnclickListener中的onClick绑定
							invercationHandler
									.addMethod(callBackMethod, method);

							// 通过Proxy创建一个annotationType的代理对象
							// listener实际上就是我们的OnClickListener
							Object listener = Proxy.newProxyInstance(
									listenerType.getClassLoader(),
									new Class<?>[] { listenerType },
									invercationHandler);
							// 给我们的控件设置点击事件
							for (int id : ids) {
								Object view = viewMap.get(id);
								// 获取我们的控件setOnClickListener方法
								Method listenerMethod = view
										.getClass()
										.getMethod(listenerSetter, listenerType);
								listenerMethod.invoke(view, listener);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return viewMap;
	}

	private static Map<Integer, Object> injectView(Context c) {
		// 控件
		Map<Integer, Object> viewMap = new HashMap<Integer, Object>();
		try {
			Class<? extends Context> clazz = c.getClass();
			Field[] fields = clazz.getDeclaredFields();
			for(int i=0;i<fields.length;i++){
				Field f = fields[i];
				f.setAccessible(true);
				String fieldsName = f.getName();
				ViewInject annotation = f.getAnnotation(ViewInject.class);
				if (annotation != null) {
					int value = annotation.value();
					// 获取当前activity身上的findViewById方法
					Method method = clazz.getMethod(METHOD_FIND_VIEW_BYID,
							int.class);
					// 执行获得我们的控件
					Object view = method.invoke(c, value);
					// 给我们的属性赋值
					f.set(c, view);

					viewMap.put(value, view);
				}
			}
//			for (Field f : fields) {
//				f.setAccessible(true);
//				String fieldsName = f.getName();
//				ViewInject annotation = f.getAnnotation(ViewInject.class);
//				if (annotation != null) {
//					int value = annotation.value();
//					// 获取当前activity身上的findViewById方法
//					Method method = clazz.getMethod(METHOD_FIND_VIEW_BYID,
//							int.class);
//					// 执行获得我们的控件
//					Object view = method.invoke(c, value);
//					// 给我们的属性赋值
//					f.set(c, view);
//
//					viewMap.put(value, view);
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return viewMap;
	}

	/**
	 * 注入布局文件
	 *
	 * @param c
	 */
	private static void injectContentView(Context c) {
		Class<? extends Context> class1 = c.getClass();
		ContentView annotation = class1.getAnnotation(ContentView.class);
		// 获取布局ID
		int layoutId = annotation.value();
		try {
			Method method = class1
					.getMethod(METHOD_SET_CONTENT_VIEW, int.class);
			method.invoke(c, layoutId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}