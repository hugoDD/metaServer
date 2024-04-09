package cn.granitech.business.plugins.trigger;

import java.util.Map;

public class FunctionHelper {
    private static Map<String, FunctionLambda> functionMap;

    public static Map<String, FunctionLambda> getFunctionMap() {
        return functionMap;
    }
}
