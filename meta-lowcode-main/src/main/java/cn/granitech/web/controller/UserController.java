package cn.granitech.web.controller;

import cn.granitech.aop.annotation.SystemRight;
import cn.granitech.business.service.CrudService;
import cn.granitech.business.service.RoleService;
import cn.granitech.business.service.UserService;
import cn.granitech.exception.ServiceException;
import cn.granitech.interceptor.CallerContext;
import cn.granitech.util.EntityHelper;
import cn.granitech.util.MD5Helper;
import cn.granitech.util.ResponseHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.pojo.IDName;
import cn.granitech.variantorm.pojo.UploadItem;
import cn.granitech.web.constant.SessionNames;
import cn.granitech.web.enumration.EntityRightEnum;
import cn.granitech.web.enumration.SystemRightEnum;
import cn.granitech.web.pojo.*;
import cn.granitech.web.pojo.application.SystemSetting;
import cn.granitech.web.pojo.vo.UserInfoVO;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.font.FontDesignMetrics;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping({"/user"})
@RestController
public class UserController {
    @Resource
    CallerContext callerContext;
    @Autowired
    CrudService crudService;
    @Resource
    RoleService roleService;
    @Resource
    SystemSetting systemSetting;
    @Autowired
    UserService userService;

    @RequestMapping({"/getLoginUser"})
    public ResponseBean<Map<String, Object>> getLoginUser(HttpServletRequest request) {
        String loginUserId = (String) request.getSession().getAttribute(SessionNames.LoginUserId);
        if (StrUtil.isBlank(loginUserId)) {
            return ResponseHelper.ok(null);
        }
        this.callerContext.setCallerId(loginUserId);
        Map<String, Object> result = new HashMap<>();
        EntityRecord user = this.userService.getUserById(ID.valueOf(loginUserId));
        result.put("id", user.id().getId());
        result.put("name", user.getName());
        result.put(UserService.LOGIN_NAME, user.getFieldValue(UserService.LOGIN_NAME));
        return ResponseHelper.ok(result);
    }

    @RequestMapping({"/getUserRole"})
    public ResponseBean<List<Map<String, Object>>> getUserRole(@RequestParam("userId") ID userId) {
        return ResponseHelper.ok(this.userService.getUserRole(userId));
    }

    @RequestMapping({"/teamOrRoleUsers"})
    public ResponseBean<List<UserInfoVO>> teamOrRoleUsers(@RequestParam("id") ID id) {
        return ResponseHelper.ok(this.userService.teamOrRoleUsers(id));
    }

    @RequestMapping({"/addTeamOrRoleUsers"})
    public ResponseBean<List<UserInfoVO>> addTeamOrRoleUsers(@RequestBody AddTeamOrRoleUserBody addTeamOrRoleUserBody) {
        this.userService.addTeamOrRoleUsers(addTeamOrRoleUserBody);
        return ResponseHelper.ok();
    }

    @RequestMapping({"/addUserRole"})
    public ResponseBean<List<UserInfoVO>> addUserRole(@RequestBody AddTeamOrRoleUserBody addTeamOrRoleUserBody) {
        this.userService.addUserRole(addTeamOrRoleUserBody);
        return ResponseHelper.ok();
    }

    @RequestMapping({"/delTeamOrRoleUsersUser"})
    public ResponseBean<?> delTeamOrRoleUsersUser(@RequestParam("id") ID id, @RequestParam("userId") ID userId) {
        this.userService.delTeamOrRoleUser(id, userId);
        return ResponseHelper.ok();
    }

    @RequestMapping(method = {RequestMethod.POST}, value = {"/login"})
    public ResponseBean<IDName> login(HttpServletRequest request, @RequestBody LoginInfo loginInfo) {
        if (loginInfo == null || StringUtils.isBlank(loginInfo.getUser()) || StringUtils.isBlank(loginInfo.getPassword())) {
            return ResponseHelper.fail("参数异常！");
        }
        IDName idName = this.userService.getLoginUserId(loginInfo.getUser().trim(), loginInfo.getPassword().trim());
        if (!ObjectUtil.isNotNull(idName)) {
            return ResponseHelper.fail("登录名或密码错误！");
        }
        this.userService.handleUserSession(request, idName.getId().toString());
        return ResponseHelper.ok(idName);
    }

