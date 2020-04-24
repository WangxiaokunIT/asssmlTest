package com.xinshang.core.aop;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.plugins.Page;
import com.xinshang.constant.Constants;
import com.xinshang.core.annotation.DictField;
import com.xinshang.core.annotation.JoinField;
import com.xinshang.core.common.annotion.BussinessLog;
import com.xinshang.core.common.constant.dictmap.base.AbstractDictMap;
import com.xinshang.core.common.constant.factory.ConstantFactory;
import com.xinshang.core.log.LogManager;
import com.xinshang.core.log.LogObjectHolder;
import com.xinshang.core.log.factory.LogTaskFactory;
import com.xinshang.core.shiro.ShiroKit;
import com.xinshang.core.shiro.ShiroUser;
import com.xinshang.core.support.HttpKit;
import com.xinshang.core.util.Contrast;
import com.xinshang.core.util.RedisUtil;
import com.xinshang.core.util.ReflectUtil;
import com.xinshang.core.util.ToolUtil;
import com.xinshang.modular.system.model.GeneralTable;
import com.xinshang.modular.system.model.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 拓展属性切面
 *
 * @author 张佳佳
 * @date 2019年11月27日16:32:52
 */
@Aspect
@Component
@Slf4j
public class ExtendAttrAop {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 关联用户表通用字段
     */
    protected static List<String> userGeneralFields=Arrays.asList("userId", "creator", "modifier");

    /**
     * 定义一个切点，后面跟随一个表达式，表达式可以定义为某个 package 下的方法，也可以是自定义注解等；
     */
    @Pointcut("execution(public * com.xinshang.modular..*.*ServiceImpl.*(..))")
    public void myService() {
    }

    @Pointcut("execution(public * com.baomidou.mybatisplus.service.impl.ServiceImpl.*(..))")
    public void mybatisPlusService() {
    }

    @Pointcut("myService() || mybatisPlusService()")
    public void cutService() {
    }


    @Around("cutService()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {

        long time1=System.currentTimeMillis();
        Object obj = point.proceed();
        long time2=System.currentTimeMillis();
        log.debug("执行方法耗时："+(time2-time1)+"ms");
        long start=System.currentTimeMillis();
        Object result = handle(obj);
        long end=System.currentTimeMillis();
        log.debug("拓展额外数据耗时"+(end-start)+"ms");
        return result;
    }

    /**
     * 包装一个list或者对象,让其增加额外字典等属性
     * @param obj
     * @return Object
     * @throws Exception
     */
    private Object handle(Object obj) {
        //初始化redis字典
        ConstantFactory.me().cacheDict();
        //动态代理工具类
        ReflectUtil reflectUtil = new ReflectUtil();
        //存放额外属性
        Map<String, Object> extendProperties = new HashMap(0);
        //存放从数据库查询出来的数据
        Map<String, Object> dbData = new HashMap(0);
        try {
            if (obj instanceof List) {
                //原list
                List sourceList = (List) obj;
                //存放结果的list
                List resultList = new ArrayList();
                for (Object item : sourceList) {
                    resultList.add(warpHandler(item,extendProperties,dbData,reflectUtil));
                }
                obj=resultList;

            } else if (obj instanceof Model) {
                obj = warpHandler(obj, extendProperties, dbData, reflectUtil);
            } else if (obj instanceof Map) {
                Map map = (Map)obj;
                for (Object o : map.keySet()) {
                    Object o1 = map.get(o);
                    if(o1 instanceof Model){
                        map.put(o,warpHandler(o1, extendProperties, dbData, reflectUtil));
                    }
                }
            }else if (obj instanceof Page) {
                Page page = (Page)obj;
                //如果page数据为空就返回,不为空就遍历
                if(page.getRecords()!=null&&page.getRecords().size()>0){
                    //原list
                    List sourceList = page.getRecords();
                    //存放结果的list
                    List resultList = new ArrayList();
                    for (Object item : sourceList) {
                        resultList.add(warpHandler(item,extendProperties,dbData,reflectUtil));
                    }
                    page.setRecords(resultList);
                    obj = page;
                }
            }
        } catch (Exception e) {
            log.error("包装对象出现异常:" + e.getMessage(), e);
        }
       return obj;
    }

