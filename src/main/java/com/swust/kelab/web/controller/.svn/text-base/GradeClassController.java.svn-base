package com.swust.kelab.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.swust.kelab.domain.Clas;
import com.swust.kelab.domain.Grade;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.GradeClassModel;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.service.GradeInfoService;
import com.swust.kelab.service.GradeService;
import com.swust.kelab.service.LogDBService;
import com.swust.kelab.web.json.JsonAndView;

/**
 * 处理班级和年级的控制器
 * 
 * @author Wu
 * 
 */
@SuppressWarnings("unused")
@Controller
@RequestMapping("/gradeClass")
public class GradeClassController {
    @Autowired
    private GradeService gradeService;
    @Autowired
    private GradeInfoService gradeInfoService;
    @Autowired
    private LogDBService logDBService;

    /**
     * 获取部门对应的年级和班级,若部门的类型不为学校,则获取所有的年级和班级
     * 
     * @param departId 部门id
     * @return 年级和班级
     * @see
     */
    @RequestMapping(value = "/viewGradeClassByDepart", method = RequestMethod.POST)
    public JsonAndView viewGradeClassByDepart(int departId) {
        JsonAndView jav = new JsonAndView();
        List<GradeClassModel> gcms = gradeService.viewGradeClassByDepart(departId);
        // 封装返回结果
        if (gcms != null) {
            jav.addData("gradeClass", gcms);
        } else {
            jav.setRet(false);
            jav.setErrcode(1);
            jav.setErrmsg("获取部门对应的年级和班级失败！");
        }
        return jav;
    }

    /**
     * 增加年级
     * 
     * @author lancer
     * @param grade
     * @return
     */
    @SuppressWarnings("finally")
    @RequestMapping(value = "/insertGrade", method = RequestMethod.POST)
    public JsonAndView insertGrade(Grade grade, HttpServletRequest request) {
        Map<String, Object> data = new HashMap<String, Object>();
        JsonAndView jv = new JsonAndView();
        try {
            Integer index = gradeInfoService.insertGrade(grade);
            if (index.intValue() == 0) {
                data.put("state", "error!");
                data.put("message", "年级已经存在！");
            } else {
                data.put("state", "success");
                data.put("message", "年级添加成功！");
                logDBService.insertNewLog(request, logDBService.ADD_OPERATION, "班级管理", "添加新的班级");
            }
            jv.addAllData(data);
        } catch (Exception e) {
            data.put("state", "error");
            data.put("message", "年级添加失败！");
            jv.addAllData(data);
        } finally {
            return jv;
        }
    }

    /**
     * 修改年级
     * 
     * @author lancer
     * @param grade
     * @return
     */
    @SuppressWarnings("finally")
    @RequestMapping(value = "/updateGrade", method = RequestMethod.POST)
    public JsonAndView updateGrade(Grade grade, HttpServletRequest request) {
        Map<String, Object> data = new HashMap<String, Object>();
        JsonAndView jv = new JsonAndView();
        try {
            gradeInfoService.updateGrade(grade);            
            data.put("state", "success");
            data.put("message", "修改成功！");
            logDBService.insertNewLog(request, logDBService.UPDATA_OPERATION, "班级管理", "修改年级:" + grade.getGradName());
            jv.addAllData(data);
        } catch (Exception e) {
            data.put("state", "error");
            data.put("message", "修改失败！");
            jv.addAllData(data);
        } finally {
            return jv;
        }
    }

    /**
     * 修改班级
     * 
     * @author lancer
     * @param clas
     * @return
     */
    @SuppressWarnings({ "finally", "unused" })
    @RequestMapping(value = "/updateClass", method = RequestMethod.POST)
    public JsonAndView updateClass(int clasId, int depaId, String clasName, String gradName, int gradId,
            String clasRemark, HttpServletRequest request) {
        Map<String, Object> data = new HashMap<String, Object>();
        JsonAndView jv = new JsonAndView();
        Clas clas = new Clas();
        if (gradName != null && gradName != "" && gradId == -10000) {
            try {
                Grade grade = new Grade();
                grade.setGradDepaId(depaId);
                grade.setGradName(gradName);
                int index = gradeInfoService.insertGrade(grade);
                if (index == 0) {
                    data.put("state", "error");
                    data.put("message", "年级已存在!");
                } else {
                    clas.setClasId(clasId);
                    clas.setClasName(clasName);
                    clas.setClasRemark(clasRemark);
                    clas.setClasGradId(index);
                    gradeInfoService.updateClass(clas);
                    data.put("state", "success");
                    data.put("message", "修改成功!");
                    logDBService.insertNewLog(request, logDBService.UPDATA_OPERATION, "班级管理", "修改班级：" + clasName);
                }
            } catch (Exception e) {
                data.put("state", "error");
                data.put("message", "修改失败!");
            } finally {
                jv.addAllData(data);
                return jv;
            }
        } else if ((gradName == null || gradName == "") && gradId > 0) {
            clas.setClasId(clasId);
            clas.setClasGradId(gradId);
            clas.setClasName(clasName);
            clas.setClasRemark(clasRemark);
            try {
                int index = gradeInfoService.updateClass(clas);
                if (index == 0) {
                    data.put("state", "error");
                    data.put("message", "班级已存在!");
                } else {
                    data.put("state", "success");
                    data.put("message", "修改成功!");
                }
            } catch (Exception e) {
                data.put("state", "error");
                data.put("message", "修改失败!");
            } finally {
                jv.addAllData(data);
                return jv;
            }
        } else {
            data.put("state", "error");
            data.put("massage", "未知错误!");
            jv.addAllData(data);
            return jv;
        }
    }

