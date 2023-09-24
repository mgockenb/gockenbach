package org.gockenbach.mySpringProject.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class HelloController {
    private String msg;
    private String author;

    public HelloController() {
        // Initialize default values
        this.msg = "Hello World!";
        this.author = "HoTownHandler";
    }

    @GetMapping("/helloworld")
    public String HelloWorld() throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        String jsonMessage = objectMapper.writeValueAsString(this);

        return this.msg;
    }

    @GetMapping("/author")
    public String Author() throws Exception {
        return this.author;
    }
}