package com.swust.kelab.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swust.kelab.domain.VedioServer;
import com.swust.kelab.repos.VedioServerDAO;

/**
 * 视频服务器Service层<br>
 * @author EasonLian
 */
@Service()
public class VedioServerService {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private VedioServerDAO vedioServerDAO;
	
	/**
	 * 通过登录用户的学校信息查询该学校的视频服务器地址<br>
	 * @param schoolName
	 * @return 服务器地址数组
	 */
	@Transactional()
	public List<String> viewLocalVedioServerList(String schoolName) {
		try {
			List<VedioServer> veseList = vedioServerDAO.viewLocalVedioServerList(schoolName);
			List<String> vedioAddList = new ArrayList<String>();
			Iterator<VedioServer> it = veseList.iterator();
			while(it.hasNext()) {
				VedioServer vese = it.next();
				vedioAddList.add("rtmp://"+vese.getVeseInnerIp()+vese.getVeseLocation());
			}
			return vedioAddList;
		} catch(Exception e) {
			logger.error("viewLocalVedioServerList ERROR!");
			e.printStackTrace();
			return null;
		}
	}
}
