package ch.wiss.m223;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import ch.wiss.m223.model.Role;

import ch.wiss.m223.model.ERole;
import ch.wiss.m223.repository.RoleRepository;

@SpringBootApplication

public class M223Application  implements CommandLineRunner {
  @Autowired
RoleRepository roleRepository;
	public static void main(String[] args) {
		SpringApplication.run(M223Application.class, args);
	}

  @Override
public void run(String... args) throws Exception {
if (roleRepository.count() == 0) {
roleRepository.save(new Role(ERole.ROLE_USER));
roleRepository.save(new Role(ERole.ROLE_ADMIN));
}
}
}
