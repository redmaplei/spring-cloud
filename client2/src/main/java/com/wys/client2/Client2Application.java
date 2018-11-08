package com.wys.client2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@EnableDiscoveryClient
@SpringBootApplication
public class Client2Application {

    public static void main(String[] args) {
        SpringApplication.run(Client2Application.class, args);
    }


}

@RestController
class ClientController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping(value = "/test")
    public List<String> test() {
        return this.discoveryClient.getServices();
    }

    @Value("${server.port}")
    String port;

    @RequestMapping("/hi")
    public String home(String name) {
        return "hi " + name + " ,i am from port:" + port;
    }
}