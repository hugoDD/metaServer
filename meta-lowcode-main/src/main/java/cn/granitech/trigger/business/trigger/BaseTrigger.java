package cn.granitech.trigger.business.trigger;

import cn.granitech.variantorm.metadata.ID;

public interface BaseTrigger {
    boolean trigger(ID paramID, String paramString);
}



