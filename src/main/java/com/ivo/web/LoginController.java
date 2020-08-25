package com.ivo.web;

import com.ivo.common.result.Result;
import com.ivo.common.utils.HttpServletUtil;
import com.ivo.common.utils.ResultUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author wj
 * @version 1.0
 */
@Controller
public class LoginController {

    @GetMapping("/getLoginUser")
    @ResponseBody
    public Result getLoginUser() {
        String user = (String) HttpServletUtil.getRequest().getSession().getAttribute("USER");
        return ResultUtil.success("OK", user);
    }

    @PostMapping("/login")
    @ResponseBody
    public Result login(String username, String password) {
        HttpServletUtil.getRequest().getSession().setAttribute("USER", username.toUpperCase());
        return ResultUtil.success();
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @GetMapping("/logout")
    public String logout() {
        HttpServletUtil.getRequest().getSession().removeAttribute("USER");
        return "login.html";
    }
}
