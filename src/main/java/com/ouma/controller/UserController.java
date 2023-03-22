package com.ouma.controller;

import com.alibaba.fastjson2.JSONArray;
import com.ouma.pojo.Role;
import com.ouma.pojo.User;
import com.ouma.service.RoleService;
import com.ouma.service.RoleServiceImpl;
import com.ouma.service.UserService;
import com.ouma.utils.Constants;
import com.ouma.utils.PageSupport;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @RequestMapping("/")
    public String defaultPage(){
        return "toLogin";
    }

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }


    @RequestMapping("/login.do")
    public String login(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("--------LoginServlet----------");
        // 获取用户名和密码
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");
        System.out.println(userCode);
        System.out.println(userPassword);
        // 调用业务层，进行用户密码匹配
        User user = userService.login(userCode, userPassword);

        if(user != null){   // 登录成功
            // 将用户信息放入session
            req.getSession().setAttribute(Constants.USER_SESSION,user);
            // 页面跳转 —— frame.jsp
            return "jsp/frame";
        } else {
            // 页面跳转 —— login.jsp
            req.setAttribute("error","用户名或密码错误！");
            return "login";
        }
    }

    @RequestMapping("/jsp/logout.do")
    public String toLogout(HttpServletRequest req, HttpServletResponse resp){
        req.getSession().removeAttribute(Constants.USER_SESSION);
        return "redirect:/toLogin";
    }

    @RequestMapping("/jsp/index")
    public String test(){
        return "/jsp/index2";
    }

    @RequestMapping("/jsp/user.do")
    public void userAction(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String method = request.getParameter("method");
        System.out.println("method----> " + method);

        String result = "";
        if(method != null && method.equals("savepwd")){
            this.updatePwd(request, response);
        } else if(method!=null && method.equals("pwdmodify")){
            this.pwdModify(request,response);
        }else if(method != null && method.equals("query")) {
            this.query(request, response);
        }else if(method != null && method.equals("add")) {
            this.add(request, response);
        } else if(method != null && method.equals("getrolelist")){
            this.getRoleList(request, response);
        } else if(method != null && method.equals("ucexist")){
            this.userCodeExist(request, response);
        } else if(method != null && method.equals("deluser")){
            this.delUser(request, response);
        } else if(method != null && method.equals("view")){
            this.getUserById(request, response,"userview.jsp");
        } else if(method != null && method.equals("modify")){
            this.getUserById(request, response,"usermodify.jsp");
        } else if(method != null && method.equals("modifyexe")) {
            this.modify(request, response);
        }
    }

    // 修改密码
    public void updatePwd(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // 从Session中取ID
        Object o = request.getSession().getAttribute(Constants.USER_SESSION);
        String newpassword = request.getParameter("newpassword");
        boolean flag = false;

        if(o != null && newpassword != null || newpassword.isEmpty()){
            flag = userService.updatePwd(((User)o).getId(),newpassword);
            if(flag){
                request.setAttribute(Constants.SYS_MESSAGE, "修改密码成功,请退出并使用新密码重新登录！");
                // 密码修改成功，session注销
                request.getSession().removeAttribute(Constants.USER_SESSION);
            }else{
                request.setAttribute(Constants.SYS_MESSAGE, "修改密码失败！");
            }
        }else{
            request.setAttribute(Constants.SYS_MESSAGE, "修改密码失败！");
        }
        request.getRequestDispatcher("pwdmodify.jsp").forward(request, response);
    }

    // 验证旧密码,session中有用户的密码
    public void pwdModify(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // 从Session中取ID
        Object o = request.getSession().getAttribute(Constants.USER_SESSION);
        String oldpassword = request.getParameter("oldpassword");
        Map<String, String> resultMap = new HashMap<String, String>();

        // session过期
        if (null == o) {
            resultMap.put("result", "sessionerror");
        } else if (oldpassword == null || oldpassword.isEmpty()) {
            // 旧密码输入为空
            resultMap.put("result", "error");
        } else {
            String sessionPwd = ((User) o).getUserPassword();
            if (oldpassword.equals(sessionPwd)) {
                resultMap.put("result", "true");
            } else {
                // 旧密码输入不正确
                resultMap.put("result", "false");
            }
        }

        response.setContentType("application/json");
        PrintWriter outWriter = response.getWriter();
        // JSONArray 阿里巴巴的JSON工具类
        outWriter.write(JSONArray.toJSONString(resultMap));
        outWriter.flush();
        outWriter.close();

    }

    // 用户管理
    public void query(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 查询用户列表
        String queryUserName = request.getParameter("queryname");
        String temp = request.getParameter("queryUserRole");
        String pageIndex = request.getParameter("pageIndex");
        int queryUserRole = 0;
        List<User> userList = null;

        // 设置页面容量
        int pageSize = Constants.pageSize;

        // 当前页码
        int currentPageNo = 1;

        System.out.println("queryUserName servlet-------->"+queryUserName);
        System.out.println("queryUserRole servlet-------->"+queryUserRole);
        System.out.println("query pageIndex----------> " + pageIndex);

        if(queryUserName == null){
            queryUserName = "";
        }
        if(temp != null && !temp.equals("")){
            queryUserRole = Integer.parseInt(temp);
        }

        if(pageIndex != null){
            try{
                currentPageNo = Integer.valueOf(pageIndex);
            }catch(NumberFormatException e){
                response.sendRedirect("error.jsp");
            }
        }
        // 总数量（表）
        int totalCount	= userService.getUserCount(queryUserName,queryUserRole);
        // 总页数
        PageSupport pages=new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);

        int totalPageCount = pages.getTotalPageCount();

        // 控制首页和尾页
        if(currentPageNo < 1){
            currentPageNo = 1;
        }else if(currentPageNo > totalPageCount){
            currentPageNo = totalPageCount;
        }


        userList = userService.getUserList(queryUserName,queryUserRole,currentPageNo, pageSize);
        request.setAttribute("userList", userList);
        List<Role> roleList = null;
        roleList = roleService.getRoleList();
        request.setAttribute("roleList", roleList);
        request.setAttribute("queryUserName", queryUserName);
        request.setAttribute("queryUserRole", queryUserRole);
        request.setAttribute("totalPageCount", totalPageCount);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("currentPageNo", currentPageNo);
        request.getRequestDispatcher("userlist.jsp").forward(request, response);
    }

    public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String userCode = request.getParameter("userCode");
        String userName = request.getParameter("userName");
        String userPassword = request.getParameter("userPassword");
        String gender = request.getParameter("gender");
        String birthday = request.getParameter("birthday");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String userRole = request.getParameter("userRole");

        User user = new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setAddress(address);
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setGender(Integer.valueOf(gender));
        user.setPhone(phone);
        user.setUserRole(Integer.valueOf(userRole));
        user.setCreationDate(new Date());
        user.setCreatedBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());

        if(userService.addUser(user)) {
            System.out.println("add success!");
            request.getRequestDispatcher("user.do?method=query").forward(request, response);
        }
