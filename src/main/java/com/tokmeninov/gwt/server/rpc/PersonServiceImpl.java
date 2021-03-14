package com.tokmeninov.gwt.server.rpc;

import com.tokmeninov.gwt.client.PersonService;
import com.tokmeninov.gwt.server.domain.Person;
import com.tokmeninov.gwt.server.repository.PersonRepository;
import com.tokmeninov.gwt.shared.PersonResp;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("GwtServiceNotRegistered")
public class PersonServiceImpl extends RemoteServiceServlet implements PersonService {

  @Autowired
  private PersonRepository personRepository;

  @Override
  public List<PersonResp> list() {
    List<PersonResp> people = new ArrayList<>();
    for (Person person : personRepository.findAll()){
      people.add(new PersonResp(
              (int)person.getId(),
              person.getName(),
              person.getSurname(),
              person.getPatronymic()));
    }
    return people;
  }

  @Override
  public PersonResp save(PersonResp data) {
    Person person = new Person();
    person.setId((int)data.getId());
    person.setName(data.getName());
    person.setSurname(data.getSurname());
    person.setPatronymic(data.getPatronymic());
    personRepository.save(person);
    data.setId((int)person.getId());
    return data;
  }

  @Override
  public boolean delete(PersonResp person) {
    personRepository.deleteById((int) person.getId());
    return true;
  }
}
