package com.example.utils;

import com.example.utils.model.tree.TreeVo;
import com.example.utils.service.UtilsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UtilsApplicationTests {

    private final UtilsService utilsService;

    @Test
    public void treeTest() {
        List<TreeVo> trees = utilsService.getTrees();
        log.info("trees: {}", trees);
    }

}
