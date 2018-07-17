<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>ILJA</title>
<jsp:include page="/jsp/templates/CSSImport.jsp" />
<jsp:include page="/jsp/templates/JSImport.jsp" />
<style type="text/css">
#modalLayer {
	width: 100%;
	height: 100%;
	top: 0px;
	left: 0px;
	position: fixed;
	display: block;
	opacity: 0.9;
	z-index: 99;
	text-align: center;
}

#loading-content {
	position: absolute;
	top: 50%;
	left: 50%;
	text-align: center;
	z-index: 5000;
}
</style>
</head>
<body>
	<div id="modalLayer" style="visibility: hidden;">
		<div id="loading-content">
			<img src="../images/custom/ajax-loader.gif" />
		</div>
	</div>
	<div id="wrapper">
		<tiles:insertAttribute name="header" />
		<tiles:insertAttribute name="menu" />
		<tiles:insertAttribute name="body" />
		<tiles:insertAttribute name="footer" />
	</div>
</body>
</html>