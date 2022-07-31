package com.rost.models;

import javax.validation.constraints.*;

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

    //Structure: Country, City, postal code(6 digits).
    @Pattern(regexp = "[A-Z]\\w+, [A-Z]\\w+, \\d{6}", message = "Your address should be in this format: Country, City, postal code(6 digits)")
    private String address;

    @Min(value = 0, message = "Age should be equals or greater than 0")
    private int age;

    @NotEmpty(message = "The email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public Object[] getFieldsForCreateWithID(int id) {
        return new Object[]{id, firstName, lastName, address, age, email};
    }
    public Object[] getFieldsForCreate() {
        return new Object[]{firstName, lastName, age, email, address};
    }

    public Object[] getFieldsForUpdate() {
        return new Object[]{firstName, lastName, address, age, email, id};
    }
}
