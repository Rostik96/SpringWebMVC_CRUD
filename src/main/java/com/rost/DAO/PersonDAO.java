package com.rost.DAO;

import com.rost.models.Person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;
    private int currentMaxId;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        currentMaxId = jdbcTemplate.queryForObject("SELECT max(id) FROM person", Integer.class);
    }



    private final BeanPropertyRowMapper<Person> personMapper = new BeanPropertyRowMapper<>(Person.class);

    public void createPerson(Person person) {
        jdbcTemplate.update("INSERT INTO person VALUES(?, ?, ?, ?, ?)", ++currentMaxId, person.getFirstName(), person.getLastName(), person.getAge(), person.getEmail());
    }

    public List<Person> readAllPerson() {
        return jdbcTemplate.query("SELECT * FROM person", personMapper);
    }

    public Person readPersonById(int id) {
        return jdbcTemplate.query("SELECT * FROM person WHERE id = ?", new Object[]{id}, personMapper).stream()
                .findAny()
                .orElse(null);
    }
}