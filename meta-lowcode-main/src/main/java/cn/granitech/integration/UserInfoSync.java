package cn.granitech.integration;

import cn.granitech.business.task.HeavyTask;
import cn.granitech.variantorm.metadata.ID;

public abstract class UserInfoSync extends HeavyTask<Integer> {
    private final ID defaultUser;

    public UserInfoSync(ID defaultUser2) {
        this.defaultUser = defaultUser2;
    }

    /* access modifiers changed from: protected */
    public Integer execute() throws Exception {
        return null;
    }
}
