package com.rost.controllers;

import com.rost.DAO.PersonDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/people")
public class PeopleController {
    private final PersonDAO personDAO;

    @Autowired
    public PeopleController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String readAllPerson(Model model) {
        System.out.println("readAllPerson");
        model.addAttribute("people", personDAO.readAllPerson());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String readPersonById(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personDAO.readPersonById(id));
        return "people/show";
    }
}