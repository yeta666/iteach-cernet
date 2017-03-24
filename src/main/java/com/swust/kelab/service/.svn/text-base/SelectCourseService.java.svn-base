package com.swust.kelab.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.swust.kelab.domain.Course;
import com.swust.kelab.domain.CourseCategory;
import com.swust.kelab.domain.EvaluateMethod;
import com.swust.kelab.domain.ReSelectCourse;
import com.swust.kelab.model.ClasModel;
import com.swust.kelab.model.ClassModel;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.CourseEvaluateModel;
import com.swust.kelab.model.CourseLearnModel;
import com.swust.kelab.model.CourseSelectModel;
import com.swust.kelab.model.DepartmentModel;
import com.swust.kelab.model.PageData;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.repos.CourseCateDAO;
import com.swust.kelab.repos.CourseDAO;
import com.swust.kelab.repos.DepartmentDao;
import com.swust.kelab.repos.EvaluateMethodDAO;
import com.swust.kelab.repos.GradeDao;
import com.swust.kelab.repos.ReSelectCourseDAO;
import com.swust.kelab.repos.bean.ListQuery;
import com.swust.kelab.utils.ExportAsExcel;
import com.swust.kelab.utils.FormatUtil;

@Service
public class SelectCourseService {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ReSelectCourseDAO reSelectCourseDAO;

    @Autowired
    private EvaluateMethodDAO evaluateMethodDAO;

    @Autowired
    private CourseDAO courseDAO;
    
    @Autowired
    private CourseCateDAO courseCateDAO;

    @Autowired
    private GradeDao gradeDao;

    @Autowired
    private DepartmentDao departmentDao;

    /**
     * 获取指定学生所选课程
     * 
     * @param stuId 学生id
     * @return 课程列表
     */
    public List<Course> viewSelectedCourses(int stuId) {
        try {
            Map query = new HashMap();
            query.put("userId", stuId);
            return reSelectCourseDAO.viewSelectedCourses(query);
        } catch (Exception e) {
            logger.error("viewSelectedCourses error!");
            return null;
        }
    }

