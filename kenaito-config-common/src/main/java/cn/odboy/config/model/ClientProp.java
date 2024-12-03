package cn.odboy.config.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ClientProp implements Serializable {
    private String server;
    private Integer port;
    private String env;
    private String dataId;
    private String cacheDir;
}
