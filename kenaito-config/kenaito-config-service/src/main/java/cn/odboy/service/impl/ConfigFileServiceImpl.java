package cn.odboy.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.config.model.msgtype.ConfigFileInfo;
import cn.odboy.domain.ConfigFile;
import cn.odboy.domain.ConfigVersion;
import cn.odboy.infra.exception.BadRequestException;
import cn.odboy.mapper.ConfigFileMapper;
import cn.odboy.service.ConfigFileService;
import cn.odboy.service.ConfigVersionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 配置文件 服务实现类
 *
 * @author odboy
 * @since 2024-12-05
 */
@Service
@RequiredArgsConstructor
public class ConfigFileServiceImpl extends ServiceImpl<ConfigFileMapper, ConfigFile>
        implements ConfigFileService {
    private final ConfigVersionService configVersionService;

    @Override
    public List<ConfigFileInfo> getFileList(String env, String dataId) {
        return getBaseMapper().selectInfoByEnvAndAppName(env, dataId);
    }

    @Override
    public List<ConfigFile> queryInAppId(Long appId) {
        return list(new LambdaQueryWrapper<ConfigFile>().in(ConfigFile::getAppId, appId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(ConfigFile.CreateArgs args) throws Exception {
        MultipartFile file = args.getFile();
        Long version = 1L;
        ConfigFile configFile = new ConfigFile();
        configFile.setEnvCode(args.getEnv());
        configFile.setAppId(args.getAppId());
        configFile.setFileName(file.getOriginalFilename());
        configFile.setVersion(version);
        save(configFile);
        ConfigVersion configVersion = new ConfigVersion();
        configVersion.setFileId(configFile.getId());
        configVersion.setFileType(FileUtil.getSuffix(file.getOriginalFilename()));
        configVersion.setFileContent(StrUtil.str(file.getBytes(), StandardCharsets.UTF_8));
        configVersion.setVersion(version);
        configVersionService.save(configVersion);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(ConfigFile.RemoveArgs args) {
        removeById(args.getId());
        configVersionService.removeBatchByFileId(args.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyFileContent(ConfigFile.ModifyFileContentArgs args) {
        String originConfigFileContent = getBaseMapper().selectContentByFileId(args.getId());
        if (StrUtil.isNotBlank(originConfigFileContent)) {
            originConfigFileContent = originConfigFileContent.replaceAll("\r", "").replaceAll("\n", "");
        }
        String commitContent = args.getFileContent().replaceAll("\r", "").replaceAll("\n", "");
        if (originConfigFileContent.equals(commitContent)) {
            throw new BadRequestException("未变更配置，无需提交");
        }
        ConfigFile originConfigFile = getById(args.getId());
        long newVersion = originConfigFile.getVersion() + 1;
        ConfigFile updVersion = new ConfigFile();
        updVersion.setId(originConfigFile.getId());
        updVersion.setVersion(newVersion);
        updateById(updVersion);
        ConfigVersion lastConfigVersion = new ConfigVersion();
        lastConfigVersion.setFileId(originConfigFile.getId());
        lastConfigVersion.setFileContent(args.getFileContent());
        lastConfigVersion.setFileType(FileUtil.getSuffix(originConfigFile.getFileName()));
        lastConfigVersion.setVersion(newVersion);
        configVersionService.save(lastConfigVersion);
    }

    @Override
    public List<ConfigFile.QueryList> queryList(ConfigFile.QueryList args) {
        return getBaseMapper().selectDetailByEnvAndAppId(args.getEnvCode(), args.getAppId());
    }

    @Override
    public String getContentById(ConfigFile args) {
        if (args.getId() == null) {
            throw new BadRequestException("id必填");
        }
        return getBaseMapper().selectContentByFileId(args.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void upload(Long appId, String envCode, MultipartFile file) throws IOException {
        if (appId == null) {
            throw new BadRequestException("appId必填");
        }
        if (StrUtil.isBlank(envCode)) {
            throw new BadRequestException("envCode必填");
        }
        if (file == null) {
            throw new BadRequestException("file必填");
        }
        String suffix = FileUtil.getSuffix(file.getOriginalFilename());
//    Properties properties = new Properties();
//    if ("yml".equals(suffix) || "yaml".equals(suffix)) {
//      String content = PropertiesUtil.castToProperties(StrUtil.str(file.getBytes(), StandardCharsets.UTF_8));
//      properties.load(IoUtil.toStream(content, StandardCharsets.UTF_8));
//    } else {
//      properties.load(file.getInputStream());
//    }
        ConfigFile oldConfigFile = getVersionBy(appId, envCode, file.getOriginalFilename());
        if (oldConfigFile == null) {
            long version = 1L;
            ConfigFile newConfigFile = new ConfigFile();
            newConfigFile.setAppId(appId);
            newConfigFile.setEnvCode(envCode);
            newConfigFile.setFileName(file.getOriginalFilename());
            newConfigFile.setVersion(version);
            save(newConfigFile);
            ConfigVersion newConfigVersion = new ConfigVersion();
            newConfigVersion.setFileId(newConfigFile.getId());
            newConfigVersion.setFileContent(StrUtil.str(file.getBytes(), StandardCharsets.UTF_8));
            newConfigVersion.setFileType(suffix);
            newConfigVersion.setVersion(version);
            configVersionService.save(newConfigVersion);
        } else {
            long newVersion = oldConfigFile.getVersion() + 1;
            ConfigFile updConfigFile = new ConfigFile();
            updConfigFile.setId(oldConfigFile.getId());
            updConfigFile.setVersion(newVersion);
            updateById(updConfigFile);
            ConfigVersion newConfigVersion = new ConfigVersion();
            newConfigVersion.setFileId(oldConfigFile.getId());
            newConfigVersion.setFileContent(StrUtil.str(file.getBytes(), StandardCharsets.UTF_8));
            newConfigVersion.setFileType(suffix);
            newConfigVersion.setVersion(newVersion);
            configVersionService.save(newConfigVersion);
        }
    }

    @Override
    public List<ConfigFile> getByAppIdEnvCode(Long appId, String envCode) {
        return list(
                new LambdaQueryWrapper<ConfigFile>()
                        .eq(ConfigFile::getAppId, appId)
                        .eq(ConfigFile::getEnvCode, envCode));
    }

    private ConfigFile getVersionBy(Long appId, String envCode, String fileName) {
        return getOne(
                new LambdaQueryWrapper<ConfigFile>()
                        .eq(ConfigFile::getAppId, appId)
                        .eq(ConfigFile::getEnvCode, envCode)
                        .eq(ConfigFile::getFileName, fileName));
    }
}
