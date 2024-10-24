package cn.granitech.business.plugins.trigger;

import cn.granitech.util.SpringHelper;

import java.util.HashMap;
import java.util.Map;

public class FunctionHelper {
    private static final Map<String, FunctionLambda> functionMap = new HashMap<>();

    public FunctionHelper() {
    }

    public static Map<String, FunctionLambda> getFunctionMap() {
        return functionMap;
    }

    static {
        functionMap.put("打印日志", (paramMap) -> SpringHelper.getBean(FunctionService.class).callBackLog(paramMap));
    }
}
