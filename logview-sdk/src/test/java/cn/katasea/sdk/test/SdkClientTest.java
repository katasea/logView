package cn.katasea.sdk.test;


import cn.katasea.bean.vo.ResponseVO;
import cn.katasea.myenum.ChannelWayType;
import cn.katasea.util.DateUtil;
import cn.katasea.myenum.ChannelType;
import cn.katasea.sdk.SdkClient;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author katasea
 * 2019/5/23 13:47
 */
public class SdkClientTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String web_pay_url = "http://127.0.0.1:8000/logview-app";
	private static final String signType = "Plain";
	private static final String encryptType = "Plain";
	private static final String appId = "1A3VL0KVK0000B020A0A0000CC3F48AD";
	private static final String appSecret = "1A3VL0KVE0010B010A0A0000277BDC91";
	private static final String version = "DEFAULT.1.0";

	/**
	 * 测试
	 */
	@Test
	public void test() {
		logger.info("开始请求");
		SdkClient sdkClient = new SdkClient(web_pay_url, appId, appSecret, signType, encryptType);
		try {
			Map<String,Object> param = new HashMap<>();
			param.put("name","testUser");
			param.put("id","A000001");
//			// 发起交易
			ResponseVO responseParams = sdkClient.unifyCall("test.service.forfun",version,param);
			logger.info("返回的结果为："+JSON.toJSONString(responseParams));
			logger.info("其中业务返回为："+JSON.toJSONString(responseParams.getBizObj()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void test2() throws InterruptedException {
		new Thread(this::test).start();
		new Thread(this::test).start();

		Thread.sleep(15000);
	}

	@Test
	public void test3() throws InterruptedException {
		// 同一时刻最大的并发线程的个数  即并发数
		final int concurrentThreadNum = 2;

		//总共多少线程
		final int countThreadNum = 2;

		ExecutorService executorService = Executors.newCachedThreadPool();
		CountDownLatch countDownLatch = new CountDownLatch(countThreadNum);
		Semaphore semaphore = new Semaphore(concurrentThreadNum);
		for (int i = 0; i< countThreadNum; i++) {
			executorService.execute(()->{
				try {
					semaphore.acquire();
					test();
					semaphore.release();
				} catch (InterruptedException e) {
					logger.error("exception", e);
				}
				countDownLatch.countDown();
			});
		}
		countDownLatch.await();
		executorService.shutdown();
		logger.info("请求完成");
	}

	/**
	 * 测试JPA 读取用户信息
	 */
	@Test
	public void testUserRepository() {
		SdkClient sdkClient = new SdkClient(web_pay_url, appId, appSecret, signType, encryptType);
		try {
			Map<String,Object> param = new HashMap<>();
			param.put("parentDir","");
			// 发起交易
			ResponseVO responseParams = sdkClient.unifyCall("file.service.getFileList","V1.0",param);
			logger.info("返回的结果为："+JSON.toJSONString(responseParams));
			logger.info("其中业务返回为："+JSON.toJSONString(responseParams.getBizObj()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
