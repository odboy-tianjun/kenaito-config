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
        generator.setDatabaseUrl(String.format("jdbc:mysql://%s:%s/%s", "kenaito-mysql.odboy.local", 13306, "kenaito_devops"));
        generator.setDatabaseUsername("root");
        generator.setDatabasePassword("root");
        genCareer(generator);
    }

    private static void genCareer(GenCmdHelper generator) {
        generator.gen("devops_", List.of(
                // 应用
//                "devops_app",
//                "devops_app_user",
//                "devops_product_line",
                // 应用迭代
//                "devops_app_iteration",
//                "devops_app_iteration_change",
                // 容器
//                "devops_containerd_cluster_config",
//                "devops_containerd_cluster_node",
//                "devops_ops_config",
//                "devops_containerd_spec_config",
                // 网络
//                "devops_network_service",
//                "devops_network_ingress",
                // 流水线
                "devops_pipeline_template_type",
                "devops_pipeline_template_language",
                "devops_pipeline_template_language_config",
                "devops_pipeline_template_app"
        ));
    }
}
