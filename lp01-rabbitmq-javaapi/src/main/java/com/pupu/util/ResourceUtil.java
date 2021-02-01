package com.pupu.util;

import java.util.ResourceBundle;

/**
 * 配置文件读取工具类
 *
 * @author lp
 * @since 2021/2/1 14:09
 **/
public class ResourceUtil {
    private static final ResourceBundle resourceBundle;

    //静态代码块，懒汉单例
    static {
        resourceBundle = ResourceBundle.getBundle("config");
    }

    public static String getKey(String key) {
        return resourceBundle.getString(key);
    }

}
