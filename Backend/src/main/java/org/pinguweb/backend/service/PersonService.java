package org.pinguweb.backend.service;

import org.pinguweb.backend.model.Person;
import org.pinguweb.backend.repository.PersonRepository;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    public PersonService(PersonRepository personRepository) {this.personRepository = personRepository;}
    public Person savePerson(Person person) {return personRepository.save(person);}
}
