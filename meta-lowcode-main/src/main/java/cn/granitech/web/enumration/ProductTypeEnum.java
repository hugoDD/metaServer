package cn.granitech.web.enumration;

import cn.granitech.util.SpringHelper;
import cn.granitech.web.pojo.application.DingTalkSetting;
import cn.granitech.web.pojo.application.SystemSetting;

import java.util.HashMap;
import java.util.Map;

public enum ProductTypeEnum {
    FREE(1, "免费版"),
    STANDARD(2, "标准版"),
    PROFESSIONAL(3, "专业版"),
    ENTERPRISE(4, "企业版"),
    ULTIMATE(5, "旗舰版");

    private final String displayName;
    private final int value;

    ProductTypeEnum(int value2, String displayName2) {
        this.value = value2;
        this.displayName = displayName2;
    }

    public static ProductTypeEnum getTypeByName(String productName) {
        for (ProductTypeEnum type : values()) {
            if (type.displayName.equalsIgnoreCase(productName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No constant with display name " + productName + " found");
    }

    public static Map<String, Object> getCurrentType() {
        ProductTypeEnum productTypeEnum = getTypeByName(SpringHelper.getBean(SystemSetting.class).getLicenseInfo().getProductType());
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("value", productTypeEnum.value);
        resultMap.put("displayName", productTypeEnum.getDisplayName());
        return resultMap;
    }

    public int getValue() {
        return this.value;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void cleanLowProductInfo(SystemSetting systemSetting) {
        if (equals(FREE) || equals(STANDARD)) {
            systemSetting.setDingTalk(null);
            DingTalkSetting dingTalkSetting = new DingTalkSetting();
            dingTalkSetting.setOpenStatus(false);
            systemSetting.setDingTalkSetting(dingTalkSetting);
        }
    }
}
