package com.xinshang.core.node;

import lombok.*;

/**
 * 
 * jquery ztree 插件的节点
 * 
 * @author fengshuonan
 * @date 2017年2月17日 下午8:25:14
 */

@Data
public class ZTreeNode {

	/**节点id**/
	private Long id;

	/**父节点id**/
	private Long parentId;

	 /**节点名称**/
	private String name;

 	/**是否打开节点**/
	private Boolean open;

 	/**是否被选中**/
	private Boolean checked;

	/**图标**/
	private String icon;

	/**类型**/
	private Integer type;

	/**下级数量**/
	private Integer childrenNum=0;

	/**是否父节点**/
	private String isParent;

	public static ZTreeNode createParent(){
		ZTreeNode zTreeNode = new ZTreeNode();
		zTreeNode.setChecked(true);
		zTreeNode.setId(0L);
		zTreeNode.setName("顶级");
		zTreeNode.setOpen(false);
		zTreeNode.setParentId(0L);
		zTreeNode.setType(1);
		zTreeNode.setIsParent("true");
		return zTreeNode;
	}
}
