package com.pluxity.webflux;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("users")
@Getter
public class User {

    @Id
    private Long id;

    private String name;
    private String email;


    public void update(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @Builder
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

}
