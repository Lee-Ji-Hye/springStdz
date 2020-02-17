package com.tony.sb_java_code.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@Profile("default")
@Controller
@RequestMapping("/view")
public class HelloController {

    /**
     * 요청시 화면으로 이동
     * /resources/templates/{path}.html
     * @return
     */
    @GetMapping
    public String template(Model model) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "한글과 컴퓨터");
        map.put("목록", Arrays.asList("누구","세요"));

        model.addAttribute("title", "hello world");
        model.addAttribute("koTitle", "안녕하세요 월드");
        model.addAttribute("jpTitle", "こんにちは世界");
        model.addAttribute("chTitle", "你好，世界");
        model.addAttribute("map", map);
        return "index";
    }
}
