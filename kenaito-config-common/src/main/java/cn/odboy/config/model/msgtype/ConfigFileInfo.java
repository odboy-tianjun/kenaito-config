package cn.odboy.config.model.msgtype;

import lombok.Data;

import java.io.Serializable;

@Data
public class ConfigFileInfo implements Serializable {
    private String fileName;
    private String fileContent;
}
