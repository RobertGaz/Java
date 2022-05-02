package orlanda.dao.person;

import org.springframework.stereotype.Component;
import orlanda.models.Person;

import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDaoArray implements PersonDaoInterface {

    private List<Person> people;

    {
        people = new ArrayList<>();
        people.add(new Person(1, "Robert", 23, "rob@gmail.com"));
        people.add(new Person(2, "Lera", 22, "lera@mail.ru"));
        people.add(new Person(3, "Ann", 19, "info@abc.ge"));
    }

    public List<Person> getAll() {
        return people;
    }

    public Person getById(int id) {
        return people.stream().filter(person -> person.getId() == id).findAny().orElse(null);
    }


    public void save(Person person) {
        person.setId(people.size() + 1);
        people.add(person);
    }

    public void update(int id, Person person) {
        getById(id).copy(person);
    }

    public void delete(int id) {
        people.removeIf(person -> person.getId() == id);
    }
}
