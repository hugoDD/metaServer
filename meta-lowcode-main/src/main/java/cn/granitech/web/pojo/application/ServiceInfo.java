package cn.granitech.web.pojo.application;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "server")
@Component
public class ServiceInfo {
    private int port;

    public int getPort() {
        return this.port;
    }

    public void setPort(int port2) {
        this.port = port2;
    }
}
