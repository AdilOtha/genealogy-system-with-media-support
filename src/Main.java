import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        Genealogy genealogy = new Genealogy();
//        PersonIdentity person = genealogy.addPerson("Natasha Romanoff");
//        Map<String, String> attributes = new HashMap<>();
//        attributes.put("date of birth", "1975-02-23");
//        attributes.put("date of death","2019-08-31");
//        attributes.put("gender", "female");
//        attributes.put("occupation","Spy");
//        System.out.println(genealogy.recordAttributes(person, attributes)?"Person attributes save success!":"Error while saving person attributes...");
        PersonIdentity person1 = genealogy.findPerson("Steve Rogers");
        PersonIdentity person2 = genealogy.findPerson("Martha Rogers");
        System.out.println(genealogy.recordChild(person2, person1)?"Child Relation Recorded!":"Error while recording child");
//        PersonIdentity person3 = genealogy.findPerson("Loki");
//        PersonIdentity person4 = genealogy.findPerson("Steve Rogers");
//        List<PersonIdentity> people = new ArrayList<>();
//        people.add(person1);
//        people.add(person2);
//        people.add(person3);
//        people.add(person4);
//        System.out.println(person);
//        System.out.println(genealogy.recordNote(person, "Part of Avengers group of heroes")?"Reference added!":"Error while adding reference...");
//        System.out.println(genealogy.findName(person));
//        FileIdentifier file = genealogy.addMediaFile("C:/Home/Family/1.png");
//        FileIdentifier file2 = genealogy.findMediaFile("C:/Home/Family/1.png");
//        System.out.println(file2);
//        System.out.println(genealogy.findFileLocation(file2));
//        System.out.println(genealogy.peopleInMedia(file2,people)?"People added to media!":"Error while adding...");
//        System.out.println(genealogy.tagMedia(file2, "Superheroes")?"Tag added to image!":"Error while adding tag...");
    }
}
