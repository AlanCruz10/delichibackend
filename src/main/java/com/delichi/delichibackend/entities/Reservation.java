package com.delichi.delichibackend.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Calendar;

@Entity
@Setter
@Getter
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String date;

    @Column(nullable = false)
    @NotBlank
    private String hour;

    @Column(nullable = false, length = 255)
    private Integer people;

    @Column(nullable = false)
    private Integer tableNumber;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    private Restaurant restaurant;

    @ManyToOne
    private User user;

}


