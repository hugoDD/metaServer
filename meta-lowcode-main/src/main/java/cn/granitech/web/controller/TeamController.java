package cn.granitech.web.controller;

import cn.granitech.business.service.TeamService;
import cn.granitech.util.ResponseHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.web.pojo.ResponseBean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping({"/team"})
@RestController
public class TeamController {
    @Resource
    TeamService teamService;

    @RequestMapping({"/saveTeam"})
    public ResponseBean saveTeam(@RequestParam("entity") String entityName, @RequestParam(required = false, value = "id") String recordId, @RequestBody Map<String, Object> dataMap) {
        this.teamService.saveTeam(entityName, recordId, dataMap);
        return ResponseHelper.ok();
    }

    @RequestMapping({"/listTeam"})
    public ResponseBean<List<Map<String, Object>>> listUser(@RequestParam(required = false) String search) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (EntityRecord userRecord : this.teamService.listTeam(search)) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put(TeamService.TEAM_ID, userRecord.getFieldValue(TeamService.TEAM_ID).toString());
            userMap.put(TeamService.TEAM_NAME, userRecord.getFieldValue(TeamService.TEAM_NAME));
            resultList.add(userMap);
        }
        return ResponseHelper.ok(resultList);
    }

    @RequestMapping({"/delTeam"})
    public ResponseBean delTeam(@RequestParam("teamId") ID teamId) {
        this.teamService.delTeam(teamId);
        return ResponseHelper.ok();
    }
}
