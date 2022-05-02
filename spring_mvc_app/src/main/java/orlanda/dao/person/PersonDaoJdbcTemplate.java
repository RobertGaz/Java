package orlanda.dao.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import orlanda.models.Person;

import java.sql.*;
import java.util.List;

@Component
public class PersonDaoJdbcTemplate implements PersonDaoInterface {

    class PersonMapper implements RowMapper<Person> {
        @Override
        public Person mapRow(ResultSet resultSet, int i) throws SQLException {
            Person person = new Person();
            person.setId(resultSet.getInt("id"));
            person.setName(resultSet.getString("name"));
            person.setAge(resultSet.getInt("age"));
            person.setEmail(resultSet.getString("email"));

            return person;
        }
    }
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Person> getAll() {
        return jdbcTemplate.query("select * from person", new PersonMapper());
    }
    public Person getById(int id) {
        return jdbcTemplate.query("select * from person where id = ? ", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
    }


    public void save(Person person) {
        jdbcTemplate.update("insert into person(name, age,email) values (?, ?, ?)",
                person.getName(), person.getAge(), person.getEmail());

    }

    public void update(int id, Person person) {
        jdbcTemplate.update("update person set name = ?, age = ?, email=? where id = ?",
                person.getName(), person.getAge(), person.getEmail(), person.getId());
    }

    public void delete(int id) {
        jdbcTemplate.update("delete from person where id = ?", id);
    }
}
