package icu.ghost.rpc.spi;

import cn.hutool.core.io.resource.ResourceUtil;
import icu.ghost.rpc.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ghost
 * @version 1.0
 * @className SPILoader
 * @date 2024/07/31 0:06
 * @since 1.0
 */
@Slf4j
public class SPILoader {

    /**
     * 存储已加载的类   接口  =》 (key =》 实现类)
     */
    private static Map<String, Map<String,Class<?>>> loadMap = new ConcurrentHashMap<>();

    /**
     * 缓存对象实例（避免重复new）   key =》 实现类   单例模式
     */
    private static Map<String,Object> instanceCache = new ConcurrentHashMap<>();

    /**
     * SPI目录
     */
    private static final String RPC_CUSTOM_SPI_DIR = "META-INF/rpc/custom/";
    private static final String RPC_SYSTEM_SPI_DIR = "META-INF/rpc/system/";

    /**
     * 扫描路径
     */
    private static final String[] SCAN_DIRS = new String[]{RPC_SYSTEM_SPI_DIR,RPC_CUSTOM_SPI_DIR};

    private static final List<Class> LOAD_CLASS_LIST = Arrays.asList(Serializer.class);


    /**
     * 获取某个接口实现类实例
     * @param tClass 接口类
     * @param key key
     * @return 接口实现类实例
     * @param <T> 泛型
     */
    public static <T> T getInstance(Class<?> tClass, String key) {
        // 接口名
        String tClassName = tClass.getName();
        Map<String, Class<?>> keyImplMap = loadMap.get(tClassName);
        if (keyImplMap == null) {
            throw new RuntimeException(String.format("SPILoader未加载%s类型", tClassName));
        }
        if (!keyImplMap.containsKey(key)) {
            throw new RuntimeException(String.format("SPILoad的%s不存在key=%s", tClassName, key));
        }

        // 获取实现类
        Class<?> implClass = keyImplMap.get(key);
        String implClassName = implClass.getName();
        // 缓存中获取指定类型的实例
        if (!instanceCache.containsKey(implClassName)) {
            // 没有缓存
            try {
                instanceCache.put(implClassName,implClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(String.format("%s类实例化失败", implClassName));
            }
        }
        // 有则，直接读取缓存
        return (T)instanceCache.get(implClassName);
    }

    /**
     * 加载某个类型
     * @param loadClass 类
     * @return
     */
    public static void load(Class<?> loadClass) {
        log.info("加载{}类型的SPI", loadClass.getName());
        Map<String,Class<?>> keyImplMap = new HashMap<>();

        // 用户自定义的SPI优先级高于系统SPI
        for (String scanDir : SCAN_DIRS) {
            String resourceURL = scanDir + loadClass.getName();
            List<URL> resources = ResourceUtil.getResources(resourceURL);
            for (URL resource : resources) {
                try {
                    InputStream inputStream = resource.openStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    // 读取一行
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] split = line.split("=");
                        if (split.length > 1){
                            String key = split[0];
                            String className = split[1];
                            Class<?> implClass = Class.forName(className);
                            keyImplMap.put(key,implClass);
                        }
                    }

                } catch (IOException | ClassNotFoundException e) {
                    log.info("SPI resource 加载失败");
                }
            }
        }
        loadMap.put(loadClass.getName(), keyImplMap);
    }

    /**
     * 加载所有的SPI
     */
    public static void loadAll() {
        log.info("加载所有SPI");
        for (Class tClass : LOAD_CLASS_LIST) {
            load(tClass);
        }
    }

}
