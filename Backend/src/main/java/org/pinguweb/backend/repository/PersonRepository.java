package org.pinguweb.backend.repository;

import org.pinguweb.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, String> {
}
