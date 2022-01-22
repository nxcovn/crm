package com.xunzhenw.crm.workbench.service.impl;

import com.xunzhenw.crm.utils.DateTimeUtil;
import com.xunzhenw.crm.utils.SqlSessionUtil;
import com.xunzhenw.crm.utils.UUIDUtil;
import com.xunzhenw.crm.vo.PaginationVo;
import com.xunzhenw.crm.workbench.dao.*;
import com.xunzhenw.crm.workbench.domain.*;
import com.xunzhenw.crm.workbench.service.ClueService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ClueServiceImpl implements ClueService {
    //线索相关表
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
    //客户相关表
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);
    //联系人相关表
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);
    //交易相关表
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    public boolean save(Clue clue) {
        boolean flag = true;
        int count = clueDao.save(clue);
        if (count!=1){
            flag=false;
        }
        return flag;
    }

    public PaginationVo<Clue> pageList(Map<String, Object> map) {
        System.out.println("进入分页查询服务层");
        int total = clueDao.getTotalByCondition(map);
        List<Clue> dataList = clueDao.getClueListByCondition(map);

        PaginationVo<Clue> vo = new PaginationVo<Clue>();
        vo.setTotal(total);
        vo.setDataList(dataList);
        return vo;
    }

    public Clue detail(String id) {

        Clue c = clueDao.detail(id);
        return c;
    }

    public boolean unbound(String id) {
        boolean flag = true;
        int count = clueDao.unbound(id);
        if (count!=1){
            flag=false;
        }
        return flag;
    }

    public boolean bound(String cid, String[] aids) {
        boolean flag = true;
        for (String aid : aids){
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setActivityId(aid);
            car.setClueId(cid);

            int count = clueActivityRelationDao.save(car);
            if (count!=1){
                flag=false;
            }
        }
        return flag;
    }

    public boolean convert(String clueId, Tran t,String createBy) {
        String createTime = DateTimeUtil.getSysTime();
        boolean flag = true;

        //(1)通过线索Id获取线索对象（）
        Clue clue = clueDao.getById(clueId);
        //（2）通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配）
        String company = clue.getCompany();
        Customer cus = customerDao.getCustomerByName(company);
        if (cus==null){
            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setAddress(clue.getAddress());
            cus.setWebsite(clue.getWebsite());
            cus.setPhone(clue.getPhone());
            cus.setOwner(clue.getOwner());
            cus.setNextContactTime(clue.getNextContactTime());
            cus.setName(company);
            cus.setDescription(clue.getDescription());
            cus.setCreateTime(createTime);
            cus.setCreateBy(createBy);
            cus.setContactSummary(clue.getContactSummary());
            //添加客户
            int count1 = customerDao.save(cus);
            if (count1 != 1){
                flag = false;
            }
        }

        //（3）通过线索对象提取联系人信息，保存联系人
        Contacts con =  new Contacts();
        con.setId(UUIDUtil.getUUID());
        con.setSource(clue.getSource());
        con.setOwner(clue.getOwner());
        con.setNextContactTime(clue.getNextContactTime());
        con.setMphone(clue.getMphone());
        con.setJob(clue.getJob());
        con.setFullname(clue.getFullname());
        con.setEmail(clue.getEmail());
        con.setDescription(clue.getDescription());
        con.setCustomerId(cus.getId());
        con.setCreateTime(createTime);
        con.setCreateBy(createBy);
        con.setContactSummary(clue.getContactSummary());
        con.setAppellation(clue.getAppellation());
        con.setAddress(clue.getAddress());
        //添加联系人
        int count2 = contactsDao.save(con);
        if (count2 != 1){
            flag = false;
        }

        //（4） 线索备注转换到客户备注以及联系人备注
        List<ClueRemark> clueRemarkList = clueRemarkDao.getListByClueId(clueId);
        for(ClueRemark cr:clueRemarkList){
            //主要取出备注信息
            String noteContent = cr.getNoteContent();
            //创建客户备注对象，添加信息
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setNoteContent(noteContent);
            customerRemark.setEditFlag("0");
            customerRemark.setCustomerId(cus.getId());
            customerRemark.setCreateTime(createTime);
            customerRemark.setCreateBy(createBy);
            int count3 = customerRemarkDao.save(customerRemark);
            if (count3 != 1){
                flag = false;
            }
            //创建联系人备注对象，添加联系人
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setNoteContent(noteContent);
            contactsRemark.setEditFlag("0");
            contactsRemark.setContactsId(con.getId());
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setCreateBy(createBy);
            int count4 = contactsRemarkDao.save(contactsRemark);
            if (count4 != 1){
                flag = false;
            }
        }

        //（5）“线索和市场活动”的关系转换到“联系人和市场活动”的关系
        //查询出与该条线索关联的市场活动，查询与市场活动的关联关系列表
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getListByClueId(clueId);
        //遍历每一条与市场活动关联的关联关系记录
        for (ClueActivityRelation clueAR:clueActivityRelationList){
            //从每一条遍历出来的记录中取出关联的市场活动id
            String activityId = clueAR.getActivityId();
            //创建联系人与市场活动的关联关系对象 让第三步生成的联系人与市场活动做关联
            ContactsActivityRelation contactsAR = new ContactsActivityRelation();
            contactsAR.setId(UUIDUtil.getUUID());
            contactsAR.setActivityId(activityId);
            contactsAR.setContactsId(con.getId());
            //添加关联关系
            int count5 = contactsActivityRelationDao.save(contactsAR);
            if (count5 != 1){
                flag = false;
            }
        }

        //（6）如果有创建交易需求，创建一条交易
        if (t!=null){
            /*
            * t对象在controller中已经封装好的信息如下；
            *   id，money，name，expectedDate，stage，activityId，createBy，createTime
            * 接下来可以通过第一步生成的clue对象，取出一些信息，继续完善对t对象的封装
            * */

            t.setSource(clue.getSource());
            t.setOwner(clue.getOwner());
            t.setNextContactTime(clue.getNextContactTime());
            t.setDescription(clue.getDescription());
            t.setCustomerId(cus.getId());
            t.setContactSummary(clue.getContactSummary());
            t.setContactsId(con.getId());
            int count6 = tranDao.save(t);
            if (count6!=1){
                flag = false;
            }
            //（7）如果创建了交易，则创建一条交易历史
            TranHistory th = new TranHistory();
            th.setId(UUIDUtil.getUUID());
            th.setCreateBy(createBy);
            th.setCreateTime(createTime);
            th.setExpectedDate(t.getExpectedDate());
            th.setMoney(t.getMoney());
            th.setStage(t.getStage());
            th.setTranId(t.getId());
            //添加交易历史
            int count7 = tranHistoryDao.save(th);
            if (count7 != 1){
                flag = false;
            }
        }

        //（8） 删除线索备注
        for(ClueRemark cr:clueRemarkList){
            int count8 = clueRemarkDao.delete(cr);
            if (count8 != 1){
                flag = false;
            }
        }

        //（9）删除线索和市场活动的关系
        for (ClueActivityRelation clueAR:clueActivityRelationList){
            int count9 = clueActivityRelationDao.delete(clueAR);
            if (count9 != 1){
                flag = false;
            }
        }

        //（10）删除线索
        int count10 = clueDao.delete(clueId);
        if (count10!=1){
            flag = false;
        }
        return flag;
    }
}
