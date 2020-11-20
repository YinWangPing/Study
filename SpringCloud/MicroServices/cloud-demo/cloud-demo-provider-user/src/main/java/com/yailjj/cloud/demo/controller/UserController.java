package com.yailjj.cloud.demo.controller;

import com.yailjj.cloud.demo.domain.User;
import com.yailjj.cloud.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController
{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DiscoveryClient client;

    @RequestMapping(value = "discovery", method = RequestMethod.GET)
    public Object discovery(){
        List<String> list = client.getServices();
        System.out.println("**********" + list);
        List<ServiceInstance> srvList = client.getInstances("cloud-demo-USER");
        for (ServiceInstance element : srvList)
        {
            System.out.println(element.getServiceId() + "\t" + element.getHost() + "\t" + element.getPort() + "\t"
                    + element.getUri());
        }
        return this.client;
    }

    @GetMapping("list")
    public List<User> list()
    {
        return userMapper.findByAll(null);
    }

    @GetMapping("get/{id}")
    public User get(@PathVariable Integer id)
    {
        return userMapper.selectByPrimaryKey(id);
    }

    @GetMapping("del/{id}")
    public boolean del(@PathVariable Integer id)
    {
        return userMapper.deleteByPrimaryKey(id) > 0;
    }

    @PostMapping("add")
    public boolean add(@RequestBody User user)
    {
        return userMapper.insert(user) > 0;
    }
}
