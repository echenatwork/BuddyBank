package web.controllers;

import db.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class GreetingController {

    @Value("${name}")
    private String foo;

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model, HttpServletRequest httpServletRequest) {
        SecurityContextHolder.getContext().getAuthentication().getPrincipal();



        model.addAttribute("name", name);

        // returns the view name
        return "greeting";
    }

}