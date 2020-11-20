# Eureka注册中心

springcloud各微服务之间是通过注册中心互相调用的，无论是eureka还是nacos,或者其他注册中心,下面是一张对比图

| Feature              | Consul                 | zookeeper             | etcd              | euerka                       |
| -------------------- | ---------------------- | --------------------- | ----------------- | ---------------------------- |
| 服务健康检查         | 服务状态，内存，硬盘等 | (弱)长连接，keepalive | 连接心跳          | 可配支持                     |
| 多数据中心           | 支持                   | —                     | —                 | —                            |
| kv存储服务           | 支持                   | 支持                  | 支持              | —                            |
| 一致性               | raft                   | paxos                 | raft              | —                            |
| cap                  | ca                     | cp                    | cp                | ap                           |
| 使用接口(多语言能力) | 支持http和dns          | 客户端                | http/grpc         | http（sidecar）              |
| watch支持            | 全量/支持long polling  | 支持                  | 支持 long polling | 支持 long polling/大部分增量 |
| 自身监控             | metrics                | —                     | metrics           | metrics                      |
| 安全                 | acl /https             | acl                   | https支持（弱）   | —                            |
| spring cloud集成     | 已支持                 | 已支持                | 已支持            | 已支持                       |

## [#](#cap原则)CAP原则

CAP原则又称CAP定理，指的是在一个分布式系统中，Consistency（一致性）、 Availability（可用性）、Partition tolerance（分区容错性），三者不可兼得

eureka遵循ap原则

Spring Cloud 封装了 Netflix 公司开发的 Eureka 模块来实现服务注册和发现(请对比Zookeeper)。

Eureka 采用了 C-S 的设计架构。Eureka Server 作为服务注册功能的服务器，它是服务注册中心。

而系统中的其他微服务，使用 Eureka 的客户端连接到 Eureka Server并维持心跳连接。这样系统的维护人员就可以通过 Eureka Server 来监控系统中各个微服务是否正常运行。SpringCloud 的一些其他模块（比如Zuul）就可以通过 Eureka Server 来发现系统中的其他微服务，并执行相关的逻辑。

Eureka包含两个组件：Eureka Server和Eureka Client Eureka Server提供服务注册服务 各个节点启动后，会在EurekaServer中进行注册，这样EurekaServer中的服务注册表中将会存储所有可用服务节点的信息，服务节点的信息可以在界面中直观的看到

EurekaClient是一个Java客户端，用于简化Eureka Server的交互，客户端同时也具备一个内置的、使用轮询(round-robin)负载算法的负载均衡器。在应用启动后，将会向Eureka Server发送心跳(默认周期为30秒)。如果Eureka Server在多个心跳周期内没有接收到某个节点的心跳，EurekaServer将会从服务注册表中把这个服务节点移除（默认90秒）

## [#](#和dubbo对比)和dubbo对比

![eureka](.\picture\eureka.png)

![dubbo](.\picture\dubbo.png)

## [#](#构建)构建

1. 新建模块`cloud-demo-eureka`

