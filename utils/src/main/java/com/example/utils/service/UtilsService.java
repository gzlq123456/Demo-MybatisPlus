package com.example.utils.service;

import com.example.utils.dao.TreeDao;
import com.example.utils.model.tree.Tree;
import com.example.utils.model.tree.TreeVo;
import com.example.utils.utils.TreeUtil;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UtilsService {

    private final TreeDao treeDao;

    public List<TreeVo> getTrees(){
        MPJLambdaWrapper<Tree> query = new MPJLambdaWrapper<>();
        query.orderByAsc(Tree::getOrderId);
        List<TreeVo> list = treeDao.selectJoinList(TreeVo.class, query);
        List<TreeVo> treeVos = TreeUtil.Recursive(list);
        return treeVos;
    }
}
