package com.xinshang.core.beetl;

import com.xinshang.core.tag.DictSelectorConTag;
import com.xinshang.core.tag.DictSelectorTag;
import com.xinshang.core.util.KaptchaUtil;
import com.xinshang.core.util.ToolUtil;
import org.beetl.core.Context;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

/**
 * beetl拓展配置,绑定一些工具类,方便在模板中直接调用
 *
 * @author zhangjiajia
 * @since 2018/2/22 21:03
 */
public class BeetlGroupUtilConfig extends BeetlGroupUtilConfiguration {

    @Autowired
    Environment env;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    DictSelectorTag dictSelectorTag;

    @Autowired
    DictSelectorConTag dictSelectorConTag;

    @Override
    public void initOther() {
        groupTemplate.registerFunctionPackage("shiro", new ShiroExt());
        groupTemplate.registerFunctionPackage("tool", new ToolUtil());
        groupTemplate.registerFunctionPackage("kaptcha", new KaptchaUtil());
        groupTemplate.registerFunctionPackage("secureUtil", new EncryptionExt());
        groupTemplate.registerTagFactory("dictSelector", ()->dictSelectorTag);
        groupTemplate.registerTagFactory("dictSelectorCon", ()->dictSelectorConTag);
        groupTemplate.registerFunction("env",(Object[] paras, Context ctx)->{
            String key = (String)paras[0];
            String value =  env.getProperty(key);
            if(value!=null) {
                try {
                    return new String(value.getBytes("iso8859-1"), "UTF-8");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if(paras.length==2) {
                return (String)paras[1];
            }
            return null;
        });
    }
}
