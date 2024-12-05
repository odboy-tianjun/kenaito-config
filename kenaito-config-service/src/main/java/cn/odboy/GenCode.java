package cn.odboy;


import cn.odboy.util.GenCmdHelper;

import java.util.List;

/**
 * 代码生成入口
 *
 * @date 2024-04-27
 */
public class GenCode {
    public static void main(String[] args) {
        GenCmdHelper generator = new GenCmdHelper();
        generator.setDatabaseUrl(String.format("jdbc:mysql://%s:%s/%s", "kenaito-mysql.odboy.local", 13306, "kenaito_config"));
        generator.setDatabaseUsername("root");
        generator.setDatabasePassword("root");
        genCareer(generator);
    }

    private static void genCareer(GenCmdHelper generator) {
        generator.gen("", List.of(
                "config_file",
                "config_version"
        ));
    }
}