    @RequestMapping(method = {RequestMethod.POST}, value = {"/logout"})
    public ResponseBean<Boolean> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            this.userService.updateLoginLog(request.getSession().getId(), "手动登出");
            session.invalidate();
        }
        return ResponseHelper.ok(Boolean.TRUE, "success");
    }

    @RequestMapping(method = {RequestMethod.POST}, value = {"/saveUser"})
    public ResponseBean<FormQueryResult> saveUser(@RequestParam("entity") String entityName, @RequestParam(required = false, value = "id") String recordId, @RequestBody Map<String, Object> dataMap) {
        return ResponseHelper.ok(this.userService.saveUser(entityName, recordId, dataMap), "success");
    }

    @RequestMapping(method = {RequestMethod.POST}, value = {"/updateLoginUser"})
    public ResponseBean<ID> updateLoginUser(@RequestParam("id") ID recordId, @RequestBody Map<String, Object> dataMap) {
        return ResponseHelper.ok(this.userService.updateLoginUser(recordId, dataMap), "success");
    }

    @RequestMapping(method = {RequestMethod.POST}, value = {"/deleteUser"})
    public ResponseBean<Boolean> deleteUser(@RequestParam String userId) {
        return ResponseHelper.ok(this.userService.deleteUser(ID.valueOf(userId)), "success");
    }

    @RequestMapping({"/updatePassword"})
    public ResponseBean<?> updatePassword(String userName, String password, String newPassword) {
        IDName idName = this.userService.getLoginUserId(userName, password);
        if (idName == null) {
            return ResponseHelper.fail("当前密码错误！");
        }
        ID userId = idName.getId();
        Map<String, Object> map = new HashMap<>();
        map.put(UserService.LOGIN_PWD, MD5Helper.md5HexForSalt(newPassword, userName));
        this.userService.saveOrUpdateRecord("User", userId, map);
        return ResponseHelper.ok("success");
    }

    @RequestMapping({"/resetPassword"})
    public ResponseBean<String> resetPassword(String userId, String password) {
        if (!this.systemSetting.getTrialVersionFlag() || !"0000021-00000000000000000000000000000001".equals(this.callerContext.getCallerId())) {
            this.userService.resetPassword(ID.valueOf(userId), password);
            return ResponseHelper.ok("success");
        }
        throw new ServiceException("当前为演示版本，功能暂不开放！");
    }

    @RequestMapping({"/getRightMap"})
    public ResponseBean<?> getRightMap() {
        try {
            return ResponseHelper.ok(this.roleService.getRightMapOfUser(ID.valueOf(this.callerContext.getCallerId())), "success");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseHelper.fail("获取用户权限失败");
        }
    }

    @GetMapping({"/avatar"})
    @ResponseBody
    public ResponseEntity<?> getAvatar(String userId, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (!StrUtil.isNotBlank(userId)) {
            return ResponseEntity.ok(null);
        }
        EntityRecord user = this.userService.getUserById(ID.valueOf(userId));
        List<UploadItem> avatar = user.getFieldValue("avatar");
        if (!CollUtil.isNotEmpty(avatar) || avatar.size() < 1) {
            response.setContentType("image/GIF");
            writeAvatar(((String) user.getFieldValue(UserService.USER_NAME)).substring(0, 1).toUpperCase(), response.getOutputStream());
            return ResponseEntity.ok("success");
        }
        request.getRequestDispatcher(avatar.get(0).getUrl()).forward(request, response);
        return ResponseEntity.ok(null);
    }

    @RequestMapping({"/listUser"})
    public ResponseBean<List<Map<String, Object>>> listUser(@RequestParam(required = false) String search) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (EntityRecord userRecord : this.userService.listUser(search)) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put(UserService.USER_ID, userRecord.getFieldValue(UserService.USER_ID).toString());
            userMap.put(UserService.USER_NAME, userRecord.getFieldValue(UserService.USER_NAME));
            resultList.add(userMap);
        }
        return ResponseHelper.ok(resultList);
    }

    @RequestMapping({"/userLoginLog"})
    @SystemRight(SystemRightEnum.LOGIN_LOG_MANAGE)
    public ResponseBean<ListQueryResult> queryListMap(@RequestBody ListQueryRequestBody requestBody) {
        requestBody.setMainEntity("LoginLog");
        return new ResponseBean<>(200, null, "success", this.userService.queryListMap(requestBody));
    }

    @RequestMapping({"/checkRight"})
    public ResponseBean<Boolean> checkRight(@RequestParam(required = false, value = "id") ID recordId, @RequestParam(required = false, value = "entityName") String entityName, int rightType) {
        EntityRecord entityRecord;
        boolean right;
        if (rightType == EntityRightEnum.create.getRightType()) {
            entityRecord = this.userService.newRecord(entityName);
        } else if (!EntityHelper.getEntity(recordId.getEntityCode()).isAuthorizable()) {
            entityRecord = this.userService.newRecord(EntityHelper.getEntity(recordId.getEntityCode()).getName());
        } else {
            entityRecord = this.userService.queryRecordById(recordId, "ownerUser", "ownerDepartment");
        }
        if (rightType == EntityRightEnum.create.getRightType()) {
            right = this.callerContext.checkCreateRight(entityRecord);
        } else if (rightType == EntityRightEnum.update.getRightType()) {
            right = this.callerContext.checkUpdateRight(entityRecord);
        } else if (rightType == EntityRightEnum.delete.getRightType()) {
            right = this.callerContext.checkDeleteRight(entityRecord);
        } else if (rightType == EntityRightEnum.assign.getRightType()) {
            right = this.callerContext.checkAssignRight(entityRecord);
        } else if (rightType == EntityRightEnum.share.getRightType()) {
            right = this.callerContext.checkShareRight(entityRecord);
        } else {
            throw new ServiceException("无效的rightType");
        }
        return ResponseHelper.ok(right);
    }

    private void writeAvatar(String drawString, OutputStream os) {
        try {
            BufferedImage buffImg = new BufferedImage(100, 100, 1);
            Graphics2D g = (Graphics2D) buffImg.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
            String themeColor = this.systemSetting.getThemeColor();
            g.setColor(new Color(Integer.parseInt(themeColor.substring(1, 3), 16), Integer.parseInt(themeColor.substring(3, 5), 16), Integer.parseInt(themeColor.substring(5, 7), 16)));
            g.fillRect(0, 0, 100, 100);
            Font font = new Font("宋体", Font.PLAIN, 50);
            FontDesignMetrics metrics = FontDesignMetrics.getMetrics(font);
            int fontWidth = metrics.stringWidth(drawString);
            int fontHeight = metrics.getHeight();
            int ascent = metrics.getAscent();
            g.setFont(font);
            g.setColor(Color.white);
            g.drawString(drawString, 50 - (fontWidth / 2), (50 - (fontHeight / 2)) + ascent);
            ImageIO.write(buffImg, "JPG", os);
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            try {
                os.close();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        } catch (Throwable th) {
            try {
                os.close();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
            throw th;
        }
    }
}
