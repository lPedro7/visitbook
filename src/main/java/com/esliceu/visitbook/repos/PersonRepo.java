package com.esliceu.visitbook.repos;

import com.esliceu.visitbook.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepo extends JpaRepository<Person,Integer> {

    Person getPersonByEmail(String email);

}
