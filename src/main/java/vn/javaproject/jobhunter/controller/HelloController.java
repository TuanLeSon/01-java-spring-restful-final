package vn.javaproject.jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.javaproject.jobhunter.util.error.IdInvalidException;

@RestController
public class HelloController {

    @GetMapping("/")
    public String getHelloWorld() throws IdInvalidException {
        // if (true)
        // throw new IdInvalidException("Id ko lon hon 1500");
        return "Hello World (Hỏi Dân IT & Eric)";
    }
}
