package com.example.utils.model.tree;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tree")
public class Tree {

    private String id;

    private String name;

    private String dsc;

    private String levelId;

    private String parentId;

    private String orderId;
}
