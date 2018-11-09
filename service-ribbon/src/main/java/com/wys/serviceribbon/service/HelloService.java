package com.wys.serviceribbon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HelloService {

    @Autowired
    RestTemplate restTemplate;

    /**
     * ribbon 负载均衡
     * 自动去找client服务
     * @param name
     * @return
     */
    public String hiService(String name) {
        return restTemplate.getForObject("http://client/hi?name="+name,String.class);
    }

}
