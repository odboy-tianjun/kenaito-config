package cn.odboy.config.model.msgtype;

import lombok.Data;

import java.io.Serializable;
/**
 * 配置文件 信息
 * @author odboy
 * @date 2024-12-09
 */
@Data
public class ConfigFileInfo implements Serializable {
    private String fileName;
    private String fileContent;
}
