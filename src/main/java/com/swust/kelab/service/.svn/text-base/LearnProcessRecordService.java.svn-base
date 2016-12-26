package com.swust.kelab.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swust.kelab.domain.Chapter;
import com.swust.kelab.domain.LearnProcessRecord;
import com.swust.kelab.domain.ReSelectCourse;
import com.swust.kelab.domain.Resource;
import com.swust.kelab.domain.SystemParameter;
import com.swust.kelab.repos.ChapterDAO;
import com.swust.kelab.repos.LearnProcessRecordDAO;
import com.swust.kelab.repos.ReSelectCourseDAO;
import com.swust.kelab.repos.ResourceDAO;
import com.swust.kelab.repos.SystemParameterDAO;

/**
 * 
 * 学习进度记录Service
 * 
 * @author EasonLian
 */
@Service()
public class LearnProcessRecordService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private LearnProcessRecordDAO learnProcessRecordDAO;

	@Autowired
	private ReSelectCourseDAO rscoDAO;

	@Autowired
	private ChapterDAO chapterDAO;
	
	@Autowired
    private ResourceDAO resourceDAO;
	
	@Autowired
	private SystemParameterDAO systemParameterDAO;
	
	@Autowired
	private SelectCourseService selectCourseService;

	/**
	 * 更新视频学习时间记录的结束时间<br>
	 * 更新休息次数、学习时间到 选课表<br>
	 * 
	 * @param lpreId
	 * @param userId
	 * @param chapId
	 * @param studyTime
	 * @return 更新状态
	 * @author easonlian
	 */
	@Transactional
	public int recordLearnProcess(Integer lpreId, Integer userId,
			Integer chapId, int studyTime,Integer resoId) {
		try {
			if (lpreId != null)
				// 更新学习时间
				learnProcessRecordDAO.recordLearnProcess(lpreId, userId,
						chapId, studyTime);
			/*
			 * 通过章节找到所属的课程，并查询课程对应‘评分标准 EvaluateMethod’
			 * 通过评分标准，按照学习时间，更新该课程的学习次数成绩、学习时间成绩
			 */
			int courId = chapterDAO.selectCourIdByChapId(chapId);
			ReSelectCourse rsco = rscoDAO.selectOneByUserIdAndUserId(courId,
					userId);
			// 登陆学习次数+1
			rsco.setRscoLoginscore(rsco.getRscoLoginscore() + 1);
			// 学习时间累加 
			rsco.setRscoLearntimescore(rsco.getRscoLearntimescore()
					+ studyTime) ;
			// 更新到Re_SelectCource表
			rscoDAO.updateOneLoginAndLearningtimeScore(rsco);
			updateStudentTotolScore(userId, courId);
			return 1;
		} catch (Exception e) {
			logger.error("recordLearnProcess error!\n"
					+ e.getLocalizedMessage());
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 添加视频学习时间记录<br>
	 * 
	 * @param userId
	 *            用户id
	 * @param chapId
	 *            章节id
	 * @return 添加的lpreId
	 * @author easonlian
	 */
	@Transactional
	public int insertLearnProcessRecord(Integer userId, Integer chapId,
			Integer resoId) {
		try {
//			if (learnProcessRecordDAO.getLearnProcessCountByResoId(userId,
//					chapId, resoId) == 0) {
				LearnProcessRecord lpr = learnProcessRecordDAO
						.insertLearnProcessRecord(userId, chapId, resoId, "");
				Chapter c=chapterDAO.findOneChapter(chapId);
				int courId=c.getChapCourId();
				ReSelectCourse rsco = rscoDAO.selectOneByUserIdAndUserId(courId,
	                    userId);
	            // 登陆学习次数+1
	            rsco.setRscoLoginscore(rsco.getRscoLoginscore() + 1);
	            //判断是否在播放时就计算上整个视频的时间
	            SystemParameter sp=systemParameterDAO
	                    .viewOneParameterByEnName("videoInterval");
	            if(sp!=null&&sp.getSypaId()>0){
	                int videoInterval=Integer.parseInt(
	                        sp.getSypaValue());
	                if(videoInterval<=0){
	                    int videoTime=0;
	                    Resource r=resourceDAO.selectOneById(resoId, null);
	                    videoTime=r.getResoVediotime();
	                    rsco.setRscoLearntimescore(rsco.getRscoLearntimescore()
	                            + (videoTime / 60));
	                }
	            }
	            
	            rscoDAO.updateOneLoginAndLearningtimeScore(rsco);
				updateStudentTotolScore(userId,courId);

				return lpr.getLpreId();
//			} else {
//				return -1; // -1代表该人该课程该视频已经记录过了
//			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("insertLearnProcessRecord\n" + e.getLocalizedMessage());
			return 0;
		}
	}
	
	private void updateStudentTotolScore(Integer userId,Integer courId) {
		// 更新该学生该课程的总成绩
		selectCourseService.updateCourseTotalScore(userId, courId);
	}
}
