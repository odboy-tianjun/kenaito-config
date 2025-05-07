package cn.odboy.infra.monitor;

import com.alibaba.druid.stat.DruidStatManagerFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Druid监控数据
 * @date 2024-09-05
 */
@RestController
@RequestMapping(value = "/druid")
public class DruidMonitorController {
    /**
     * 获取数据源的监控数据
     */
    @GetMapping("/stat")
    public List<Map<String, Object>> druidStat() {
        return DruidStatManagerFacade.getInstance().getDataSourceStatDataList();
    }
}