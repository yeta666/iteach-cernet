package com.swust.kelab.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.swust.kelab.domain.Attachment;
import com.swust.kelab.domain.BbsPost;
import com.swust.kelab.domain.Course;
import com.swust.kelab.domain.Department;
import com.swust.kelab.domain.User;
import com.swust.kelab.model.BBSReplyModel;
import com.swust.kelab.model.BbsPostModel;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.PageData;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.repos.AttachmentDAO;
import com.swust.kelab.repos.BbsPostDAO;
import com.swust.kelab.repos.BbsReplyDAO;
import com.swust.kelab.repos.CourseDAO;
import com.swust.kelab.repos.DepartmentDao;
import com.swust.kelab.repos.ReSelectCourseDAO;
import com.swust.kelab.repos.ReUserRoleDAO;
import com.swust.kelab.repos.UserDAO;
import com.swust.kelab.repos.bean.ListQuery;
import com.swust.kelab.utils.FormatUtil;

/**
 * 处理bbs主贴的service
 * 
 * @author 吴岘辉
 * 
 */
@Service
public class BbsPostService {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BbsPostDAO bbsPostDAO;

    @Autowired
    private BbsReplyDAO bbsReplyDAO;

    @Autowired
    private ReUserRoleDAO reUserRoleDAO;

    @Autowired
    private DepartmentDao departmentDAO;

    @Autowired
    private CourseService courseService;

    @Autowired
    private AttachmentDAO attachmentDAO;

    @Autowired
    private ReSelectCourseDAO reSelectCourseDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private SelectCourseService selectCourseService;

