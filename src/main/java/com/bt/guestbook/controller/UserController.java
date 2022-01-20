package com.bt.guestbook.controller;

import com.bt.guestbook.model.Comment;
import com.bt.guestbook.model.User;
import com.bt.guestbook.service.CommentService;
import com.bt.guestbook.service.UserService;
import com.bt.guestbook.utils.PassEncoding;
import com.bt.guestbook.utils.Roles;
import com.bt.guestbook.utils.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    GlobalController globalController;

    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;

    @RequestMapping("/")
    public String root(Model model) {
        model.addAttribute("reqUser", new User());
        logger.info("root");
        return "login";
    }

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("reqUser", new User());
        logger.info("login");
        return "login";
    }

    @RequestMapping("/home")
    public String home(Model model) {
        Comment comment = new Comment();
        model.addAttribute("reqComment", comment);
        model.addAttribute("allComment", commentService.findByUserIdStatus(globalController.getLoginUser().getId(), Status.ACTIVE.getValue()));
        model.addAttribute("allPassiveComment", commentService.findByUserIdStatus(globalController.getLoginUser().getId(), Status.PASSIVE.getValue()));
        logger.info("home");
        return "home";
    }

    @RequestMapping("/admin")
    public String helloAdmin(Model model) {
        Comment comment = new Comment();
        model.addAttribute("reqComment", comment);
        model.addAttribute("allComment",commentService.findAll());
        model.addAttribute("allPassiveComment", commentService.findByUserIdStatus(globalController.getLoginUser().getId(), Status.PASSIVE.getValue()));
        logger.info("admin");
        return "admin";
    }

    @RequestMapping("/register")
    public String register(Model model) {
        model.addAttribute("reqUser", new User());
        logger.info("register");
        return "register";
    }

    @RequestMapping(value = {"/user/register"}, method = RequestMethod.POST)
    public String register(@ModelAttribute("reqUser") User reqUser,
                           final RedirectAttributes redirectAttributes) {

        logger.info("/user/register");
        User user = userService.findByUserName(reqUser.getUsername());
        if (user != null) {
            redirectAttributes.addFlashAttribute("saveUser", "exist-name");
            return "redirect:/register";
        }
        user = userService.findByEmail(reqUser.getEmail());
        if (user != null) {
            redirectAttributes.addFlashAttribute("saveUser", "exist-email");
            return "redirect:/register";
        }

        reqUser.setPassword(PassEncoding.getInstance().passwordEncoder.encode(reqUser.getPassword()));
        reqUser.setRole(Roles.ROLE_USER.getValue());

        if (userService.save(reqUser) != null) {
            redirectAttributes.addFlashAttribute("saveUser", "success");
        } else {
            redirectAttributes.addFlashAttribute("saveUser", "fail");
        }

        return "redirect:/register";
    }
}