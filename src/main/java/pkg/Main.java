package pkg;

import java.util.*;

public class Main {
    final static String familyCommand = "family";
    final static String mediaCommand = "media";
    final static String reportCommand = "report";
    final static String quitCommand = "quit";
    final static String returnCommand = "return";
    public static void main(String[] args) {

        String userCommand = "";
        Scanner scanner = new Scanner(System.in);

        Genealogy genealogy = new Genealogy();

        System.out.println("Commands available:");
        System.out.println("family - manage the family tree");
        System.out.println("media - manage the media archive");
        System.out.println("report - fetch data from the family tree database");
        System.out.println("quit - exit the program");

        do {
            userCommand = scanner.nextLine();

            if (userCommand.equalsIgnoreCase(familyCommand)){
                runFamilyMenu(scanner, genealogy);
            } else if (userCommand.equalsIgnoreCase(mediaCommand)){
                runMediaMenu(scanner, genealogy);
            } else if (userCommand.equalsIgnoreCase(reportCommand)){
                runReportMenu(scanner, genealogy);
            } else if (userCommand.equalsIgnoreCase(quitCommand)){
                System.out.println("Goodbye!");
            } else {
                System.out.println("Invalid command. Please try again.");
            }

        } while (!userCommand.equalsIgnoreCase(quitCommand));

    }

    private static void runFamilyMenu(Scanner scanner, Genealogy genealogy) {
        String addCommand = "add";
        String attributeCommand = "attributes";
        String notesCommand = "note";
        String referenceCommand = "reference";
        String childCommand = "child";
        String marriageCommand = "marriage";
        String divorceCommand = "divorce";

        System.out.println("Commands available:");
        System.out.println("add - add a new person to the family tree");
        System.out.println("attributes - add attributes to a person");
        System.out.println("reference - add a reference to a person");
        System.out.println("note - add a note to a person");
        System.out.println("child - record a child for a person");
        System.out.println("marriage - record a marriage for between two people");
        System.out.println("divorce - record a divorce for between two people");
        System.out.println("return - return to the main menu");
        String selectedFamilyCommand = "";

        do {
            selectedFamilyCommand = scanner.nextLine();
            String userArgument = "";

            if (selectedFamilyCommand.equalsIgnoreCase(returnCommand)){
                break;
            }

            if (selectedFamilyCommand.equalsIgnoreCase(addCommand)){
                System.out.println("Enter the name of the person to add:");
                userArgument = scanner.nextLine();
                genealogy.addPerson(userArgument);
            } else if (selectedFamilyCommand.equalsIgnoreCase(attributeCommand)) {
                System.out.println(("Enter the name of the person to add attributes to:"));
                userArgument = scanner.nextLine();
                PersonIdentity person = genealogy.findPerson(userArgument);

                if (person != null) {
                    // create a map that stores person attributes and their values
                    Map<String, String> attributes = new HashMap<>();
                    System.out.println("Enter attributes and values, one per line, enter an empty line to finish:");
                    System.out.println("Input Format: <key>=<value>");
                    do {
                        userArgument = scanner.nextLine();
                        if (!userArgument.isEmpty()) {
                            String[] attribute = userArgument.split("=");
                            attributes.put(attribute[0], attribute[1]);
                        }
                    } while (!userArgument.isEmpty());

                    if (genealogy.recordAttributes(person, attributes)) {
                        System.out.println("Attributes successfully added to " + person.getName());
                    } else {
                        System.out.println("Failed to add attributes to " + person.getName());
                    }
                } else {
                    System.out.println("Person not found");
                }
            } else if (selectedFamilyCommand.equalsIgnoreCase(notesCommand)){
                System.out.println("Enter the name of the person to add a note to:");
                userArgument = scanner.nextLine();
                PersonIdentity person = genealogy.findPerson(userArgument);

                if (person != null) {
                    System.out.println("Enter the note to add:");
                    userArgument = scanner.nextLine();
                    if (genealogy.recordNote(person, userArgument)) {
                        System.out.println("Note successfully added to " + person.getName());
                    } else {
                        System.out.println("Failed to add note to " + person.getName());
                    }
                } else {
                    System.out.println("Person not found");
                }
            } else if (selectedFamilyCommand.equalsIgnoreCase(referenceCommand)) {
                System.out.println("Enter the name of the person to add a reference to:");
                userArgument = scanner.nextLine();
                PersonIdentity person = genealogy.findPerson(userArgument);

                if (person != null) {
                    System.out.println("Enter the note to add:");
                    userArgument = scanner.nextLine();
                    if (genealogy.recordReference(person, userArgument)) {
                        System.out.println("Note successfully added to " + person.getName());
                    } else {
                        System.out.println("Failed to add note to " + person.getName());
                    }
                } else {
                    System.out.println("Person not found");
                }
            }
            else if (selectedFamilyCommand.equalsIgnoreCase(childCommand)) {
                System.out.println("Enter the name of the person to add a child to:");
                userArgument = scanner.nextLine();
                PersonIdentity person = genealogy.findPerson(userArgument);

                if (person != null) {
                    System.out.println("Enter the name of the child to add:");
                    userArgument = scanner.nextLine();

                    PersonIdentity child = genealogy.findPerson(userArgument);

                    if (child != null) {
                        if (genealogy.recordChild(person, child)) {
                            System.out.println("Child successfully added to " + person.getName());
                        } else {
                            System.out.println("Failed to add child to " + person.getName());
                        }
                    } else {
                        System.out.println("Child not found");
                    }
                } else {
                    System.out.println("Person not found");
                }
            } else if (selectedFamilyCommand.equalsIgnoreCase(marriageCommand)) {
                System.out.println("Enter the name of the person to add a marriage to:");
                userArgument = scanner.nextLine();
                PersonIdentity person = genealogy.findPerson(userArgument);

                if (person != null) {
                    System.out.println("Enter the name of the spouse to add:");
                    userArgument = scanner.nextLine();
                    PersonIdentity spouse = genealogy.findPerson(userArgument);
                    if (spouse != null) {
                        if (genealogy.recordPartnering(person, spouse)) {
                            System.out.println("Marriage successfully added to " + person.getName());
                        } else {
                            System.out.println("Failed to add marriage to " + person.getName());
                        }
                    } else {
                        System.out.println("Spouse not found");
                    }
                } else {
                    System.out.println("Person not found");
                }
            } else if (selectedFamilyCommand.equalsIgnoreCase(divorceCommand)) {
                System.out.println("Enter the name of the person to add a divorce to:");
                userArgument = scanner.nextLine();
                PersonIdentity person = genealogy.findPerson(userArgument);

                if (person != null) {
                    System.out.println("Enter the name of the spouse to add:");
                    userArgument = scanner.nextLine();
                    PersonIdentity spouse = genealogy.findPerson(userArgument);
                    if (spouse != null) {
                        if (genealogy.recordDissolution(person, spouse)) {
                            System.out.println("Divorce successfully added to " + person.getName());
                        } else {
                            System.out.println("Failed to add divorce to " + person.getName());
                        }
                    } else {
                        System.out.println("Spouse not found");
                    }
                } else {
                    System.out.println("Person not found");
                }
            } else {
                System.out.println("Invalid command");
            }

        } while (!selectedFamilyCommand.equalsIgnoreCase(returnCommand));
    }

