package org.pamdesa.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "endpoint")
@Data
public class Endpoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String path;

    // Getters and setters
}
