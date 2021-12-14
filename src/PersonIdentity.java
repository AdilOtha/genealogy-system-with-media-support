
public class PersonIdentity {
    private int personId;
    private String name;

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
}