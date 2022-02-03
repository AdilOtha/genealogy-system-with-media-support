
# A Genealogy System for Family Tree information and Picture Archive

## About the System

The following project defines a system that connects family tree information with an archive of images and its metadata. It can be used by genealogists to find ancestors/descendants for an individual, find the relation between a pair of individuals as well as find corresponding pictures for those people. The system can also keep a history of marriages and divorces between individuals. The entire family tree data and media archive remains persistent through the MySQL database.

The system is implemented using Java to follow Object-Oriented Programming practices. The hierarchy of the family tree is stored in the MySQL database and represents a Directed Acyclic Graph. Algorithms such as Breadth-first Search are used to find ancestors/descendants of an individual and Lowest Common Ancestor to find a biological relationship between a pair of people.

## Main Features

 1. Family Tree generation with the ability to add new people to the tree and set their relation with others.
 
 2. Retrieve Ancestors and Descendants of an Individual.
 3. Find relation between 2 individuals as per the following:

![Sample Family Tree](https://i.imgur.com/vDrThyJ.png)

Sample Family Tree
![Finding relation based on "Degree of Cousinship" and "Level of Separation":](https://i.imgur.com/JTXaAFI.png)
Finding relation based on "Degree of Cousinship" and "Level of Separation"
 4. Get respective images of the individual and their ancestors and descendants.

## Installation Steps

 1. Clone repository
 2. Open using IntelliJ IDEA Editor and install dependencies.
 3. Import SQL file provided in "sql" folder on MySQL Database.

## How to test

Run GenealogyTest.java provided in "src" folder.
**Here are some of the methods provided as an interface to the user:**

***Manage the Family Tree***

 1. *PersonIdentity addPerson( String name )* 
Add an individual to the family tree database.

 2. *Boolean recordAttributes( PersonIdentity person, Map<String, String>*
    attributes )
Record information about an individual in the family tree database. Each attribute name is the key to the Map. Examples of attributes are “date of birth”, “gender”, and “occupation”. The Map stores the attributes as Strings, but you are allowed to store them in different formats to make other operations easier to perform.
Returns true if all attributes were stored.

 3. Boolean recordReference( PersonIdentity person, String reference )
Record a source reference material for the individual. A person can have multiple source
references for them. Examples of a reference could be the location of a birth certificate or a web page that lists when someone graduated.
Returns true if the reference was stored in the system.
 4. *Boolean recordNote( PersonIdentity person, String note )*
Record a note for the individual. A person can have multiple notes for them.
Return true if the note was stored in the system.
 5. *Boolean recordChild( PersonIdentity parent, PersonIdentity child )*
Record a parent/child relation for the individuals.
Return true if the relation was stored in the system.
 6. Boolean recordPartnering( PersonIdentity partner1, PersonIdentity
    partner2 )
Record a symmetric partnering relation between the individuals.
Return true if the relation was stored in the system.
 7. Boolean recordDissolution( PersonIdentity partner1, PersonIdentity
    partner2 )
Record a symmetric dissolution of a partnering relation between the individuals.
Return true if the dissolution was stored in the system.

***Manage the media archive***
 1. FileIdentifier addMediaFile( String fileLocation )
Add a media file to the media archive. Return a media file identifier, which will be used to
represent the file in the rest of the archive functions.

 2. Boolean recordMediaAttributes( FileIdentifier fileIdentifier,
    Map<String, String> attributes )
Record information about a media file in the archive. Each attribute name is the key to the
Map. Examples of attributes are “year”, “date”, and “city”. The Map stores the attributes as
Strings, but you are allowed to store them in different formats to make other operations easier to perform.
Return true if all attributes were stored.

 3. Boolean peopleInMedia( FileIdentifier fileIdentifier, List<PersonIdentity> people )
Record that a set of people appear in the given media file.
Return true if the people are now connected to the medial file in the system.
 4. Boolean tagMedia( FileIdentifier, fileIdentifier, String tag )
Record a tac for a media file. A media file can have many tags.
Return true if the tag was stored in the system.

***Reporting***

 1. PersonIdentity findPerson( String name )
Locate an individual in the family tree.

 2. FileIdentifier findMediaFile( String name )
Locate an individual in the family tree.
 3. String findName( PersonIdentity id )
Return the name of an individual.
 4. String findMediaFile( FileIdentifier fileId )
Return the file name of the media file associated with the file identifier.
 5. BiologicalRelation findRelation( PersonIdentity person1, PersonIdentity person2 )
Report how two individuals are related.
 6. Set<PersonIdentity> descendents( PersonIdentity person, Integer
    generations )
Report all descendents in the family tree who are within “generations” generations of the
person. Consider the children of “person” as being 1 generation away.
 7. Set<PersonIdentity> ancestores( PersonIdentity person, Integer
    generations )
Report all ancestors in the family tree who are within “generations” generations of the person.
Consider the children of “person” as being 1 generation away.
 8. List<String> notesAndReferences( PersonIdentity person )
Return all the notes and references on the individual, returned in the same order in which they were added to the family tree.
 9. Set<FileIdentifier> findMediaByTag( String tag , String startDate,
    String endDate)
Return the set of media files linked to the given tag whose dates fall within the date range. Null values for the dates indicate no restrictions on the dates.
 10. Set<FileIdentifier> findMediaByLocation( String location, String
     startDate, String endDate)
Return the set of media files linked to the given location whose dates fall within the date range.
Null values for the dates indicate no restrictions on the dates.
For simplicity, we require that the location name be a perfect match. For example, a query
location of “Halifax” will not match a stored location of “Halifax, Nova Scotia”. You can go
beyond this limitation if you want.
 11. List<FileIdentifier> findIndividualsMedia( Set<PersonIdentity> people, String startDate, String endDate)
Return the set of media files that include any of individuals given in the list of people whose dates fall within the date range. Null values for the dates indicate no restrictions on the dates. Returns the files in ascending chronological order (breaking ties by the ascending order of the file names). List files with no dates at the end of the list.
 12. List<FileIdentifier> findBiologicalFamilyMedia(PersonIdentity
     person)
Return the set of media files that include the specified person’s immediate children.
Return the files in ascending chronological order (breaking ties by the ascending order of the file names). List files with no dates at the end of the list.
