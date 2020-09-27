package com.ivo.system;

import com.ivo.common.result.Result;
import com.ivo.common.utils.HttpServletUtil;
import com.ivo.common.utils.ResultUtil;
import com.ivo.rest.HrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录
 * @author wj
 * @version 1.0
 */
@Controller
@Slf4j
public class LoginController {

    private HrService hrService;

    @Autowired
    public LoginController(HrService hrService) {
        this.hrService = hrService;
    }

    /**
     * 登录操作
     * @param username 用户名
     * @param password 密码
     * @return Result
     */
    @PostMapping("/login")
    @ResponseBody
    public Result login(String username, String password) {
        log.info("登录  " + username);
        boolean b = hrService.verify(username, password);
        if(b) {
            Map map = hrService.getEmployee(username);
            HttpSession session = HttpServletUtil.getRequest().getSession();
            session.setAttribute("USER", username.toUpperCase()); //工号
            session.setAttribute("USERNAME", map.get("name")); //姓名
            session.setAttribute("DEPT", map.get("deptS")); //部门
            return ResultUtil.success("登录成功");
        } else {
            return ResultUtil.error("登录失败，工号或密码错误");
        }
    }

    /**
     * 登录页面
     * @return String
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * 登出操作
     * @return String
     */
    @GetMapping("/logout")
    public String logout() {
        HttpSession session = HttpServletUtil.getRequest().getSession();
        if(session != null) {
            session.removeAttribute("USER");
        }
        return "redirect:/login";
    }

    /**
     * 首页
     * @return String
     */
    @GetMapping("/")
    public String index() {
        return "index2";
    }

    /**
     * 获取当前登录用户
     * @return Result
     */
    @GetMapping("/getLoginUser")
    @ResponseBody
    public Result getLoginUser() {
        HttpSession session = HttpServletUtil.getRequest().getSession();
        String USER = (String) session.getAttribute("USER");
        String USERNAME = (String) session.getAttribute("USERNAME");
        String DEPT = (String) session.getAttribute("DEPT");
        Map<String, String> map = new HashMap();
        map.put("USER", USER);
        map.put("USERNAME", USERNAME);
        map.put("DEPT", DEPT);
        return ResultUtil.success(map);
    }
}
