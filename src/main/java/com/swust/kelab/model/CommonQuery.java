package com.swust.kelab.model;

import java.util.Date;

import com.swust.kelab.repos.bean.GenericQuery;
import com.swust.kelab.repos.bean.ListQuery;

public class CommonQuery {
    private Date startTime;
    private Date endTime;
    private int[] pageArray; // 分页显示时页数
    private int recordPerPage; // 分页显示时每页限制条数
    private Integer searchType;// 搜索字段
    private String searchWord;// 搜索的关键字

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    public Integer getSearchType() {
        return searchType;
    }

    public void setSearchType(Integer searchType) {
        this.searchType = searchType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int[] getPageArray() {
        return pageArray;
    }

    public void setPageArray(int[] pageArray) {
        this.pageArray = pageArray;
    }

    public int getRecordPerPage() {
        return recordPerPage;
    }

    public void setRecordPerPage(int recordPerPage) {
        this.recordPerPage = recordPerPage;
    }

    public ListQuery format() {
        ListQuery query = new GenericQuery();
        if (this.getStartTime() != null) {
            query.fill("startTime", this.getStartTime());
        }
        if (this.getEndTime() != null) {
            query.fill("endTime", this.getEndTime());
        }
        if (this.getSearchType() != null) {
            query.fill("searchType", searchType);
        }
        if (this.getSearchWord() != null && this.getSearchWord()!="") {
            query.fill("searchWord", searchWord);
        }
        return query;
    }

}
