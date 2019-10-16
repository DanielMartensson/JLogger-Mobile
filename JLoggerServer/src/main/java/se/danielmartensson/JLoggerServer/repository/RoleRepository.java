package se.danielmartensson.JLoggerServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.danielmartensson.JLoggerServer.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}