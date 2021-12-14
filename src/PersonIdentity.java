/**
 * Defines minimum data and methods that can uniquely identify a person in the database.
 */
public class PersonIdentity {
    // The primary key of the media file in the database
    private int personId;

    // the name of the individual, not necessarily unique in the database
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

}