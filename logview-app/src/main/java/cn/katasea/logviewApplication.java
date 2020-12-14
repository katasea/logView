package cn.katasea;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
@Slf4j
public class logviewApplication {

	public static void main(String[] args) {
		try {
			SpringApplication.run(logviewApplication.class, args);
			log.info("服务启动成功...");
		}catch (Exception e) {
			log.error("服务启动失败：{}",e.getMessage());
		}
	}

}
