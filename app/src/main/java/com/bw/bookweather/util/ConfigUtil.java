package com.bw.bookweather.util;

import com.bw.bookweather.db.AppConfig;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zxc on 2020/1/15.
 */

public class ConfigUtil {
    private static Map<String, AppConfig> appConfigMap = new HashMap<>();
    private static List<String> configList = new ArrayList<>();

    static {
//        configList.add("设置");
    }

    public static Map<String, AppConfig> getAppConfigMap() {
        synchronized (appConfigMap) {
            List<AppConfig> appConfigList = DataSupport.findAll(AppConfig.class);
            if (appConfigMap.size() != appConfigList.size()) {
////                数据库初始化
//                if (appConfigList.size()<2){
//                    AppConfig ac = new AppConfig();
//                    ac.setType(ConfigEnum.TYPE_CONFIG.getValue());
//                    ac.setCode("config");
//                    ac.setValue("设置");
//                    ac.setOrderId(0);
//                }
                for (AppConfig a : appConfigList) {
                    appConfigMap.put(a.getCode(), a);
                }
            }
            return appConfigMap;
        }
    }

    public static void setAppConfigMap(Map<String, AppConfig> appConfigMap) {
        ConfigUtil.appConfigMap = appConfigMap;
    }

    public static List<String> getConfigList() {
        synchronized (configList){
            if (configList.size()<1){
                configList.add("地点");
                configList.add("");
                configList.add("关于");
//                configList.add("");
            }
        }
        return configList;
    }

    public static void setConfigList(List<String> configList) {
        ConfigUtil.configList = configList;
    }
}
