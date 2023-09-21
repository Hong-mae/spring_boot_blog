package com.moong_bee.blog.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.Getter;
import lombok.Setter;

@Controller
public class ExampleController {

    @GetMapping("/thymeleaf/example")
    public String thymeExample(Model model) {
        Person examplePerson = new Person();
        examplePerson.setId(1L);
        examplePerson.setName("홍길동");
        examplePerson.setAge(11);
        examplePerson.setHobbies(List.of("혼코노", "코딩"));

        model.addAttribute("person", examplePerson);
        model.addAttribute("today", LocalDateTime.now());

        return "example";
    }

    @Setter
    @Getter
    class Person {
        private Long id;
        private String name;
        private int age;
        private List<String> hobbies;
    }
}
