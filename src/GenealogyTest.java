import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Defines JUnit tests for methods of Genealogy class.
 */
class GenealogyTest {

    @Test
    void addPerson() {
        Genealogy genealogy = new Genealogy();

        // Name as null
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.addPerson(null);
        });

        // Name as empty string
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.addPerson("  ");
        });

        // for testing purpose
        double random = Math.random(); // generating random token for unique name on every test run

        // Valid name
        String name = "Edward "+random;
        PersonIdentity person = genealogy.addPerson(name);
        assertNotNull(person);
        assertTrue(person.getPersonId()>0);

        // Duplicate name
        PersonIdentity person2 = genealogy.addPerson(name);
        assertNotNull(person2);
        assertTrue(person2.getPersonId()>0 && person.getPersonId()!= person2.getPersonId());

    }

    @Test
    void recordReference() {
        Genealogy genealogy = new Genealogy();

        // person object as null
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.recordReference(null,"abcdef");
        });

        // invalid person object
        PersonIdentity person = new PersonIdentity(0,"Adam");
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.recordReference(person,"abcdef");
        });

        PersonIdentity person2 = genealogy.findPerson("Chris");

        // reference as null
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.recordReference(person2,null);
        });

        // reference as empty string
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.recordReference(person2,"  ");
        });

        // passing valid person object and reference
        assertTrue(genealogy.recordReference(person2,"Mark"));

        // adding duplicate reference
        assertTrue(genealogy.recordReference(person2,"Mark"));
    }

    @Test
    void recordNote() {
        Genealogy genealogy = new Genealogy();

        // person object as null
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.recordNote(null,"ghijkl");
        });

        // invalid person object
        PersonIdentity person = new PersonIdentity(0,"Adam");
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.recordNote(person,"abcdef");
        });

        PersonIdentity person2 = genealogy.findPerson("Chris");

        // note as null
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.recordNote(person2,null);
        });

        // note as empty string
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.recordNote(person2,"  ");
        });

        // passing valid person object and note
        assertTrue(genealogy.recordNote(person2,"Fond of skydiving"));

        // adding duplicate note
        assertTrue(genealogy.recordNote(person2,"Fond of skydiving"));
    }

    @Test
    void findPerson() {
        Genealogy genealogy = new Genealogy();

        // name as null
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.findPerson(null);
        });

        // name as empty string
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.findPerson("  ");
        });

        // for testing purpose
        double random = Math.random(); // generating random token for unique name on every test run
        String name = "Ben "+random;

        genealogy.addPerson(name);
        genealogy.addPerson(name);

        // giving name that is assigned to more than 1 person
        Assertions.assertThrows(RuntimeException.class,()->{
            genealogy.findPerson("Ben");
        });

        // valid name for which a person exists
        PersonIdentity person = genealogy.findPerson("Tony Stark");
        assertNotNull(person);
        assertTrue(person.getPersonId()>0);
    }

    @Test
    void findName() {
        Genealogy genealogy = new Genealogy();

        // person object as null
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.findName(null);
        });

        // invalid person object
        PersonIdentity person = new PersonIdentity(0,"Adam");
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.findName(person);
        });

        PersonIdentity person2 = genealogy.findPerson("Tony Stark");
        assertNotNull(person2);
        assertNotNull(genealogy.findName(person2));
        assertFalse(genealogy.findName(person2).trim().isEmpty());
    }

    @Test
    void addMediaFile() {
        Genealogy genealogy = new Genealogy();

        // fileLocation as null
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.addMediaFile(null);
        });

        // fileLocation as empty string
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.addMediaFile("  ");
        });

        double random = Math.random(); // generating random token for unique filename on every test run

        // Valid fileLocation
        FileIdentifier fileIdentifier = genealogy.addMediaFile("D:/media/toronto-"+random+".png");
        assertNotNull(fileIdentifier);
        assertTrue(fileIdentifier.getMediaId()>0);

        // Duplicate fileLocation throws Illegal Argument Exception
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.addMediaFile("D:/media/toronto-"+random+".png");
        });
    }

    @Test
    void findMediaFile() {
        Genealogy genealogy = new Genealogy();

        // fileLocation as null
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.findMediaFile(null);
        });

        // fileLocation as empty string
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.findMediaFile("  ");
        });

        // valid fileLocation for which a file exists
        FileIdentifier fileIdentifier = genealogy.findMediaFile("C:/Home/Family/1.png");
        assertNotNull(fileIdentifier);
        assertTrue(fileIdentifier.getMediaId()>0);
    }

    @Test
    void findFileLocation() {
        Genealogy genealogy = new Genealogy();

        // file object as null
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.findFileLocation(null);
        });

        // invalid file object
        FileIdentifier fileIdentifier = new FileIdentifier(0,"abc");
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.findFileLocation(fileIdentifier);
        });
        FileIdentifier fileIdentifier2 = genealogy.findMediaFile("C:/Home/Family/1.png");
        assertNotNull(fileIdentifier2);
        assertNotNull(genealogy.findFileLocation(fileIdentifier2));
        assertFalse(genealogy.findFileLocation(fileIdentifier2).trim().isEmpty());
    }

    @Test
    void peopleInMedia() {
        Genealogy genealogy = new Genealogy();

        // file object as null
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.peopleInMedia(null, new ArrayList<>());
        });

        // invalid file object
        FileIdentifier fileIdentifier = new FileIdentifier(0,"abc");
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.findFileLocation(fileIdentifier);
        });

        // list of people as null
        FileIdentifier fileIdentifier2 = genealogy.findMediaFile("E:/photos/family.png");
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.peopleInMedia(fileIdentifier2, null);
        });

        // empty people list
        assertTrue(genealogy.peopleInMedia(fileIdentifier2, new ArrayList<>()));

        PersonIdentity person1 = genealogy.findPerson("A");
        PersonIdentity person2 = genealogy.findPerson("C");
        PersonIdentity person3 = genealogy.findPerson("X");
        PersonIdentity person4 = genealogy.findPerson("Y");

        List<PersonIdentity> personIdentityList = new ArrayList<>();
        personIdentityList.add(person1);
        personIdentityList.add(person2);
        personIdentityList.add(person3);
        personIdentityList.add(person4);

        // record people in media file
        assertTrue(genealogy.peopleInMedia(fileIdentifier2,personIdentityList));

        // re-record people in media file
        assertTrue(genealogy.peopleInMedia(fileIdentifier2,personIdentityList));
    }

    @Test
    void tagMedia() {
        Genealogy genealogy = new Genealogy();

        // file object as null
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.tagMedia(null, "abcd");
        });

        // invalid file object
        FileIdentifier fileIdentifier = new FileIdentifier(0,"abc");
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.tagMedia(fileIdentifier,"abcd");
        });

        FileIdentifier fileIdentifier2 = genealogy.findMediaFile("E:/photos/family.png");
        // tag as null
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.tagMedia(fileIdentifier2,null);
        });

        // tag as empty string
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.tagMedia(fileIdentifier2,"   ");
        });

        // assign tag that already exists
        assertTrue(genealogy.tagMedia(fileIdentifier2,"Travel"));

        // for testing purpose
        double random = Math.random(); // generating random token for unique name on every test run

        // assign new tag
        String tagName = "Computer Science "+random;
        assertTrue(genealogy.tagMedia(fileIdentifier2,tagName));

        // re-assign new tag
        assertTrue(genealogy.tagMedia(fileIdentifier2,tagName));

        FileIdentifier fileIdentifier3 = genealogy.findMediaFile("C:/Home/Family/1.png");

        // re-assign tag to another file
        assertTrue(genealogy.tagMedia(fileIdentifier3,tagName));

    }

    @Test
    void recordAttributes() {
        Genealogy genealogy = new Genealogy();

        // person object as null
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.recordAttributes(null, new HashMap<>());
        });

        // invalid person object
        PersonIdentity person = new PersonIdentity(0,"Adam");
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.recordAttributes(person, new HashMap<>());
        });

        PersonIdentity person2 = genealogy.findPerson("Evan Romanoff");

        // empty attributes map
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.recordAttributes(person2, new HashMap<>());
        });

        // empty string as key attributes map
        Map<String,String> attributes = new HashMap<>();
        attributes.put("  ","abcd");
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.recordAttributes(person2, attributes);
        });

        // empty string as value attributes map
        Map<String,String> attributes2 = new HashMap<>();
        attributes.put("abcd","   ");
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.recordAttributes(person2, attributes2);
        });

        // adding valid map
        Map<String,String> attributes3 = new HashMap<>();
        attributes3.put("date of birth", "1910-07-18");
        attributes3.put("date of death","1990-02-25");
        attributes3.put("gender", "male");
        attributes3.put("occupation","Doctor");
        assertTrue(genealogy.recordAttributes(person2,attributes3));

        // re-assigning same attributes except with one value changed
        attributes3.put("occupation","Doctor M.D.");
        assertTrue(genealogy.recordAttributes(person2,attributes3));

    }

    @Test
    void recordMediaAttributes() {
        Genealogy genealogy = new Genealogy();

        // fileIdentifier object as null
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.recordMediaAttributes(null, new HashMap<>());
        });

        // invalid fileIdentifier object
        FileIdentifier fileIdentifier = new FileIdentifier(0,"abcf");
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.recordMediaAttributes(fileIdentifier, new HashMap<>());
        });

        double random = Math.random();
        FileIdentifier fileIdentifier2 = genealogy.addMediaFile("D:/media/vancouver-"+random+".png");

        // empty attributes map
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.recordMediaAttributes(fileIdentifier2, new HashMap<>());
        });

        // empty string as key attributes map
        Map<String,String> attributes = new HashMap<>();
        attributes.put("  ","abcd");
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.recordMediaAttributes(fileIdentifier2, attributes);
        });

        // empty string as value attributes map
        Map<String,String> attributes2 = new HashMap<>();
        attributes.put("abcd","   ");
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.recordMediaAttributes(fileIdentifier2, attributes2);
        });

        // adding valid map
        Map<String,String> attributes3 = new HashMap<>();
        attributes3.put("date", "2017-04");
        attributes3.put("location", "British Columbia Airport");
        attributes3.put("city","Vancouver");
        assertTrue(genealogy.recordMediaAttributes(fileIdentifier2,attributes3));

        // re-assigning same attributes except with one value changed
        attributes3.put("date","2021-08-01");
        assertTrue(genealogy.recordMediaAttributes(fileIdentifier2,attributes3));
    }

    @Test
    void recordChild() {
        Genealogy genealogy = new Genealogy();

        // for testing purpose
        double random = Math.random(); // generating random token for unique name on every test run

        PersonIdentity person = new PersonIdentity(0,"Adam");
        PersonIdentity person2 = genealogy.addPerson("Walter White "+random);
        PersonIdentity person3 = genealogy.addPerson("Tyler White "+random);
        PersonIdentity person4 = genealogy.addPerson("Skyler White "+random);
        PersonIdentity person5 = genealogy.findPerson("Odin");

        // parent object as null
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.recordChild(null,person2);
        });

        // invalid parent object
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.recordChild(person,person2);
        });

        // child object as null
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.recordChild(person2, null);
        });

        // invalid child object
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.recordChild(person2,person);
        });

        // record parent-child relation
        assertTrue(genealogy.recordChild(person2,person3));

        // re-record parent-child relation
        assertTrue(genealogy.recordChild(person2,person3));

        // add new parent for same child
        assertTrue(genealogy.recordChild(person4,person3));

        // try to add parent for a child that already has 2 parents
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.recordChild(person5,person3);
        });

    }

    @Test
    void descendants() {
        Genealogy genealogy = new Genealogy();

        // person object as null
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.descendants(null,2);
        });

        // invalid person object
        PersonIdentity person = new PersonIdentity(0,"Adam");
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.descendants(person,2);
        });

        PersonIdentity person2 = genealogy.findPerson("A");
        PersonIdentity person3 = genealogy.findPerson("Steve Rogers");

        // invalid generations
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.descendants(person2,-2);
        });

        // invalid generations
        Set<PersonIdentity> personIdentitySet = genealogy.descendants(person2,0);
        assertNotNull(personIdentitySet);
        assertTrue(personIdentitySet.size()==0);

        // find descendants for a valid person
        personIdentitySet = genealogy.descendants(person2,2);
        assertNotNull(personIdentitySet);
        assertTrue(personIdentitySet.size()>0);


        // find descendants for a valid person for generations higher than which exist
        Set<PersonIdentity> personIdentitySet2 = genealogy.descendants(person2,10);
        assertNotNull(personIdentitySet2);
        assertTrue(personIdentitySet2.size()>0);

        // find descendants for a valid person that has no descendents
        personIdentitySet = genealogy.descendants(person3,5);
        assertNotNull(personIdentitySet);
        assertTrue(personIdentitySet.size()==0);

    }

    @Test
    void ancestors() {
        Genealogy genealogy = new Genealogy();

        // person object as null
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.ancestors(null,2);
        });

        // invalid person object
        PersonIdentity person = new PersonIdentity(0,"Adam");
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.ancestors(person,2);
        });

        PersonIdentity person2 = genealogy.findPerson("I");
        PersonIdentity person3 = genealogy.findPerson("Steve Rogers");

        // invalid generations
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.ancestors(person2,-2);
        });

        // invalid generations
        Set<PersonIdentity> personIdentitySet = genealogy.ancestors(person2,0);
        assertNotNull(personIdentitySet);
        assertTrue(personIdentitySet.size()==0);

        // find ancestors for a valid person
        personIdentitySet = genealogy.ancestors(person2,2);
        assertNotNull(personIdentitySet);
        assertTrue(personIdentitySet.size()>0);


        // find ancestors for a valid person for generations higher than which exist
        Set<PersonIdentity> personIdentitySet2 = genealogy.ancestors(person2,10);
        assertNotNull(personIdentitySet2);
        assertTrue(personIdentitySet2.size()>0);

        // find ancestors for a valid person that has no ancestors
        personIdentitySet = genealogy.ancestors(person3,5);
        assertNotNull(personIdentitySet);
        assertTrue(personIdentitySet.size()==0);
    }

    @Test
    void recordPartnering() {
        Genealogy genealogy = new Genealogy();

        // person objects as null
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.recordPartnering(null,null);
        });

        // invalid person objects
        PersonIdentity person = new PersonIdentity(0,"Adam");
        PersonIdentity person2 = new PersonIdentity(0,"Drake");
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.recordPartnering(person,person2);
        });

        // for testing purpose
        double random = Math.random(); // generating random token for unique name on every test run

        PersonIdentity person5 = genealogy.addPerson("Jake Black "+random);
        PersonIdentity person6 = genealogy.addPerson("Rita Grey "+random);

        // record partnership between 2 persons that don't have any previous partnerships or dissolutions
        assertTrue(genealogy.recordPartnering(person5,person6));

        // record partnership between 2 persons that have existing partnership
        assertTrue(genealogy.recordPartnering(person5,person6));

        // record divorce event
        genealogy.recordDissolution(person5,person6);

        // record partnership between 2 persons who have divorce as their latest event
        assertTrue(genealogy.recordPartnering(person5,person6));
    }

    @Test
    void recordDissolution() {
        Genealogy genealogy = new Genealogy();

        // person objects as null
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.recordDissolution(null,null);
        });

        // invalid person objects
        PersonIdentity person = new PersonIdentity(0,"Adam");
        PersonIdentity person2 = new PersonIdentity(0,"Drake");
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.recordDissolution(person,person2);
        });

        // for testing purpose
        double random = Math.random(); // generating random token for unique name on every test run

        PersonIdentity person5 = genealogy.addPerson("Aaron Red "+random);
        PersonIdentity person6 = genealogy.addPerson("Alice White "+random);

        // record dissolution between 2 persons that don't have any previous partnerships or dissolutions
        assertTrue(genealogy.recordDissolution(person5,person6));

        // record dissolution between 2 persons that have existing dissolution
        assertTrue(genealogy.recordDissolution(person5,person6));

        // record partnership event
        genealogy.recordPartnering(person5,person6);

        // record dissolution between 2 persons who have partnership as their latest event
        assertTrue(genealogy.recordDissolution(person5,person6));
    }

    @Test
    void findRelation() {
        Genealogy genealogy = new Genealogy();

        // person objects as null
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.findRelation(null,null);
        });

        // invalid person objects
        PersonIdentity person = new PersonIdentity(0,"Adam");
        PersonIdentity person2 = new PersonIdentity(0,"Drake");
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.findRelation(person,person2);
        });

        // for testing purpose
        double random = Math.random(); // generating random token for unique name on every test run

        PersonIdentity person3 = genealogy.addPerson("Johnny Doe "+random);
        PersonIdentity person4 = genealogy.addPerson("Jenny Doe "+random);

        // find relation between 2 unrelated people
        BiologicalRelation biologicalRelation = genealogy.findRelation(person3,person4);
        assertNull(biologicalRelation);

        PersonIdentity person5 = genealogy.findPerson("Node9");
        PersonIdentity person6 = genealogy.findPerson("Node10");
        PersonIdentity person7 = genealogy.findPerson("Node2");

        // find relation between 2 people part of a family tree
        // degree of cousinship and level of separation is 1 and 0, respectively for this pair of persons
        // THIS FAMILY TREE IS DEPICTED IN EXTERNAL DOCUMENTATION, PAGE 5
        biologicalRelation = genealogy.findRelation(person5,person6);
        assertNotNull(biologicalRelation);
        assertTrue((biologicalRelation.getCousinship()==1) && (biologicalRelation.getRemoval()==0));

        // find relation between 2 people, one of which is the ancestor of the other
        biologicalRelation = genealogy.findRelation(person5,person7);
        assertNotNull(biologicalRelation);
        assertTrue((biologicalRelation.getCousinship()==-1) && (biologicalRelation.getRemoval()==2));


    }

    @Test
    void notesAndReferences() {
        Genealogy genealogy = new Genealogy();

        // person object as null
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.notesAndReferences(null);
        });

        // invalid person object
        PersonIdentity person = new PersonIdentity(0,"Adam");
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.notesAndReferences(person);
        });

        PersonIdentity person2 = genealogy.findPerson("Natasha Romanoff");
        PersonIdentity person3 = genealogy.findPerson("Tony Stark");

        // call method for a person who has no notes or references
        List<String> notesReferences = genealogy.notesAndReferences(person3);
        assertNotNull(notesReferences);
        assertEquals(0, notesReferences.size());

        // call method for a person who has many notes and references
        notesReferences = genealogy.notesAndReferences(person2);
        assertNotNull(notesReferences);
        assertTrue(notesReferences.size()>0);
    }

    @Test
    void findMediaByTag() {
        Genealogy genealogy = new Genealogy();

        // tag as null
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.findMediaByTag(null, "1975","2021");
        });

        // tag as empty string
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.findMediaByTag("   ","1975", "2021");
        });

        // start date as empty string
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.findMediaByTag("Education","   ", "2021");
        });

        // end date as empty string
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.findMediaByTag("Education","1975", "   ");
        });

        // invalid date format (for end date)
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.findMediaByTag("Education","1975", "30");
        });

        // start date after end date
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.findMediaByTag("Education","2021-04", "1900-12-23");
        });

        // both start and end date as null
        Set<FileIdentifier> fileIdentifierSet;
        fileIdentifierSet = genealogy.findMediaByTag("Education",null, null);
        assertTrue(fileIdentifierSet.size()>0);

        // only end date as null
        fileIdentifierSet = genealogy.findMediaByTag("Education","1976-01", null);
        assertTrue(fileIdentifierSet.size()>0);

        // only start date as null
        fileIdentifierSet = genealogy.findMediaByTag("Education",null, "1999-01");
        assertTrue(fileIdentifierSet.size()>0);

        // both valid dates are provided
        fileIdentifierSet = genealogy.findMediaByTag("Education","1975", "2001-01-01");
        assertTrue(fileIdentifierSet.size()>0);

        // searching by tag that does not exist
        fileIdentifierSet = genealogy.findMediaByTag("xyz","1975", "2021");
        assertEquals(0,fileIdentifierSet.size());
    }

    @Test
    void findMediaByLocation() {
        Genealogy genealogy = new Genealogy();

        // location as null
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.findMediaByLocation(null, "1975","2021");
        });

        // location as empty string
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.findMediaByLocation("   ","1975", "2021");
        });

        // start date as empty string
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.findMediaByLocation("South Park","   ", "2021");
        });

        // end date as empty string
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.findMediaByLocation("South Park","1975", "   ");
        });

        // invalid date format (for end date)
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.findMediaByLocation("South Park","1975", "30");
        });

        // start date after end date
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.findMediaByLocation("South Park","2021-04", "1900-12-23");
        });

        // both start and end date as null
        Set<FileIdentifier> fileIdentifierSet;
        fileIdentifierSet = genealogy.findMediaByLocation("South Park",null, null);
        assertTrue(fileIdentifierSet.size()>0);

        // only end date as null
        fileIdentifierSet = genealogy.findMediaByLocation("South Park","1976-01", null);
        assertTrue(fileIdentifierSet.size()>0);

        // only start date as null
        fileIdentifierSet = genealogy.findMediaByLocation("South Park",null, "2000-12");
        assertTrue(fileIdentifierSet.size()>0);

        // both valid dates are provided
        fileIdentifierSet = genealogy.findMediaByLocation("South Park","1975", "2001-01-01");
        assertTrue(fileIdentifierSet.size()>0);

        // searching by tag that does not exist
        fileIdentifierSet = genealogy.findMediaByLocation("xyz","1975", "2021");
        assertEquals(0,fileIdentifierSet.size());
    }

    @Test
    void findBiologicalFamilyMedia() {
        Genealogy genealogy=new Genealogy();

        // person object as null
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.notesAndReferences(null);
        });

        // invalid person object
        PersonIdentity person = new PersonIdentity(0,"Adam");
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.notesAndReferences(person);
        });

        PersonIdentity person1 = genealogy.findPerson("A");
        PersonIdentity person2 = genealogy.findPerson("G");
        PersonIdentity person3 = genealogy.findPerson("Steve Rogers");

        // find media files for person with multiple immediate children
        List<FileIdentifier> fileIdentifierList=null;
        fileIdentifierList=genealogy.findBiologicalFamilyMedia(person1);
        assertNotNull(fileIdentifierList);
        assertTrue(fileIdentifierList.size()>0);

        // find media file for person with a child but no images
        fileIdentifierList=genealogy.findBiologicalFamilyMedia(person2);
        assertNotNull(fileIdentifierList);
        assertEquals(0,fileIdentifierList.size());

        // find media file for person with no children
        fileIdentifierList=genealogy.findBiologicalFamilyMedia(person3);
        assertNotNull(fileIdentifierList);
        assertEquals(0,fileIdentifierList.size());

    }

    @Test
    void findIndividualsMedia() {
        Genealogy genealogy = new Genealogy();

        PersonIdentity person1 = genealogy.findPerson("A");
        PersonIdentity person2 = genealogy.findPerson("C");
        PersonIdentity person3 = genealogy.findPerson("X");
        PersonIdentity person4 = genealogy.findPerson("Y");
        PersonIdentity person5 = genealogy.findPerson("Natasha Romanoff");

        Set<PersonIdentity> personIdentitySet = new HashSet<>();
        personIdentitySet.add(person1);
        personIdentitySet.add(person2);
        personIdentitySet.add(person3);
        personIdentitySet.add(person4);
        personIdentitySet.add(person5);

        // people set as null
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.findIndividualsMedia(null, "1975","2021");
        });

        // people set as empty
        Set<PersonIdentity> personIdentitySet2=new HashSet<>();
        List<FileIdentifier> fileIdentifierList = genealogy.findIndividualsMedia(personIdentitySet2,"1975", "2021");
        assertEquals(0,fileIdentifierList.size());

        // start date as empty string
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.findIndividualsMedia(personIdentitySet,"   ", "2021");
        });

        // end date as empty string
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.findIndividualsMedia(personIdentitySet,"1975", "   ");
        });

        // invalid date format (for end date)
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.findIndividualsMedia(personIdentitySet,"1975", "30");
        });

        // start date after end date
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            genealogy.findIndividualsMedia(personIdentitySet,"2021-04", "1900-12-23");
        });

        // both start and end date as null
        fileIdentifierList = genealogy.findIndividualsMedia(personIdentitySet,null, null);
        assertTrue(fileIdentifierList.size()>0);

        // only end date as null
        fileIdentifierList = genealogy.findIndividualsMedia(personIdentitySet,"1976-01", null);
        assertTrue(fileIdentifierList.size()>0);

        // only start date as null
        fileIdentifierList = genealogy.findIndividualsMedia(personIdentitySet,null, "2021-12");
        assertTrue(fileIdentifierList.size()>0);

        // both valid dates are provided
        fileIdentifierList = genealogy.findIndividualsMedia(personIdentitySet,"1975", "2022-01-01");
        assertTrue(fileIdentifierList.size()>0);

        // searching images for people with no images
        Set<PersonIdentity> personIdentitySet3=new HashSet<>();
        personIdentitySet3.add(genealogy.findPerson("John Doe"));
        personIdentitySet3.add(genealogy.findPerson("Jane Doe"));
        fileIdentifierList = genealogy.findIndividualsMedia(personIdentitySet3,"1975", "2021");
        assertEquals(0,fileIdentifierList.size());
    }
}