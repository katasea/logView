package cn.katasea.reflect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 反射配置类
 */
public class ReflectConfig implements Serializable{
	private static final long serialVersionUID = -5658969389322144625L;

	private String beanName;
	private String methodName;

	private List<Object> params = new ArrayList<>();

	public ReflectConfig() {
		super();
	}

	public ReflectConfig(String beanName,String methodName){
		this.beanName = beanName;
		this.methodName = methodName;
	}

	public ReflectConfig(String beanName,String methodName, List<Object> params) {
		this.beanName = beanName;
		this.methodName = methodName;
		this.params.addAll(params);
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public List<Object> getParams() {
		return params;
	}

	public void setParam(Object param) {
		this.params.add(param);
	}
	public void setParams(List<Object> params) {
		this.params = params;
	}
}
