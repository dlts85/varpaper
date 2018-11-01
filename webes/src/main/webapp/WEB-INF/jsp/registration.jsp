<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="cn">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1; charset=UTF-8">
<title>欢迎注册</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width">
<link rel="stylesheet"
		href='<c:url value="/web-resources/lib/bootstrap-3.3.6/css/bootstrap.min.css" />' >
<link rel="stylesheet"
		href='<c:url value="/web-resources/lib/css/main.css" />'>
</head>
<body>
	<div class="mainmenu-wrapper">
      <div class="container">
       <div class="menuextras">
         <div class="extras">
           <ul>
           	 <li><a href="login">登陆</a></li>
           	 <li><a href="registration">注册</a></li>
           </ul>
         </div>
       </div>
       <nav id="mainmenu" class="mainmenu">
         <ul>
           <li class="logo-wrapper"><a href="login"><img src="web-resources/lib/img/logo_new.png" alt="Multipurpose Twitter Bootstrap Template" height ="30" width="170"></img></a></li>
           <li>
             <a href="login"></a>
           </li>
           <li>
             <a><b>重新定义文献检索：专业知识+人工智能=所想即所得</b></a>
           </li>
         </ul>
       </nav>
     </div>
   </div>
   
   <!-- Page Title -->
   <div class="section section-breadcrumbs">
    <div class="container">
     <div class="row">
      <div class="col-md-12">
       <h1>VarPaper-欢迎注册</h1>
      </div>
     </div>
    </div>
   </div>
   
   <div class="section">
    <div class="container">
     <div class="row">
      <div class="col-sm-6">
       <div class="basic-login">
         <form:form method="POST" modelAttribute="accountForm" class="form-signin">
		
			<spring:bind path="userName">
				<div class="form-group ${status.error? 'has-error': ''}">
					<label for="register-username"><i class="icon-user"></i><b>用户名</b></label><span class="required">*</span>
					<form:input path="userName" type="text" class="form-control" placeholder="您的登录名和账户名"
								autofocus="true" />
					<form:errors path="userName"></form:errors>
				</div>
			</spring:bind>
			
			<spring:bind path="password">
				<div class="form-group ${status.error? 'has-error': ''}">
					<label for="register-password"><i class="icon-lock"></i><b>设置密码</b></label><span class="required">*</span>
					<form:input path="password" type="password" class="form-control" placeholder="至少8位不超过32位"/>
					<form:errors path="password"></form:errors>
				</div>
			</spring:bind>
			
			<spring:bind path="passwordConfirm">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label for="register-password2"><i class="icon-lock"></i><b>确认密码</b></label><span class="required">*</span>
	                <form:input type="password" path="passwordConfirm" class="form-control"
	                            placeholder="请再次输入密码"></form:input>
	                <form:errors path="passwordConfirm"></form:errors>
	            </div>
			</spring:bind>
			
			<spring:bind path="telphone">
				<div class="form-group ${status.error? 'has-error': ''}">
					<label for="register-username"><i class="icon-user"></i><b>联系方式</b></label><span class="required">*</span>
					<form:input path="telphone" type="text" class="form-control" placeholder="您的常用联系方式"
								autofocus="true" />
					<form:errors path="telphone"></form:errors>
				</div>
			</spring:bind>
			
			<spring:bind path="email">
				<div class="form-group ${status.error? 'has-error': ''}">
					<label for="register-username"><i class="icon-user"></i><b>邮箱</b></label><span class="required">*</span>
					<form:input path="email" type="text" class="form-control" placeholder="您的常用邮箱"
								autofocus="true" />
					<form:errors path="email"></form:errors>
				</div>
			</spring:bind>
			
			<spring:bind path="researchPoint">
				<div class="form-group ${status.error? 'has-error': ''}">
					<label for="register-username"><i class="icon-user"></i><b>研究方向</b></label><span class="required">*</span>
					<form:input path="researchPoint" type="text" class="form-control" placeholder="您目前研究方向"
								autofocus="true" />
					<form:errors path="researchPoint"></form:errors>
				</div>
			</spring:bind>
			
			<spring:bind path="company">
				<div class="form-group ${status.error? 'has-error': ''}">
					<label for="register-username"><i class="icon-user"></i><b>所在公司</b></label><span class="required">*</span>
					<form:input path="company" type="text" class="form-control" placeholder="您目前所在公司名称"
								autofocus="true" />
					<form:errors path="company"></form:errors>
				</div>
			</spring:bind>
			
			<spring:bind path="demand">
				<div class="form-group ${status.error? 'has-error': ''}">
					<label for="register-username"><i class="icon-user"></i><b>您的需求描述</b></label>
					<form:input path="demand" type="text" class="form-control" placeholder="您目前希望借助varpaper所获得的需求"
								autofocus="true" />
					<form:errors path="demand"></form:errors>
				</div>
			</spring:bind>
			
			<form:hidden path="active" value="true" />
			<form:hidden path="userRole" value="EMPOLYEE" />
			
			<div class="form-group">
				<button class="btn pull-right" type="submit">立即注册</button>
				<a href="login">已有账号？请登录</a>
				<div class="clearfix"></div>
			</div>
		</form:form>
       </div>
     </div>
     <div class="col-sm-6 social-login">
      <div class="social-login">
       	<p><b>VarPaper愿景：</b></p>
       	<p>重新定义文献检索：专业知识 + 人工智能=所想即所得</p>
       	<p><b>VarPaper使命：</b></p>
       	<p>通过<font color="red">智能化PubMed</font>文献，构建生物医疗领域的超级搜索平台，即变异、基因、表型、药物的智能搜索引擎和关联数据库，为<font color="red">科研和临床</font>提供高效的<font color="red">精准检索和知识探索</font>服务。VarPaper被誉为科研与学术的天堂，关联分析与知识探索的优秀平台，服务于<font color="red">科研最开始一公里的选项目定课题和临床最后一公里的决策支持</font>。</p>
       	<p><b>VarPaper场景:</b></p>
       	<a><img src="web-resources/lib/img/scenary.png" width=100%></a>
       	<div class="not-member">
		 <p>VarPaper用到的部分数据库如下：</p>
		 <a><img src="web-resources/lib/img/db.png" width=100%></a>
		</div>
      </div>
     </div>
    </div>
   </div>
   <!-- Footer -->
   <div class="footer">
    <div class="row">
      <div class="col-sm-12">
        <div class="footer-copyright">
          &copy; 2018 varpaper. 版权所有，侵权必究.
        </div>
      </div>
    </div>
  </div>
  </div>
</body>
</html>