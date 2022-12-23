package com.lexinda.veryrule.platform.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum VeryRuleElementGroupEnum {

	GROUP_ONE(1, "组1"),
	GROUP_TWO(2, "组2");

    private int code;
    private String name;

    VeryRuleElementGroupEnum(int code, String name) {
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

    public static String getView(Integer code) {
        if (code == null) {
            return "";
        }
        VeryRuleElementGroupEnum[] enumList = VeryRuleElementGroupEnum.values();
        for (VeryRuleElementGroupEnum e : enumList) {
            if (e.code == code) {
                return e.name;
            }
        }
        return "";
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
	
}
