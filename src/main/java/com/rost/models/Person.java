package com.rost.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Person {
    private int id;
    
    @NotEmpty
    @Size(min = 2, max = 30, message = "First name should be correct")
    private String firstName;

    @NotEmpty
    @Size(min = 2, max = 30, message = "Last name should be correct")
    private String lastName;

    @Min(value = 0, message = "Age should be equals or greater than 0")
    private int age;

    @NotEmpty
    @Email(message = "Email should be valid")
    private String email;

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }
}
