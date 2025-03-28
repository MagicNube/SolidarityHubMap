package org.pinguweb.backend.service;

import org.pinguweb.model.Person;
import org.springframework.stereotype.Service;
import org.pinguweb.backend.repository.PersonRepository;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    public PersonService(PersonRepository personRepository) {this.personRepository = personRepository;}
    public Person savePerson(Person person) {return personRepository.save(person);}
}
