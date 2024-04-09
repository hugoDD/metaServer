package cn.granitech.business.service.dataimport;

import cn.granitech.util.AssertHelper;
import cn.granitech.util.SpringHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.web.pojo.ImportRequestBody;
import cn.granitech.web.pojo.UploadDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ImportRule {
    public static final String EXCEL_PATH = "/excelData/";
    public static final int REPEAT_OPT_IGNORE = 3;
    public static final int REPEAT_OPT_SKIP = 2;
    public static final int REPEAT_OPT_UPDATE = 1;
    public static final String tempPath = (SpringHelper.getBean(UploadDir.class).getFileDir() + EXCEL_PATH);
    private static final Logger LOG = LoggerFactory.getLogger(ImportRule.class);
    private final ID defaultOwningUser;
    private final Map<Field, Integer> fieldsMapping;
    private final Field[] repeatFields;
    private final int repeatOpt;
    private final File sourceFile;
    private final Entity toEntity;

    protected ImportRule(File sourceFile2, Entity toEntity2, int repeatOpt2, Field[] repeatFields2, ID defaultOwningUser2, Map<Field, Integer> fieldsMapping2) {
        this.sourceFile = sourceFile2;
        this.toEntity = toEntity2;
        this.repeatOpt = repeatOpt2;
        this.repeatFields = repeatFields2;
        this.defaultOwningUser = defaultOwningUser2;
        this.fieldsMapping = fieldsMapping2;
    }

    public static ImportRule parse(ImportRequestBody importRequestBody, Entity entity) throws IllegalArgumentException {
        AssertHelper.isNotNull(importRequestBody.getMainEntity(), "Node `entity` cannot be null");
        AssertHelper.isNotNull(importRequestBody.getFilePath(), "Node `file` cannot be null");
        AssertHelper.isNotNull(importRequestBody.getRepeatOpt(), "Node `repeat_opt` cannot be null");
        AssertHelper.isNotNull(importRequestBody.getFieldsMapping(), "Node `fields_mapping` cannot be null");
        File file = new File(tempPath + importRequestBody.getFilePath());
        if (!file.exists()) {
            URL fileUrl = ImportRule.class.getClassLoader().getResource(importRequestBody.getFilePath());
            if (fileUrl != null) {
                try {
                    file = new File(fileUrl.toURI());
                } catch (URISyntaxException e) {
                    throw new IllegalArgumentException("File not found : " + file, e);
                }
            }
            LOG.warn("Use file not found : " + file);
        }
        if (!file.exists()) {
            throw new IllegalArgumentException("File not found : " + file);
        }
        int repeatOpt2 = importRequestBody.getRepeatOpt().intValue();
        Field[] repeatFields2 = null;
        if (repeatOpt2 != 3) {
            AssertHelper.isNotNull(importRequestBody.getRepeatFields(), "Node `repeat_fields`");
            Set<Field> rfs = new HashSet<>();
            for (String repeatField : importRequestBody.getRepeatFields()) {
                rfs.add(entity.getField(repeatField));
            }
            AssertHelper.isTrue(Boolean.valueOf(!rfs.isEmpty()), "Node `repeat_fields`");
            repeatFields2 = rfs.toArray(new Field[0]);
        }
        ID user = importRequestBody.getOwningUser();
        ID ownUser = ID.isId(user) ? ID.valueOf(user) : null;
        Map<String, Integer> fieldsMapping2 = importRequestBody.getFieldsMapping();
        Map<Field, Integer> filedsMapping = new HashMap<>();
        for (Map.Entry<String, Integer> e2 : fieldsMapping2.entrySet()) {
            filedsMapping.put(entity.getField(e2.getKey()), e2.getValue());
        }
        return new ImportRule(file, entity, repeatOpt2, repeatFields2, ownUser, filedsMapping);
    }

    public File getSourceFile() {
        return this.sourceFile;
    }

    public Entity getToEntity() {
        return this.toEntity;
    }

    public int getRepeatOpt() {
        return this.repeatOpt;
    }

    public Field[] getRepeatFields() {
        return this.repeatFields;
    }

    public ID getDefaultOwningUser() {
        return this.defaultOwningUser;
    }

    public Map<Field, Integer> getFieldsMapping() {
        return this.fieldsMapping;
    }
}