    private static void runMediaMenu(Scanner scanner, Genealogy genealogy) {
        String selectedMediaCommand = "";
        String userArgument = "";

        String addCommand = "add";
        String attributeCommand = "attributes";
        String peopleCommand = "people";
        String tagCommand = "tag";
        String returnCommand = "return";

        System.out.println("Commands available:");
        System.out.println("add - add a media file");
        System.out.println("attributes - add attributes to a media file");
        System.out.println("people - assign people to a media file");
        System.out.println("tag - add a tag to a media file");

        do {
            System.out.println("Enter a command:");
            selectedMediaCommand = scanner.nextLine();

            if(selectedMediaCommand.equalsIgnoreCase(returnCommand)){
                break;
            }

            if (selectedMediaCommand.equalsIgnoreCase(addCommand)) {
                System.out.println("Enter the name of the media file to add:");
                userArgument = scanner.nextLine();
                FileIdentifier file = genealogy.addMediaFile(userArgument);
                if (file != null) {
                    System.out.println("Media file successfully added");
                } else {
                    System.out.println("Failed to add media file");
                }
            } else if (selectedMediaCommand.equalsIgnoreCase(attributeCommand)) {
                System.out.println("Enter the name of the media file to add attributes to:");
                userArgument = scanner.nextLine();
                FileIdentifier file = genealogy.findMediaFile(userArgument);
                if (file != null) {
                    // create a map that stores media attributes and their values
                    Map<String, String> attributes = new HashMap<>();
                    System.out.println("Enter attributes and values, one per line, enter an empty line to finish:");
                    System.out.println("Input Format: <key>=<value>");
                    do {
                        userArgument = scanner.nextLine();
                        if (!userArgument.isEmpty()) {
                            String[] attribute = userArgument.split("=");
                            attributes.put(attribute[0], attribute[1]);
                        }
                    } while (!userArgument.isEmpty());

                    if (genealogy.recordMediaAttributes(file, attributes)) {
                        System.out.println("Attributes successfully added to " + file.getFileLocation());
                    } else {
                        System.out.println("Failed to add attributes to " + file.getFileLocation());
                    }
                } else {
                    System.out.println("Media file not found");
                }
            } else if (selectedMediaCommand.equalsIgnoreCase(peopleCommand)) {
                System.out.println("Enter name of people to add to media file, one per line, enter an empty line to finish:");
                List<PersonIdentity> people = new ArrayList<>();
                FileIdentifier file = genealogy.findMediaFile(userArgument);
                if (file != null) {
                    do {
                        userArgument = scanner.nextLine();
                        if (!userArgument.isEmpty()) {
                            PersonIdentity person = genealogy.findPerson(userArgument);
                            if (person != null) {
                                people.add(person);
                            } else {
                                System.out.println("Person not found: " + userArgument);
                                break;
                            }
                            if (genealogy.peopleInMedia(file, people)) {
                                System.out.println("People successfully added to " + file.getFileLocation());
                            } else {
                                System.out.println("Failed to add people to " + file.getFileLocation());
                            }
                        }
                    } while (!userArgument.isEmpty());
                } else {
                    System.out.println("Media file not found");
                }
            } else if (selectedMediaCommand.equalsIgnoreCase(tagCommand)) {
                System.out.println("Enter the name of the media to add a tag to:");
                userArgument = scanner.nextLine();
                FileIdentifier file = genealogy.findMediaFile(userArgument);

                if (file != null) {
                    System.out.println("Enter the note to add:");
                    userArgument = scanner.nextLine();
                    if (genealogy.tagMedia(file, userArgument)) {
                        System.out.println("Tag successfully added to " + file.getFileLocation());
                    } else {
                        System.out.println("Failed to add tag to " + file.getFileLocation());
                    }
                } else {
                    System.out.println("Person not found");
                }
            } else {
                System.out.println("Invalid command");
            }
        } while (!selectedMediaCommand.equalsIgnoreCase(returnCommand));

    }

