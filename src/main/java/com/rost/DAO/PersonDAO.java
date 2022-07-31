package com.rost.DAO;

import com.rost.models.Person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    private int currentMaxID;

    private void updateCurrentMaxID() {
        currentMaxID = getCurrentMaxID();
    }
    private int getCurrentMaxID() {
        currentMaxID = Objects.requireNonNullElse(jdbcTemplate.queryForObject("SELECT max(id) FROM person", Integer.class), 0);
        return currentMaxID;
    }

    private void alterSequenceForPerson_id() {
        updateCurrentMaxID();
        jdbcTemplate.update("ALTER SEQUENCE person_id_seq RESTART WITH " + (currentMaxID + 1));
    }

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        alterSequenceForPerson_id();
    }


    private final BeanPropertyRowMapper<Person> personMapper = new BeanPropertyRowMapper<>(Person.class);

    public void createPerson(Person person) {
        jdbcTemplate.update("INSERT INTO person(first_name, last_name, age, email) VALUES(?, ?, ?, ?)", person.getFieldsForCreate());
    }

    public List<Person> readAllPerson() {
        return jdbcTemplate.query("SELECT * FROM person ORDER BY id", personMapper);
    }

    public Person readPersonById(int id) { //stream'ы появлись в Java 8
        return jdbcTemplate.query("SELECT * FROM person WHERE id = ?", new Object[]{id}, personMapper).stream()
                .findAny()
                .orElse(null);
    }

    public Optional<Person> readPersonByEmail(String email) {
        return jdbcTemplate.query("SELECT * FROM person WHERE email = ?", new Object[]{email}, personMapper).stream()
                .findAny();
    }

    public void updatePerson(Person person) {
        jdbcTemplate.update("UPDATE person SET first_name = ?, last_name = ?, age = ?, email = ? WHERE id = ?", person.getFieldsForUpdate());
    }

    public void deletePerson(int id) {
        jdbcTemplate.update("DELETE FROM person WHERE id = ?", id);
        alterSequenceForPerson_id();
    }

    //Testing performance of batch update

    public void testMultipleUpdate() {
        List<Person> people = create1000Person();
        long before = System.currentTimeMillis();
        alterSequenceForPerson_id();
        for (Person person : people) {
            jdbcTemplate.update("INSERT INTO person(first_name, last_name, age, email) VALUES (?, ?, ?, ?)", person.getFieldsForCreate());
        }
        long after = System.currentTimeMillis();
        System.out.printf("Time = %ds%n", (after - before) / 1000);
    }

    public void testBatchUpdate() {
        List<Person> people = create1000Person();
        long before = System.currentTimeMillis();
        alterSequenceForPerson_id();
        jdbcTemplate.batchUpdate("INSERT INTO person(first_name, last_name, age, email) VALUES (?, ?, ?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, people.get(i).getFirstName());
                ps.setString(2, people.get(i).getLastName());
                ps.setInt(3, people.get(i).getAge());
                ps.setString(4, people.get(i).getEmail());
            }

            @Override
            public int getBatchSize() {
                return people.size();
            }
        });
        long after = System.currentTimeMillis();
        System.out.printf("Time = %ds%n", (after - before) / 1000);
    }

    private List<Person> create1000Person() {
        List<Person> people = new ArrayList<>(1000);
        int currentMaxId = getCurrentMaxID();
        for (int i = 0; i < 1000; i++) {
            people.add(new Person(++currentMaxId, "SomeFirstName#" + i, "SomeLastName#" + i, 322, "testEmail" + i + "@mail.ru"));
        }
        return people;
    }
}