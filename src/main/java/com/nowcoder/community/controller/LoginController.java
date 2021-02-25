package com.nowcoder.community.controller;

import com.google.code.kaptcha.Producer;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.ActivationCode;
import com.nowcoder.community.util.LoginTicketCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @Author ztyh
 * @Description 登录模块控制器
 * @Date 2021/2/10 14:51
 */
@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptchaProducer;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @GetMapping("/register")
    public String toRegisterPage() {
        return "/site/register";
    }

    @GetMapping("/login")
    public String toLoginPage() {
        return "/site/login";
    }

    @PostMapping("/register")
    public String register(Model model, User user) {
        Map<String, Object> map = userService.register(user);
        if (map == null || map.isEmpty()) {
            model.addAttribute("msg", "注册成功！已向您发送激活邮件！");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        }
        model.addAttribute("usernameMsg", map.get("usernameMsg"));
        model.addAttribute("passwordMsg", map.get("passwordMsg"));
        model.addAttribute("emailMsg", map.get("emailMsg"));
        return "/site/register";
    }

    @GetMapping("/activation/{userId}/{code}")
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code) {
        int result = userService.activation(userId, code);
        if (result == ActivationCode.SUCCESS.getStatus()) {
            model.addAttribute("msg", "激活成功！您的账号可以正常使用！");
            model.addAttribute("target", "/login");
        } else if (result == ActivationCode.REPEAT.getStatus()) {
            model.addAttribute("msg", ActivationCode.REPEAT.getMessage());
            model.addAttribute("target", "/index");
        } else {
            model.addAttribute("msg", ActivationCode.FAILURE.getMessage());
            model.addAttribute("target", "/index");
        }
        return "/site/operate-result";
    }

    @GetMapping("/kaptcha")
    public void getKaptcha(HttpServletResponse response, HttpSession session) {
        //生成验证码
        String text = kaptchaProducer.createText();
        //生成图片
        BufferedImage image = kaptchaProducer.createImage(text);
        //将验证码存入session，后续登录验证
        session.setAttribute("kaptcha", text);

        response.setContentType("image/png");

        try {
            //获得字节输出流
            OutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            logger.error("响应验证码失败：" + e.getMessage());
        }
    }

    @PostMapping("/login")
    public String login(String username, String password, String code,
                        boolean rememberMe, Model model, HttpSession session, HttpServletResponse response) {
        String kaptcha = (String) session.getAttribute("kaptcha");
        if (!kaptcha.equalsIgnoreCase(code) || StringUtils.isBlank(code)) {
            model.addAttribute("codeMsg", "验证码错误！");
            return "/site/login";
        }
        int expiredSeconds = rememberMe ? LoginTicketCode.REMEMBER_EXPIRED_SECONDS.getSeconds() : LoginTicketCode.DEFAULT_EXPIRED_SECONDS.getSeconds();
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        if (map.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            //设置cookie访问路径
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            //将cookie发送给客户端
            response.addCookie(cookie);
            return "redirect:/index";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "/site/login";
        }
    }

    @GetMapping("/logout")
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/login";
    }

}
