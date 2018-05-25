package wemake;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import wemake.util.StringUtil;

@Controller
@Scope("request")
public class TestController {
	protected static Logger log = LoggerFactory.getLogger(TestController.class);
		
	private String endPointUrl = "https://distribution-xml.booking.com";
	private String charsetName = "UTF-8";
	private String id = "wemakeprice1";
	private String pw = "dnlapvm1";
	
	private int responseCode = 0;
	long totalTime = 0;
		
	@RequestMapping(value="/booking", method=RequestMethod.GET, produces = "text/html")
	public String getBooking(Model model) throws Exception{
		
		return "booking";
	}
	
	@RequestMapping(value="/searchProc", method=RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String getSearchProc(Model model, @RequestParam Map<String,Object> map) throws Exception{
		String searchText = StringUtil.null2str(map.get("searchText"));
		
		String url = endPointUrl + "/2.1/json/autocomplete?";
		String data = "language=ko&text="+searchText;
		
		log.info("url======" + url + data);
		
		long start = System.currentTimeMillis();		
		String responseData = getHttpUrlConnection(url, data, "GET");
		long end = System.currentTimeMillis();
		log.info("hotelAvailbilty Time : " + (end - start) );
		
		log.info("result------" + responseData);
		return responseData;
	}	
	
	@RequestMapping(value="/bookingProc", method=RequestMethod.POST, produces = "text/html")
	public String getBookingProc(Model model, @RequestParam Map<String,Object> map) throws Exception{
		
		Runnable r = () -> {
			String type = StringUtil.null2str(map.get("type"));
			String id = StringUtil.null2str(map.get("id"));
			String checkin = StringUtil.null2str(map.get("checkin"));
			String checkout = StringUtil.null2str(map.get("checkout"));
			String orderBy = StringUtil.null2str(map.get("orderBy"));
			String rows = StringUtil.null2str(map.get("rows"));		

			/* extras
			hotel_amenities : 호텔 편의 시설 보이기
			hotel_details : 호텔 추가 정보 표시
			payment_terms : 객실의 선불 또는 취소와 같은 지불 조건 표시
			room_amenities : 객실 내 편의 시설 보이기
			room_details : 객실 세부 정보 표시
			room_policies : 방에 대한 정책 표시
			*/
			String url = endPointUrl + "/2.1/json/hotelAvailability?";
			String data = "checkin="+checkin+"&checkout="+checkout;
			data += "&room1=A,A,14,12"; // 성인 2명
			data += "&extras=hotel_amenities,room_details,hotel_details,payment_terms,room_amenities,room_policies";
			data += "&order_by=" + orderBy;
			data += "&rows=" + rows;
			data += "&language=ko";
			
			if("hotel".equals(type)) data += "&hotel_ids="+id;
			if("city".equals(type)) data += "&city_ids="+id;
			if("district".equals(type)) data += "&district_ids="+id; // 지역
			if("airport".equals(type)) data += "&airport="+id; // 항공(IATA코드)
			
			log.info("url======" + url + data);
			
			try {
	        		
				long start = System.currentTimeMillis();
				String responseData = getHttpUrlConnection(url, data, "POST");		
				long end = System.currentTimeMillis();
				long responseTime = end - start;
				totalTime += responseTime;
				log.info("result-------" + responseData);
				log.info("hotelAvailbilty Time : " + responseTime);
				
				ObjectMapper objectMapper = new ObjectMapper();
				Map<String, Object> jMap = objectMapper.readValue(responseData, new TypeReference<Map<String, Object>>(){});
				
				model.addAttribute("map", map);
				model.addAttribute("responseCode", responseCode);
				model.addAttribute("jMap", jMap);
				model.addAttribute("responseTime", responseTime);
			}catch(Exception e) {
				e.printStackTrace();
			}
		};
		
		int i = 0;
		int cnt = 1;
		//totalTime = 0;
		ArrayList<Thread> tList = new ArrayList<Thread>();
		
		while( i < cnt) {
			log.info("i-------" + i);
			Thread test = new Thread(r);
			test.start();
			tList.add(test);
			i++;
		}
		
		
		tList.forEach(t -> {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		
		log.info("totalTime-----" + totalTime);
		
		
		return "booking";
	}	
	
		
	public String getHttpUrlConnection(String address, String sendData, String method ) throws Exception {
        
	    String urlData = "";
	    HttpsURLConnection con = null;
	    
	    try {           
	        URL url = new URL(address);  // 요청을 보낸 URL
	                    
            con = (HttpsURLConnection) url.openConnection(); 
            con.setDoOutput(true);
            con.setRequestMethod(method);
            
	        // http basic auth
            byte[] userInfo = ( id + ":" + pw ).getBytes();
            String encInfo = Base64.encodeBase64String( userInfo );
            
            
            con.setRequestProperty( "Authorization", "Basic " +  encInfo);
            
            con.connect();
             
            DataOutputStream dos = new DataOutputStream(con.getOutputStream());
            dos.write(sendData.getBytes());
            dos.flush();
                        
            responseCode = con.getResponseCode();
        	log.info("error : " + con.getResponseMessage());
        	urlData = responseCode != 200 ? readContents(con.getErrorStream()) : readContents(con.getInputStream());
        	
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if( con != null) con.disconnect();
        }
        return urlData;
    }
        
    public String readContents(InputStream cont) throws Exception {
        
    	try( BufferedReader in = new BufferedReader(new InputStreamReader(cont, charsetName)) ){
    		StringBuffer outStr = new StringBuffer();
    		String inputLine = "";
    		    		
    		while ((inputLine = in.readLine()) != null) {
                outStr.append(inputLine).append("\n");
            }
    		
    		return outStr.toString();
    	}
    } 
    
}
