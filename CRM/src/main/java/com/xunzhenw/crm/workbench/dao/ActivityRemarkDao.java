package com.xunzhenw.crm.workbench.dao;

import com.xunzhenw.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {
    int getCountByAids(String[] ids);

    int deleteByAids(String[] ids);

    List<ActivityRemark> getRemarkListByAId(String activityId);

    int deleteById(String id);

    int saveRemark(ActivityRemark aRemark);

    int updateRemark(ActivityRemark aRemark);
}
