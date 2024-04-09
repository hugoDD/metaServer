package cn.granitech.trigger.business.trigger.aviator;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.math.BigDecimal;
import java.util.Map;


public class NumberRoundFunction
        extends AbstractFunction {
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        double num = FunctionUtils.getNumberValue(arg1, env).doubleValue();
        int newScale = FunctionUtils.getNumberValue(arg2, env).intValue();
        return AviatorDouble.valueOf((new BigDecimal(num)).setScale(newScale, 4).doubleValue());
    }

    public String getName() {
        return "number.round";
    }
}



