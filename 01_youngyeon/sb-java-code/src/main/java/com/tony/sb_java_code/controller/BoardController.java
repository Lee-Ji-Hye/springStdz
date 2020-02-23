package com.tony.sb_java_code.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view")
public class BoardController {

    @GetMapping("/board")
    public String getBoard(Model model) {
        model.addAttribute("title", "스프링 연구소");
        return "board";
    }
}
