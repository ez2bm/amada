<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
</head>
<body>
<form id="frm" name="frm" method="post" action="/bookingProc">
검색 : <input type="text" name="searchText" id="searchText"/>
<input type="button" value="검색" onclick="fnSearch()" /><br /><br />


종류 :
 <input type="radio" id="type" name="type" value="hotel" ${map.type eq 'hotel' ? 'checked' : ''} />호텔
 <input type="radio" id="type" name="type" value="city" ${map.type eq 'city' ? 'checked' : ''}/>도시
 <input type="radio" id="type" name="type" value="district" ${map.type eq 'district' ? 'checked' : ''}/>지역
 <input type="radio" id="type" name="type" value="airport" ${map.type eq 'airport' ? 'checked' : ''}/>항공<br />    
조회코드 : <input type="text" name="id" id="id" value="${map.id}" /><br />
체크인 : <input type="text" name="checkin" id="checkin" value="${map.checkin}" /><br />
체크아웃 : <input type="text" name="checkout" id="checkout" value="${map.checkout}" /><br />
정렬 : <input type="radio" name="orderBy" value="distance" ${map.orderBy eq 'distance' ? 'checked' : ''} />거리
<input type="radio" name="orderBy" value="popularity" ${map.orderBy eq 'popularity' ? 'checked' : ''} />인기도
<input type="radio" name="orderBy" value="price" ${map.orderBy eq 'price' ? 'checked' : ''} />가격
<input type="radio" name="orderBy" value="ranking" ${map.orderBy eq 'ranking' ? 'checked' : ''} />순위
<input type="radio" name="orderBy" value="review_score" ${map.orderBy eq 'review_score' ? 'checked' : ''} />리뷰점수
<input type="radio" name="orderBy" value="stars" ${map.orderBy eq 'stars' ? 'checked' : ''} />별점 
<br />
출력수 : <input type="text" name="rows" id="rows" value="${map.rows}" /><br />
<input type="submit" value="실행"/><br />
</form>
<div id="searchResult" /></div>

<div style="border:1px solid #000;padding:10px">
responseCode : ${responseCode} <br />
responseTime : ${responseTime} <br />
총 조회수 : ${fn:length(jMap.result)}


<div id="result">
<span onclick="$('#result').hide();$('#resultBtn').show()"> output</span><br />

result : ${jMap}<br/><br/>
</div>

<div id="resultBtn" style="display:none">
<span onclick="$('#result').show();$('#resultBtn').hide()">output 보기</span>
</div> 
<br />

<c:forEach var="list" items="${jMap.result}">
호텔 url : ${list.hotel_url}<br />
호텔 이미지 : ${list.photo}<br/>
호텔명 : ${list.hotel_name}<br/>
호텔등급 : ${list.stars}<br />
호텔주소 : ${list.address}<br />
중심지로부터 몇 km : <br />
평가 : ${list.review_score}<br />
금액 : ${list.hotel_currency_code} ${list.price}<br />
	<c:forEach var="room" items="${list.rooms}">
	    방번호: ${room.room_id}
		성인 : ${room.adults} ${room.children}<br />
	</c:forEach>
	<br/>
</c:forEach>


hotel_amenities : 호텔 편의 시설 보이기<br />
hotel_details : 호텔 추가 정보 표시<br />
payment_terms : 객실의 선불 또는 취소와 같은 지불 조건 표시<br />
room_amenities : 객실 내 편의 시설 보이기<br />
room_details : 객실 세부 정보 표시<br />
room_policies : 방에 대한 정책 표시
 hotel_facilities,room_description,room_photos,hotel_info,room_facilities,hotel_photos,room_info,hotel_policies,hotel_description,payment_details
 </div>
 

<script>
function fnSearch(){
	$.post("/searchProc", $("#frm").serialize(), function(data){
		var resultTxt = "";
		
		for(var i=0;i<data.result.length;i++){
			var row = data.result[i];
			var type = row.type;
			var id = row.id;
			var name = row.name;
			var contury_name = row.country_name;
			
			resultTxt += i + ".type : " + type + "<br />"
                      + i + ".id : " + id + "<br />" 
                      + i + ".name : " + name + "<br />"
                      + i + ".contury_name :" + contury_name + "<br />";
			console.log(i + " : " + type);
			console.log(i + " : " + id);
			console.log("\n");
			
			if(i==0){
				$("[name=type]").each(function() { 
					if(this.value == type) 
						$(this).prop("checked", true); 
				});
				$("#id").val(id);
			}
		}
		
		$("#searchResult").html(resultTxt);
	}, "json");
}

</script>
</body>
</html>
