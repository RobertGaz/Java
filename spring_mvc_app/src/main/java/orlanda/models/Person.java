package orlanda.models;

import javax.validation.constraints.*;

public class Person {
    public Person() {

    }

    public Person(int id, String name, int age, String email) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
    }

    public void copy(Person other) {
        this.name = other.name;
        this.age = other.age;
        this.email = other.email;
    }

    private int id;

    @NotEmpty(message="Name shouldn't be empty")
    @Size(min=2, max=30, message="Incorrect name size")
    private String name;

    @Min(value=0, message="Invalid age")
    private int age;

    @NotEmpty(message="E-mail shouldn't be empty")
    @Email(message="Invalid e-mail")
    private String email;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
