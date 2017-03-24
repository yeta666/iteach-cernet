package com.swust.kelab.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.swust.kelab.domain.MassedLearning;
import com.swust.kelab.domain.ReSelectCourse;
import com.swust.kelab.domain.StudentMassedLearning;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.MassedLearningModel;
import com.swust.kelab.model.PageData;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.model.StudentMassedLearningInfoModel;
import com.swust.kelab.repos.MassedLearningDao;
import com.swust.kelab.repos.ReSelectCourseDAO;
import com.swust.kelab.repos.bean.ListQuery;
import com.swust.kelab.utils.FormatUtil;

@Service(value = "massedLearningService")
public class MassedLearningService {

	private MassedLearningDao massedLearningDao;

	private ReSelectCourseDAO reSelectCourseDao;

	@Autowired
	private SelectCourseService selectCourseService;

	/**
	 * 失败
	 */
	final static private Integer FAILED = 0;

	/**
	 * 成功
	 */
	final static private Integer SUCCESS = 1;

	/**
	 * 无数据
	 */
	final static private Integer NODATA = -1;

	@Autowired
	public void setMassedLearningDao(MassedLearningDao massedLearningDao) {
		this.massedLearningDao = massedLearningDao;
	}

	@Autowired
	public void setReSelectCourseDao(ReSelectCourseDAO reSelectCourseDao) {
		this.reSelectCourseDao = reSelectCourseDao;
	}

	/**
	 * 查询集中学习记录
	 * 
	 * @param commonQuery
	 * @param mlModel
	 * @return
	 * @throws Exception
	 */
	public QueryData queryMassedLearningInfo(CommonQuery commonQuery,
			MassedLearningModel mlModel) throws Exception {
		QueryData queryData = new QueryData();
		ListQuery query = commonQuery.format();
		query.fill("maleId", mlModel.getMaleId());
		query.fill("maleTeacherId", mlModel.getMaleTeacherId());
		query.fill("maleSchoolId", mlModel.getMaleSchoolId());
		query.fill("maleState", mlModel.getMaleState());
		query.fill("maleName", mlModel.getMaleName());
		query.fill("maleCreateTime", mlModel.getMaleCreateTime());

		int countAllInfo = massedLearningDao.countMassedLearning(query);
		queryData.setTotalCount(countAllInfo);
		if (countAllInfo <= 0) {
			return queryData;
		}

		if (commonQuery.getRecordPerPage() <= 0) {
			commonQuery.setRecordPerPage(10);
		}
		query.setMaxCount(commonQuery.getRecordPerPage());
		int totalPage = QueryData.computeTotalPage(countAllInfo,
				commonQuery.getRecordPerPage());
		queryData.setTotalPage(totalPage);

		List<PageData> pageDataList = Lists.newArrayList();
		// 未指定页数，则只读取前三页数据
		if (commonQuery.getPageArray() == null) {
			commonQuery.setPageArray(new int[] { 1, 2, 3 });
		}
		query.fill("maxCount", commonQuery.getRecordPerPage());
		for (int index = 0; index < commonQuery.getPageArray().length; index++) {
			int indexPage = commonQuery.getPageArray()[index];
			if (indexPage > totalPage || indexPage <= 0) {
				continue;
			}
			query.fill(
					"startIndex",
					QueryData.computeStartIndex(indexPage,
							commonQuery.getRecordPerPage()));
			List<MassedLearningModel> massedLearningModels = massedLearningDao
					.findMassedLearningInfoByQuery(query);

			// 查出每门集中学习已填加的学生数
			massedLearningModels = countStudenFromStuMale(massedLearningModels);
			pageDataList.add(new PageData(indexPage,
					formatDate(massedLearningModels)));

		}
		queryData.setPageData(pageDataList);
		return queryData;
	}

	/**
	 * 查出每门集中学习的已填加的学生数
	 * 
	 * @param list
	 * @return
	 */
	private List<MassedLearningModel> countStudenFromStuMale(
			List<MassedLearningModel> list) {
		for (MassedLearningModel m : list) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("maleId", m.getMaleId());
			m.setMaleActualStuNum(massedLearningDao
					.countStudeNumFromStudentMale(data));
		}