    /**
     * 动态添加属性处理
     * @param object 要添加属性的对象
     * @param extendProperties 扩展属性
     * @param dbData 从数据库查询出来的数据
     * @param reflectUtil 属性扩展工具类
     * @return
     * @throws Exception
     */
    Object warpHandler(Object object,Map<String, Object> extendProperties,Map<String, Object> dbData,ReflectUtil reflectUtil) throws Exception{
        //该属性的值
        String value = null;
        //清空属性map
        extendProperties.clear();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.get(object) == null|| StrUtil.isEmpty(field.get(object).toString())||"serialVersionUID".equals(field.getName())) {
                continue;
            }
            value = field.get(object).toString();
            //判断属性是否标注了@DictField
            if (field.isAnnotationPresent(DictField.class)) {
                //获取@DictField的值
                DictField dictField = field.getAnnotation(DictField.class);
                //获取@DictField的值 参数值
                String dictCode = dictField.value();
                if (StrUtil.isBlank(dictCode)){
                    continue;
                }

                //从redis缓存中读取所需要的字段值
                Object dictName = redisUtil.hget(Constants.asmall_SYSTEM_DICT_KEY,dictCode+Constants.asmall_REDIS_KEY_SEPARATOR_SYMBOL+value);
                extendProperties.put(ToolUtil.replaceFieldIdToName(field.getName()),dictName==null?"":dictName);
            }

            //判断属性是否标注了@JoinField
            if (field.isAnnotationPresent(JoinField.class)) {
                //获取@JoinField的值
                JoinField joinField = field.getAnnotation(JoinField.class);
                //获取@JoinField的表名
                String tableName = joinField.tableName();
                //要关联的字段名
                String relationColumn = joinField.relationColumn();
                //取出的字段名
                String targetColumn = joinField.targetColumn();
                //如果其中任何一个为空就什么也不做
                if (StrUtil.isBlank(tableName)||StrUtil.isBlank(targetColumn)||StrUtil.isBlank(relationColumn)){
                    continue;
                }

                if(dbData.get(tableName+Constants.asmall_REDIS_KEY_SEPARATOR_SYMBOL+relationColumn+Constants.asmall_REDIS_KEY_SEPARATOR_SYMBOL+targetColumn)==null){
                    List<GeneralTable> generalTableList = ConstantFactory.me().findInTable(tableName,relationColumn,targetColumn);
                    Map<String,Object> dataTempMap = new HashMap();
                    for(GeneralTable gt : generalTableList){
                        dataTempMap.put(gt.getRelationColumn(),gt.getTargetColumn());
                    }
                    dbData.put(tableName+Constants.asmall_REDIS_KEY_SEPARATOR_SYMBOL+relationColumn+Constants.asmall_REDIS_KEY_SEPARATOR_SYMBOL+targetColumn,dataTempMap);
                }

                Map<String,Object> dataMap = (Map)dbData.get(tableName+Constants.asmall_REDIS_KEY_SEPARATOR_SYMBOL+relationColumn+Constants.asmall_REDIS_KEY_SEPARATOR_SYMBOL+targetColumn);
                extendProperties.put(ToolUtil.replaceFieldIdToName(field.getName()), dataMap.get(value)==null?"":dataMap.get(value));
            }

            //判断是否是增加人或者是修改人等跟用户管理的字段
            if (userGeneralFields.contains(field.getName())) {
                //如果redis中没有就从数据库中查询出来放到redis中
                if(!redisUtil.hasKey(Constants.asmall_SYSTEM_USER_KEY)){
                    List<User> list = ConstantFactory.me().listAllUserIdAndName();
                    Map<String,Object> userTempMap = new HashMap();
                    for(User user : list){
                        userTempMap.put(user.getId().toString(),user.getName());
                    }
                    redisUtil.hmset(Constants.asmall_SYSTEM_USER_KEY,userTempMap);
                }
                Object userName = redisUtil.hget(Constants.asmall_SYSTEM_USER_KEY,value);
                extendProperties.put(ToolUtil.replaceFieldIdToName(field.getName()), userName==null?"":userName);
            }
        }

        //如果有额外属性,就添加动态对象,否则添加原对象
        if (extendProperties.size() > 0) {
            return reflectUtil.getTarget(object, extendProperties);
        }
        return object;
    }

    /**
     * 包装一个对象,让该对象的默认字段都自动赋值、创建人、创建时间、修改人、修改时间
     * @param model
     * @return Object
     * @throws Exception
     */
    public Model addWarpDefaultValue(Model model){
        Field[] fields = model.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);

                //判断是否是添加时间
                if ("gmtCreate".equals(field.getName()) || "createTime".equals(field.getName())) {
                    field.set(model,new Date());
                }

                //判断是否是增加人
                if ("creator".equals(field.getName())) {
                    field.set(model, ShiroKit.getUser().getId());
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return model;
    }

    /**
     * 包装一个对象,让该对象的默认字段都自动赋值、修改人、修改时间
     * @param model
     * @return Object
     * @throws Exception
     */
    public Model updateWarpDefaultValue(Model model){
        Field[] fields = model.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                //判断是否是是修改时间
                if ("gmtModified".equals(field.getName()) || "updateTime".equals(field.getName())) {
                    field.set(model,new Date());
                }
                //判断是否是是修改人
                if ("modifier".equals(field.getName())) {
                    field.set(model, ShiroKit.getUser().getId());
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return model;
    }

}