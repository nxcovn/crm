package com.xunzhenw.crm.workbench.service.impl;

import com.xunzhenw.crm.utils.SqlSessionUtil;
import com.xunzhenw.crm.workbench.dao.CustomerDao;
import com.xunzhenw.crm.workbench.service.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    public List<String> getCustomerName(String name) {
        List<String> list = customerDao.getCustomerName(name);
        return list;
    }
}