    @Autowired
    private CourseDAO courseDAO;
    /**
     * 查看主贴列表
     * 
     * @param query
     *            查询对象，主要是分页
     * @param departId
     *            查询的部门id,只针对教务员
     * @param userId
     *            用户id
     * @param courseId
     *            课程id
     * @return 主贴列表
     */
    public QueryData viewBbsPostList(CommonQuery query, int departId,
            int userId, int courseId, int queryType) {
        QueryData result = new QueryData();
        ListQuery myQuery = query.format();
        if (courseId > 0) {
            myQuery.put("courseId", courseId);
        }
        // 根据角色，分别构造查询条件
        if (departId > 0) {// 论坛管理：（教务员或者管理员）
            // 判断是市级还是校级
            Department depart = null;
            try {
                depart = departmentDAO.queryDepartmentById(departId);
            } catch (Exception e) {
                logger.error("queryDepartType error!\n"
                        + e.getLocalizedMessage());
                result = null;
                return result;
            }
            int departType = -1;
            if (depart != null) {
                departType = depart.getDepaType();
            }
            // 构造查询条件
            if (departType == 3) {// 校级
                myQuery.put("departId", departId);
            }else{
                myQuery.put("departId", -9);
            }
        } else {// 教师、学生
            myQuery.put("departId", -7);
            if (departId == -2 || queryType >= 2) {// 我的发帖和我的回复,或者论坛管理（学生）
                myQuery.put("userId", userId);
                // 获取其选择的课程（退课的不算）
                List<Integer> courseIds = new ArrayList<Integer>();
                List<Map> courses = courseService
                        .viewCoursesByUserId(userId, 1);
                if (courses == null || courses.size() <= 0) {
                    return result;
                }
                for (Map map : courses) {
                    courseIds.add((Integer) map.get("courseId"));
                }
                myQuery.put("courseIds", courseIds);
            } else if (courseId <= 0) {// 未指定课程（老师：我的讨论和论坛管理）
                // 获取相应的课程
                List<Integer> courseIds = new ArrayList<Integer>();
                List<Map> courses = courseService
                        .viewCoursesByUserId(userId, 1);
                if (courses == null || courses.size() <= 0) {
                    return result;
                }
                for (Map map : courses) {
                    courseIds.add((Integer) map.get("courseId"));
                }
                myQuery.put("courseIds", courseIds);
            }
        }

        // 统计总数
        int totalNum = 0;
        try {
            totalNum = bbsPostDAO.countPostNum(myQuery, queryType);
        } catch (Exception e) {
            logger.error("viewSelectedCourses error!\n"
                    + e.getLocalizedMessage());
        }
        //System.out.println("totalNum:" + totalNum);
        if (totalNum == 0) {
            return result;
        }
        //统计置顶的帖子数
        int totalTopNum=0;
        try {
            totalTopNum = bbsPostDAO.countTopNum(myQuery, queryType);
        } catch (Exception e) {
            logger.error("viewSelectedCourses error!\n"
                    + e.getLocalizedMessage());
        }
        
        // 按照分页，查询具体的帖子
        // 默认分页信息
        if (query.getRecordPerPage() <= 0) {
            query.setRecordPerPage(10);
        }
        if (query.getPageArray() == null) {
            query.setPageArray(new int[] { 1, 2, 3 });
        }
        result.setTotalCount(totalNum);
        int totalPage = QueryData.computeTotalPage(totalNum,
                query.getRecordPerPage());
        result.setTotalPage(totalPage);
        int startIndex = (query.getPageArray()[0] - 1)
                * query.getRecordPerPage();
        int fetchSize = query.getPageArray().length * query.getRecordPerPage();        
        
        List<BbsPostModel> posts = new ArrayList<BbsPostModel>();
        //先处理置顶的帖子
        int topNum=0;
        if(totalTopNum>0&&startIndex<totalTopNum){
            int end=totalTopNum-startIndex;            
            int begin=end-fetchSize;
            int fetchTop=fetchSize;
            if(begin<0){
                begin=0;
                fetchTop=end;
            }
            myQuery.fill("startIndex", begin);
            myQuery.fill("maxCount", fetchTop);
            try {
                List<BbsPostModel> topPosts =null;
                if(queryType==3){
                    topPosts=bbsPostDAO.viewBbsPostList(myQuery, 3);
                }else{
                    topPosts=bbsPostDAO.viewBbsPostList(myQuery, 1);
                }
                if(topPosts!=null&&!topPosts.isEmpty()){
                    for (int i = topPosts.size()-1; i >=0; i--) {
                        posts.add(topPosts.get(i));
                    }
                    topNum=topPosts.size();
                }
            } catch (Exception e) {
                logger.error("viewTopBbsPostList error!\n" + e.getLocalizedMessage());
            }
        }
        
        //查询非置顶的帖子
        if(topNum<fetchSize&&totalNum-totalTopNum>0){
            try {
                int end=totalNum-totalTopNum;
                int fetchNotTop=fetchSize-topNum;
                if(topNum<=0){
                    end-=startIndex;                    
                }
                if(end<=0){
                    return result;
                }
                int begin=end-fetchNotTop;                
                if(begin<0){
                    begin=0;
                    fetchNotTop=end;
                }
                myQuery.fill("startIndex", begin);
                myQuery.fill("maxCount", fetchNotTop);                             
                List<BbsPostModel> curPosts  =null;
                if(queryType==3){
                    curPosts=bbsPostDAO.viewBbsPostList(myQuery, 4);
                }else{
                    curPosts=bbsPostDAO.viewBbsPostList(myQuery, 2);
                }
                if(curPosts!=null&&!curPosts.isEmpty()){
                    for (int i = curPosts.size()-1; i >=0; i--) {
                        posts.add(curPosts.get(i));
                    }
                }
            } catch (Exception e) {
                logger.error("viewBbsPostList error!\n" + e.getLocalizedMessage());
            }
        }

        // 封装主贴列表
        int endIndex;
        if (posts != null && posts.size() > 0) {
            // 先转换时间格式
            for (BbsPostModel bpm : posts) {
                if (bpm.getBbpoTime() != null) {
                    bpm.setRealTime(FormatUtil.formatDate(bpm.getBbpoTime()));
                }
                if (bpm.getBbpoUpdatetime() != null) {
                    bpm.setRealUpdatetime(FormatUtil.formatDate(bpm
                            .getBbpoUpdatetime()));
                } else if (bpm.getBbpoTime() != null) {
                    bpm.setRealUpdatetime(FormatUtil.formatDate(bpm
                            .getBbpoTime()));
                }
            }
            // 封装数据
            List<PageData> pageDataList = Lists.newArrayList();
            for (int k = 0; k < query.getPageArray().length; k++) {
                int page = query.getPageArray()[k];
                if (page <= 0 || page > totalPage) {
                    continue;
                }
                startIndex = k * query.getRecordPerPage();
                endIndex = startIndex + query.getRecordPerPage();
                if (startIndex >= posts.size()) {
                    continue;
                }
                if (endIndex > posts.size()) {
                    endIndex = posts.size();
                }
                List<BbsPostModel> pageDatas = posts.subList(startIndex,
                        endIndex);
                pageDataList.add(new PageData(page, pageDatas));
            }
            result.setPageData(pageDataList);
        }
        return result;
    }

