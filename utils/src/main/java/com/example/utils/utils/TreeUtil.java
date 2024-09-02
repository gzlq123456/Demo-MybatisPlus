package com.example.utils.utils;


import com.example.utils.model.tree.TreeVo;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 把数据转换成树形结构
 */
public class TreeUtil {

    /**
     *
     * @param treeNodes
     * @param parentId
     * @return
     */
    public static List<TreeVo> Recursive(List<TreeVo> treeNodes, String parentId){
        List<TreeVo> trees = new ArrayList<TreeVo>();

        //获取所有数据treeNodes
        for (TreeVo treeNode : treeNodes) {
            //传入需要显示的，递归级别领头的父id，parentId
            if ((StringUtils.isNotBlank(parentId) && parentId.equals(treeNode.getParentId())) || null == treeNode.getParentId() ) {
                trees.add(findChildren(treeNode,treeNodes));
            }
        }
        return trees;
    }

    public static List<TreeVo> Recursive(List<TreeVo> treeNodes){
        List<TreeVo> trees = new ArrayList<TreeVo>();

        //获取所有数据treeNodes
        for (TreeVo treeNode : treeNodes) {
            //如果 parentId 等于 null，则为根节点
            if (null == treeNode.getParentId()) {
                trees.add(findChildren(treeNode,treeNodes));
            }
        }
        return trees;
    }

    /**
     * 递归查找地址子节点
     * @param treeNodes
     * @return
     */
    public static TreeVo findChildren(TreeVo treeNode,List<TreeVo> treeNodes) {
        for (TreeVo it : treeNodes) {
            if(treeNode.getId().equals(it.getParentId())) {
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<TreeVo>());
                }
                treeNode.getChildren().add(findChildren(it,treeNodes));
            }
        }
        return treeNode;
    }
}




