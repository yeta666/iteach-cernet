package com.swust.kelab.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.swust.kelab.repos.WebTestDao;

@Service
public class WebTestService {
    @Resource
    private WebTestDao webTestDao;

    public String query() {
        return webTestDao.query();
    }
}
