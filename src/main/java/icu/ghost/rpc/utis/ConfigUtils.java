package icu.ghost.rpc.utis;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;

/**
 * @author Ghost
 * @version 1.0
 * @className ConfigUtils
 * @date 2024/07/29 21:23
 * @since 1.0
 */
public class ConfigUtils {
    /**
     * 加载配置文件
     * @param tclass
     * @param prefix
     * @return
     * @param <T>
     */
    public static <T> T loadConfig(Class<T> tclass, String prefix) {
        return loadConfig(tclass,prefix,"");
    }


    /**
     * 加载配置文件，区分环境
     * @param tclass 对象类型
     * @param prefix 前缀
     * @param environment 环境（prod,test...）
     * @return 泛型
     * @param <T> 泛型
     */
    public static <T> T loadConfig(Class<T> tclass, String prefix, String environment) {
        StringBuilder configBuilder = new StringBuilder("application");
        if (StrUtil.isNotBlank(environment)) {
            configBuilder.append("-").append(environment);
        }
        configBuilder.append(".properties");
        Props props = new Props(configBuilder.toString());
        return props.toBean(tclass, prefix);
    }
}
