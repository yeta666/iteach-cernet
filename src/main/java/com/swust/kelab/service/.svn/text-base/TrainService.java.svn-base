package com.swust.kelab.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.swust.kelab.domain.ExamPaper;
import com.swust.kelab.domain.ExamQuestion;
import com.swust.kelab.domain.GroupPaperParameter;
import com.swust.kelab.domain.Option;
import com.swust.kelab.domain.ReAutoTest;
import com.swust.kelab.domain.ReGroupPaper;
import com.swust.kelab.domain.User;
import com.swust.kelab.model.AutoGroupPaperModel;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.CourseModel;
import com.swust.kelab.model.ExamCourModel;
import com.swust.kelab.model.ExamInfo;
import com.swust.kelab.model.ExamPaperModel;
import com.swust.kelab.model.ExamQuestionModel;
import com.swust.kelab.model.ExamResultModel;
import com.swust.kelab.model.GradeModel;
import com.swust.kelab.model.PageData;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.model.ResultQuesModel;
import com.swust.kelab.model.ScoreModel;
import com.swust.kelab.repos.AttachmentDAO;
import com.swust.kelab.repos.CourseDAO;
import com.swust.kelab.repos.ExamDAO;
import com.swust.kelab.repos.PaperDAO;
import com.swust.kelab.repos.QuestionDAO;
import com.swust.kelab.repos.ReSelectCourseDAO;
import com.swust.kelab.repos.bean.GenericQuery;
import com.swust.kelab.repos.bean.ListQuery;
import com.swust.kelab.utils.CommonUtil;
import com.swust.kelab.utils.CookieUtil;
import com.swust.kelab.utils.ExcelResolver_03;
import com.swust.kelab.utils.ExcelResolver_07;
import com.swust.kelab.utils.FormatUtil;
import com.swust.kelab.web.json.JsonAndView;

@Service(value = "trainService")
public class TrainService {

    final Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private ExamDAO examDAO;
    @Resource
    private QuestionDAO questionDAO;
    @Resource
    private AttachmentDAO attachmentDAO;
    @Resource
    private PaperDAO paperDAO;
    @Resource
    private CourseDAO courseDao;
    @Resource
    private ReSelectCourseDAO reSelectCourseDAO;
    @Autowired
    private SelectCourseService selectCourseService;

