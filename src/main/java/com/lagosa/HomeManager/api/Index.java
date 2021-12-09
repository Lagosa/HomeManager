package com.lagosa.HomeManager.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class Index {

    @GetMapping(path ="")
    public String index(){
        return "Hello!";
    }
}
