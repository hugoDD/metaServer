package cn.granitech.trigger.business.trigger.aviator;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;

import java.math.BigDecimal;
import java.util.Map;


public class AmountsFunction
        extends AbstractFunction {
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
        Number amounts = FunctionUtils.getNumberValue(arg1, env);

        if (amounts.intValue() >= 10000) {
            return new AviatorString(BigDecimal.valueOf(amounts.doubleValue()).divide(new BigDecimal(10000)).stripTrailingZeros().toPlainString() + "万元");
        }
        return new AviatorString(BigDecimal.valueOf(amounts.doubleValue()).stripTrailingZeros().toPlainString() + "元");
    }

    public String getName() {
        return "amounts";
    }
}



