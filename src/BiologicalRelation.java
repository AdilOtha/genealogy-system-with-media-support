/**
 * Defines relation between 2 individuals in the form of cousinship and level of separation.
 */
public class BiologicalRelation {

    // the degree of cousinship
    int degreeOfCousinship;

    // the level of separation
    int levelOfSeparation;

    public int getCousinship() {
        return degreeOfCousinship;
    }

    public int getRemoval() {
        return levelOfSeparation;
    }

    public void setCousinship(int degreeOfCousinship) {
        this.degreeOfCousinship = degreeOfCousinship;
    }

    public void setRemoval(int levelOFSeparation) {
        this.levelOfSeparation = levelOFSeparation;
    }
}
