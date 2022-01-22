package com.xunzhenw.crm.web.listener;

import com.xunzhenw.crm.settings.domain.DicValue;
import com.xunzhenw.crm.settings.service.DicService;
import com.xunzhenw.crm.settings.service.impl.DicServiceImpl;
import com.xunzhenw.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysInitListener implements ServletContextListener {

    /*
        该方法是用来监听上下文域对象的方法，当服务器启动，上文域对象创建
        对象创建完毕后，马上执行该方法

        event：改参数能够取得监听的对象
                监听的是什么对象，就可以通过该参数取得什么对象
                例如我们现在监听的是上下文域对象，通过该参数就可以获得上下文域对象
    * */
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("上下文域对象创建了");
        ServletContext application = event.getServletContext();

        //取数据字典
        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());
        /*
            向业务层要7个List
            可以打包成一个map，
                map.put("appellation",dvList1);
                map.put("clueState",dvList2);
                map.put("stage",dvList3);
                ...
                ...
        */
        Map<String, List<DicValue>> map = ds.getAll();

        Set<String> set = map.keySet();
        for (String key:set){
            application.setAttribute(key,map.get(key));
        }
        System.out.println("服务器缓存处理数据字典结束");

        //数据字典处理完毕后，处理stage2possibility.properties
        //把它处理成Java中的键值对
        //解析properties文件
        Map<String, String> pMap = new HashMap<String, String>();
        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> e = rb.getKeys();
        while(e.hasMoreElements()){
            String key = e.nextElement();
            String value = rb.getString(key);
            pMap.put(key, value);
        }
        application.setAttribute("pMap",pMap);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("上下文域对象创建了");
        ServletContext application = servletContextEvent.getServletContext();

        //取数据字典
        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());
        /*
            向业务层要7个List
            可以打包成一个map，
                map.put("appellation",dvList1);
                map.put("clueState",dvList2);
                map.put("stage",dvList3);
                ...
                ...
        */
        Map<String, List<DicValue>> map = ds.getAll();

        Set<String> set = map.keySet();
        for (String key:set){
            application.setAttribute(key,map.get(key));
        }
        System.out.println("服务器缓存处理数据字典结束");

        //数据字典处理完毕后，处理stage2possibility.properties
        //把它处理成Java中的键值对
        //解析properties文件
        Map<String, String> pMap = new HashMap<String, String>();
        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> e = rb.getKeys();
        while(e.hasMoreElements()){
            String key = e.nextElement();
            String value = rb.getString(key);
            pMap.put(key, value);
        }
        application.setAttribute("pMap",pMap);
    }
}
