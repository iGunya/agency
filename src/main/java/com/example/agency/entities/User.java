package com.example.agency.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_user;

    @Size(min = 2, max = 25, message = "Недопустимый логин имени")
    private String login;

    private String password;

    private String role;

    @ManyToMany(cascade = CascadeType.ALL,fetch= FetchType.EAGER)
    @JoinTable( name="user_object",
            joinColumns = @JoinColumn(name="id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_object"))
    private List<Object> objects=new ArrayList<>();

    public User(Long id_user,  String login, String password, String role) {
        this.id_user = id_user;
        this.login = login;
        this.password = password;
        this.role = role;
    }
}
