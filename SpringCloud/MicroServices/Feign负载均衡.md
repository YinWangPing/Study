# Feign负载均衡

## [#](#是什么)是什么

Feign是Netflix开发的声明式、模板化的HTTP客户端， Feign可以帮助我们更快捷、优雅地调用HTTP API。

在Spring Cloud中，使用Feign非常简单——创建一个接口，并在接口上添加一些注解，代码就完成了。Feign支持多种注解，例如Feign自带的注解或者JAX-RS注解等。

Spring Cloud对Feign进行了增强，使Feign支持了Spring MVC注解，并整合了Ribbon和Eureka，从而让Feign的使用更加方便。

Spring Cloud Feign是基于Netflix feign实现，整合了Spring Cloud Ribbon和Spring Cloud Hystrix，除了提供这两者的强大功能外，还提供了一种声明式的Web服务客户端定义的方式。

Spring Cloud Feign帮助我们定义和实现依赖服务接口的定义。在Spring Cloud feign的实现下，只需要创建一个接口并用注解方式配置它，即可完成服务提供方的接口绑定，简化了在使用Spring Cloud Ribbon时自行封装服务调用客户端的开发量。

Spring Cloud Feign具备可插拔的注解支持，支持Feign注解、JAX-RS注解和Spring MVC的注解。

## [#](#怎么用)怎么用

1. api添加依赖

   ```xml
   	   <!-- Fegin相关 -->
           <dependency>
               <groupId>org.springframework.cloud</groupId>
               <artifactId>spring-cloud-starter-openfeign</artifactId>
           </dependency>
   ```

2. 新建RemoteUserService

   ```java
   package com.yailjj.cloud.demo.feign;
   
   import java.util.List;
   
   import org.springframework.cloud.openfeign.FeignClient;
   import org.springframework.web.bind.annotation.GetMapping;
   import org.springframework.web.bind.annotation.PathVariable;
   
   import com.wind.cloud.zero.domain.User;
   
   @FeignClient(value = "CLOUD-ZERO-USER")
   public interface RemoteUserService
   {
       @GetMapping("user/get/{id}")
       public User get(@PathVariable(value = "id") Integer id);
   
       @GetMapping("user/list")
       public List<User> list();
   }
   ```

3. 消费者修改Controller

   ```java
       @Autowired
       private RemoteUserService   remoteUserService;
   
       @RequestMapping(value = "get/{id}")
       public User get(@PathVariable("id") Integer id)
       {
           return remoteUserService.get(id);
       }
   
       @RequestMapping(value = "list")
       public List<User> list()
       {
           return remoteUserService.list();
       }
   ```

4. 启动器去掉@RibbonClient注解，去掉ribbon的作用

   注解新增@EnableFeignClients

   ```java
   @EnableFeignClients("com.wind.cloud.zero")
   ```

5. 启动1个eureka,2个prvider，1个consumer测试轮询效果，feign自带负载均衡

Feign通过接口的方法调用Rest服务（之前是Ribbon+RestTemplate），该请求发送给Eureka服务器（http://127.0.0.1/user/list）,通过Feign直接找到服务接口，由于在进行服务调用的时候融合了Ribbon技术，所以也支持负载均衡作用。