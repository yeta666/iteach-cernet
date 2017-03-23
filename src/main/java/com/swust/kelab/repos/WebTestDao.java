package com.swust.kelab.repos;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository
public class WebTestDao {
    @Resource
    private SqlSession sqlSession;

    public String query() {
        Integer count = sqlSession.selectOne("webtest.query");
        return count + "";
    }
}
