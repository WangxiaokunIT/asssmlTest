package com.xinshang.generator.engine.config;

import com.xinshang.core.constant.IsMenu;
import com.xinshang.core.util.ToolUtil;
import lombok.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 全局配置
 *
 * @author fengshuonan
 * @date 2017-05-08 20:21
 */

@Data
public class SqlConfig {

    private String sqlPathTemplate;

    private ContextConfig contextConfig;

    private Connection connection;

    private String parentMenuName;

    private List<Menu> menus = new ArrayList<>(6);

    private Integer maxId;

    public void init() {

        this.sqlPathTemplate = "\\src\\main\\java\\{}.sql";

        if (parentMenuName == null) {
            return;
        }

        //根据父菜单查询数据库中的parentId和seq
        String[] idAndSeq = getIdAndSeq();
        if (idAndSeq == null) {
            System.err.println("父级菜单名称输入有误!!!!");
            return;
        }

        //业务菜单
        Menu menu = new Menu();
        menu.setId(maxId++);
        menu.setCode(contextConfig.getBizEnName());
        menu.setParentId(Integer.valueOf(idAndSeq[0]));
        menu.setSeq(idAndSeq[2] + menu.getId()+".");
        menu.setName(contextConfig.getBizChName());
        menu.setIcon("");
        menu.setUrl("/" + contextConfig.getBizEnName());
        menu.setSortNum(99);
        menu.setLevel(Integer.parseInt(idAndSeq[1])+1);
        menu.setIsMenu(IsMenu.YES.getCode());
        menu.setState(1);
        menu.setIsOpen(0);
        menus.add(menu);

        //列表
        Menu list = createSubMenu(menu);
        list.setCode(contextConfig.getBizEnName() + "_list");
        list.setName(contextConfig.getBizChName() + "列表");
        list.setUrl("/" + contextConfig.getBizEnName() + "/list");
        menus.add(list);

        //添加
        Menu add = createSubMenu(menu);
        add.setCode(contextConfig.getBizEnName() + "_add");
        add.setName(contextConfig.getBizChName() + "添加");
        add.setUrl("/" + contextConfig.getBizEnName() + "/add");
        menus.add(add);

        //更新
        Menu update = createSubMenu(menu);
        update.setCode(contextConfig.getBizEnName() + "_update");
        update.setName(contextConfig.getBizChName() + "更新");
        update.setUrl("/" + contextConfig.getBizEnName() + "/update");
        menus.add(update);

        //删除
        Menu delete = createSubMenu(menu);
        delete.setCode(contextConfig.getBizEnName() + "_delete");
        delete.setName(contextConfig.getBizChName() + "删除");
        delete.setUrl("/" + contextConfig.getBizEnName() + "/delete");
        menus.add(delete);

        //详情
        Menu detail = createSubMenu(menu);
        detail.setCode(contextConfig.getBizEnName() + "_detail");
        detail.setName(contextConfig.getBizChName() + "详情");
        detail.setUrl("/" + contextConfig.getBizEnName() + "/detail");
        menus.add(detail);
    }

    private Menu createSubMenu(Menu parentMenu) {
        Menu menu = new Menu();
        menu.setId(maxId++);
        menu.setParentId(parentMenu.getId());
        menu.setSeq(parentMenu.getSeq() + menu.getId()+ "." );
        menu.setIcon("");
        menu.setSortNum(99);
        menu.setLevel(parentMenu.getLevel() + 1);
        menu.setIsMenu(IsMenu.NO.getCode());
        menu.setState(1);
        menu.setIsOpen(0);
        return menu;
    }

    public String[] getIdAndSeq() {
        PreparedStatement preparedStatement = null;
        ResultSet results = null;
        try {
            preparedStatement = connection.prepareStatement("select max(id) as max_id from sys_menu");
            results = preparedStatement.executeQuery();
            while (results.next()) {
                maxId = results.getInt("max_id")+1;
            }
            // id,level,seq
            if ("顶级".equals(parentMenuName)) {
                return new String[]{"0", "1", "0."};
            }

            preparedStatement = connection.prepareStatement("select id,level,seq from sys_menu where name = ?");
            preparedStatement.setString(1,  parentMenuName);
             results = preparedStatement.executeQuery();
            while (results.next()) {
                Integer id = results.getInt("id");
                Integer level = results.getInt("level");
                String seq = results.getString("seq");
                if (ToolUtil.isNotEmpty(id) && ToolUtil.isNotEmpty(seq)) {
                    String[] strings = {id.toString(),level.toString(), seq};
                    return strings;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