2. 加入依赖

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <project xmlns="http://maven.apache.org/POM/4.0.0"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
       <parent>
           <artifactId>cloud-zero</artifactId>
           <groupId>com.wind</groupId>
           <version>1.0.0</version>
       </parent>
       <modelVersion>4.0.0</modelVersion>
   
       <artifactId>cloud-zero-eureka</artifactId>
       <dependencies>
           <!--服务中心 -->
           <dependency>
               <groupId>org.springframework.cloud</groupId>
               <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
           </dependency>
       </dependencies>
   
   </project>
   ```

3. 新建启动器

   ```java
   package com.wind.cloud.zero;
   
   import org.springframework.boot.SpringApplication;
   import org.springframework.boot.autoconfigure.SpringBootApplication;
   import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
   
   @SpringBootApplication
   @EnableEurekaServer // EurekaServer服务器端启动类,接受其它微服务注册进来
   public class EurekaApp
   {
       public static void main(String[] args)
       {
           SpringApplication.run(EurekaApp.class);
       }
   }
   ```

4. 新建`application.yml`

   ```yaml
   server: 
     port: 7001
    
   eureka: 
     instance:
       hostname: eureka7001.com #eureka服务端的实例名称
     client: 
       register-with-eureka: false     #false表示不向注册中心注册自己。
       fetch-registry: false     #false表示自己端就是注册中心，我的职责就是维护服务实例，并不需要去检索服务
       service-url: 
         #单机 
         #设置与Eureka Server交互的地址查询服务和注册服务都需要依赖这个地址（单机）。
         defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/       
         #defaultZone: http://eureka7002.com:7002/eureka/,http://eureka7003.com:7003/eureka/
   ```

5. 绑定`host`，绑定是为方便后续调用或者更改，部署也方便很多

   ```text
   127.0.0.1 eureka7001.com
   #集群需要
   127.0.0.1 eureka7002.com
   127.0.0.1 eureka7003.com
   ```

6. provider-user连接注册中心，

   pom.xml

   ```xml
   	    <!--eureka 客户端 -->
           <dependency>
               <groupId>org.springframework.cloud</groupId>
               <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
           </dependency>
   		<!-- actuator监控信息完善 -->
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-actuator</artifactId>
           </dependency>
   
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
           </plugins>
       </build>
   ```

   application.yml添加配置

   ```yaml
   eureka:
     client: # 客户端注册进eureka服务列表内
       service-url:
         defaultZone: http://localhost:7001/eureka
         #defaultZone: http://eureka7001.com:7001/eureka/,http://eureka7002.com:7002/eureka/,http://eureka7003.com:7003/eureka/
     instance:
       instance-id: ${spring.cloud.client.ip-address}:${spring.application.name}:${server.port}
       prefer-ip-address: true     # 访问路径可以显示IP地址
   #后面监控也会用到    
   info:
     app.name: cloud-zero
     company.name: www.zmrit.com
     build.artifactId: @project.artifactId@
     build.version: @project.version@
   ```

   UserController

   ```java
   @Autowired
   private DiscoveryClient client;
   
   @RequestMapping(value = "discovery", method = RequestMethod.GET)
   public Object discovery(){
           List<String> list = client.getServices();
           System.out.println("**********" + list);
           List<ServiceInstance> srvList = client.getInstances("cloud-zero-USER");
           for (ServiceInstance element : srvList)
           {
               System.out.println(element.getServiceId() + "\t" + element.getHost() + "\t" + element.getPort() + "\t"
                       + element.getUri());
           }
           return this.client;
   }
   ```

## [#](#自我保护)自我保护

默认情况下，如果EurekaServer在一定时间内没有接收到某个微服务实例的心跳，EurekaServer将会注销该实例（默认90秒）。但是当网络分区故障发生时，微服务与EurekaServer之间无法正常通信，以上行为可能变得非常危险了——因为微服务本身其实是健康的，此时本不应该注销这个微服务。Eureka通过“自我保护模式”来解决这个问题——当EurekaServer节点在短时间内丢失过多客户端时（可能发生了网络分区故障），那么这个节点就会进入自我保护模式。一旦进入该模式，EurekaServer就会保护服务注册表中的信息，不再删除服务注册表中的数据（也就是不会注销任何微服务）。当网络故障恢复后，该Eureka Server节点会自动退出自我保护模式。

在自我保护模式中，Eureka Server会保护服务注册表中的信息，不再注销任何服务实例。当它收到的心跳数重新恢复到阈值以上时，该Eureka Server节点就会自动退出自我保护模式。它的设计哲学就是宁可保留错误的服务注册信息，也不盲目注销任何可能健康的服务实例。一句话讲解：好死不如赖活着

综上，自我保护模式是一种应对网络异常的安全保护措施。它的架构哲学是宁可同时保留所有微服务（健康的微服务和不健康的微服务都会保留），也不盲目注销任何健康的微服务。使用自我保护模式，可以让Eureka集群更加的健壮、稳定。

在Spring Cloud中，可以使用eureka.server.enable-self-preservation = false 禁用自我保护模式。

## [#](#集群)集群

eureka相互注册，客户端注册到所有集群

```
serve
spring:
  profiles:
    active: 7001
---
spring:
  profiles: 7001

server:
  port: 7001

eureka:
  instance:
    hostname: eureka7001.com #eureka服务端的实例名称
  client:
    register-with-eureka: false     #false表示不向注册中心注册自己。
    fetch-registry: false     #false表示自己端就是注册中心，我的职责就是维护服务实例，并不需要去检索服务
    service-url:
      #单机
      #设置与Eureka Server交互的地址查询服务和注册服务都需要依赖这个地址（单机）。
      defaultZone: http://eureka7002.com:7002/eureka/,http://eureka7003.com:7003/eureka/
---
spring:
  profiles: 7002

server:
  port: 7002

eureka:
  instance:
    hostname: eureka7002.com #eureka服务端的实例名称
  client:
    register-with-eureka: false     #false表示不向注册中心注册自己。
    fetch-registry: false     #false表示自己端就是注册中心，我的职责就是维护服务实例，并不需要去检索服务
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka/,http://eureka7003.com:7003/eureka/

---
spring:
  profiles: 7003

server:
  port: 7003

