package com.xunzhenw.crm.workbench.service;

import com.xunzhenw.crm.workbench.domain.Tran;
import com.xunzhenw.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranService {

    boolean save(Tran t);

    Tran detail(String id);

    List<TranHistory> getHistoryListByTranId(String tranId);

    boolean changeStage(Tran t);

    Map<String, Object> getCharts();
}
