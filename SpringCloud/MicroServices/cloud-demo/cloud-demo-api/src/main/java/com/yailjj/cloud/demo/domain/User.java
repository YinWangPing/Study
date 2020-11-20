package com.yailjj.cloud.demo.domain;

import lombok.Data;

@Data
public class User {
    /**
     * 编号
     */
    private Integer id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;
}
