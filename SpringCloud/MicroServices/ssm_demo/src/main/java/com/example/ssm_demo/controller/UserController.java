package com.example.ssm_demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.ssm_demo.pojo.User;
import com.example.ssm_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired(required = false)
    private UserService userService;
    @RequestMapping(value="getUserById1",method= RequestMethod.POST)
    public User getUserById1(HttpServletRequest request, HttpServletResponse response)throws IOException, SQLException {
        Integer id= Integer.valueOf(request.getParameter("id"));
        User user=this.userService.selectByPrimaryKey(id);
        return user;
    }
    @RequestMapping(value="getUserById2",method= RequestMethod.POST,consumes = "application/json")
    public User getUserById2(@RequestBody JSONObject jsonObject)throws IOException, SQLException {
        Integer id= jsonObject.getInteger("id");
        User user=this.userService.selectByPrimaryKey(id);
        return user;
    }
}
