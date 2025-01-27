package org.pamdesa.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "role_endpoint")
public class RoleEndpoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "endpoint_id")
    private Endpoint endpoint;

    private String method;
}
