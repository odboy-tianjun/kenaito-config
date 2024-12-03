package cn.odboy.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class ConfigCenterClient {

    private final RestTemplate restTemplate;
    private final String configCenterUrl;

    public ConfigCenterClient(String configCenterUrl) {
        this.restTemplate = new RestTemplate();
        this.configCenterUrl = configCenterUrl;
    }

    public Map<String, Object> fetchConfig() {
        ResponseEntity<Map> response = restTemplate.getForEntity(configCenterUrl, Map.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to fetch config from config center");
        }
    }
}
