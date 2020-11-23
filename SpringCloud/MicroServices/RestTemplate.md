# RestTemplate

## [#](#构建)构建

新建`cloud-demo-consumer-user`

pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>cloud-demo</artifactId>
        <groupId>com.yailjj</groupId>
        <version>1.0.0</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>cloud-demo-consumer-user</artifactId>
    <dependencies>
        <!--eureka 客户端 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <!-- SpringBoot Web容器 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!--api模块-->
        <dependency>
            <groupId>com.yailjj</groupId>
            <artifactId>cloud-demo-api</artifactId>
            <version>${project.version}</version>
        </dependency>
        <!-- spring-boot-devtools -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <optional>true</optional> <!-- 表示依赖不会传递 -->
        </dependency>
    </dependencies>
    <build>
        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <filtering>true</filtering>
            </resource>
        </resources>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-resources-plugin</artifactId>
                <configuration>
                    <delimiters>@</delimiters>
                    <useDefaultDelimiters>false</useDefaultDelimiters>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <fork>true</fork> <!-- 如果没有该配置，devtools不会生效 -->
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

application.yml

```yaml
server:
  port: 80
  
eureka:
  client:
    register-with-eureka: false
    service-url: 
      defaultZone: http://eureka7001.com:7001/eureka/
```

ConsumerUserController

```java
package com.yailjj.cloud.demo.controller;

import java.util.List;

import com.yailjj.cloud.demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("user")
public class ConsumerUserController
{
    private static final String REST_URL_PREFIX = "http://localhost:8001";

    /**
     * 使用 使用restTemplate访问restful接口非常的简单粗暴无脑。 (url, requestMap,
     * ResponseBean.class)这三个参数分别代表 REST请求地址、请求参数、HTTP响应转换被转换成的对象类型。
     */
    @Autowired
    private RestTemplate        restTemplate;

    @RequestMapping(value = "add")
    public boolean add(User user)
    {
        return restTemplate.postForObject(REST_URL_PREFIX + "/user/add", user, Boolean.class);
    }

    @RequestMapping(value = "get/{id}")
    public User get(@PathVariable("id") Long id)
    {
        return restTemplate.getForObject(REST_URL_PREFIX + "/user/get/" + id, User.class);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "list")
    public List<User> list()
    {
        return restTemplate.getForObject(REST_URL_PREFIX + "/user/list", List.class);
    }

    // 测试@EnableDiscoveryClient,消费端可以调用服务发现
    @RequestMapping(value = "discovery")
    public Object discovery()
    {
        return restTemplate.getForObject(REST_URL_PREFIX + "/user/discovery", Object.class);
    }
}
```

ConfigBean

```java
package com.yailjj.cloud.demo.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ConfigBean
{
    @Bean
    public RestTemplate getRestTemplate()
    {
        return new RestTemplate();
    }
}
```

CloudDemoConsumerUserApplication

```java
package com.yailjj.cloud.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class CloudDemoConsumerUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudDemoConsumerUserApplication.class, args);
    }

}

```

访问 127.0.0.1/user/list 查看效果