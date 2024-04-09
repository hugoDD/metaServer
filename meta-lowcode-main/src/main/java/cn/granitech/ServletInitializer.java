package cn.granitech;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @Description:
 * @Author: VDP 思南
 * @Date: 2020/12/25 10:14
 * @Version: 1.0
 */

/* 本类用于war打包后，部署在Tomcat中自动启动SpringBootApplication类 */

public class ServletInitializer extends SpringBootServletInitializer {

    public ServletInitializer() {
        System.out.println("初始化ServletInitializer...");
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }
}