    /**
     * 查询主贴的详细信息
     * 
     * @param bbsPostId
     *            主贴id
     * @return 包含主贴详细信息的map
     * @see BbsPostModel
     */
    public Map viewBbsPostDetail(int bbsPostId) {
        Map result = new HashMap();
        // 基本信息
        BbsPostModel post = null;
        try {
            post = bbsPostDAO.viewBbsPostDetail(bbsPostId);
        } catch (Exception e) {
            logger.error("viewBbsPostDetail error!\n" + e.getLocalizedMessage());
        }
        if (post == null) {
            result = null;
            return result;
        }
        result.put("postId", post.getBbpoId());
        result.put("postTitle", post.getBbpoTitle());
        result.put("postContent", post.getBbpoContent());
        result.put("courseId", post.getCourseId());
        result.put("courseName", post.getCourseName());
        result.put("userName", post.getUserName());
        result.put("pubTime", FormatUtil.formatDate(post.getBbpoTime()));
        if (post.getBbpoUpdatetime() == null) {
            result.put("updateTime", FormatUtil.formatDate(post.getBbpoTime()));
        } else {
            result.put("updateTime",
                    FormatUtil.formatDate(post.getBbpoUpdatetime()));
        }
        result.put("visitNum", post.getBbpoVisitnum());
        result.put("replyNum", post.getBbpoReplynum());
        result.put("isBest", post.isBbpoIsbest());
        result.put("isTop", post.isBbpoIstop());

        // 附件信息
        if (post.isBbpoHasattach()) {
            Map query = new HashMap();
            query.put("sourceId", bbsPostId);
            query.put("sourceType", 1);
            List<Attachment> attas = null;
            try {
                attas = attachmentDAO.getAttachBySourceTypeId(query);
            } catch (Exception e) {
                logger.error("getAttachBySourceTypeId error!\n"
                        + e.getLocalizedMessage());
            }
            if (attas != null && attas.size() > 0) {
                result.put("attachs", attas);
            }
        }
        return result;
    }

    /**
     * 更新主贴的状态，比如访问数、回复数、最近更新时间等
     * 
     * @param bbsPostId
     *            主贴id
     * @param updateType
     *            1表示增加访问数，2表示增加回复数同时重新设置最近更新时间 3表示减少回复数
     * @return
     */
    public String updateBbsPost(int bbsPostId, int updateType) {
        String result = "fail";
        if (updateType == 1) {
            // 增加访问数
            int change = 0;
            try {
                change = bbsPostDAO.addVisitNum(bbsPostId);
                ;
            } catch (Exception e) {
                logger.error("addVisitNum error!\n" + e.getLocalizedMessage());
            }
            if (change == 1) {
                result = "success";
            }
        } else if (updateType == 2) {
            // 增加回复数同时重新设置最近更新时间
            Map query = new HashMap();
            query.put("bbsPostId", bbsPostId);
            query.put("newTime", new Date());
            int change = 0;
            try {
                change = bbsPostDAO.addReplyNum(query);
            } catch (Exception e) {
                logger.error("addReplyNum error!\n" + e.getLocalizedMessage());
            }
            if (change == 1) {
                result = "success";
            }
        } else if (updateType == 3) {
            // 减少回复数
            int change = 0;
            try {
                change = bbsPostDAO.reduceReplyNum(bbsPostId);
            } catch (Exception e) {
                logger.error("reduceReplyNum error!\n"
                        + e.getLocalizedMessage());
            }
            if (change == 1) {
                result = "success";
            }
        }
        return result;
    }

