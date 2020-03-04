$(function(){
	// 공통코드
	ajax("", "POST", "");
	
	
	
/**
 * Transaction 처리
 * */
	function ajax(url, method, data) {
		let result;
		
		/*var params={
				"mst_cd":"all"
				, "cust_id":$('#cust_id').val()
				, "member_no":$('#member_no').val()
		};*/
		
		$.ajax({
			type : "POST",
			url : // "${pageContext.request.contextPath}/bmp/getComList"
			data : data // JSON.stringify(params),
			dataType : "json",
			async : false,
			cache : false,
			contentType: "application/json; charset=UTF-8",
			timeout : 30000,
			success : function(rstData) {
			}
		});
		
		console.log(result);
	}


	
/**
 * 이벤트 처리
 * */
	// 할일 추가 버튼 클릭 이벤트
    $("#taskAddBtn").on("click", function() {
        console.log("c");  
    });
    
    // 입력 검증
});