    /**
     * 考试结束提交试卷数据
     * 
     * @author yangzq
     * @param resultModel
     * @return
     */
    @Transactional
    public Map<String, Object> submitResult(int courseId, ExamResultModel resultModel) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        // 判断当前是否有记录
        List<ReAutoTest> reats = examDAO.examAutoTest(resultModel);
        if (reats != null && reats.size() > 0) {
            // 有记录则判断当前得分是否大于已有的分数，是则更新，否则保持记录
            // if(resultModel.getTotalScore()>reats.get(0).getRateScore()){
            int rateId = reats.get(0).getRateId();
            // 答案数量
            int solveCount = 0;
            resultModel.setRateId(rateId);
            examDAO.updateAutoTest(resultModel);
            String jsonArrStr = ("[" + resultModel.getSolveArr() + "]").toString();
            List<ResultQuesModel> quesList = JSON.parseArray(jsonArrStr, ResultQuesModel.class);
            // 判断是否有相应的答案
            if (rateId > 0) {
                solveCount = examDAO.checkSolveCount(rateId);
            }
            if (solveCount <= 0) {
                for (ResultQuesModel ques : quesList) {
                    ques.setRateId(rateId);
                    examDAO.resultQues(ques);
                }
            } else {
                for (ResultQuesModel ques : quesList) {
                    ques.setRateId(resultModel.getRateId());
                    examDAO.resultQuesForUpdate(ques);
                }
            }
            map.put("f", "s");
            // }else{
            // map.put("f", "keep");
            // }
        } else {
            int rateId = examDAO.examResult(resultModel);
            if (rateId > 0) {
                String jsonArrStr = ("[" + resultModel.getSolveArr() + "]").toString();
                List<ResultQuesModel> quesList = JSON.parseArray(jsonArrStr, ResultQuesModel.class);
                for (ResultQuesModel ques : quesList) {
                    ques.setRateId(rateId);
                    examDAO.resultQues(ques);
                }
                map.put("f", "s");
            } else {
                throw new Exception();
            }
        }
        if (resultModel.isIsfinish()) {
            // 更新学生总成绩
            // int courId = examDAO.selectExamInfoById(resultModel.getExaminId()).getCourseId();
            // 跟新测试对应课程成绩 courseId 查找课程的考试数量
            int courseTestNum = 1;
            float totalScore = 0.0f;
            float courseScore = 0.0f;
            ListQuery query = new GenericQuery();
            query.fill("courseId", courseId);
            query.fill("userId", resultModel.getUserId());
            courseTestNum = examDAO.selectCount(query);
            totalScore = examDAO.courseTestScore(query);
            courseScore = totalScore / courseTestNum;
            query.fill("score", courseScore);
            reSelectCourseDAO.updateUserTestScore(query);
            selectCourseService.updateCourseTotalScore(resultModel.getUserId(), courseId);
        }
        return map;
    }

    /**
     * 手动评分
     * 
     * @author yangzq
     * @param markScoreArr
     * @throws Exception
     */
    @Transactional
    public int markScore(int userId, int exinId, int rateId, String markScoreArr) throws Exception {
        float addScore = 0;
        for (String markScore : markScoreArr.split(",")) {
            String mark = markScore.split("&")[1];
            String ordinal = markScore.split("&")[0];
            if (mark == null || ordinal == null) {
                throw new Exception("参数输入有问题!");
            }
            addScore += Float.parseFloat(mark);
            Map<String, Object> update = new HashMap<String, Object>();
            update.put("rateId", rateId);
            update.put("ordinal", ordinal);
            update.put("score", mark);
            examDAO.markScore(update);
        }
        ExamResultModel resultModel = new ExamResultModel();
        resultModel.setExaminId(exinId);
        resultModel.setUserId(userId);
        List<ReAutoTest> reats = examDAO.examAutoTest(resultModel);
        if (reats == null) {
            throw new Exception("未查询到纪录!");
        } else {
            addScore += reats.get(0).getRateScore();
        }
        // 更新评卷之后的试卷分数和状态
        ExamResultModel updateModel = new ExamResultModel();
        updateModel.setExaminId(exinId);
        updateModel.setUserId(userId);
        updateModel.setIsfinish(true);
        updateModel.setStartTime(reats.get(0).getRateStarttime());
        updateModel.setSubmitTime(reats.get(0).getRateSubmittime());
        updateModel.setRateId(reats.get(0).getRateId());
        updateModel.setTotalScore(addScore);
        examDAO.updateAutoTest(updateModel);
        // 更新学生总成绩 此处为教师评卷的结果更新
        // exinId
        ExamInfo examInfo = examDAO.selectExamInfoById(exinId);
        if (examInfo != null) {
            int paperId = examInfo.getExinExpaId();
            ExamPaper examPaper = examDAO.selectExamPaper(paperId);
            if (examPaper.getExpaCourId() > 0) {
                // 跟新测试对应课程成绩 courseId 查找课程的考试数量
                int courseTestNum = 1;
                float totalScore = 0.0f;
                float courseScore = 0.0f;
                ListQuery query = new GenericQuery();
                query.fill("courseId", examPaper.getExpaCourId());
                query.fill("userId", resultModel.getUserId());
                courseTestNum = examDAO.selectCount(query);
                totalScore = examDAO.courseTestScore(query);
                courseScore = totalScore / courseTestNum;
                query.fill("score", courseScore);
                reSelectCourseDAO.updateUserTestScore(query);
                selectCourseService.updateCourseTotalScore(resultModel.getUserId(), examPaper.getExpaCourId());
            }
        }
        return 0;
    }

    /**
     * 获取考试结束后的试卷对应的考试结果数据
     * 
     * @author yangzq
     * @param exinId
     * @param userId
     * @return
     */
    public ExamResultModel loadExamResult(int exinId, int userId) {
        Map<String, Object> query = new HashMap<String, Object>();
        query.put("exinId", exinId);
        query.put("userId", userId);
        ExamResultModel results = examDAO.loadExamResult(query);
        if (results.getStartTime() != null && !"".equals(results.getStartTime())) {
            results.setStartTimeFormat(FormatUtil.formatDate(results.getStartTime()));
        } else {
            results.setStartTimeFormat("");
        }
        if (results.getSubmitTime() != null && !"".equals(results.getSubmitTime())) {
            results.setSubmitTimeFormat(FormatUtil.formatDate(results.getSubmitTime()));
        } else {
            results.setSubmitTimeFormat("");
        }
        return results;
    }

    /**
     * 查询试题数据
     * 
     * @author yangzq
     * @param quType2
     * @param quCourse2
     * @param userType
     * @param departType
     * @param commonquery
     * @return
     */
    public QueryData queryQuestions(Integer userId, Integer departId, Integer departType, Integer userType,
            int quCourse, int quType, CommonQuery commonquery) {
        QueryData queryData = new QueryData();
        // 构造查询条件
        ListQuery query = commonquery.format();
        if (quCourse > 0) {
            query.fill("quCourse", quCourse);
        }
        if (quType > 0) {
            query.fill("quType", quType);
        }
        if (userType.intValue() == 2) {
            query.fill("teacherId", "," + userId + ",");
        }
        if ((userType.intValue() == 3 || userType.intValue() == 4) && departType.intValue() != 1) {
            query.fill("departId", departId);
        }
        int totalCount = questionDAO.selectQuesCount(query);
        queryData.setTotalCount(totalCount);
        if (totalCount == 0) {
            return queryData;
        }
        if (commonquery.getRecordPerPage() <= 0) {
            commonquery.setRecordPerPage(10);
        }
        query.fill("maxCount", commonquery.getRecordPerPage());
        int totalPage = QueryData.computeTotalPage(totalCount, commonquery.getRecordPerPage());
        queryData.setTotalPage(totalPage);
        List<PageData> pageDataList = Lists.newArrayList();
        // 未指定页数，则只读取前三页数据
        if (commonquery.getPageArray() == null) {
            commonquery.setPageArray(new int[] { 1, 2, 3 });
        }
        // 分别获取每页的数据
        for (int i = 0; i < commonquery.getPageArray().length; i++) {
            int page = commonquery.getPageArray()[i];
            if (page <= 0 || page > totalPage) {
                continue;
            }
            query.fill("startIndex", QueryData.computeStartIndex(page, commonquery.getRecordPerPage()));
            List<ExamQuestionModel> examQuestions = questionDAO.selectQuesDetail(query);
            for (ExamQuestionModel question : examQuestions) {
                if (question.getCreatTime() != null && !"".equals(question.getCreatTime())) {
                    question.setCreatTimeFormat(DateFormatUtils.ISO_DATE_FORMAT.format(question.getCreatTime()));
                } else {
                    question.setCreatTimeFormat("");
                }
            }
            pageDataList.add(new PageData(page, examQuestions));
        }
        // 装载返回结果
        queryData.setPageData(pageDataList);
        return queryData;
    }

    /**
     * 试卷管理数据
     * 
     * @author yangzq
     * @param quoteType
     * @param commonquery
     * @return
     */
    public QueryData querPapers(Integer courseId, CommonQuery commonquery, Integer departId, Integer userId,
            Integer departType, Integer userType) {
        QueryData queryData = new QueryData();
        // 构造查询条件
        ListQuery query = commonquery.format();
        if (courseId > 0 && courseId != null) {
            query.fill("courseId", courseId);
        }
        if (userType.intValue() == 2) {
            query.fill("teacherId", "," + userId + ",");
        }
        if (userType.intValue() == 3 && departType.intValue() != 1 || userType.intValue() == 4
                && departType.intValue() != 1) {
            query.fill("departId", departId);
        }
        int totalCount = paperDAO.selectPapersCount(query);
        queryData.setTotalCount(totalCount);
        if (totalCount == 0) {
            return queryData;
        }
        if (commonquery.getRecordPerPage() <= 0) {
            commonquery.setRecordPerPage(10);
        }
        query.fill("maxCount", commonquery.getRecordPerPage());
        int totalPage = QueryData.computeTotalPage(totalCount, commonquery.getRecordPerPage());
        queryData.setTotalPage(totalPage);
        List<PageData> pageDataList = Lists.newArrayList();
        // 未指定页数，则只读取前三页数据
        if (commonquery.getPageArray() == null) {
            commonquery.setPageArray(new int[] { 1, 2, 3 });
        }
        // 分别获取每页的数据
        for (int i = 0; i < commonquery.getPageArray().length; i++) {
            int page = commonquery.getPageArray()[i];
            if (page <= 0 || page > totalPage) {
                continue;
            }
            query.fill("startIndex", QueryData.computeStartIndex(page, commonquery.getRecordPerPage()));
            List<ExamPaperModel> examPapers = paperDAO.selectPapersDetail(query);
            for (ExamPaperModel examPaper : examPapers) {
                if (examPaper.getExpaCreatetime() != null && !"".equals(examPaper.getExpaCreatetime())) {
                    examPaper.setExpaCreatetimeFormat(DateFormatUtils.ISO_DATE_FORMAT.format(examPaper
                            .getExpaCreatetime()));
                } else {
                    examPaper.setExpaCreatetimeFormat("");
                }
            }
            pageDataList.add(new PageData(page, examPapers));
        }
        // 装载返回结果
        queryData.setPageData(pageDataList);
        return queryData;
    }

    /**
     * 新增试题保存试题数据
     * 
     * @author yangzq
     * @param type 试题类型
     * @param title 试题题干
     * @param options 试题选项
     * @param answer 试题正确答案
     * @param optionAttachs 选项附件
     * @param courseId 对应课程编号
     * @param difficulty 试题难度
     * @param titleAttach 试题附件
     * @return
     */
    @Transactional
    public JsonAndView saveQuestions(ExamQuestion question, String optionTitles, String optionAttachs,
            String titleAttach) throws Exception {
        JsonAndView jv = new JsonAndView();
        boolean attachflag = false;
        // 试题是否有附件
        if (!StringUtils.isEmpty(titleAttach)) {
            attachflag = true;
            question.setExquAttachment(true);
        } else {
            question.setExquAttachment(false);
        }
        question.setExquCreatetime(new Date());
        int exquId = questionDAO.saveQuestions(question);
        if (exquId <= 0) {
            throw new Exception("问题保存错误！");
        }
        if (attachflag) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("sourceId", exquId);
            map.put("attachId", titleAttach);
            attachmentDAO.changeAttachSourceId(map);
        }
        // 如果是选择题分割试题选项
        if (question.getExquType().equals(CommonUtil.SINGLE) || question.getExquType().equals(CommonUtil.MULTI)) {
            String[] optionArr = optionTitles.split("&");
            String[] optionAtt = optionAttachs.split(",");
            Option option = null;
            for (int i = 0; i < optionArr.length; i++) {
                option = new Option();
                option.setOptiQuesId(exquId);
                option.setOptiDescribe(optionArr[i]);
                option.setOptiOrdinal(i + 1);
                // 试题是否有附件
                boolean optionFlag = false;
                if (!StringUtils.isEmpty(optionAtt[i])) {
                    option.setOptiAttachment(true);
                    optionFlag = true;
                } else {
                    option.setOptiAttachment(false);
                }
                int opId = questionDAO.saveOption(option);
                if (optionFlag) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("sourceId", opId);
                    map.put("attachId", optionAtt[i]);
                    attachmentDAO.changeAttachSourceId(map);
                }
            }
        }
        jv.setRet(true);
        return jv;
    }

    /**
     * 编辑保存问题
     * 
     * @author yangzq
     * @param question
     * @param optionTitles
     * @param optionAttachs
     * @param titleAttach
     * @return
     */
    @Transactional
    public JsonAndView editQuestions(ExamQuestion question, String optionTitles, String optionAttachs,
            String titleAttach) throws Exception {
        JsonAndView jv = new JsonAndView();
        if (question.getExquId() <= 0) {
            throw new Exception("问题保存错误！");
        }
        // 试题是否有附件
        if (!StringUtils.isEmpty(titleAttach)) {
            question.setExquAttachment(true);
        } else {
            question.setExquAttachment(false);
        }
        if (question.getExquAttachment()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("sourceId", question.getExquId());
            map.put("attachId", titleAttach);
            attachmentDAO.changeAttachSourceId(map);
        }
        question.setExquCreatetime(new Date());
        questionDAO.updateQuestions(question);
        // 删除旧选项
        String[] delQues = { question.getExquId().toString() };
        questionDAO.delOptions(delQues);
        // 如果是选择题分割试题选项
        if (question.getExquType().equals(CommonUtil.SINGLE) || question.getExquType().equals(CommonUtil.MULTI)) {
            String[] optionArr = optionTitles.split("&");
            String[] optionAtt = optionAttachs.split(",");
            Option option = null;
            for (int i = 0; i < optionArr.length; i++) {
                option = new Option();
                option.setOptiQuesId(question.getExquId());
                option.setOptiDescribe(optionArr[i]);
                option.setOptiOrdinal(i + 1);
                // 试题是否有附件
                if (!StringUtils.isEmpty(optionAtt[i])) {
                    option.setOptiAttachment(true);
                } else {
                    option.setOptiAttachment(false);
                }
                // 保存新的选项
                questionDAO.saveOption(option);
                if (option.getOptiAttachment()) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("sourceId", option.getOptiId());
                    map.put("attachId", optionAtt[i]);
                    attachmentDAO.changeAttachSourceId(map);
                }
            }
        }
        jv.setRet(true);
        return jv;
    }

    /**
     * 删除试题
     * 
     * @author yangzq
     * @param delQuestions
     * @return
     */
    @Transactional
    public JsonAndView delQuestions(String delQuestions) throws Exception {
        JsonAndView jv = new JsonAndView();
        String[] delQues = delQuestions.split(",");
        questionDAO.delOptions(delQues);
        questionDAO.delQuestion(delQues);
        jv.setRet(true);
        return jv;
    }

    /**
     * 根据编号获取试题信息
     * 
     * @author yangzq
     * @param quesId
     * @return
     */
    public ExamQuestionModel showOneQuestion(Integer quesId) {
        ExamQuestionModel ques = questionDAO.getOneQuestion(quesId);
        if (ques != null) {
            if (ques.getCreatTime() != null && !"".equals(ques.getCreatTime())) {
                ques.setCreatTimeFormat(DateFormatUtils.ISO_DATE_FORMAT.format(ques.getCreatTime()));
            }
        }
        // 有附件
        if (ques.getAttachment() != null && ques.getAttachment() == 1) {
            attachmentDAO.findAttachmentsByType(3);
        }
        return ques;
    }

    /**
     * 查询已选试题
     * 
     * @author yangzq
     * @param queIds
     * @return
     */
    public List<ExamQuestionModel> selectedInfos(String queIds) {
        String[] queId = queIds.split(";");
        List<ExamQuestionModel> quesList = questionDAO.selectedInfos(queId);
        for (ExamQuestionModel model : quesList) {
            if (model.getCreatTime() != null && !"".equals(model.getCreatTime())) {
                model.setCreatTimeFormat(DateFormatUtils.ISO_DATE_FORMAT.format(model.getCreatTime()));
            }
        }
        return quesList;
    }

    /**
     * 保存组卷试题
     * 
     * @author yangzq
     * @param paperModel 组卷名字
     * @param selectedQues 选择的试题
     * @param perAsk 问答每道分数
     * @param perFill 填空每道分数
     * @param perJudge 判断每道分数
     * @param perMulti 多选每道分数
     * @param perRadio 单选每道分数
     * @return
     */
    @Transactional
    public JsonAndView commitSelectedQues(ExamPaperModel paperModel, float perRadio, float perMulti, float perJudge,
            float perFill, float perAsk, String quTypeArr, String selectedQues) throws Exception {
        JsonAndView jv = new JsonAndView();
        paperModel.setExpaCreatetime(new Date());
        int expaId = paperDAO.insertNewPaper(paperModel);
        String queId[] = selectedQues.split(",");
        String qutypes[] = quTypeArr.split(",");
        ReGroupPaper groupPaper = null;
        for (int i = 0; i < queId.length; i++) {
            groupPaper = new ReGroupPaper();
            if (Integer.parseInt(qutypes[i]) == CommonUtil.SINGLE) {// 单选
                groupPaper.setRgpaScore(perRadio);
            } else if (Integer.parseInt(qutypes[i]) == CommonUtil.MULTI) {// 多选
                groupPaper.setRgpaScore(perMulti);
            } else if (Integer.parseInt(qutypes[i]) == CommonUtil.JUDGE) {// 判断
                groupPaper.setRgpaScore(perJudge);
            } else if (Integer.parseInt(qutypes[i]) == CommonUtil.Fill) {// 填空
                groupPaper.setRgpaScore(perFill);
            } else if (Integer.parseInt(qutypes[i]) == CommonUtil.Q_A) {// 问答
                groupPaper.setRgpaScore(perAsk);
            } else {
                groupPaper.setRgpaScore((float) 0);
            }
            groupPaper.setRgpaExpaId(expaId);
            groupPaper.setRgpaExquId(Integer.parseInt(queId[i]));
            groupPaper.setRgpaOrdinal(i + 1);
            paperDAO.insertNewGroupPaper(groupPaper);
        }
        jv.setRet(true);
        return jv;
    }

    /**
     * 检验所组卷名称是否有重复
     * 
     * @author yangzq
     * @param name
     * @return 返回为 false没有通过验证， true为通过验证
     */
    public Boolean checkExamInfoTitle(String name) {
        int i = paperDAO.checkExamInfoTitle(name);
        if (i > 0) {
            return false;
        } else {
            return true;
        }
    }

    public String writeCourseIds(String fileName, HttpServletRequest request) throws Exception {
        String rtesult = "fail";
        Map<String, Object> query = new HashMap<String, Object>();
        User user = CookieUtil.getCookieUser(request);
        if (user.getUserType() == 1) {
            query.put("userId", user.getUserId());
        }
        if (user.getUserType() == 2) {
            query.put("teacherId", "," + user.getUserId() + ",");
        }
        if (user.getUserType() == 3 && user.getUserDepartType() != 1 || user.getUserType() == 4
                && user.getUserDepartType() != 1) {
            query.put("departId", user.getUserDepaId());
        }
        List<CourseModel> courseList = courseDao.dropDownCourse(query);
        // 创建导出文件
        String directory = request.getSession().getServletContext().getRealPath("upload/resource/");
        File record = new File(new File(directory), "CourseIds.xls");
        if (!record.getParentFile().exists()) {
            record.getParentFile().mkdirs();
        } else {
            if (record.exists()) {
                record.delete();
                record = new File(new File(directory), "CourseIds.xls");
            }
        }
        // //xlsx
        HSSFWorkbook wbook = new HSSFWorkbook();
        HSSFSheet wsheet = wbook.createSheet("用户表");
        String[] title = { "课程编号id", "课程名" };
        HSSFCellStyle cellStyle = wbook.createCellStyle();
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_DOUBLE);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_DOUBLE);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
        HSSFFont font = wbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cellStyle.setFont(font);
        // 设置Excel表头
        HSSFRow excelTitle = wsheet.createRow(0);
        excelTitle.setHeightInPoints(22);
        for (int i = 0; i < title.length; i++) {
            HSSFCell titleCell = excelTitle.createCell(i);
            titleCell.setCellValue(title[i]);
            titleCell.setCellStyle(cellStyle);
        }
        FileOutputStream fOut = new FileOutputStream(record);
        for (int i = 0; i < courseList.size(); i++) {
            CourseModel courseModel = courseList.get(i);
            HSSFRow row = wsheet.createRow(i + 1);
            HSSFCell hssfCell1 = row.createCell(0);
            HSSFCell hssfCell2 = row.createCell(1);
            if (courseModel.getCourId() != null)
                hssfCell1.setCellValue(courseModel.getCourId());
            else
                hssfCell1.setCellValue("");
            if (courseModel.getCourName() != null)
                hssfCell2.setCellValue(courseModel.getCourName());
            else
                hssfCell2.setCellValue("");
        }
        wbook.write(fOut);
        fOut.flush();
        fOut.close();
        rtesult = "success";
        return rtesult;
    }

    /**
     * 自动组卷，分拣参数用于组成一套试卷
     * 
     * @author yangzq
     * @param userId
     * @param param
     * @param examName
     * @param examTotalScore
     * @param examCourId
     * @param examTotal
     * @param examSpendTime
     * @param examSpendTime2
     * @return
     */
    @Transactional
    public JsonAndView autoGroupPaper(Integer userId, String param, String examName, String examDescribe,
            Integer examCourId, String examTotalScore) throws Exception {
        JsonAndView jv = new JsonAndView();
        // 错误参数
        int failFlag = 0;
        List<AutoGroupPaperModel> autoGroupList = JSON.parseArray(param, AutoGroupPaperModel.class);
        GroupPaperParameter paramRadio = null;
        GroupPaperParameter paramCheck = null;
        GroupPaperParameter paramJudge = null;
        GroupPaperParameter paramFill = null;
        GroupPaperParameter paramAnsw = null;
        Date creatTime = new Date();
        String gppaName = String.valueOf(creatTime.getTime());
        boolean autoScore = true;
        for (AutoGroupPaperModel paperParam : autoGroupList) {
            if (paperParam.getQuesTypeIndex() == CommonUtil.SINGLE) {
                paramRadio = new GroupPaperParameter();
                paramRadio.setGppaQuestiontype(paperParam.getQuesTypeIndex());// 试题类型
                paramRadio.setGppaName(gppaName);// 创建一个组卷编号，对因gppaname，设置当前时间的秒为编号
                paramRadio.setGppaCreatetime(creatTime);
                paramRadio.setGppaCreatorid(userId);
                paramRadio.setGppaQuestionnum(paperParam.getQuesInputNumber());
                paramRadio.setGppaQuestionscore(paperParam.getQuesInputScore());
                paramRadio.setGppaDifficulty(paperParam.getQuesDifficulty());
                paramRadio.setGppaOrdinal(1);
            } else if (paperParam.getQuesTypeIndex() == CommonUtil.MULTI) {
                paramCheck = new GroupPaperParameter();
                paramCheck.setGppaQuestiontype(paperParam.getQuesTypeIndex());// 试题类型
                paramCheck.setGppaName(gppaName);// 创建一个组卷编号，对因gppaname，设置当前时间的秒为编号
                paramCheck.setGppaCreatetime(creatTime);
                paramCheck.setGppaCreatorid(userId);
                paramCheck.setGppaQuestionnum(paperParam.getQuesInputNumber());
                paramCheck.setGppaQuestionscore(paperParam.getQuesInputScore());
                paramCheck.setGppaDifficulty(paperParam.getQuesDifficulty());
                paramCheck.setGppaOrdinal(2);
            } else if (paperParam.getQuesTypeIndex() == CommonUtil.JUDGE) {
                paramJudge = new GroupPaperParameter();
                paramJudge.setGppaQuestiontype(paperParam.getQuesTypeIndex());// 试题类型
                paramJudge.setGppaName(gppaName);// 创建一个组卷编号，对因gppaname，设置当前时间的秒为编号
                paramJudge.setGppaCreatetime(creatTime);
                paramJudge.setGppaCreatorid(userId);
                paramJudge.setGppaQuestionnum(paperParam.getQuesInputNumber());
                paramJudge.setGppaQuestionscore(paperParam.getQuesInputScore());
                paramJudge.setGppaDifficulty(paperParam.getQuesDifficulty());
                paramJudge.setGppaOrdinal(3);
            } else if (paperParam.getQuesTypeIndex() == CommonUtil.Fill) {
                paramFill = new GroupPaperParameter();
                paramFill.setGppaQuestiontype(paperParam.getQuesTypeIndex());// 试题类型
                paramFill.setGppaName(gppaName);// 创建一个组卷编号，对因gppaname，设置当前时间的秒为编号
                paramFill.setGppaCreatetime(creatTime);
                paramFill.setGppaCreatorid(userId);
                paramFill.setGppaQuestionnum(paperParam.getQuesInputNumber());
                paramFill.setGppaQuestionscore(paperParam.getQuesInputScore());
                paramFill.setGppaDifficulty(paperParam.getQuesDifficulty());
                paramFill.setGppaOrdinal(4);
                autoScore = false;
            } else if (paperParam.getQuesTypeIndex() == CommonUtil.Q_A) {
                paramAnsw = new GroupPaperParameter();
                paramAnsw.setGppaQuestiontype(paperParam.getQuesTypeIndex());// 试题类型
                paramAnsw.setGppaName(gppaName);// 创建一个组卷编号，对因gppaname，设置当前时间的秒为编号
                paramAnsw.setGppaCreatetime(creatTime);
                paramAnsw.setGppaCreatorid(userId);
                paramAnsw.setGppaQuestionnum(paperParam.getQuesInputNumber());
                paramAnsw.setGppaQuestionscore(paperParam.getQuesInputScore());
                paramAnsw.setGppaDifficulty(paperParam.getQuesDifficulty());
                paramAnsw.setGppaOrdinal(5);
                autoScore = false;
            } else {
                failFlag = 1;
                jv.setRet(false);
                jv.setErrmsg("组卷参数有误！");
                throw new Exception("组卷失败！组卷参数有误！");
            }
        }
        // 难度
        Integer diff = 0;
        // 组卷参数id用于存放在paper表中
        int expaGppaId = 0;
        if (paramRadio != null) {
            expaGppaId = paperDAO.saveGpPaParameter(paramRadio);
            diff = diff + paramRadio.getGppaDifficulty();
        }
        if (paramCheck != null) {
            expaGppaId = paperDAO.saveGpPaParameter(paramCheck);
            diff = diff + paramCheck.getGppaDifficulty();
        }
        if (paramJudge != null) {
            expaGppaId = paperDAO.saveGpPaParameter(paramJudge);
            diff = diff + paramJudge.getGppaDifficulty();
        }
        if (paramFill != null) {
            expaGppaId = paperDAO.saveGpPaParameter(paramFill);
            diff = diff + paramFill.getGppaDifficulty();
        }
        if (paramAnsw != null) {
            expaGppaId = paperDAO.saveGpPaParameter(paramAnsw);
            diff = diff + paramAnsw.getGppaDifficulty();
        }
        ExamPaperModel examPaper = new ExamPaperModel();
        examPaper.setExpaCourId(examCourId);
        examPaper.setExpaName(examName);
        examPaper.setExpaDescribe(examDescribe);
        examPaper.setExpaCreatetime(creatTime);
        examPaper.setExpaCreateUserid(userId);
        examPaper.setExpaGppaId(expaGppaId);
        if (!StringUtils.isEmpty(examTotalScore)) {
            examPaper.setExpaTotalscore(Integer.parseInt(examTotalScore));
        }
        examPaper.setExpaDifficulty(diff / 5);
        // 判断是否能够自动组卷，判断标准是是否有主观题
        if (autoScore) {
            examPaper.setExpaType(CommonUtil.AUTO_MARK);
        } else {
            examPaper.setExpaType(CommonUtil.MANUL_MARK);
        }
        examPaper.setExpaOrdinal(0);
        int expaId = paperDAO.insertNewPaper(examPaper);
        // 试题顺序
        int ordinal = 1;
        if (paramRadio != null) {
            ordinal = autoGroupSearchQuesId(ordinal, paramRadio, examCourId, userId, expaId);
            if (ordinal <= 0) {
                failFlag = 2;
                throw new Exception("组卷失败！单选题参数错误！");
            }
        }
        if (paramCheck != null) {
            ordinal = autoGroupSearchQuesId(ordinal, paramCheck, examCourId, userId, expaId);
            if (ordinal <= 0) {
                failFlag = 3;
                throw new Exception("组卷失败！多选题参数错误！");
            }
        }
        if (paramJudge != null) {
            ordinal = autoGroupSearchQuesId(ordinal, paramJudge, examCourId, userId, expaId);
            if (ordinal <= 0) {
                failFlag = 4;
                throw new Exception("组卷失败！判断题参数错误！");
            }
        }
        if (paramFill != null) {
            ordinal = autoGroupSearchQuesId(ordinal, paramFill, examCourId, userId, expaId);
            if (ordinal <= 0) {
                failFlag = 5;
                throw new Exception("组卷失败！填空题参数错误！");
            }
        }
        if (paramAnsw != null) {
            ordinal = autoGroupSearchQuesId(ordinal, paramAnsw, examCourId, userId, expaId);
            if (ordinal <= 0) {
                failFlag = 6;
                throw new Exception("组卷失败！问答题参数错误！");
            }
        }
        failFlag = 0;
        jv.setRet(true);
        jv.setErrmsg("组卷成功！");
        jv.addData("failFlag", failFlag);
        return jv;
    }

    /**
     * 根据组卷参数获取随机生成的试题,将试题id封装为list
     * 
     * @author yangzq
     * @param param
     * @param examCourId
     * @param userId
     * @return
     */
    public int autoGroupSearchQuesId(int ordinal, GroupPaperParameter param, Integer examCourId, Integer userId,
            Integer expaId) {
        List<Integer> retList = new ArrayList<Integer>();
        // 组卷查询合法的试题
        if (param != null) {
            // 获取同类型的试题id
            Map<String, Object> query = new HashMap<String, Object>();
            query.put("examCourId", examCourId);
            query.put("questiontype", param.getGppaQuestiontype());
            query.put("difficulty", param.getGppaDifficulty());
            // 查出问题数量
            List<Integer> singleList = paperDAO.selectGroupQues(query);
            for (int i = 0; i < param.getGppaQuestionnum(); i++) {
                int random = CommonUtil.getRandom(singleList.size());
                // 判断随机数是否重复，没重复，就查询到的id存放到list
                retList.add(singleList.get(random));
                singleList.remove(random);
            }
        }
        ReGroupPaper groupPaper = null;
        if (retList.size() > 0) {
            // 获取随机试题编号
            for (Integer que : retList) {
                groupPaper = new ReGroupPaper();
                groupPaper.setRgpaExpaId(expaId);
                groupPaper.setRgpaExquId(que);
                groupPaper.setRgpaOrdinal(ordinal);
                groupPaper.setRgpaScore(param.getGppaQuestionscore());
                paperDAO.insertNewGroupPaper(groupPaper);
                ordinal++;
            }
            return ordinal;
        } else {
            return 0;
        }
    }

    /**
     * 删除试卷
     * 
     * @author yangzq
     * @param delPapers
     * @return
     */
    @Transactional
    public JsonAndView delPapers(String delPapers) throws Exception {
        JsonAndView jv = new JsonAndView();
        String[] paperIds = delPapers.split(",");
        // 删除试卷对于的考试信息和成绩
        List<Integer> delExinIds = paperDAO.findExamIdsByPaper(paperIds);
        if (delExinIds.size() > 0) {
            jv.setRet(false);
            jv.setErrmsg("该试卷已经参与考试，若要删除请在“课程测试管理”中先删除试卷对应的课程测试！");
            return jv;
            // List<Integer> rateIds = paperDAO.findRatrIdsByExam(delExinIds);
            // String rateIdString = rateIds.toString();
            // String[] deleteScoreQuery = rateIdString.substring(1, rateIdString.length() - 1).split(",");
            // if (deleteScoreQuery.length > 0) {
            // examDAO.deleteSolve(deleteScoreQuery);
            // examDAO.deleteCourseScore(deleteScoreQuery);
            // }
            // String examIdString = delExinIds.toString();
            // String[] delExins = examIdString.substring(1, examIdString.length() - 1).split(",");
            // if (delExins.length > 0) {
            // examDAO.delExin(delExins);
            // }
        } else {
            // 删除结束
            paperDAO.delReGroupPaper(paperIds);
            List<Integer> gppaIds = paperDAO.queryGppaIds(paperIds);
            paperDAO.delPapers(paperIds);
            if (gppaIds != null && gppaIds.size() > 0) {
                paperDAO.delGroupPaperParameter(gppaIds);
            }
            jv.setRet(true);
            return jv;
        }
    }

    /**
     * 校验用户自动组卷时的参数合法化
     * 
     * @author yangzq
     * @param quesType
     * @param quesNums
     * @param quesDifficult
     * @param quesCourId
     * @return
     */
    public JsonAndView validateQuesTypeNums(Integer quesType, Integer quesNums, Integer quesDifficult,
            Integer quesCourId) {
        JsonAndView jv = new JsonAndView();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("examCourId", quesCourId);
        map.put("questiontype", quesType);
        map.put("difficulty", quesDifficult);
        int quesNum = paperDAO.countQuesNums(map);
        if (quesNum > 0 && quesNum >= quesNums) {
            jv.setRet(true);
        } else {
            jv.setRet(false);
            jv.setErrmsg("试题数量不足，请更改条件!");
        }
        jv.addData("quesNum", quesNum);
        return jv;
    }

    /**
     * 课程测试管理展现已经配置好的测试信息
     * 
     * @author yangzq
     * @param quCourse
     * @param commonquery
     * @return
     */
    public QueryData viewAllExamInfo(Integer quCourse, CommonQuery commonquery, Integer departId, Integer userId,
            Integer departType, Integer userType) {
        QueryData queryData = new QueryData();
        // 构造查询条件
        ListQuery query = commonquery.format();
        if (userType.intValue() == 2) {
            query.fill("teacherId", "," + userId + ",");
        }
        if (userType.intValue() == 3 && departType.intValue() != 1 || userType.intValue() == 4
                && departType.intValue() != 1) {
            query.fill("departId", departId);
        }
        if (quCourse > 0) {
            query.fill("courseId", quCourse);
        }
        int totalCount = examDAO.queryAllExamInfoCount(query);
        queryData.setTotalCount(totalCount);
        if (totalCount == 0) {
            return queryData;
        }
        if (commonquery.getRecordPerPage() <= 0) {
            commonquery.setRecordPerPage(10);
        }
        query.fill("maxCount", commonquery.getRecordPerPage());
        int totalPage = QueryData.computeTotalPage(totalCount, commonquery.getRecordPerPage());
        queryData.setTotalPage(totalPage);
        List<PageData> pageDataList = Lists.newArrayList();
        // 未指定页数，则只读取前三页数据
        if (commonquery.getPageArray() == null) {
            commonquery.setPageArray(new int[] { 1, 2, 3 });
        }
        Date date = new Date();
        // 分别获取每页的数据
        for (int i = 0; i < commonquery.getPageArray().length; i++) {
            int page = commonquery.getPageArray()[i];
            if (page <= 0 || page > totalPage) {
                continue;
            }
            query.fill("startIndex", QueryData.computeStartIndex(page, commonquery.getRecordPerPage()));
            List<ExamInfo> examInfos = examDAO.queryAllExamInfo(query);
            for (ExamInfo examInfo : examInfos) {
                examInfo.setExinState("开放中");
                examInfo.setState(1);
                examInfo.setExinBgtime(DateFormatUtils.ISO_DATE_FORMAT.format(examInfo.getExinCreatetime()));
                if (examInfo.getExinBegintime() != null) {
                    if (date.before(examInfo.getExinBegintime())) {
                        examInfo.setExinState("未开放");
                        examInfo.setState(0);
                    }
                    examInfo.setExinBgtime(DateFormatUtils.ISO_DATE_FORMAT.format(examInfo.getExinBegintime()));
                } else if (date.before(examInfo.getExinCreatetime())) {
                    examInfo.setExinState("未开放");
                    examInfo.setState(0);
                }
                if (examInfo.getExinEndtime() != null) {
                    if (date.after(examInfo.getExinEndtime())) {
                        examInfo.setExinState("已结束");
                        examInfo.setState(2);
                    }
                    examInfo.setExinEdtime(DateFormatUtils.ISO_DATE_FORMAT.format(examInfo.getExinEndtime()));
                } else {
                    examInfo.setExinEdtime("");
                }
            }
            pageDataList.add(new PageData(page, examInfos));
        }
        // 装载返回结果
        queryData.setPageData(pageDataList);
        return queryData;
    }

    /**
     * Excel导入试题
     * 
     * @author yangzq
     * @param inputStream
     * @param fileType
     * @return
     * @throws Exception
     */
    @Transactional
    public String importQuesInfoFromExcel(User user, InputStream inputStream, String fileType) throws Exception {
        long startTime = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        List<ExamQuestion> ques = new ArrayList<ExamQuestion>();
        if (fileType.equals(".xls")) {
            // 处理03版本的excel
            HSSFSheet sheet = ExcelResolver_03.newInstance().createWorkbook(inputStream).getSheet(0);
            if (!ExcelResolver_03.isQuesInfoExcel(sheet)) {
                sb.append("导入的03版本excel试题库，数据源文件格式有误！请勿修改下载的模板文件！\r\n");
                return sb.toString();
            }
            ques = ExcelResolver_03.newInstance().quesInfoFromExcel(sheet);
        } else {
            // 处理07版本的excel
            XSSFSheet sheet = ExcelResolver_07.newInstance().createWorkBook(inputStream).getSheet(0);
            if (!ExcelResolver_07.isQuesInfoExcel(sheet)) {
                sb.append("导入的07版本excel试题库，数据源文件格式有误！请勿修改下载的模板文件！\r\n");
                return sb.toString();
            }
            ques = ExcelResolver_07.newInstance().quesInfoFromExcel(sheet);
        }
        if (ques.size() == 0) {
            sb.append("导入的试题库不能为空！\r\n");
            return sb.toString();
        }
        for (int i = 0; i < ques.size(); i++) {
            if (ques.get(i).getExquCourId() == 0 || ques.get(i).getExquCourId() == null) {
                sb.append("试题库中，第" + (i + 1) + "条数据序号非法！此数据未导入！\r\n");
                continue;
            }
            if (ques.get(i).getExquType() == 0 || ques.get(i).getExquType() == null) {
                sb.append("试题库中，第" + (i + 1) + "条数据试题类型非法！此数据未导入！\r\n");
                continue;
            }
            if (ques.get(i).getExquDescribe() == null || "".equals(ques.get(i).getExquDescribe())) {
                sb.append("试题库中，第" + (i + 1) + "条数据题干为空！此数据未导入！\r\n");
                continue;
            }
            if (ques.get(i).getExquType().equals(1) || ques.get(i).getExquType().equals(2)) {
                if (ques.get(i).getOptions().size() <= 0) {
                    sb.append("试题库中，第" + (i + 1) + "条数据没有选项！此数据未导入！\r\n");
                    continue;
                }
            }
            if (ques.get(i).getExquCorrectanswer() == null || "".equals(ques.get(i).getExquCorrectanswer())) {
                sb.append("试题库中，第" + (i + 1) + "条数据答案非法！此数据未导入！\r\n");
                continue;
            }
            if (ques.get(i).getExquDifficulty() == null || ques.get(i).getExquDifficulty().equals(-1)) {
                sb.append("试题库中，第" + (i + 1) + "条数据难度非法！此数据未导入！\r\n");
                continue;
            }
            Map<String, Object> query = new HashMap<String, Object>();
            if (user.getUserType() == 1) {
                query.put("userId", user.getUserId());
            }
            if (user.getUserType() == 2) {
                query.put("teacherId", "," + user.getUserId() + ",");
            }
            if (user.getUserType() == 3 && user.getUserDepartType() != 1 || user.getUserType() == 4
                    && user.getUserDepartType() != 1) {
                query.put("departId", user.getUserDepaId());
            }
            query.put("courseId", ques.get(i).getExquCourId());
            if (ques.get(i).getExquCourId() == 0 || ques.get(i).getExquCourId() == null) {
                sb.append("试题库中，第" + (i + 1) + "条数据课程号非法！此数据未导入！\r\n");
                continue;
            } else if (courseDao.findCourseByid(ques.get(i).getExquCourId()) == null) {
                sb.append("试题库中，第" + (i + 1) + "条数据课程号不存在！此数据未导入！\r\n");
                continue;
            } else if (courseDao.findCourseByAuthority(query) == 0) {
                sb.append("试题库中，第" + (i + 1) + "条数据,您没有此课程的权限！此数据未导入！\r\n");
                continue;
            }
            ques.get(i).setExquCreateUserid(String.valueOf(user.getUserId()));
            ques.get(i).setExquCreatetime(new Date());
            int exquId = questionDAO.saveQuestions(ques.get(i));
            if (exquId <= 0) {
                sb.append("试题库中，第" + (i + 1) + "条数据课程号非法！该条数据未导入！\r\n");
                continue;
            }
            // 如果是选择题分割试题选项
            if (ques.get(i).getOptions().size() > 0) {
                for (Option option : ques.get(i).getOptions()) {
                    option.setOptiQuesId(exquId);
                    questionDAO.saveOption(option);
                }
            }
        }
        long endTime = System.currentTimeMillis();
        logger.debug("time:" + (endTime - startTime));
        return sb.toString();
    }

    /**
     * 我的成绩察看
     * 
     * @author yangzq
     * @param quCourse
     * @param query
     * @return
     */
    public QueryData viewAllMyGrades(Integer userId, Integer quCourse, CommonQuery commonquery) {
        QueryData queryData = new QueryData();
        // 构造查询条件
        ListQuery query = commonquery.format();
        if (quCourse > 0) {
            query.fill("quCourse", quCourse);
        }
        query.fill("userId", userId);
        int totalCount = examDAO.selectAllMyGradesCount(query);
        queryData.setTotalCount(totalCount);
        if (totalCount == 0) {
            return queryData;
        }
        if (commonquery.getRecordPerPage() <= 0) {
            commonquery.setRecordPerPage(10);
        }
        query.fill("maxCount", commonquery.getRecordPerPage());
        int totalPage = QueryData.computeTotalPage(totalCount, commonquery.getRecordPerPage());
        queryData.setTotalPage(totalPage);
        List<PageData> pageDataList = Lists.newArrayList();
        // 未指定页数，则只读取前三页数据
        if (commonquery.getPageArray() == null) {
            commonquery.setPageArray(new int[] { 1, 2, 3 });
        }
        // 分别获取每页的数据
        for (int i = 0; i < commonquery.getPageArray().length; i++) {
            int page = commonquery.getPageArray()[i];
            if (page <= 0 || page > totalPage) {
                continue;
            }
            query.fill("startIndex", QueryData.computeStartIndex(page, commonquery.getRecordPerPage()));
            List<GradeModel> grades = examDAO.selectAllMyGradesDetail(query);
            for (GradeModel grade : grades) {
                if (grade.getExinTestTime() != null && !"".equals(grade.getExinTestTime())) {
                    grade.setExinTestFormatTime(DateFormatUtils.ISO_DATE_FORMAT.format(grade.getExinTestTime()));
                } else {
                    grade.setExinTestFormatTime("");
                }
            }
            pageDataList.add(new PageData(page, grades));
        }
        // 装载返回结果
        queryData.setPageData(pageDataList);
        return queryData;
    }

    /**
     * 检验创建课程测试名称是否有重复
     * 
     * @author yangzq
     * @param testName
     * @return 返回为 false没有通过验证， true为通过验证
     */
    public Boolean checkTestName(String name) {
        int i = examDAO.checkTestName(name);
        if (i > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 创建新的课程测试
     * 
     * @author yangzq
     * @param examInfo
     * @return
     */
    @Transactional
    public JsonAndView newExaminfo(ExamInfo examInfo) throws Exception {
        JsonAndView jv = new JsonAndView();
        String[] parsePatterns = { "yyyy-MM-dd" };
        if (examInfo.getExinBgtime() != null && !"".equals(examInfo.getExinBgtime())) {
            Date beginTime = DateUtils.parseDate(examInfo.getExinBgtime(), parsePatterns);
            examInfo.setExinBegintime(beginTime);
        } else {

        }
        if (examInfo.getExinEdtime() != null && !"".equals(examInfo.getExinEdtime())) {
            examInfo.setExinEndtime(DateUtils.parseDate(examInfo.getExinEdtime(), parsePatterns));
        }
        examInfo.setExinCreatetime(new Date());
        examDAO.newExaminfo(examInfo);
        jv.setRet(true);
        return jv;
    }

    /**
     * 编辑更新课程测试信息
     * 
     * @author yangzq
     * @param examInfo
     * @return
     */
    @Transactional
    public JsonAndView updateExaminfo(ExamInfo examInfo) throws Exception {
        JsonAndView jv = new JsonAndView();
        String[] parsePatterns = { "yyyy-MM-dd" };
        if (examInfo.getExinBgtime() != null && !"".equals(examInfo.getExinBgtime())) {
            Date beginTime = DateUtils.parseDate(examInfo.getExinBgtime(), parsePatterns);
            examInfo.setExinBegintime(beginTime);
        }
        if (examInfo.getExinEdtime() != null && !"".equals(examInfo.getExinEdtime())) {
            examInfo.setExinEndtime(DateUtils.parseDate(examInfo.getExinEdtime(), parsePatterns));
        }
        examDAO.updateExaminfo(examInfo);
        jv.setRet(true);
        return jv;
    }

    /**
     * 删除课程测试数据
     * 
     * @author yangzq
     * @param delRecords
     * @return
     */
    @Transactional
    public JsonAndView delExin(String delRecords) throws Exception {
        JsonAndView jv = new JsonAndView();
        String[] delExins = delRecords.split(",");
        if (delExins.length > 0) {
            // 删除试卷对于的考试信息和成绩
            List<Integer> delExinIds = Lists.newArrayList();
            for (int i = 0; i < delExins.length; i++) {
                delExinIds.add(Integer.valueOf(delExins[i]));
            }
            List<Integer> rateIds = paperDAO.findRatrIdsByExam(delExinIds);
            String rateIdString = rateIds.toString();
            String[] deleteScoreQuery = rateIdString.substring(1, rateIdString.length() - 1).split(",");
            if (deleteScoreQuery.length > 0) {
                examDAO.deleteSolve(deleteScoreQuery);
                examDAO.deleteCourseScore(deleteScoreQuery);
            }
            examDAO.delExin(delExins);
        }
        jv.setRet(true);
        return jv;
    }

    /**
     * 获取考试信息
     * 
     * @author yangzq
     * @param exinId
     * @return
     */
    public ExamInfo queryExinById(Integer exinId) {
        ExamInfo examInfo = examDAO.selectExamInfoById(exinId);
        if (examInfo.getExinBegintime() != null && !"".equals(examInfo.getExinBegintime())) {
            examInfo.setExinBgtime(DateFormatUtils.ISO_DATE_FORMAT.format(examInfo.getExinBegintime()));
        }
        if (examInfo.getExinEndtime() != null && !"".equals(examInfo.getExinEndtime())) {
            examInfo.setExinEdtime(DateFormatUtils.ISO_DATE_FORMAT.format(examInfo.getExinEndtime()));
        }
        return examInfo;
    }

    /**
     * 获得所有考试课程
     * 
     * @author lixw
     * @return
     */
    public List<ExamCourModel> getAllExamCource() {
        return examDAO.getUserCourse();
    }

    /**
     * 获得所有考试成绩
     * 
     * @author lixw
     * @param commonquery
     * @param rateExinId
     * @return
     */

    public QueryData showAllScore(CommonQuery commonquery, Integer courId, Integer departId, Integer userId,
            Integer departType, Integer userType) {

        QueryData queryData = new QueryData();
        // 构造查询条件
        ListQuery query = commonquery.format();
        if (userType.intValue() == 2) {
            query.fill("teacherId", "," + userId + ",");
            query.fill("departId", departId);
        }
        if (userType.intValue() == 3 && departType.intValue() != 1 || userType.intValue() == 4
                && departType.intValue() != 1) {
            query.fill("departId", departId);
        }
        if (courId > 0) {
            query.fill("courId", courId);
        }
        int totalCount = examDAO.countAllScore(query);
        queryData.setTotalCount(totalCount);
        if (totalCount == 0) {
            return queryData;
        }
        if (commonquery.getRecordPerPage() <= 0) {
            commonquery.setRecordPerPage(10);
        }
        query.fill("maxCount", commonquery.getRecordPerPage());
        int totalPage = QueryData.computeTotalPage(totalCount, commonquery.getRecordPerPage());
        queryData.setTotalPage(totalPage);
        List<PageData> pageDataList = Lists.newArrayList();
        // 未指定页数，则只读取前三页数据
        if (commonquery.getPageArray() == null) {
            commonquery.setPageArray(new int[] { 1, 2, 3 });
        }
        // 分别获取每页的数据
        for (int i = 0; i < commonquery.getPageArray().length; i++) {
            int page = commonquery.getPageArray()[i];
            if (page <= 0 || page > totalPage) {
                continue;
            }
            query.fill("startIndex", QueryData.computeStartIndex(page, commonquery.getRecordPerPage()));
            List<ScoreModel> scores = examDAO.getAllScore(query);
            for (ScoreModel score : scores) {
                if (score.getRateTime() != null && !"".equals(score.getRateTime())) {
                    score.setRateTimeFomat(DateFormatUtils.ISO_DATE_FORMAT.format(score.getRateTime()));
                } else {
                    score.setRateTimeFomat("");
                }
            }
            pageDataList.add(new PageData(page, scores));
        }
        // 装载返回结果
        queryData.setPageData(pageDataList);
        return queryData;

    }

    /**
     * 获得所有考试成绩
     * 
     * @author lixw
     * @param String [] deleteScoreQuery
     * @return
     */

    public void deleteScore(String[] deleteScoreQuery) throws Exception {
        examDAO.deleteSolve(deleteScoreQuery);
        examDAO.deleteCourseScore(deleteScoreQuery);
    }

}