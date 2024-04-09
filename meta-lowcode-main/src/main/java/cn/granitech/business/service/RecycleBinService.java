package cn.granitech.business.service;

import cn.granitech.interceptor.CallerContext;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class RecycleBinService extends BaseService {
    @Resource
    CallerContext callerContext;
    @Resource
    PersistenceManager pm;

    @Transactional
    public void restore(ID recordId) {
        EntityRecord recycleBin = this.pm.createRecordQuery().queryOne("RecycleBin", String.format(" entityId = '%s' AND restoreBy IS NULL", recordId.getId()), null, null, "recycleBinId", "detailEntityIds");
        if (recycleBin != null) {
            String detailEntityIds = recycleBin.getFieldValue("detailEntityIds");
            if (StringUtils.isNotBlank(detailEntityIds)) {
                for (String id : detailEntityIds.split(",")) {
                    this.pm.restore(ID.valueOf(id));
                }
            }
            this.pm.restore(recordId);
            recycleBin.setFieldValue("restoreBy", ID.valueOf(this.callerContext.getCallerId()));
            recycleBin.setFieldValue("restoreOn", new Date());
            this.pm.update(recycleBin);
        }
    }
}
