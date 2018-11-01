$(function () {
    var rows = 20,currentPage, pageCount; textLength=200;
    //回车搜索
    $("#searchVar").keydown(function (e) {
        if (e.keyCode == 13) {
            var varVal = $(this).val();
            var gene = $("#searchGene").val();
            var disease = $("#searchDisease").val();
            var chemical = $("#searchChemical").val();
            var param = {
                "var": varVal,
                "gene":gene,
                "disease":disease,
                "chemical": chemical,
                "rows": rows,
                "page": "1",
                "if_range": $("#ifSelect").children("option:selected").val(),
                "date_range": $("#dateSelect").children("option:selected").val(),
                "model_select": $("#modelSelect").children("option:selected").val()
            };

            if (varVal == "" && gene == "" && disease == "" && chemical == "" ) {
            	layer.msg("请输入搜索内容");
            }else{
            	currentPage = 1;
                GetSearch(param,currentPage,rows); //获取搜索结果及记录数、页数
                $("#toHidden")[0].style.display="none";
                $("#toShow")[0].style.display="";
            }
        }
    });
    $("#searchGene").keydown(function (e) {
        if (e.keyCode == 13) {
            var gene = $(this).val();
            var varVal = $("#searchVar").val();
            var disease = $("#searchDisease").val();
            var chemical = $("#searchChemical").val();
            var param = {
        		"var": varVal,
                "gene":gene,
                "disease":disease,
                "chemical": chemical,
            	"rows": rows,
                "page": "1",
                "if_range": $("#ifSelect").children("option:selected").val(),
                "date_range": $("#dateSelect").children("option:selected").val(),
                "model_select": $("#modelSelect").children("option:selected").val()
            };

            if (varVal == "" && gene == "" && disease == "" && chemical == "" ) {
            	layer.msg("请输入搜索内容");
            }else{
            	currentPage = 1;
                GetSearch(param,currentPage,rows); //获取搜索结果及记录数、页数
                $("#toHidden")[0].style.display="none";
                $("#toShow")[0].style.display="";
            }
        }
    });
    $("#searchDisease").keydown(function (e) {
        if (e.keyCode == 13) {
            var disease = $(this).val();
            var varVal = $("#searchVar").val();
            var gene = $("#searchGene").val();
            var chemical = $("#searchChemical").val();
            var param = {
        		"var": varVal,
                "gene":gene,
                "disease":disease,
                "chemical": chemical, 
            	"rows": rows,
                "page": "1",
                "if_range": $("#ifSelect").children("option:selected").val(),
                "date_range": $("#dateSelect").children("option:selected").val(),
                "model_select": $("#modelSelect").children("option:selected").val()
            };

            if (varVal == "" && gene == "" && disease == "" && chemical == "" ) {
            	layer.msg("请输入搜索内容");
            }else{
            	currentPage = 1;
                GetSearch(param,currentPage,rows); //获取搜索结果及记录数、页数
                $("#toHidden")[0].style.display="none";
                $("#toShow")[0].style.display="";
            }
        }
    });
    $("#searchChemical").keydown(function (e) {
        if (e.keyCode == 13) {
            var chemical = $(this).val();
            var varVal = $("#searchVar").val();
            var gene = $("#searchGene").val();
            var disease = $("#searchDisease").val();
            var param = {
        		"var": varVal,
                "gene":gene,
                "disease":disease,
                "chemical": chemical,
            	"rows": rows,
                "page": "1",
                "if_range": $("#ifSelect").children("option:selected").val(),
                "date_range": $("#dateSelect").children("option:selected").val(),
                "model_select": $("#modelSelect").children("option:selected").val()
            };

            if (varVal == "" && gene == "" && disease == "" && chemical == "" ) {
            	layer.msg("请输入搜索内容");
            }else{
            	currentPage = 1;
                GetSearch(param,currentPage,rows); //获取搜索结果及记录数、页数
                $("#toHidden")[0].style.display="none";
                $("#toShow")[0].style.display="";
            }
        }
    });
    
    //搜索按钮
    $("#submit").click(function (e) {
        var disease = $("#searchDisease").val();
        var varVal = $("#searchVar").val();
        var gene = $("#searchGene").val();
        var chemical = $("#searchChemical").val();
        var param = {
    		"var": varVal,
            "gene":gene,
            "disease":disease,
            "chemical": chemical, 
        	"rows": rows,
            "page": "1",
            "if_range": $("#ifSelect").children("option:selected").val(),
            "date_range": $("#dateSelect").children("option:selected").val(),
            "model_select": $("#modelSelect").children("option:selected").val()
        };

        if (varVal == "" && gene == "" && disease == "" && chemical == "" ) {
        	layer.msg("请输入搜索内容");
        }else{
        	currentPage = 1;
            GetSearch(param,currentPage,rows); //获取搜索结果及记录数、页数
            $("#toHidden")[0].style.display="none";
            $("#toShow")[0].style.display="";
        }
    });
    
    $(".leftArrow").unbind("click").click(function(){
    	ClickLeftArrow(rows);
    })
    
    //下载按钮
    $("#download").click(function (e) {
        var disease = $("#searchDisease").val();
        var varVal = $("#searchVar").val();
        var gene = $("#searchGene").val();
        var chemical = $("#searchChemical").val();
        var param = {
    		"var": varVal,
            "gene":gene,
            "disease":disease,
            "chemical": chemical, 
            "if_range": $("#ifSelect").children("option:selected").val(),
            "date_range": $("#dateSelect").children("option:selected").val(),
            "model_select": $("#modelSelect").children("option:selected").val()
        };

        if (varVal == "" && gene == "" && disease == "" && chemical == "" ) {
        	layer.msg("请输入下载内容");
        }else{
            download(param); //下载内容
        }
    });
});

