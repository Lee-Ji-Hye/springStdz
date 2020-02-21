package com.tony.sb_java_code.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/jsp")
public class JspController {
    @GetMapping("/hello")
    public String hello(Model m) {
        m.addAttribute("message", "hello");
        return "index";
    }
}
