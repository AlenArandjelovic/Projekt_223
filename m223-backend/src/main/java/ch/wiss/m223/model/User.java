package ch.wiss.m223.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter @Getter @NoArgsConstructor
@Table(name="users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
    
  @NotBlank
  private String username;
  @NotBlank
  private String email;
  @JsonIgnore
  @NotBlank
  private String password;
    
  public User(String name, String email, String password) {
      this.username = name;
      this.email = email;
      this.password = password;
  }
  @ManyToMany(fetch = FetchType.LAZY) // N:M Mapping
  private Set<Role> roles = new HashSet<>();
}


