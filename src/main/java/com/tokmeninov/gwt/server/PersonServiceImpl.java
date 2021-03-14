package com.tokmeninov.gwt.server;

import com.tokmeninov.gwt.client.PersonService;
import com.tokmeninov.gwt.server.domain.Person;
import com.tokmeninov.gwt.server.repository.PersonRepository;
import com.tokmeninov.gwt.server.service.AppContext;
import com.tokmeninov.gwt.shared.PersonResp;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.ArrayList;
import java.util.List;

/**
 * The server side implementation of the RPC service.
 */
public class PersonServiceImpl extends RemoteServiceServlet implements
        PersonService {

  private final PersonRepository persons = AppContext.getBean(PersonRepository.class);

  @Override
  public List<PersonResp> list() {
    List<PersonResp> people = new ArrayList<>();
    for (Person person : persons.findAll()){
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
    persons.save(person);
    data.setId((int)person.getId());
    return data;
  }

  @Override
  public boolean delete(PersonResp person) {
    persons.deleteById((int) person.getId());
    return true;
  }
}
