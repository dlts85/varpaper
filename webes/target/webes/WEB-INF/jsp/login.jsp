<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>


<html lang="cn">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1; charset=UTF-8">
<title>欢迎登陆</title> 
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
	  	<h1>VarPaper-登陆</h1>
	  </div>
	 </div>
	</div>
   </div>
   
   <div class="section">
    <div class="container">
     <div class="row">
      <div class="col-sm-6">
       <div class="basic-login">
        <form method="POST" action="request_for_login" class="form-signin">
        <span>${message}</span>
		<div class="form-group ${error != null ? 'has-error' : ''}">
			<label for="login-username"><i class="icon-user"></i> <b>用户名</b></label>
			<input name="userName" type="text" class="form-control" placeholder="用户名"
					autofocus="true" />
		</div>
		<div class="form-group ${error != null ? 'has-error' : ''}">
			<label for="login-password"><i class="icon-lock"></i> <b>密码</b></label>
			<input name="password" type="password" class="form-control" placeholder="密码" />
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		</div>
		<span>${error}</span>
		<div class="form-group">
			<button class="btn pull-right" type="submit">登录</button>
			<a href="registration">还没有账号？立即注册</a>
			<div class="clearfix"></div>
		</div>
		</form>
       </div>
       <br/>
       <div class="social-login">
          <p>一、个人用户<font color="red">免费</font>，申请方法如下：</p>
        <div class="not-member">
          <p>1、个人用户请点击右上角的注册按钮进入注册页面进行注册</p>
          <p>2、请如实填写注册所需个人信息，注册成功后即可使用varpaper平台</p>      
        </div>
       </div>
       <div class="social-login">
          <p>二、企业用户：</p>
        <div class="not-member">
          <p>除了在线检索和过滤服务，还能<font color="red">调用api，批量下载</font>文献PMID信息，商务合作请咨询万先生、孙先生</p>
        </div>
        <div class="col-sm-6">
	       	<p>万先生：13661937357</p>
	       	<img src="web-resources/lib/img/wan.png" width="50%">
        </div>
        <div class="col-sm-6">
	       	<p>孙先生：18801902547</p>
	       	<img src="web-resources/lib/img/sun.png" width="50%">
        </div>
        <a>&nbsp;</a>
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

<script type="text/javascript" src='<c:url value="/web-resources/lib/jquery/jquery-2.0.0.min.js"/>'></script>
<script type="text/javascript"
		src='<c:url value="/web-resources/lib/bootstrap-3.3.6/js/bootstrap.min.js"/>'></script>
</body>
</html>