    /**
     * 课程选修统计
     * 
     * @param query 查询对象,其中的searchWord指搜索学生名称
     * @param courseId 课程id,0表示所有
     * @param departId 学校id,0表示所有
     * @param gradeId 年级id,0表示所有
     * @param classId 班级id,0表示所有
     * @param userId 教师id,未指定,则查询所有课程(教务统计)
     * @param ismainortour 1表示主讲教师，2表示辅导教师
     * @param scoreType 1表示低于10分，2表示10分到20分（不到20）， 3表示20分到60分（不足60），4表示60分及以上, 5表示不到60分（用于教务统计的学生成绩查看）
     * @param confirmOrNot 成绩是否确认：0表示所有，1表示未确认，2表示已确认
     * @param courseYear 课程的学年：-1表示所有，0未设置、 1第一学年、 2第二学年 、3第三学年
     * @param courseTerm 课程的学期：-1表示所有，0未设置、 1上学期 、2下学期
     * @param courseTermPhase 课程的学段：-1表示所有，0未设置、 1不分学段、 2第一学段 、3第二学段
     * @param courseArtScience 课程的文理方向：-1表示所有，0公共 、1人文方向 、2理工方向
     * @param courseType 课程的类别：空串""表示所有，B表示必修、 XIA表示选修IA 、 XIB表示选修IB 、 XII表示选修II
     * @return 课程选修列表
     * @see CourseSelectModel,CommonQuery,QueryData
     */
    public QueryData viewCourseSelectList(CommonQuery query, int courseId, int departId, int gradeId, int classId,
            int userId, int ismainortour, int scoreType,int confirmOrNot,int courseYear,int courseTerm,
            int courseTermPhase,int courseArtScience,String courseType) {
        QueryData result = new QueryData();

        // 查询条件
        ListQuery myQuery = query.format();
        // 课程限制
        List<Course> courses = null;
        if (courseId > 0) {
            myQuery.fill("courseId", courseId);
        } else {
            // 获取该教师辅导或者主讲的课程
            if (userId > 0) {
                Map temp = new HashMap();
                temp.put("teacherId", userId);
                temp.put("type", 3);
                try {
                    courses = courseDAO.viewCourseListByTeacher(temp);
                } catch (Exception e) {
                    logger.error("viewCourseListByTeacher error!\n" + e.getMessage());
                    return null;
                }
                if (courses == null || courses.size() <= 0) {
                    return result;
                }
                myQuery.fill("courses", courses);
            }else if(courseYear!=-1||courseTerm!=-1
                    ||courseTermPhase!=-1||courseArtScience!=-1
                    ||courseType!=null&&!courseType.isEmpty()){
                //按照课程的条件筛选
                Map courQuery=new HashMap();
                if(courseYear>-1){
                    courQuery.put("year", courseYear);
                }
                if(courseTerm>-1){
                    courQuery.put("term", courseTerm);
                }
                if(courseTermPhase>-1){
                    courQuery.put("phase", courseTermPhase);
                }
                if(courseArtScience>-1){
                    courQuery.put("artScience", courseArtScience);
                }
                if(courseType!=null&&!courseType.isEmpty()){
                    courQuery.put("type", courseType);
                }
                //查询
                try {
                    courses = courseDAO.viewStatisticCourseList(courQuery);
                } catch (Exception e) {
                    logger.error("viewStatisticCourseList error!\n" + e.getMessage());
                    return null;
                }
                if (courses == null || courses.size() <= 0) {
                    result.setTotalCount(0);
                    return result;
                }
                myQuery.fill("courses", courses);
            }
        }
        // 机构限制
        /*
         * if(userId<=0&&departId>0){ myQuery.fill("departId", departId); }else if(userId>0){
         * if(ismainortour==2&&departId>0){ myQuery.fill("departId", departId); } }
         */
        if (departId > 0) {
            myQuery.fill("departId", departId);
        }
        // 班级限制
        if (classId > 0) {
            myQuery.fill("classId", classId);
        } else if (gradeId > 0) {
            List<Integer> classIds = new ArrayList<Integer>();
            List<ClassModel> classes = gradeDao.queryClassByGradeId(gradeId);
            if (classes == null || classes.isEmpty()) {
                logger.error("cannot find classes in grade:" + gradeId);
                return result;
            }
            for (ClassModel cm : classes) {
                classIds.add(cm.getClasId());
            }
            myQuery.fill("classIds", classIds);
        }
        // 成绩限制（是否合格）
        if (scoreType > 0) {
            myQuery.fill("scoreType", scoreType);
        }
        if(confirmOrNot>0){
            myQuery.fill("confirm", confirmOrNot);
        }
        // 查询总数
        int totalNum = 0;
        try {
            totalNum = reSelectCourseDAO.countSelectNumByQuery(myQuery);
        } catch (Exception e) {
            logger.error("countSelectNumByQuery error!\n" + e.getMessage());
            return null;
        }
        result.setTotalCount(totalNum);
        if (totalNum <= 0) {
            return result;
        }

        // 预先查询基本信息（课程、班级（年级）、机构）
        // 课程信息
        List<Integer> courseIds = new ArrayList<Integer>();
        if (courseId > 0) {
            courseIds.add(courseId);
        } else {
            if (courses != null && courses.size() > 0) {
                for (Course c : courses) {
                    courseIds.add(c.getCourId());
                }
            }
        }
        Map<Integer, CourseLearnModel> courseInfo = new HashMap<Integer, CourseLearnModel>();
        try {
            Map map = new HashMap();
            map.put("courseIds", courseIds);
            List<CourseLearnModel> clms = courseDAO.viewCourseBaseList(map);
            map.clear();
            map = null;
            if (clms != null && !clms.isEmpty()) {
                for (CourseLearnModel clm : clms) {
                    //转换课程类别
                    String courType=clm.getCourseType();
                    if(courType.equals("B")){
                        clm.setCourseType("必修");
                    }else
                    if(courType.equals("XIA")){
                        clm.setCourseType("选修IA");
                    }else
                    if(courType.equals("XIB")){
                        clm.setCourseType("选修IB");
                    }else
                    if(courType.equals("XII")){
                        clm.setCourseType("选修II");
                    }
                    courseInfo.put(clm.getCourseId(), clm);
                }
            }
        } catch (Exception e) {
            logger.error("viewCourseBaseList error!\n" + e.getMessage());
        }
        courseIds.clear();
        courseIds = null;
        // 班级年级信息
        Map<Integer, ClasModel> classInfo = new HashMap<Integer, ClasModel>();
        try {
            Map map = new HashMap();
            if (classId > 0) {
                map.put("classId", classId);
            } else if (gradeId > 0) {
                map.put("gradeId", gradeId);
            } else if (departId > 0) {
                map.put("depaId", departId);
            }
            List<ClasModel> cms = gradeDao.queryClassGrade(map);
            map.clear();
            map = null;
            if (cms != null && !cms.isEmpty()) {
                for (ClasModel cm : cms) {
                    classInfo.put(cm.getClasId(), cm);
                }
            }
        } catch (Exception e) {
            logger.error("queryClassGrade error!\n" + e.getMessage());
        }
        // 机构名称
        Map<Integer, String> depaInfo = new HashMap<Integer, String>();
        try {
            Integer depaId = null;
            if (departId > 0) {
                depaId = departId;
            }
            List<DepartmentModel> dms = departmentDao.queryDepartName(depaId);
            if (dms != null && !dms.isEmpty()) {
                for (DepartmentModel dm : dms) {
                    depaInfo.put(dm.getDepaId(), dm.getDepaName());
                }
            }
        } catch (Exception e) {
            logger.error("queryDepartName error!\n" + e.getMessage());
        }

        // 根据分页查询列表
        // 默认分页信息
        if (query.getRecordPerPage() <= 0) {
            query.setRecordPerPage(10);
        }
        if (query.getPageArray() == null) {
            query.setPageArray(new int[] { 1, 2, 3 });
        }
        result.setTotalPage(QueryData.computeTotalPage(totalNum, query.getRecordPerPage()));
        // 查询
        int startIndex = (query.getPageArray()[0] - 1) * query.getRecordPerPage();
        int fetchSize = query.getPageArray().length * query.getRecordPerPage();
        myQuery.fill("startIndex", startIndex);
        myQuery.fill("maxCount", fetchSize);
        List<CourseSelectModel> csms = null;
        try {
            csms = reSelectCourseDAO.viewCourseSelectList(myQuery);
        } catch (Exception e) {
            logger.error("viewCourseSelectList error!\n" + e.getMessage());
            return null;
        }

        // 数据处理(时间格式 计算学习进度)和封装
        // System.out.println(csms.size());
        if (csms != null && csms.size() > 0) {
            // 填充预先查询的数据
            for (CourseSelectModel csm : csms) {
                CourseLearnModel clm = courseInfo.get(csm.getCourseId());
                csm.setCourseName(clm.getCourseName());
                csm.setCourseCredit(clm.getCourseCredit());
                csm.setCourseCode(clm.getCourseCode());
                csm.setCourseType(clm.getCourseType());
                csm.setTepaId(clm.getCourTepaId());
                ClasModel cm = classInfo.get(csm.getStuClassId());
                if (cm != null) {
                    csm.setStuClass(cm.getClasName());
                    csm.setStuGrade(cm.getGradeName());
                }
                csm.setSchoolName(depaInfo.get(csm.getSchoolId()));
            }

            // 时间格式
            for (CourseSelectModel csm : csms) {
                csm.setFormarttedTime(FormatUtil.formatDate(csm.getSelectTime()));
            }
            // 获取相关的课程
            Map<Integer, Boolean> courseMap = Maps.newHashMap();
            for (CourseSelectModel lpm : csms) {
                if (!courseMap.containsKey(lpm.getCourseId())) {
                    courseMap.put(lpm.getCourseId(), true);
                }
            }
            // 查询课程的考核方式
            List<CourseEvaluateModel> evaMethods = Lists.newArrayList();
            try {
                evaMethods = evaluateMethodDAO.getEvaluateMethodByCourse(new ArrayList<Integer>(courseMap.keySet()));
            } catch (Exception e) {
                logger.error("getEvaluateMethodByCourse error!" + e.getLocalizedMessage());
            }
            courseMap.clear();
            courseMap = null;
            // 解析考核方式，并放入map
            Map<Integer, List<Double>> patterns = new HashMap<Integer, List<Double>>();
            Map<Integer, List<Integer>> threholds = new HashMap<Integer, List<Integer>>();
            if (evaMethods != null && evaMethods.size() > 0) {
                for (CourseEvaluateModel cem : evaMethods) {
                    String patternStr = cem.getEvaluatePattern();
                    // System.out.println("pattern:"+patternStr);
                    if (patternStr != null && patternStr.length() > 0) {
                        List<Double> p = new ArrayList<Double>();
                        String[] temp = patternStr.split(",");
                        for (String str : temp) {
                            p.add(1.0 * Double.parseDouble(str) / 100);
                        }
                        patterns.put(cem.getCourseId(), p);
                    }
                    String threholdStr = cem.getThrehold();
                    // System.out.println("hold:"+threholdStr);
                    if (threholdStr != null && threholdStr.length() > 0) {
                        List<Integer> t = new ArrayList<Integer>();
                        String[] temp = threholdStr.split(",");
                        for (String str : temp) {
                            t.add(Integer.parseInt(str));
                        }
                        threholds.put(cem.getCourseId(), t);
                    }
                }
            }
            // 根据考核方式获取成绩
            // 集中学习的时间被合并到视频学习时间中，不单独计算成绩
            for (CourseSelectModel csm : csms) {
                List<Integer> threhold = threholds.get(csm.getCourseId());
                List<Double> pattern = patterns.get(csm.getCourseId());
                if (threhold != null && threhold.size() == 4 && pattern != null && pattern.size() == 4) {
                    Double tempScore = 0.0;
                    Double totalScore = 0.0;
                    // 集中学习
                    /*
                     * if(csm.getMassedLearnTime()>=threhold.get(0)){ tempScore=100.0*pattern.get(0); }else{
                     * tempScore=100.0*csm.getMassedLearnTime() /threhold.get(0)*pattern.get(0); }
                     * totalScore+=tempScore; csm.setMassedLearnScore(Float.valueOf(tempScore.toString()));
                     */
                    // 学习次数
                    if (csm.getLearnNum() >= threhold.get(0)) {
                        tempScore = 100.0 * pattern.get(0);
                    } else {
                        tempScore = 100.0 * csm.getLearnNum() / threhold.get(0) * pattern.get(0);
                    }
                    totalScore += tempScore;
                    csm.setLearnNumScore(Float.valueOf(tempScore.toString()));
                    // (视频)学习时间
                    csm.setLearnTime(csm.getLearnTime() + csm.getMassedLearnTime());
                    if (csm.getLearnTime() >= threhold.get(1)) {
                        tempScore = 100.0 * pattern.get(1);
                    } else {
                        tempScore = 100.0 * csm.getLearnTime() / threhold.get(1) * pattern.get(1);
                    }
                    totalScore += tempScore;
                    csm.setLearnTimeScore(Float.valueOf(tempScore.toString()));
                    // 论坛讨论
                    if (csm.getBbsDiscuss() >= threhold.get(2)) {
                        tempScore = 100.0 * pattern.get(2);
                    } else {
                        tempScore = 100.0 * csm.getBbsDiscuss() / threhold.get(2) * pattern.get(2);
                    }
                    totalScore += tempScore;
                    csm.setBbsDiscussScore(Float.valueOf(tempScore.toString()));
                    // 在线自测
                    if (csm.getTestSourceScore() >= threhold.get(3)) {
                        tempScore = 100.0 * pattern.get(3);
                    } else {
                        tempScore = 100.0 * csm.getTestSourceScore() / threhold.get(3) * pattern.get(3);
                    }
                    totalScore += tempScore;
                    csm.setTestScore(Float.valueOf(tempScore.toString()));
                    // 主观评价
                    /*
                     * if(csm.getSubAssessSourceScore()>=threhold.get(5)){ tempScore=100.0*pattern.get(5); }else{
                     * tempScore=100.0*csm.getSubAssessSourceScore() /threhold.get(5)*pattern.get(5); }
                     * totalScore+=tempScore; csm.setSubAssessScore(Float.valueOf(tempScore.toString()));
                     */
                    // 总分
                    csm.setTotalScore(Float.valueOf(totalScore.toString()));
                } else {
                    logger.error("找不到相应课程的考核方式,或者考核方式格式有误");
                    csm.setTotalScore(-1);
                }
            }
            patterns.clear();
            patterns = null;
            threholds.clear();
            threholds = null;
            // 分页处理
            int endIndex;
            List<PageData> pageDataList = Lists.newArrayList();
            for (int k = 0; k < query.getPageArray().length; k++) {
                int page = query.getPageArray()[k];
                if (page <= 0 || page > result.getTotalPage()) {
                    continue;
                }
                startIndex = k * query.getRecordPerPage();
                endIndex = startIndex + query.getRecordPerPage();
                if (startIndex >= csms.size()) {
                    continue;
                }
                if (endIndex > csms.size()) {
                    endIndex = csms.size();
                }
                List<CourseSelectModel> pageDatas = csms.subList(startIndex, endIndex);
                pageDataList.add(new PageData(page, pageDatas));
            }
            result.setPageData(pageDataList);
        }
        return result;
    }

