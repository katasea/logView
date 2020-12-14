package cn.katasea.auto;

import java.lang.annotation.*;

/**
 * 自定义列注解
 * @author chenfuqiang
 *
 */
//该注解用于方法声明
//表示注解加在接口、类、枚举等
@Target(ElementType.TYPE)
//VM将在运行期也保留注释，因此可以通过反射机制读取注解的信息
@Retention(RetentionPolicy.RUNTIME)
//将此注解包含在javadoc中
@Documented
//允许子类继承父类中的注解
@Inherited
public @interface MyFunction {
	/**
	 * 从access 数据库初始化内置数据过来，需要自己在本软件资源文件夹下面的db.access放入对应表名的数据内容。
	 * @return
	 */
	public boolean autoInitFromAccess() default false;
	/**
	 * 拷贝上年数据，如果你的表不是按年分表。要求表名table2016 。
	 * 暂不支持table里面有theyear字段的拷贝数据。。
	 * @return
	 */
	public boolean copyLastYearData() default false;
	/**
	 * 排序树结构，并支持调整子节点所在的父节点。
	 * @return
	 */
	public boolean treeSort() default false;
	/**
	 * 暂未实现，只是设想
	 * @return
	 */
	//public boolean copyLastMonthData() default false;
}
