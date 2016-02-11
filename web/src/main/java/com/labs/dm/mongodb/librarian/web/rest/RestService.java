/*
 * Copyright daniel.mroczka@gmail.com. All rights reserved. 
 */
package com.labs.dm.mongodb.librarian.web.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author daniel
 */
@Controller
@RequestMapping("/book")
public class RestService {

    @RequestMapping("/get/{isbn}")
    public @ResponseBody
    String hello(@PathVariable String isbn) {
        return "OK" + isbn;
    }

    @RequestMapping("/get/{isbn}")
    public @ResponseBody
    String readAll(@PathVariable String isbn) {
        return "OK" + isbn;
    }

    @RequestMapping("/hello")
    public @ResponseBody
    String hello() {
        return "OK";
    }

    /*
     @RequestMapping("/book")
     public @ResponseBody
     Book greeting(@RequestParam(value = "id", required = false) String id) {
     System.out.println("get!");
     return new Book("dummy");
     //return new Book(counter.incrementAndGet(),
     //                  String.format(template, name));
     }*/
}
