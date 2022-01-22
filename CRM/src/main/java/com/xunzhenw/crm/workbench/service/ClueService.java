package com.xunzhenw.crm.workbench.service;

import com.xunzhenw.crm.vo.PaginationVo;
import com.xunzhenw.crm.workbench.domain.Clue;
import com.xunzhenw.crm.workbench.domain.Tran;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface ClueService {
    boolean save(Clue clue);

    PaginationVo<Clue> pageList(Map<String, Object> map);

    Clue detail(String id);

    boolean unbound(String id);

    boolean bound(String cid, String[] aids);

    boolean convert(String clueId, Tran t,String createBy);
}
