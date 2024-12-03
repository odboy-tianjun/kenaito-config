package cn.odboy;

import cn.odboy.infra.context.EasyBootApplication;
import cn.odboy.infra.context.SpringContextHolder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAsync
@SpringBootApplication
@EnableTransactionManagement
public class KenaitoConfigServiceRun extends EasyBootApplication {
    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(KenaitoConfigServiceRun.class);
        initd(springApplication.run(args));
    }
}
