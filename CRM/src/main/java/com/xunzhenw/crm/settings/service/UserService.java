package com.xunzhenw.crm.settings.service;

import com.xunzhenw.crm.exception.LoginException;
import com.xunzhenw.crm.settings.domain.User;

import java.util.List;

public interface UserService {

    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();
}