//获取查询文章结果及总记录数
function GetSearch(param,currentPage,rows) {
    $.ajax({
        url: "/webes/api/v1/pmid2title/publications",
        contentType: "application/json; charset=utf-8",
        type: "post",
        dataType: "json",
        data:  JSON.stringify(param),
        success: function (data) {
        	
        	$(".count").text(data.data.count);//count
            pageCount = Math.ceil(data.data.count/rows);

            $(".pageCount").text(pageCount);//pageCount总页数
            //只有一页的情况给箭头添加不能点击的样式
		    if(pageCount == 1){
		        $(".leftArrow").addClass("arrowEnable");
		        $(".rightArrow").addClass("arrowEnable");
		    }else{
		        $(".leftArrow").addClass("arrowEnable");
		    }
        	
        	console.log(currentPage);
            setData(data,currentPage,rows);
        },
        error: function () {

        }
    })
}

//下载内容
function download(param) {
    $.ajax({
        url: "/webes/api/v1/batch/var2title",
        contentType: "application/json; charset=utf-8",
        type: "post",
        dataType: "json",
        data:  JSON.stringify(param),
        success: function (data) {
            getFile(data);
        },
        error: function () {

        }
    })
}

//获取文件
function getFile(data) {
	var flag = data.flag;
	if(flag== "limit15") {
		layer.msg("您24小时内搜索次数上限为15次，目前已达上限");
	} else if(flag=="not enterprise") {
		layer.msg("您不是企业级用户");
	} else {
		downloadObjectAsJson(data, "varpaper");
	}
   
}

function downloadObjectAsJson(exportObj, exportName){
    var blob = new Blob([json2table(exportObj)], {
 	   type: "text/json;charset=utf-8,"
    });
    var link = document.createElement('a');
    link.href = window.URL.createObjectURL(blob);
    link.download = exportName;
    link.click();
    window.URL.revokeObjectURL(link.href);
  
}

function json2table(json) {
	if(json.length == 0) {
		return '';
	}
	var cols = Object.keys(json[0]);
	var headerRow = '';
	var bodyRow = '';
	
	cols.map(function(col) {
		headerRow += col + '	';
	});
	
	json.map(function(row) {
		cols.map(function(colName) {
			bodyRow += row[colName] + '	';
		});
		bodyRow += '\r\n';
	});
	
	return headerRow + '\r\n' + bodyRow;
	
}

