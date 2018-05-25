package wemake.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@EnableWebMvc
@Configuration
public class MvcWebConfig extends WebMvcConfigurerAdapter {
	protected static Logger log = LoggerFactory.getLogger(MvcWebConfig.class);
	
	@Autowired private AuthInterceptor authInterceptor;
	
	/**
	 * Interceptor 설정
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authInterceptor);
	}
	
	
	/**
	 * JSP
	 */
	@Bean
	public ViewResolver getViewResolver() {		
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/view/");
		viewResolver.setSuffix(".jsp");
		viewResolver.setOrder(1);		
		
		return viewResolver;
	}
	
	/**
	* JS, CSS, IMG 폴더
	*/
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
     }
}
