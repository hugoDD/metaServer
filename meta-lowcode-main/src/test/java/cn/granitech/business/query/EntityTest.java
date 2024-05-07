package cn.granitech.business.query;

import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.util.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

public class EntityTest {

    @Test
    public void jsonToEntity() throws IOException {
        String json = FileUtils.readFileToString(new ClassPathResource("user.json").getFile());
        EntityBo entity = JsonUtils.readJsonValue(json, new TypeReference<EntityBo>() {
        });
        System.out.println(entity);
    }
}
