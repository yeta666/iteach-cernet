package com.swust.kelab.web.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.swust.kelab.domain.Attachment;
import com.swust.kelab.domain.Department;
import com.swust.kelab.domain.Role;
import com.swust.kelab.domain.User;
import com.swust.kelab.httpClient.Result;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.model.StudentMassedLearningInfoModel;
import com.swust.kelab.model.TeacherModel;
import com.swust.kelab.model.UserPersonalInfo;
import com.swust.kelab.model.UserSearchInfoModel;
import com.swust.kelab.oauth.AccessToken;
import com.swust.kelab.oauth.AccessTokenService;
import com.swust.kelab.service.AttachmentService;
import com.swust.kelab.service.DepartmentService;
import com.swust.kelab.service.LoginService;
import com.swust.kelab.service.SystemParameterService;
import com.swust.kelab.service.UserService;
import com.swust.kelab.utils.CommonUtil;
import com.swust.kelab.utils.CookieUtil;
import com.swust.kelab.web.json.JsonAndView;

/**
 * This Controller contains all user's operation after logged in.<br>
 * This Controller uses '/user/' as default namespace.<br>
 * Dependence on {@link com.swust.kelab.service.UserService}<br>
 * <br>
 * 
 * @version 1.0
 * @author Easonlian yangfindStudentByIdsZQ LuoHui
 * @See com.swust.kelab.service.UserService
 */
@Controller()
@RequestMapping("/user")
public class UserController {
	
	@Autowired
    private AccessTokenService accessTokenService;
	
    final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private UserService userService;
    @Autowired
    private AttachmentService attachmentService;
    @Resource
    private LoginService loginService;
    @Resource
    private SystemParameterService parameterService;

    @Resource
    HttpServletRequest request;

    /** Injected by Spring */
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private DepartmentService departmentService;
    /**
     * 找回密码验证问题
     * 
     * @param userRealname
     * @param depaId
     * @param gradId
     * @param clasId
     * @return
     */
    @RequestMapping(value = "/confirmUserInfo.do", method = RequestMethod.POST)
    public JsonAndView confirmUserInfo(
            @RequestParam(value = "userRealname") String userRealname,
            @RequestParam(value = "userIdNum", required = false) String userIdNum,
            @RequestParam(value = "depaId") Integer depaId, @RequestParam(value = "gradId") Integer gradId) {
        JsonAndView jav = new JsonAndView();
        String uLoginname = null;
        userIdNum = judgeParam(userIdNum);
        uLoginname = userService.confirmUserInfo(userRealname, depaId, userIdNum, gradId);
        return jav.addData("uLoginname", uLoginname);
    }

    private static String judgeParam(String arg) {
        if (arg == null || (arg != null && arg.equals("")))
            return null;
        else
            return arg;
    }

