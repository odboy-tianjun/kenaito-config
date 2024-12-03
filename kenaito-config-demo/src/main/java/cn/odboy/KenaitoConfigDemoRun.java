package cn.odboy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAsync
@SpringBootApplication
@EnableTransactionManagement
public class KenaitoConfigDemoRun {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(KenaitoConfigDemoRun.class);
        springApplication.run(args);
    }
}
