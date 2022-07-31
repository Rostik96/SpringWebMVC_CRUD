package com.rost.util;

import com.rost.DAO.PersonDAO;
import com.rost.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component //Хотим, чтобы Spring создал bean и внедрил зависимость.
public class PersonValidator implements Validator {
    private final PersonDAO personDAO;

    @Autowired //Spring може внедрить и без этого.
    public PersonValidator(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    //Будет вызываться в PeopleController для obj класса Person, который приходит с формы.
    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        //Есть ли человек с таким же email в БД?
        if (personDAO.readPersonByEmail(person.getEmail()).isPresent()) { //проверка на null – mauvais ton
            errors.rejectValue("email", "", "This email is already taken");
        }
    }
}
