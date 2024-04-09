package cn.granitech.web.enumration;

import org.apache.commons.lang3.StringUtils;

public enum RedisKeyEnum {
    NOTIFICATION_COUNT,
    REFERENCE_CACHE("RC"),
    REFERENCE_LIST_MAP,
    TRIGGER_ASSIGN,
    CONFIG_SETTING,
    USER_LAYOUT_CACHE,
    USER_CACHE,
    QUICK_FILTER,
    DEFAULT_FILTER,
    TMP_CACHE;

    String shortName;

    RedisKeyEnum() {
    }

    RedisKeyEnum(String shortName) {
        this.shortName = shortName;
    }

    public String getKey() {
        return StringUtils.isEmpty(this.shortName) ? this.name() : this.shortName;
    }

    public String getKey(String... keys) {
        return this.getKey() + ":" + String.join(":", keys);
    }
}
