package com.testautomation.mesaj.models.users;

import io.cucumber.java.eo.Se;
import lombok.*;

@EqualsAndHashCode(exclude = {"lastName"})
@Data
public class Foo {

    private String name;
    private String lastName;
    private String email;
    private int age;
}
