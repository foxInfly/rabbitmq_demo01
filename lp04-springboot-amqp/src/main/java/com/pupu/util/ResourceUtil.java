package com.pupu.util;

import java.util.ResourceBundle;

/**
 * 饿汉模式的：静态代码块；单例
 * @author lp
 * @since 2021/2/6 14:26
 **/
public class ResourceUtil {
    private static final ResourceBundle resourceBundle;

    static{
        resourceBundle = ResourceBundle.getBundle("gupaomq");
    }

    public static String getKey(String key){
        return resourceBundle.getString(key);
    }

}
