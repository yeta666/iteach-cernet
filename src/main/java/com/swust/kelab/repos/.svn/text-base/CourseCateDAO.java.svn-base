package com.swust.kelab.repos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.swust.kelab.domain.CourseCategory;
import com.swust.kelab.model.CourseCategoryModel;

@Repository()
public class CourseCateDAO {
 private SqlSession sqlSession;
 @Autowired
 public void setSqlSession(SqlSession sqlSession) {
	 this.sqlSession = sqlSession;
	}
 
 	/**
 	 * 通过courseCate的id查询名字
 	 * @param ids
 	 * @author EasonLian
 	 * @throws Exception
 	 */
 	public List<String> viewCocaNameByIds(String ids) throws Exception {
 		Map<String,Object> params = new HashMap<String,Object>();
 		params.put("ids", ids);
 		return sqlSession.selectList("courseCate.viewCocaNameByIds",params);
 	}
 
	/**
	 * 获得所有分类的Map
	 * @author pery
	 * @return
	 */
	public Map<String,String> getAllEvame(){
		Map<String,String> allCate = new HashMap<String,String>();
		List<CourseCategory> list = sqlSession.selectList("courseCate.getAllEvame", null);
		for (CourseCategory c:list) {
			allCate.put(String.valueOf(c.getCocaId()), c.getCocaName());
		}
		return allCate;
	}
	
	/**
	 * 按照查询条件(分页),获取类别列表
	 * 
	 * @param query 查询条件
	 * @return      类别列表
	 * @see CourseCategoryModel
	 */
	public List<CourseCategoryModel> viewCourseCateList(Map query){
	    return sqlSession.selectList("courseCate.viewList", query);
	}
	
	/**
	 * 获取类别总数
	 * @return 类别总数
	 */
	public int countTotalCate(){
	    return ((Integer)sqlSession
	            .selectOne("courseCate.countTotalCate"))
	            .intValue();
	}
	
	/**
	 * 创建课程类别
	 * @param courseCate 课程类别对象
	 * @return 插入的记录条数
	 */
	public int createCourseCate(CourseCategory courseCate){
	    return ((Integer)sqlSession
                .insert("courseCate.create",courseCate))
                .intValue();
	}
	
	/**
	 * 批量删除课程类别
	 * @param courseCateIds 类别的id列表
	 * @return  实际删除的记录数
	 */
	public int delCourseCates(List<Integer> courseCateIds){
	    return ((Integer)sqlSession
                .delete("courseCate.delete",courseCateIds))
                .intValue();
	}
	
	/**
	 * 修改课程类别
	 * @param courseCate 新的课程类别对象
	 * @return           影响的数据库记录条数
	 */
	public int modifyCourseCate(CourseCategory courseCate){
	    return ((Integer)sqlSession
                .update("courseCate.modify",courseCate))
                .intValue();
	}
	
	/**
	 * 统计类别对应的课程数
	 * 
	 * @param cateIds 指定的类别id列表,
	 *                 若为空或者null,则分别统计所有类别对应的课程数
	 * @return        类别课程数
	 */
	public List<CourseCategoryModel> countCateCourseNum(List<Integer> cateIds){
	    return sqlSession.selectList("courseCate.countCourseNum",cateIds);
	}
	
	/**
     * 获得所有分类的列表
     * @author  Wu
     * @return  分类列表
     */
    public List<CourseCategory> viewAllCourseCates(int isOpen){
        List<CourseCategory> list = sqlSession.selectList("courseCate.getAllEvame", isOpen);
        return list;
    }
}
