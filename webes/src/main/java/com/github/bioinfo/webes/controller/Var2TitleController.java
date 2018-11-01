package com.github.bioinfo.webes.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bioinfo.webes.service.Pmid2TitleService;

@Controller
public class Var2TitleController {
	
	/*public static void main(String[] args) {
		@SuppressWarnings("resource")
		ApplicationContext ac = new ClassPathXmlApplicationContext("servlet-context.xml");
		DemoInterface demo = (DemoInterface) ac.getBean("demo");
		System.out.println(demo.sayHelloWorld());
		
	}*/
	

	public static ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	Pmid2TitleService service;
	
	@RequestMapping(value={"api/v1/pmid2title/publications"}, method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getTitleAbstractByVar(@RequestBody Map<String, Object> jsonParams) {
		String var = "";
		if(null != jsonParams.get("var")) {
			var = jsonParams.get("var").toString(); //var
		}
		String gene = "";
		if(null != jsonParams.get("gene")) {
			gene = jsonParams.get("gene").toString();//gene
		}
		String disease = "";
		if(null != jsonParams.get("disease")) {
			disease = jsonParams.get("disease").toString();//disease
		}
		String chemical = "";
		if(null != jsonParams.get("chemical")) {
			chemical = jsonParams.get("chemical").toString();//chemical
		}
				
		Integer rows = Integer.parseInt(jsonParams.get("rows").toString());
		Integer page = Integer.parseInt(jsonParams.get("page").toString());
		String ifRange = jsonParams.get("if_range").toString();
		String dateRange = jsonParams.get("date_range").toString();
		String modelSelect = jsonParams.get("model_select").toString();
		
		Map<String, Object> result = new HashMap<String, Object>();

		Map<String, Object> entities = service.getTitleAbstractByPmid(var, gene, disease, chemical, rows, page, ifRange, dateRange, modelSelect);
		result.put("data", entities);
		
		return result;
		
	}
	
	@RequestMapping(value={"api/v1/batch/var2title"}, produces= "application/json",  method=RequestMethod.POST)
	@ResponseBody
	public Object getBatchTitleInfoByVar(@RequestBody Map<String, Object> jsonParams, HttpServletResponse response) {
				
		String var = "";
		if(null != jsonParams.get("var")) {
			var = jsonParams.get("var").toString(); //var
		}
		String gene = "";
		if(null != jsonParams.get("gene")) {
			gene = jsonParams.get("gene").toString();//gene
		}
		String disease = "";
		if(null != jsonParams.get("disease")) {
			disease = jsonParams.get("disease").toString();//disease
		}
		String chemical = "";
		if(null != jsonParams.get("chemical")) {
			chemical = jsonParams.get("chemical").toString();//chemical
		}
		String ifRange = jsonParams.get("if_range").toString();
		String dateRange = jsonParams.get("date_range").toString();
		String modelSelect = jsonParams.get("model_select").toString();
		
		Map<String, Object> tmp = service.getBatchTitleByVar(var, gene, disease, chemical, ifRange, dateRange, modelSelect);
		
		//只取核心数据部分
		return tmp.get("titleList");
				
	}
}
