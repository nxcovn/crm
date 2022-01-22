package com.xunzhenw.crm.settings.service.impl;

import com.xunzhenw.crm.settings.dao.DicTypeDao;
import com.xunzhenw.crm.settings.dao.DicValueDao;
import com.xunzhenw.crm.settings.domain.DicType;
import com.xunzhenw.crm.settings.domain.DicValue;
import com.xunzhenw.crm.settings.service.DicService;
import com.xunzhenw.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {
    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    public Map<String, List<DicValue>> getAll() {

        //取出字典类型列表
        List<DicType> dicTypeList= dicTypeDao.getTypeList();

        Map<String,List<DicValue>> map = new HashMap<String, List<DicValue>>();
        for (DicType type: dicTypeList) {
            List<DicValue> dicValueList = dicValueDao.getValueByCode(type.getCode());
            map.put(type.getCode(),dicValueList);
        }
        return map;
    }
}
