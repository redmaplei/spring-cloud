# spring-cloud

#### 项目介绍
使用spring-cloud 
使用Eureka 来注册服务，生产client端
转发路由使用

官方文档和一些博客
http://spring.io/guides
https://www.fangzhipeng.com/archive/?tag=SpringCloud
http://blog.didispace.com/springcloud5/

### 1.eureka 服务的注册与发现
    client名字可以一样
![服务注册](https://images.gitee.com/uploads/images/2018/1108/175558_fb9661f5_792824.png "eureka服务注册.png")
### 2.ribbon service pom使用官方

```xml
    spring-cloud-starter-netflix-eureka-client
    spring-cloud-starter-netflix-ribbon
```

使用的时候根据service名字来使用
```java
      @Autowired
    RestTemplate restTemplate;

    public String hiService(String name) {
        return restTemplate.getForObject("http://client/hi?name="+name,String.class);
    }
```
![ribbon-1](https://images.gitee.com/uploads/images/2018/1108/175654_7f72f113_792824.png "ribbon-1.png")
![ribbon-2](https://images.gitee.com/uploads/images/2018/1108/175706_c8b66335_792824.png "ribbon-2.png")

