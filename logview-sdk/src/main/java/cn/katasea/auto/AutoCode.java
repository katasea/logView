package cn.katasea.auto;

import java.lang.annotation.*;

/**
 * 自动生成代码注解
 * @author chenfuqiang
 *
 */
//表示注解加在接口、类、枚举等
@Target(ElementType.TYPE)
//VM将在运行期也保留注释，因此可以通过反射机制读取注解的信息
@Retention(RetentionPolicy.RUNTIME)
//将此注解包含在javadoc中
@Documented
//允许子类继承父类中的注解
@Inherited
public @interface AutoCode {
	/**
	 * 是否可以覆盖原先自动生成的文件
	 * 默认不覆盖已有的文件
	 * @return
	 */
	public abstract boolean isOverride() default false;
	/**
	 * 默认false
	 * 是否包含pojo对象的父类，这里只支持一层继承关系
	 * @return
	 */
	public abstract boolean includeSupperClass() default false;

	/**
	 * 展示形式
	 * 暂时支持 tree 树【默认】和 list 列表分页
	 * @return
	 */
	public abstract String showWay() default "tree";
}
