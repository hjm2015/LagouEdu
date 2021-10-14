package com.lagou.demo.service.impl;

import com.lagou.demo.service.DemoService;
import com.lagou.edu.framework.annotation.LgService;

@LgService
public class DemoServiceImpl implements DemoService {
    @Override
    public String get(String name) {
        System.out.println(name);
        return name;
    }
}
