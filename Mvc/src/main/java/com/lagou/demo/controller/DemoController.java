package com.lagou.demo.controller;

import com.lagou.demo.service.DemoService;
import com.lagou.edu.framework.annotation.LgAutowired;
import com.lagou.edu.framework.annotation.LgController;
import com.lagou.edu.framework.annotation.LgRequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@LgController
@LgRequestMapping("/demo")
public class DemoController {

    @LgAutowired
    private DemoService demoService;

    public String query(HttpServletRequest servletRequest , HttpServletResponse servletResponse , String name){
        return demoService.get(name);
    }
}
