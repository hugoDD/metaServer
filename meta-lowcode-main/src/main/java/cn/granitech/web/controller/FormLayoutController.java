package cn.granitech.web.controller;

import cn.granitech.business.service.*;
import cn.granitech.util.CommonHelper;
import cn.granitech.util.QiNiuHelper;
import cn.granitech.util.ResponseHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.metadata.fieldtype.FieldTypes;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.variantorm.pojo.OptionModel;
import cn.granitech.variantorm.pojo.TagModel;
import cn.granitech.web.pojo.FormLayout;
import cn.granitech.web.pojo.FormQueryResult;
import cn.granitech.web.pojo.FormUploadParam;
import cn.granitech.web.pojo.ResponseBean;
import cn.granitech.web.pojo.application.CloudStorageSetting;
import cn.granitech.web.pojo.application.SystemSetting;
import cn.granitech.web.pojo.layout.FieldProps;
import cn.hutool.core.util.ObjectUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.model.BucketInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/formLayout"})
public class FormLayoutController extends BaseController {
    @Autowired
    EntityManagerService entityManagerService;

    @Autowired
    OptionManagerService optionManagerService;

    @Autowired
    TagManagerService tagManagerService;

    @Autowired
    CrudService crudService;

    @Autowired
    FormLayoutManager formLayoutManager;

    @Resource
    SystemSetting systemSetting;

    @RequestMapping({"/save"})
    public ResponseBean<ID> saveLayout(@RequestParam("entity") String entityName, @RequestBody String layoutJson) {
        ID layoutId = this.formLayoutManager.saveLayout(entityName, layoutJson);
        return new ResponseBean<>(200, null, "success", layoutId);
    }

    @RequestMapping({"/update"})
    public ResponseBean<ID> updateLayout(@RequestParam("layoutId") String layoutId, @RequestBody String layoutJson) {
        ID savedId = this.formLayoutManager.updateLayout(layoutId, layoutJson);
        return new ResponseBean<>(200, null, "success", savedId);
    }

    @RequestMapping({"/get"})
    public ResponseBean<FormLayout> getLayout(@RequestParam("entity") String entityName) throws IOException {
        FormLayout formLayout = this.formLayoutManager.getLayout(entityName);
        if (formLayout == null) {
            FormLayout blankFormLayout = new FormLayout();
            EntityRecord formLayoutRecord = this.crudService.newRecord("FormLayout");
            blankFormLayout.setEntityRecord(formLayoutRecord);
            blankFormLayout.setLayoutJson(null);
            blankFormLayout.setOptionData(buildOptionData(entityName));
            setUploadConfig(blankFormLayout);
            return new ResponseBean<>(200, null, "success", blankFormLayout);
        }
        formLayout.setOptionData(buildOptionData(entityName));
        setUploadConfig(formLayout);
        return new ResponseBean<>(200, null, "success", formLayout);
    }

    private void setUploadConfig(FormLayout formLayout) throws QiniuException {
        CloudStorageSetting cloudStorageSetting = this.systemSetting.getCloudStorageSetting();
        boolean cloudStorage = (ObjectUtil.isNotNull(cloudStorageSetting) && cloudStorageSetting.isOpenStatus() && StringUtils.isNotBlank(cloudStorageSetting.getHost()) && StringUtils.isNotBlank(cloudStorageSetting.getBucket()) && StringUtils.isNotBlank(cloudStorageSetting.getSecretKey()) && StringUtils.isNotBlank(cloudStorageSetting.getAccessKey()));
        FormUploadParam fuParam = new FormUploadParam();
        if (cloudStorage) {
            fuParam.setCloudStorage("true");
            fuParam.setCloudUploadToken(QiNiuHelper.getToken(null));
            BucketInfo bucketInfo = QiNiuHelper.getBucketInfo();
            String region = bucketInfo.getRegion();
            fuParam.setPicUploadURL(String.format("https://up-%s.qiniup.com", region));
            fuParam.setFileUploadURL(String.format("https://up-%s.qiniup.com", region));
            fuParam.setPicDownloadPrefix("/picture/get/QiNiu=");
            fuParam.setFileDownloadPrefix("/file/get/QiNiu=");
        } else {
            fuParam.setCloudStorage("false");
            fuParam.setCloudUploadToken("");
            fuParam.setPicUploadURL("DSV['uploadServer'] + '/picture/upload'");
            fuParam.setFileUploadURL("DSV['uploadServer'] + '/file/upload'");
            fuParam.setPicDownloadPrefix("/picture/get/");
            fuParam.setFileDownloadPrefix("/file/get/");
        }
        formLayout.setFormUploadParam(fuParam);
    }

    private Map<String, List<OptionModel>> buildOptionData(String entityName) {
        Map<String, List<OptionModel>> resultMap = new HashMap<>();
        Entity entity = this.entityManagerService.getMetadataManager().getEntity(entityName);
        addOptionDataOfEntity(entity, resultMap);
        entity.getDetailEntitySet().forEach(de -> addOptionDataOfEntity(de, resultMap));
        return resultMap;
    }

    private void addOptionDataOfEntity(Entity entity, Map<String, List<OptionModel>> optionDataMap) {
        for (Field fld : entity.getFieldSet()) {
            if (fld.getType() == FieldTypes.OPTION || fld.getType() == FieldTypes.MULTIOPTION) {
                List<OptionModel> omList = this.optionManagerService.getOptionList(entity.getName(), fld.getName());
                optionDataMap.put(fld.getName(), omList);
                continue;
            }
            if (fld.getType() == FieldTypes.STATUS) {
                List<OptionModel> omList = this.optionManagerService.getStatusList(fld.getName());
                optionDataMap.put(fld.getName(), omList);
                continue;
            }
            if (fld.getType() == FieldTypes.TAG) {
                List<TagModel> tmList = this.tagManagerService.getTagList(entity.getName(), fld.getName());
                List<OptionModel> newOmList = new ArrayList<>();
                int index = 1;
                for (TagModel tm : tmList) {
                    OptionModel newOm = new OptionModel(index, tm.getValue(), tm.getDisplayOrder());
                    newOmList.add(newOm);
                    index++;
                }
                optionDataMap.put(fld.getName(), newOmList);
                continue;
            }
            fld.getType();
        }
    }

    @RequestMapping({"/previewLayout"})
    public ResponseBean<FormQueryResult> previewLayout(@RequestParam("entity") String entityName) {
        Map<String, FieldProps> fieldPropsMap = new HashMap<>();
        Entity entity = this.entityManagerService.getMetadataManager().getEntity(entityName);
        entity.getFieldSet().forEach(field -> {
            FieldProps fieldProps = CommonHelper.copyFieldProps(field);
            fieldPropsMap.put(field.getName(), fieldProps);
        });
        FormQueryResult formQueryResult = new FormQueryResult(null, fieldPropsMap, null, null, null);
        return ResponseHelper.ok(formQueryResult, "success");
    }
}
