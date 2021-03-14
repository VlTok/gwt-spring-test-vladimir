package com.tokmeninov.gwt.server.repository;

import com.tokmeninov.gwt.server.domain.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person,Integer> {
    void deleteById(int id);
}
