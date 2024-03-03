package com.parcelhub.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {

    private BeanCopyUtils() {

    }

    public static <V> V copyBean(Object source, Class<V> clazz) {
        //创建目标对象
        V result = null;
        try {
            result = clazz.newInstance();
            //实现属性拷贝
            BeanUtils.copyProperties(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //返回结果
        return result;
    }

    //List<V>是返回值的类型
    public static <K,V> List<V> copyBeanList(List<K> list, Class<V> clazz) {
        return list.stream()
                .map(item -> copyBean(item, clazz))
                .collect(Collectors.toList());
    }
}