    /**
     * 导出成绩数据
     * 
     * @author lancer
     * @param query 查询对象,包含分页信息 时间限定 和 搜索信息, 其中的searchType为: 1表示学生姓名,2表示学号
     * @param request 请求对象
     * @param response 响应对象
     * @param courseId 课程id,0表示该教师辅导或者主讲的所有课程
     * @param departId 学生所属学校id,0表示所有
     * @param gradeId 年级id,0表示所有
     * @param classId 班级id,0表示所有
     * @param userId 教师id,未指定(非正整数)则查询所有课程(教务统计)
     * @param passOrNot 4表示通过（分数大于等于60分），5表示未通过
     * @param confirmOrNot 0表示所有，1表示未确认，2表示已确认
     * @param courseYear 课程的学年：-1表示所有，0未设置、 1第一学年、 2第二学年 、3第三学年
     * @param courseTerm 课程的学期：-1表示所有，0未设置、 1上学期 、2下学期
     * @param courseTermPhase 课程的学段：-1表示所有，0未设置、 1不分学段、 2第一学段 、3第二学段
     * @param courseArtScience 课程的文理方向：-1表示所有，0公共 、1人文方向 、2理工方向
     * @param courseType 课程的类别：空串""表示所有，B表示必修、 XIA表示选修IA 、 XIB表示选修IB 、 XII表示选修II
     * @return
     * @throws Exception
     */
    public String exportCourseSelectList(String basePath, CommonQuery query, int courseId, int departId, int gradeId,
            int classId, int userId, int passOrNot,int confirmOrNot,int courseYear,int courseTerm,
            int courseTermPhase,int courseArtScience,String courseType) {
        String result = null;
        // 查询条件，暂时不考虑针对老师的导出（即userId暂不使用）
        ListQuery myQuery = query.format();
        // 课程限制
        if (courseId > 0) {
            myQuery.fill("courseId", courseId);
        }else if(courseYear!=-1||courseTerm!=-1
                ||courseTermPhase!=-1||courseArtScience!=-1
                ||courseType!=null&&!courseType.isEmpty()){
            //按照课程的条件筛选
            Map courQuery=new HashMap();
            if(courseYear>-1){
                courQuery.put("year", courseYear);
            }
            if(courseTerm>-1){
                courQuery.put("term", courseTerm);
            }
            if(courseTermPhase>-1){
                courQuery.put("phase", courseTermPhase);
            }
            if(courseArtScience>-1){
                courQuery.put("artScience", courseArtScience);
            }
            if(courseType!=null&&!courseType.isEmpty()){
                courQuery.put("type", courseType);
            }
            //查询
            List<Course> courses=null;
            try {
                courses = courseDAO.viewStatisticCourseList(courQuery);
            } catch (Exception e) {
                logger.error("viewStatisticCourseList error!\n" + e.getMessage());
                return null;
            }
            if (courses == null || courses.size() <= 0) {
                return result;
            }
            myQuery.fill("courses", courses);
        }
        // 机构限制
        if (departId > 0) {
            myQuery.fill("departId", departId);
        }
        // 班级限制
        if (classId > 0) {
            myQuery.fill("classId", classId);
        } else if (gradeId > 0) {
            List<Integer> classIds = new ArrayList<Integer>();
            List<ClassModel> classes = gradeDao.queryClassByGradeId(gradeId);
            if (classes == null || classes.isEmpty()) {
                logger.error("cannot find classes in grade:" + gradeId);
                return result;
            }
            for (ClassModel cm : classes) {
                classIds.add(cm.getClasId());
            }
            myQuery.fill("classIds", classIds);
        }
        // 成绩限制（是否合格）
        if (passOrNot > 0) {
            myQuery.fill("scoreType", passOrNot);
        }
        if(confirmOrNot>0){
            myQuery.fill("confirm", confirmOrNot);
        }
        // 查询总数
        int totalNum = 0;
        try {
            totalNum = reSelectCourseDAO.countSelectNumByQuery(myQuery);
        } catch (Exception e) {
            logger.error("countSelectNumByQuery error!\n" + e.getMessage());
            return null;
        }
        if (totalNum <= 0) {
            return null;
        }
        
        // 查询选课成绩、课程信息以及选课人基本信息
        List<CourseSelectModel> csms = null;
        try {
            csms = reSelectCourseDAO.viewExportCourseSelectList(myQuery);
        } catch (Exception e) {
            logger.error("viewCourseSelectList error!\n" + e.getMessage());
            return null;
        }
        if(csms==null||csms.size()<=0){
            return result;
        }
        //先获取所有课程的科目（课程领域）
        List<CourseCategory> cates=null;
        try {
            cates = courseCateDAO.viewAllCourseCates(-1);
        } catch (Exception e) {
            logger.error("viewAllCourseCates error!\n" + e.getMessage());
            return null;
        }
        if(cates==null||cates.size()<=0){
            return result;
        }
        Map<Integer,String> cateNames=new HashMap<Integer, String>();
        for(CourseCategory cc:cates){
            cateNames.put(cc.getCocaId(), cc.getCocaName());
        }
        // 生成课程编码
        Map<Integer,CourseLearnModel> courseEncodings=new HashMap<Integer, CourseLearnModel>();
        Map<String,Integer> encodingsCourse=new HashMap<String, Integer>();
        Map<String,Integer> encodings=new HashMap<String, Integer>();
        for (CourseSelectModel csm : csms) {
            if(!courseEncodings.containsKey(csm.getCourseId())){
                StringBuilder encoding=new StringBuilder();
                String cateIds=csm.getCourCategory();
                if(cateIds!=null&&cateIds.length()>0){
                    cateIds=cateIds.substring(1,cateIds.length()-1);
                    String ids[]=cateIds.split(",");
                    int cateId=Integer.parseInt(ids[0]);
                    if(cateNames.containsKey(cateId)){
                        encoding.append(cateNames.get(cateId));
                    }
                    for(int i=1;i<ids.length;i++){
                        cateId=Integer.parseInt(ids[i]);
                        if(cateNames.containsKey(cateId)){
                            encoding.append("|").append(cateNames.get(cateId));
                        }
                    }                    
                }
                if(encoding.length()==0){
                    encoding.append("无科目");
                }
                encoding.append(".").append(csm.getCourYear())
                        .append(csm.getCourTerm())
                        .append(csm.getCourStudyPhase())
                        .append(".").append(csm.getCourArtScience())
                        .append(".").append(csm.getCourseType());
                String finalEncoding=encoding.toString();
                if(encodings.containsKey(finalEncoding)){
                    int count=encodings.get(finalEncoding);
                    if(count==1){
                        int tempCourId=encodingsCourse.get(finalEncoding);
                        CourseLearnModel temp=courseEncodings.get(tempCourId);
                        temp.setEncoding(finalEncoding+"(01)");
                    }
                    count++;
                    encodings.put(finalEncoding, count);
                    if(count<10){
                        finalEncoding=encoding.append("(").append("0")
                                .append(count).append(")").toString();
                    }else{
                        finalEncoding=encoding.append("(")
                                .append(count).append(")").toString();
                    }
                }else{
                    encodings.put(finalEncoding, 1);
                    encodingsCourse.put(finalEncoding, csm.getCourseId());
                }
                CourseLearnModel clm=new CourseLearnModel();
                clm.setCourseId(csm.getCourseId());
                clm.setCourseName(csm.getCourseName());
                clm.setEncoding(finalEncoding);
                courseEncodings.put(csm.getCourseId(), clm);
            }
        }
        
        // 导出操作
        String path = "upload/temp/学生成绩表-"+(int)(Math.random()*100000)+".xls";
        File file = new File(basePath + "/" + path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error("创建文件失败!\n" + e.getLocalizedMessage());
        }
        boolean flag = ExportAsExcel.exportStudentScore(file, csms, courseEncodings);
        if(flag){
            result=path;
        }
        return result;
    }

