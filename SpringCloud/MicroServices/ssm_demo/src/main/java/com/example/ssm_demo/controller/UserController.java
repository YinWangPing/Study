package com.example.ssm_demo.controller;

import com.example.ssm_demo.pojo.User;
import com.example.ssm_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @RequestMapping(value="getUserById",method= RequestMethod.POST)
    public User getUserById(HttpServletRequest request, HttpServletResponse response)throws IOException, SQLException {
        User user=this.userService.selectByPrimaryKey(1);
        return user;
    }
}