/*//获取总记录
function GetCount(paramCount,rows){
    $.ajax({
        url: "/webcore/api/v1/pmid2title/publications",
        contentType: "application/json; charset=utf-8",
        type: "post",
        dataType: "json",
        data: JSON.stringify(paramCount),
        success: function (data) {
//            var data = {
//                "data": {
//                    "count": 30
//                }
//            };
       $(".count").text(data.data.count);//count
             pageCount = Math.ceil(data.data.count/rows);

       $(".pageCount").text(pageCount);//pageCount总页数
            //只有一页的情况给箭头添加不能点击的样式
         if(pageCount == 1){
             $(".leftArrow").addClass("arrowEnable");
             $(".rightArrow").addClass("arrowEnable");
         }else{
             $(".leftArrow").addClass("arrowEnable");
         }
        },
        error: function () {

        }
    })

}*/

//切换page获取数据
function GetPage(data,currentPage,rows){
    $.ajax({
        url: "/webes/api/v1/pmid2title/publications",
        contentType: "application/json; charset=utf-8",
        type: "post",
        dataType: "json",
        data:  JSON.stringify(data),
        success: function (data) {
            setData(data,currentPage,rows);
        },
        error: function () {

        }
    })
}

//设置查询文章结果
function setData(data,currentPage,rows) {
	console.log(data);
	console.log(currentPage);
	var flag = data.data.flag;
	if(flag== "limit15") {
		layer.msg("您24小时内搜索次数上限为15次，目前已达上限");
	} else {
	    data = data.data;
//	    JSON.parse(data);
	    console.log(data.titleList);
	    var sHtml = '';
	    for (var i = 0; i < data.titleList.length; i++) {
	        var high_light = data.titleList[i].highLight;
	        var lightStr ="";
	        for(var j=0;j<high_light.length;j++){
	            lightStr+=high_light[j]+"!";
	        }
	        
	        var high_context = data.titleList[i].lightContext;
	        var ssHtml = '<br/><br/>';
	        for(var k=0; k<high_context.length;k++){
	        	ssHtml +=
	        		"<span class='contextHighLight'>" + (k+1) + ". " + high_context[k] + "</span><br/><br/>";
	        }
	        
	        var context = data.titleList[i].context;
	        sHtml += "<li> " +
	            "<span class='listNum'>"+((parseInt(currentPage) - 1) * parseInt(rows) + 1+i)+"</span>" +
	            "<p class='pMargin title'>" + data.titleList[i].title + "</p>" +
	            " <p class='pMargin'>" +
	            "<span class='pmid'><i class='fa fa-external-link'></i><a target='_blank' href='https://www.ncbi.nlm.nih.gov/pubmed/" + data.titleList[i].pmid  + "'>PMID:" + data.titleList[i].pmid +"</a></span>" +
	            "<span class='pmcid'><i class='fa fa-external-link'></i><a target='_blank' href='https://www.ncbi.nlm.nih.gov/pmc/articles/" + data.titleList[i].pmcid  + "'>PMCID:" + data.titleList[i].pmcid +"</a></span>"+
	            "<span class='doi'>doi:"+data.titleList[i].doi+"</span>"+
	            "<span class='author'>authors: "+data.titleList[i].authorAddress+"</span>" +
	            " </p>" +
	            " <p class='pMargin'>" +
	            "<span><i class='fa fa-external-link'></i><a target='_blank' href='http://sci-hub.tw/" + data.titleList[i].doi + "'>全文在线预览DOI:" + data.titleList[i].doi + "</a></span>" +
	            "<span><i class='fa fa-external-link'></i><a target='_blank' href='http://libgen.io/scimag/index.php?s=" + data.titleList[i].pmid + "'>全文下载PMID:" + data.titleList[i].pmid + "</a></span>" +
	            " </p>"
	            		+
	            " <p class='pMargin secondP'>" +
	            "<span>Journal: "+data.titleList[i].journal+"</span>" +
	            "<span>issn: "+data.titleList[i].issn+"</span>" +
	            "<span>IF: "+data.titleList[i].IF+"</span>" +
	            "<span>casCategory: "+data.titleList[i].casCategory+"</span>" +
	            "<span>casIndex: "+data.titleList[i].casIndex+"</span>" +
	            "<span class='pubDate'>pubDate:&nbsp;<i class='fa fa-calendar'></i> "+data.titleList[i].pubDate+"</span>" +
	            "<span class='lightStr' style='display:none'>"+lightStr+"</span>" +
	            " </p>"
	        if(context.length >textLength ){
	            sHtml +=   " <p class='contextP'>" +
	                "<span class='context'>"+context.substr(0,textLength)+ "</span>" +
	                "<span class='contextTwo'>"+context.substr(parseInt(textLength)+1) +"</span>"  +
	                "<span class='contextThree'>" + ssHtml + "</span>" +
	                "<span class='contextElli'>...</span>" +
	                "<span class='seeMore'>more</span>"+
	                "</p>" +
	                "</li>"
	        } else{
	            sHtml +=  " <p class='contextP'>" +
	                "<span class='context'>"+context.substr(0,textLength)+ ssHtml +"</span>" +
	                "</p>" +
	                "</li>"
	        }
	    }
	    
	    $(".resultUl").empty().append(sHtml);

	    //设置页面数字
	    $(".startCount").text((parseInt(currentPage) - 1) * parseInt(rows) + 1);
	    $(".endCount").text((parseInt(currentPage) - 1) * parseInt(rows)+ data.titleList.length);
	    $(".currentPage").text(currentPage);

	    mark();
	    
	    $(".seeMore").click(function(){
	        if($(this).text()=="more"){
	           $(this).siblings(".contextTwo").addClass("showMore");
	           $(this).siblings(".contextThree").addClass("showMore");
	            $(this).text("less");
	            $(this).siblings(".contextElli").hide();
	        }else{
	            $(this).siblings(".contextTwo").removeClass("showMore");
	            $(this).siblings(".contextThree").removeClass("showMore");
	            $(this).text("more");
	            $(this).siblings(".contextElli").show();
	        }
	       });
	    
	    //下一页点击事件
	    $(".rightArrow").unbind("click").click(function(){
	        //最后一页不能点
	        currentPage = parseInt($(".currentPage").text());
	        pageCount = $(".pageCount").text();
	          if(currentPage ==pageCount || currentPage == 100){
	               return false;
	          }
	        currentPage++;
	        if(currentPage ==pageCount || currentPage == 100){
	            $(".rightArrow").addClass("arrowEnable");
	        }else{
	            $(".rightArrow").removeClass("arrowEnable");
	        };
	        if(currentPage !=1){
	        	  $(".leftArrow").removeClass("arrowEnable");
	        };
	        var param = {
	    		"var": $("#searchVar").val(),
	            "gene":$("#searchGene").val(),
	            "disease":$("#searchDisease").val(),
	            "chemical": $("#searchChemical").val(),
	        	"rows": rows,
	            "page":currentPage,
	            "if_range": $("#ifSelect").children("option:selected").val(),
	            "date_range": $("#dateSelect").children("option:selected").val(),
                "model_select": $("#modelSelect").children("option:selected").val()
	        };
	        GetPage(param,currentPage,rows);
	    });
	}
   
}

