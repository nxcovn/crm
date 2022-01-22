package com.xunzhenw.crm.settings.dao;

import com.xunzhenw.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getValueByCode(String code);
}
