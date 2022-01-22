package com.xunzhenw.crm.workbench.dao;

import com.xunzhenw.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer cus);

    List<String> getCustomerName(String name);
}
