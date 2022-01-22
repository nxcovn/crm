package com.xunzhenw.crm.workbench.service;

import com.xunzhenw.crm.vo.PaginationVo;
import com.xunzhenw.crm.workbench.domain.Activity;
import com.xunzhenw.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    boolean save(Activity activity);

    PaginationVo<Activity> pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndActivity(String id);

    boolean update(Activity activity);

    Activity detail(String id);

    List<ActivityRemark> getRemarkListByAId(String activityId);

    boolean deleteRemark(String id);


    boolean saveRemark(ActivityRemark aRemark);

    boolean updateRemark(ActivityRemark aRemark);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityListByNameAndNotByClueId(Map<String, Object> map);

    List<Activity> getActivityListByName(String name);
}
