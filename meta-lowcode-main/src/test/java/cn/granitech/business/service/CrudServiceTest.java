package cn.granitech.business.service;


import cn.granitech.variantorm.persistence.EntityRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

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
        List<EntityRecord> users = crudService.queryListRecord("User", " [departmentId.departmentName] like '%公司%' ",
                null, null, null);
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(users));

//        users = crudService.queryListRecord("User", " [departmentId.departmentName] like '%公司2%' ",
//                null, null, null);
//        System.out.println(objectMapper.writeValueAsString(users));
    }

    //@Test
    public void testAccount() throws JsonProcessingException {
        List<EntityRecord> accounts = crudService.queryListRecord("Account", null,
                null, null, null, "accountName", "sponsor");
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(accounts));
    }

}
