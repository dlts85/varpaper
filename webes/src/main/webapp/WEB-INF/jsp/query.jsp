<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
    <meta name="viewport" content="initial-scale=1, width=device-width, maximum-scale=1, user-scalable=no">
    <title>搜索</title>
    <link rel="stylesheet" href="/webes/resources/css/reset.css">
	<link href="https://cdn.bootcss.com/bootstrap/4.1.1/css/bootstrap.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.6.3/css/font-awesome.css" rel="stylesheet">
    <link rel="stylesheet" href="/webes/resources/css/layer.css">
    <link rel="stylesheet" href="/webes/resources/css/search.css">
    <link rel="stylesheet" href="/webes/resources/lib/css/main.css">

    <script src="http://code.jquery.com/jquery-2.1.1.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap/4.1.1/js/bootstrap.js"></script>
    <script type="text/javascript" src="/webes/resources/js/jquery.mark.min.js"></script>
    <script src="/webes/resources/js/layer.js"></script>
    <script src="/webes/resources/js/search.js"></script>
    <style>
        span.match {
            background: #ffcdd2;
            color:#f44336;
        }
    </style>
</head>
<body>
<div class="mainmenu-wrapper">
	<div class="container">
		<div class="menuextras">
        	<div class="extras">
           		<ul>
		           	<c:choose>
		           	 <c:when test="${sessionScope.user != null}">
		           	  <li><a href="#">欢迎您，<c:out value="${sessionScope.user}"></c:out></a></li>
		           	  <li><a href="logout">退出</a></li>
		           	 </c:when>
		           	 <c:otherwise>
		          	  <li><a href="login">登陆</a></li>
		           	 </c:otherwise>
		           	</c:choose>
            	</ul>
        	</div>
		</div>
       	<nav id="mainmenu" class="mainmenu">
	        <ul>
	          <li class="logo-wrapper"><a href="query"><img src="resources/lib/img/logo_new.png" alt="Multipurpose Twitter Bootstrap Template" height ="30" width="170"></img></a></li>
	          <li>
	            <a href="query">主页</a>
	            <!-- <a href="batch">批量下载</a> -->
	          </li>
	          <li>
	            <a><b>重新定义文献检索：专业知识+人工智能=所想即所得</b></a>
	          </li>
	        </ul>
       	</nav>
	</div>
</div>
<div class="nav">
    <div class="container">
        <div class="searchDiv">
            <input id="searchVar" placeholder="变异" type="text" style="height:25px;width:15%;vertical-align: bottom;">
            <input id="searchGene" placeholder="基因" type="text" style="height:25px;width:15%;vertical-align: bottom;">
            <input id="searchDisease" placeholder="表型" type="text" style="height:25px;width:15%;vertical-align: bottom;">
            <input id="searchChemical" placeholder="药物" type="text" style="height:25px;width:15%;vertical-align: bottom;">
            <button id="submit" class="icon" style="height:25px;width:4%;"><i class="fa fa-search"></i></button>
            <button id="download" class="icon" style="height:25px;width:4%;"><i class="fa fa-download"></i></button>
        </div>
        
    </div>
</div>
<div class="content">
	<div class="container" id="toHidden">
		<div class="row">
			<div class="try col-sm-3">
			<p>变异 </p><p>Try:</p>
			<p><span class="var1">g.123274794T>C</span></p><p><span class="var2">rs113488022</span></p><p><span class="var3">c.313A>G</span></p><p><span class="var4">p.Asp110Tyr</span></p><p><span class="var5">c.474dupC</span></p><p><span class="var6">n.174059232G>C</span></p><p><span class="var7">COSM5048645</span></p><p><span class="var8">217308</span>(clinvarID)</p>
			</div>
			<div class="try col-sm-3">
			<p>基因</p><p>Try:</p>
			<p><span class="gene1">EGFR</span></p><!-- <p><span class="gene4">1956</span></p> --><p><span class="gene2">PRRT2;KCTD13;TBX6</span></p>
			</div>
			<div class="try col-sm-3">
			<p>表型</p><p>Try:</p>
			<p><span class="disease1">MESH:D000741</span></p><p><span class="disease6">HP:0012327</span></p><p><span class="disease7">DOID:10488</span></p><p><span class="disease8">UMLS:C0003466</span></p><!-- <p><span class="disease9">SNOMEDCT_US:204712000</span></p> --><p><span class="disease10">OMIM:207500</span></p><!-- <p><span class="disease11">GTR:AN0098163</span></p> --><p><span class="disease12">ORDO:AN0510403</span></p><p><span class="disease13">NBK11167</span></p><p><span class="disease14">16p11.2</span></p><p><span class="disease3">coronary disease</span></p><p><span class="disease4">Fever</span></p><p><span class="disease5">Fatigue;anemia</span></p>
			</div>
			<div class="try col-sm-3">
			<p>药物</p><p>Try:</p>
			<p><span class="chemical1">MESH:D000432</span></p><p><span class="chemical6">CasRN:53-59-8</span></p><p><span class="chemical7">DrugBank:DB00717</span></p><p><span class="chemical8">ChEBI:3897</span></p><p><span class="chemical9">PharmGKB:PA164779052</span></p><p><span class="chemical10">PubChem_sub:46506425</span></p><p><span class="chemical11">TTD:DAP001246</span></p><p><span class="chemical2">Digitoxin</span></p><p><span class="chemical3">Calcium</span></p><p><span class="chemical4">Methanol</span></p><p><span class="chemical5">Metformin</span></p>
			</div>
		</div>
	</div>
    <div class="container" id="toShow" style="display:none">
    	<div>
    	  <a>IF :</a>
    	  <select class="select" id="ifSelect">
    	    <option value="all">all</option>
    		<option value="3">>=3</option>
    		<option value="5">>=5</option>
    		<option value="10">>=10</option>
    		<option value="20">>=20</option>
    	  </select>
    	  <a>Date :</a>
    	  <select class="select" id="dateSelect">
    	    <option value="all">all</option>
    		<option value="1995-1999">1995-1999</option>
    		<option value="2000-2005">2000-2005</option>
    		<option value="2006-2010">2006-2010</option>
    		<option value="2011-2015">2011-2015</option>
    		<option value="2016-2020">2016-2020</option>
    	  </select>
    	  <a>Model :</a>
    	  <select class="select" id="modelSelect">
    	    <option value="all">all</option>
    		<option value="var">var</option>
    		<option value="gene">gene</option>
    		<option value="disease">disease</option>
    		<option value="chemical">drug</option>
    	  </select>
    	</div>
    		
        <p class="totalCount">Showing  <span class="startCount"></span> to <span class="endCount"></span> of <span class="count"></span> publications </p>
        <div class="selectPage">
            <span class="fa fa-chevron-left leftArrow" ></span>
            Page   <span class="currentPage"></span> of <span class="pageCount"></span>
            <span class="fa fa-chevron-right rightArrow"></span>
        </div>

        <ul class="resultUl context">
        </ul>
    
    </div>
</div>
</body>
</html>