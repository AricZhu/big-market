package com.platform.bigmarket.test;

import com.alibaba.fastjson.JSON;
import com.platform.bigmarket.infrastructure.persistent.dao.IAwardDao;
import com.platform.bigmarket.infrastructure.persistent.po.Award;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class InfrastructureTest {
    @Autowired
    private IAwardDao awardDao;

    @Test
    public void test_queryAwardList() {
        List<Award> awards = awardDao.queryAwardList();
        log.info("query award list: {}", JSON.toJSONString(awards));
    }
}
