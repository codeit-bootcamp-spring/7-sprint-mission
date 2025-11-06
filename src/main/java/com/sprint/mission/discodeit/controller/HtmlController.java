package com.sprint.mission.discodeit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HtmlController {

    @RequestMapping(value = "/show")
    public String showWebPage(){
        return "user-list-bonobono";
    }
}
