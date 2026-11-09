package com.example.myspringbootprojectsession12.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "medical_supplies")
@Getter
@Setter
@NoArgsConstructor
public class Supply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    private String specification;
    private String provider;

    @Column(nullable = false)
    private int quantity = 0;

    @Column(nullable = false)
    private boolean isDeleted = false;

    public Supply(String name, String specification, String provider) {
        this.name = name;
        this.specification = specification;
        this.provider = provider;
    }
}
