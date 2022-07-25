package com.rost.controllers;

import com.rost.DAO.PersonDAO;
import com.rost.models.Person;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/people")
public class PeopleController {
    private final PersonDAO personDAO;

    @Autowired
    public PeopleController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GetMapping("/new")
    public String createNewPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping()
    public String createNewPerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "people/new";
        System.out.println(person);
        personDAO.createPerson(person);
        return "redirect:/people";
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