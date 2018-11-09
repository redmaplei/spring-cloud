package com.wys.servicefeign;

import org.springframework.stereotype.Component;

@Component
public class SchedualServiceHiHystric implements SchedualServiceHi {

    @Override
    public String sayHiFromOneClient(String name) {
        return "sorry feign "+name;
    }
}
