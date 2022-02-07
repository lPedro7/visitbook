package com.esliceu.visitbook.services;


import com.esliceu.visitbook.models.Entry;
import com.esliceu.visitbook.models.Person;
import com.esliceu.visitbook.repos.EntryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class EntryService {

    @Autowired
    EntryRepo entryRepo;

    @Autowired
    HttpSession session;

    public void newEntry(String text) {
        Entry entry = new Entry();

        Person person = (Person) session.getAttribute("person");
        entry.setIduser(person.getId());
        entry.setPerson(person);
        entry.setText(text);


        entryRepo.save(entry);
    }

    public List<Entry> findAll() {

        return entryRepo.getByOrderByIdDesc();
    }

    public Entry getEntryById(String id) throws NumberFormatException {
        return entryRepo.getById(Integer.valueOf(id));
    }

    public void updateEntry(String id, String text) throws NumberFormatException {

        Entry entry = entryRepo.getById(Integer.valueOf(id));
        entry.setText(text);
        entryRepo.save(entry);


    }

    public void delete(String id) throws NumberFormatException{
        Entry entry = entryRepo.getById(Integer.valueOf(id));
        entryRepo.delete(entry);
    }
}
