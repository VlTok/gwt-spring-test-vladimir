package com.tokmeninov.gwt.server.repository;

import com.tokmeninov.gwt.server.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person,Long> {
    void deleteById(Long id);
}
