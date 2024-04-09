package cn.granitech.variantorm.constant;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CommonFields {

    public static final String lastApprovedOn = "lastApprovedOn";
    public static final String lastApprovedBy = "lastApprovedBy";
    public static final String lastApprovalRemark = "lastApprovalRemark";
    public static final String createdBy = "createdBy";
    public static final String quickCode = "quickCode";
    public static final String isDeleted = "isDeleted";
    public static final String ownerUser = "ownerUser";
    public static final String approvalStatus = "approvalStatus";
    public static final String ownerDepartment = "ownerDepartment";
    public static final String modifiedBy = "modifiedBy";
    public static final String createdOn = "createdOn";
    public static final String modifiedOn = "modifiedOn";
    public static final String approvalConfigId = "approvalConfigId";
    private static final List<String> commonFieldList = Arrays
            .asList(lastApprovedOn,lastApprovedBy,lastApprovalRemark,createdBy,quickCode,isDeleted
            ,ownerUser,approvalStatus,ownerDepartment,modifiedBy,createdOn,modifiedOn,approvalConfigId);

    public static boolean containField(String fieldName) {
        return     commonFieldList.contains(fieldName);
    }


}
