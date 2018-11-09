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
### 2.ribbon service 负载均衡，调用其他的服务

pom使用官方

```xml
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
        </dependency>

    -------

      <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Finchley.SR1</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
      </dependencyManagement>

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
-----
![ribbon-2](https://images.gitee.com/uploads/images/2018/1108/175706_c8b66335_792824.png "ribbon-2.png")

### 3.feigin service 负载均衡，调用其他的服务

采用的是基于接口的注解

```java
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
```

## 熔断器处理 hystrix

### 4 ribbon 使用hystrix 

```java
@EnableHystrix


@HystrixCommand(fallbackMethod = "hiError")
   public String hiService(String name) {
       return restTemplate.getForObject("http://client/hi?name="+name,String.class);
   }

   public String hiError(String name) {
       return "hi,"+name+",sorry,error!";
   }
```
### 使用

| Application | AMIs | Availability Zones | Status |
|-------|---------------|-----|-----|
| CLIENT      | n/a (2) |   (2)      | UP (2) - 192.168.1.107:client:8002 , 192.168.1.107:client:8001 |

1.client都开  正常使用

2.关闭一个client 8001 让断路器生效
ribbon一样进行负载均衡切换消费服务 
当消费关闭的那个服务时，会起断路器作用
![输入图片说明](https://images.gitee.com/uploads/images/2018/1109/162252_7f379cae_792824.png "Hystrix-1.png")

![输入图片说明](https://images.gitee.com/uploads/images/2018/1109/162305_4671d256_792824.png "Hystrix-2.png")


-----
当关闭了一个client后，调用多几次发现那个服务没了，到后面 再调用的时候，负载均衡看着调用的，还是有可能用到关了的那个的
client注册信息还在的时候，会调用  当没了的时候，好像不调用了
-----


3.都关闭
都是段路了

![输入图片说明](https://images.gitee.com/uploads/images/2018/1109/162404_8f36982d_792824.png "Hystrix-2.png")

没有加有断路器，然后client服务都关闭的时候
是这样的
![输入图片说明](https://images.gitee.com/uploads/images/2018/1109/162851_30f51765_792824.png "client关闭 使用.png")

### 4 feign 使用hystrix
和ribbon 作用差不多
使用有区别

SchedualServiceHi 接口使用
```java

@FeignClient(value = "client", fallback = SchedualServiceHiHystric.class)
public interface SchedualServiceHi {

    @RequestMapping(value = "/hi", method = RequestMethod.GET)
    String sayHiFromOneClient(@RequestParam(value = "name") String name);


}
```
增加一个接口的实现类 用来处理断路器
```java
package com.wys.servicefeign;

import org.springframework.stereotype.Component;

@Component
public class SchedualServiceHiHystric implements SchedualServiceHi {

    @Override
    public String sayHiFromOneClient(String name) {
        return "sorry feign "+name;
    }
}

```

#### 然后client 都开启了后，等Eureka 服务注册中心 把client注册成功了，有注册信息后，负载均衡一样可以正常使用
