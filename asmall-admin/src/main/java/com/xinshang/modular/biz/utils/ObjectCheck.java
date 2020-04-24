package com.xinshang.modular.biz.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.function.Predicate;

/**
 * 对象校验
 * 注：未完成的功能
 * @author lyk
 */
public class ObjectCheck {

    public static boolean check(Object model, Predicate<Object> predicate) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        //获取实体类的所有属性，返回Field数组
        Field[] field = model.getClass().getDeclaredFields();
        //遍历所有属性
        for (int j = 0; j < field.length; j++) {
            //获取属性的名字
            String name = field[j].getName();

            System.out.println("attribute name:" + name);
            //将属性的首字符大写，方便构造get，set方法
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            //获取属性的类型
            String type = field[j].getGenericType().toString();
            //如果type是类类型，则前面包含"class "，后面跟类名
            if (type.equals("class java.lang.String")) {
                Method m = model.getClass().getMethod("get" + name);
                //调用getter方法获取属性值
                String value = (String) m.invoke(model);
                if (value != null) {

                    System.out.println("attribute value:" + value);
                }
            }
            if (type.equals("class java.lang.Integer")) {
                Method m = model.getClass().getMethod("get" + name);
                Integer value = (Integer) m.invoke(model);
                if (value != null) {
                    System.out.println("attribute value:" + value);
                }
            }
            if (type.equals("class java.lang.Short")) {
                Method m = model.getClass().getMethod("get" + name);
                Short value = (Short) m.invoke(model);
                if (value != null) {
                    System.out.println("attribute value:" + value);
                }
            }
            if (type.equals("class java.lang.Double")) {
                Method m = model.getClass().getMethod("get" + name);
                Double value = (Double) m.invoke(model);
                if (value != null) {
                    System.out.println("attribute value:" + value);
                }
            }
            if (type.equals("class java.lang.Boolean")) {
                Method m = model.getClass().getMethod("get" + name);
                Boolean value = (Boolean) m.invoke(model);
                if (value != null) {
                    System.out.println("attribute value:" + value);
                }
            }
            if (type.equals("class java.util.Date")) {
                Method m = model.getClass().getMethod("get" + name);
                Date value = (Date) m.invoke(model);
                if (value != null) {
                    System.out.println("attribute value:" + value.toLocaleString());
                }
            }
        }

        return false;
    }
}
