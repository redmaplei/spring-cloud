package com.wys.servicefeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * client一改了端口号之后，就要重新启动Service-feign服务
 *  * 不然只能找到前面发现的端口号的服务
 * 是根据服务名字来找，不支持消费的服务动态的端口号修改
 */
@FeignClient(value = "client")
public interface SchedualServiceHi {

    @RequestMapping(value = "/hi", method = RequestMethod.GET)
    String sayHiFromOneClient(@RequestParam(value = "name") String name);


}
