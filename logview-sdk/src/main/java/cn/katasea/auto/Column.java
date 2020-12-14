package cn.katasea.auto;


import cn.katasea.util.Global;

import java.lang.annotation.*;

/**
 * 自定义列注解
 * @author chenfuqiang
 *
 */
//该注解用于方法声明
@Target(ElementType.FIELD)
//VM将在运行期也保留注释，因此可以通过反射机制读取注解的信息
@Retention(RetentionPolicy.RUNTIME)
//将此注解包含在javadoc中
@Documented
//允许子类继承父类中的注解
@Inherited
public @interface Column {
	//=====================数据库定义方面=====================//
	/**
	 * e.g.  1  101  10101  102 10201
	 * 自动根据数据库记录按上方规则生成主键代码。
	 * 只能放置于 treeId 是true 的列
	 * @return
	 */
	public abstract boolean autoGenneral() default false;

	/**
	 * 默认当前字段名。
	 * @return
	 */
	public abstract String name() default Global.NULLSTRING;
	/**
	 * 默认值是normal
	 * 仅可以填写【primary,normal】其他无效。
	 * 可以多个字段都是primary做联合主键。
	 * @return
	 */
	public abstract String flag() default "normal";
	/**
	 * 默认值是varchar(50)
	 * 【varchar(50),decimal(18,2),int,smallint,...】
	 * 字段类型，支持SQLSERVER数据库类型
	 * @return
	 */
	public abstract String type() default "varchar(50)";

	/**
	 * 【identity(1,1),NOT NULL,NULL】
	 * 自增，非空，空，默认空值
	 * 默认不写表示primary则是NOT NULL normal是NULL 如果是normal 想要NOT NULL 可以加OTH="NOT NULL"
	 * @return
	 */
	public abstract String oth() default Global.NULLSTRING;

}
