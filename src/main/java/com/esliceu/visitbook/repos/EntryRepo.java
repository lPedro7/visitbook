package com.esliceu.visitbook.repos;

import com.esliceu.visitbook.models.Entry;
import com.esliceu.visitbook.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryRepo extends JpaRepository<Entry,Integer> {

    List<Entry> getByOrderByIdDesc();

}