		return list;
	}

	private List<MassedLearningModel> formatDate(List<MassedLearningModel> list) {
		for (MassedLearningModel model : list) {
			if (model.getMaleCreateTime() != null) {
				model.setMaleCreateDate(FormatUtil.formatDate(
						model.getMaleCreateTime(), "yyyy-MM-dd"));
			}
			if (model.getMaleStartDate() != null) {
				model.setMaleStartTime(FormatUtil.formatDate(
						model.getMaleStartDate(), "yyyy-MM-dd HH:mm"));
			}
			if (model.getMaleEndDate() != null) {
				model.setMaleEndTime(FormatUtil.formatDate(
						model.getMaleEndDate(), "yyyy-MM-dd HH:mm"));
			}
		}
		return list;
	}

	/**
	 * 添加集中学习记录
	 * 
	 * @param mlModel
	 * @return 返回值中的status中<br>
	 *         1代表此集中学习的计划时间在当前时间之前，<br>
	 *         2表示集中学习添加失败 <br>
	 *         3表示集中学习添加成功
	 * @author lujoCom
	 */
	public Map<String, Object> addMassedLearningInfo(MassedLearningModel mlModel) {

		Map<String, Object> returnData = new HashMap<String, Object>();

		if (mlModel.getMaleStartDate().getTime() < System.currentTimeMillis()) {
			returnData.put("status", 1);
			returnData.put("message", "请将集中学习的计划时间置于当前时间之后");
			return returnData;
		}
		// 封装将要创建的集中学习基本信息
		MassedLearning maLearning = new MassedLearning();
		maLearning.setMaleName(mlModel.getMaleName());
		maLearning.setMaleCreatetime(new Date());
		maLearning.setMaleStarttime(mlModel.getMaleStartDate());
		maLearning.setMaleResoId(mlModel.getMaleResouseId());
		maLearning.setMaleTeacherid(mlModel.getMaleTeacherId());
		maLearning.setMaleSchoolid(mlModel.getMaleSchoolId());
		maLearning.setMaleCourId(mlModel.getMaleCourseId());
		maLearning.setMaleState(0);
		maLearning.setMaleDescribe(mlModel.getMaleDescribe());

		// 根据集中学习的持续时间长度，算出集中学习的截止时间
		Date endDate = new Date(mlModel.getMaleStartDate().getTime()
				+ mlModel.getMaleDuration() * 60000);
		maLearning.setMaleEndTime(endDate);

		// 将集中学习基本信息保存到数据库中并返回此集中学习的id
		Integer id = massedLearningDao.createNewMassedLearning(maLearning);
		if (id <= 0) {
			returnData.put("status", 2);
			returnData.put("message", "添加集中学习课程失败");
			return returnData;
		}
		returnData.put("status", 3);
		returnData.put("maleId", id);
		returnData.put("message", "添加集中学习课程成功");

		return returnData;
	}

	/**
	 * 从集中学习中删除学生
	 * 
	 * @param deleStuIds
	 * @param smleMaleId
	 * @return
	 * @author lujoCom
	 */
	public Integer deleteStudentFromMassedLearning(String deleStuIds,
			Integer smleMaleId) {
		if (deleStuIds == null || deleStuIds.equals("")) {
			return NODATA;
		}
		String[] deleStuId = deleStuIds.split(",");
		Map<String, Object> queryData = new HashMap<String, Object>();
		queryData.put("deleStuId", deleStuId);
		queryData.put("smleMaleId", smleMaleId);
		try {
			massedLearningDao.deleteStudentFromMassedLearning(queryData);
		} catch (Exception e) {
			return FAILED;
		}
		return SUCCESS;
	}

	/**
	 * 向某个集中学习课程添加学生
	 * 
	 * @param addStuIds
	 * @param smleMaleId
	 * @return
	 */
	public Map<String, Object> addStudentToMassedLearning(String addStuIds,
			Integer smleMaleId) {
		Map<String, Object> data = new HashMap<String, Object>();
		// 没有操作数据，同样表示操作成功将数据返回
		if (addStuIds == null || addStuIds.equals("")) {
			data.put("message", "调整成功");
			return data;
		}
		String[] addStuId = addStuIds.split(",");
		List<StudentMassedLearning> list = new ArrayList<StudentMassedLearning>();
		try {
			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put("maleId", smleMaleId);
			// 查询此集中学习是否存在
			List<MassedLearningModel> mLearningModels = massedLearningDao
					.findMassedLearningInfoByQuery(queryMap);
			if (mLearningModels.size() == 0) {
				data.put("message", "无此集中选课，请选择集中选课项");
				return data;
			}
			// 遍历学生id数组，如果此学生已经参与集中学习则跳过进入下一个循环，否则添加到将要添加的list中
			for (String s : addStuId) {
				StudentMassedLearning sml = new StudentMassedLearning();
				// 如果此学生的id不为全数字格式跳出此循环，进入下一个循环
				if (!s.matches("^[1-9]+[0-9]*$")) {
					continue;
				}
				sml.setSmleUserId(Integer.parseInt(s));
				sml.setSmleMaleId(smleMaleId);
				// 如果该学生已经在此集中学习课程时，跳过此循环进入下一个循环
				if (massedLearningDao.findStudentMassedLearning(sml).size() != 0) {
					continue;
				}
				list.add(sml);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			data.put("message", "数据格式有误");
			return data;
		}
		Integer row = massedLearningDao.addStudentToMassedLearning(list);
		if (row <= 0) {
			data.put("message", "添加失败");
			return data;
		}
		data.put("message", "调整成功");
		return data;
	}

	/**
	 * 开始学习时，记录学习时间
	 * 
	 * @param maleId
	 * @param studyTime
	 * @param courseId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public Map<String, Object> startMassedLearning(Integer maleId,
			Integer studyTime, Integer courseId) throws Exception {
		Map<String, Object> returnData = new HashMap<String, Object>();
		Map<String, Object> updateData = new HashMap<String, Object>();
		StudentMassedLearning sml = new StudentMassedLearning();
		sml.setSmleMaleId(maleId);
		List<StudentMassedLearningInfoModel> studentInfos = massedLearningDao
				.findStudentMassedLearning(sml);
		if (studentInfos.size() == 0) {
			updateData.put("maleId", maleId);
			updateData.put("state", 1);
			returnData.put("startMassedLearningInfo", "此集中学习无学生，无法记录成绩");
			massedLearningDao.updateMassedLearningState(updateData);
			return returnData;
		}

		Integer[] stuIds = new Integer[studentInfos.size()];
		int index = 0;
		for (StudentMassedLearningInfoModel s : studentInfos) {
			stuIds[index++] = s.getStuId();
		}
		Integer row = null;

		if (index > 0) {
			updateData.put("rscoCourId", courseId);
			updateData.put("rscoUserId", stuIds);
			updateData.put("studyTime", studyTime);

			row = reSelectCourseDao.updateStudentsMassLearningScore(updateData);
		}
		if (row <= 0) {
			returnData.put("startMassedLearningInfo", "开始学习失败，请稍后再试");
			return returnData;
		}
		updateData.put("maleId", maleId);
		updateData.put("state", 1);
		massedLearningDao.updateMassedLearningState(updateData);
		returnData.put("startMassedLearningInfo", "开始学习成功..");

		// 更新学生总成绩
		for (int i = 0; i < stuIds.length; i++)
			selectCourseService.updateCourseTotalScore(stuIds[i], courseId);

		return returnData;
	}

	/**
	 * 批量删除集中学习
	 * 
	 * @author ZhangXin
	 * @param mlIds
	 * @return 1 标识删除失败，2标识删除成功
	 */
	@Transactional
	public int deleteMl(List<Integer> li,String mlIds) {

		massedLearningDao.deleteMlStudent(mlIds);
		int affect = massedLearningDao.deleteMl(li);
		if (affect == 0)
			return 1;
		else
			return 2;
	}
	
}
