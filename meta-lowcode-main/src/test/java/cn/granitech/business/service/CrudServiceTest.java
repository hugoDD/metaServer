package cn.granitech.business.service;


import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: VDP 思南
 * @Date: 2020/12/26 15:51
 * @Version: 1.0
 */


@RunWith(SpringRunner.class)
@SpringBootTest
public class CrudServiceTest {

    @Autowired
    CrudService crudService;


    @Test
    public void testUserQuery() throws JsonProcessingException {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("depName","%公司%");
        List<EntityRecord> users = crudService.queryListRecord("User", " [departmentId.departmentName] like pn_depName ",
                paramMap, null, null,"userId","userName");
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(users));

//        users = crudService.queryListRecord("User", " [departmentId.departmentName] like '%公司2%' ",
//                null, null, null);
//        System.out.println(objectMapper.writeValueAsString(users));
    }

    @Test
    public void testAccount() throws JsonProcessingException {
        ID id = new ID("0000021-00000000000000000000000000000001");

        EntityRecord accounts = crudService.queryById(id,  "userId", "userName");
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(accounts));
    }

}
