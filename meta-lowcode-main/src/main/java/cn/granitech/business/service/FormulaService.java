package cn.granitech.business.service;

import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.pojo.Entity;
import com.alibaba.excel.util.StringUtils;
import com.googlecode.aviator.AviatorEvaluator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FormulaService extends BaseService {
    public Object execute(EntityRecord entityRecord, String formula) {
        List<String> fieldNameList = getFieldNameList(formula);
        Map<String, Object> variables = new HashMap<>();
        Entity entity = entityRecord.getEntity();
        for (String fieldName : fieldNameList) {
            if (entity.containsField(fieldName)) {
                variables.put(fieldName, entityRecord.getFieldValue(fieldName));
            }
        }
        return AviatorEvaluator.execute(formula, variables);
    }

    private List<String> getFieldNameList(String formula) {
        List<String> resultList = new ArrayList<>();
        Matcher matcher = Pattern.compile("\\w*").matcher(formula);
        while (matcher.find()) {
            String foundGroup = matcher.group();
            if (!StringUtils.isEmpty(foundGroup)) {
                resultList.add(foundGroup);
            }
        }
        return resultList;
    }
}
