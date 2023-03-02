package com.project.zaiika.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @GetMapping("/")
    public String test(){
        return "complete";
    }
}
