package se.danielmartensson.JLoggerServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.danielmartensson.JLoggerServer.model.Online;

@Repository
public interface OnlineRepository extends JpaRepository<Online, Long> {

	Online findByUsername(String username);

	Online findByDevice(String devicename);
}