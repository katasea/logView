package cn.katasea.main.service.impl;

import cn.katasea.bean.po.Application;
import cn.katasea.main.service.ApplicationService;
import org.springframework.stereotype.Service;

/**
 * @author katasea
 * 2019/9/16 10:44
 */
@Service
public class ApplicationServiceImpl implements ApplicationService {
	@Override
	public Application getApplication(String appid) {
		//TODO CACHED BY REDIS
		Application application = new Application();
		application.setAppId("1A3VL0KVK0000B020A0A0000CC3F48AD");
		application.setAppSecret("1A3VL0KVE0010B010A0A0000277BDC91");
		application.setAppName("模拟应用返回数据");
		return application;
	}
}