    /**
     * 登陆
     * 
     * @param user
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JsonAndView login(User user, String authcode, HttpServletResponse response) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (user.getUserLoginname() == null && user.getUserPwd() == null) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("登陆失败,请检查");
            return jv;
        }
        HttpSession session = request.getSession();
        String checkCode = (String) session.getAttribute("checkCode");
        if (!checkCode.equals(authcode)) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("验证码有误,请检查");
            return jv;
        }
        User getUser = loginService.login(user.getUserLoginname());
        String result = "fail";
        if (getUser != null) {
            // 验证是否登陆
            int uniqueLogin = Integer.parseInt(parameterService.attainValueByEnName("uniqueLogin").toString());
            if (uniqueLogin == 1 && getUser.getUserLoginstate().equals(CommonUtil.LOGIN)) {
                result = "isLogin";
            }
            // 用户未验证
            else if (!getUser.getUserVerify()) {
                result = "unVerify";
            }
            // 用户登录成功
            else if (getUser.getUserPwd().equals(user.getUserPwd())) {
                Department depart = null;
                try {
                    depart = departmentService.queryDepartmentById(getUser.getUserDepaId());
                } catch (Exception e) {
                    logger.error("addCookieToResponse error!\n" + e.getLocalizedMessage());
                }
                int departType = -1;
                if (depart != null) {
                    departType = depart.getDepaType();
                } else {
                    jv.setRet(false);
                    jv.setErrcode(601);
                    jv.setErrmsg("联系管理员");
                    return jv;
                }
                String sb = CookieUtil.buildCookieInfo(getUser, request, departType, depart.getDepaName());
                try {
                    CookieUtil.addCookieToResponse(sb, "user", "/", 24 * 60 * 60, response);
                    String userPhoto = "";
                    if (getUser.getUserCoverPictureId() != null && getUser.getUserCoverPictureId() > 0) {
                        Attachment attach = attachmentService.findAttachmentById(getUser.getUserCoverPictureId());
                        if (attach != null) {
                            userPhoto = attach.getAttaFilename();
                        }
                    }
                    CookieUtil.colsCookie(userPhoto, "photo", "/", "", 100000, response);
                    result = "success";
                    
                    //获取access_token
                    Result getAccessTokenResult = accessTokenService.getAccessTokenService();
                    if (!getAccessTokenResult.isSuccess()) {
                    	request.getSession().setAttribute("access_token", null);
                    } else {
                    	AccessToken accessToken = (AccessToken) getAccessTokenResult.getData();
                    	request.getSession().setAttribute("access_token", accessToken);
                    }
                    
                } catch (UnsupportedEncodingException e) {
                    logger.error("addCookieToResponse error!\n" + e.getLocalizedMessage());
                }
                session.setAttribute("userId", getUser.getUserId());
                loginService.updateLoginStatus(getUser);
            } else
                result = "passwordError";
        } else {
            result = "null";
        }
        jv.addData("result", result);
        jv.setRet(true);
        return jv;
    }

    /**
     * 退出登陆，主要更新用户登陆状态设置为“0”
     * 
     * @author yangzq
     * @param userId
     * @return
     */
    @RequestMapping(value = "/loginOff", method = RequestMethod.POST)
    public JsonAndView loginOff(@RequestParam("userId") Integer userId) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (userId == 0) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("退出失败");
            return jv;
        }
        boolean flag = loginService.loginOff(userId);
        jv.setRet(flag);
        HttpSession session = request.getSession();
        session.removeAttribute("userId");
        request.getSession().setAttribute("access_token", null);
        return jv;
    }

    /**
     * This method is to modify user's pwd.<br>
     * to visit : /handler/user/modifyUserPwd.html<br>
     * 
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @author EasonLian
     * @return JsonAndView
     */
    @RequestMapping(value = "/modifyUserPwd.html", method = RequestMethod.POST)
    public JsonAndView modifyUserPwd(@RequestParam("oldPwd") String oldPwd, @RequestParam("newPwd") String newPwd,int userId) {
        return new JsonAndView().addData("status", userService.modifyUserPwd(oldPwd, newPwd, userId) ? 1 : 0);
    }

    /**
     * This method is to find logged user by userId in session.<br>
     * to visit : /handler/user/findOneUser.html<br>
     * 
     * @author EasonLian
     * @return JsonAndView
     */
    @RequestMapping(value = "/findOneUser.html", method = RequestMethod.GET)
    public JsonAndView findOneUser(HttpServletRequest request) {
        JsonAndView jav = new JsonAndView();
        return jav.addData("status", userService.findOneUser(CookieUtil.getCookieUser(request).getUserId()));
    }

    /**
     * This method is allowed user to log out,clear the login record in session.<br>
     * to visit : /handler/user/loggout.html<br>
     * 
     * @author EasonLian
     * @return JsonAndView
     */
    @RequestMapping(value = "/loggout.html", method = RequestMethod.GET)
    public JsonAndView loggout(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            Integer userId=(Integer)(session.getAttribute("userId"));
            loginService.loginOff(userId);
            session.removeAttribute("userId");
            return new JsonAndView().addData("status", 1);
        } catch (Exception e) {
            return new JsonAndView().addData("status", 0);
        }
    }

    /**
     * 用户信息查询
     * 
     * @param query
     * @return
     */
    @RequestMapping(value = "/userInfoManage", method = RequestMethod.POST)
    public JsonAndView studentCourse(CommonQuery query, UserSearchInfoModel userSearch) {
        JsonAndView jv = new JsonAndView();
        // if(userSearch.getClassId()<0)
        // userSearch.setClassId(null);
        // if(userSearch.getCourseId()<0)
        // userSearch.setCourseId(null);
        // if(userSearch.getDeptId()<0)
        // userSearch.get
        QueryData queryData = null;
        try {
            queryData = userService.searchUsersInfo(query, userSearch);
        } catch (Exception e) {
            e.printStackTrace();
        }
        jv.addData("totalPage", queryData.getTotalPage());
        jv.addData("totalCount", queryData.getTotalCount());
        return jv.addData("pageData", queryData.getPageData());
    }

    /**
     * 根据角色id和机构id,获取用户列表,供筛选使用
     * 
     * @param roleId 0表示所有,1表示学生,2表示主讲教师,3表示辅导教师
     * @param depaId 用户所属机构id,0表示所有机构
     * @return 用户列表
     */
    @RequestMapping(value = "/viewUsersByRole", method = RequestMethod.POST)
    public JsonAndView viewUsersByRole(int roleId, int depaId) {
        JsonAndView jav = new JsonAndView();
        List<User> users = userService.viewUsersByRole(roleId, depaId);
        if (users != null) {
            jav.addData("users", users);
        } else {
            jav.setRet(false);
            jav.setErrcode(1);
            jav.setErrmsg("获取用户列表失败！");
        }
        return jav;
    }

    /**
     * 根据学校id,年级id,班级id,以及学生姓名<br>
     * 查询学生集中选课信息是否有与当前集中选课是否有冲突
     * 
     * @param commonQuery
     * @param clasId
     * @param gradeId
     * @param departId
     * @param stuName
     * @return
     * @author lujoCom
     */
    @RequestMapping(value = "/finStudentByIds", method = RequestMethod.POST)
    public JsonAndView findStudentByIds(CommonQuery commonQuery, StudentMassedLearningInfoModel smlInfo) {
        JsonAndView jv = new JsonAndView();
        if (smlInfo.getStuClasId() != null && smlInfo.getStuClasId() <= 0)
            smlInfo.setStuClasId(null);
        if (smlInfo.getStuGradeId() != null && smlInfo.getStuGradeId() <= 0)
            smlInfo.setStuGradeId(null);
        try {
            QueryData queryData = userService.findStudentByIds(commonQuery, smlInfo);

            if (queryData == null) {
                jv.setRet(false);
                jv.setErrmsg("查询数据为空，请重新输入查询条件");
                return jv;
            }
            jv.setRet(true);
            jv.addData("totalPage", queryData.getTotalPage());
            jv.addData("totalCount", queryData.getTotalCount());
            jv.addData("pageData", queryData.getPageData());
        } catch (Exception e) {
            e.printStackTrace();
            jv.setRet(false);
            jv.setErrmsg("查询发生意外性的错误，请检查查询数据格式");
        }
        return jv;
    }

    /**
     * 获取用户对应的角色列表(角色id,角色名)
     * 
     * @param userId 用户id
     * @return 角色列表
     * @see JsonAndView,Role
     */
    @RequestMapping(value = "/findRolesByUser", method = RequestMethod.POST)
    public JsonAndView viewUserRoles(int userId) {
        JsonAndView jav = new JsonAndView();
        List<Role> roles = userService.viewUserRoles(userId);
        if (roles != null) {
            jav.addData("roles", roles);
        } else {
            jav.setRet(false);
            jav.setErrcode(1);
            jav.setErrmsg("获取用户的角色列表失败！");
        }
        return jav;
    }

    /**
     * 通过id查询单个用户
     * 
     * @author ZhangXin
     * @param userId
     * @return
     * @throws UnsupportedEncodingException 
     */
    @RequestMapping(value = "/findUserById", method = RequestMethod.POST)
    public JsonAndView findUserById(Integer userId, HttpServletResponse respons) throws UnsupportedEncodingException {
        JsonAndView jav = new JsonAndView();
        User findUser = new User();
        findUser = userService.findOneUser(userId);
        String userPhoto = "";
        if (findUser.getUserCoverPictureId() != null && findUser.getUserCoverPictureId() > 0) {
            Attachment attach = attachmentService.findAttachmentById(findUser.getUserCoverPictureId());
            if (attach != null) {
                userPhoto = attach.getAttaFilename();
            }
        }
       CookieUtil.colsCookie(userPhoto, "photo", "/", "", 100000, respons);
        return jav.addData("data", findUser);
    }

    /**
     * 通过用户类型和所属机构，获取用户列表
     * 
     * @param userType 用户类型
     * @param depaId 机构id
     * @return 用户列表
     * @see JsonAndView，User
     */
    @RequestMapping(value = "/viewUsersByTypeAndDepa", method = RequestMethod.POST)
    public JsonAndView viewUsersByTypeAndDepa(int userType, int depaId) {
        JsonAndView jav = new JsonAndView();
        List<User> users = userService.viewUsersByTypeAndDepa(userType, depaId);
        if (users != null) {
            jav.addData("users", users);
        } else {
            jav.setRet(false);
            jav.setErrcode(1);
            jav.setErrmsg("获取指定类型的用户列表失败！");
        }
        return jav;
    }

    /**
     * 修改单个User部分信息
     * 
     * @author ZhangXin
     * @param info
     * @return
     */
    @RequestMapping(value = "/modifyPersonalInfo", method = RequestMethod.POST)
    public JsonAndView modifyPersonalInfo(UserPersonalInfo info) {
        JsonAndView jav = new JsonAndView();
        Map<String, Object> data = userService.modifyPersonalInfo(info);
        jav.addData("data", data);
        return jav;
    }

    /**
     * 在观看视频是保持session不销毁，定期发送请求
     * 
     * to visit : /user/keepAliveWhenWatchingVedio.do
     * 
     * @author easonlian
     */
    @RequestMapping(value = "/keepAliveWhenWatchingVideo.do", method = RequestMethod.GET)
    public JsonAndView keepAliveWhenWatchingVedio() {
        // do nothing,just keep session alive when the client is watching video.
        return new JsonAndView();
    }
    
    /**
     * 获取主讲教师的信息（包括个人信息，和所授课程）
     * 
     * @param teacherId 指定的教师id，若为非正整数，则获取所有主讲教师信息
     * @return 
     */
    @RequestMapping(value = "/viewMajorTeachers", method = RequestMethod.POST)
    public JsonAndView viewAllMajorTeachers(int teacherId){
        JsonAndView jav=new JsonAndView();
        List<TeacherModel> teachers = userService.viewAllMajorTeachers(teacherId);
        if (teachers != null) {
            jav.addData("users", teachers);
        } else {
            jav.setRet(false);
            jav.setErrcode(1);
            jav.setErrmsg("获取名师简介信息失败！");
        }
        return jav;
    }
}