    /**
     * 删除课程
     * 
     * @author lancer
     * @param clasIdArray
     * @return
     */
    @SuppressWarnings("finally")
    @RequestMapping(value = "/deleteClass", method = RequestMethod.POST)
    public JsonAndView deleteClass(String clasIdArray, HttpServletRequest request) {
        Map<String, Object> data = new HashMap<String, Object>();
        JsonAndView jv = new JsonAndView();
        String[] clasId = clasIdArray.split(",");
        try {
            gradeInfoService.deleteClass(clasId);
            data.put("state", "success");
            data.put("message", "删除成功！");
            logDBService.insertNewLog(request, logDBService.DELETE_OPERATION, "班级管理", "刪除班级");
        } catch (Exception e) {
            data.put("state", "error");
            data.put("message", "删除失败,请先删除该班级下的所有学生！");
        } finally {
            jv.addAllData(data);
            return jv;
        }
    }

    /**
     * 查询所有班级
     * 
     * @author lancer
     * @param depaId
     * @param gradId
     * @param query
     * @return
     */
    @RequestMapping(value = "/selectAllClass", method = RequestMethod.POST)
    public JsonAndView selectAllClass(int depaId, int gradId, CommonQuery query) {
        JsonAndView jv = new JsonAndView();
        QueryData queryData = gradeInfoService.selectAllClass(depaId, gradId, query);
        jv.addData("totalPage", queryData.getTotalPage());
        jv.addData("totalCount", queryData.getTotalCount());
        jv.addData("pageData", queryData.getPageData());
        return jv;
    }

    /**
     * 增加班级
     * 
     * @author lancer
     * @param depaId
     * @param clasName
     * @param gradName
     * @param gradId
     * @param clasRemark
     * @return
     */
    @SuppressWarnings({ "finally", "unused" })
    @RequestMapping(value = "/insertClass", method = RequestMethod.POST)
    public JsonAndView insertClass(@RequestParam("depaId") int depaId, @RequestParam("clasName") String clasName,
            @RequestParam("gradName") String gradName, @RequestParam("gradId") Integer gradId,
            @RequestParam("clasRemark") String clasRemark, HttpServletRequest request) {
        Map<String, Object> data = new HashMap<String, Object>();
        JsonAndView jv = new JsonAndView();
        Clas clas = new Clas();
        if (gradName != null && gradName != "" && gradId == -10000) {
            try {
                Grade grade = new Grade();
                grade.setGradDepaId(depaId);
                grade.setGradName(gradName);
                int index = gradeInfoService.insertGrade(grade);
                if (index == 0) {
                    data.put("state", "error");
                    data.put("message", "年级已经存在！");
                } else {
                    clas.setClasName(clasName);
                    clas.setClasRemark(clasRemark);
                    clas.setClasGradId(index);
                    gradeInfoService.insertClass(clas);
                    data.put("state", "success");
                    data.put("message", "添加成功！");
                    logDBService.insertNewLog(request, logDBService.ADD_OPERATION, "班级管理", "添加年级：" + clasName);
                }
            } catch (Exception e) {
                data.put("state", "error");
                data.put("message", "添加失败！");
            } finally {
                jv.addAllData(data);
                return jv;
            }
        } else if ((gradName == null || gradName == "") && gradId > 0) {
            clas.setClasGradId(gradId);
            clas.setClasName(clasName);
            clas.setClasRemark(clasRemark);
            try {
                int index = gradeInfoService.insertClass(clas);
                if (index == 0) {
                    data.put("state", "error");
                    data.put("message", "班级已存在！");
                } else {
                    data.put("state", "success");
                    data.put("message", "添加成功！");
                }
            } catch (Exception e) {
                data.put("state", "error");
                data.put("message", "添加失败！");
            } finally {
                jv.addAllData(data);
                return jv;
            }
        } else {
            data.put("state", "error");
            data.put("message", "未知错误！");
            jv.addAllData(data);
            return jv;
        }
    }

    /**
     * 查询单个班级
     * 
     * @author lancer
     * @param classId
     * @return
     */
    @RequestMapping(value = "/selectOneClass", method = RequestMethod.POST)
    public JsonAndView selectOneClass(int classId) {
        Map<String, Object> data = new HashMap<String, Object>();
        JsonAndView jv = new JsonAndView();
        data.put("data", gradeInfoService.selectClass(classId));
        jv.addAllData(data);
        return jv;
    }

}
