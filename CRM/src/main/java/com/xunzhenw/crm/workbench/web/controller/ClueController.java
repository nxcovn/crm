package com.xunzhenw.crm.workbench.web.controller;

import com.xunzhenw.crm.settings.domain.User;
import com.xunzhenw.crm.settings.service.UserService;
import com.xunzhenw.crm.settings.service.impl.UserServiceImpl;
import com.xunzhenw.crm.utils.DateTimeUtil;
import com.xunzhenw.crm.utils.PrintJson;
import com.xunzhenw.crm.utils.ServiceFactory;
import com.xunzhenw.crm.utils.UUIDUtil;
import com.xunzhenw.crm.vo.PaginationVo;
import com.xunzhenw.crm.workbench.domain.Activity;
import com.xunzhenw.crm.workbench.domain.ActivityRemark;
import com.xunzhenw.crm.workbench.domain.Clue;
import com.xunzhenw.crm.workbench.domain.Tran;
import com.xunzhenw.crm.workbench.service.ActivityService;
import com.xunzhenw.crm.workbench.service.ClueService;
import com.xunzhenw.crm.workbench.service.impl.ActivityServiceImpl;
import com.xunzhenw.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到线索控制器");

        String path = request.getServletPath();

        if ("/workbench/clue/getUserList.do".equals(path)){
            getUserList(request,response);
        }else if ("/workbench/clue/save.do".equals(path)){
            save(request,response);
        }else if ("/workbench/clue/pageList.do".equals(path)){
            pageList(request,response);
        }else if ("/workbench/clue/detail.do".equals(path)){
            detail(request,response);
        }else if ("/workbench/clue/getActivityListByClueId.do".equals(path)){
            getActivityListByClueId(request,response);
        }else if ("/workbench/clue/unbound.do".equals(path)){
            unbound(request,response);
        }else if ("/workbench/clue/getActivityListByNameAndNotByClueId.do".equals(path)){
            getActivityListByNameAndNotByClueId(request,response);
        }else if ("/workbench/clue/bound.do".equals(path)){
            bound(request,response);
        }else if ("/workbench/clue/getActivityListByName.do".equals(path)){
            getActivityListByName(request,response);
        }else if ("/workbench/clue/convert.do".equals(path)){
            convert(request,response);
        }
    }

    private void convert(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("执行转换线索操作（无交易）");
        String clueId = request.getParameter("clueId");
        String flag = request.getParameter("flag");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();


        Tran t = null;
        if ("a".equals(flag)){
            t = new Tran();

            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expectedDate = request.getParameter("expectedDate");
            String stage = request.getParameter("stage");
            String activityId = request.getParameter("activityId");
            String id = UUIDUtil.getUUID();
            String createTime = DateTimeUtil.getSysTime();


            t.setId(id);
            t.setMoney(money);
            t.setName(name);
            t.setExpectedDate(expectedDate);
            t.setStage(stage);
            t.setActivityId(activityId);
            t.setCreateTime(createTime);
            t.setCreateBy(createBy);
        }
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag1 = cs.convert(clueId,t,createBy);
        if (flag1){
            response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");
        }


    }

    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据name查询市场活动列表");
        String name = request.getParameter("aname");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList = as.getActivityListByName(name);
        PrintJson.printJsonObj(response,aList);
    }

    private void bound(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("添加关联关系表");
        String cid = request.getParameter("cid");
        String[] aids = request.getParameterValues("aid");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.bound(cid, aids);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getActivityListByNameAndNotByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据id查询关联关系表并去除已经关联的内容");
        String aName = request.getParameter("aName");
        String clueId = request.getParameter("clueId");

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("aName",aName);
        map.put("clueId",clueId);
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList = as.getActivityListByNameAndNotByClueId(map);
        PrintJson.printJsonObj(response,aList);
    }

    private void unbound(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据id删除关联信息！");
        String id = request.getParameter("id");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.unbound(id);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据id查询关联的市场活动列表");
        String clueId = request.getParameter("clueId");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList = as.getActivityListByClueId(clueId);
        PrintJson.printJsonObj(response,aList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("跳转到详细信息页");
        String id = request.getParameter("id");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue c = cs.detail(id);

        request.setAttribute("c",c);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request,response);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行线索列表查询操作(条件+分页)");

        String fullname = request.getParameter("fullname");
        String company = request.getParameter("company");
        String phone = request.getParameter("phone");
        String source = request.getParameter("source");
        String clueOwner = request.getParameter("clueOwner");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);

        //计算出略过的记录数
        int skipCount = (pageNo-1)*pageSize;

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("fullname",fullname);
        map.put("company",company);
        map.put("phone",phone);
        map.put("source",source);
        map.put("clueOwner",clueOwner);
        map.put("mphone",mphone);
        map.put("state",state);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        PaginationVo<Clue> vo = cs.pageList(map);
        PrintJson.printJsonObj(response,vo);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("创建新线索操作");

        String id = UUIDUtil.getUUID();
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = ((User)request.getSession().getAttribute("user")).getId();
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String editBy = request.getParameter("");
        String editTime = request.getParameter("");
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Clue clue = new Clue();
        clue.setId(id);
        clue.setFullname(fullname);
        clue.setAppellation(appellation);
        clue.setOwner(owner);
        clue.setCompany(company);
        clue.setJob(job);
        clue.setEmail(email);
        clue.setPhone(phone);
        clue.setWebsite(website);
        clue.setMphone(mphone);
        clue.setState(state);
        clue.setSource(source);
        clue.setCreateBy(createBy);
        clue.setCreateTime(createTime);
        clue.setEditBy(editBy);
        clue.setEditTime(editTime);
        clue.setDescription(description);
        clue.setContactSummary(contactSummary);
        clue.setNextContactTime(nextContactTime);
        clue.setAddress(address);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.save(clue);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("获取用户信息列表");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getUserList();
        PrintJson.printJsonObj(response,uList);
    }


}
