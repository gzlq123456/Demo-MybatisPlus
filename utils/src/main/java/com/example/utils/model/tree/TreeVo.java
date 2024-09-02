package com.example.utils.model.tree;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 树结构
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreeVo implements Serializable {

    private String id;

    private String name;

    private String dsc;

    private String levelId;

    private String parentId;

    private List<TreeVo> children;

    public TreeVo(String id, String name, String levelId, String parentId) {
        this.id = id;
        this.name = name;
        this.levelId = levelId;
        this.parentId = parentId;
    }


    public List<TreeVo> getChildren(){
        return children;
    }

}
