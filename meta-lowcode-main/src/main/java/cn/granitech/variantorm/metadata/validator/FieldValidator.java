package cn.granitech.variantorm.metadata.validator;


import cn.granitech.variantorm.exception.IllegalFieldValueException;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;

public interface FieldValidator {
    void validate(PersistenceManager paramPersistenceManager, EntityRecord paramEntityRecord, String paramString) throws IllegalFieldValueException;
}
