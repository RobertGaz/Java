package orlanda.dao.person;

import orlanda.models.Person;

import java.util.List;

public interface PersonDaoInterface {
    List<Person> getAll();

    Person getById(int id);

    void save(Person person);

    void update(int id, Person person);

    void delete(int id);

}
