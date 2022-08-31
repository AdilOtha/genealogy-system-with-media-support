package pkg;

public class BiologicalRelation {

    int degreeOfCousinship;
    int levelOFSeparation;

    public int getCousinship() {
        return degreeOfCousinship;
    }

    public int getRemoval() {
        return levelOFSeparation;
    }

    public void setCousinship(int degreeOfCousinship) {
        this.degreeOfCousinship = degreeOfCousinship;
    }

    public void setRemoval(int levelOFSeparation) {
        this.levelOFSeparation = levelOFSeparation;
    }

    @Override
    public String toString() {
        return "pkg.BiologicalRelation{" +
                "degreeOfCousinship=" + degreeOfCousinship +
                ", levelOFSeparation=" + levelOFSeparation +
                '}';
    }
}
