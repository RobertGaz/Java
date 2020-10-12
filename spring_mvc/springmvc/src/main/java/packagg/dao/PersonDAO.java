package packagg.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import packagg.models.Person;

@Component
public class PersonDAO {
	private List<Person> people;
	
	{
		people = new ArrayList<>();
		
		people.add(new Person(1, "Rob"));
		people.add(new Person(2, "Pavel"));
		people.add(new Person(3, "Yobo"));
	}
	
	public List<Person> findAll() {
		return people;
	}
	
	public Person find(int id) {
		return people.stream().filter(person -> person.getId() == id).findAny().orElse(null);
	}
	
	public void save(Person person) {
		person.setId(people.size()+1);
		people.add(person);
	}
}
