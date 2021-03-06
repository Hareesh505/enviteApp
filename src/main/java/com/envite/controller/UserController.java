package com.envite.controller;

import com.envite.model.User;
import com.envite.service.UserService;
import com.envite.utils.EVUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hareesh on 1/7/2017.
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/envite/findUser")
    public String findUserByUserName(ModelMap model,
                                     @RequestParam("userName") String userName) {
        model.addAttribute("message", "Welcome");
        model.addAttribute("user",userService.findByUserName(userName));
        return "user";
    }

    @RequestMapping(value = "/ev/updateUser")
    @ResponseBody
    public String updateUserDetails(@RequestParam("firstName") String firstName,
                                    @RequestParam("lastName") String lastName,
                                    @RequestParam("email") String email,
                                    @RequestParam("phoneNumber") String phoneNumber) {
        return EVUtils.convertObjectTOJSONString(userService.updateUserDetails(firstName,lastName,phoneNumber,email,userService.getCurrentUserDO()));
    }


}
