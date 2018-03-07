package com.taotao.manage.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PropertiesService {

    // 获取env.properties里面的配置信息
    @Value("${REPOSITORY_PATH}")
    public String REPOSITORY_PATH;

    // 获取env.properties里面的配置信息
    @Value("${IMAGE_BASE_URL}")
    public String IMAGE_BASE_URL;

}
