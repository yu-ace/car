package com.example.car.controller;

import com.example.car.service.IUserService;
import com.example.car.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;


@Controller
public class UserController {

    @Autowired
    IUserService userService;

    @RequestMapping(path = "register",method = RequestMethod.POST)
    public String register(
            @RequestParam(name="name")
            String name,
            @RequestParam(name="password")
            String password,Model model,HttpSession session){
        User user1 = (User) session.getAttribute("user");
        if(user1 == null){
            model.addAttribute("error","您已退出系统，请重新登陆");
            return "login";
        }
        userService.register(name,password);
        return "register";
    }

    @RequestMapping(path = "login",method = RequestMethod.POST)
    public String login(
            @RequestParam(name="name")
            String name,
            @RequestParam(name="password")
            String password, Model model, HttpSession session){
        User user = userService.getUser(name);
        if(user == null){
            model.addAttribute("error","该用户不存在！");
            return "login";
        }
        if(user.getPassword().equals(password)){
            session.setAttribute("user",user);
            return "redirect:/driveBoard";
        }else{
            model.addAttribute("error","密码错误！");
            return "login";
        }
    }

    @RequestMapping(path="changePassword",method = RequestMethod.POST)
    public String changePassword(
            @RequestParam(name="id")
            int id,
            @RequestParam(name="password")
            String password,
            @RequestParam(name="newPassword")
            String newPassword,
            @RequestParam(name="newPassword1")
            String newPassword1,Model model,HttpSession session){
        User user1 = (User) session.getAttribute("user");
            if(user1 == null){
                model.addAttribute("error","您已退出系统，请重新登陆");
                return "login";
            }
        User user = userService.getUserById(id);
        if(user == null){
            model.addAttribute("tip","该用户不存在！");
            return "changePassword";
        }
        if(user.getPassword().equals(password)){
            if(newPassword.equals(newPassword1)){
                model.addAttribute("tip","更改成功！");
                userService.changePassword(id,newPassword);
                return "changePassword";
            }else if(user.getPassword().equals(newPassword) || user.getPassword().equals(newPassword1)){
                model.addAttribute("tip","新密码与原密码相同，更改失败！");
                return "changePassword";
            }else{
                model.addAttribute("tip","两次密码输入不同，更改失败！");
                return "changePassword";
            }
        }else{
            model.addAttribute("tip","密码错误！");
            return "changePassword";
        }
    }

    @RequestMapping(path="delete",method = RequestMethod.POST)
    public String delete(
            @RequestParam(name="delete")
            int id,Model model,HttpSession session){
        User user1 = (User) session.getAttribute("user");
        if(user1 == null){
            model.addAttribute("error","您已退出系统，请重新登陆");
            return "login";
        }
        User user = userService.getUserById(id);
        if(user == null){
            model.addAttribute("tip1","该用户不存在！");
            return "delete";
        }else{
            userService.deleteUser(id);
            model.addAttribute("tip1","删除成功！");
            return "delete";
        }
    }
}