    /**
     * 创建主贴
     * 
     * @param bbsPost
     *            主贴的详细内容
     * @param attaches
     *            所有附件id，多个用逗号隔开
     * @return
     */
    public String createBbsPost(BbsPost bbsPost, String attaches) {
        String result = "fail";
        // 判断是否有附件
        if (attaches != null && !attaches.isEmpty()
                && attaches.matches("\\d+(,\\d+)*")) {
            bbsPost.setBbpoHasattach(true);
        } else {
            bbsPost.setBbpoHasattach(false);
        }
        //发帖人信息
        User user = userDAO.findOneUser(bbsPost.getBbpoUserId());
        bbsPost.setBbpoUserName(user.getUserRealname());
        bbsPost.setBbpoUserDepartId(user.getUserDepaId());
        //课程信息
        Course cm=courseDAO.
                findCourseByid(bbsPost.getBbpoCourId());
        bbsPost.setBbpoCourName(cm.getCourName());
        // 存储主贴
        int postId = 0;
        try {
            int change = bbsPostDAO.createBbsPost(bbsPost);
            if (change > 0) {
                postId = bbsPost.getBbpoId();
            }
        } catch (Exception e) {
            logger.error("createBbsPost error!\n" + e.getLocalizedMessage());
        }
        if (postId <= 0) {
            return result;
        }
        // 设置附件的原id
        if (bbsPost.getBbpoHasattach()) {
            String[] idStr = attaches.split(",");
            for (String str : idStr) {
                int attachId = Integer.parseInt(str);
                Map query = new HashMap();
                query.put("attachId", attachId);
                query.put("sourceId", postId);
                int change = 0;
                try {
                    change = attachmentDAO.changeAttachSourceId(query);
                } catch (Exception e) {
                    logger.error("changeAttachSourceId error!\n"
                            + e.getLocalizedMessage());
                }
                if (change <= 0) {
                    logger.error("changeAttachSourceId failed!");
                }
            }
        }
        // 若发帖人为学生，则要增加其bbs讨论数      
        if (user != null && user.getUserType() == 1) {
            Map query = new HashMap();
            query.put("stuId", bbsPost.getBbpoUserId());
            query.put("courseId", bbsPost.getBbpoCourId());
            try {
                reSelectCourseDAO.addBbsDiscussNum(query);
                // 更新总成绩
                selectCourseService.updateCourseTotalScore(
                        bbsPost.getBbpoUserId(), bbsPost.getBbpoCourId());
            } catch (Exception e) {
                logger.error("addBbsDiscussNum error!\n"
                        + e.getLocalizedMessage());
            }
        }
        result = "success";
        return result;
    }

    /**
     * 置顶某个帖子
     * 
     * @author pangjt
     * @param postId
     *            帖子id
     * @param setValue
     *            设置的值，1表示设置 2表示取消
     * @return 返回执行状态
     */
    public String setPostTop(int postId, int setValue) {
        String result = "success";
        try {
            Map<String, Object> query = new HashMap<String, Object>();
            query.put("bbsPostId", postId);
            query.put("topValue", false);
            if (setValue == 1) {
                query.put("topValue", true);
            }
            bbsPostDAO.setPostTop(query);

        } catch (Exception e) {
            logger.error("setPostTop error!\n" + e.getLocalizedMessage());
            return "fail";
        }
        return result;
    }