function ClickLeftArrow(rows){
	 //第一页不能点
   currentPage = parseInt($(".currentPage").text());
    if(currentPage ==1){
        return false;
    }
    currentPage--;
    if(currentPage == 1){
        $(".leftArrow").addClass("arrowEnable");
    }else{
        $(".leftArrow").removeClass("arrowEnable");
    };
    var param = {
		"var": $("#searchVar").val(),
        "gene":$("#searchGene").val(),
        "disease":$("#searchDisease").val(),
        "chemical": $("#searchChemical").val(),
    	"rows": rows,
        "page": currentPage,
        "if_range": $("#ifSelect").children("option:selected").val(),
        "date_range": $("#dateSelect").children("option:selected").val(),
        "model_select": $("#modelSelect").children("option:selected").val()
    };
    GetPage(param,currentPage,rows);
}


function mark() {
    var options = {
        "className": "match"
    };
    var lis = $(".resultUl").find("li");
    for(var i=0;i<lis.length;i++){
        var lightStrArr = $(lis[i]).find(".lightStr").text().split("!");
        lightStrArr.pop();
        console.log(lightStrArr);
        var contextP =   $(lis[i]).find(".contextP");
        $(contextP).removeMark();
        $(contextP).mark(lightStrArr, options);
    }
}

