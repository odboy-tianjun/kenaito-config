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
    /**
     * 文件名变量，用于存储文件的名称
     */
    private String fileName;

    /**
     * 文件内容变量，用于存储文件的文本内容
     */
    private String fileContent;

}
