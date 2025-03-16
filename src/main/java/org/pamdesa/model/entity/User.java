package org.pamdesa.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pamdesa.model.entity.base.BaseEntity;
import org.pamdesa.model.enums.UserRole;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "username"),
    @UniqueConstraint(columnNames = "email"), @UniqueConstraint(columnNames = "phone_number")})
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity implements Serializable {

  @NotBlank
  @Size(max = 20)
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(max = 50)
  @Column(name = "full_name")
  private String fullName;

  @NotBlank
  @Size(max = 15)
  @Column(name = "phone_number")
  private String phoneNumber;

  @Size(max = 255)
  private String address;

  @NotBlank
  @Size(max = 120)
  private String password;

  private boolean active;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "organization_id", nullable = false)
  private Organization organization;

  @Column(name = "user_role")
  @Enumerated(EnumType.STRING)
  private UserRole userRole;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ValidToken> validTokens;

}
