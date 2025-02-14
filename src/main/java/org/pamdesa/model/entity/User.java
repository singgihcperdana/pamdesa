package org.pamdesa.model.entity;

import lombok.Data;
import org.pamdesa.model.entity.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "username"),
    @UniqueConstraint(columnNames = "email"), @UniqueConstraint(columnNames = "phone_number")})
@Data
public class User extends BaseEntity implements Serializable {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

  @NotBlank @Size(max = 20) private String username;

  @NotBlank @Size(max = 50) @Email private String email;

  @NotBlank @Size(max = 50) @Column(name = "full_name") private String fullName;

  @NotBlank @Size(max = 15) @Column(name = "phone_number") private String phoneNumber;

  @Size(max = 255) private String address;

  @NotBlank @Size(max = 120) private String password;

  private boolean active;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ValidToken> validTokens;

  @Version private Integer version;
}
