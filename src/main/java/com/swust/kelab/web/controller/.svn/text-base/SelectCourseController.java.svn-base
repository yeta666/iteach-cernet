package com.swust.kelab.web.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.support.ServletContextResource;

import com.swust.kelab.domain.Attachment;
import com.swust.kelab.domain.Course;
import com.swust.kelab.domain.ScoreExportConfirm;
import com.swust.kelab.domain.User;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.repos.AttachmentDAO;
import com.swust.kelab.service.AttachmentService;
import com.swust.kelab.service.LogDBService;
import com.swust.kelab.service.ScoreExportConfirmService;
import com.swust.kelab.service.SelectCourseService;
import com.swust.kelab.utils.CookieUtil;
import com.swust.kelab.web.json.JsonAndView;

/**
 * 处理选课的controller
 * 
 * @author Wu
 * 
 */
@Controller
@RequestMapping("/selectCourse")
public class SelectCourseController {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    SelectCourseService selectCourseService;
    
    @Autowired
    AttachmentService attachmentService;
    
    @Autowired
    ScoreExportConfirmService scoreExportConfirmService;

    @Autowired
    private LogDBService logDBService;

    /**
     * 学生成绩查看,针对教师和教务员(根据是否传入userId的有效性来区分)
     * 
     * @param query 查询对象,包含分页信息 时间限定 和 搜索信息, 其中的searchType为: 1表示学生姓名,2表示学号
     * @param courseId 课程id,0表示该教师辅导或者主讲的所有课程
     * @param departId 学生所属学校id,0表示所有
     * @param gradeId 年级id,0表示所有
     * @param classId 班级id,0表示所有
     * @param userId 教师id,未指定（非正整数）,则查询所有课程(教务统计)
     * @param ismainortour 区分字段：0表示教务统计， 1表示主讲教师（或者同时为主讲和辅导），2表示辅导教师
     * @param scoreType 成绩筛选：0表示所有，1表示低于10分，2表示10分到20分（不到20）， 3表示20分到60分（不足60），4表示60分及以上, 5表示不到60分（用于教务统计的学生成绩查看）
     * @param confirmOrNot 成绩是否确认：0表示所有，1表示未确认，2表示已确认
     * @param courseYear 课程的学年：-1表示所有，0未设置、 1第一学年、 2第二学年 、3第三学年
     * @param courseTerm 课程的学期：-1表示所有，0未设置、 1上学期 、2下学期
     * @param courseTermPhase 课程的学段：-1表示所有，0未设置、 1不分学段、 2第一学段 、3第二学段
     * @param courseArtScience 课程的文理方向：-1表示所有，0公共 、1人文方向 、2理工方向
     * @param courseType 课程的类别：空串""表示所有，B表示必修、 XIA表示选修IA 、 XIB表示选修IB 、 XII表示选修II
     * @return 课程选修列表
     * @see JsonAndView,CommonQuery,QueryData
     */
    @RequestMapping(value = "/viewList", method = RequestMethod.POST)
    public JsonAndView viewCourseSelectList(CommonQuery query, 
            int courseId, int departId, int gradeId, int classId,
            int userId, int ismainortour, int scoreType,
            int confirmOrNot,int courseYear,int courseTerm,
            int courseTermPhase,int courseArtScience,String courseType) {
        JsonAndView jav = new JsonAndView();
        QueryData qd = selectCourseService.viewCourseSelectList(query, courseId, departId, gradeId, classId, userId,
                ismainortour, scoreType,confirmOrNot,courseYear,courseTerm,
                courseTermPhase,courseArtScience,courseType);
        if (qd == null) {
            jav.setRet(false);
            jav.setErrcode(1);
            jav.setErrmsg("统计学生学习信息失败！");
        } else {
            jav.addData("totalPage", qd.getTotalPage());
            jav.addData("totalCount", qd.getTotalCount());
            jav.addData("pageData", qd.getPageData());
        }
        return jav;
    }

    /**
     * 查询某个老师的所有课程
     * 
     * @author ZhangXin
     * @param userId
     * @return
     */
    @RequestMapping(value = "/queryTeacherCourses", method = RequestMethod.POST)
    public JsonAndView queryOneTeachersCourses(int userId) {
        JsonAndView jv = new JsonAndView();
        List<Course> list = selectCourseService.queryOneTeachersCourses(userId, 3);
        jv.addData("data", list);
        return jv;
    }

