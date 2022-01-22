package com.xunzhenw.crm.workbench.dao;

import com.xunzhenw.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {


    int save(Clue clue);

    int getTotalByCondition(Map<String, Object> map);

    List<Clue> getClueListByCondition(Map<String, Object> map);

    Clue detail(String id);

    int unbound(String id);

    Clue getById(String clueId);

    int delete(String clueId);
}
