package cn.granitech.business.service;

import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.ReferenceCacheEO;
import cn.granitech.variantorm.pojo.ReferenceListCacheEO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class QueryCacheManager {
    @Autowired
    PersistenceManager pm;

    @EventListener
    public void updateIDName(ReferenceCacheEO refCEO) {
        this.pm.getQueryCache().updateIDName(refCEO.getIdName());
    }

    @EventListener
    public void updateIDNameList(ReferenceListCacheEO refListCEO) {
        System.out.println("update roles cache...");
        this.pm.getQueryCache().updateIDNameList(refListCEO.getEntityName(), refListCEO.getRefFieldName(), refListCEO.getRecordId(), refListCEO.getIdNameList());
    }
}