    /**
     * 教务统计成绩导出
     * 
     * @author lixw
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
     */
    @RequestMapping(value = "/exportCountScore", method = RequestMethod.GET)
    @SuppressWarnings("finally")
    public void exportCountScore(CommonQuery query, HttpServletRequest request, HttpServletResponse response,
            int courseId, int departId, int gradeId, int classId, int userId, int passOrNot
            ,int confirmOrNot,int courseYear,int courseTerm,
            int courseTermPhase,int courseArtScience
            ,String courseType) {
        String basePath = request.getSession().getServletContext().getRealPath("/");
        String filePath = selectCourseService.exportCourseSelectList(basePath, query, courseId, departId, gradeId,
                classId, userId, passOrNot,confirmOrNot,courseYear,courseTerm,
                courseTermPhase,courseArtScience,courseType);
        if (filePath != null) {
            ServletContextResource downFile = new ServletContextResource(request.getSession().getServletContext(),
                    filePath);
            if (!downFile.exists()) {
                logger.error("下载的文件不存在！");
                try {
                    response.reset();
                    response.sendRedirect("../../static/html/down_error.html");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return;
            }
            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            // 导出记录,先保存导出的文件
            String path="upload/score/";
            String saveFileName=(int)(Math.random()*1000)+"5"+new Date().getTime()+"5"+(int)(Math.random()*100000)+".xls";
            try {
                FileUtils.copyFile(downFile.getFile(), new File(basePath+"/"+path+saveFileName));
            } catch (IOException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
                logger.error("保存导出文件到指定文件夹失败！");
                try {
                    response.reset();
                    response.sendRedirect("../../static/html/down_error.html");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return;
            }
            Attachment attach=new Attachment();
            attach.setAttaDescribe("学生成绩导出表");
            attach.setAttaFilename(saveFileName);
            attach.setAttaLocation(path);
            attach.setAttaOriFilename(fileName);
            attach.setAttaSourceType(7);
            attachmentService.createAttachment(attach);
            int attachId=attach.getAttaId();
            if(attachId<=0){
                logger.error("保存导出文件信息到附件表失败！");
                try {
                    response.reset();
                    response.sendRedirect("../../static/html/down_error.html");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return;
            }
            User me = CookieUtil.getCookieUser(request);
            ScoreExportConfirm sec=new ScoreExportConfirm();
            sec.setSecoExportAttachId(attachId);
            sec.setSecoExportUserId(me.getUserId());
            scoreExportConfirmService.insertScoreExport(sec);
            int secId=sec.getSecoId();
            if(secId<=0){
                logger.error("保存导出表失败！");
                try {
                    response.reset();
                    response.sendRedirect("../../static/html/down_error.html");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return;
            }
            attachmentService.changeAttachSourceId(attachId, secId);
            String agintname = request.getHeader("User-Agent").toUpperCase();
            try {
                fileName = new String(fileName.getBytes(), "ISO-8859-1");
                if (agintname.indexOf("MSIE") > 0)
                    fileName = URLEncoder.encode(fileName, "ISO-8859-1");// IE浏览器
            } catch (UnsupportedEncodingException e1) {
                // TODO Auto-generated catch block
                logger.error("写入输出流失败！\n" + e1.getLocalizedMessage());
            }
            OutputStream os = null;
            try {
                os = response.getOutputStream();
                response.reset();
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
                response.setContentType("application/octet-stream; charset=utf-8");
                os.write(FileUtils.readFileToByteArray(downFile.getFile()));
                os.flush();                
                // 日志记录
                logDBService.insertNewLog(request, LogDBService.SELECT_OPERATION, "学生成绩查看", "导出选课及学习情况列表");
            } catch (Exception e) {
                logger.error("写入输出流失败！\n" + e.getLocalizedMessage());
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        logger.error("关闭输出流失败！\n" + e.getLocalizedMessage());
                    }
                }
                // 删除临时文件
                try {
                    downFile.getFile().delete();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    logger.error("删除临时文件失败！\n" + e.getLocalizedMessage());
                }
            }
        } else {
            logger.error("数据为空或者获取需要导出的数据时失败！");
            try {
                response.reset();
                response.sendRedirect("../../static/html/down_error.html");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
