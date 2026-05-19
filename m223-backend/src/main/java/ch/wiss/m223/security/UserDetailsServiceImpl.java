package ch.wiss.m223.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ch.wiss.m223.model.Role;
import ch.wiss.m223.model.User;
import ch.wiss.m223.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
  @Autowired UserRepository userRepository;
  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
    List<GrantedAuthority> authorities = new ArrayList<>(); // important role to authorities mapping
    for (Role r : user.getRoles()) {
      authorities.add(new SimpleGrantedAuthority(r.toString()));
    }
    return UserDetailsImpl.build(user);
  }
}

