package com.xunzhenw.crm.workbench.service.impl;

import com.xunzhenw.crm.utils.DateTimeUtil;
import com.xunzhenw.crm.utils.SqlSessionUtil;
import com.xunzhenw.crm.utils.UUIDUtil;
import com.xunzhenw.crm.workbench.dao.CustomerDao;
import com.xunzhenw.crm.workbench.dao.TranDao;
import com.xunzhenw.crm.workbench.dao.TranHistoryDao;
import com.xunzhenw.crm.workbench.domain.Customer;
import com.xunzhenw.crm.workbench.domain.Tran;
import com.xunzhenw.crm.workbench.domain.TranHistory;
import com.xunzhenw.crm.workbench.service.TranService;

import javax.xml.transform.Transformer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranServiceImpl implements TranService {
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    public Map<String, Object> getCharts() {
        //取得总条数
        int total = tranDao.getTotal();
        //取dataList
        List<Map<String, Object>> dataList = tranDao.getCharts();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total",total);
        map.put("dataList",dataList);
        return map;
    }

    public boolean changeStage(Tran t) {
        boolean flag = true;
        int count = tranDao.changeStage(t);
        if (count!=1){
            flag=false;
        }

        //生成交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setCreateBy(t.getEditBy());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setExpectedDate(t.getExpectedDate());
        th.setMoney(t.getMoney());
        th.setTranId(t.getId());
        th.setStage(t.getStage());
        int count2 = tranHistoryDao.save(th);
        if (count2!=1){
            flag=false;
        }
        return flag;
    }

    public List<TranHistory> getHistoryListByTranId(String tranId) {
        List<TranHistory> list= tranHistoryDao.getHistoryListByTranId(tranId);
        return list;
    }

    public Tran detail(String id) {
        Tran t = tranDao.detail(id);
        return t;
    }

    public boolean save(Tran t) {

        /*
        *   交易添加业务
        *       参数t里少条信息，就是客户的主键，customerId
        *       （1）判断是否有此客户，有就直接查询封装，没有就创建此客户
        * */
        boolean flag = true;
        Customer customer = customerDao.getCustomerByName(t.getCustomerId());
        if (customer==null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setName(t.getCustomerId());
            customer.setCreateBy(t.getCreateBy());
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setContactSummary(t.getContactSummary());
            customer.setNextContactTime(t.getNextContactTime());
            customer.setOwner(t.getOwner());
            int count1 = customerDao.save(customer);
            if (count1!=1){
                flag = false;
            }
        }
        //无论上面有没有创建客户，都要在这里取得id
        t.setCustomerId(customer.getId());
        int count2 = tranDao.save(t);
        if (count2!=1){
            flag = false;
        }
        //添加交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(t.getId());
        th.setStage(t.getStage());
        th.setMoney(t.getMoney());
        th.setExpectedDate(t.getExpectedDate());
        th.setCreateBy(t.getCreateBy());
        th.setCreateTime(DateTimeUtil.getSysTime());
        int count3 = tranHistoryDao.save(th);
        if (count3!=1){
            flag = false;
        }
        return flag;
    }
}
