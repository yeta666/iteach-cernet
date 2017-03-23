package com.swust.kelab.web.adapter;

import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.swust.kelab.model.CommonQuery;
import com.swust.kelab.model.MassedLearningModel;

public class MassedLearningModelResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		if (MassedLearningModel.class.isAssignableFrom(parameter.getParameterType())) {
			return true;
		}
		return false;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		MassedLearningModel m = new MassedLearningModel();
		if(null != webRequest.getParameter("maleCourseId"))
			m.setMaleCourseId(Integer.valueOf(webRequest.getParameter("maleCourseId")));
		if(null != webRequest.getParameter("maleDuration"))
			m.setMaleDuration(Integer.valueOf(webRequest.getParameter("maleDuration")));
		if(null != webRequest.getParameter("maleSchoolId"))
			m.setMaleSchoolId(Integer.valueOf(webRequest.getParameter("maleSchoolId")));
		if(null != webRequest.getParameter("maleResouseId"))
			m.setMaleResouseId(Integer.valueOf(webRequest.getParameter("maleResouseId")));
		m.setMaleName(webRequest.getParameter("maleName"));
		if(null != webRequest.getParameter("maleTeacherId"))
			m.setMaleTeacherId(Integer.valueOf(webRequest.getParameter("maleTeacherId")));
		if(webRequest.getParameter("maleState")!=null)
			m.setMaleState(Integer.valueOf(webRequest.getParameter("maleState")));

		// 开始时间
		String startTime = webRequest.getParameter("maleStartDate");
		try {
			if (StringUtils.isNotBlank(startTime)) {
				m.setMaleStartDate(DateUtils.parseDate(startTime, "yyyy-MM-dd HH:MM:SS"));
			}
		} catch (ParseException e) {
			return null;
		}
		return m;
	}
}
