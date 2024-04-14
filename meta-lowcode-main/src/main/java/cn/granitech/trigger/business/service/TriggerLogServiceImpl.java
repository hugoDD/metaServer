package cn.granitech.trigger.business.service;

import cn.granitech.business.service.CrudService;
import cn.granitech.interceptor.CallerContext;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class TriggerLogServiceImpl {

    @Resource
    private CrudService crudService;

    @Resource
    private PersistenceManager pm;

    @Resource
    private CallerContext callerContext;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(EntityRecord entityRecord, String callerId,String deptId) {
        callerContext.setCallerId(callerId);
        callerContext.setDepartmentIdLocal(deptId);
        crudService.saveOrUpdateRecord(null, entityRecord);
    }
}