    private static void runReportMenu(Scanner scanner, Genealogy genealogy) {
        String selectedReportCommand = "";
        String userArgument = "";

        String findPersonCommand = "find-person";
        String findMediaCommand = "find-media";
        String findRelationshipCommand = "find-relationship";
        String findDescendantsCommand = "find-descendants";
        String findAncestorsCommand = "find-ancestors";
        String notesReferencesCommand = "notes-references";
        String findMediaByTag = "find-media-by-tag";
        String findMediaByLocation = "find-media-by-location";
        String findPeopleMedia = "find-people-media";
        String findBiologicalFamilyMedia = "find-biological-family-media";
        String returnCommand = "return";

        System.out.println("Commands available:");
        System.out.println("find-person - find a person");
        System.out.println("find-media - find a media file");
        System.out.println("find-relationship - find a relationship");
        System.out.println("find-descendants - find descendants");
        System.out.println("find-ancestors - find ancestors");
        System.out.println("notes-references - find notes and references");
        System.out.println("find-media-by-tag - find media by tag");
        System.out.println("find-media-by-location - find media by location");
        System.out.println("find-people-media - find media containing people");
        System.out.println("find-biological-family-media - find media containing biological family");


        do {
            System.out.println("Enter a command:");
            selectedReportCommand = scanner.nextLine();

            if(selectedReportCommand.equalsIgnoreCase(returnCommand)){
                break;
            }

            if(selectedReportCommand.equalsIgnoreCase(findPersonCommand)) {
                System.out.println("Enter the name of the person to find:");
                userArgument = scanner.nextLine();
                PersonIdentity person = genealogy.findPerson(userArgument);
                if (person != null) {
                    System.out.println("Person found: " + person.getName());
                } else {
                    System.out.println("Person not found");
                }
            } else if (selectedReportCommand.equalsIgnoreCase(findMediaCommand)) {
                System.out.println("Enter the name of the media file to find:");
                userArgument = scanner.nextLine();
                FileIdentifier file = genealogy.findMediaFile(userArgument);
                if (file != null) {
                    System.out.println("Media file found: " + file.getFileLocation());
                } else {
                    System.out.println("Media file not found");
                }
            } else if (selectedReportCommand.equalsIgnoreCase(findRelationshipCommand)) {
                System.out.println("Enter the name of person 1:");
                userArgument = scanner.nextLine();
                PersonIdentity person1 = genealogy.findPerson(userArgument);
                System.out.println("Enter the name of person 2:");
                userArgument = scanner.nextLine();
                PersonIdentity person2 = genealogy.findPerson(userArgument);

                if(person1==null || person2==null) {
                    System.out.println("Person not found");
                    continue;
                }

                BiologicalRelation relationship = genealogy.findRelation(person1, person2);
                if (relationship != null) {
                    System.out.println("Relationship found: ");
                    System.out.println(relationship);
                } else {
                    System.out.println("Relationship not found");
                }
            } else if (selectedReportCommand.equalsIgnoreCase(findDescendantsCommand)) {
                System.out.println("Enter the name of the person to find descendants of:");
                userArgument = scanner.nextLine();
                PersonIdentity person = genealogy.findPerson(userArgument);
                System.out.println("Enter the number of generations to search:");
                int generations = Integer.parseInt(scanner.nextLine());
                if (person != null) {
                    Set<PersonIdentity> descendants = genealogy.descendents(person, generations);
                    if (descendants != null) {
                        System.out.println("Descendants found:");
                        for (PersonIdentity descendant : descendants) {
                            System.out.println(descendant.getName());
                        }
                    } else {
                        System.out.println("Descendants not found");
                    }
                } else {
                    System.out.println("Person not found");
                }
            } else if (selectedReportCommand.equalsIgnoreCase(findAncestorsCommand)) {
                System.out.println("Enter the name of the person to find ancestors of:");
                userArgument = scanner.nextLine();
                PersonIdentity person = genealogy.findPerson(userArgument);
                System.out.println("Enter the number of generations to search:");
                int generations = Integer.parseInt(scanner.nextLine());
                if (person != null) {
                    Set<PersonIdentity> ancestors = genealogy.ancestors(person, generations);
                    if (ancestors != null) {
                        System.out.println("Ancestors found:");
                        for (PersonIdentity ancestor : ancestors) {
                            System.out.println(ancestor.getName());
                        }
                    } else {
                        System.out.println("Ancestors not found");
                    }
                } else {
                    System.out.println("Person not found");
                }
            } else if (selectedReportCommand.equalsIgnoreCase(notesReferencesCommand)) {
                System.out.println("Enter the name of the person to find notes and references of:");
                userArgument = scanner.nextLine();
                PersonIdentity person = genealogy.findPerson(userArgument);
                if (person != null) {
                    List<String> notesAndReferences = genealogy.notesAndReferences(person);
                    if (notesAndReferences != null) {
                        System.out.println("Notes and references found:");
                        for (String noteAndReference : notesAndReferences) {
                            System.out.println(noteAndReference);
                        }
                    } else {
                        System.out.println("Notes and references not found");
                    }
                } else {
                    System.out.println("Person not found");
                }
            } else if (selectedReportCommand.equalsIgnoreCase(findMediaByTag)) {
                System.out.println("Enter the tag to find media with:");
                userArgument = scanner.nextLine();
                System.out.println("Enter start date:");
                String startDate = scanner.nextLine();
                System.out.println("Enter end date:");
                String endDate = scanner.nextLine();
                Set<FileIdentifier> media = genealogy.findMediaByTag(userArgument, startDate, endDate);
                if (media != null) {
                    System.out.println("Media found:");
                    for (FileIdentifier mediaFile : media) {
                        System.out.println(mediaFile.getFileLocation());
                    }
                } else {
                    System.out.println("Media not found");
                }
            } else if (selectedReportCommand.equalsIgnoreCase(findMediaByLocation)) {
                System.out.println("Enter the location to find media with:");
                userArgument = scanner.nextLine();
                System.out.println("Enter start date:");
                String startDate = scanner.nextLine();
                System.out.println("Enter end date:");
                String endDate = scanner.nextLine();
                Set<FileIdentifier> media = genealogy.findMediaByLocation(userArgument, startDate, endDate);
                if (media != null) {
                    System.out.println("Media found:");
                    for (FileIdentifier mediaFile : media) {
                        System.out.println(mediaFile.getFileLocation());
                    }
                } else {
                    System.out.println("Media not found");
                }
            } else {
                System.out.println("Invalid command");
            }
        } while (!selectedReportCommand.equalsIgnoreCase(returnCommand));

    }
}
