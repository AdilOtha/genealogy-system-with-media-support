import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class PersonIdentity {
    private int personId;
    private String name;
    private int genderId;
    private Map<String,String> attributes = new HashMap<>();

    public PersonIdentity() {
    }

    public PersonIdentity(String name) {
        this.name = name;
    }

    public PersonIdentity(int person_id, String name) {
        this.personId = person_id;
        this.name = name;
    }

    public int getPersonId() {
        return personId;
    }

    public String getName() {
        return name;
    }

    public void setPersonId(int person_id) {
        this.personId = person_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public int getGenderId() {
        return genderId;
    }

    public void setGenderId(int genderId) {
        this.genderId = genderId;
    }

    @Override
    public String toString() {
        return "PersonIdentity{" +
                "personId=" + personId +
                ", name='" + name + '\'' +
                ", attributes=" + attributes +
                '}';
    }
}