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

public class CommonQueryMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (CommonQuery.class.isAssignableFrom(parameter.getParameterType())) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // 从request中解析参数放入CommonQuery中
        CommonQuery query = new CommonQuery();

        // 查询字段
        String searchType = webRequest.getParameter("searchType");
        if (StringUtils.isNotBlank(searchType)) {
            query.setSearchType(NumberUtils.toInt(searchType));
        }
        // 查询内容
        String searchWord = webRequest.getParameter("searchWord");
        if (StringUtils.isNotBlank(searchWord)) {
            query.setSearchWord(searchWord);
        }

        // 开始时间
        String startTime = webRequest.getParameter("startTime");
        try {
            if (StringUtils.isNotBlank(startTime)) {
                query.setStartTime(DateUtils.parseDate(startTime, "yyyy-MM-dd"));
            }
            // 结束时间
            String endTime = webRequest.getParameter("endTime");
            if (StringUtils.isNotBlank(endTime)) {
                query.setEndTime(DateUtils.parseDate(endTime, "yyyy-MM-dd"));
            }
        } catch (ParseException e) {
            return null;
        }
        // 查询页码
        String pageArrays = webRequest.getParameter("pageArray");
        if (pageArrays != null) {
            String[] pages = StringUtils.split(pageArrays, ",");
            int[] pageArray = new int[pages.length];
            if (pages.length == 0) {
                pageArray[0] = 1;
            } else {
                for (int i = 0; i < pages.length; i++) {
                    pageArray[i] = NumberUtils.toInt(pages[i]);
                }
            }
            query.setPageArray(pageArray);
        }
        // 每页记录条数
        String strRecordPerPage = webRequest.getParameter("recordPerPage");
        if (strRecordPerPage != null) {
            query.setRecordPerPage(NumberUtils.toInt(strRecordPerPage, 10));
        }
        return query;
    }
}
