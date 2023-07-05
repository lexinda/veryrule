package com.lexinda.veryrule;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lexinda.veryrule.bo.RuleBo;

import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.OgnlOps;
import ognl.TypeConverter;

public class RuleTypeConverter  implements TypeConverter{

	@Override
	public Object convertValue(Map context, Object target, Member member, String propertyName, Object value,
			Class toType) {
		// TODO Auto-generated method stub
		Object result = null;
		if(value!=null) {
			if(value.getClass().equals(ArrayList.class)) {
				Class componentType = toType.getComponentType();
				List<String> valueList = (ArrayList)value;
                result = Array.newInstance(componentType, valueList.size());
                for(int i = 0, icount = valueList.size(); i < icount; i++) {
                	try {
						Object root = componentType.getDeclaredConstructor().newInstance();
						OgnlContext contextItem = (OgnlContext) Ognl.createDefaultContext(root);
						Object tree = Ognl.parseExpression(valueList.get(i).toString());
						Ognl.getValue(tree, contextItem,contextItem.getRoot());
						Array.set(result, i, root);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
			}
		}
		return result;
	}

}
