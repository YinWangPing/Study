package com.example.ssm_demo.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import static org.springframework.web.cors.CorsConfiguration.ALL;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${uploadFile.resourceHandler}")
    private String resourceHandler;//请求 url 中的资源映射，不推荐写死在代码中，最好提供可配置，如 /uploadFiles/**
    @Value("${uploadFile.location}")
    private String location;//上传文件保存的本地目录，使用@Value获取全局配置文件中配置的属性值，如 E:/uploadFiles/
    /**
     * 配置静态资源映射
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //就是说 url 中出现 resourceHandler 匹配时，则映射到 location 中去,location 相当于虚拟路径
        //映射本地文件时，开头必须是 file:/// 开头，表示协议
        registry.addResourceHandler(resourceHandler).addResourceLocations("file:///" + location);
//        registry.addResourceHandler("/upload/** ").addResourceLocations("file:///" + location);
    }
    /*
     *
     * @param registry
     * @return: void
     * @author: WangPingYin
     * description：前台跨域请求设置
     * @date: 2019/7/31 10:20
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")//设置允许跨域的路径
                .allowedOrigins(ALL)
                .allowedMethods(ALL)
                .allowedHeaders(ALL)
                .allowCredentials(true);
//                .allowedOrigins("*")//设置允许跨域请求的域名
//                .allowCredentials(true)//是否允许证书 不再默认开启
//                .allowedMethods("GET", "POST", "PUT", "DELETE")//设置允许的方法
//                .maxAge(36000);//跨域允许时间
    }
}

