package com.delichi.delichibackend.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "ceos")
public class Ceo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false, length = 255)
    @NotBlank
    private String name;

    @Column(length = 255)
    private String firstSurname;

    @Column(length = 255)
    private String secondSurname;

    @Column(nullable = false, unique = true, length = 255)
    @Email
    private String email;

    @Column(nullable = false, length = 255)
    @NotBlank
    private String password;

    @Column(nullable = false, unique = true)
    private Long phoneNumber;

    @OneToMany(mappedBy = "ceo")
    private List<Restaurant> restaurants;

    @OneToMany(mappedBy = "ceo")
    private List<Image> images;

}
