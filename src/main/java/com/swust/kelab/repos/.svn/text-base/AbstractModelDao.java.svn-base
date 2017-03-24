package com.swust.kelab.repos;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qunar.base.meerkat.dao.QueryDAO;
import com.qunar.base.meerkat.dao.UpdateDAO;

/**
 * 抽象模型数据访问
 * 
 * @author zhongyuan.zhang
 */
public abstract class AbstractModelDao {

    /** 日志 */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /** 查询DAO */
    protected QueryDAO queryDAO;

    /** 更新DAO */
    protected UpdateDAO updateDAO;

    @Resource
    public void setQueryDAO(QueryDAO queryDAO) {
        this.queryDAO = queryDAO;
    }

    @Resource
    public void setUpdateDAO(UpdateDAO updateDAO) {
        this.updateDAO = updateDAO;
    }
}
