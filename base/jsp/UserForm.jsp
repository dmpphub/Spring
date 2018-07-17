<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@	taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>User Creation</title>
	<style type="text/css">
		.col-lg-6 {
			padding-bottom: 6px;
		}
		.error {
			border: 1px solid red;
		}
	</style>
	<script type="text/javascript">
		function callUserCreation() {
			location.href = "${pageContext.request.contextPath}/base/UserForm.htm";
		}
	</script>
</head>
	<body>
		<!-- Page Content -->
        <div id="page-wrapper" >
        <form:form id="userCreationFormId" action="/ilja/base/UserController.htm" method="post" modelAttribute="userVO" >
        
            <div class="container-fluid" style="margin-top: 20px;">
                <div class="row">
                <div class="col-lg-8" style="margin-left:165px;">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            User Creation
                            <div class="panel-head-icon">
								<span onclick="callUserCreation()"> <i class="fa fa-file-text-o panel-head-icon-size" ></i> </span>
							</div>
                        </div>
						<div class="panel-body">
                            <div class="row">
								<div class="col-lg-6">
									<form:label path="userName" id="lbl_userName" cssClass="col-lg-4 required">User Name </form:label>
									<div class="col-lg-8">
										<form:input path="userName" cssClass="form-control" data-required="true" />
										<form:hidden path="userId" />
									</div>
								</div>
								<div class="col-lg-6">
									<form:label path="firstName" id="lbl_firstName" cssClass="col-lg-4 required">First Name </form:label>
									<div class="col-lg-8">
										<form:input path="firstName" cssClass="form-control" data-required="true" />
									</div>
								</div>
								<div class="col-lg-6">
									<form:label path="lastName" id="lbl_lastName" cssClass="col-lg-4 required" >Last Name </form:label>
									<div class="col-lg-8">
										<form:input path="lastName" cssClass="form-control" />
									</div>
								</div>
								<div class="col-lg-6">
									<form:label path="password" id="lbl_password" cssClass="col-lg-4 required" >Password </form:label>
									<div class="col-lg-8">
										<form:password path="password" cssClass="form-control" />
									</div>
								</div>
								<div class="col-lg-6">
									<form:label path="emailId" id="lbl_email" cssClass="col-lg-4 required" >E-Mail </form:label>
									<div class="col-lg-8">
										<form:input path="emailId" cssClass="form-control" />
									</div>
								</div>
								<div class="col-lg-6">
									<form:label path="aadharNo" id="lbl_aadharNo" cssClass="col-lg-4 required" >Aadhar No </form:label>
									<div class="col-lg-8">
										<form:input path="aadharNo" cssClass="form-control" />
									</div>
								</div>
								<div class="col-lg-6">
									<form:label path="mobileNo" id="lbl_mobileNo" cssClass="col-lg-4 required" >Mobile No </form:label>
									<div class="col-lg-8">
										<form:input path="mobileNo" cssClass="form-control" />
									</div>
								</div>
								
								<div class="col-lg-6">
									<form:label path="isActive" id="lbl_activeFlag" cssClass="col-lg-4 required" > Active Flag </form:label>
									<div class="col-lg-8">
										<form:select path="isActive" cssClass="form-control">
											<form:option value="">Select</form:option>
											<form:option value="Y">Yes</form:option>
											<form:option value="N">No</form:option>
										</form:select>
									</div>
								</div>
								<div class="col-lg-12 btn-center" >
									<button type="submit" class="btn btn-primary" id="submitBtn">Submit</button>
								</div>
							</div>
						</div>
                    </div>
                </div>
                <!-- /.row -->
                </div>
            </div>
            </form:form>
            <!-- /.container-fluid -->
        </div>
	</body>
</html>