package orlanda.dao.person;

import org.springframework.stereotype.Component;
import orlanda.models.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDaoJdbcApiPreparedStatement implements PersonDaoInterface {

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
            String SQL = "select * from person";
            PreparedStatement statement = connection.prepareStatement(SQL);
            System.out.println(statement);
            ResultSet resultSet = statement.executeQuery();

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
        Person person = null;
        try {
            String SQL = "select * from person where id = ?";
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setInt(1, id);
            System.out.println(statement);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                person = new Person();
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
            String SQL = "insert into person(name, age, email) values (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setString(1, person.getName());
            statement.setInt(2, person.getAge());
            statement.setString(3, person.getEmail());

            System.out.println(statement);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(int id, Person person) {
        try {
            String SQL = "update person set name = ?, age = ?, email = ? where id = ?";
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setString(1, person.getName());
            statement.setInt(2, person.getAge());
            statement.setString(3, person.getEmail());
            statement.setInt(4, id);
            System.out.println(statement);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try {
            String SQL = "delete from person where id = ?";
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setInt(1, id);
            System.out.println(statement);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
