/*
 * Copyright daniel.mroczka@gmail.com. All rights reserved. 
 */

package com.labs.dm.mongodb.librarian.web.controller;

import com.labs.dm.mongodb.librarian.web.WebConfiguration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

/**
 *
 * @author daniel
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebConfiguration.class)
public class HomeControllerTest {

    @Autowired
    private HomeController controller;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private HandlerAdapter handlerAdapter;
   // private MockMvc mockMvc;

    @Before
    public void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        handlerAdapter = new AnnotationMethodHandlerAdapter();
       // mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void home() throws Exception {
        request.setRequestURI("/home.do");
        final ModelAndView mav = handlerAdapter.handle(request, response, controller);
        assertEquals("index", mav.getViewName());
    }

    @Test
    public void testMessage() {
        assertNotNull("Constructor message instance is null.", controller);
        assertEquals("index", controller.homePage(new BindingAwareModelMap()).getViewName());
    }

    @Test
    public void testHandleLogin() throws Exception {
       // mockMvc.perform(get("/home.do")).andExpect(status().isOk()).andExpect(view().name("index"));
    }
}