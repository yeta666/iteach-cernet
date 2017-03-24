package com.swust.kelab.recom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.swust.kelab.domain.Course;
import com.swust.kelab.model.CourseModel;
import com.swust.kelab.model.UserModel;
import com.swust.kelab.service.CourseService;

@Service
public class RecomService {
	@Autowired
	private RecomDao recomDao;
	
	@Autowired
	private CourseService courseService;

	private int featureCount=4;
	
	private double regularizer=0.001;
	
	private int maxIt = 200;
	//推荐个数
	private int topK = 10;
	
	@Scheduled(cron ="0 0 4 * * ?")//每天凌晨4点开始进行推荐
	public void recomScheduled(){
		System.out.println("开始推荐更新");
		final PMFModelTrain recom = new PMFModelTrain(recomDao, featureCount, regularizer, maxIt);
		recom.bulidModel();
		if(recom.isTrainFinish){
			int totalUserPage = recom.userCountPage;
			int pageSize = recom.pageSize;
			final Date now = new Date();
			ExecutorService pool = Executors.newFixedThreadPool(10);
			final RecomDao db = recomDao;
			for (int page = 1; page <=totalUserPage; page++) {
				List<Integer> users = recomDao.getUsersByBatch(page, pageSize);
				for (int i = 0; i < users.size();i++) {
					final int userId = users.get(i);
					pool.submit(new Runnable() {
						@Override
						public void run() {
							try {
								String recomItems = recom.recomItemIds(userId, topK);
								//优化Tips：如果用户量比较多的话。这里可以批量写入推荐结果，注意多线程同步
								db.replaceIntoUserRecom(userId, recomItems,now);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
			 // 等待所有任务执行完成后关闭启动线程
			pool.shutdown();
	        // 等待子线程结束，再继续执行下面的代码
			try {
				pool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			 System.out.println("all thread complete");
		}
	}
	
	public UserRecom getUserRecom(int userId){
		return recomDao.getUserRecom(userId);
	}
	
	public List<CourseModel> getUserRecomCourse(int userId){
		UserRecom u = this.getUserRecom(userId);
		if(u==null){
			return Lists.newArrayList();
		}
		
		String recomStr = u.getRecom();
		if(StringUtils.isBlank(recomStr)){
			return Lists.newArrayList();
		}
		String [] idsStr = recomStr.split(",");
		List<Integer> ids = Lists.newArrayList();
		for (int i = 0; i < idsStr.length; i++) {
			ids.add(Integer.parseInt(idsStr[i]));
		}
		return courseService.getCourseByIds(ids);
	}
	public int insertUserRate(int userId,int courseId,double rate){
		return recomDao.insertUserRate(userId,courseId,rate);
	}

	public List<CourseModel> getIndexCourseRecom() {
		// 取选课人数最多的几个课程
		List<Integer> courseList = recomDao.getTopKSelectCourse(topK);
		if (courseList.size() < topK) {
			Set<Integer> set = new HashSet<Integer>(courseList);
			// 从前两千个随机产生
			List<Integer> courseIdList = recomDao.getCoursesByBatch(1, 2000);
			Random rd = new Random();
			while (set.size() < topK && courseIdList.size() > 0 && courseIdList.size() > topK) {
				set.add(courseIdList.get(rd.nextInt(courseIdList.size())));
			}
			courseList = new ArrayList<Integer>(set);
		}
		return courseService.getCourseByIds(courseList);
	}
	
	public void randomUserRate() {
		final Date now = new Date();
		final List<UserRate> rateList = Lists.newArrayListWithCapacity(200);
		List<Integer> courseList = recomDao.getCoursesByBatch(1, 1000);
		
		Random rc = new Random();
		for (int page = 1; page <=50; page++) {
			System.out.println("第"+page+"页");
			List<Integer> users = recomDao.getUsersByBatch(page, 1000);
			for (int i = 0; i < users.size();i++) {
				if(rateList.size()>=200){
					recomDao.insertUserRateByBatch(rateList);
					rateList.clear();
				}
				int course = courseList.get(rc.nextInt(courseList.size()));
				double rate = rc.nextInt(5)+1; 
				rateList.add(new UserRate(users.get(i),course,rate,now));
			}
		}
		if(!rateList.isEmpty()){
			recomDao.insertUserRateByBatch(rateList);
		}
	}
}
