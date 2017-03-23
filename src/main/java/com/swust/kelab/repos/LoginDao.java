package com.swust.kelab.repos;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;


import com.swust.kelab.domain.User;

@Repository
public class LoginDao {
	 @Resource
	 private SqlSession sqlSession;
	 
	 
	 public User login(String userName,String password){
		// sqlSession.selectList(statement);
		 return null;
	 }
}
