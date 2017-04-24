package com.swust.kelab.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swust.kelab.domain.User;
import com.swust.kelab.repos.UserDAO;
import com.swust.kelab.utils.CommonUtil;

@Service(value = "loginService")
public class LoginService {

    final Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private UserDAO useDao;

    /**
     * 用户登陆操作事务，同时更新用户登陆状态为“1”登陆状态
     * 
     * @author yangzq
     * @param userLoginname
     * @return
     */
    @Transactional
    public User login(String userLoginname) {
        try {
            User user = new User();
            user.setUserLoginname(userLoginname);
            User findUser = null;
            List<User> users = useDao.findUsersByUser(user);
            if (users.size() > 0) {
                findUser = users.get(0);
            }
            return findUser;
        } catch (Exception e) {
            logger.error("loginService login error!\n" + e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * 退出登陆，更新用户状态为“0”，未登陆
     * 
     * @author yangzq
     * @param userId
     * @return
     */
    @Transactional
    public boolean loginOff(Integer userId) {
        try {
            if (userId > 0) {
                User user = new User();
                user.setUserId(userId);
                user.setUserLoginstate(CommonUtil.OUT);
                useDao.updateUserState(user);// 退出登陆，设置状态为“0”
                return true;
            }
        } catch (Exception e) {
            logger.error("loginService login error!\n" + e.getLocalizedMessage());
        }
        return false;
    }

    /**
     * 更新用户登录状态。
     * @param getUser
     */
    @Transactional
    public void updateLoginStatus(User getUser) {
        getUser.setUserLoginstate(CommonUtil.LOGIN);
        useDao.updateUserState(getUser);// 修改用户登陆状态
    }
}
