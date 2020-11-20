#  Rest服务起手

## [#](#初始化)初始化

新建文件夹 cloud-zero

添加`pom.xml`

```java
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.yailjj</groupId>
    <artifactId>cloud-demo</artifactId>
    <packaging>pom</packaging>
    <version>1.0.0</version>


    <properties>
        <java.version>1.8</java.version>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <spring-boot.version>2.1.7.RELEASE</spring-boot.version>
        <spring-cloud.version>Greenwich.SR2</spring-cloud.version>
        <mybatis.spring.version>1.3.2</mybatis.spring.version>
        <lombok.version>1.18.4</lombok.version>
    </properties>


    <dependencyManagement>
        <dependencies>
            <!-- spring cloud -->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!-- SpringBoot的依赖配置 -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring-boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

            <dependency>
                <groupId>org.mybatis.spring.boot</groupId>
                <artifactId>mybatis-spring-boot-starter</artifactId>
                <version>${mybatis.spring.version}</version>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <!--子模块可以自动继承-->
    <dependencies>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>

    <build>

    </build>

</project>
```

## 构建模块

`springcloud`的单体就是`springboot`，那么先从一个`springboot`项目开始

### [#](#cloud-demo-api)cloud-demo-api

封装的一些模型和公共配置

- pom.xml

  ```java
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
  
      <artifactId>cloud-demo-api</artifactId>
      <packaging>jar</packaging>
  
  </project>
  ```

### [#](#cloud-demo-provider-user)cloud-demo-provider-user

微服务落地提供者 用户

- pom.xml

  ```java
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
  
      <artifactId>cloud-zero-provider-user</artifactId>
      <packaging>jar</packaging>
      <dependencies>
          <dependency>
              <groupId>com.yailjj</groupId>
              <artifactId>cloud-demo-api</artifactId>
              <version>${project.version}</version>
          </dependency>
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-web</artifactId>
          </dependency>
          <dependency>
              <groupId>mysql</groupId>
              <artifactId>mysql-connector-java</artifactId>
          </dependency>
          <dependency>
              <groupId>org.mybatis.spring.boot</groupId>
              <artifactId>mybatis-spring-boot-starter</artifactId>
          </dependency>
      </dependencies>
  </project>
  ```