    /**
     * 置某个帖子为精华贴
     * 
     * @author pangjt
     * @param postId
     *            帖子id
     * @return 返回执行状态
     */
    public String setPostEssence(int postId, int setValue) {
        String result = "success";
        try {
            Map<String, Object> query = new HashMap<String, Object>();
            query.put("bbsPostId", postId);
            query.put("bestValue", false);
            if (setValue == 1) {
                query.put("bestValue", true);
            }
            bbsPostDAO.setPostEssence(query);
        } catch (Exception e) {
            logger.error("setPostEssence error!\n" + e.getLocalizedMessage());
            result = "fail";
        }
        return result;
    }

    /**
     * 删除一个帖子的所有相关信息，包括附件文件，附件记录
     * <p>
     * 还包括帖子的所有回复，主体贴
     * 
     * @author pery
     * @param bbsPostId
     *            该帖子的id
     * @param basePath
     *            删附件使用的绝对路径的前缀路径
     * @return 删除状态 succss ?fail
     */
    public String deleteOnePost(int bbsPostId, String basePath) {
        // 查询所有的回复列表的附件
        Map<String, Integer> query = new HashMap<String, Integer>();
        query.put("sourceId", bbsPostId);
        query.put("sourceType", 2); // 回帖
        try { // 回帖附件
            attachmentDAO.deleteBatchAttachmentAndRecored(query, basePath);
            query.put("sourceType", 1);// 主贴
            attachmentDAO.deleteBatchAttachmentAndRecored(query, basePath);

            // 减少相关学生的发帖数
            Map<Integer, Integer> userBbsNum = new HashMap<Integer, Integer>();
            // 获取主贴
            BbsPostModel bbsPost = null;
            try {
                bbsPost = bbsPostDAO.viewBbsPostDetail(bbsPostId);
            } catch (Exception e) {
                logger.error("viewBbsPostDetail error!\n"
                        + e.getLocalizedMessage());
            }
            if (bbsPost == null) {
                logger.error("主贴不存在，可能已被其他用户删除！");
                return "fail";
            }
            userBbsNum.put(bbsPost.getUserId(), 1);
            // 获取所有回帖
            List<BBSReplyModel> replys = null;
            try {
                Map temp = new HashMap();
                temp.put("bbsPostId", bbsPostId);
                replys = bbsReplyDAO.viewReplyList(temp);
            } catch (Exception e) {
                logger.error("viewReplyList error!\n" + e.getLocalizedMessage());
            }
            if (replys != null && replys.size() > 0) {
                for (BBSReplyModel brm : replys) {
                    if (userBbsNum.containsKey(brm.getUserId())) {
                        userBbsNum.put(brm.getUserId(),
                                userBbsNum.get(brm.getUserId()) + 1);
                    } else {
                        userBbsNum.put(brm.getUserId(), 1);
                    }
                }
            }
            // 更新论坛讨论数
            for (Integer key : userBbsNum.keySet()) {
                Map temp = new HashMap();
                temp.put("stuId", key);
                temp.put("courseId", bbsPost.getCourseId());
                temp.put("reduceNum", userBbsNum.get(key));
                try {
                    reSelectCourseDAO.reduceBbsDiscussNum(temp);
                } catch (Exception e) {
                    logger.error("reduceBbsDiscussNum error!\n"
                            + e.getLocalizedMessage());
                }
                // 更新总成绩
                try {
                    selectCourseService.updateCourseTotalScore(key,
                            bbsPost.getCourseId());
                } catch (Exception e) {
                    logger.error("updateCourseTotalScore error!\n"
                            + e.getLocalizedMessage());
                }
            }

            // 删除回帖
            int replyResult = bbsReplyDAO.deleteReplyOfPost(bbsPostId); // 删除回复
            // 删除主贴
            int postResult = bbsPostDAO.deleteOnePost(bbsPostId); // 删除主贴

            return (replyResult > -1 && postResult > -1) ? "success" : "fail";
        } catch (Exception e) {
            logger.error("setPostEssence error!\n" + e.getLocalizedMessage());
            return "fail";
        }
    }
}
