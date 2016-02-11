/*
 * Copyright daniel.mroczka@gmail.com. All rights reserved. 
 */
package com.labs.dm.mongodb.librarian.web.controller;

import com.labs.dm.mongodb.librarian.core.Book;
import com.labs.dm.mongodb.librarian.core.SearchDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.net.UnknownHostException;
import java.util.List;

/**
 * @author daniel
 */
@Controller
public class SearchController {

    @RequestMapping({"searchForm.do"})
    public ModelAndView searchForm(Model model) {
        return new ModelAndView("search");
    }

    @RequestMapping({"ajax.search.do"})
    public @ResponseBody
            
    List<Book> find(@RequestParam("title") String title) throws UnknownHostException {
        SearchDAO dao = new SearchDAO();
        return dao.find(title, null);
    }

}
