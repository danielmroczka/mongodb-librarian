/*
 * Copyright daniel.mroczka@gmail.com. All rights reserved. 
 */
package com.labs.dm.mongodb.librarian.web.controller;

import com.labs.dm.mongodb.librarian.core.Book;
import com.labs.dm.mongodb.librarian.core.SearchDAO;
import java.net.UnknownHostException;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author daniel
 */
@Controller
public class SearchController {

    @RequestMapping({"searchForm.do"})
    public ModelAndView searchForm(Model model) {
        return new ModelAndView("search");
    }

    @RequestMapping(value = {"search.do"}, method = {RequestMethod.POST})
    public ModelAndView search(@RequestParam("title") String title) throws UnknownHostException {
        SearchDAO dao = new SearchDAO();
        List<Book> list  = dao.find(title);
        ModelAndView mav = new ModelAndView("results", "list", list);
        return mav;
    }

}