    /**
     * 查询某个老师的所有课程
     * 
     * @author ZhangXin
     * @param userId
     * @param type
     * @return
     */
    public List<Course> queryOneTeachersCourses(int userId, int type) {
        Map temp = new HashMap();
        temp.put("teacherId", userId);
        temp.put("type", type);
        List<Course> courses = null;
        try {
            courses = courseDAO.viewCourseListByTeacher(temp);
        } catch (Exception e) {
            logger.error("viewCourseListByTeacher error!\n" + e.getMessage());
        }
        return courses;
    }

    /**
     * @param userId 学生id
     * @param courId 课程id
     * 
     *            更新指定学生对于指定课程的总成绩
     */
    public void updateCourseTotalScore(int userId, int courId) {
        // 获取学习情况
        ReSelectCourse sc = null;
        try {
            sc = reSelectCourseDAO.selectOneByUserIdAndUserId(courId, userId);
        } catch (Exception e) {
            logger.error("selectOneByUserIdAndUserId error!\n" + e.getMessage());
        }
        if (sc != null) {
            // 获取并解析课程对应的考核方式
            EvaluateMethod em = null;
            try {
                em = evaluateMethodDAO.selectOneByCourseId(courId);
            } catch (Exception e) {
                logger.error("selectOneByCourseId error!\n" + e.getMessage());
            }
            if (em == null || em.getEvmePattern() == null || em.getEvmeThrehold() == null) {
                return;
            }
            String[] threholdStrs = em.getEvmeThrehold().split(",");
            String[] patternStrs = em.getEvmePattern().split(",");
            List<Integer> threhold = new ArrayList<Integer>();
            List<Double> pattern = new ArrayList<Double>();
            for (String str : threholdStrs) {
                threhold.add(Integer.parseInt(str));
            }
            for (String str : patternStrs) {
                pattern.add(1.0 * Double.parseDouble(str) / 100);
            }
            // 计算总分
            // 集中学习的时间被合并到视频学习时间中，不单独计算成绩
            Double totalScore = 0.0;
            if (threhold != null && threhold.size() == 4 && pattern != null && pattern.size() == 4) {
                Double tempScore = 0.0;
                // 集中学习
                /*
                 * if(sc.getRscoMassedlearnscore()>=threhold.get(0)){ tempScore=100.0*pattern.get(0); }else{
                 * tempScore=100.0*sc.getRscoMassedlearnscore() /threhold.get(0)*pattern.get(0); }
                 * totalScore+=tempScore;
                 */
                // 学习次数
                if (sc.getRscoLoginscore() >= threhold.get(0)) {
                    tempScore = 100.0 * pattern.get(0);
                } else {
                    tempScore = 100.0 * sc.getRscoLoginscore() / threhold.get(0) * pattern.get(0);
                }
                totalScore += tempScore;
                // (视频)学习时间
                sc.setRscoLearntimescore(sc.getRscoLearntimescore() + sc.getRscoMassedlearnscore());
                if (sc.getRscoLearntimescore() >= threhold.get(1)) {
                    tempScore = 100.0 * pattern.get(1);
                } else {
                    tempScore = 100.0 * sc.getRscoLearntimescore() / threhold.get(1) * pattern.get(1);
                }
                totalScore += tempScore;
                // 论坛讨论
                if (sc.getRscoBbsdiscussscore() >= threhold.get(2)) {
                    tempScore = 100.0 * pattern.get(2);
                } else {
                    tempScore = 100.0 * sc.getRscoBbsdiscussscore() / threhold.get(2) * pattern.get(2);
                }
                totalScore += tempScore;
                // 在线自测
                if (sc.getRscoTestscore() >= threhold.get(3)) {
                    tempScore = 100.0 * pattern.get(3);
                } else {
                    tempScore = 100.0 * sc.getRscoTestscore() / threhold.get(3) * pattern.get(3);
                }
                totalScore += tempScore;
                /*
                 * //主观评价 if(sc.getRscoSubassessscore()>=threhold.get(5)){ tempScore=100.0*pattern.get(5); }else{
                 * tempScore=100.0*sc.getRscoSubassessscore() /threhold.get(5)*pattern.get(5); } totalScore+=tempScore;
                 */
            } else {
                return;
            }
            // 更新总分
            sc.setRscoTotalscore(Float.parseFloat(totalScore.toString()));
            List<ReSelectCourse> scs = new ArrayList<ReSelectCourse>();
            scs.add(sc);
            try {
                reSelectCourseDAO.updateTotalScoreById(scs);
            } catch (Exception e) {
                logger.error("updateTotalScoreById error!\n" + e.getMessage());
            }
        }
    }
}
