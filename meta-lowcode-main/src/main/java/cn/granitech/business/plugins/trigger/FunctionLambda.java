package cn.granitech.business.plugins.trigger;

import java.util.Map;

@FunctionalInterface
public interface FunctionLambda {
    void execute(Map<String, Object> map);
}
