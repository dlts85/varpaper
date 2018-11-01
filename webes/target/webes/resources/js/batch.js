$(function () {
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
                "if_range": $("#ifSelect").children("option:selected").val(),
                "date_range": $("#dateSelect").children("option:selected").val(),
                "model_select": $("#modelSelect").children("option:selected").val()
            };

            if (varVal == "" && gene == "" && disease == "" && chemical == "" ) {
            	layer.msg("请输入搜索内容");
            }else{
                download(param); //下载内容
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
                "if_range": $("#ifSelect").children("option:selected").val(),
                "date_range": $("#dateSelect").children("option:selected").val(),
                "model_select": $("#modelSelect").children("option:selected").val()
            };

            if (varVal == "" && gene == "" && disease == "" && chemical == "" ) {
            	layer.msg("请输入搜索内容");
            }else{
                download(param); //下载内容
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
                "if_range": $("#ifSelect").children("option:selected").val(),
                "date_range": $("#dateSelect").children("option:selected").val(),
                "model_select": $("#modelSelect").children("option:selected").val()
            };

            if (varVal == "" && gene == "" && disease == "" && chemical == "" ) {
            	layer.msg("请输入搜索内容");
            }else{
                download(param); //下载内容
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
                "if_range": $("#ifSelect").children("option:selected").val(),
                "date_range": $("#dateSelect").children("option:selected").val(),
                "model_select": $("#modelSelect").children("option:selected").val()
            };

            if (varVal == "" && gene == "" && disease == "" && chemical == "" ) {
            	layer.msg("请输入搜索内容");
            }else{
                download(param); //下载内容
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
            "if_range": $("#ifSelect").children("option:selected").val(),
            "date_range": $("#dateSelect").children("option:selected").val(),
            "model_select": $("#modelSelect").children("option:selected").val()
        };

        if (varVal == "" && gene == "" && disease == "" && chemical == "" ) {
        	layer.msg("请输入搜索内容");
        }else{
            download(param); //下载内容
        }
    });
});

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
    var blob = new Blob([JSON.stringify(exportObj)], {
 	   type: "text/json;charset=utf-8,"
    });
    var link = document.createElement('a');
    link.href = window.URL.createObjectURL(blob);
    link.download = exportName;
    link.click();
    window.URL.revokeObjectURL(link.href);
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
	            "if_range": ifVal,
	            "date_range": dateVal,
	            "model_select": modelVal
	        };
		if (varVal == "" && gene == "" && disease == "" && chemical == "" ) {
        	layer.msg("请输入搜索内容");
        }else{
        	download(param); //下载内容
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
	            "if_range": ifVal,
	            "date_range": dateVal,
	            "model_select": modelVal
	        };
		if (varVal == "" && gene == "" && disease == "" && chemical == "" ) {
        	layer.msg("请输入搜索内容");
        }else{
        	download(param); //下载内容
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
	            "if_range": ifVal,
	            "date_range": dateVal,
	            "model_select": modelVal
	        };
		if (varVal == "" && gene == "" && disease == "" && chemical == "" ) {
        	layer.msg("请输入搜索内容");
        }else{
        	download(param); //下载内容
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
	            "if_range": "all",
	            "date_range": "all",
	            "model_select": "all"
	        };
		$("#searchVar").val(varParam);
    	download(param); //下载内容
	}
	
	//gene example
	$(".gene1").click(function() {
		var param = $(".gene1").text();
		addGene(param);
	})
	/*$(".gene2").click(function() {
		var param = $(".gene2").text();
		addGene(param);
	})
	$(".gene3").click(function() {
		var param = $(".gene3").text();
		addGene(param);
	})*/
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
	            "if_range": "all",
	            "date_range": "all",
	            "model_select": "all"
	        };
		$("#searchGene").val(varParam);
    	download(param); //下载内容
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
	            "if_range": "all",
	            "date_range": "all",
	            "model_select": "all"
	        };
		$("#searchDisease").val(varParam);
    	download(param); //下载内容
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
	            "if_range": "all",
	            "date_range": "all",
	            "model_select": "all"
	        };
		$("#searchChemical").val(varParam);
    	download(param); //下载内容
	}
	
})
