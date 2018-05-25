package wemake.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
	protected static Logger log = LoggerFactory.getLogger(AuthInterceptor.class);
	
	/**
	 * Controller를 접근하기 전
	 */
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {

		return true;
	}
	
	/**
	 * Controller 실행 후 View를 보여주기전
	 */
	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse res, Object handler, ModelAndView model) throws Exception {
		
	}
}
