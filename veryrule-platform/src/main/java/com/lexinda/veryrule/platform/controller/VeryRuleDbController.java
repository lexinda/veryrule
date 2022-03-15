package com.lexinda.veryrule.platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lexinda.veryrule.platform.service.mybatis.VeryRuleDbMbService;

/**
* @author zhumengle
* @version 创建时间：2022-3-1 16:17:30
* 类说明
*/
@RestController
@RequestMapping("/veryrule")
public class VeryRuleDbController {
	
	@Autowired
	public VeryRuleDbMbService veryRuleDbService;

	@RequestMapping("/initDb")
	public String initDb() {
		veryRuleDbService.createVeryRuleElement();
		veryRuleDbService.createVeryRuleFlow();
		veryRuleDbService.createVeryRuleFlowTemplet();
		veryRuleDbService.createVeryRuleFlowTempletLog();
		return "success";
	}
	
	@RequestMapping("/createVeryRuleElement")
	public String createVeryRuleElement() {
		veryRuleDbService.createVeryRuleElement();
		return "success";
	}
	
	@RequestMapping("/createVeryRuleFlow")
	public String createVeryRuleFlow() {
		veryRuleDbService.createVeryRuleFlow();
		return "success";
	}
	
	@RequestMapping("/createVeryRuleFlowTemplet")
	public String createVeryRuleFlowTemplet() {
		veryRuleDbService.createVeryRuleFlowTemplet();
		return "success";
	}
	
	@RequestMapping("/createVeryRuleFlowTempletLog")
	public String createVeryRuleFlowTempletLog() {
		veryRuleDbService.createVeryRuleFlowTempletLog();
		return "success";
	}
	
}
