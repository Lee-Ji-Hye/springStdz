package com.tony.sb_java_code.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/", "/index"})
public class IndexController {
    @GetMapping
    public String index(Model model) {
        model.addAttribute("title", "helloWorld");
        model.addAttribute("koTitle", "한글도 잘되요.");
        return "index";
    }
}
