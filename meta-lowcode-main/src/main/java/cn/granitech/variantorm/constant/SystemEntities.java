package cn.granitech.variantorm.constant;


import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SystemEntities {
    public static final String DataListView = "DataListView";
    public static final String RevisionHistory = "RevisionHistory";
    public static final String FormLayout = "FormLayout";
    public static final String Notification = "Notification";

    public static final String StatusItem = "StatusItem";
    public static final String ApprovalConfig = "ApprovalConfig";
    public static final String LoginLog = "LoginLog";
    public static final String Department = "Department";
    public static final String TodoTask = "TodoTask";
    public static final String TriggerConfig = "TriggerConfig";
    public static final String Chart = "Chart";
    public static final String TriggerLog = "TriggerLog";
    public static final String MetaApi = "MetaApi";
    public static final String RecycleBin = "RecycleBin";
    public static final String DepartmentNode = "DepartmentNode";
    public static final String ShareAccess = "ShareAccess";
    public static final String FollowUp = "FollowUp";
    public static final String TagItem = "TagItem";
    public static final String Team = "Team";
    public static final String ApprovalTask = "ApprovalTask";
    public static final String LayoutConfig = "LayoutConfig";
    public static final String RouterMenu = "RouterMenu";
    public static final String ApprovalFlow = "ApprovalFlow";
    public static final String ApprovalHistory = "ApprovalHistory";
    public static final String ReportConfig = "ReportConfig";
    public static final String Role = "Role";
    public static final String ReferenceCache = "ReferenceCache";
    public static final String OptionItem = "OptionItem";
    public static final String SystemSetting = "SystemSetting";
    public static final String ReferenceListMap = "ReferenceListMap";
    public static final String BackupDatabase = "BackupDatabase";
    public static final String User = "User";
    private static final Set<String> SYSTEM_ENTITY_SET = Stream.of(
            Chart,
            MetaApi,
            TriggerConfig,
            ReportConfig,
            ApprovalConfig,
            TodoTask).collect(Collectors.toSet());

    private static final Set<String> INTERNAL_ENTITY_SET = Stream.of(
            OptionItem
            , StatusItem
            , TagItem
            , ReferenceListMap
            , ReferenceCache
            , SystemSetting
            , FormLayout
            , DataListView
            , RouterMenu
            , DepartmentNode
            , BackupDatabase
            , ApprovalFlow
            , ApprovalHistory
            , ApprovalTask
            , Notification
            , RecycleBin
            , RevisionHistory
            , ShareAccess
            , LayoutConfig
            , LoginLog
            , TriggerLog).collect(Collectors.toSet());

    private static final Set<String> delete_entity_set = Stream.of(
            ApprovalFlow,
            ApprovalTask,
            ApprovalHistory,
            DataListView,
            DepartmentNode,
            FormLayout,
            OptionItem,
            ReferenceCache,
            ReferenceListMap,
            RouterMenu,
            SystemSetting,
            TagItem,
            StatusItem,
            RecycleBin,
            ShareAccess,
            LoginLog,
            TriggerLog
    ).map(String::toUpperCase).collect(Collectors.toSet());


    public static boolean isSystemEntity(String entityName) {
        return SYSTEM_ENTITY_SET.contains(entityName);
    }

    public static boolean isInternalEntity(String entityName) {
        return INTERNAL_ENTITY_SET.contains(entityName);
    }

    public static boolean hasDeletedFlag(String entityName) {
        return !delete_entity_set.contains(entityName.toUpperCase());
    }


}
