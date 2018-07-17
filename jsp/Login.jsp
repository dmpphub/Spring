<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Login</title>
		<link rel="stylesheet" href="../css/login/loginStyle.css"/>
		<script src="../js/jquery/jquery.js"></script>
	</head>
	<body>
		<div class="main-wrap">
	        <div class="login-main">
	         <form:form action="LoginController.htm" method="post" >
	        	<form:input path="userName" cssClass="box1 border1" placeholder="user name" />
	        	<form:password path="password" cssClass="box1 border2" placeholder="password" />
	        	<input type="submit" class="send" id="submitbtn" value="Go">
	            <p>Forgot Your Password? <a href="#">click here</a></p>
	        </form:form> 
	        </div>
    	</div>
	</body>
	<script type="text/javascript">
	$('#submitbtn').click(function() {
		
		if ($('#userName').val() == '') {
			alert ('Please enter user name');
			$('#userName').focus();
			return false;
		}
		
		if ($('#password').val() == '') {
			alert ('Please enter password');
			$('#password').focus();
			return false;
		}
		
		$('#submitbtn').submit();
	});
	
	
	</script>
</html>