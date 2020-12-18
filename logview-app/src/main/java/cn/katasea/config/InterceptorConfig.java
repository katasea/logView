package cn.katasea.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author katasea
 * 2020/5/12 13:48
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {

	/**
	 * 添加静态资源
	 * @param registry
	 */
	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
	}


	/**
	 * 这里需要先将限流拦截器入住，不然无法获取到拦截器中的redistemplate
	 * @return
	 */
	@Bean
	public AuthenticationInterceptor getAuthenticationInterceptor() {
		return new AuthenticationInterceptor();
	}

	/**
	 * 添加拦截器，忽略的URL不填则会判断是否登录
	 * @param registry
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(getAuthenticationInterceptor())
				.addPathPatterns("/**")
				.excludePathPatterns("/login")
				.excludePathPatterns("/publicKey")
//				.excludePathPatterns("/privateKey")
				.excludePathPatterns("/css/**")
				.excludePathPatterns("/js/**")
				.excludePathPatterns("/img/**")
				.excludePathPatterns("/fonts/**")
				.excludePathPatterns("/demo/**")
				.excludePathPatterns("/scss/**")
				.excludePathPatterns("/error");

		super.addInterceptors(registry);
	}


}
