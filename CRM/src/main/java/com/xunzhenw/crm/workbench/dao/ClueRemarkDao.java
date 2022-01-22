package com.xunzhenw.crm.workbench.dao;

import com.xunzhenw.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> getListByClueId(String clueId);

    int delete(ClueRemark cr);
}