$(document).ready(function(){
	
	var rows = 20;
	$("#ifSelect").change(function(){
		var ifVal = $(this).children("option:selected").val();
		var dateVal = $("#dateSelect").children("option:selected").val();
		var modelVal = $("#modelSelect").children("option:selected").val();
		var varVal = $("#searchVar").val();
        var gene = $("#searchGene").val();
        var disease = $("#searchDisease").val();
        var chemical = $("#searchChemical").val();
		var param = {
				"var":  varVal,
	            "gene": gene,
	            "disease": disease,
	            "chemical": chemical,
	            "rows": rows,
	            "page":"1",
	            "if_range": ifVal,
	            "date_range": dateVal,
	            "model_select": modelVal
	        };
		if (varVal == "" && gene == "" && disease == "" && chemical == "" ) {
        	layer.msg("请输入搜索内容");
        }else{
        	currentPage = 1;
            GetSearch(param,currentPage,rows); //获取搜索结果及记录数、页数
        }
		
	})
	
	$("#dateSelect").change(function(){
		var ifVal = $("#ifSelect").children("option:selected").val();
		var dateVal = $(this).children("option:selected").val();
		var modelVal = $("#modelSelect").children("option:selected").val();
		var varVal = $("#searchVar").val();
        var gene = $("#searchGene").val();
        var disease = $("#searchDisease").val();
        var chemical = $("#searchChemical").val();
		var param = {
				"var":  varVal,
	            "gene": gene,
	            "disease": disease,
	            "chemical": chemical,
				"rows": rows,
	            "page":"1",
	            "if_range": ifVal,
	            "date_range": dateVal,
	            "model_select": modelVal
	        };
		if (varVal == "" && gene == "" && disease == "" && chemical == "" ) {
        	layer.msg("请输入搜索内容");
        }else{
        	currentPage = 1;
            GetSearch(param,currentPage,rows); //获取搜索结果及记录数、页数
        }
		
	})
	$("#modelSelect").change(function(){
		var ifVal = $("#ifSelect").children("option:selected").val();
		var dateVal = $("#dateSelect").children("option:selected").val();
		var modelVal = $(this).children("option:selected").val();
		var varVal = $("#searchVar").val();
        var gene = $("#searchGene").val();
        var disease = $("#searchDisease").val();
        var chemical = $("#searchChemical").val();
		var param = {
				"var":  varVal,
	            "gene": gene,
	            "disease": disease,
	            "chemical": chemical,
				"rows": rows,
	            "page":"1",
	            "if_range": ifVal,
	            "date_range": dateVal,
	            "model_select": modelVal
	        };
		if (varVal == "" && gene == "" && disease == "" && chemical == "" ) {
        	layer.msg("请输入搜索内容");
        }else{
        	currentPage = 1;
            GetSearch(param,currentPage,rows); //获取搜索结果及记录数、页数
        }
		
	})
	//var example
	$(".var1").click(function() {
		var param = $(".var1").text();
		addVar(param);
	})
	$(".var2").click(function() {
		var param = $(".var2").text();
		addVar(param);
	})
	$(".var3").click(function() {
		var param = $(".var3").text();
		addVar(param);
	})
	$(".var4").click(function() {
		var param = $(".var4").text();
		addVar(param);
	})
	$(".var5").click(function() {
		var param = $(".var5").text();
		addVar(param);
	})
	$(".var6").click(function() {
		var param = $(".var6").text();
		addVar(param);
	})
	$(".var7").click(function() {
		var param = $(".var7").text();
		addVar(param);
	})
	$(".var8").click(function() {
		var param = $(".var8").text();
		addVar(param);
	})

	function addVar(varParam) {
		var param = {
				"var":  varParam,
	            "gene": "",
	            "disease": "",
	            "chemical": "",
				"rows": rows,
	            "page":"1",
	            "if_range": "all",
	            "date_range": "all",
	            "model_select": "all"
	        };
		$("#searchVar").val(varParam);
		currentPage = 1;
        GetSearch(param,currentPage,rows); //获取搜索结果及记录数、页数
        $("#toHidden")[0].style.display="none";
        $("#toShow")[0].style.display="";
	}
	
	//gene example
	$(".gene1").click(function() {
		var param = $(".gene1").text();
		addGene(param);
	})
	$(".gene2").click(function() {
		var param = $(".gene2").text();
		addGene(param);
	})

	$(".gene4").click(function() {
		var param = $(".gene4").text();
		addGene(param);
	})

	function addGene(varParam) {
		var param = {
				"var":  "",
	            "gene": varParam,
	            "disease": "",
	            "chemical": "",
				"rows": rows,
	            "page":"1",
	            "if_range": "all",
	            "date_range": "all",
	            "model_select": "all"
	        };
		$("#searchGene").val(varParam);
		currentPage = 1;
        GetSearch(param,currentPage,rows); //获取搜索结果及记录数、页数
        $("#toHidden")[0].style.display="none";
        $("#toShow")[0].style.display="";
	}
	
	//disease example
	$(".disease1").click(function() {
		var param = $(".disease1").text();
		addDisease(param);
	})
	$(".disease2").click(function() {
		var param = $(".disease2").text();
		addDisease(param);
	})
	$(".disease3").click(function() {
		var param = $(".disease3").text();
		addDisease(param);
	})
	$(".disease4").click(function() {
		var param = $(".disease4").text();
		addDisease(param);
	})
	$(".disease5").click(function() {
		var param = $(".disease5").text();
		addDisease(param);
	})
	$(".disease6").click(function() {
		var param = $(".disease6").text();
		addDisease(param);
	})
	$(".disease7").click(function() {
		var param = $(".disease7").text();
		addDisease(param);
	})
	$(".disease8").click(function() {
		var param = $(".disease8").text();
		addDisease(param);
	})
	$(".disease9").click(function() {
		var param = $(".disease9").text();
		addDisease(param);
	})
	$(".disease10").click(function() {
		var param = $(".disease10").text();
		addDisease(param);
	})
	$(".disease11").click(function() {
		var param = $(".disease11").text();
		addDisease(param);
	})
	$(".disease12").click(function() {
		var param = $(".disease12").text();
		addDisease(param);
	})
	$(".disease13").click(function() {
		var param = $(".disease13").text();
		addDisease(param);
	})
	$(".disease14").click(function() {
		var param = $(".disease14").text();
		addDisease(param);
	})

	function addDisease(varParam) {
		var param = {
				"var":  "",
	            "gene": "",
	            "disease": varParam,
	            "chemical": "",
				"rows": rows,
	            "page":"1",
	            "if_range": "all",
	            "date_range": "all",
	            "model_select": "all"
	        };
		$("#searchDisease").val(varParam);
		currentPage = 1;
        GetSearch(param,currentPage,rows); //获取搜索结果及记录数、页数
        $("#toHidden")[0].style.display="none";
        $("#toShow")[0].style.display="";
	}
	
	//chemical example
	$(".chemical1").click(function() {
		var param = $(".chemical1").text();
		addChemical(param);
	})
	$(".chemical2").click(function() {
		var param = $(".chemical2").text();
		addChemical(param);
	})
	$(".chemical3").click(function() {
		var param = $(".chemical3").text();
		addChemical(param);
	})
	$(".chemical4").click(function() {
		var param = $(".chemical4").text();
		addChemical(param);
	})
	$(".chemical5").click(function() {
		var param = $(".chemical5").text();
		addChemical(param);
	})
	$(".chemical6").click(function() {
		var param = $(".chemical6").text();
		addChemical(param);
	})
	$(".chemical7").click(function() {
		var param = $(".chemical7").text();
		addChemical(param);
	})
	$(".chemical8").click(function() {
		var param = $(".chemical8").text();
		addChemical(param);
	})
	$(".chemical9").click(function() {
		var param = $(".chemical9").text();
		addChemical(param);
	})
	$(".chemical10").click(function() {
		var param = $(".chemical10").text();
		addChemical(param);
	})
	$(".chemical11").click(function() {
		var param = $(".chemical11").text();
		addChemical(param);
	})

	function addChemical(varParam) {
		var param = {
				"var":  "",
	            "gene": "",
	            "disease": "",
	            "chemical": varParam,
				"rows": rows,
	            "page":"1",
	            "if_range": "all",
	            "date_range": "all",
	            "model_select": "all"
	        };
		$("#searchChemical").val(varParam);
		currentPage = 1;
        GetSearch(param,currentPage,rows); //获取搜索结果及记录数、页数
        $("#toHidden")[0].style.display="none";
        $("#toShow")[0].style.display="";
	}
	
})
