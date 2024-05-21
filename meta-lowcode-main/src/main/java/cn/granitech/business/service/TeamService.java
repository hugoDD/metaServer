package cn.granitech.business.service;

import cn.granitech.exception.ServiceException;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static cn.granitech.variantorm.constant.SystemEntities.Team;

@Service
public class TeamService extends BaseService {
    public static final String TEAM_ID = "teamId";
    public static final String TEAM_NAME = "teamName";
    private final Map<ID, EntityRecord> teamCache = new LinkedHashMap<>();
    @Resource
    UserService userService;

    public void saveTeam(String entityName, String recordId, Map<String, Object> dataMap) {
        ID teamId = StrUtil.isNotBlank(recordId) ? ID.valueOf(recordId) : null;
        String teamName = (String) dataMap.get(TEAM_NAME);
        if (isTeamNameAlreadyExists(teamName, teamId)) {
            throw new ServiceException("存在相同的团队名称:" + teamName);
        }
        saveOrUpdateRecord(entityName, teamId, dataMap);
        loadTeamCache();
    }

    private boolean isTeamNameAlreadyExists(String teamName, ID teamId) {
        for (EntityRecord value : this.teamCache.values()) {
            if (value.getFieldValue(TEAM_NAME).equals(teamName) && (teamId == null || !value.id().equals(teamId))) {
                return true;
            }
        }
        return false;
    }

    public void delTeam(ID teamId) {
        for (EntityRecord entityRecord : this.userService.getTeamOrRoleUser(teamId)) {
            this.userService.updateTeamOrRoleUser(teamId, entityRecord.id(), false);
        }
        deleteRecord(teamId);
        this.userService.reloadCache();
        loadTeamCache();
    }

    public String getTeamNameById(ID teamId) {
        if (ArrayUtil.isEmpty(this.teamCache)) {
            loadTeamCache();
        }
        if (this.teamCache.containsKey(teamId)) {
            return this.teamCache.get(teamId).getFieldValue(TEAM_NAME);
        }
        return null;
    }

    public List<EntityRecord> listTeam(String search) {
        if (!StringUtils.isNotBlank(search)) {
            search = "";
        }
        return queryListRecord(Team, String.format(" [disabled]=0 and [teamName] like '%%%s%%' ", search), null, null, null, TEAM_ID, TEAM_NAME);
    }

    private void loadTeamCache() {
        List<EntityRecord> teamList = queryListRecord(Team, null, null, null, null);
        this.teamCache.clear();
        for (EntityRecord team : teamList) {
            this.teamCache.put(team.getFieldValue(TEAM_ID), team);
        }
    }
}
