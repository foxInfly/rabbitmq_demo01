package com.pupu.util;

import java.util.ResourceBundle;

/**
 *
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
