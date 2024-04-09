package cn.granitech.web.controller;

import cn.granitech.interceptor.CallerContext;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseController {
    @Autowired
    protected CallerContext callerContext;
}
