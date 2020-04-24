package com.xinshang.core.node;

import com.xinshang.core.constant.IsMenu;
import lombok.*;

import java.util.*;

/**
 * @author fengshuonan
 * @desc 菜单的节点
 * @date 2016年12月6日 上午11:34:17
 */

@Data
public class MenuNode implements Comparable {

    /**
     * 节点id
     */
    private Integer id;

    /**
     * 父节点
     */
    private Integer parentId;

    /**
     * 节点名称
     */
    private String name;

    /**
     * 按钮级别
     */
    private Integer level;

    /**
     * 按钮级别
     */
    private Integer isMenu;

    /**
     * 按钮的排序
     */
    private Integer sortNum;

    /**
     * 节点的url
     */
    private String url;

    /**
     * 节点图标
     */
    private String icon;

    /**
     * 子节点的集合
     */
    private List<MenuNode> children;

    /**
     * 查询子节点时候的临时集合
     */
    private List<MenuNode> linkedList = new ArrayList<MenuNode>();

    public MenuNode() {
        super();
    }

    public MenuNode(Integer id, Integer parentId) {
        super();
        this.id = id;
        this.parentId = parentId;
    }

    /**
     * 重写排序比较接口，首先根据等级排序，然后更具排序字段排序
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(Object o) {
        MenuNode menuNode = (MenuNode) o;
        Integer sortNum = menuNode.getSortNum();
        Integer level = menuNode.getLevel();
        if (sortNum == null) {
            sortNum = 0;
        }
        if (level == null) {
            level = 0;
        }
        if (this.level.compareTo(level) == 0) {
            return this.sortNum == null? 99 : this.sortNum.compareTo(sortNum);
        } else {
            return this.level.compareTo(level);
        }

    }

    /**
     * 构建页面菜单列表
     */
    public static List<MenuNode> buildTitle(List<MenuNode> nodes) {
        if (nodes.size() <= 0) {
            return nodes;
        }
        //剔除非菜单
        nodes.removeIf(node -> node.getIsMenu() != IsMenu.YES.getCode());
        //对菜单排序，返回列表按菜单等级，序号的排序方式排列
        Collections.sort(nodes);
        return mergeList(nodes, nodes.get(nodes.size() - 1).getLevel(), null);
    }

    /**
     * 递归合并数组为子数组，最后返回第一层
     *
     * @param menuList
     * @param listMap
     * @return
     */
    private static List<MenuNode> mergeList(List<MenuNode> menuList, int rank, Map<Integer, List<MenuNode>> listMap) {
        //保存当次调用总共合并了多少元素
        int n;
        //保存当次调用总共合并出来的list
        Map<Integer, List<MenuNode>> currentMap = new HashMap<>();
        //由于按等级从小到大排序，需要从后往前排序
        //判断该节点是否属于当前循环的等级,不等于则跳出循环
        for (n = menuList.size() - 1; n >=0&&menuList.get(n).getLevel() == rank; n--) {
            //判断之前的调用是否有返回以该节点的id为key的map，有则设置为children列表。
            if (listMap != null && listMap.get(menuList.get(n).getId()) != null) {
                menuList.get(n).setChildren(listMap.get(menuList.get(n).getId()));
            }
            if (menuList.get(n).getParentId()!=null&&menuList.get(n).getParentId()!=0) {
                //判断当前节点所属的pid是否已经创建了以该pid为key的键值对，没有则创建新的链表
                currentMap.computeIfAbsent(menuList.get(n).getParentId(), k -> new LinkedList<>());
                //将该节点插入到对应的list的头部
                currentMap.get(menuList.get(n).getParentId()).add(0, menuList.get(n));
            }
        }
        if (n <0) {
            return menuList;
        } else {
            return mergeList(menuList.subList(0, n+1), menuList.get(n).getLevel(), currentMap);
        }
    }


}
