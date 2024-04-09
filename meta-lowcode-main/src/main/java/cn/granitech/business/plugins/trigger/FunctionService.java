package cn.granitech.business.plugins.trigger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FunctionService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    public void callBackLog(Map<String, Object> paramMap) {
        this.log.info("触发器：函数回调，参数：", paramMap);
    }
}
