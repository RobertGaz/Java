package orlanda.dao.person;

import org.springframework.stereotype.Component;
import orlanda.models.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDaoJdbcApi implements PersonDaoInterface {

//    @Value("${db.url}")
    private static final String URL = "jdbc:mysql://localhost:3306/spring_app_db";
//    @Value("${db.username}")
    private static final String USERNAME = "root";
//    @Value("${db.password}")
    private static final String PASSWORD = "pony";

    private static Connection connection;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Person> getAll() {
        List<Person> people = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String SQL = "select * from person";
            System.out.println(SQL);
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                Person person = new Person();
                person.setId(resultSet.getInt("id"));
                person.setName(resultSet.getString("name"));
                person.setAge(resultSet.getInt("age"));
                person.setEmail(resultSet.getString("email"));

                people.add(person);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return people;
    }
    public Person getById(int id) {
        Person person = new Person();;
        try {
            Statement statement = connection.createStatement();
            String SQL = "select * from person where id = " + id;
            System.out.println(SQL);
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                person.setId(resultSet.getInt("id"));
                person.setName(resultSet.getString("name"));
                person.setAge(resultSet.getInt("age"));
                person.setEmail(resultSet.getString("email"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return person;

    }


    public void save(Person person) {
        try {
            Statement statement = connection.createStatement();
            String SQL = "insert into person(name, age, email) values ('" +
                    person.getName() + "', '" + person.getAge() + "', '" + person.getEmail() + "')";
            System.out.println(SQL);
            statement.executeUpdate(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(int id, Person person) {
        try {
            Statement statement = connection.createStatement();
            String SQL = "update person " +
                    "set name = '" + person.getName() +
                    "', age = " + person.getAge() +
                    ", email = '" + person.getEmail() +
                    "' where id = " + id;
            System.out.println(SQL);
            statement.executeUpdate(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try {
            Statement statement = connection.createStatement();
            String SQL = "delete from person where id = " + id;
            System.out.println(SQL);
            statement.executeUpdate(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
