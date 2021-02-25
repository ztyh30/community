package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

/**
 * @Author ztyh
 * @Description UserController
 * @Date 2021/2/19 16:37
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private HostHolder hostHolder;

    @GetMapping("/setting")
    @LoginRequired
    public String toSettingPage() {
        return "/site/setting";
    }

    @PostMapping("/upload")
    @LoginRequired
    public String uploadHeader(MultipartFile image, Model model) {
        if (image == null) {
            model.addAttribute("error", "没有上传文件！请上传！");
            return "/site/setting";
        }
        String fileName = image.getOriginalFilename();
        //获取文件后缀名
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "文件格式错误！请重新上传！");
            return "/site/setting";
        }
        //生成随机文件名
        fileName = CommunityUtil.getUUID() + suffix;
        //文件存储到本地磁盘
        File dest = new File(uploadPath + "/" + fileName);
        try {
            //将文件存储到本地磁盘
            image.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败：" + e.getMessage());
            throw new RuntimeException("上传文件失败,服务器发生异常!", e);
        }
        //获取当前用户
        User user = hostHolder.getUser();
        //更新用户头像(web访问路径)
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeaderUrl(headerUrl, user.getId());
        return "redirect:/index";
    }

    @GetMapping("/header/{fileName}")
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        //本地磁盘存放路径
        fileName = uploadPath + "/" + fileName;
        //获取文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        //响应图片
        response.setContentType("image/" + suffix);
        try (FileInputStream fis = new FileInputStream(fileName);
             ServletOutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int index = 0;
            while ((index = fis.read(buffer)) != -1) {
                os.write(buffer, 0, index);
            }
        } catch (FileNotFoundException e) {
            logger.error("文件读取异常：" + e.getMessage());
        } catch (IOException e) {
            logger.error("读取头像失败：" + e.getMessage());
        }
    }

    @PostMapping("/updatePassword")
    @LoginRequired
    public String updatePassword(String oldPwd, String newPwd, Model model, String confirmPwd) {
        //获取当前user对象
        User user = hostHolder.getUser();
        Map<String, Object> map = userService.updatePassword(oldPwd, newPwd, confirmPwd, user);
        if (map == null || map.isEmpty()) {
            return "redirect:/logout";
        }
        model.addAttribute("oldPwdMsg", map.get("oldPwdMsg"));
        model.addAttribute("newPwdMsg", map.get("newPwdMsg"));
        model.addAttribute("confirmPwd", map.get("confirmPwd"));
        return "/site/setting";
    }
}
