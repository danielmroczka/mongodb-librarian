/*
 * Copyright daniel.mroczka@gmail.com. All rights reserved. 
 */
package com.labs.dm.mongodb.librarian.web.controller;

import com.labs.dm.mongodb.librarian.core.Book;
import com.labs.dm.mongodb.librarian.core.SearchDAO;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @RequestMapping(value = "ajax.search.do", method = RequestMethod.GET, headers = "Accept=*/*", produces = "application/json")
    private @ResponseBody
        List<Book> find(@RequestParam("title") String title) throws UnknownHostException {
        SearchDAO dao = new SearchDAO();
        List<Book> list = dao.find(title, null);
        return list;
    }

}
