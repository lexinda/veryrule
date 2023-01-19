package com.lexinda.veryrule.platform.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum VeryRuleElementGroupEnum {

	NODE("node", "节点"),
	DATA("date", "数据");

    private String code;
    private String name;

    VeryRuleElementGroupEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static List<Map<String, Object>> toList() {
    	VeryRuleElementGroupEnum[] enumList = VeryRuleElementGroupEnum.values();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (VeryRuleElementGroupEnum e : enumList) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", e.code);
            map.put("name", e.name);
            list.add(map);
        }
        return list;
    }

    public static String getName(String code) {
        if (code == null) {
            return "";
        }
        VeryRuleElementGroupEnum[] enumList = VeryRuleElementGroupEnum.values();
        for (VeryRuleElementGroupEnum e : enumList) {
            if (e.code.equals(code)) {
                return e.name;
            }
        }
        return "";
    }
    
    public static String getCode(String name) {
        if (name == null) {
            return "";
        }
        VeryRuleElementGroupEnum[] enumList = VeryRuleElementGroupEnum.values();
        for (VeryRuleElementGroupEnum e : enumList) {
            if (e.name.equals(name)) {
                return e.code;
            }
        }
        return "";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
	
}
