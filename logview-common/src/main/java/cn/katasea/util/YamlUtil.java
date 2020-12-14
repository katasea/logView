package cn.katasea.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * type.yml 配置文件对应下拉集合显示
 */
public class YamlUtil {
	private static Logger logger = LoggerFactory.getLogger(YamlUtil.class);
	public static final String GATEWAY_CONFIG = "pay-reflection-handler.yml";
	public static final String SRVPAY_CONFIG = "/config/imethod-type-reflection-handler.yml";

	/**
	 * 读取下拉状态配置参数返回map
	 * @param typeName 检索 a/b/c
	 */
	public static Map getTypePropertieMap(String config, String typeName) throws IOException{
		String yamlPath = getClassResources()+ config; // yaml路径
		logger.info("查找配置文件信息，关键字：{}，检索文件路径：{}",typeName,yamlPath);
		File yamlFile = new File(yamlPath);
		Yaml yaml = new Yaml();
		Map map = yaml.loadAs(new FileInputStream(yamlFile), Map.class);
		//检索
		String[] words = typeName.split("/");
		for(String word : words) {
			if(map!=null && map.keySet().size()>0) {
				map = (HashMap) map.get(word);
			}else {
				logger.error("检索配置文件失败了：{},{}",typeName,word);
			}
		}
		logger.info("获取配置文件内容为：{}",map);
		return map;
	}

	/*
	 * 获取classpath2
	 */
	public static String getClassResources(){
		String path =  System.getProperty("user.dir");
		if(path.indexOf(":") != 1){
			path = File.separator + path;
		}
		return path;
	}

}