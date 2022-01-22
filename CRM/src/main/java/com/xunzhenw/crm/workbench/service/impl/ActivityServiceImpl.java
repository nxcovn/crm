package com.xunzhenw.crm.workbench.service.impl;

import com.xunzhenw.crm.settings.dao.UserDao;
import com.xunzhenw.crm.settings.domain.User;
import com.xunzhenw.crm.utils.SqlSessionUtil;
import com.xunzhenw.crm.vo.PaginationVo;
import com.xunzhenw.crm.workbench.dao.ActivityDao;
import com.xunzhenw.crm.workbench.dao.ActivityRemarkDao;
import com.xunzhenw.crm.workbench.domain.Activity;
import com.xunzhenw.crm.workbench.domain.ActivityRemark;
import com.xunzhenw.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);

    public List<Activity> getActivityListByName(String name) {
        List<Activity> aList = activityDao.getActivityListByName(name);
        return aList;
    }

    public List<Activity> getActivityListByNameAndNotByClueId(Map<String, Object> map) {
        List<Activity> aList = activityDao.getActivityListByNameAndNotByClueId(map);
        return aList;
    }

    public List<Activity> getActivityListByClueId(String clueId) {
        List<Activity> aList = activityDao.getActivityListByClueId(clueId);
        return aList;
    }

    public boolean save(Activity activity) {
        boolean flag = true;
        int count = activityDao.save(activity);
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    public PaginationVo<Activity> pageList(Map<String, Object> map) {
        System.out.println("进入分页查询服务层");
        int total = activityDao.getTotalByCondition(map);
        List<Activity> dataList = activityDao.getActivityListByCondition(map);

        PaginationVo<Activity> vo = new PaginationVo<Activity>();
        vo.setTotal(total);
        vo.setDataList(dataList);
        return vo;
    }

    public boolean delete(String[] ids) {
        boolean flag = true;
        //查询出需删除的备注的数量
        int count1 = activityRemarkDao.getCountByAids(ids);

        //删除备注，返回受到影响的条数（实际删除的数量）
        int count2 = activityRemarkDao.deleteByAids(ids);

        //删除市场活动
        if (count1==count2){
            int count3 = activityDao.delete(ids);
            if (count3!=ids.length){
                flag=false;
            }
        }else{
            flag=false;
        }


        return flag;
    }

    public Map<String, Object> getUserListAndActivity(String id) {
        System.out.println("进入修改信息查询");
        List<User> uList = userDao.getUserList();
        Activity activity = activityDao.getById(id);

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("uList",uList);
        map.put("activity",activity);
        return map;
    }

    public boolean update(Activity activity) {
        System.out.println("进入市场活动修改");
        boolean flag = true;
        int count = activityDao.update(activity);

        if (count!=1){
            flag=false;
        }
        return flag;
    }

    public Activity detail(String id) {
        Activity a = activityDao.detail(id);
        return a;
    }

    public List<ActivityRemark> getRemarkListByAId(String activityId) {
        List<ActivityRemark> list = activityRemarkDao.getRemarkListByAId(activityId);
        return list;
    }

    public boolean deleteRemark(String id) {
        boolean flag = true;
        int count = activityRemarkDao.deleteById(id);
        if (count!=1){
            flag = false;
        }
        return flag;
    }

    public boolean saveRemark(ActivityRemark aRemark) {
        boolean flag = true;
        int count = activityRemarkDao.saveRemark(aRemark);
        if (count!=1){
            flag = false;
        }
        return flag;
    }

    public boolean updateRemark(ActivityRemark aRemark) {
        boolean flag = true;
        int count = activityRemarkDao.updateRemark(aRemark);
        if (count!=1){
            flag = false;
        }
        return flag;
    }
}
