package ch.wiss.m223.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class JwtResponse{
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
  
    public JwtResponse(String accessToken, 
Long id, String userName, 
String email, List<String> roles) {
      this.token = accessToken;
      this.id = id;
      this.username = userName;
      this.email = email;
      this.roles = roles;
    }
}

