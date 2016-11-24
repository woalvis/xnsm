package tech.xinong.xnsm.util.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//作用范围---注解类
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BaseEvent {
	/**
	 * listenerSetter方法名称 例如（setOnClickListener）
	 * @return
	 */
	String listenerSetter();
	//listener类型  例如（OnClickListener，OnLongClickListener）
	Class<?> listenerType();
	//回调方法
	String callBackMethod();
}
