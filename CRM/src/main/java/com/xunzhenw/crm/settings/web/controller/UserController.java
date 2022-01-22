package com.xunzhenw.crm.settings.web.controller;

import com.xunzhenw.crm.settings.domain.User;
import com.xunzhenw.crm.settings.service.UserService;
import com.xunzhenw.crm.settings.service.impl.UserServiceImpl;
import com.xunzhenw.crm.utils.MD5Util;
import com.xunzhenw.crm.utils.PrintJson;
import com.xunzhenw.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到用户控制器");

        String path = request.getServletPath();

        if ("/settings/user/login.do".equals(path)){
            login(request,response);
        }else if ("settings/user/xxx.do".equals(path)){

        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入登陆验证操作");
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        System.out.println(loginAct);
        System.out.println(loginPwd);
        //将密码的明文形式转换为MD5的密文形式
        loginPwd = MD5Util.getMD5(loginPwd);
        //接收浏览器IP地址
        String ip = request.getRemoteAddr();
        System.out.println("ip------:" + ip);

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        try {
            User user = us.login(loginAct,loginPwd,ip);
            request.getSession().setAttribute("user",user);
            /*String str = "{\"success\":true}";
            response.getWriter().print(str);*/
            PrintJson.printJsonFlag(response,true);
        }catch (Exception e){
            e.printStackTrace();
            String msg = e.getMessage();
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("success",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(response,map);
        }

    }


}
