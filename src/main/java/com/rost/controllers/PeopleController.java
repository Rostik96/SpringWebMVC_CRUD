package com.rost.controllers;

import com.rost.DAO.PersonDAO;
import com.rost.models.Person;
import javax.validation.Valid;

import com.rost.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/people")
public class PeopleController {
    private final PersonDAO personDAO;
    private final PersonValidator personValidator;

    @Autowired
    public PeopleController(PersonDAO personDAO, PersonValidator personValidator) {
        this.personDAO = personDAO;
        this.personValidator = personValidator;
    }

    @GetMapping("/new")
    public String createNewPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping()
    public String createNewPerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors())
            return "people/new";
        System.out.println(person);
        personDAO.createPerson(person);
        return "redirect:/people";
    }

    @GetMapping()
    public String readAllPerson(Model model) {
        model.addAttribute("people", personDAO.readAllPerson());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String readPersonById(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personDAO.readPersonById(id));
        return "people/show";
    }

    @GetMapping("/edit/{id}")
    public String updatePerson(@PathVariable("id") int id, Model model) {
        System.out.println("updatePerson");
        model.addAttribute("person", personDAO.readPersonById(id));
        return "people/edit";
    }

    @PatchMapping()
    public String updatePerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "people/edit";
        personDAO.updatePerson(person);
        return "people/show";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") int id) {
        personDAO.deletePerson(id);
        return "redirect:/people";
    }
}