package com.swust.kelab.service;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.net.util.SubnetUtils.SubnetInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.swust.kelab.domain.VedioServer;
import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.PageData;
import com.swust.kelab.model.QueryData;
import com.swust.kelab.model.VideoServerModel;
import com.swust.kelab.repos.VideoServerDAO;
import com.swust.kelab.repos.bean.ListQuery;
import com.swust.kelab.utils.NetWorkConfirmUtil;
import java.util.Collections;

@Service
public class VideoServerService {

	private VideoServerDAO videoServerDAO;

	@Autowired
	public void setVideoServerDAO(VideoServerDAO videoServerDAO) {
		this.videoServerDAO = videoServerDAO;
	}

	/**
	 * 返回服务器列表
	 * 
	 * @author lancer
	 * @return List<VideoServerModel> list
	 */
	public QueryData selectAllServer(CommonQuery commonquery) {

		QueryData queryData = new QueryData();
		// 构造查询条件
		ListQuery query = commonquery.format();
		int totalCount = videoServerDAO.countAllServer(query);
		queryData.setTotalCount(totalCount);
		if (totalCount == 0) {
			return queryData;
		}
		if (commonquery.getRecordPerPage() <= 0) {
			commonquery.setRecordPerPage(10);
		}
		query.fill("maxCount", commonquery.getRecordPerPage());
		int totalPage = QueryData.computeTotalPage(totalCount,
				commonquery.getRecordPerPage());
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
			query.fill(
					"startIndex",
					QueryData.computeStartIndex(page,
							commonquery.getRecordPerPage()));
			List<VideoServerModel> scores = videoServerDAO
					.selectAllServer(query);
			pageDataList.add(new PageData(page, scores));
		}
		// 装载返回结果
		queryData.setPageData(pageDataList);
		return queryData;

	}

	/**
	 * 修改服务器数据
	 * 
	 * @author lancer
	 * @param vedioServer
	 * @throws Exception
	 */
	public void updateServer(VedioServer vedioServer) throws Exception {
		// InetAddress inet=
		// InetAddress.getByName(vedioServer.getVeseOuterIp());
		// int con =
		// NetWorkConfirmUtil.asNetMask(vedioServer.getVeseOuterMask()) ;
		// Map<String,String> map =
		// NetWorkConfirmUtil.asNetWork(inet.toString().split("/")[1]+"/"+con);
		// vedioServer.setVeseLowAddress(map.get("lowAddress"));
		// vedioServer.setVeseHighAddress(map.get("highAddress"));
		videoServerDAO.updateServer(vedioServer);
	}

	/**
	 * 添加服务器数据
	 * 
	 * @author lancer
	 * @param vedioServer
	 * @throws Exception
	 */
	public void insertServer(VedioServer vedioServer) throws Exception {
		// InetAddress inet=
		// InetAddress.getByName(vedioServer.getVeseOuterIp());
		// int con =
		// NetWorkConfirmUtil.asNetMask(vedioServer.getVeseOuterMask()) ;
		// Map<String,String> map =
		// NetWorkConfirmUtil.asNetWork(inet.toString().split("/")[1]+"/"+con);
		// vedioServer.setVeseLowAddress(map.get("lowAddress"));
		// vedioServer.setVeseHighAddress(map.get("highAddress"));
		videoServerDAO.addServer(vedioServer);
	}

	/**
	 * 删除服务器数据
	 * 
	 * @author lancer
	 * @param veseId
	 * @throws Exception
	 */
	public void deleteServer(String[] veseId) throws Exception {
		videoServerDAO.deleteServer(veseId);
	}

	/**
	 * @author lancer
	 * @param veseId
	 * @return
	 */
	public VideoServerModel selectById(int veseId) {
		return videoServerDAO.selectById(veseId);
	}

	/**
	 * @author lancer
	 * @param ip传入
	 * @return
	 * @throws Exception
	 *             域名或IP解析错误
	 */
	@SuppressWarnings("finally")
	public Map<String, Object> selectByOrder(String ip) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> slaver = new ArrayList<String>();
		List<String> master = new ArrayList<String>();
		try {
			for (VideoServerModel vedio : videoServerDAO.selectOrderServer()) {
				if (vedio.getVeseType().equals("1")) {// 判断服务器类型，"1"为主服务器
					master.add(vedio.getVeseInnerIp());
				} else {
					SubnetUtils utils = new SubnetUtils(vedio.getVeseOuterIp(),
							vedio.getVeseOuterMask());
					SubnetInfo info = utils.getInfo();
					if (info.isInRange(ip)) {
						slaver.add(vedio.getVeseInnerIp());
					} else {
						continue;
					}
				}
			}
			Collections.shuffle(slaver);
			Collections.shuffle(master);
			slaver.addAll(master);
			map.put("servers", slaver);
			// map.put("master", master);
		} catch (Exception e) {
			map.put("error", "IP匹配失败！");
		} finally {
			return map;
		}
	}
}
