package cn.granitech.trigger.business.trigger.aviator;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Options;
import com.googlecode.aviator.exception.CompileExpressionErrorException;
import com.googlecode.aviator.exception.ExpressionSyntaxErrorException;
import com.googlecode.aviator.runtime.type.AviatorFunction;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Map;


public class AviatorUtils {
    private static final AviatorEvaluatorInstance AVIATOR = AviatorEvaluator.newInstance();


    static {
        AVIATOR.setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, Boolean.TRUE);
        AVIATOR.setOption(Options.ENABLE_PROPERTY_SYNTAX_SUGAR, Boolean.FALSE);
//        AVIATOR.setOption(Options.ALLOWED_CLASS_SET, Collections.emptySet());
        AVIATOR.setOption(Options.TRACE_EVAL, Boolean.FALSE);


        try {
            AVIATOR.addStaticFunctions("StringUtils", StringUtils.class);
            AVIATOR.addStaticFunctions("DateUtil", DateUtil.class);
            AVIATOR.addStaticFunctions("NumberUtil", NumberUtil.class);
            AVIATOR.addStaticFunctions("Convert", Convert.class);

            AVIATOR.importFunctions(SystemUtils.class);
        } catch (Exception exception) {
        }


        AVIATOR.addFunction(new AmountsFunction());
        AVIATOR.addFunction(new NumberRoundFunction());
    }


    public static Object eval(String expression, Map<String, Object> env) {
        expression = clearExpression(expression);
        return AVIATOR.execute(expression, env);
    }


    public static Object eval(String expression) {
        return eval(expression, null);
    }


    public static String validate(String expression) {
        try {
            expression = clearExpression(expression);
            getInstance().validate(expression);
            return null;
        } catch (ExpressionSyntaxErrorException | CompileExpressionErrorException ex) {
            return ex.getMessage();
        }
    }

    private static String clearExpression(String expression) {
        if (StringUtils.isBlank(expression)) {
            return "";
        }
        return expression.replaceAll("\\{([a-zA-Z0-9$\\.]+)}", "$1");
    }


    public static AviatorEvaluatorInstance getInstance() {
        return AVIATOR;
    }
}



