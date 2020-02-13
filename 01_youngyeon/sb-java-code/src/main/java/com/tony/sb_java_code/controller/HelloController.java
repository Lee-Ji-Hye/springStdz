package com.tony.sb_java_code.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Profile("dev")
@Controller
public class HelloController {

    /**
     * 요청시 화면으로 이동
     * /resources/templates/{path}.html
     * @param path
     * @return
     */
    @GetMapping("/{path}")
    public String template(@PathVariable String path, Model model) {
        model.addAttribute("titleName", "hello world");
        return path;
    }
}
