package com.yailjj.cloud.demo.controller;

import java.util.List;

import com.yailjj.cloud.demo.domain.User;
import com.yailjj.cloud.demo.feign.RemoteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("user")
public class ConsumerUserController {
    //    private static final String REST_URL_PREFIX = "http://localhost:8001";
    private static final String REST_URL_PREFIX = "http://CLOUD-DEMO-USER";

    /**
     * 使用 使用restTemplate访问restful接口非常的简单粗暴无脑。 (url, requestMap,
     * ResponseBean.class)这三个参数分别代表 REST请求地址、请求参数、HTTP响应转换被转换成的对象类型。
     */
//    @Autowired
//    private RestTemplate restTemplate;
//
//    @RequestMapping(value = "add")
//    public boolean add(User user) {
//        return restTemplate.postForObject(REST_URL_PREFIX + "/user/add", user, Boolean.class);
//    }
//
//    @RequestMapping(value = "get/{id}")
//    public User get(@PathVariable("id") Long id) {
//        return restTemplate.getForObject(REST_URL_PREFIX + "/user/get/" + id, User.class);
//    }
//
//    @SuppressWarnings("unchecked")
//    @RequestMapping(value = "list")
//    public List<User> list() {
//        return restTemplate.getForObject(REST_URL_PREFIX + "/user/list", List.class);
//    }
//
//    // 测试@EnableDiscoveryClient,消费端可以调用服务发现
//    @RequestMapping(value = "discovery")
//    public Object discovery() {
//        return restTemplate.getForObject(REST_URL_PREFIX + "/user/discovery", Object.class);
//    }
//    ---------------------------------------------------feign
    @Autowired
    private RemoteUserService remoteUserService;
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
}