eureka:
  instance:
    hostname: eureka7003.com #eureka服务端的实例名称
  client:
    register-with-eureka: false     #false表示不向注册中心注册自己。
    fetch-registry: false     #false表示自己端就是注册中心，我的职责就是维护服务实例，并不需要去检索服务
    service-url:
      defaultZone: http://eureka7002.com:7002/eureka/,http://eureka7001.com:7001/eureka/
client
#client
defaultZone: http://eureka7001.com:7001/eureka/,http://eureka7002.com:7002/eureka/,http://eureka7003.com:7003/eureka/
```

演示完毕后改回单机模式，节省性能

## [#](#与zookeeper对比)与Zookeeper对比

作为服务注册中心，Eureka比Zookeeper好在哪里 著名的CAP理论指出，一个分布式系统不可能同时满足C(一致性)、A(可用性)和P(分区容错性)。由于分区容错性P在是分布式系统中必须要保证的，因此我们只能在A和C之间进行权衡。 因此 Zookeeper保证的是CP， Eureka则是AP。

###### [#](#zookeeper保证cp)Zookeeper保证CP

当向注册中心查询服务列表时，我们可以容忍注册中心返回的是几分钟以前的注册信息，但不能接受服务直接down掉不可用。也就是说，服务注册功能对可用性的要求要高于一致性。但是zk会出现这样一种情况，当master节点因为网络故障与其他节点失去联系时，剩余节点会重新进行leader选举。问题在于，选举leader的时间太长，30 ~ 120s, 且选举期间整个zk集群都是不可用的，这就导致在选举期间注册服务瘫痪。在云部署的环境下，因网络问题使得zk集群失去master节点是较大概率会发生的事，虽然服务能够最终恢复，但是漫长的选举时间导致的注册长期不可用是不能容忍的。

###### [#](#eureka保证ap)Eureka保证AP

Eureka看明白了这一点，因此在设计时就优先保证可用性。Eureka各个节点都是平等的，几个节点挂掉不会影响正常节点的工作，剩余的节点依然可以提供注册和查询服务。而Eureka的客户端在向某个Eureka注册或时如果发现连接失败，则会自动切换至其它节点，只要有一台Eureka还在，就能保证注册服务可用(保证可用性)，只不过查到的信息可能不是最新的(不保证强一致性)。除此之外，Eureka还有一种自我保护机制，如果在15分钟内超过85%的节点都没有正常的心跳，那么Eureka就认为客户端与注册中心出现了网络故障，此时会出现以下几种情况：

1. Eureka不再从注册列表中移除因为长时间没收到心跳而应该过期的服务
2. Eureka仍然能够接受新服务的注册和查询请求，但是不会被同步到其它节点上(即保证当前节点依然可用)
3. 当网络稳定时，当前实例新的注册信息会被同步到其它节点中

因此， Eureka可以很好的应对因网络故障导致部分节点失去联系的情况，而不会像zookeeper那样使整个注册服务瘫痪。

## [#](#删除服务)删除服务

1. 直接停掉服务。

   默认情况下，如果Eureka Server在90秒没有收到Eureka客户的续约，它会将实例从其注册表中删除。但这种做法的不好之处在于， 客户端已经停止了运行，但仍然在注册中心的列表中。 虽然通过一定的负载均衡策略或使用熔断器可以让服务正常进行，但有没有方法让注册中心马上知道服务已经下线呢？

2. 为了让注册中心马上知道服务要下线， 可以向eureka 注册中心发送delete 请求

   ```html
   http://${server}:${port}/eureka/apps/${serviceName}/${instanceId}/
   ```

   如`http://eureka7001.com:7001/eureka/apps/cloud-zero-USER/192.168.84.1:cloud-zero-user:8001/`

   **值得注意的是，Eureka客户端每隔一段时间（默认30秒）会发送一次心跳到注册中心续约。如果通过这种方式下线了一个服务，而没有及时停掉的话，该服务很快又会回到服务列表中。**

   所以，可以先停掉服务，再发送请求将其从列表中移除。

3. 客户端主动通知注册中心下线

   如果你的eureka客户端是是一个spring boot应用，可以通过调用以下代码通知注册中心下线。

   DiscoveryManager.getInstance().shutdownComponent();

   例子如下，

   ```java
   @RestController
   public class HelloController {
       @Autowired
       private DiscoveryClient client;
    
       @RequestMapping(value = "/hello", method = RequestMethod.GET)
       public String index() {
           java.util.List<ServiceInstance> instances = client.getInstances("hello-service");       
           return "Hello World";
       }
       
       @RequestMapping(value = "/offline", method = RequestMethod.GET)
       public void offLine(){
       	DiscoveryManager.getInstance().shutdownComponent();
       }   
   }
   ```