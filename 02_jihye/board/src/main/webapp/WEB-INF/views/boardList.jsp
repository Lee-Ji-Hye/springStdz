<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${tagName}</title>
</head>
<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
<script type="text/javascript">
	$(function(){
		$("#savebtn").click(function(){
			 $.ajax({  
			     type: "POST", 
			     url: "/api/board",  
			     data: $('#theForm').serialize(),
			     dataType: "json",
			     success: function(e){
				     console.log(e);
				  },
			     error:function(request,status,error){
			        alert("코드 : "+request.status+"\n"+"출력 : "+request.responseText+"\n"+"에러 : "+error);
			       }
			     }
			 );
		});
	});
</script>
<body>

	<form id="theForm">
		<p>
			아이디 : <input type="text" id="userId"  name="userId"  value="" />
			제   목 : <input type="text" id="title"   name="title"   value="" />
			내   용 : <input type="text" id="content" name="content" value="" />
		</p>
		<p>
			<input type="button" id="savebtn" value="등록" />
		</p>
		
		<div>



		</div>
	</form>
</body>
</html>