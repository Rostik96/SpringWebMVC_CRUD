package com.rost.models;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor()
@ToString
public class Person {
    private int id;

    public Person() {}

    @NotEmpty(message = "The first name cannot be empty")
    @Size(min = 1, max = 30, message = "First name should be correct")
    private String firstName;

    @NotEmpty(message = "The last name cannot be empty")
    @Size(min = 2, max = 30, message = "Last name should be correct")
    private String lastName;

    @Min(value = 0, message = "Age should be equals or greater than 0")
    private int age;

    @NotEmpty(message = "The email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public Object[] getFieldsForCreateWithID() {
        return new Object[]{id, firstName, lastName, age, email};
    }
    public Object[] getFieldsForCreate() {
        return new Object[]{firstName, lastName, age, email};
    }

    public Object[] getFieldsForUpdate() {
        return new Object[]{firstName, lastName, age, email, id};
    }
}
