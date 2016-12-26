-- MySQL dump 10.13  Distrib 5.5.47, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: iteach_cernet
-- ------------------------------------------------------
-- Server version	5.5.47-0ubuntu0.14.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Action`
--

DROP TABLE IF EXISTS `Action`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Action` (
  `acti_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `acti_name` varchar(255) NOT NULL COMMENT '名称',
  `acti_describe` text COMMENT '描述',
  `acti_url` varchar(255) NOT NULL COMMENT '请求路径，对应ajax中的url，或者form表单中的Action',
  `acti_type` int(11) NOT NULL COMMENT '类型：1表示公共的Action，2表示涉及权限的Action',
  PRIMARY KEY (`acti_id`)
) ENGINE=MyISAM AUTO_INCREMENT=200 DEFAULT CHARSET=gbk COMMENT='Action表，用做权限的灵活管理，一个Action代表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Action`
--

LOCK TABLES `Action` WRITE;
/*!40000 ALTER TABLE `Action` DISABLE KEYS */;
INSERT INTO `Action` VALUES (1,'修改角色','defult','sysman_Competence_edit.html',2),(2,'增加测试','课程测试管理功能','sysman_CourseTest_add.html',2),(3,'删除测试','课程测试管理功能','/train/delExin',2),(4,'编辑测试','课程测试管理功能','sysman_CourseTest_edit.html',2),(5,'增加试题','题库管理功能','sysman_Questions_add.html',2),(6,'删除试题','题库管理功能','/train/delQuestions',2),(7,'批量导入试题','题库管理功能','/train/importQuestionsInfo',2),(8,'编辑试题','题库管理功能','sysman_Questions_edit.html',2),(9,'手动组卷','试卷管理功能','sysman_Paper-manual.html',2),(10,'自动组卷','试卷管理功能','sysman_Paper-automatic.html',2),(11,'删除试卷','试卷管理功能','/train/delPapers',2),(15,'查看测试','课程测试功能','courseTest.html',2),(16,'查看成绩','我的成绩功能','/train/viewAllMyGrades',2),(17,'教师帖子管理','我的讨论模块','/bbsPost/setPostType',2),(18,'查看主贴列表','我的讨论、论坛管理','/bbsPost/viewBbsPostList',1),(19,'教师删帖','我的讨论模块','/bbsPost/deletePost',2),(20,'发主帖','发起讨论模块','bbs_newPost.html',2),(21,'查看主贴详细信息','我的讨论模块','/bbsPost/viewBbsPostDetail',1),(22,'查看主贴的回帖列表','我的讨论模块','/bbsReply/viewReplyList',1),(23,'回帖','我的讨论模块','/bbsReply/createBbsReply',2),(24,'更新主贴访问数','我的讨论模块','/bbsPost/addVisitNum',1),(25,'下载帖子附件','我的讨论等模块','/load/download',1),(26,'上传附件','我的讨论等模块','/load/upload',1),(27,'删除附件','我的讨论等模块','/attachment/delete',2),(28,'查看用户对应的课程','获取用户的课程列表','/course/viewCoursesByUser',1),(29,'查询学校对应的年级和班级','获取学校的年级和班级','/gradeClass/viewGradeClassByDepart',1),(30,'查看课程选修统计列表','课程选修统计模块','/selectCourse/viewList',2),(31,'查看考核方式列表','获取考核方式列表-考核方式管理','/EvaluateMethod/viewEvaluateMethodList.do',2),(32,'添加和修改考核方式','添加和修改考核方式-考核方式模块','/EvaluateMethod/addEvaluateMethod.do',2),(34,'删除考核方式','删除考核方式-考核方式模块','/EvaluateMethod/delEvaluateMethod.do',2),(35,'查询所有已选课程','学习中心模块','/course/viewCourseList.do',1),(36,'查看章节具体内容','学习中心模块','Learning_chooseCourse.html',2),(37,'查看章节具体学习资源','学习中心模块','learning_video.html',2),(38,'查看资源','查看资源-资源管理模块','/resource/viewResourceListForAdmin.do',1),(39,'添加资源','添加资源-资源管理模块','/resource/addResourceByAdmin.do',2),(40,'修改资源','修改资源-资源管理模块','/resource/modResourceByAdmin.do',2),(41,'删除资源','删除资源-资源管理模块','/resource/delResourceByAdmin.do',2),(42,'获取指定类型的机构列表','通过类型获取机构列表','/department/viewDepartments',1),(43,'查看章节列表','查看章节列表-教务管理模块','/chapter/viewAllChapterList.do?data=8asjlkfj281lavcl',1),(46,'添加章节','添加章节-教务管理模块','/chapter/addChapterByAdmin.do',2),(47,'修改章节','修改章节信息-教务管理模块','/chapter/addOrModChapterWithResource.do',2),(48,'删除章节','删除章节-教务管理模块','/chapter/delChapterByAdmin.do',2),(49,'查看课程领域列表','课程领域管理查看','/courseCategory/viewList',2),(50,'获取指定机构的课程','通过机构获取课程列表','/course/viewCourseListByDepart',1),(51,'查看课程论坛列表','论坛管理','/course/viewCoursesBbsList',2),(52,'开关论坛','论坛管理','/course/setCourseBbsStatus',2),(53,'查看机构列表','机构管理','/department/searchByMutiTerm',1),(54,'新增机构','机构列表','/department/addDepartment',2),(55,'删除机构','机构管理','/department/deleteDepartments',2),(56,'导入机构','机构管理','/department/importExcel',2),(57,'导出机构','机构管理','/department/exportExcel',2),(58,'查看日志','日志管理','/log/queryLogs',2),(59,'导出日志','日志管理','/log/exportLog',2),(60,'删除日志','日志管理','/log/deleteLogs',2),(61,'查看平台应用情况','平台应用统计','/courseStatistic/platform',1),(62,'查看课程选修列表','课程选修统计','/courseStatistic/staCourseLearning',2),(63,'导出学习成绩列表','课程选修统计','/selectCourse/exportCountScore',2),(64,'查看教师辅导列表','教师辅导统计','/courseStatistic/teacher',2),(67,'添加课程分类','新增课程领域','/courseCategory/create',2),(68,'修改课程分类','课程分类管理','/courseCategory/modify',2),(69,'删除课程分类','课程分类管理','/courseCategory/delete',2),(70,'删除课程','课程管理','/course/delete',2),(71,'获取课程类别','课程管理','/courseCategory/viewAllCates',1),(72,'获取考核方式','获取所有考核方式','/EvaluateMethod/viewAllEvaMethods',1),(73,'新增课程','课程管理','eduman_Courses_add.html',2),(74,'显示课程详情','课程管理-修改','/course/viewDetail',2),(75,'修改课程','课程管理-修改','eduman_Course_Modify.html',2),(76,'更新课程成绩','考核方式变化后，更新课程的选课成绩','/course/updateScore',2),(77,'课程列表显示','课程管理','/course/courseList',2),(78,'课程审批列表','课程审批','/course/queryCourseOfCheck',2),(79,'课程申请信息','课程审批','/course/queryOneCheckCourse',2),(81,'审核课程','课程审批','/course/checkCouse',2),(82,'修改服务器','修改视频服务器','/videoServer/updateServer',2),(83,'删除服务器','删除视频服务器','/videoServer/deleteServer',2),(84,'显示服务器','显示视频服务器','/videoServer/selectAllServer',2),(85,'显示系统参数','显示系统参数','/sypaController/viewAllSypa',2),(86,'更新系统参数','更新系统参数','/sypaController/updateSypa',2),(87,'教师任课列表显示','教师任课管理','/course/viewCourseAndTeahcerInfo',2),(88,'获取用户列表','教师任课管理','/user/viewUsersByTypeAndDepa',1),(89,'添加友情链接','添加友情链接','/sypaController/addLink',2),(90,'修改任课教师','教师任课管理','/course/setTeacherToCourse',2),(91,'更新友情链接','更新友情链接','/sypaController/updateLink',2),(92,'删除友情链接','删除友情链接','/sypaController/deleteLink',2),(93,'查询友情链接','显示友情链接','/sypaController/sypaController',2),(94,'查询班级','显示班级','/gradeClass/selectAllClass',2),(95,'增加班级','  增加班级','/gradeClass/insertClass',2),(96,'查询单个班级','查询单个班级','/gradeClass/selectOneClass',2),(97,'删除班级','班级删除','/gradeClass/deleteClass',2),(98,'修改班级','班级修改','/gradeClass/updateClass',2),(99,'查询成绩','成绩查询','/train/showAllScore',2),(100,'删除成绩','删除成绩','/train/deleteScore',2),(122,'查看顶部图片','查看顶部图片','/banner/viewAllBanner',1),(123,'删除顶部图片','删除顶部图片','/banner/delBanner',2),(124,'增加顶部图片','增加顶部图片','/banner/addBanner',2),(129,'老师课程展示','获取老师所教授的课程','/course/viewAllCourseByTeacher.do',1),(130,'查看最近的资源列表','获取资源列表','/resource/viewRecentResourceList.do',1),(132,'查询我的课程','获取老师授课信息','/teacher/getTeacherCourseInfo',1),(134,'添加集中学习','新增集中学习','/massedLearning/addNewMassedLearning',2),(135,'为集中学习添加学生','添加集中学习学生','/massedLearning/addStudentToOneMassedLearning',2),(136,'查询集中学习','查询当前老师所创建的集中学习','/massedLearning/selectMassedLearningInfo',2),(137,'查询可添加的学生','查询本学校可以添加到集中学习的学生','/user/finStudentByIds',1),(138,'查询课程列表','获取集中学习的课程列表','/course/viewCourseList',2),(140,'查询年级','获取某个学校的年级列表','/register/gradeInfo',1),(141,'查询班级','获取某个学校某年级的班级列表','/register/classesInfo',1),(142,'获取考核方式','考核方式列表','/course/categorysort',1),(143,'注册课程页面','注册一个新的课程','my_Tutor_registerNewCourse.html',2),(144,'获取考核方式','获取考核方式','/course/evaluateMethod',1),(152,'查询资源路径','通过课程id查询资源路径','/resource/viewVideoAddress.do',1),(153,'开始集中学习','开始集中学习','/massedLearning/startMassedLearning',1),(156,'查询课程时间列表','获取课程相应选课时间的列表','/course/courseTime',2),(158,'设置课程选课时间','设置课程选课时间','/course/setTimeOfSelectCourse',2),(159,'删除选课时间','清除课程的选课时间','/course/deleteTimeOfSelectCourse',2),(160,'查询学生列表','学生用户管理','sysman_Student.html',2),(161,'查询老师列表','老师用户管理','sysman_Teacher.html',2),(162,'查询教务员列表','教务员用户管理','sysman_Educational.html',2),(163,'查询管理员列表','管理员用户管理','sysman_Manager.html',2),(164,'添加学生用户','','sysman_Student_add.html',2),(165,'添加老师用户','','sysman_Teacher_add.html',2),(166,'添加管理员用户','','sysman_Manager_add.html',2),(167,'添加教务员用户','','sysman_Educational_add.html',2),(168,'删除学生用户','通过userId删除学生用户','/adminUserInfo/deleteUserInfos',2),(169,'删除教师用户','删除教师','/adminUserInfo/deleteUserInfos',2),(170,'查询老师所教授课程','查询老师所教授课程列表','/selectCourse/queryTeacherCourses',1),(171,'用户信息修改','用户信息修改','sysman_User_Mod.html',2),(172,'查询某个用户的所有角色','查询某个用户的所有角色','/role/findRoleById',1),(173,'查询用户信息','通过用户id','/user/findUserById',1),(174,'查询角色','查询所有角色列表','/role/findAllRole',2),(175,'修改用户信息','修改所有用户的信息','/adminUserInfo/modefiedUserInfo',2),(176,'个人资料','个人资料查看与修改','personalInformation.html',1),(177,'个人资料修改','修改个人资料','/user/modifyPersonalInfo',2),(178,'删除回复','删除一条回复','/bbsReply/deleteBbsReply',2),(179,'获取对应名称的系统参数值','获取对应名称的系统参数值','/sypaController/attainValueByEnName',1),(187,'新增栏目','新增栏目','/column/saveNewCol',2),(188,'修改栏目','修改栏目','/column/editCols',2),(189,'删除栏目','删除栏目','/column/delCols',2),(190,'添加角色','添加角色','sysman_Competence_add.html',2),(191,'删除角色','删除角色','/authority/delRoleAuthority',2),(192,'播放集中学习资源','播放集中学习资源','my_Tutor_Learningvedio.html',2),(193,'删除教务员用户','删除教务员','/adminUserInfo/deleteUserInfos',2),(194,'删除管理员用户','删除管理员','/adminUserInfo/deleteUserInfos',2),(195,'查看课程的成绩','查看课程成绩','my_Tutor_selectCourseSta.html',1),(196,'用户等级管理','用户等级管理','eduman_User_stand.html',2),(197,'用户中心',NULL,'userCenter.html',2);
/*!40000 ALTER TABLE `Action` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Attachment`
--

DROP TABLE IF EXISTS `Attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Attachment` (
  `atta_id` int(11) NOT NULL AUTO_INCREMENT,
  `atta_filename` varchar(255) NOT NULL COMMENT '附件名称',
  `atta_location` varchar(1000) NOT NULL COMMENT '附件存放位置',
  `atta_size` varchar(255) DEFAULT NULL COMMENT '附件大小，由字符串表示，如“1.5M”',
  `atta_downnum` int(10) unsigned DEFAULT '0' COMMENT '附件下载数',
  `atta_source_type` int(11) NOT NULL COMMENT '附件所属原件的类别。1表示主贴、2表示回帖、3表示试题、4表示选项、5表示首页logo、6表示课程封面等。',
  `atta_sourceid` int(11) DEFAULT NULL COMMENT '附件所属帖子的id',
  `atta_describe` text COMMENT '附件描述',
  `atta_ori_filename` varchar(255) NOT NULL COMMENT '附件的原文件名',
  PRIMARY KEY (`atta_id`)
) ENGINE=MyISAM AUTO_INCREMENT=434 DEFAULT CHARSET=gbk COMMENT='附件表，存放bbs、试卷等中的相关附件,首页�';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Attachment`
--

LOCK TABLES `Attachment` WRITE;
/*!40000 ALTER TABLE `Attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `Attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BbsPost`
--

DROP TABLE IF EXISTS `BbsPost`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BbsPost` (
  `bbpo_id` int(11) NOT NULL AUTO_INCREMENT,
  `bbpo_cour_id` int(11) NOT NULL COMMENT '课程id',
  `bbpo_user_id` int(11) NOT NULL COMMENT '发贴人id',
  `bbpo_title` varchar(1000) NOT NULL COMMENT '标题',
  `bbpo_content` longtext NOT NULL COMMENT '正文',
  `bbpo_cont_len` int(11) DEFAULT NULL COMMENT '内容长度',
  `bbpo_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发表时间',
  `bbpo_updateTime` timestamp NULL DEFAULT NULL COMMENT '最近更新时间',
  `bbpo_visitnum` int(10) unsigned DEFAULT '0' COMMENT '访问数',
  `bbpo_replynum` int(10) unsigned DEFAULT '0' COMMENT '回复数',
  `bbpo_isbest` tinyint(1) NOT NULL DEFAULT '0' COMMENT '标识是否为精品',
  `bbpo_istop` tinyint(1) DEFAULT '0' COMMENT '标识是否置顶',
  `bbpo_hasattach` tinyint(1) DEFAULT '0' COMMENT '标识是否有附件',
  `bbpo_ori_id` varchar(255) DEFAULT NULL,
  `bbpo_user_name` varchar(255) DEFAULT NULL COMMENT '帖子所属部门名称',
  `bbpo_user_departId` int(10) DEFAULT NULL COMMENT '帖子所属部门id',
  `bbpo_cour_name` varchar(255) DEFAULT NULL COMMENT '帖子所属课程名称',
  PRIMARY KEY (`bbpo_id`),
  UNIQUE KEY `oriId` (`bbpo_ori_id`) USING BTREE,
  KEY `quickQuery` (`bbpo_istop`,`bbpo_cour_id`,`bbpo_time`,`bbpo_user_departId`,`bbpo_id`) USING BTREE,
  KEY `fk_BbsTopic_Course1` (`bbpo_cour_id`),
  KEY `fk_BbsPost_User1` (`bbpo_user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=gbk;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BbsPost`
--

LOCK TABLES `BbsPost` WRITE;
/*!40000 ALTER TABLE `BbsPost` DISABLE KEYS */;
/*!40000 ALTER TABLE `BbsPost` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BbsReply`
--

DROP TABLE IF EXISTS `BbsReply`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BbsReply` (
  `bbre_id` int(11) NOT NULL AUTO_INCREMENT,
  `bbre_bbpo_id` int(11) NOT NULL COMMENT '回复对应的主贴id',
  `bbre_user_id` int(11) NOT NULL COMMENT '回复人id',
  `bbre_content` text NOT NULL COMMENT '回复内容',
  `bbre_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '回复时间',
  `bbre_hasattach` tinyint(1) DEFAULT '0' COMMENT '标识是否有附件',
  PRIMARY KEY (`bbre_id`),
  KEY `fk_Post_BbsTopic1` (`bbre_bbpo_id`),
  KEY `fk_Post_User1` (`bbre_user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=gbk;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BbsReply`
--

LOCK TABLES `BbsReply` WRITE;
/*!40000 ALTER TABLE `BbsReply` DISABLE KEYS */;
/*!40000 ALTER TABLE `BbsReply` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Chapter`
--

DROP TABLE IF EXISTS `Chapter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Chapter` (
  `chap_id` int(11) NOT NULL AUTO_INCREMENT,
  `chap_cour_id` int(11) NOT NULL COMMENT '章节对应的课程id',
  `chap_name` varchar(255) NOT NULL,
  `chap_describe` text COMMENT '章节描述',
  `chap_ordinal` varchar(255) DEFAULT NULL COMMENT '章节序号',
  `chap_state` int(11) DEFAULT NULL COMMENT '章节的状态',
  `chap_createtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '章节创建时间',
  PRIMARY KEY (`chap_id`),
  KEY `fk_Chapter_Course1` (`chap_cour_id`)
) ENGINE=MyISAM AUTO_INCREMENT=177 DEFAULT CHARSET=utf8 COMMENT='课程章节表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Chapter`
--

LOCK TABLES `Chapter` WRITE;
/*!40000 ALTER TABLE `Chapter` DISABLE KEYS */;
/*!40000 ALTER TABLE `Chapter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Class`
--

DROP TABLE IF EXISTS `Class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Class` (
  `clas_id` int(11) NOT NULL AUTO_INCREMENT,
  `clas_grad_id` int(11) NOT NULL,
  `clas_name` varchar(255) NOT NULL COMMENT '班级名称',
  `clas_remark` mediumtext COMMENT '班级备注',
  PRIMARY KEY (`clas_id`),
  KEY `fk_Class_Grade1` (`clas_grad_id`)
) ENGINE=MyISAM AUTO_INCREMENT=1358 DEFAULT CHARSET=gbk COMMENT='班级表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Class`
--

LOCK TABLES `Class` WRITE;
/*!40000 ALTER TABLE `Class` DISABLE KEYS */;
INSERT INTO `Class` VALUES (34,20,'25班',NULL);
/*!40000 ALTER TABLE `Class` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Course`
--

DROP TABLE IF EXISTS `Course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Course` (
  `cour_id` int(11) NOT NULL AUTO_INCREMENT,
  `cour_cateIds` varchar(1000) DEFAULT NULL COMMENT '课程类别，一个课程可能有多个类别,例如"a2,3,4a"',
  `cour_tepa_id` int(11) DEFAULT NULL COMMENT '课程的考核方式id',
  `cour_code` varchar(255) NOT NULL DEFAULT '' COMMENT '课程代码',
  `cour_name` varchar(255) NOT NULL COMMENT '课程名',
  `cour_describe` text COMMENT '课程简介',
  `cour_credit` float unsigned DEFAULT NULL COMMENT '课程学分',
  `cour_teacher_ids` varchar(500) DEFAULT NULL COMMENT '该课程所有任课教师的id,多个id用分号隔开,例如",2,3,4,"',
  `cour_mentro_teaids` varchar(500) DEFAULT NULL COMMENT '该课程所有辅导教师的id,多个id用分号隔开,例如",2,3,4,"',
  `cour_time_schedule` varchar(1000) DEFAULT NULL COMMENT '课程计划，即课程的时间安排',
  `cour_state` int(11) DEFAULT '1' COMMENT '课程状态,2表示关闭,1表示开启',
  `cour_create_userid` int(11) DEFAULT NULL COMMENT '课程创建者id,课程开设者的部门即为课程所属学校',
  `cour_create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '课程创建时间',
  `cour_poston` tinyint(1) DEFAULT '1' COMMENT '标识该课程对应论坛版块是否开启,1表示开启,2表示关闭',
  `cour_forumName` varchar(255) DEFAULT '' COMMENT '课程论坛名称,暂时不使用(即论坛名和课程名保持一致)',
  `cour_verify` int(1) DEFAULT '0' COMMENT '标识课程是否通过审核,,0表示未审核,1表示审核通过,2表示审核未通过',
  `cour_coverPictureId` int(11) unsigned zerofill DEFAULT '00000000000' COMMENT '标识是否有封面图片',
  `cour_ori_id` varchar(255) DEFAULT NULL,
  `cour_ori_creator_id` varchar(255) DEFAULT NULL,
  `cour_openToAll` tinyint(1) DEFAULT '0' COMMENT '0表示仅对创建者的机构开放，1表示对全市开放',
  `cour_chooseNum` int(11) DEFAULT '0' COMMENT '选课人数',
  `cour_test_limitScore` float DEFAULT '0' COMMENT '学生参加该门课程相关测试的分数限制',
  `cour_assess_score` double DEFAULT '0' COMMENT '课程的综合评分，用于课程排序',
  `cour_type` varchar(45) DEFAULT '',
  `cour_year` int(11) NOT NULL,
  `cour_term` int(11) NOT NULL,
  `cour_studyPhase` int(11) NOT NULL,
  `cour_artScience` int(11) NOT NULL,
  PRIMARY KEY (`cour_id`,`cour_code`),
  KEY `fk_Course_TestPattern1` (`cour_tepa_id`)
) ENGINE=MyISAM AUTO_INCREMENT=49 DEFAULT CHARSET=gbk;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Course`
--

LOCK TABLES `Course` WRITE;
/*!40000 ALTER TABLE `Course` DISABLE KEYS */;
/*!40000 ALTER TABLE `Course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CourseCategory`
--

DROP TABLE IF EXISTS `CourseCategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CourseCategory` (
  `coca_id` int(11) NOT NULL AUTO_INCREMENT,
  `coca_name` varchar(255) NOT NULL COMMENT '课程类别名',
  `coca_descirbe` text COMMENT '类别描述',
  `coca_code` varchar(255) DEFAULT NULL COMMENT '类别代码',
  `coca_create_userid` int(11) DEFAULT NULL COMMENT '创建人id',
  `coca_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `coca_state` int(11) DEFAULT '1' COMMENT '课程类别状态,1表示开启,0表示关闭',
  `coca_org_id` varchar(255) DEFAULT NULL,
  `coca_org_creator` varchar(255) NOT NULL DEFAULT '',
  `coca_org_department` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`coca_id`,`coca_org_creator`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=gbk;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CourseCategory`
--

LOCK TABLES `CourseCategory` WRITE;
/*!40000 ALTER TABLE `CourseCategory` DISABLE KEYS */;
/*!40000 ALTER TABLE `CourseCategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CourseSelectInformation`
--

DROP TABLE IF EXISTS `CourseSelectInformation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CourseSelectInformation` (
  `csin_id` int(11) NOT NULL AUTO_INCREMENT,
  `csin_cour_id` int(11) NOT NULL COMMENT '课程id',
  `csin_depa_id` int(11) NOT NULL COMMENT '部门id',
  `csin_year` int(11) DEFAULT NULL COMMENT '年度',
  `csin_pubtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '选课信息发布时间',
  `csin_opentime` timestamp NULL DEFAULT NULL COMMENT '选课开放时间，为空表示发布时间即为开发时间',
  `csin_closetime` timestamp NULL DEFAULT NULL COMMENT '选课截止时间，为空表示永不截止',
  `csin_remark` text COMMENT '备注.一些附件说明信息.',
  `csin_on` tinyint(1) NOT NULL DEFAULT '1' COMMENT '标识该选课信息是否有效,,0表示关闭,1表示开启',
  PRIMARY KEY (`csin_id`),
  KEY `fk_CourseSelectInformation_Course1` (`csin_cour_id`),
  KEY `fk_CourseSelectInformation_Department1` (`csin_depa_id`)
) ENGINE=MyISAM AUTO_INCREMENT=39 DEFAULT CHARSET=gbk COMMENT='选课信息表，主要保存课程的选课开放和关闭时间等信息。';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CourseSelectInformation`
--

LOCK TABLES `CourseSelectInformation` WRITE;
/*!40000 ALTER TABLE `CourseSelectInformation` DISABLE KEYS */;
/*!40000 ALTER TABLE `CourseSelectInformation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Department`
--

DROP TABLE IF EXISTS `Department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Department` (
  `depa_id` int(11) NOT NULL AUTO_INCREMENT,
  `depa_name` varchar(255) NOT NULL COMMENT '部门名称',
  `depa_abbreviation` varchar(255) DEFAULT NULL COMMENT '部门简称',
  `depa_code` varchar(255) DEFAULT NULL COMMENT '部门代码',
  `depa_parent_id` int(11) DEFAULT NULL COMMENT '上级部门id',
  `depa_type` int(11) NOT NULL COMMENT '部分级别，分为市、区和学校3个级别,1表示市，2表示区，3表示学校',
  `depa_ori_id` varchar(255) DEFAULT NULL COMMENT '机构原始id，导数据用',
  PRIMARY KEY (`depa_id`),
  KEY `fk_Department_Department1` (`depa_parent_id`)
) ENGINE=MyISAM AUTO_INCREMENT=41 DEFAULT CHARSET=gbk;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Department`
--

LOCK TABLES `Department` WRITE;
/*!40000 ALTER TABLE `Department` DISABLE KEYS */;
INSERT INTO `Department` VALUES (12,'高中选修课网络课堂','网络课堂','',NULL,1,'00010004-0000-0000-0000-000000000000'),(34,'绵阳南山中学','南山中学','1111',12,3,'00010004-0022-0006-0000-000000000000');
/*!40000 ALTER TABLE `Department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EvaluateMethod`
--

DROP TABLE IF EXISTS `EvaluateMethod`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EvaluateMethod` (
  `evme_id` int(11) NOT NULL AUTO_INCREMENT,
  `evme_name` varchar(255) NOT NULL COMMENT '考核方式的名称',
  `evme_pattern` varchar(255) NOT NULL COMMENT '量化的考核方式，比如“10,40,20,30”，其中每个数字依次表示学习次数、视频学习时间(包括集中学习时间)、参加讨论次数、测试成绩所占的成绩在总成绩的比重（要求和为100）。',
  `evme_threhold` varchar(255) NOT NULL COMMENT '量化的考核方式中，各分项成绩对应的满分阈值，比如“5,120,0,0”可以表示学习次数达到5次，学习时间达到120分钟，成绩为满分，否则按照比例进行折算。',
  `evme_describe` text COMMENT '考核方式的详细描述',
  PRIMARY KEY (`evme_id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=gbk COMMENT='考核方式表,课程的考核方式决定了课程的成�';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EvaluateMethod`
--

LOCK TABLES `EvaluateMethod` WRITE;
/*!40000 ALTER TABLE `EvaluateMethod` DISABLE KEYS */;
INSERT INTO `EvaluateMethod` VALUES (1,'预设考核方式','20,40,20,20','10,60,30,100','数据迁移时默认的考核方式');
/*!40000 ALTER TABLE `EvaluateMethod` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ExamInformation`
--

DROP TABLE IF EXISTS `ExamInformation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ExamInformation` (
  `exin_id` int(11) NOT NULL AUTO_INCREMENT,
  `exin_expa_id` int(11) NOT NULL COMMENT '试卷id',
  `exin_name` varchar(255) NOT NULL COMMENT '考试名称',
  `exin_describe` text COMMENT '考试说明',
  `exin_totaltime` int(11) NOT NULL COMMENT '考试时长,单位为分钟',
  `exin_createtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '该考试创建时间',
  `exin_begintime` timestamp NULL DEFAULT NULL COMMENT '考试开放时间，为空表示创建时间即为开放时间',
  `exin_endtime` timestamp NULL DEFAULT NULL COMMENT '考试关闭时间，为空表示永不截止',
  PRIMARY KEY (`exin_id`),
  KEY `fk_ExamInformation_ExamPaper1` (`exin_expa_id`)
) ENGINE=MyISAM AUTO_INCREMENT=48 DEFAULT CHARSET=gbk COMMENT='在线测试信息表,包含课程测试的相关信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ExamInformation`
--

LOCK TABLES `ExamInformation` WRITE;
/*!40000 ALTER TABLE `ExamInformation` DISABLE KEYS */;
/*!40000 ALTER TABLE `ExamInformation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ExamPaper`
--

DROP TABLE IF EXISTS `ExamPaper`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ExamPaper` (
  `expa_id` int(11) NOT NULL AUTO_INCREMENT,
  `expa_cour_id` int(11) NOT NULL,
  `expa_ordinal` int(11) NOT NULL COMMENT '试卷编号,一门课程可能有多套试卷',
  `expa_difficulty` int(11) DEFAULT NULL COMMENT '试卷难度,包括：困难、偏难、一般、简单等几个难度，用数字区分',
  `expa_totalscore` int(11) NOT NULL COMMENT '试卷总分',
  `expa_name` varchar(2555) NOT NULL COMMENT '试卷名称',
  `expa_describe` text COMMENT '试卷描述',
  `expa_createtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '试卷创建时间',
  `expa_create_userid` int(11) NOT NULL COMMENT '试卷创建人id',
  `expa_type` int(11) NOT NULL COMMENT '试卷类型,标识能否自动评分等信息',
  `expa_gppa_id` int(11) DEFAULT NULL COMMENT '试卷的组卷参数id,仅对自动产生的试卷有效',
  `expa_ori_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`expa_id`),
  KEY `fk_ExaminationPaper_Course1` (`expa_cour_id`),
  KEY `fk_ExamPaper_GroupPaperParameter1` (`expa_gppa_id`)
) ENGINE=MyISAM AUTO_INCREMENT=51 DEFAULT CHARSET=gbk COMMENT='测试试卷表,包含了一份测试试卷的基本信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ExamPaper`
--

LOCK TABLES `ExamPaper` WRITE;
/*!40000 ALTER TABLE `ExamPaper` DISABLE KEYS */;
/*!40000 ALTER TABLE `ExamPaper` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ExamQuestion`
--

DROP TABLE IF EXISTS `ExamQuestion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ExamQuestion` (
  `exqu_id` int(11) NOT NULL AUTO_INCREMENT,
  `exqu_cour_id` int(11) NOT NULL COMMENT '试题对应的课程id',
  `exqu_describe` text NOT NULL COMMENT '问题描述',
  `exqu_type` int(11) NOT NULL COMMENT '问题的类型,比如单选题，多选题,判断题,填空题和问答题,用数字区分',
  `exqu_correctanswer` text NOT NULL COMMENT '正确答案，根据题目类型，答案形式不一样。',
  `exqu_createtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '试题创建时间',
  `exqu_create_userid` varchar(45) NOT NULL COMMENT '试题创建者id',
  `exqu_difficulty` int(11) DEFAULT NULL COMMENT '难度,包括：困难、偏难、一般、简单等几个难度，用数字区分',
  `exqu_attachment` tinyint(1) DEFAULT NULL COMMENT '标识是否有附件',
  `exqu_ori_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`exqu_id`),
  UNIQUE KEY `oriId` (`exqu_ori_id`) USING BTREE,
  KEY `fk_ExamQuestion_Course1` (`exqu_cour_id`)
) ENGINE=MyISAM AUTO_INCREMENT=681 DEFAULT CHARSET=gbk;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ExamQuestion`
--

LOCK TABLES `ExamQuestion` WRITE;
/*!40000 ALTER TABLE `ExamQuestion` DISABLE KEYS */;
/*!40000 ALTER TABLE `ExamQuestion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FunctionAction`
--

DROP TABLE IF EXISTS `FunctionAction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FunctionAction` (
  `fuac_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `fuac_fumo_id` int(11) NOT NULL COMMENT '功能模块id',
  `fuac_acti_id` int(11) NOT NULL COMMENT 'Action的id',
  PRIMARY KEY (`fuac_id`),
  KEY `fk_FunctionModel_has_Action_Action1` (`fuac_acti_id`),
  KEY `fk_FunctionModel_has_Action_FunctionModel1` (`fuac_fumo_id`)
) ENGINE=MyISAM AUTO_INCREMENT=262 DEFAULT CHARSET=gbk COMMENT='功能Action表，一个功能可能对应多个Action，一';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FunctionAction`
--

LOCK TABLES `FunctionAction` WRITE;
/*!40000 ALTER TABLE `FunctionAction` DISABLE KEYS */;
INSERT INTO `FunctionAction` VALUES (19,19,132),(48,48,1),(58,24,15),(59,25,16),(60,26,2),(61,26,3),(62,26,4),(63,27,5),(64,27,6),(65,27,7),(66,27,8),(67,28,9),(68,28,10),(69,28,11),(73,17,17),(74,17,18),(75,17,19),(77,18,20),(78,17,21),(79,17,22),(80,17,23),(81,17,24),(82,17,25),(83,17,26),(84,17,27),(91,39,32),(93,39,34),(94,16,35),(95,16,36),(96,16,37),(97,32,38),(98,32,39),(99,32,40),(100,32,41),(101,23,43),(102,23,46),(103,23,47),(104,23,48),(105,33,38),(106,36,49),(107,33,39),(108,33,40),(109,33,41),(110,34,38),(111,34,39),(112,34,40),(113,34,41),(116,36,68),(117,36,69),(118,37,70),(119,37,42),(120,37,71),(121,30,42),(122,30,50),(123,30,18),(124,37,72),(125,37,26),(126,37,27),(127,31,42),(128,31,51),(129,31,52),(130,37,73),(131,37,74),(132,37,75),(133,38,42),(134,37,77),(135,38,78),(136,42,42),(137,42,53),(138,42,54),(139,42,55),(140,42,56),(141,42,57),(142,38,79),(144,38,81),(145,35,82),(146,52,58),(147,52,59),(148,52,60),(149,35,83),(150,35,84),(151,49,85),(152,49,86),(153,53,61),(154,41,42),(155,41,87),(156,41,88),(157,41,90),(158,50,89),(159,50,91),(160,50,92),(161,50,93),(162,43,94),(163,54,42),(164,54,50),(165,54,62),(166,54,63),(167,43,95),(168,43,97),(169,43,98),(170,29,99),(171,29,100),(172,55,29),(173,55,30),(174,55,31),(175,55,42),(176,55,50),(177,55,63),(178,56,42),(179,56,50),(180,56,64),(181,51,122),(182,51,123),(183,51,124),(184,22,143),(185,40,42),(186,40,156),(187,44,42),(188,44,168),(190,46,162),(191,47,163),(196,44,140),(197,44,141),(200,45,42),(202,46,42),(203,47,42),(204,45,50),(205,17,178),(206,30,178),(207,17,179),(208,18,179),(209,30,22),(210,30,17),(211,30,19),(212,30,21),(215,30,28),(221,87,187),(222,87,188),(223,87,189),(224,48,190),(225,48,191),(226,20,192),(227,39,31),(228,36,67),(229,37,76),(230,40,158),(231,40,159),(232,44,160),(233,44,164),(234,44,171),(238,45,161),(239,45,165),(240,45,171),(241,45,169),(242,46,171),(244,46,167),(245,46,193),(246,47,166),(247,47,171),(248,47,194),(249,19,195),(250,20,134),(251,20,135),(252,20,136),(253,20,137),(254,20,138),(256,37,142),(257,88,1),(258,89,196),(259,14,197);
/*!40000 ALTER TABLE `FunctionAction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FunctionModel`
--

DROP TABLE IF EXISTS `FunctionModel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FunctionModel` (
  `fumo_id` int(11) NOT NULL AUTO_INCREMENT,
  `fumo_parentid` int(11) DEFAULT NULL COMMENT '父亲模块id。模块是分级的',
  `fumo_name` varchar(255) NOT NULL COMMENT '功能模块名称',
  `fumo_describe` text COMMENT '功能模块描述',
  `fumo_type` int(11) NOT NULL COMMENT '功能模块类型,比如课程、论坛、测试等',
  `fumo_url` varchar(1000) NOT NULL COMMENT '功能模块对应的页面url',
  `fumo_ordinal` int(11) NOT NULL COMMENT '模块序号',
  `fumo_icon` varchar(255) DEFAULT NULL COMMENT '栏目对应的图标',
  PRIMARY KEY (`fumo_id`),
  KEY `fk_FunctionModel_FunctionModel1` (`fumo_parentid`)
) ENGINE=MyISAM AUTO_INCREMENT=101 DEFAULT CHARSET=gbk COMMENT='功能模块表，即权限表，每个功能模块都对应';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FunctionModel`
--

LOCK TABLES `FunctionModel` WRITE;
/*!40000 ALTER TABLE `FunctionModel` DISABLE KEYS */;
INSERT INTO `FunctionModel` VALUES (1,NULL,'个人中心','个人中心',1,'userCenter.html',1,'fa fa-user'),(2,NULL,'学习中心','学习中心',1,'Learning.html',2,'fa fa-book'),(3,NULL,'课程讨论','课程讨论',1,'bbs_index.html',3,'fa fa-comments-o'),(4,NULL,'我的测试','我的测试',1,'my_Test.html',4,'fa fa-pencil-square-o'),(5,NULL,'我的课堂','我的课程',1,'my_Tutor.html',5,'fa fa-mortar-board'),(6,NULL,'测试管理','测试管理',1,'sysman_CourseTest.html',6,'fa fa-calendar-o'),(7,NULL,'论坛管理','论坛管理',1,'eduman_BbsInfoManage.html',7,'fa fa-group'),(8,NULL,'资源管理','资源管理',1,'sysman_Video.html',8,'fa fa-folder-open'),(9,NULL,'教务管理','教务管理',1,'eduman_Courses_Class.html',9,'fa fa-qrcode'),(10,NULL,'机构管理','机构、班级管理',1,'sysman_Department.html',10,'fa fa-sitemap'),(11,NULL,'用户管理','用户管理',1,'sysman_Student.html',11,'fa fa-credit-card'),(12,NULL,'系统管理','系统管理',1,'sysman_Parameters.html',14,'fa fa-gears'),(13,NULL,'教务统计','教务统计',1,'eduman_Statistic_CourseStaForAdmin.html',13,'fa fa-bar-chart-o'),(14,1,'个人中心','个人中心',2,'userCenter.html',1,NULL),(15,1,'消息中心','消息中心',2,'notice.html',2,NULL),(16,2,'学习中心','学习中心',2,'Learning.html',1,NULL),(17,3,'课程讨论',NULL,2,'bbs_index.html',1,NULL),(18,3,'发起讨论','发起新的讨论',2,'bbs_newPost.html',2,NULL),(19,5,'我的课程','我的课程',2,'my_Tutor.html',1,''),(20,5,'集中学习','集中学习',2,'my_Tutor_studyTogether.html',2,''),(22,5,'新课申请','新课申请',2,'my_Tutor_registerNewCourse.html',4,''),(23,8,'章节管理','章节管理',2,'sysman_Chapter.html',5,''),(24,4,'课程测试','课程测试',2,'my_Test.html',1,NULL),(25,4,'我的成绩','我的成绩',2,'my_Score.html',2,NULL),(26,6,'课程测试管理','课程测试管理',2,'sysman_CourseTest.html',1,NULL),(27,6,'题库管理','题库管理',2,'sysman_Questions.html',2,NULL),(28,6,'试卷管理',NULL,2,'sysman_Paper.html',3,NULL),(29,6,'成绩管理',NULL,2,'sysman_Score.html',4,NULL),(30,7,'课程讨论管理',NULL,2,'eduman_BbsInfoManage.html',1,NULL),(31,7,'论坛管理',NULL,2,'eduman_BbsopenCloseManage.html',2,NULL),(32,8,'视频管理',NULL,2,'sysman_Video.html',1,NULL),(33,8,'文档管理',NULL,2,'sysman_Document.html',2,NULL),(34,8,'链接管理',NULL,2,'sysman_Link.html',3,NULL),(35,8,'服务器管理',NULL,2,'sysman_Server.html',4,NULL),(36,9,'课程领域管理',NULL,2,'eduman_Courses_Class.html',1,NULL),(37,9,'课程管理',NULL,2,'eduman_Courses_Manage.html',2,NULL),(38,9,'课程审批',NULL,2,'eduman_Courses_Approval.html',3,NULL),(39,9,'考核方式管理',NULL,2,'eduman_ExaminingMode.html',4,NULL),(40,9,'选课时间管理',NULL,2,'eduman_Schedule.html',5,NULL),(41,9,'辅导教师管理',NULL,2,'eduman_Courses_Teacher.html',6,NULL),(42,10,'机构管理',NULL,2,'sysman_Department.html',1,NULL),(43,10,'班级管理',NULL,2,'sysman_Class.html',2,NULL),(44,11,'学生管理',NULL,2,'sysman_Student.html',1,NULL),(45,11,'教师管理',NULL,2,'sysman_Teacher.html',2,NULL),(46,11,'教务员管理',NULL,2,'sysman_Educational.html',3,NULL),(47,11,'管理员管理',NULL,2,'sysman_Manager.html',4,NULL),(48,11,'权限管理',NULL,2,'sysman_Competence.html',5,NULL),(49,12,'系统参数管理',NULL,2,'sysman_Parameters.html',1,NULL),(50,12,'友情链接管理',NULL,2,'sysman_Links.html',2,NULL),(51,12,'顶部图片管理',NULL,2,'sysman_Banner.html',3,NULL),(52,12,'日志管理',NULL,2,'sysman_Journal.html',4,NULL),(53,12,'平台应用统计',NULL,2,'sysman_platformAppData.html',5,NULL),(54,13,'课程选修统计',NULL,2,'eduman_Statistic_CourseStaForAdmin.html',1,NULL),(55,13,'学生成绩查看',NULL,2,'eduman_Statistic_scoreInquiry.html',2,NULL),(56,13,'教师辅导统计',NULL,2,'eduman_Statistic_TeacherTutorSta.html',3,NULL),(87,12,'栏目管理',NULL,2,'sysman_Column.html',6,NULL),(88,13,'学生成绩确认统计',NULL,2,'eduman_Statistic_confirm.html',4,NULL),(89,9,'用户等级管理',NULL,2,'eduman_User_stand.html',7,NULL),(90,NULL,'1',NULL,1,'2',4,'3'),(91,NULL,'1',NULL,1,'1',1,'1'),(92,NULL,'1',NULL,1,'1',1,'1'),(93,1,'2',NULL,2,'2',2,NULL),(94,NULL,'测试测试',NULL,1,'home.html',100,'dsd'),(95,NULL,'个人中心',NULL,1,'userCenter.html',1,'fa fa-user'),(96,1,'个人中心',NULL,2,'userCenter.html',1,NULL),(97,NULL,'测试一级',NULL,1,'gggg.html',100,'fa fa-user'),(98,97,'测试二级',NULL,2,'gggg.html',100,NULL);
/*!40000 ALTER TABLE `FunctionModel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Grade`
--

DROP TABLE IF EXISTS `Grade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Grade` (
  `grad_id` int(11) NOT NULL AUTO_INCREMENT,
  `grad_depa_id` int(11) NOT NULL COMMENT '年级所属学校的id',
  `grad_name` varchar(255) NOT NULL COMMENT '年级名称',
  PRIMARY KEY (`grad_id`)
) ENGINE=MyISAM AUTO_INCREMENT=83 DEFAULT CHARSET=gbk;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Grade`
--

LOCK TABLES `Grade` WRITE;
/*!40000 ALTER TABLE `Grade` DISABLE KEYS */;
INSERT INTO `Grade` VALUES (20,34,'2012级');
/*!40000 ALTER TABLE `Grade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GroupPaperParameter`
--

DROP TABLE IF EXISTS `GroupPaperParameter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GroupPaperParameter` (
  `gppa_id` int(11) NOT NULL AUTO_INCREMENT,
  `gppa_questionType` int(11) NOT NULL COMMENT '试题类型',
  `gppa_difficulty` int(11) NOT NULL COMMENT '试题难度',
  `gppa_questionScore` float NOT NULL COMMENT '每个试题分数',
  `gppa_questionNum` int(11) NOT NULL COMMENT '该类试题的数量',
  `gppa_name` varchar(255) NOT NULL COMMENT '组卷参数名称，相同名称的参数是一个整体，共同表示一类试卷的参数',
  `gppa_createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '参数创建时间',
  `gppa_creatorId` int(11) NOT NULL COMMENT '创建者id',
  `gppa_ordinal` int(11) NOT NULL COMMENT '参数序号,方便查询和按固定顺序显示,序号从1开始',
  `gppa_ori_id` varchar(255) DEFAULT NULL COMMENT '用于数据迁移，存放对应的原始ID',
  PRIMARY KEY (`gppa_id`)
) ENGINE=MyISAM AUTO_INCREMENT=15 DEFAULT CHARSET=gbk COMMENT='组卷参数表，用于自动组卷，用来限定试卷的题型、分值和难度分布。';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GroupPaperParameter`
--

LOCK TABLES `GroupPaperParameter` WRITE;
/*!40000 ALTER TABLE `GroupPaperParameter` DISABLE KEYS */;
/*!40000 ALTER TABLE `GroupPaperParameter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `HomepageColumn`
--

DROP TABLE IF EXISTS `HomepageColumn`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `HomepageColumn` (
  `hoco_id` int(11) NOT NULL AUTO_INCREMENT,
  `hoco_name` varchar(255) NOT NULL COMMENT '版块名称',
  `hoco_descirbe` text COMMENT '版块描述',
  `hoco_type` int(11) NOT NULL COMMENT '版块类型,比如课程、通知公告等',
  `hoco_createtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '版块创建时间',
  PRIMARY KEY (`hoco_id`,`hoco_name`)
) ENGINE=MyISAM DEFAULT CHARSET=gbk;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `HomepageColumn`
--

LOCK TABLES `HomepageColumn` WRITE;
/*!40000 ALTER TABLE `HomepageColumn` DISABLE KEYS */;
/*!40000 ALTER TABLE `HomepageColumn` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `HomepageDetail`
--

DROP TABLE IF EXISTS `HomepageDetail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `HomepageDetail` (
  `hode_id` int(11) NOT NULL AUTO_INCREMENT,
  `hode_hoco_id` int(11) NOT NULL COMMENT '所属首页版块id',
  `hode_title` varchar(500) NOT NULL COMMENT '标题',
  `hode_content` text NOT NULL COMMENT '内容',
  `hode_create_userid` int(11) NOT NULL COMMENT '创建者id',
  `hode_create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `hode_change_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `hode_attachment` tinyint(1) DEFAULT NULL COMMENT '标识是否有附件',
  PRIMARY KEY (`hode_id`),
  KEY `fk_HomepageDetail_HomepageColumn1` (`hode_hoco_id`)
) ENGINE=MyISAM DEFAULT CHARSET=gbk;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `HomepageDetail`
--

LOCK TABLES `HomepageDetail` WRITE;
/*!40000 ALTER TABLE `HomepageDetail` DISABLE KEYS */;
/*!40000 ALTER TABLE `HomepageDetail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `LearnProcessRecord`
--

DROP TABLE IF EXISTS `LearnProcessRecord`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `LearnProcessRecord` (
  `lpre_id` int(11) NOT NULL AUTO_INCREMENT,
  `lpre_user_id` int(11) NOT NULL COMMENT '学生id',
  `lpre_chap_id` int(11) NOT NULL COMMENT '课程章节id',
  `lpre_content` text NOT NULL COMMENT '学习内容',
  `lpre_begintime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始学习时间',
  `lpre_endtime` timestamp NULL DEFAULT NULL COMMENT '结束学习时间',
  `lpre_learntime` int(11) DEFAULT NULL COMMENT '学习时长,单位为分钟',
  `lpre_reso_id` int(10) NOT NULL,
  PRIMARY KEY (`lpre_id`),
  KEY `fk_LearnProcessRecord_User1` (`lpre_user_id`),
  KEY `fk_LearnProcessRecord_Chapter1` (`lpre_chap_id`)
) ENGINE=MyISAM AUTO_INCREMENT=33140 DEFAULT CHARSET=gbk;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LearnProcessRecord`
--

LOCK TABLES `LearnProcessRecord` WRITE;
/*!40000 ALTER TABLE `LearnProcessRecord` DISABLE KEYS */;
/*!40000 ALTER TABLE `LearnProcessRecord` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MassedLearning`
--

DROP TABLE IF EXISTS `MassedLearning`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MassedLearning` (
  `male_id` int(11) NOT NULL AUTO_INCREMENT,
  `male_cour_id` int(11) NOT NULL COMMENT '课程id',
  `male_reso_id` int(11) NOT NULL COMMENT '资源id',
  `male_name` varchar(255) NOT NULL COMMENT '名称',
  `male_describe` text COMMENT '描述',
  `male_createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `male_startTime` timestamp NULL DEFAULT NULL COMMENT '开始时间',
  `male_endTime` timestamp NULL DEFAULT NULL COMMENT '集中学习时长,单位为分钟',
  `male_schoolId` int(11) NOT NULL COMMENT '学校id',
  `male_teacherId` int(11) NOT NULL COMMENT '辅导教师id',
  `male_state` int(10) unsigned DEFAULT NULL COMMENT '当前状态，0表示未完成，1表示已完成',
  PRIMARY KEY (`male_id`),
  KEY `fk_MassedLearning_Course1` (`male_cour_id`),
  KEY `fk_MassedLearning_Resource1` (`male_reso_id`)
) ENGINE=MyISAM AUTO_INCREMENT=14 DEFAULT CHARSET=gbk;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MassedLearning`
--

LOCK TABLES `MassedLearning` WRITE;
/*!40000 ALTER TABLE `MassedLearning` DISABLE KEYS */;
/*!40000 ALTER TABLE `MassedLearning` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MassedLearningManage`
--

DROP TABLE IF EXISTS `MassedLearningManage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MassedLearningManage` (
  `mlma_id` int(11) NOT NULL AUTO_INCREMENT,
  `mlma_cour_id` int(11) NOT NULL COMMENT '课程id',
  `mlma_maxTime` int(11) NOT NULL COMMENT '最大集中学习时间',
  `mala_maxStudentNum` int(11) NOT NULL COMMENT '每次集中学习的最大人数',
  `male_createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`mlma_id`),
  KEY `fk_MassedLearningManage_Course1` (`mlma_cour_id`)
) ENGINE=MyISAM DEFAULT CHARSET=gbk;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MassedLearningManage`
--

LOCK TABLES `MassedLearningManage` WRITE;
/*!40000 ALTER TABLE `MassedLearningManage` DISABLE KEYS */;
/*!40000 ALTER TABLE `MassedLearningManage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `NoticeAnnouncement`
--

DROP TABLE IF EXISTS `NoticeAnnouncement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `NoticeAnnouncement` (
  `noan_id` int(11) NOT NULL AUTO_INCREMENT,
  `noan_title` varchar(255) NOT NULL COMMENT '标题',
  `noan_content` text NOT NULL COMMENT '内容',
  `noan_roleids` varchar(20) NOT NULL COMMENT '保存要发送的用户类别',
  `noan_pubtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `noan_topdays` int(11) NOT NULL COMMENT '置顶的天数',
  `noan_isimport` tinyint(1) DEFAULT NULL COMMENT '标识是否重要或紧急',
  `noan_viewnum` int(11) DEFAULT NULL COMMENT '浏览数',
  `noan_attachment` int(10) DEFAULT NULL COMMENT '标识是否有附件',
  `noan_creatorid` varchar(10) NOT NULL COMMENT '发布人id',
  `noan_deparid` int(10) DEFAULT NULL,
  `noan_courid` int(10) DEFAULT NULL,
  PRIMARY KEY (`noan_id`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=gbk;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `NoticeAnnouncement`
--

LOCK TABLES `NoticeAnnouncement` WRITE;
/*!40000 ALTER TABLE `NoticeAnnouncement` DISABLE KEYS */;
/*!40000 ALTER TABLE `NoticeAnnouncement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Option`
--

DROP TABLE IF EXISTS `Option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Option` (
  `opti_id` int(11) NOT NULL AUTO_INCREMENT,
  `opti_ques_id` int(11) NOT NULL COMMENT '选项对应的问题id',
  `opti_describe` varchar(500) NOT NULL COMMENT '选项描述',
  `opti_ordinal` int(11) DEFAULT NULL COMMENT '选项编号',
  `opti_attachment` tinyint(1) DEFAULT NULL COMMENT '标识当前选项是否有附件',
  `opti_ori_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`opti_id`),
  UNIQUE KEY `oriId` (`opti_ori_id`) USING BTREE,
  KEY `fk_Option_Question1` (`opti_ques_id`)
) ENGINE=MyISAM AUTO_INCREMENT=2729 DEFAULT CHARSET=gbk COMMENT='选项表，主要针对选择题。';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Option`
--

LOCK TABLES `Option` WRITE;
/*!40000 ALTER TABLE `Option` DISABLE KEYS */;
/*!40000 ALTER TABLE `Option` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Re_Authority`
--

DROP TABLE IF EXISTS `Re_Authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Re_Authority` (
  `reau_id` int(11) NOT NULL AUTO_INCREMENT,
  `reau_role_id` int(11) NOT NULL COMMENT '角色id',
  `reau_fumo_id` int(11) NOT NULL COMMENT '功能模块id',
  `reau_createtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '权限分配时间',
  `reau_Actions` varchar(1000) NOT NULL COMMENT '限定权限对应的ation的Id,多个用逗号隔开',
  PRIMARY KEY (`reau_id`),
  KEY `fk_Role_has_FunctionModel_FunctionModel1` (`reau_fumo_id`),
  KEY `fk_Role_has_FunctionModel_Role1` (`reau_role_id`)
) ENGINE=MyISAM AUTO_INCREMENT=1797 DEFAULT CHARSET=gbk COMMENT='权限分配表，即指定角色对应的若干功能模块';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Re_Authority`
--

LOCK TABLES `Re_Authority` WRITE;
/*!40000 ALTER TABLE `Re_Authority` DISABLE KEYS */;
INSERT INTO `Re_Authority` VALUES (1473,1,14,'2014-01-14 10:54:30','125'),(1474,1,15,'2014-01-14 10:54:30','126'),(1475,1,16,'2014-01-14 10:54:30','35,36,37,139'),(1476,1,17,'2014-01-14 10:54:30','18,21,22,23,24,25,26,27,179'),(1477,1,18,'2014-01-14 10:54:30','20,179'),(1478,1,24,'2014-01-14 10:54:30','15'),(1479,1,25,'2014-01-14 10:54:30','16'),(1701,5,14,'2014-03-18 14:53:00','125'),(1702,5,15,'2014-03-18 14:53:00','126'),(1703,5,16,'2014-03-18 14:53:00','35,36,37,139'),(1704,5,17,'2014-03-18 14:53:00','17,18,19,21,22,23,24,25,26,27,178,179'),(1705,5,18,'2014-03-18 14:53:00','20,179'),(1706,5,24,'2014-03-18 14:53:00','15'),(1707,5,25,'2014-03-18 14:53:00','16'),(1708,5,19,'2014-03-18 14:53:00','132,195'),(1709,5,20,'2014-03-18 14:53:00','192,134,135,136,137,138'),(1710,5,22,'2014-03-18 14:53:00','143'),(1711,5,26,'2014-03-18 14:53:00','2,3,4'),(1712,5,27,'2014-03-18 14:53:00','5,6,7,8'),(1713,5,28,'2014-03-18 14:53:00','9,10,11'),(1714,5,29,'2014-03-18 14:53:00','99,100'),(1715,5,30,'2014-03-18 14:53:00','42,50,18,178,22,17,19,21,28'),(1716,5,31,'2014-03-18 14:53:00','42,51,52'),(1717,5,23,'2014-03-18 14:53:00','43,46,47,48'),(1718,5,32,'2014-03-18 14:53:00','38,39,40,41'),(1719,5,33,'2014-03-18 14:53:00','38,39,40,41'),(1720,5,34,'2014-03-18 14:53:00','38,39,40,41'),(1721,5,35,'2014-03-18 14:53:00','80,82,83,84'),(1722,5,36,'2014-03-18 14:53:00','49,68,69,67'),(1723,5,37,'2014-03-18 14:53:00','70,42,71,72,26,27,73,74,75,77,76,142'),(1724,5,38,'2014-03-18 14:53:00','42,78,79,81'),(1725,5,39,'2014-03-18 14:53:00','32,34,31'),(1726,5,40,'2014-03-18 14:53:00','42,156,158,159'),(1727,5,41,'2014-03-18 14:53:00','42,87,88,90'),(1728,5,42,'2014-03-18 14:53:00','42,53,54,55,56,57'),(1729,5,43,'2014-03-18 14:53:00','94,95,97,98'),(1730,5,44,'2014-03-18 14:53:00','42,168,140,141,160,164,171'),(1731,5,45,'2014-03-18 14:53:00','42,50,161,165,171,169'),(1732,5,46,'2014-03-18 14:53:00','162,42,171,167,193'),(1733,5,47,'2014-03-18 14:53:00','163,42,166,171,194'),(1734,5,48,'2014-03-18 14:53:00','1,190,191'),(1735,5,49,'2014-03-18 14:53:00','85,86'),(1736,5,50,'2014-03-18 14:53:00','89,91,92,93'),(1737,5,51,'2014-03-18 14:53:00','122,123,124'),(1738,5,52,'2014-03-18 14:53:00','58,59,60'),(1739,5,53,'2014-03-18 14:53:00','61'),(1740,5,87,'2014-03-18 14:53:00','187,188,189'),(1741,5,54,'2014-03-18 14:53:00','42,50,62,63'),(1742,5,55,'2014-03-18 14:53:00','29,30,31,42,50,63'),(1743,5,56,'2014-03-18 14:53:00','42,50,64'),(1744,5,88,'2014-03-18 14:53:00','1'),(1745,4,14,'2014-03-18 14:53:08','125'),(1746,4,15,'2014-03-18 14:53:08','126'),(1747,4,26,'2014-03-18 14:53:08','2,3,4'),(1748,4,27,'2014-03-18 14:53:08','5,6,7,8'),(1749,4,28,'2014-03-18 14:53:08','9,10,11'),(1750,4,29,'2014-03-18 14:53:08','99,100'),(1751,4,30,'2014-03-18 14:53:08','42,50,18,178,22,17,19,21,28'),(1752,4,31,'2014-03-18 14:53:08','42,51,52'),(1753,4,23,'2014-03-18 14:53:08','43,46,47,48'),(1754,4,32,'2014-03-18 14:53:08','38,39,40,41'),(1755,4,33,'2014-03-18 14:53:08','38,39,40,41'),(1756,4,34,'2014-03-18 14:53:08','38,39,40,41'),(1757,4,35,'2014-03-18 14:53:08','80,82,83,84'),(1758,4,36,'2014-03-18 14:53:08','49,68,69,67'),(1759,4,37,'2014-03-18 14:53:08','70,42,71,72,26,27,73,74,75,77,76,142'),(1760,4,39,'2014-03-18 14:53:08','32,34,31'),(1761,4,40,'2014-03-18 14:53:08','42,156,158,159'),(1762,4,41,'2014-03-18 14:53:08','42,87,88,90'),(1763,4,42,'2014-03-18 14:53:08','42,53,54,55,56,57'),(1764,4,43,'2014-03-18 14:53:08','94,95,97,98'),(1765,4,44,'2014-03-18 14:53:08','42,168,140,141,160,164,171'),(1766,4,45,'2014-03-18 14:53:08','42,50,161,165,171,169'),(1767,4,46,'2014-03-18 14:53:08','162,42,171,167,193'),(1768,4,47,'2014-03-18 14:53:08','163,42,166,171,194'),(1769,4,48,'2014-03-18 14:53:08','1,190,191'),(1770,4,49,'2014-03-18 14:53:08','85,86'),(1771,4,50,'2014-03-18 14:53:08','89,91,92,93'),(1772,4,51,'2014-03-18 14:53:08','122,123,124'),(1773,4,52,'2014-03-18 14:53:08','58,59,60'),(1774,4,53,'2014-03-18 14:53:08','61'),(1775,4,54,'2014-03-18 14:53:08','42,50,62,63'),(1776,4,55,'2014-03-18 14:53:08','29,30,31,42,50,63'),(1777,4,56,'2014-03-18 14:53:08','42,50,64'),(1778,4,88,'2014-03-18 14:53:08','1'),(1779,2,14,'2014-04-23 16:06:09','125'),(1780,2,15,'2014-04-23 16:06:09','126'),(1781,2,16,'2014-04-23 16:06:09','35,36,37,139'),(1782,2,17,'2014-04-23 16:06:09','17,18,19,21,22,23,24,25,26,27,178,179'),(1783,2,18,'2014-04-23 16:06:09','20'),(1784,2,19,'2014-04-23 16:06:09','132,195'),(1785,2,20,'2014-04-23 16:06:09','192,134,135,136,137,138'),(1786,2,30,'2014-04-23 16:06:09','18'),(1787,3,14,'2014-04-23 16:06:19','125'),(1788,3,15,'2014-04-23 16:06:19','126'),(1789,3,30,'2014-04-23 16:06:19','42,50,18,178,22,17,19,21,28'),(1790,3,41,'2014-04-23 16:06:19','42,87,88,90'),(1791,3,43,'2014-04-23 16:06:19','94,95,97,98'),(1792,3,44,'2014-04-23 16:06:19','42,168,140,141,160,164,171'),(1793,3,45,'2014-04-23 16:06:19','42,50,161,165,171,169'),(1794,3,54,'2014-04-23 16:06:19','42,50,62,63'),(1795,3,55,'2014-04-23 16:06:19','29,30,31,42,50,63'),(1796,3,56,'2014-04-23 16:06:19','42,50,64');
/*!40000 ALTER TABLE `Re_Authority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Re_AutoTest`
--

DROP TABLE IF EXISTS `Re_AutoTest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Re_AutoTest` (
  `rate_id` int(11) NOT NULL AUTO_INCREMENT,
  `rate_user_id` int(11) NOT NULL COMMENT '测试者id,主要指学生',
  `rate_exin_id` int(11) NOT NULL COMMENT '对应的考试id',
  `rate_score` float DEFAULT '-1' COMMENT '分数',
  `rate_starttime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始测试试卷',
  `rate_submittime` timestamp NULL DEFAULT NULL COMMENT '提交时间',
  `rate_isfinished` tinyint(1) NOT NULL COMMENT '是否完成',
  PRIMARY KEY (`rate_id`),
  KEY `fk_User_has_ExamInformation_ExamInformation1` (`rate_exin_id`),
  KEY `fk_User_has_ExamInformation_User1` (`rate_user_id`)
) ENGINE=MyISAM AUTO_INCREMENT=10502 DEFAULT CHARSET=gbk COMMENT='在线自测表,主要记录学生参与的测试。';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Re_AutoTest`
--

LOCK TABLES `Re_AutoTest` WRITE;
/*!40000 ALTER TABLE `Re_AutoTest` DISABLE KEYS */;
/*!40000 ALTER TABLE `Re_AutoTest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Re_ChapterResource`
--

DROP TABLE IF EXISTS `Re_ChapterResource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Re_ChapterResource` (
  `rcre_id` int(11) NOT NULL AUTO_INCREMENT,
  `rcre_chap_id` int(11) NOT NULL COMMENT '课程章节id',
  `rcre_reso_id` int(11) NOT NULL COMMENT '资源id',
  PRIMARY KEY (`rcre_id`),
  KEY `fk_Chapter_has_Resource_Resource1` (`rcre_reso_id`),
  KEY `fk_Chapter_has_Resource_Chapter1` (`rcre_chap_id`)
) ENGINE=MyISAM AUTO_INCREMENT=177 DEFAULT CHARSET=utf8 COMMENT='章节和资源间的利用关系表。';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Re_ChapterResource`
--

LOCK TABLES `Re_ChapterResource` WRITE;
/*!40000 ALTER TABLE `Re_ChapterResource` DISABLE KEYS */;
/*!40000 ALTER TABLE `Re_ChapterResource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Re_GroupPaper`
--

DROP TABLE IF EXISTS `Re_GroupPaper`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Re_GroupPaper` (
  `rgpa_id` int(11) NOT NULL AUTO_INCREMENT,
  `rgpa_expa_id` int(11) NOT NULL COMMENT '试卷id',
  `rgpa_exqu_id` int(11) NOT NULL COMMENT '试题id',
  `rgpa_ordinal` int(11) DEFAULT NULL COMMENT '试题在试卷中的序号',
  `rgpa_score` float DEFAULT NULL COMMENT '试题在试卷中所在分值',
  PRIMARY KEY (`rgpa_id`),
  KEY `fk_ExaminationPaper_has_ExamQuestion_ExamQuestion1` (`rgpa_exqu_id`),
  KEY `fk_ExaminationPaper_has_ExamQuestion_ExaminationPaper1` (`rgpa_expa_id`)
) ENGINE=MyISAM AUTO_INCREMENT=675 DEFAULT CHARSET=gbk COMMENT='组卷表,包含了试卷和试题之间的包含关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Re_GroupPaper`
--

LOCK TABLES `Re_GroupPaper` WRITE;
/*!40000 ALTER TABLE `Re_GroupPaper` DISABLE KEYS */;
/*!40000 ALTER TABLE `Re_GroupPaper` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Re_SelectCource`
--

DROP TABLE IF EXISTS `Re_SelectCource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Re_SelectCource` (
  `rsco_id` int(11) NOT NULL AUTO_INCREMENT,
  `rsco_user_id` int(11) NOT NULL COMMENT '选课人id',
  `rsco_cour_id` int(11) NOT NULL COMMENT '课程id',
  `rsco_verify` int(10) unsigned DEFAULT NULL COMMENT '标识选课是否通过',
  `rsco_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '选课时间',
  `rsco_massedLearnScore` float unsigned DEFAULT '0' COMMENT '集中学习分数',
  `rsco_loginScore` float unsigned DEFAULT '0' COMMENT '登录次数分数,这儿的登录次数指的是学习次数',
  `rsco_learnTimeScore` float unsigned DEFAULT '0' COMMENT '学习时间分数',
  `rsco_bbsDiscussScore` float unsigned DEFAULT '0' COMMENT '参与论坛讨论分数',
  `rsco_subAssessScore` float unsigned DEFAULT '0' COMMENT '主观评价分数',
  `rsco_testScore` float unsigned DEFAULT '0' COMMENT '在线自测分数',
  `rsco_totalScore` float unsigned DEFAULT '0' COMMENT '总分',
  `rsco_state` int(10) unsigned DEFAULT '0' COMMENT '状态,0表示课程未完结,1表示课程已完结.课程是否完结以是否已导出成绩为主，即0表示尚未导出成绩',
  `rsco_ori_user_id` varchar(255) DEFAULT NULL,
  `rsco_ori_cour_id` varchar(255) DEFAULT NULL,
  `rsco_user_depaName` varchar(255) DEFAULT NULL COMMENT '选课人所属学校名称',
  `rsco_class_name` varchar(255) DEFAULT NULL COMMENT '选课人所属班级名称',
  `rsco_grade_id` int(10) DEFAULT NULL COMMENT '选课人所属年级id',
  `rsco_grade_name` varchar(255) DEFAULT NULL COMMENT '选课人所属年级名称',
  `rsco_valid` int(11) unsigned zerofill DEFAULT '00000000001' COMMENT '选课是否有效，1表示有效，2表示无效（已退课）',
  `rsco_confirmId` int(11) DEFAULT NULL,
  PRIMARY KEY (`rsco_id`),
  KEY `fk_User_has_Course_Course1` (`rsco_cour_id`),
  KEY `fk_User_has_Course_User1` (`rsco_user_id`)
) ENGINE=MyISAM AUTO_INCREMENT=184296 DEFAULT CHARSET=gbk;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Re_SelectCource`
--

LOCK TABLES `Re_SelectCource` WRITE;
/*!40000 ALTER TABLE `Re_SelectCource` DISABLE KEYS */;
/*!40000 ALTER TABLE `Re_SelectCource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Re_UserRole`
--

DROP TABLE IF EXISTS `Re_UserRole`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Re_UserRole` (
  `rero_id` int(11) NOT NULL AUTO_INCREMENT,
  `rero_user_id` int(11) NOT NULL COMMENT '用户id',
  `rero_role_id` int(11) NOT NULL COMMENT '角色id',
  `rero_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '角色分配时间',
  PRIMARY KEY (`rero_id`),
  KEY `fk_user_has_role_role1` (`rero_role_id`),
  KEY `fk_user_has_role_user1` (`rero_user_id`)
) ENGINE=MyISAM AUTO_INCREMENT=95983 DEFAULT CHARSET=gbk COMMENT='用户角色关联表。对用户指定角色，就相当于给用户分配相应的权限。';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Re_UserRole`
--

LOCK TABLES `Re_UserRole` WRITE;
/*!40000 ALTER TABLE `Re_UserRole` DISABLE KEYS */;
INSERT INTO `Re_UserRole` VALUES (70239,150639,1,'2014-01-14 08:28:30'),(81860,151193,5,'2014-09-23 12:31:45');
/*!40000 ALTER TABLE `Re_UserRole` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Resource`
--

DROP TABLE IF EXISTS `Resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Resource` (
  `reso_id` int(11) NOT NULL AUTO_INCREMENT,
  `reso_cour_id` int(11) NOT NULL,
  `reso_type` int(11) NOT NULL COMMENT '资源类型，1是链接，2是文档，3是视频',
  `reso_title` varchar(255) DEFAULT NULL COMMENT '资源标题',
  `reso_location` varchar(1000) NOT NULL COMMENT '资源位置，如果视频或者文档资源，则为相应视频文件或者pdf文件的存放路径；若果为连接文件，则为具体的连接',
  `reso_filename` varchar(255) DEFAULT NULL COMMENT '资源文件名称，链接资源无此属性',
  `reso_describe` text COMMENT '资源描述',
  `reso_userid` int(11) NOT NULL COMMENT '资源上传者id',
  `reso_addtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '资源上传时间',
  `reso_downnum` int(11) DEFAULT NULL COMMENT '附件下载数',
  `reso_share` tinyint(1) DEFAULT NULL COMMENT '标识资源是否共享,即能否被其他课程使用.默认不共享.',
  `reso_vedioTime` bigint(11) DEFAULT NULL COMMENT '视频时长（单位为分钟），只针对视频资源',
  `reso_status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '1 正常状态；0 审核状态；2 删除状态',
  PRIMARY KEY (`reso_id`)
) ENGINE=MyISAM AUTO_INCREMENT=272 DEFAULT CHARSET=gbk COMMENT='资源表，所有资源均和课程进行多对一关联。';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Resource`
--

LOCK TABLES `Resource` WRITE;
/*!40000 ALTER TABLE `Resource` DISABLE KEYS */;
/*!40000 ALTER TABLE `Resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Role`
--

DROP TABLE IF EXISTS `Role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) NOT NULL COMMENT '角色名',
  `role_descirbe` text COMMENT '角色描述，主要是相应权限描述。',
  PRIMARY KEY (`role_id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=gbk;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Role`
--

LOCK TABLES `Role` WRITE;
/*!40000 ALTER TABLE `Role` DISABLE KEYS */;
INSERT INTO `Role` VALUES (1,'学生','学生权限模板'),(2,'教师','教师权限模板'),(3,'教务员','教务员权限模板'),(4,'管理员','管理员权限模板'),(5,'超级管理员','超级管理员');
/*!40000 ALTER TABLE `Role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ScoreExportConfirm`
--

DROP TABLE IF EXISTS `ScoreExportConfirm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ScoreExportConfirm` (
  `seco_id` int(11) NOT NULL AUTO_INCREMENT,
  `seco_export_userId` int(11) NOT NULL COMMENT '导出人id',
  `seco_export_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '导出时间',
  `seco_export_attachId` int(11) NOT NULL COMMENT '导出文件在附件表中的id',
  `seco_confirm_userId` int(11) DEFAULT NULL COMMENT '确认人id',
  `seco_confirm_time` timestamp NULL DEFAULT NULL COMMENT '确认时间',
  `seco_confirm_attachId` int(11) DEFAULT NULL COMMENT '确认的文件在附件表中的id',
  `seco_confirm_remark` text COMMENT '确认时备注信息',
  `seco_result_attachId` int(11) DEFAULT NULL COMMENT '确认后的结果文件在附件表的id',
  PRIMARY KEY (`seco_id`)
) ENGINE=MyISAM AUTO_INCREMENT=50 DEFAULT CHARSET=gbk;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ScoreExportConfirm`
--

LOCK TABLES `ScoreExportConfirm` WRITE;
/*!40000 ALTER TABLE `ScoreExportConfirm` DISABLE KEYS */;
/*!40000 ALTER TABLE `ScoreExportConfirm` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Solve`
--

DROP TABLE IF EXISTS `Solve`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Solve` (
  `solv_id` int(11) NOT NULL AUTO_INCREMENT,
  `solv_rate_id` int(11) NOT NULL COMMENT '测试id',
  `solv_ordinal` int(11) NOT NULL COMMENT '试题序号',
  `solv_answer` text NOT NULL COMMENT '试题答案。选择题则为选项id；其他题为答案字符串。',
  `solv_score` float DEFAULT NULL COMMENT '得分，客观题自动评分，主观题由老师打分。',
  PRIMARY KEY (`solv_id`),
  KEY `fk_Solve_Re_AutoTest1` (`solv_rate_id`)
) ENGINE=MyISAM AUTO_INCREMENT=34835 DEFAULT CHARSET=gbk COMMENT='测试答案表,主要记录学生每次测试提交的答案。';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Solve`
--

LOCK TABLES `Solve` WRITE;
/*!40000 ALTER TABLE `Solve` DISABLE KEYS */;
/*!40000 ALTER TABLE `Solve` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `StudentMassedLearning`
--

DROP TABLE IF EXISTS `StudentMassedLearning`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `StudentMassedLearning` (
  `smle_id` int(11) NOT NULL AUTO_INCREMENT,
  `smle_user_id` int(11) NOT NULL COMMENT '学生id',
  `smle_male_id` int(11) NOT NULL COMMENT '集中学习id',
  PRIMARY KEY (`smle_id`),
  KEY `fk_User_has_MassedLearning_MassedLearning1` (`smle_male_id`),
  KEY `fk_User_has_MassedLearning_User1` (`smle_user_id`)
) ENGINE=MyISAM AUTO_INCREMENT=47 DEFAULT CHARSET=gbk COMMENT='学生的集中学习记录表，记录学生参与集中学习的情况。';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `StudentMassedLearning`
--

LOCK TABLES `StudentMassedLearning` WRITE;
/*!40000 ALTER TABLE `StudentMassedLearning` DISABLE KEYS */;
/*!40000 ALTER TABLE `StudentMassedLearning` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SystemParameter`
--

DROP TABLE IF EXISTS `SystemParameter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SystemParameter` (
  `sypa_id` int(11) NOT NULL AUTO_INCREMENT,
  `sypa_name` varchar(255) NOT NULL COMMENT '参数名称',
  `sypa_en_name` varchar(255) NOT NULL DEFAULT '' COMMENT '参数英文名',
  `sypa_value` text NOT NULL COMMENT '参数值',
  `sypa_type` int(11) DEFAULT NULL COMMENT '1表示学习中心，2表示论坛交流，3系统设置，10为友情链接',
  `sypa_remark` text COMMENT '备注,即参数的解释说明',
  `sypa_order` int(11) DEFAULT NULL,
  PRIMARY KEY (`sypa_id`)
) ENGINE=MyISAM AUTO_INCREMENT=46 DEFAULT CHARSET=gbk COMMENT='系统参数';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SystemParameter`
--

LOCK TABLES `SystemParameter` WRITE;
/*!40000 ALTER TABLE `SystemParameter` DISABLE KEYS */;
INSERT INTO `SystemParameter` VALUES (1,'每天最长学习时间','dayLearnMaxTime','180',1,'*设置每天最长学习时间，单位：分钟',1),(2,'集中时间上限','mlmaMaxTime','60',1,'*设置集中学习时间上限，单位：分钟',2),(4,'集中学习人数上限','malaMaxStudentNum','100',1,'*设置集中学习人数上限，单位：人',3),(5,'建议学习模块','studyModule','素质;科学;历史;语文',1,'*设置建议学习模块，注意：以\";\"分隔',9),(8,'建议学分','suggestCredit','6',1,'*设置建议学分，单位：分（整数）',5),(9,'选课最高学分','courseMaxCredit','10',1,'*设置选课最高学分，单位：分（整数 ）',6),(11,'论坛敏感词','sensitiveWords','江泽民;胡锦涛;温家宝;习近平',2,'*设置论坛敏感词，注意：以\";\"分隔每个词语',12),(12,'论坛帖子最小长度','minWordsNum','5',2,'*设置论坛帖子最小长度，单位：字',9),(13,'图片时延','carouselTime','3000',3,'*顶部图片更换的时间，1秒＝1000毫秒',16),(14,'发帖间隔时间','postInterval','10',2,'*设置发帖间隔时间，单位：秒',10),(15,'附件格式','fileFormats','txt;doc;xls;pdf;exe;jpg',2,'*设置附件格式，注意：以“;”分隔每种格式',13),(16,'附件大小','fileSizeLimit','1',2,'*设置附件大小，单位：兆',11),(17,'是否开放注册','register','否',3,'*设置是否开放用户注册',14),(18,'是否开放作弊控制','cheatingControl','否',1,'*设置是否开放作弊控制',7),(19,'系统名称','systemName','绵阳市普通高中选修课网络学习平台',3,'*设置页眉信息，注意：长度不超过20个字',17),(20,'版权信息','copyright','主办单位：绵阳市教育体育局<br/>承办单位：绵阳市教育体育局基础教育科&绵阳市教育科学研究所&绵阳市电化教育馆<br/>平台开发与技术支持：绵阳网科教育科技研究院<br/>\n蜀ICP备15004079号<br/>',3,'*设置页脚信息，注意，长度不宜过长&lt;br&gt; 标签用于换行',21),(21,'考试须知','examInfo','<p>第一条学生必须提前十分钟进入指定考场，迟到十分钟以上者不得进入考场，作旷考论处。</p><p>第二条学生进入考场后按指定座位就坐，并将学生证或身份证放在考桌上，以便监考教师检查。学生在考场内不得随意走动，发卷后即不得相互交谈和观望他人试卷。</p><p>第三条学生可携带必要的钢笔(园珠笔、铅笔)、橡皮、直尺、绘图仪器、一般功能计算器进入考场，但不得互相借用。其他物品，一律不准带入考场。</p><p>第四条没有交卷的学生，未经监考教师的同意，中途不得离开考场。学生在考试结束时，应立即停止答卷并将试卷折好后放在桌上，由监考教师收卷后离开考场，不得拒交。中途完成答卷，需提前离开考场的学生，应举手向监考教师示意，待监考教师收卷后，即离开考场。</p><p>第五条除试卷分发错误和字迹模糊等问题，可举手询问外，学生对试卷有疑问时，不得向监考教师询问。</p><p>第六条答题均须用钢笔或园珠笔书写，字迹要工整、清楚。</p><p>第七条有作弊等行为的学生，监考教师应终止其考试，令其退出考场，并作相应记录。及时将情况报告巡视员及有关系进行纪律处分。</p><p>第八条规定考试时间，未经同意不得延长。</p>',4,'*在线考试相关事项，以及相关规则',20),(22,'缓存页数','cachePage','3',3,'*一般设置为（3-4）设置分页缓存页数',18),(23,'每页显示条数','recordPerPage','10',3,'*对于分页数据，每页数据条数',19),(37,'绵阳教育体育网','link','www.my-edu.net/',10,'1',NULL),(38,'首页介绍信息','welcomeMsg','<img src=\"../img/welcomemsg.jpg\" />\n请使用<a href=\"http://www.google.cn/intl/zh-CN/chrome/\">谷歌浏览器（Chrome）</a>、<a href=\"http://firefox.com.cn/\">火狐浏览器（Firefox）</a>、<a href=\"http://se.360.cn/\">360安全浏览器（V6）</a>和<a href=\"http://www.microsoft.com/zh-cn/download/internet-explorer-8-details.aspx\">IE8</a>浏览本站。',3,'*首页课程介绍信息，用于显示在首页。',22),(42,'防作弊提示时间间隔','CheatingInterval','10',1,'*防作弊提示时间间隔，单位:分钟',8),(43,'开启唯一性登录','uniqueLogin','0',5,'*同一帐号在同一时刻只能登录一次，第二次登录时提示已登录',NULL),(44,'首页主讲教师是否显示','isShowTeacher','否',3,'*是否在主页显示主讲教师',NULL),(45,'视频记录时间间隔','videoInterval','5',1,'*设置记录学生视频学习时间的间隔，0表示一次性记录所有视频时间，单位为分钟',10);
/*!40000 ALTER TABLE `SystemParameter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_depa_id` int(11) DEFAULT NULL,
  `user_clas_id` int(11) DEFAULT NULL,
  `user_loginname` varchar(255) NOT NULL COMMENT '登录名，一般为学号、身份证号或者邮箱(学生采用学号)',
  `user_pwd` varchar(255) NOT NULL COMMENT '登陆密码',
  `user_realname` varchar(255) NOT NULL COMMENT '姓名',
  `user_gender` varchar(255) NOT NULL COMMENT '性别',
  `user_type` int(11) DEFAULT NULL COMMENT '用户身份，标识当前用户是学生(1)、教师(2)、教务管理员(3)还是系统管理员(4)',
  `user_year_of_entrance` varchar(255) DEFAULT NULL COMMENT '入学年份',
  `user_cadas_exam_num` varchar(255) DEFAULT NULL COMMENT '考籍号',
  `user_id_num` varchar(255) DEFAULT NULL COMMENT '身份证号',
  `user_email` varchar(255) DEFAULT NULL COMMENT '电子邮箱',
  `user_phone_num` varchar(45) DEFAULT NULL COMMENT '联系电话',
  `user_address` mediumtext COMMENT '通讯地址',
  `user_edu_level` varchar(255) DEFAULT NULL COMMENT '学历层次',
  `user_workunit` varchar(255) DEFAULT NULL COMMENT '工作或者学习单位',
  `user_verify` tinyint(1) NOT NULL COMMENT '标识是否激活',
  `user_remark` mediumtext COMMENT '备注',
  `user_loginState` int(10) unsigned DEFAULT '0' COMMENT '登录状态,0表示未登录,1表示已登录',
  `user_loginNum` int(10) unsigned DEFAULT '0' COMMENT '登录次数',
  `user_photoId` int(11) DEFAULT NULL COMMENT '用户头像在附件表中的id',
  `user_ori_id` varchar(255) DEFAULT NULL COMMENT '原始id，导数据用',
  `user_ori_classInfo` varchar(255) DEFAULT NULL COMMENT '用户原来的班级信息，导入数据时使用',
  `user_coverPictureId` int(11) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `oriId` (`user_ori_id`) USING BTREE,
  KEY `fk_user_department` (`user_depa_id`),
  KEY `fk_User_Class1` (`user_clas_id`)
) ENGINE=MyISAM AUTO_INCREMENT=176376 DEFAULT CHARSET=gbk COMMENT='用户表，包含学生、教师、教务管理员和系统';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (150639,34,34,'test','b59c67bf196a4758191e42f76670ceba','邢丹阳','女',1,'2010-09-01','','121743782112345124','1234567890@q.com','1234到底','',NULL,NULL,1,'',0,673,NULL,'84F5B33C-71AF-4EFC-8975-5E73AEC7FD7E','一班',404),(151193,12,NULL,'admin','b59c67bf196a4758191e42f76670ceba','测试系统管理员','女',4,NULL,NULL,'111111111111111','','','',NULL,NULL,1,'',1,0,NULL,NULL,NULL,396);
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `VedioServer`
--

DROP TABLE IF EXISTS `VedioServer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `VedioServer` (
  `vese_id` int(11) NOT NULL AUTO_INCREMENT,
  `vese_name` varchar(255) NOT NULL COMMENT '地址名称',
  `vese_location` varchar(255) NOT NULL COMMENT '访问路径',
  `vese_remark` text COMMENT '备注信息',
  `vese_depa_id` int(11) NOT NULL COMMENT '视频服务地址所属的部门id',
  `vese_type` int(11) NOT NULL COMMENT '区分主从服务器地址,1表示文件同步主服务器，2表示视频播放(兼文件同步)服务器,3表示视频播放一级从服务器,4表示视频播放二级从服务器',
  `vese_state` int(11) NOT NULL COMMENT '服务器状态,1表示正常，0表示故障',
  `vese_enable` tinyint(4) DEFAULT '1' COMMENT '标识是否正被使用,1表示使用,0表示未使用',
  `vese_loginName` varchar(255) DEFAULT NULL,
  `vese_password` varchar(255) DEFAULT NULL,
  `vese_innerIp` varchar(255) NOT NULL COMMENT '服务器IP地址：提供视频服务的IP地址',
  `vese_outerIp` varchar(255) NOT NULL COMMENT '客户端IP/客户网络NAT网关IP：客户端采用路由方式访问视频服务器时，为客户端网络地址；当客户端采用NAT访问视频服务器时，则为NAT网关地址；',
  `vese_innerMask` varchar(255) NOT NULL,
  `vese_outerMask` varchar(255) NOT NULL,
  PRIMARY KEY (`vese_id`),
  KEY `fk_VedioUrl_Department1` (`vese_depa_id`)
) ENGINE=MyISAM AUTO_INCREMENT=20 DEFAULT CHARSET=gbk COMMENT='视频服务器地址表，保存了视频服务的所有可用地址';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `VedioServer`
--

LOCK TABLES `VedioServer` WRITE;
/*!40000 ALTER TABLE `VedioServer` DISABLE KEYS */;
/*!40000 ALTER TABLE `VedioServer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log`
--

DROP TABLE IF EXISTS `log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT,
  `log_user_id` int(11) NOT NULL COMMENT '管理员id',
  `log_opt` text NOT NULL COMMENT '操作内容',
  `log_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `log_ip` varchar(255) DEFAULT NULL COMMENT '访问者Ip地址，主要针对学生日志',
  `log_client` varchar(255) DEFAULT NULL COMMENT '访问者的客户端类型，比如火狐，IE等',
  `log_type` int(11) NOT NULL COMMENT '日志类型，1表示管理员日志，2表示学生日志',
  PRIMARY KEY (`log_id`)
) ENGINE=MyISAM AUTO_INCREMENT=40938 DEFAULT CHARSET=gbk;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log`
--

LOCK TABLES `log` WRITE;
/*!40000 ALTER TABLE `log` DISABLE KEYS */;
/*!40000 ALTER TABLE `log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_rate`
--

DROP TABLE IF EXISTS `user_rate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_rate` (
  `user` int(11) NOT NULL,
  `course` int(11) NOT NULL,
  `rate` double(5,0) NOT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user`,`course`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_rate`
--

LOCK TABLES `user_rate` WRITE;
/*!40000 ALTER TABLE `user_rate` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_rate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_recom`
--

DROP TABLE IF EXISTS `user_recom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_recom` (
  `user` int(11) NOT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `recom` varchar(300) NOT NULL,
  PRIMARY KEY (`user`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_recom`
--

LOCK TABLES `user_recom` WRITE;
/*!40000 ALTER TABLE `user_recom` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_recom` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-03-31 10:20:40
