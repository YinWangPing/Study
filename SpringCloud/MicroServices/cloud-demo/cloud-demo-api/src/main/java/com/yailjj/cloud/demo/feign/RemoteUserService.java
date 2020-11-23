package com.yailjj.cloud.demo.feign;
import java.util.List;

import com.yailjj.cloud.demo.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(value = "CLOUD-DEMO-USER")
public interface RemoteUserService
{
    @GetMapping("user/get/{id}")
    public User get(@PathVariable(value = "id") Integer id);

    @GetMapping("user/list")
    public List<User> list();
}

