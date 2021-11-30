public class BiologicalRelation {

    int degreeOfCousinship;
    int levelOFSeparation;

    public int getDegreeOfCousinship() {
        return degreeOfCousinship;
    }

    public int getLevelOFSeparation() {
        return levelOFSeparation;
    }

    public Boolean recordChild(PersonIdentity parent, PersonIdentity child) {
        return false;
    }

    public Boolean recordPartnering(PersonIdentity partner1, PersonIdentity partner2) {
        return false;
    }

    public Boolean recordDissolution( PersonIdentity partner1, PersonIdentity partner2 ){
        return false;
    }
}
