package wemake.config;

import java.nio.charset.Charset;

import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.filter.CharacterEncodingFilter;

import wemake.util.StringUtil;

@Configuration
public class FilterConfig {
	protected static Logger log = LoggerFactory.getLogger(FilterConfig.class);

	/**
	 * ResponseBody 출력시 문자 인코딩
	 */
	@Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        return new StringHttpMessageConverter(Charset.forName(StringUtil.charset));
    }
 
	/**
	 * 데이터 인코딩
	 */
    @Bean
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding(StringUtil.charset);
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }
}