//        }else {
//            request.getRequestDispatcher("useradd.jsp").forward(request, response);
//        }
    }


    // 获取角色列表
    private void getRoleList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Role> roleList = null;
        roleList = roleService.getRoleList();
        // 把roleList转换成json对象输出
        response.setContentType("application/json");
        PrintWriter outPrintWriter = response.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(roleList));
        outPrintWriter.flush();
        outPrintWriter.close();
    }


    // 删除用户
    public void delUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String id = request.getParameter("uid");
        Integer delId = 0;
        try{
            delId = Integer.parseInt(id);
        }catch (Exception e) {
            delId = 0;
        }
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(delId <= 0){
            resultMap.put("delResult", "notexist");
        }else{
            if(userService.deleteUserById(delId)){
                resultMap.put("delResult", "true");
            }else{
                resultMap.put("delResult", "false");
            }
        }

        // 把resultMap转换成json对象输出
        response.setContentType("application/json");
        PrintWriter outPrintWriter = response.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();
        outPrintWriter.close();
    }

    // 修改用户信息
    public void modify(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("uid");
        String userName = request.getParameter("userName");
        String gender = request.getParameter("gender");
        String birthday = request.getParameter("birthday");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String userRole = request.getParameter("userRole");

        User user = new User();
        user.setId(Integer.valueOf(id));
        user.setUserName(userName);
        user.setGender(Integer.valueOf(gender));
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.valueOf(userRole));
        user.setModifyBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
        user.setModifyDate(new Date());

        if(userService.modify(user)){
            response.sendRedirect(request.getContextPath()+"/jsp/user.do?method=query");
        }else{
            request.getRequestDispatcher("usermodify.jsp").forward(request, response);
        }

    }

    // 通过userId查询user
    public void getUserById(HttpServletRequest request, HttpServletResponse response,String url)
            throws ServletException, IOException {

        String id = request.getParameter("uid");
        if(id != null && !id.isEmpty()){
            // 调用后台方法得到user对象
            User user = userService.getUserById(id);
            request.setAttribute("user", user);
            request.getRequestDispatcher(url).forward(request, response);
        }

    }

    // 根据userCode查询出User
    public void userCodeExist(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 判断用户账号是否可用
        String userCode = request.getParameter("userCode");

        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(userCode == null && userCode.isEmpty()){
            // userCode == null || userCode.equals("")
            resultMap.put("userCode", "exist");
        }else{
            User user = userService.selectUserCodeExist(userCode);
            if(null != user){
                resultMap.put("userCode","exist");
            }else{
                resultMap.put("userCode", "notexist");
            }
        }

        // 把resultMap转为json字符串以json的形式输出
        // 配置上下文的输出类型
        response.setContentType("application/json");
        // 从response对象中获取往外输出的writer对象
        PrintWriter outPrintWriter = response.getWriter();
        // 把resultMap转为json字符串 输出
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush(); // 刷新
        outPrintWriter.close(); // 关闭流
    }

}

