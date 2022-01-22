package com.xunzhenw.crm.settings.service.impl;

import com.xunzhenw.crm.exception.LoginException;
import com.xunzhenw.crm.settings.dao.UserDao;
import com.xunzhenw.crm.settings.domain.User;
import com.xunzhenw.crm.settings.service.UserService;
import com.xunzhenw.crm.utils.DateTimeUtil;
import com.xunzhenw.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    public User login(String loginAct, String loginPwd, String ip) throws LoginException {

        Map<String,String> map = new HashMap<String, String>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);

        User user = userDao.login(map);

        if (user==null){
            throw new LoginException("账号或密码错误！");
        }

        //验证失效时间
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if (expireTime.compareTo(currentTime)<0){
            throw new LoginException("账号密码已失效！");
        }

        //判断账号是否锁定
        String lockState = user.getLockState();
        if ("0".equals(lockState)){
            throw new LoginException("账号已锁定！");
        }

        //判断IP地址是否符合
        String allowIps = user.getAllowIps();
        if (!allowIps.equals("")){
            if (!allowIps.contains(ip)){
                throw new LoginException("登录IP地址受限！");
            }
        }

        return user;
    }

    public List<User> getUserList() {
        List<User> uList = userDao.getUserList();
        return uList;
    }
}
