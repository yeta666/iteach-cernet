package com.swust.kelab.web.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.swust.kelab.domain.ExamQuestion;
import com.swust.kelab.domain.User;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.CourseModel;
import com.swust.kelab.model.ExamInfo;
import com.swust.kelab.model.ExamPaperModel;
import com.swust.kelab.model.ExamQuestionModel;
import com.swust.kelab.model.ExamResultModel;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.service.CourseService;
import com.swust.kelab.service.LogDBService;
import com.swust.kelab.service.TrainService;
import com.swust.kelab.utils.CookieUtil;
import com.swust.kelab.web.json.JsonAndView;

@Controller
@RequestMapping("/train")
public class TrainController {
    final Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private CourseService courseService;

    @Resource
    private TrainService trainService;

    @Resource
    private LogDBService logDBService;

    /**
     * @author JesseHe Yangzq
     * @param userId
     *            用户id
     * @param courState
     *            课程状态
     * @param query
     *            查询条件
     * @return
     */
    @RequestMapping(value = "/viewAllCourse", method = RequestMethod.POST)
    public JsonAndView viewAllUser(int userId, int courState, CommonQuery query) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (query == null || userId <= 0) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        QueryData queryData = courseService.viewAllCourse(userId, courState,
                query);
        jv.addData("totalPage", queryData.getTotalPage());
        jv.addData("totalCount", queryData.getTotalCount());
        jv.addData("pageData", queryData.getPageData());
        return jv;
    }

    /**
     * @author JesseHe Yangzq
     * @param userId
     *            用户id
     * @param courState
     *            课程状态
     * @param query
     *            查询条件
     * @return
     */
    @RequestMapping(value = "/viewCourseInfo", method = RequestMethod.POST)
    public JsonAndView viewCourseInfo(Integer courId) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (courId <= 0) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        CourseModel courseModel = courseService.viewCourseInfo(courId);
        jv.addData("data", courseModel);
        return jv;
    }

    /**
     * @author JesseHe Yangzq
     * @param courId
     *            课程id
     * @param query
     *            查询条件
     * @return
     */
    @RequestMapping(value = "/viewAllTest", method = RequestMethod.POST)
    public JsonAndView viewAllTest(@Param("userId") int userId,
            @Param("courseId") int courseId, CommonQuery query) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (query == null) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        QueryData queryData = courseService
                .viewAllTest(userId, courseId, query);
        jv.addData("totalPage", queryData.getTotalPage());
        jv.addData("totalCount", queryData.getTotalCount());
        jv.addData("pageData", queryData.getPageData());
        return jv;
    }

    /**
     * 获取测试的试卷
     * 
     * @param paperId
     * @return
     */
    @RequestMapping(value = "/viewExamPaper", method = RequestMethod.POST)
    public JsonAndView viewExamPaper(@Param("paperId") int paperId,
            @Param("examinId") int examinId) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (paperId <= 0) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        Map<String, Object> map = courseService
                .viewExamPaper(paperId, examinId);
        jv.addData("pageData", map);
        return jv;
    }


    /**
     * 记录开始考试日志
     * @author yangzq
     * @param request
     * @return
     */
    @RequestMapping(value = "/logBeginTest", method = RequestMethod.POST)
    public JsonAndView logBeginTest(HttpServletRequest request,@Param("testName")String testName){
        JsonAndView jv = new JsonAndView();
        try {
            logDBService.insertNewLog(request, 4, "课程测试", "开始测试:"+testName);
        } catch (Exception e) {
            jv.setRet(false);
        }
        return jv;
    }



    /**
     * 考试结束，将考生答题答案返回给数据库
     * 
     * @author yangzq
     * @param stuAnswerValue
     * @return
     */
    @RequestMapping(value = "/submitResult", method = RequestMethod.POST)
    public JsonAndView submitResult(HttpServletRequest request,
            @Param("examinName")String examinName,@Param("courseId")Integer courseId,
            ExamResultModel resultModel) {
        JsonAndView jv = new JsonAndView();
        // 对象数组
        Map<String, Object> map = null;
        try {
            map = trainService.submitResult(courseId,resultModel);
            logDBService.insertNewLog(request, 1, "课程测试", "测试:"+examinName+"完成,提交结果");
        } catch (Exception e) {
            jv.setRet(false);
            map.put("f", "f");
        }
        jv.addData("pageData", map);
        return jv;
    }

    /**
     * 教师评卷
     * 
     * @author yangzq
     * @param markScoreArr
     * @return
     */
    @RequestMapping(value = "/markScore", method = RequestMethod.POST)
    public JsonAndView markScore(HttpServletRequest request,@Param("userId") int userId,
            @Param("exinId") int exinId,@Param("rateId") int rateId,@Param("examinName")String examinName,
            @Param("markScoreArr") String markScoreArr) {
        JsonAndView jv = new JsonAndView();
        if (userId == 0 || exinId == 0 || markScoreArr == null
                || markScoreArr == "") {
            jv.setRet(false);
            jv.setErrmsg("评卷失败！参数加载有误！");
            return jv;
        }
        try {
            trainService.markScore(userId, exinId, rateId ,markScoreArr);
            logDBService.insertNewLog(request, 1, "成绩管理", "评卷:"+examinName);
        } catch (Exception e) {
            e.printStackTrace();
            jv.setRet(false);
            jv.setErrmsg("评卷失败！系统出错！请重新登陆系统试一试！");
            return jv;
        }
        return jv;
    }

    /**
     * 加载考试结果数据，用于展现考试用户的试卷
     * 
     * @author yangzq
     * @param exinId
     * @param exinExpaId
     * @param userId
     * @return
     */
    @RequestMapping(value = "/loadExamResult", method = RequestMethod.POST)
    public JsonAndView loadExamResult(HttpServletRequest request,@Param("exinId") int exinId,
            @Param("examinName")String examinName,@Param("userId") int userId,
            @Param("postCol") int postCol) {
        JsonAndView jv = new JsonAndView();
        if (exinId == 0 || userId == 0) {
            jv.setRet(false);
            jv.setErrmsg("参数获取错误!");
            return jv;
        }
        ExamResultModel examResult = trainService.loadExamResult(exinId, userId);
        String where = "";
        if(postCol == 1){
            where = "我的成绩";
        }else if(postCol == 2){
            where = "成绩管理";
        }
        logDBService.insertNewLog(request, 4, where, "加载试卷:"+examinName);
        jv.addData("result", examResult);
        return jv;
    }


    /**
     * 课程测试管理，展现所有已配置好的考试信息
     * 
     * @author yangzq
     * @param quCourse
     * @param query
     * @return
     */
    @RequestMapping(value = "/viewAllExamInfo", method = RequestMethod.POST)
    public JsonAndView viewAllExamInfo(HttpServletRequest request,Integer quCourse, CommonQuery query,
            Integer departId, Integer userId, Integer departType,
            Integer userType) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (query == null) {
            errorData(jv);
            return jv;
        }
        // 对象数组
        QueryData queryData = trainService.viewAllExamInfo(quCourse, query,
                departId, userId, departType, userType);
        //logDBService.insertNewLog(request, 4, "课程测试管理", "加载课程测试");
        jv.addData("totalPage", queryData.getTotalPage());
        jv.addData("totalCount", queryData.getTotalCount());
        jv.addData("pageData", queryData.getPageData());
        return jv;
    }

    /**
     * 我的成绩察看
     * 
     * @author yangzq
     * @param quCourse
     * @param query
     * @return
     */
    @RequestMapping(value = "/viewAllMyGrades", method = RequestMethod.POST)
    public JsonAndView viewAllMyGrades(HttpServletRequest request,
            Integer quCourse, CommonQuery query) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (query == null) {
            errorData(jv);
            return jv;
        }
        User user = CookieUtil.getCookieUser(request);
        // 对象数组
        QueryData queryData = trainService.viewAllMyGrades(user.getUserId(),
                quCourse, query);
        //logDBService.insertNewLog(request, 4, "我的成绩", "加载用户测试成绩");
        jv.addData("totalPage", queryData.getTotalPage());
        jv.addData("totalCount", queryData.getTotalCount());
        jv.addData("pageData", queryData.getPageData());
        return jv;
    }

    /**
     * 试题管理加载信息
     * 
     * @author yangzq
     * @param query
     * @return
     */
    @RequestMapping(value = "/viewQuestions", method = RequestMethod.POST)
    public JsonAndView viewQuestions(HttpServletRequest request,
            @Param("userId")int userId,@Param("departId")int departId,
            @Param("departType")int departType,@Param("userType")int userType,
            @Param("postType")int postType,
            int quCourse, int quType, CommonQuery query) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (query == null) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        QueryData queryData = trainService.queryQuestions(userId,departId,departType,userType,
                quCourse, quType, query);
        String where = "";
        if(postType == 1){
            where = "题库管理";
        }else {
            where = "试卷管理";
        }
        //logDBService.insertNewLog(request, 4, where, "加载题库数据");
        jv.addData("totalPage", queryData.getTotalPage());
        jv.addData("totalCount", queryData.getTotalCount());
        jv.addData("pageData", queryData.getPageData());
        return jv;
    }

    /**
     * 试卷信息加载
     * 
     * @author yangzq
     * @param query
     * @return
     */
    @RequestMapping(value = "/viewPapers", method = RequestMethod.POST)
    public JsonAndView viewPapers(HttpServletRequest request,Integer courseId, CommonQuery query,
            Integer departId, Integer userId, Integer departType,
            Integer userType) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (query == null) {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        QueryData queryData = trainService.querPapers(courseId, query,
                departId, userId, departType, userType);
        //logDBService.insertNewLog(request, 4, "试卷管理", "加载试卷数据");
        jv.addData("totalPage", queryData.getTotalPage());
        jv.addData("totalCount", queryData.getTotalCount());
        jv.addData("pageData", queryData.getPageData());
        return jv;
    }

    /**
     * 新增试题操作，保存试题相关数据
     * 
     * @author yangzq
     * @param type
     *            试题类型
     * @param title
     *            试题题干
     * @param options
     *            试题选项
     * @param answer
     *            试题正确答案
     * @param optionAttachs
     *            选项附件
     * @param courseId
     *            对应课程编号
     * @param difficulty
     *            试题难度
     * @param titleAttach
     *            试题附件
     * @return
     */
    @RequestMapping(value = "/saveQuestions", method = RequestMethod.POST)
    public JsonAndView saveQuestions(HttpServletRequest request,
            ExamQuestion question, String optionTitles, String optionAttachs,
            String titleAttach) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (question.getExquCourId() == 0 || question.getExquType() == 0
                || question.getExquDescribe() == ""
                || question.getExquCreateUserid() == "0") {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        User user = CookieUtil.getCookieUser(request);
        question.setExquCreateUserid(String.valueOf(user.getUserId()));
        try {
            jv = trainService.saveQuestions(question, optionTitles,
                    optionAttachs, titleAttach);
            logDBService.insertNewLog(request, 1, "题库管理", "增加试题");
        } catch (Exception e) {
            e.printStackTrace();
            jv.setRet(false);
            jv.setErrmsg("试题增加失败");
            return jv;
        }
        return jv;
    }

    /**
     * 编辑修改试题，更新试题数据
     * 
     * @author yangzq
     * @param question
     * @param optionTitles
     * @param optionAttachs
     * @param titleAttach
     * @return
     */
    @RequestMapping(value = "/editQuestions", method = RequestMethod.POST)
    public JsonAndView editQuestions(HttpServletRequest request,ExamQuestion question,
            String optionTitles, String optionAttachs, String titleAttach) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (question.getExquId() == 0 || question.getExquCourId() == 0
                || question.getExquType() == 0
                || question.getExquDescribe() == ""
                || question.getExquCreateUserid() == "0") {
            jv.setRet(false);
            jv.setErrcode(601);
            jv.setErrmsg("数据格式错误");
            return jv;
        }
        try {
            jv = trainService.editQuestions(question, optionTitles,
                    optionAttachs, titleAttach);
            logDBService.insertNewLog(request, 3, "题库管理", "修改试题");
        } catch (Exception e) {
            jv.setErrmsg("保存失败!该试题存在于已组好的试卷中,请先删除对应的试卷!");
            return jv.setRet(false);
        }
        return jv;
    }

    /**
     * 试题删除
     * 
     * @author yangzq
     * @param delQuestions
     * @return
     */
    @RequestMapping(value = "/delQuestions", method = RequestMethod.POST)
    public JsonAndView delQuestions(HttpServletRequest request,String delQuestions) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (StringUtils.isEmpty(delQuestions)) {
            errorData(jv);
            return jv;
        }
        try {
            jv = trainService.delQuestions(delQuestions);
            logDBService.insertNewLog(request, 2, "题库管理", "删除试题");
        } catch (Exception e) {
            jv.setRet(false);
            jv.setErrmsg("试题删除失败!该试题存在于已组好的试卷中,请先删除对应的试卷!");
            logger.error("train delQuestions error " + e.getLocalizedMessage());
        }
        return jv;
    }

    /**
     * 选中试题组卷下一步展示试题
     * 
     * @author yangzq
     * @param queIds
     * @return
     */
    @RequestMapping(value = "/selectedInfos", method = RequestMethod.POST)
    public JsonAndView selectedInfos(String queIds) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (queIds == null || queIds == "") {
            errorData(jv);
            return jv;
        }
        List<ExamQuestionModel> examQuestions = trainService
                .selectedInfos(queIds);
        jv.addData("pageData", examQuestions);
        return jv;
    }

    /**
     * 保存组卷试题信息
     * 
     * @param groupPaName
     * @param perRadio
     * @param perMulti
     * @param perJudge
     * @param perFill
     * @param perAsk
     * @param selectedQues
     * @return
     */
    @RequestMapping(value = "/commitSelectedQues", method = RequestMethod.POST)
    public JsonAndView commitSelectedQues(HttpServletRequest request,
            ExamPaperModel paperModel, float perRadio, float perMulti,
            float perJudge, float perFill, float perAsk, String quTypeArr,
            String selectedQues) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (paperModel.getExpaName() == null || paperModel.getExpaName() == ""
                || selectedQues == null) {
            errorData(jv);
            return jv;
        }
        User user = CookieUtil.getCookieUser(request);
        paperModel.setExpaCreateUserid(user.getUserId());
        paperModel.setExpaGppaId(null);// 手动组卷
        paperModel.setExpaOrdinal(0);// 设置一个默认值
        try {
            jv = trainService.commitSelectedQues(paperModel, perRadio,
                    perMulti, perJudge, perFill, perAsk, quTypeArr,
                    selectedQues);
            logDBService.insertNewLog(request, 1, "试卷管理", "手动组卷");
        } catch (Exception e) {
            jv.setRet(false);
            jv.setErrmsg("组卷失败!请检查参数是否有误!");
        }
        return jv;
    }

    /**
     * 自动组卷，传入参数分拣后根据用户 选择试题类进行自动选择相应试题生成试卷
     * 
     * @author yangzq
     * @param param
     * @param examName
     * @param examTotalScore
     * @param examSpendTime
     * @return
     */
    @RequestMapping(value = "/autoGroupPaper", method = RequestMethod.POST)
    public JsonAndView autoGroupPaper(HttpServletRequest request, String param,
            String examName, String examDescribe, Integer examCourId,
            String examTotalScore) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (param == null || param == "" || examName == null || examName == ""
                || examTotalScore == null || examTotalScore == "") {
            errorData(jv);
            jv.addData("failFlag", 100);
            return jv;
        }
        User user = CookieUtil.getCookieUser(request);
        try {
            jv = trainService.autoGroupPaper(user.getUserId(), param, examName,
                    examDescribe, examCourId, examTotalScore);
            logDBService.insertNewLog(request, 1, "试卷管理", "自动组卷");
        } catch (Exception e) {
            e.printStackTrace();
            jv.addData("failFlag", 8);
            return jv.setRet(false);
        }
        return jv;
    }

    /**
     * 删除试卷,支持批量删除
     * 
     * @author yangzq
     * @param delPapers
     * @return
     */
    @RequestMapping(value = "/delPapers", method = RequestMethod.POST)
    public JsonAndView delPapers(HttpServletRequest request,String delPapers) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (delPapers == null || delPapers == "") {
            errorData(jv);
            return jv;
        }
        try {
            jv = trainService.delPapers(delPapers);
            logDBService.insertNewLog(request, 2, "试卷管理", "删除试卷");
        } catch (Exception e) {
            jv.setRet(false);
            jv.setErrmsg("该试卷已经参与考试，若要删除请先删除试卷对应的课程测试！");
            return jv;
        }
        return jv;
    }

    /**
     * 编辑获取试题数据
     * 
     * @author yangzq
     * @param quesId
     * @return
     */
    @RequestMapping(value = "/showOneQuestion", method = RequestMethod.POST)
    public JsonAndView showOneQuestion(Integer quesId) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (quesId <= 0) {
            errorData(jv);
            return jv;
        }
        ExamQuestionModel quesModel = trainService.showOneQuestion(quesId);
        jv.addData("data", quesModel);
        return jv;
    }

    /**
     * 检验自动组卷选择类型时试题数据合法
     * 
     * @author yangzq
     * @param quesType
     * @param quesNums
     * @param quesDifficult
     * @param quesCourId
     * @return
     */
    @RequestMapping(value = "/validateQuesTypeNums", method = RequestMethod.POST)
    public JsonAndView validateQuesTypeNums(Integer quesType, Integer quesNums,
            Integer quesDifficult, Integer quesCourId) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (quesCourId <= 0 || quesNums <= 0 || quesDifficult < 0
                || quesCourId <= 0) {
            errorData(jv);
            return jv;
        }
        jv = trainService.validateQuesTypeNums(quesType, quesNums,
                quesDifficult, quesCourId);
        return jv;
    }

    /**
     * 检验组卷名称是否有重复
     * 
     * @author yangzq
     * @param name
     * @return
     */
    @RequestMapping(value = "/checkExamInfoTitle", method = RequestMethod.POST)
    public JsonAndView checkExamInfoTitle(String name) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (StringUtils.isEmpty(name)) {
            errorData(jv);
            return jv;
        }
        Boolean bool = trainService.checkExamInfoTitle(name);
        jv.setRet(bool);
        return jv;
    }

    /**
     * 检验创建的课程测试名称是否有重复
     * 
     * @author yangzq
     * @param name
     * @return
     */
    @RequestMapping(value = "/checkTestName", method = RequestMethod.POST)
    public JsonAndView checkTestName(String name) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (StringUtils.isEmpty(name)) {
            errorData(jv);
            return jv;
        }
        Boolean bool = trainService.checkTestName(name);
        jv.setRet(bool);
        return jv;
    }

    /**
     * 保存新增课程测试
     * 
     * @author yangz
     * @param examInfo
     * @return
     */
    @RequestMapping(value = "/newExaminfo", method = RequestMethod.POST)
    public JsonAndView newExaminfo(HttpServletRequest request,ExamInfo examInfo) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (examInfo == null) {
            errorData(jv);
            return jv;
        }
        try {
            jv = trainService.newExaminfo(examInfo);
            logDBService.insertNewLog(request, 1, "课程测试管理", "创建测试："+examInfo.getExinName());
        } catch (Exception e) {
            logger.error("TrainService newExaminfo error "
                    + e.getLocalizedMessage());
            jv.setRet(false);
            jv.setErrmsg("新增失败!请检查参数是否有误!");
        }
        return jv;
    }

    /**
     * 编辑更新考试信息
     * 
     * @author yangzq
     * @param examInfo
     * @return
     */
    @RequestMapping(value = "/updateExaminfo", method = RequestMethod.POST)
    public JsonAndView updateExaminfo(HttpServletRequest request,ExamInfo examInfo) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (examInfo == null) {
            errorData(jv);
            return jv;
        }
        try {
            jv = trainService.updateExaminfo(examInfo);
            logDBService.insertNewLog(request, 3, "课程测试管理", "更新测试："+examInfo.getExinName());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            jv.setRet(false);
            jv.setErrmsg("编辑考试失败，请检查数据是否有误！");
        }
        return jv;
    }

    /**
     * 删除课程测试数据，批量删除
     * 
     * @author yangzq
     * @param delPapers
     * @return
     */
    @RequestMapping(value = "/delExin", method = RequestMethod.POST)
    public JsonAndView delExin(HttpServletRequest request,String delRecords) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (delRecords == null || delRecords == "") {
            errorData(jv);
            return jv;
        }
        try {
            jv = trainService.delExin(delRecords);
            logDBService.insertNewLog(request, 2, "课程测试管理", "删除测试");
        } catch (Exception e) {
            jv.setRet(false);
            jv.setErrmsg("课程测试信息删除失败,该测试已生成成绩，请先管理成绩数据!");
            logger.error("TrainService delExin error "
                    + e.getLocalizedMessage());
        }
        return jv;
    }

    /**
     * 根据考试编号查询考试信息
     * 
     * @author yangzq
     * @param exinId
     * @return
     */
    @RequestMapping(value = "/queryExinById", method = RequestMethod.POST)
    public JsonAndView queryExinById(Integer exinId) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (exinId == null || exinId <= 0) {
            errorData(jv);
            return jv;
        }
        ExamInfo examInfo = trainService.queryExinById(exinId);
        if (examInfo == null) {
            jv.setRet(false);
            jv.setErrmsg("考试信息获取失败！");
            return jv;
        }
        jv.addData("examInfo", examInfo);
        return jv;
    }

    private void errorData(JsonAndView jv) {
        jv.setRet(false);
        jv.setErrcode(601);
        jv.setErrmsg("数据格式错误");
    }

    /**
     * 下载课程对应id
     * 
     * @param fileName
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/downloadRe", method = RequestMethod.GET)
    public void downloadFile(String fileName, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // 获取文件资源
        String filePath = "upload/resource/" + fileName;
        if (filePath == null || filePath.isEmpty()) {
            return;
        }
        if (fileName.contains("CourseIds.xls")) {
            String result = trainService.writeCourseIds(fileName, request);
            if (result == "fail")
                return;
        }
        ServletContextResource downFile = new ServletContextResource(request
                .getSession().getServletContext(), filePath);
        if (!downFile.exists()) {
            logger.error("找不到文件或者文件不存在！！");
            return;
        }
        String agintname = request.getHeader("User-Agent").toUpperCase();
        fileName = new String(fileName.getBytes(), "ISO-8859-1");
        if (agintname.indexOf("MSIE") > 0)
            fileName = URLEncoder.encode(fileName, "ISO-8859-1");// IE浏览器
        OutputStream os = response.getOutputStream();
        try {
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename="
                    + fileName);
            response.setContentType("application/octet-stream; charset=utf-8");
            os.write(FileUtils.readFileToByteArray(downFile.getFile()));
            os.flush();
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

    /**
     * 导入试题库
     * 
     * @author yangzq
     * @param request
     * @param response
     */
    @RequestMapping(value = "/importQuestionsInfo", method = RequestMethod.POST)
    public JsonAndView importQuestionsInfo(HttpServletRequest request,
            HttpServletResponse response) {
        CommonsMultipartFile comMultiFile = (CommonsMultipartFile) ((MultipartHttpServletRequest) request)
                .getFile("file");

        String fileName = comMultiFile.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf("."));

        // 防止返回数据被IE当做下载流
        response.reset();
        response.setContentType("text/html; charset=utf-8");

        JsonAndView jv = new JsonAndView();
        try {
            if (!(fileType.equals(".xls") || fileType.equals(".xlsx"))) {
                jv.setRet(false);
                jv.setErrcode(2);
                jv.setErrmsg("文件格式不正确,必须是excel表格");
                return jv;
            }
            // 导入试题
            User user = CookieUtil.getCookieUser(request);
            String message = trainService.importQuesInfoFromExcel(
                    user, comMultiFile.getInputStream(), fileType);
            logDBService.insertNewLog(request, 1, "题库管理", "批量导入试题");
            if (message.length() == 0) {
                jv.setRet(true);
                return jv;
            } else {
                jv.setRet(false);
                jv.setErrcode(1);
                String logoRealPathDir = request.getSession().getServletContext().getRealPath("/upload/ques/errmsg/errorMsg.txt");     
                /**根据真实路径创建目录**/    
                File logoSaveFile = new File(logoRealPathDir);     
                if(!logoSaveFile.exists())     
                    logoSaveFile.mkdirs();
                PrintWriter out = new PrintWriter(new FileWriter(logoRealPathDir)); 
                out.println(message);
                out.close();
                return jv;
            }
        } catch (Exception e) {
            e.printStackTrace();
            jv.setRet(false);
            jv.setErrcode(2);
            jv.setErrmsg("数据在导入的过程中出现意外的错误，请重新导入");
            return jv;
        }
    }


    /**
     * 下载课程对应id
     * 
     * @param fileName
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/downloadErrorMsg", method = RequestMethod.GET)
    public void downloadErrorMsg(String fileName, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // 获取文件资源
        String filePath = "upload/ques/errmsg/" + fileName;
        if (filePath == null || filePath.isEmpty()) {
            return;
        }
        ServletContextResource downFile = new ServletContextResource(request
                .getSession().getServletContext(), filePath);
        if (!downFile.exists()) {
            logger.error("找不到文件或者文件不存在！！");
            return;
        }
        String agintname = request.getHeader("User-Agent").toUpperCase();
        fileName = new String(fileName.getBytes(), "ISO-8859-1");
        if (agintname.indexOf("MSIE") > 0)
            fileName = URLEncoder.encode(fileName, "ISO-8859-1");// IE浏览器
        OutputStream os = response.getOutputStream();
        try {
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename="
                    + fileName);
            response.setContentType("application/octet-stream; charset=utf-8");
            os.write(FileUtils.readFileToByteArray(downFile.getFile()));
            os.flush();
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

    /**
     * 返回已考科目列表
     * 
     * @author lixw
     * @param query
     * @return
     */
    @RequestMapping(value = "/getExamCourse", method = RequestMethod.GET)
    public JsonAndView getExamCourse(CommonQuery query) {
        JsonAndView jv = new JsonAndView();
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("data", trainService);
        jv.addAllData(data);
        return jv;
    }

    /**
     * 测试管理--成绩管理
     * @author lixw
     * @param deleteScoreQuery
     * @return json
     */
    @SuppressWarnings("finally")
    @RequestMapping(value = "/deleteScore", method = RequestMethod.POST)
    public JsonAndView deleteScore(HttpServletRequest request,String deleteScores) {
        String[] deleteScoreQuery = deleteScores.split(",");
        Map<String, Object> data = new HashMap<String, Object>();
        JsonAndView jv = new JsonAndView();
        try {
            trainService.deleteScore(deleteScoreQuery);
            logDBService.insertNewLog(request, 2, "成绩管理", "删除成绩");
            data.put("state", "删除成功！");
        } catch (Exception e) {
            data.put("state", "删除失败！");
        } finally {
            jv.addAllData(data);
            return jv;
        }
    }

    /**
     * @author lixw
     * @param rateExinId
     * @param query
     * @return json
     */
    @RequestMapping(value = "/showAllScore", method = RequestMethod.POST)
    public JsonAndView showAllScore(CommonQuery commonquery, Integer courId,
            Integer departId, Integer userId, Integer departType,
            Integer userType) {
        JsonAndView jv = new JsonAndView();
        // 查询条件格式验证
        if (commonquery == null) {
            errorData(jv);
            return jv;
        }
        // 对象数组
        QueryData queryData = trainService.showAllScore(commonquery, courId,
                departId, userId, departType, userType);
        jv.addData("totalPage", queryData.getTotalPage());
        jv.addData("totalCount", queryData.getTotalCount());
        jv.addData("pageData", queryData.getPageData());
        return jv;
    }

}
