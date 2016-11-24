package tech.xinong.xnsm.util.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//表示注解使用的范围 ！@Target
//ElementType.TYPE---表示注解使用的范围是类
@Target(ElementType.TYPE)
//RetentionPolicy---声明注解类的生命周期
//RetentionPolicy.RUNTIME---表示注解保存在程序运行的过程中，让我们的虚拟机通过反射机制加载
@Retention(RetentionPolicy.RUNTIME)
public @interface ContentView {
	//布局的ID
	int value();
}
