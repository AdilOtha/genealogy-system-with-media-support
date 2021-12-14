import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Defines data and methods that can be used by the user to perform operations on the family tree and media archive.
 */
public class Genealogy {

    // keys that represent types of partnership in the database
    // key for marriage event
    static final int MARRIAGE_DB_KEY = 1;
    // key for divorce event
    static final int DIVORCE_DB_KEY = 2;

    // Represents conditions for methods where start and end dates are provided as parameters
    // condition if neither start nor end date is provided
    static final int NO_DATES_PROVIDED = 1;
    // condition if both start and end date is provided
    static final int BOTH_DATES_PROVIDED = 2;
    // condition if only start date is provided
    static final int START_DATE_PROVIDED = 3;
    // condition if only end date is provided
    static final int END_DATE_PROVIDED = 4;


    /**
     * Add an individual to the family tree database.
     * @param name : name of the individual
     * @return PersonIdentity object to identify an individual
     */
    public PersonIdentity addPerson(String name){
        // input validation
        if(name==null){
            throw new IllegalArgumentException("Name cannot be null");
        }
        if(name.trim().isEmpty()){
           throw new IllegalArgumentException("Name cannot be an empty string");
        }

        // define SQL query to insert person data
        String SQL = "INSERT INTO person_details(name) VALUES(?)";
        Connection conn = null;
        PreparedStatement pStmt = null;
        PersonIdentity person = null;
        ResultSet generatedKeys = null;
        int result = 0;
        boolean exceptionOccurred = false;

        try {
            // get Connection object for DB interaction
            conn = DBConnection.getConnection();

            // form PreparedStatement based on above query
            pStmt = conn.prepareStatement(SQL, new String[] {"person_id"});

            // set parameter for PreparedStatement query
            pStmt.setString(1,name);

            // execute the insert query
            result = pStmt.executeUpdate();

            // get last inserted ID of the individual
            generatedKeys = pStmt.getGeneratedKeys();

            // if last inserted ID is not found then throw exception
            if (result==0 || !generatedKeys.isBeforeFirst()){
                throw new SQLException("Error while adding person");
            } else {
                int insertedId = 0;
                while (generatedKeys.next()){
                    insertedId = generatedKeys.getInt(1);
                }
                if(insertedId==0){
                    throw new SQLException("Error while adding person");
                }

                // create new person identity object from data fetched from database
                person = new PersonIdentity(insertedId, name);
            }
        } catch(SQLException e){
            // set flag to true if exception occurred
            exceptionOccurred = true;
        } finally { // close Connection, PreparedStatement and ResultSet object if used
            if(pStmt!=null){
                try {
                    pStmt.close();
                } catch (SQLException e) {
                }
            }
            if(generatedKeys!=null){
                try {
                    generatedKeys.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        if(exceptionOccurred){
            return null;
        }
        return person;
    }

    /**
     * Record a source reference material for the individual. A person can have multiple source
     * references for them.
     * @param person The person to assign the reference to
     * @param reference The reference to be assigned
     * @return true if reference was recorded
     */
    public Boolean recordReference(PersonIdentity person, String reference){
        // input validation
        if(person==null){
            throw new IllegalArgumentException("person object cannot be null");
        }
        if(person.getPersonId()<1){
            throw new IllegalArgumentException("person does not exist");
        }
        if(reference==null){
            throw new IllegalArgumentException("reference cannot be null");
        }
        if(reference.trim().isEmpty()){
            throw new IllegalArgumentException("reference cannot be an empty string");
        }
        Connection conn = null;
        PreparedStatement pStmt = null;

        // define an SQL query to insert new reference
        String SQL = "INSERT INTO person_references (reference, person_id) VALUES(?, ?)";
        int result = 0;
        int index = 1;
        boolean exceptionOccurred = false;

        try{
            // get Connection object for DB interaction
            conn = DBConnection.getConnection();

            // form PreparedStatement based on above query
            pStmt = conn.prepareStatement(SQL);

            // set parameters for the query
            pStmt.setString(index++, reference);
            pStmt.setInt(index, person.getPersonId());

            // execute insert query
            result = pStmt.executeUpdate();
        } catch (SQLException e){
            // set flag to true if exception occurred
            exceptionOccurred = true;
        } finally { // close Connection, PreparedStatement object if used
            if (pStmt != null) {
                try {
                    pStmt.close();
                } catch (SQLException e) {
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }

        if(exceptionOccurred){
            return false;
        }

        // return true if the reference was recorded for the person
        return result!=0;
    }

    /**
     * Record a note for the individual.
     * @param person The person to assign the note to
     * @param note The note to be assigned
     * @return true if note was recorded
     */
    public Boolean recordNote(PersonIdentity person, String note){
        // input validation
        if(person==null){
            throw new IllegalArgumentException("person object cannot be null");
        }
        if(person.getPersonId()<1){
            throw new IllegalArgumentException("invalid person object");
        }
        if(note==null){
            throw new IllegalArgumentException("note cannot be null");
        }
        if(note.trim().isEmpty()){
            throw new IllegalArgumentException("note cannot be an empty string");
        }
        Connection conn = null;
        PreparedStatement pStmt = null;

        // define insert query to insert new note
        String SQL = "INSERT INTO person_notes (note, person_id) VALUES(?, ?)";
        int result = 0;
        int index = 1;
        boolean exceptionOccurred = false;

        try{
            // get Connection object for DB interaction
            conn = DBConnection.getConnection();

            // form PreparedStatement based on above query
            pStmt = conn.prepareStatement(SQL);

            // set parameters for the query
            pStmt.setString(index++, note);
            pStmt.setInt(index, person.getPersonId());

            // execute insert query
            result = pStmt.executeUpdate();
        } catch (SQLException e){
            // set flag to true if exception occurred
            exceptionOccurred = true;
        } finally { // close Connection, PreparedStatement object if used
            if (pStmt != null) {
                try {
                    pStmt.close();
                } catch (SQLException e) {
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }

        if(exceptionOccurred){
            return false;
        }

        // return true if the note was recorded for the person
        return result!=0;
    }

    /**
     * Locate an individual in the family tree.
     * @param name Name of the person to search
     * @return PersonIdentity object containing ID and name of that person, if found
     * throws RuntimeException if more than one person with same name is found
     */
    public PersonIdentity findPerson(String name){
        // input validation
        if(name==null){
            throw new IllegalArgumentException("name cannot be null");
        }
        if(name.trim().isEmpty()){
            throw new IllegalArgumentException("name cannot be an empty string");
        }
        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet resultSet = null;
        PersonIdentity person = null;
        boolean exceptionOccurred = false;

        // define query to find a person by name
        String SQL = "SELECT * FROM person_details WHERE name=?";

        try {
            // get Connection object for DB interaction
            conn = DBConnection.getConnection();

            // form PreparedStatement based on above query
            pStmt = conn.prepareStatement(SQL);

            // set parameters for the query
            pStmt.setString(1, name);

            // execute query
            resultSet = pStmt.executeQuery();

            int personId = 0;
            int rowCount = 0;

            // iterate over resultSet to find the ID of the person
            while (resultSet.next()){
                // increment rowCount for every row fetched from DB
                rowCount++;
                personId = resultSet.getInt("person_id");
            }

            // if more than one row found, i.e., more than one person, then throw exception
            if(rowCount>1){
                throw new RuntimeException();
            }

            // if personID is valid then create new PersonIdentity object
            if(personId != 0){
                person = new PersonIdentity(personId, name);
            }

        } catch (SQLException e) {
            // set flag to true if exception occurred
            exceptionOccurred = true;
        } finally { // close Connection, PreparedStatement and ResultSet object if used
            if(pStmt!=null){
                try {
                    pStmt.close();
                } catch (SQLException e) {
                }
            }
            if(resultSet!=null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                }
            }
            try {
                if(conn!=null){
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }
        if(exceptionOccurred){
            // return null object if exception occurred
            return null;
        }
        return person;
    }

    /**
     * Return the name of an individual.
     * @param person the PersonIdentity object whose name is to be found
     * @return String: name of the individual
     */
    String findName(PersonIdentity person) {
        // input validation
        if(person == null){
            throw new IllegalArgumentException("person object cannot be null");
        }
        if(person.getPersonId()<1){
            throw new IllegalArgumentException("invalid person object");
        }
        // return name of the person
        return person.getName();
    }

    /**
     * Add a media file to the media archive
     * @param fileLocation
     * @return FileIdentifier object containing ID and location of the file
     */
    FileIdentifier addMediaFile(String fileLocation) {
        // input validation
        if(fileLocation==null){
            throw new IllegalArgumentException("file location cannot be null");
        }
        // check if fileLocation is an empty string
        if(fileLocation.trim().isEmpty()){
            throw new IllegalArgumentException("file location cannot be an empty string");
        }
        // SQL query to check for existing file
        String CHECK_EXISTING_FILE = "SELECT * FROM media_details WHERE file_location=?";

        // SQL query to insert new file to database
        String SQL = "INSERT INTO media_details(file_location) VALUES(?)";
        Connection conn = null;
        PreparedStatement pStmt = null;
        FileIdentifier fileIdentifier = null;
        ResultSet generatedKeys = null;
        ResultSet resultSet = null;
        int result = 0;
        boolean exceptionOccurred = false;
        int existingMediaId = 0;

        try {
            // get Connection object for DB interaction
            conn = DBConnection.getConnection();

            // form PreparedStatement for select query
            pStmt=conn.prepareStatement(CHECK_EXISTING_FILE);

            // set parameters for the query
            pStmt.setString(1,fileLocation);

            // execute query
            resultSet = pStmt.executeQuery();

            // get existing media ID if exists
            while(resultSet.next()){
                existingMediaId=resultSet.getInt("media_id");
            }

            // throw exception if media file already exists
            if(existingMediaId!=0){
                throw new IllegalArgumentException("Media file already exists");
            }

            pStmt.close();

            // form PreparedStatement for insert query
            pStmt = conn.prepareStatement(SQL, new String[] {"media_id"});

            // set parameters for the query
            pStmt.setString(1,fileLocation);

            // execute insert query
            result = pStmt.executeUpdate();

            // get last inserted ID
            generatedKeys = pStmt.getGeneratedKeys();

            // if last inserted ID is not found then throw exception
            if (result==0 || !generatedKeys.isBeforeFirst()){
                throw new SQLException();
            } else {
                int insertedId = 0;
                // fetch last inserted ID
                while (generatedKeys.next()){
                    insertedId = generatedKeys.getInt(1);
                }
                if(insertedId==0){
                    throw new SQLException();
                }

                // create new FileIdentifier object from data from database
                fileIdentifier = new FileIdentifier(insertedId, fileLocation);
            }
        } catch(SQLException e){
            // set flag to true if exception occurred
            exceptionOccurred = true;
        } finally { // close Connection, PreparedStatement and ResultSet object if used
            if(pStmt!=null){
                try {
                    pStmt.close();
                } catch (SQLException e) {
                }
            }
            if(generatedKeys!=null){
                try {
                    generatedKeys.close();
                } catch (SQLException e) {
                }
            }
            if(resultSet!=null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        if(exceptionOccurred){
            // return null media object if exception occurred
            return null;
        }
        return fileIdentifier;
    }

    /**
     * Locate a media file in the media archive
     * @param fileLocation the unique location of the file to search
     * @return FileIdentifier object containing ID and location of the file
     */
    FileIdentifier findMediaFile(String fileLocation){
        // input validation
        if(fileLocation==null){
            throw new IllegalArgumentException("file location cannot be null");
        }
        if(fileLocation.trim().isEmpty()){
            throw new IllegalArgumentException("file location cannot be an empty string");
        }
        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet resultSet = null;
        FileIdentifier fileIdentifier = null;
        boolean exceptionOccurred = false;

        // define SQL query to find media file from fileLocation
        String SQL = "SELECT * FROM media_details WHERE file_location=?";;
        try {
            // get Connection object for DB interaction
            conn = DBConnection.getConnection();

            // form PreparedStatement based on above query
            pStmt = conn.prepareStatement(SQL);

            // set parameters for the query
            pStmt.setString(1, fileLocation);

            // execute query
            resultSet = pStmt.executeQuery();

            int media_id = 0;
            int rowCount=0;

            while (resultSet.next()){
                rowCount++;
                media_id = resultSet.getInt("media_id");
            }

            // throw exception if more than one file exists for a fileLocation
            if(rowCount>1){
                throw new RuntimeException();
            }

            if(media_id != 0){
                // create FileIdentifier object from data from database
                fileIdentifier = new FileIdentifier(media_id, fileLocation);
            }

        } catch (SQLException e) {
            // set flag to true if exception occurred
            exceptionOccurred=true;
        } finally { // close Connection, PreparedStatement and ResultSet object if used
            if(pStmt!=null){
                try {
                    pStmt.close();
                } catch (SQLException e) {
                }
            }
            if(resultSet!=null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                }
            }
            try {
                if(conn!=null){
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }
        if(exceptionOccurred){
            // return null object if exception occurred
            return null;
        }
        return fileIdentifier;
    }

    /**
     * Returns the unique location of a file
     * @param file FileIdentifier object whose location is to be found
     * @return String: unique location of a file
     */
    String findFileLocation(FileIdentifier file) {
        // input validation
        if(file==null){
            throw new IllegalArgumentException("file object cannot be null");
        }
        if(file.getMediaId()<1){
            throw new IllegalArgumentException("invalid file object");
        }
        // return fileLocation
        return file.getFileLocation();
    }

    /**
     * Record that a set of people appear in the given media file.
     * @param fileIdentifier FileIdentifier object where the people appear
     * @param people List of PersonIdentity objects that represent the people
     * @return True if the people are now connected to the medial file in the system, else false
     */
    Boolean peopleInMedia(FileIdentifier fileIdentifier, List<PersonIdentity> people){
        // input validation
        if(fileIdentifier==null){
            throw new IllegalArgumentException("FileIdentifier object cannot be null");
        }
        if(fileIdentifier.getMediaId()<1){
            throw new IllegalArgumentException("invalid FileIdentifier object");
        }
        if(people==null){
            throw new IllegalArgumentException("list of people cannot be null");
        }
        if(people.isEmpty()){
            return true;
        }
        int i=0;

        // build insert query depending on number of people
        StringBuilder SQL = new StringBuilder("INSERT INTO person_media(person_id, media_id) VALUES ");
        for(PersonIdentity person : people){
            if(person.getPersonId()<1){
                throw new IllegalArgumentException("invalid person object in list");
            }
            SQL.append("( ?, ? ) ");
            if(i!=(people.size()-1)){
                SQL.append(", ");
            }
            i++;
        }
        // set condition in query to insert if record does not exist, else update the record
        SQL.append("AS new ON DUPLICATE KEY UPDATE person_id=new.person_id, media_id=new.media_id");

        Connection conn = null;
        PreparedStatement pStmt = null;
        int result = 0;
        boolean exceptionOccurred = false;

        try{
            // get Connection object for DB interaction
            conn = DBConnection.getConnection();

            int index=1;
            // form PreparedStatement based on above query
            pStmt = conn.prepareStatement(SQL.toString());

            // set parameters for the query, iterate over people list to add all their IDs
            for(PersonIdentity person : people) {
                pStmt.setInt(index++, person.getPersonId());
                pStmt.setInt(index++, fileIdentifier.getMediaId());
            }
            // execute the query
            result = pStmt.executeUpdate();

        } catch (SQLException e) {
            // set flag to true if exception occurred
            exceptionOccurred = true;
        } finally { // close Connection, PreparedStatement and ResultSet object if used
            if(pStmt!=null){
                try {
                    pStmt.close();
                } catch (SQLException e) {
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        if(exceptionOccurred){
            // return false if exception occurred
            return false;
        }
        return result!=0;
    }

    /**
     * Record a tag for a media file. A media file can have many tags.
     * @param fileIdentifier FileIdentifier object to which tag must be assigned
     * @param tag The unique tag to assign to the file
     * @return True if the tag was stored in the system
     */
    Boolean tagMedia(FileIdentifier fileIdentifier, String tag) {
        // input validation
        if(fileIdentifier==null){
            throw new IllegalArgumentException("FileIdentifier object cannot be null");
        }
        if(fileIdentifier.getMediaId()<1){
            throw new IllegalArgumentException("invalid FileIdentifier object");
        }
        if(tag==null){
            throw new IllegalArgumentException("tag cannot be null");
        }
        if(tag.trim().isEmpty()){
            throw new IllegalArgumentException("tag cannot be an empty string");
        }
        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet resultSet = null;
        ResultSet generatedKeys = null;

        // query to check for existing tag
        String CHECK_EXISTING_TAG = "SELECT * FROM media_tags_types WHERE tag_name=?";

        // query to check existing relation between media and tag
        String CHECK_EXISTING_RECORD = "SELECT * FROM media_tags WHERE media_id=? AND tag_id=?";

        // query to insert new tag
        String INSERT_NEW_TAG = "INSERT INTO media_tags_types(tag_name) VALUES(?)";

        // query to record new media-tag relation
        String RECORD_TAG = "INSERT INTO media_tags (tag_id, media_id) VALUES(?, ?)";
        int result = 0;
        int existingRecord = 0;
        boolean exceptionOccurred = false;

        int existingTagId=0;

        try{
            // get Connection object for DB interaction
            conn = DBConnection.getConnection();

            // form PreparedStatement based on above query
            pStmt = conn.prepareStatement(CHECK_EXISTING_TAG);

            // set parameters for the query
            pStmt.setString(1, tag);

            // execute query
            resultSet = pStmt.executeQuery();

            // fetch existing tag ID
            while (resultSet.next()){
                existingTagId=resultSet.getInt("tag_id");
            }

            // if tag does not exist, then add tag to DB
            if(existingTagId==0){
                pStmt.close();
                pStmt = conn.prepareStatement(INSERT_NEW_TAG, new String[]{"tag_id"});
                pStmt.setString(1,tag);

                result = pStmt.executeUpdate();
                generatedKeys=pStmt.getGeneratedKeys();

                if(result==0 || !generatedKeys.isBeforeFirst()){
                    throw new SQLException();
                }

                while(generatedKeys.next()){
                    existingTagId=generatedKeys.getInt(1);
                }
            } else { // check for existing media-tag relation
                pStmt.close();
                resultSet.close();

                pStmt = conn.prepareStatement(CHECK_EXISTING_RECORD);
                pStmt.setInt(1,fileIdentifier.getMediaId());
                pStmt.setInt(2,existingTagId);

                resultSet = pStmt.executeQuery();


                while(resultSet.next()){
                    existingRecord++;
                }
            }

            if(existingTagId==0){
                throw new SQLException();
            }

            // if media-tag relation does not exist then record new relation
            if(existingRecord==0){
                pStmt.close();
                pStmt = conn.prepareStatement(RECORD_TAG);

                pStmt.setInt(1,existingTagId);
                pStmt.setInt(2,fileIdentifier.getMediaId());

                result = pStmt.executeUpdate();

                if(result==0){
                    throw new SQLException();
                }
            }

        } catch (SQLException e){
            // set flag to true if exception occurred
            exceptionOccurred = true;
        } finally { // close Connection, PreparedStatement object if used
            if (pStmt != null) {
                try {
                    pStmt.close();
                } catch (SQLException e) {
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }

        if(exceptionOccurred){
            // return false if exception occurred
            return false;
        }

        return true;
    }

    /**
     * Record information about an individual in the family tree database. Each attribute name is the
     * key to the Map.
     * @param person PersonIdentity object for which we record the attributes
     * @param attributes key-value pairs that represent the attribute type and their values
     * @return True if all attributes were stored, else false
     */
    Boolean recordAttributes(PersonIdentity person, Map<String, String> attributes) {
        // input validation
        if(person==null){
            throw new IllegalArgumentException("Person object cannot be null");
        }
        if(person.getPersonId()<1){
            throw new IllegalArgumentException("invalid person object");
        }
        int attributeCount = attributes.size();
        if(attributeCount==0){
            throw new IllegalArgumentException("no attributes provided");
        }
        for(Map.Entry<String,String> attribute: attributes.entrySet()){
            if(attribute.getKey().trim().isEmpty()){
                throw new IllegalArgumentException("empty key passed in attribute");
            }
            if(attribute.getValue().trim().isEmpty()){
                throw new IllegalArgumentException("empty value passed in attribute");
            }
        }
        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet resultSet = null;
        boolean exceptionOccurred = false;
        int result = 0;

        Map<String, Integer> attributeTypes = new HashMap<>();

        // query to find attribute types
        String GET_ATTRIBUTE_KEYS = "SELECT * FROM person_attributes_types";

        // build query to insert the provided attributes
        StringBuilder INSERT_ATTRIBUTES_SQL = new StringBuilder("INSERT INTO person_attributes VALUES ");
        for(int i=0;i<attributeCount;i++){
            if(i<(attributeCount-1)){
                INSERT_ATTRIBUTES_SQL.append("(?,?,?), ");
            } else {
                INSERT_ATTRIBUTES_SQL.append("(?,?,?) ");
            }
        }

        // set condition in query to insert if record does not exist, else update the record
        INSERT_ATTRIBUTES_SQL.append("AS new ON DUPLICATE KEY UPDATE attribute_value=new.attribute_value");

        try {
            // get Connection object for DB interaction
            conn = DBConnection.getConnection();

            // form PreparedStatement to get attribute types
            pStmt = conn.prepareStatement(GET_ATTRIBUTE_KEYS);

            resultSet = pStmt.executeQuery();

            // if attribute types not found then throw exception
            if(!resultSet.isBeforeFirst()){
                throw new SQLException("No attribute types are defined in the database");
            }

            // store attribute types as key-value pairs
            while (resultSet.next()){
                attributeTypes.put(resultSet.getString("attribute_type"), resultSet.getInt("attribute_id"));
            }

            pStmt.close();

            // form PreparedStatement to record attributes
            pStmt = conn.prepareStatement(INSERT_ATTRIBUTES_SQL.toString());

            int index=1;
            for(Map.Entry<String,String> attribute: attributes.entrySet()){
                int attributeId = 0;

                // if new attribute type is NOT found then record this new type in database
                if(!attributeTypes.containsKey(attribute.getKey())){
                    attributeId = addNewAttributeType(attribute.getKey(), "person_attributes_types", conn);
                } else { // else record person-attribute relation
                    // check format if attribute key is a date
                    if(attribute.getKey().contains("date")){
                        if(!attribute.getValue().matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")
                                && !attribute.getValue().matches("^\\d{4}-(0[1-9]|1[0-2])")
                                && !attribute.getValue().matches("^\\d{4}")){
                            throw new IllegalArgumentException("Invalid Date Format");
                        }
                    }
                    // get attribute type ID
                    attributeId=attributeTypes.get(attribute.getKey());
                }
                if(attributeId==0){
                    throw new SQLException("Cannot find attribute type id");
                }
                // set parameters to record person-attribute relation
                pStmt.setInt(index++,person.getPersonId());
                pStmt.setInt(index++, attributeId);
                pStmt.setString(index++, attribute.getValue());
            }

            result = pStmt.executeUpdate();

            if(result==0){
                throw new SQLException();
            }

        } catch(SQLException sqe){
            // set flag to true if exception occurred
            exceptionOccurred = true;
        } finally {
            if(pStmt!=null){
                try {
                    pStmt.close();
                } catch (SQLException e) {
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        if(exceptionOccurred){
            return false;
        }

        return true;
    }

    /**
     * Adds a new Attribute type to the database
     * Used by recordAttributes() and recordMediaAttribute() method to add a new attribute type
     * if encountered in the Map passed as a parameter to those methods.
     * @param attributeType The new attribute type to record
     * @param tableName The name of the table to store the attribute type
     * @param conn The Connection object for database interaction
     * @return A positive integer if attribute type is recorded in the database, else 0
     */
    int addNewAttributeType(String attributeType, String tableName, Connection conn){
        if(attributeType==null){
            return 0;
        }
        if(attributeType.trim().isEmpty()){
            return 0;
        }

        PreparedStatement pStmt = null;
        boolean exceptionOccurred = false;
        ResultSet generatedKeys=null;
        int result = 0;
        int newKey=0;

        // query to insert new attribute type
        String INSERT_ATTRIBUTE_TYPE = "INSERT INTO "+tableName+" (attribute_type) VALUES(?)";

        try {
            pStmt = conn.prepareStatement(INSERT_ATTRIBUTE_TYPE,new String[]{"attribute_id"});

            pStmt.setString(1,attributeType);

            result=pStmt.executeUpdate();
            generatedKeys=pStmt.getGeneratedKeys();

            if(result==0 || generatedKeys==null){
                throw new SQLException();
            }

            while(generatedKeys.next()){
                newKey = generatedKeys.getInt(1);
            }

            if(newKey==0){
                throw new SQLException();
            }

        } catch(SQLException sqe){
            exceptionOccurred = true;
        } finally {
            if(pStmt!=null){
                try {
                    pStmt.close();
                } catch (SQLException e) {
                }
            }
        }
        if(exceptionOccurred){
            return 0;
        }
        return newKey;
    }

    /**
     * Record information about a media file in the archive. Each attribute name is the key to the
     * Map.
     * @param fileIdentifier FileIdentifier object for which we record the attributes
     * @param attributes key-value pairs that represent the attribute type and their values
     * @return True if all attributes were stored, else false
     */
    Boolean recordMediaAttributes(FileIdentifier fileIdentifier, Map<String, String> attributes) {
        // input validation
        if(fileIdentifier==null){
            throw new IllegalArgumentException("fileIdentifier object cannot be null");
        }
        if(fileIdentifier.getMediaId()<1){
            throw new IllegalArgumentException("invalid fileIdentifier object");
        }
        int attributeCount = attributes.size();
        if(attributeCount==0){
            throw new IllegalArgumentException("no attributes provided");
        }
        for(Map.Entry<String,String> attribute: attributes.entrySet()){
            if(attribute.getKey().trim().isEmpty()){
                throw new IllegalArgumentException("empty key passed in attribute");
            }
            if(attribute.getValue().trim().isEmpty()){
                throw new IllegalArgumentException("empty value passed in attribute");
            }
        }
        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet resultSet = null;
        boolean exceptionOccurred = false;
        int result = 0;

        Map<String, Integer> attributeTypes = new HashMap<>();

        // query to find attribute types
        String GET_ATTRIBUTE_KEYS = "SELECT * FROM media_attributes_types";

        // build query to insert the provided attributes
        StringBuilder INSERT_ATTRIBUTES_SQL = new StringBuilder("INSERT INTO media_attributes VALUES ");
        for(int i=0;i<attributeCount;i++){
            if(i<(attributeCount-1)){
                INSERT_ATTRIBUTES_SQL.append("(?,?,?), ");
            } else {
                INSERT_ATTRIBUTES_SQL.append("(?,?,?) ");
            }
        }

        // set condition in query to insert if record does not exist, else update the record
        INSERT_ATTRIBUTES_SQL.append("AS new ON DUPLICATE KEY UPDATE attribute_value=new.attribute_value");

        try {
            // get Connection object for DB interaction
            conn = DBConnection.getConnection();

            // form PreparedStatement to get attribute types
            pStmt = conn.prepareStatement(GET_ATTRIBUTE_KEYS);

            resultSet = pStmt.executeQuery();

            // if attribute types not found then throw exception
            if(!resultSet.isBeforeFirst()){
                throw new SQLException("No attribute types are defined in the database");
            }

            // store attribute types as key-value pairs
            while (resultSet.next()){
                attributeTypes.put(resultSet.getString("attribute_type"), resultSet.getInt("attribute_id"));
            }

            pStmt.close();
            // form PreparedStatement to record attributes
            pStmt = conn.prepareStatement(INSERT_ATTRIBUTES_SQL.toString());

            int index=1;
            for(Map.Entry<String,String> attribute: attributes.entrySet()){
                int attributeId = 0;
                // if new attribute type is NOT found then record this new type in database
                if(!attributeTypes.containsKey(attribute.getKey())){
                    attributeId = addNewAttributeType(attribute.getKey(), "media_attributes_types", conn);
                } else { // else record person-attribute relation
                    // check format if attribute key is a date
                    if(attribute.getKey().contains("date")){
                        if(!attribute.getValue().matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")
                        && !attribute.getValue().matches("^\\d{4}-(0[1-9]|1[0-2])")
                        && !attribute.getValue().matches("^\\d{4}")){
                            throw new IllegalArgumentException("Invalid Date format");
                        }
                    }
                    // store attribute type ID
                    attributeId=attributeTypes.get(attribute.getKey());
                }
                if(attributeId==0){
                    throw new SQLException("Cannot find attribute type id");
                }
                // set parameters to record person-attribute relation
                pStmt.setInt(index++,fileIdentifier.getMediaId());
                pStmt.setInt(index++, attributeId);
                pStmt.setString(index++, attribute.getValue());
            }

            result = pStmt.executeUpdate();

            if(result==0){
                throw new SQLException();
            }

        } catch(SQLException sqe){
            // set flag to true if exception occurred
            exceptionOccurred = true;
        } finally { // close Connection, PreparedStatement object if used
            if(pStmt!=null){
                try {
                    pStmt.close();
                } catch (SQLException e) {
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        if(exceptionOccurred){
            return false;
        }

        return true;
    }

    /**
     * Record a parent/child relation for the individuals
     * @param parent PersonIdentity object of parent
     * @param child PersonIdentity object of child
     * @return true if the relation was stored in the system, else false
     */
    Boolean recordChild(PersonIdentity parent, PersonIdentity child) {
        // input validation
        if(parent==null){
            throw new IllegalArgumentException("parent object cannot be null");
        }
        if(child==null){
            throw new IllegalArgumentException("child object cannot be null");
        }
        if(parent.getPersonId()<1){
            throw new IllegalArgumentException("invalid parent object");
        }
        if(child.getPersonId()<1){
            throw new IllegalArgumentException("invalid child object");
        }
        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet resultSet = null;
        int result = 0;
        boolean exceptionOccurred = false;
        int noOfParents = 0, existingParentId=0,existingChildId=0;

        // query to find existing relation between parent and child
        String CHECK_EXISTING_RELATION = "SELECT * FROM parent_child WHERE parent_id=? AND child_id=?";

        // query to check number of parents for the child
        String FIND_PARENTS = "SELECT COUNT(parent_id) as noOfParents FROM parent_child WHERE child_id=?";

        // query to record parent-child relation
        String INSERT_NEW_CHILD = "INSERT INTO parent_child VALUES(?,?)";

        try {
            // get Connection object for DB interaction
            conn = DBConnection.getConnection();

            pStmt=conn.prepareStatement(CHECK_EXISTING_RELATION);
            pStmt.setInt(1,parent.getPersonId());
            pStmt.setInt(2,child.getPersonId());
            resultSet = pStmt.executeQuery();

            while(resultSet.next()){
                existingParentId=resultSet.getInt("parent_id");
                existingChildId=resultSet.getInt("child_id");
            }

            // if relation does not exist between parent and child then check no. of parents
            if(!(existingParentId>0 && existingChildId>0)){
                pStmt.close();
                resultSet.close();
                pStmt = conn.prepareStatement(FIND_PARENTS);
                pStmt.setInt(1,child.getPersonId());
                resultSet = pStmt.executeQuery();

                while(resultSet.next()){
                    noOfParents = resultSet.getInt("noOfParents");
                }

                // throw exception if 2 parents already exist for the child
                if(noOfParents>=2){
                    throw new IllegalArgumentException("2 parents already exist for child");
                }

                pStmt.close();
                // record parent-child relation
                pStmt = conn.prepareStatement(INSERT_NEW_CHILD);
                pStmt.setInt(1,parent.getPersonId());
                pStmt.setInt(2,child.getPersonId());

                result = pStmt.executeUpdate();

                if(result==0){
                    throw new SQLException();
                }
            }

        } catch(SQLException e){
            // set flag to true if exception occurred
            exceptionOccurred = true;
        } finally { // close Connection, PreparedStatement and ResultSet object if used
            if(pStmt!=null){
                try {
                    pStmt.close();
                } catch (SQLException e) {
                }
            }
            if(resultSet!=null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        if(exceptionOccurred){
            return false;
        }
        return true;
    }

    /**
     * Report all descendants in the family tree who are within “generations” generations of the
     * person.
     * @param person PersonIdentity object of individual
     * @param generations The level of generation to search for. The children of “person” as being 1 generation away
     * @return Set of PersonIdentity objects that represent the descendants
     */
    Set<PersonIdentity> descendants(PersonIdentity person, Integer generations){
        // input validation
        if(person==null){
            throw new IllegalArgumentException("person object cannot be null");
        }

        if(person.getPersonId()<1){
            throw new IllegalArgumentException("invalid person object");
        }

        // throw exception if generations is provided as negative integer
        if(generations<0){
            throw new IllegalArgumentException("generations cannot be a negative integer");
        }
        Set<PersonIdentity> descendants = new LinkedHashSet<>();
        // if generations is 0 then return empty set
        if(generations==0){
            return descendants;
        }

        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet resultSet = null;
        boolean exceptionOccurred = false;

        int parentId = person.getPersonId();

        // define recursive query to find all descendants
        String FIND_DESCENDANTS_RECURSIVE = "with recursive descendants (child_id, gen) as "+
                "(select child_id, 1 from parent_child where parent_id=? " +
                "union all " +
                "select pc.child_id, d.gen+1 from descendants d " +
                "inner join parent_child pc on pc.parent_id=d.child_id where d.gen<?) " +
                "select pd.person_id, pd.name from descendants d inner join person_details pd on d.child_id=pd.person_id";

        try {
            // get Connection object for DB interaction
            conn = DBConnection.getConnection();

            // form PreparedStatement from query
            pStmt = conn.prepareStatement(FIND_DESCENDANTS_RECURSIVE);

            // set parameters for query
            pStmt.setInt(1, parentId);
            pStmt.setInt(2, generations);

            resultSet = pStmt.executeQuery();

            // iterate over resultSet to add descendants to the set
            while(resultSet.next()){
                int childId = resultSet.getInt("person_id");
                String childName = resultSet.getString("name");
                if(childId < 1 || childName == null || childName.trim().isEmpty()){
                    throw new SQLException();
                }
                PersonIdentity child = new PersonIdentity(childId,childName);
                descendants.add(child);
            }

        } catch (SQLException sqe){
            // set flag to true if exception occurred
            exceptionOccurred=true;
        } finally { // close Connection, PreparedStatement and ResultSet object if used
            if(pStmt!=null){
                try {
                    pStmt.close();
                } catch (SQLException e) {
                }
            }
            if(resultSet!=null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }

        if(exceptionOccurred){
            return null;
        }

        return descendants;
    }

    /**
     * Report all ancestors in the family tree who are within “generations” generations of the
     * person.
     * @param person PersonIdentity object of individual
     * @param generations The level of generation to search for. The children of “person” as being 1 generation away
     * @return Set of PersonIdentity objects that represent the ancestors
     */
    Set<PersonIdentity> ancestors(PersonIdentity person, Integer generations){
        // input validation
        if(person==null){
            throw new IllegalArgumentException("person object cannot be null");
        }

        if(person.getPersonId()<1){
            throw new IllegalArgumentException("invalid person object");
        }

        // throw exception if generations is provided as negative integer
        if(generations<0){
            throw new IllegalArgumentException("generations cannot be a negative integer");
        }

        Set<PersonIdentity> ancestors = new LinkedHashSet<>();
        // if generations is 0 then return empty set
        if(generations==0){
            return ancestors;
        }

        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet resultSet = null;
        boolean exceptionOccurred = false;

        int childId = person.getPersonId();

        // define recursive query to find all ancestors
        String FIND_ANCESTORS_RECURSIVE = "with recursive ancestors (parent_id, gen) as " +
                "(select parent_id, 1 from parent_child where child_id=? " +
                "union all " +
                "select pc.parent_id, a.gen+1 from parent_child pc " +
                "inner join ancestors a on pc.child_id=a.parent_id where a.gen<?) " +
                "select pd.person_id, pd.name from ancestors a inner join person_details pd on a.parent_id=pd.person_id";

        try {
            // get Connection object for DB interaction
            conn = DBConnection.getConnection();

            // form PreparedStatement from query
            pStmt = conn.prepareStatement(FIND_ANCESTORS_RECURSIVE);

            // set parameters for query
            pStmt.setInt(1, childId);
            pStmt.setInt(2, generations);

            resultSet = pStmt.executeQuery();

            // iterate over resultSet to add descendants to the set
            while(resultSet.next()){
                int parentId = resultSet.getInt("person_id");
                String parentName = resultSet.getString("name");
                if(parentId < 1 || parentName == null || parentName.trim().isEmpty()){
                    throw new SQLException();
                }
                PersonIdentity parent = new PersonIdentity(parentId,parentName);
                ancestors.add(parent);
            }

        } catch (SQLException sqe){
            // set flag to true if exception occurred
            exceptionOccurred=true;
        } finally { // close Connection, PreparedStatement and ResultSet object if used
            if(pStmt!=null){
                try {
                    pStmt.close();
                } catch (SQLException e) {
                }
            }
            if(resultSet!=null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }

        if(exceptionOccurred){
            return null;
        }

        return ancestors;
    }

    /**
     * Report how two individuals are related based on cousinship and level of separation
     * @param person1 PersonIdentity object of individual 1
     * @param person2 PersonIdentity object of individual 2
     * @return BiologicalRelation object that represents cousinship and level of separation
     */
    BiologicalRelation findRelation(PersonIdentity person1, PersonIdentity person2) {
        // input validation
        if(person1==null || person2==null){
            throw new IllegalArgumentException("person objects cannot be null");
        }
        if(person1.getPersonId()<1 || person2.getPersonId()<1){
            throw new IllegalArgumentException("invalid person objects");
        }
        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet resultSet = null;
        boolean exceptionOccurred = false;

        int depth1=-1;
        int depth2=-1;
        int LCA = -1;
        BiologicalRelation biologicalRelation=null;

        // define query to find common ancestor between 2 people and their respective depths to that ancestor
        String FIND_LCA = "with table1 as ( " +
                "with recursive parentList1 (child_id, parent_id, depth) as ( " +
                "select child_id, parent_id, 1 from parent_child where child_id=? " +
                "union all " +
                "select pc.child_id, pc.parent_id, depth+1 from parent_child pc " +
                "inner join parentList1 pl1 on pc.child_id=pl1.parent_id " +
                "), initialNodeTable1 as ( " +
                "select child_id, 0 as depth from parent_child where child_id=?) " +
                "select child_id as parent_id, depth from initialNodeTable1 union select parent_id, depth from parentList1 " +
                "), table2 as ( " +
                "with recursive parentList2 (child_id, parent_id, depth) as ( " +
                "select child_id, parent_id, 1 from parent_child where child_id=? " +
                "union all " +
                "select pc.child_id, pc.parent_id, depth+1 from parent_child pc " +
                "inner join parentList2 pl2 on pc.child_id=pl2.parent_id " +
                "), initialNodeTable2 as ( " +
                "select child_id, 0 as depth from parent_child where child_id=?) " +
                "select child_id as parent_id, depth from initialNodeTable2 union select parent_id, depth from parentList2 " +
                ") select t1.parent_id as LCA, t1.depth as depth1, t2.depth as depth2 from table1 t1 inner join table2 t2 on t1.parent_id=t2.parent_id LIMIT 1";

        try {
            // get Connection object for DB interaction
            conn = DBConnection.getConnection();

            pStmt=conn.prepareStatement(FIND_LCA);

            // set parameters for query
            pStmt.setInt(1,person1.getPersonId());
            pStmt.setInt(2,person1.getPersonId());
            pStmt.setInt(3,person2.getPersonId());
            pStmt.setInt(4,person2.getPersonId());

            resultSet = pStmt.executeQuery();

            // store lowest common ancestor person ID, and the depths of each person supplied to that ancestor
            while(resultSet.next()){
                LCA=resultSet.getInt("LCA");
                depth1=resultSet.getInt("depth1");
                depth2=resultSet.getInt("depth2");
            }

            if(LCA==-1 || depth1==-1 || depth2==-1){
                throw new SQLException();
            }

            // find (minimum of depths - 1) to get cousinship
            int cousinship = Math.min(depth1, depth2)-1;

            // create BiologicalRelation object
            biologicalRelation = new BiologicalRelation();
            biologicalRelation.setCousinship(cousinship);
            // find absolute difference between 2 depths to get level of separation
            biologicalRelation.setRemoval(Math.abs(depth1-depth2));

        } catch (SQLException sqe){
            // set flag to true if exception occurred
            exceptionOccurred=true;
        } finally { // close Connection, PreparedStatement and ResultSet object if used
            if(pStmt!=null){
                try {
                    pStmt.close();
                } catch (SQLException e) {
                }
            }
            if(resultSet!=null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }

        if(exceptionOccurred){
            return null;
        }

        return biologicalRelation;
    }

    /**
     * Record a symmetric partnering relation between the individuals.
     * @param partner1 PersonIdentity object of individual 1
     * @param partner2 PersonIdentity object of individual 2
     * @return true if the partnership was stored in the system, else false
     */
    Boolean recordPartnering(PersonIdentity partner1, PersonIdentity partner2){
        // input validation
        if(partner1==null || partner2==null){
            throw new IllegalArgumentException("person objects cannot be null");
        }
        if(partner1.getPersonId()<1 || partner2.getPersonId()<1){
            throw new IllegalArgumentException("invalid person objects");
        }
        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet resultSet = null;
        boolean exceptionOccurred = false;

        // query to find the latest existing event type between the pair of individuals
        String FIND_EXISTING_EVENT_TYPE = "SELECT event_type_id FROM person_events WHERE (person_id_1=? AND person_id_2=?) OR (person_id_2=? AND person_id_1=?) ORDER BY event_id DESC LIMIT 1";

        // query to record partnership between individuals
        String INSERT_NEW_EVENT = "INSERT INTO person_events(person_id_1,person_id_2,event_type_id) VALUES(?,?,?)";
        int existingEventTypeId=0;
        int result=0;

        try {
            conn=DBConnection.getConnection();
            pStmt = conn.prepareStatement(FIND_EXISTING_EVENT_TYPE);

            pStmt.setInt(1,partner1.getPersonId());
            pStmt.setInt(2,partner2.getPersonId());
            pStmt.setInt(3,partner1.getPersonId());
            pStmt.setInt(4,partner2.getPersonId());

            resultSet = pStmt.executeQuery();

            while(resultSet.next()){
                existingEventTypeId=resultSet.getInt("event_type_id");
            }

            // if individuals are NOT already married then record new event
            if(existingEventTypeId != MARRIAGE_DB_KEY){
                pStmt.close();
                pStmt = conn.prepareStatement(INSERT_NEW_EVENT);

                pStmt.setInt(1,partner1.getPersonId());
                pStmt.setInt(2,partner2.getPersonId());
                pStmt.setInt(3,MARRIAGE_DB_KEY);

                result=pStmt.executeUpdate();

                if(result==0){
                    throw new SQLException();
                }
            }

        } catch (SQLException sqe){
            exceptionOccurred=true;
        } finally {
            if(pStmt!=null){
                try {
                    pStmt.close();
                } catch (SQLException e) {
                }
            }
            if(resultSet!=null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }

        if(exceptionOccurred){
            return false;
        }

        return true;
    }

    /**
     * Record a symmetric dissolution of a partnering relation between the individuals
     * @param partner1 PersonIdentity object of individual 1
     * @param partner2 PersonIdentity object of individual 2
     * @return true if the dissolution was stored in the system, else false
     */
    Boolean recordDissolution(PersonIdentity partner1, PersonIdentity partner2){
        // input validation
        if(partner1==null || partner2==null){
            throw new IllegalArgumentException("person objects cannot be null");
        }
        if(partner1.getPersonId()<1 || partner2.getPersonId()<1){
            throw new IllegalArgumentException("invalid person objects");
        }
        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet resultSet = null;
        boolean exceptionOccurred = false;

        // query to find the latest existing event type between the pair of individuals
        String FIND_EXISTING_EVENT_TYPE = "SELECT event_type_id FROM person_events WHERE (person_id_1=? AND person_id_2=?) OR (person_id_2=? AND person_id_1=?) ORDER BY event_id DESC LIMIT 1";

        // query to record partnership between individuals
        String INSERT_NEW_EVENT = "INSERT INTO person_events(person_id_1,person_id_2,event_type_id) VALUES(?,?,?)";
        int existingEventTypeId=0;
        int result=0;

        try {
            conn=DBConnection.getConnection();
            pStmt = conn.prepareStatement(FIND_EXISTING_EVENT_TYPE);

            pStmt.setInt(1,partner1.getPersonId());
            pStmt.setInt(2,partner2.getPersonId());
            pStmt.setInt(3,partner1.getPersonId());
            pStmt.setInt(4,partner2.getPersonId());

            resultSet = pStmt.executeQuery();

            while(resultSet.next()){
                existingEventTypeId=resultSet.getInt("event_type_id");
            }

            // if individuals are NOT already divorced then record new event
            if(existingEventTypeId != DIVORCE_DB_KEY){
                pStmt.close();
                pStmt = conn.prepareStatement(INSERT_NEW_EVENT);

                pStmt.setInt(1,partner1.getPersonId());
                pStmt.setInt(2,partner2.getPersonId());
                pStmt.setInt(3,DIVORCE_DB_KEY);

                result=pStmt.executeUpdate();

                if(result==0){
                    throw new SQLException();
                }
            }

        } catch (SQLException sqe){
            exceptionOccurred=true;
        } finally {
            if(pStmt!=null){
                try {
                    pStmt.close();
                } catch (SQLException e) {
                }
            }
            if(resultSet!=null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }

        if(exceptionOccurred){
            return false;
        }

        return true;
    }

    /**
     * Fetches all the notes and references on the individual, returned in the same order in which they
     * were added to the family tree.
     * @param person PersonIdentity object of individual
     * @return List of notes and references
     */
    List<String> notesAndReferences(PersonIdentity person){
        // input validation
        if(person==null){
            throw new IllegalArgumentException("person object cannot be null");
        }
        if(person.getPersonId()<1){
            throw new IllegalArgumentException("invalid person object");
        }

        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet resultSet = null;
        boolean exceptionOccurred = false;

        List<String> notesReferences = new ArrayList<>();

        // query to find notes and references for an individual, adding notes first then references
        // notes and references are ordered by their insertion ID
        String GET_NOTES_REFERENCES = "with table1 as ( " +
                "select pd.person_id, pd.name, pn.note as noteOrReference from person_details pd " +
                "inner join person_notes pn " +
                "on pd.person_id=pn.person_id " +
                "where pd.person_id=? " +
                "order by pn.note_id ASC " +
                "), table2 as ( " +
                "select pd.person_id, pd.name, pr.reference as noteOrReference from person_details pd " +
                "inner join person_references pr " +
                "on pd.person_id=pr.person_id " +
                "where pd.person_id=? " +
                "order by pr.reference_id ASC " +
                ") select * from table1 " +
                "union " +
                "select * from table2;";

        try {
            conn = DBConnection.getConnection();
            pStmt = conn.prepareStatement(GET_NOTES_REFERENCES);

            pStmt.setInt(1,person.getPersonId());
            pStmt.setInt(2,person.getPersonId());

            resultSet=pStmt.executeQuery();

            while(resultSet.next()){
                notesReferences.add(resultSet.getString("noteOrReference"));
            }

        }catch (SQLException sqe){
            exceptionOccurred=true;
        } finally {
            if(pStmt!=null){
                try {
                    pStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(resultSet!=null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        if(exceptionOccurred){
            return null;
        }

        return notesReferences;
    }

    /**
     * Fetches the set of media files linked to the given tag whose dates fall within the date range. Null
     * values for the dates indicate no restrictions on the dates.
     * @param tag The tag by which to search the files
     * @param startDate
     * @param endDate
     * @return Set of FileIdentifier objects that match the above filters
     */
    Set<FileIdentifier> findMediaByTag(String tag, String startDate, String endDate){
        // input validation
        if(tag==null){
            throw new IllegalArgumentException("tag cannot be null");
        }
        if(tag.trim().isEmpty()){
            throw new IllegalArgumentException("tag cannot be an empty string");
        }
        if(startDate!=null && startDate.trim().isEmpty()){
            throw new IllegalArgumentException("start date cannot be an empty string");
        }
        if(endDate!=null && endDate.trim().isEmpty()){
            throw new IllegalArgumentException("end date cannot be an empty string");
        }

        // regex to match date of types: yyyy-MM-dd, yyyy-MM, yyyy
        // if date does not match any of the above formats then throw exception
        if(startDate!=null && !startDate.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")
                && !startDate.matches("^\\d{4}-(0[1-9]|1[0-2])")
                && !startDate.matches("^\\d{4}")){
            throw new IllegalArgumentException("Invalid Start Date Format");
        }
        if(endDate!=null && !endDate.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")
                && !endDate.matches("^\\d{4}-(0[1-9]|1[0-2])")
                && !endDate.matches("^\\d{4}")){
            throw new IllegalArgumentException("Invalid End Date Format");
        }

        // check if start date lies after end date
        // if true then throw exception
        if(startDate!=null && endDate!=null){
            try {
                Date start;
                Date end;
                if(startDate.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")){
                    start = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
                } else if (startDate.matches("^\\d{4}-(0[1-9]|1[0-2])")){
                    start = new SimpleDateFormat("yyyy-MM").parse(startDate);
                } else {
                    start = new SimpleDateFormat("yyyy").parse(startDate);
                }

                if(endDate.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")){
                    end = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
                } else if (endDate.matches("^\\d{4}-(0[1-9]|1[0-2])")){
                    end = new SimpleDateFormat("yyyy-MM").parse(endDate);
                } else {
                    end = new SimpleDateFormat("yyyy").parse(endDate);
                }
                if(start.after(end)){
                    throw new IllegalArgumentException("Start Date occurs after End Date");
                }
            } catch (ParseException pe){
            }
        }

        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet resultSet = null;
        boolean exceptionOccurred = false;

        int dateCondition = 0;

        // query to check existence of provided tag in the database
        String CHECK_EXISTING_TAG = "SELECT * FROM media_tags_types WHERE tag_name=?";

        // build query to find media by tag and provided dates
        StringBuilder GET_MEDIA_BY_TAG = new StringBuilder("with table1 as( " +
                "select md.media_id, md.file_location, mtt.tag_name, mat.attribute_type, " +
                "STR_TO_DATE(ma.attribute_value,'%Y-%m-%d') as date_created " +
                "from media_details md " +
                "inner join media_tags mt on md.media_id=mt.media_id " +
                "inner join media_tags_types mtt on mt.tag_id=mtt.tag_id " +
                "left join media_attributes ma on md.media_id=ma.media_id " +
                "left join media_attributes_types mat on ma.attribute_id=mat.attribute_id " +
                "where mtt.tag_id=? " +
                "order by date_created " +
                "), table2 as ( " +
                "select media_id, file_location, tag_name, date_created from table1 " +
                "where attribute_type='date' and date_created ");

        // if neither start nor end date is provided then change query
        if(startDate==null && endDate==null){
            GET_MEDIA_BY_TAG = new StringBuilder("select md.media_id, md.file_location, mtt.tag_name " +
                    "from media_details md " +
                    "inner join media_tags mt on md.media_id=mt.media_id " +
                    "inner join media_tags_types mtt on mt.tag_id=mtt.tag_id " +
                    "where mtt.tag_id=?");
            dateCondition=NO_DATES_PROVIDED;
        } else if(startDate!=null && endDate!=null){ // condition to add if both start and end dates are provided
            GET_MEDIA_BY_TAG.append("between str_to_date(?,'%Y-%m-%d') and str_to_date(?,'%Y-%m-%d'))  " +
                    "select * from table2 " +
                    "order by date_created ASC, file_location ASC");
            dateCondition=BOTH_DATES_PROVIDED;
        } else if(endDate==null){ // condition to add if only end date is provided
            GET_MEDIA_BY_TAG.append(">= str_to_date(?,'%Y-%m-%d'))  " +
                    "select * from table2 " +
                    "order by date_created ASC, file_location ASC");
            dateCondition=START_DATE_PROVIDED;
        } else{ // condition to add if only start date is provided
            GET_MEDIA_BY_TAG.append("<= str_to_date(?,'%Y-%m-%d'))  " +
                    "select * from table2 " +
                    "order by date_created ASC, file_location ASC");
            dateCondition=END_DATE_PROVIDED;
        }

        Set<FileIdentifier> fileIdentifierSet=new LinkedHashSet<>();
        int existingTagId=0;

        try {
            conn = DBConnection.getConnection();
            pStmt = conn.prepareStatement(CHECK_EXISTING_TAG);

            pStmt.setString(1,tag);
            resultSet = pStmt.executeQuery();

            // if tag exists in DB then proceed to find media files
            if(resultSet.isBeforeFirst()){
                while(resultSet.next()){
                    existingTagId = resultSet.getInt("tag_id");
                }

                if(existingTagId==0){
                    throw new SQLException();
                }

                pStmt.close();
                resultSet.close();

                pStmt = conn.prepareStatement(GET_MEDIA_BY_TAG.toString());
                pStmt.setInt(1, existingTagId);

                // set parameters based on the provided start and end dates
                switch (dateCondition){
                    case BOTH_DATES_PROVIDED:
                        pStmt.setString(2, startDate);
                        pStmt.setString(3, endDate);
                        break;
                    case START_DATE_PROVIDED:
                        pStmt.setString(2, startDate);
                        break;
                    case END_DATE_PROVIDED:
                        pStmt.setString(2, endDate);
                        break;
                }

                boolean result=pStmt.execute();

                if(result){
                    resultSet= pStmt.getResultSet();
                    // add found media files to set
                    while(resultSet.next()){
                        int mediaId=resultSet.getInt("media_id");
                        String fileLocation=resultSet.getString("file_location");
                        FileIdentifier fileIdentifier = new FileIdentifier(mediaId,fileLocation);

                        fileIdentifierSet.add(fileIdentifier);
                    }
                }

            }

        } catch (SQLException sqe){
            // set flag to true if exception occurred
            exceptionOccurred=true;
        } finally { // close Connection, PreparedStatement and ResultSet object if used
            if(pStmt!=null){
                try {
                    pStmt.close();
                } catch (SQLException e) {
                }
            }
            if(resultSet!=null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }

        if(exceptionOccurred){
            return null;
        }

        return fileIdentifierSet;
    }

    /**
     * Fetches the set of media files linked to the given location whose dates fall within the date range.
     * Null values for the dates indicate no restrictions on the dates.
     * @param location Location to search for. Method can handle partial locations as well.
     *                 For example, Halifax will match Halifax and Halifax, Nova Scotia
     * @param startDate
     * @param endDate
     * @return Set of FileIdentifier objects that match the above filters
     */
    Set<FileIdentifier> findMediaByLocation(String location, String startDate, String endDate){
        // input validation
        if(location==null){
            throw new IllegalArgumentException("location cannot be null");
        }
        if(location.trim().isEmpty()){
            throw new IllegalArgumentException("location cannot be an empty string");
        }
        if(startDate!=null && startDate.trim().isEmpty()){
            throw new IllegalArgumentException("start date cannot be an empty string");
        }
        if(endDate!=null && endDate.trim().isEmpty()){
            throw new IllegalArgumentException("end date cannot be an empty string");
        }
        // regex to match date of types: yyyy-MM-dd, yyyy-MM, yyyy
        // if date does not match any of the above formats then throw exception
        if(startDate!=null && !startDate.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")
                && !startDate.matches("^\\d{4}-(0[1-9]|1[0-2])")
                && !startDate.matches("^\\d{4}")){
            throw new IllegalArgumentException("Invalid Start Date Format");
        }
        if(endDate!=null && !endDate.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")
                && !endDate.matches("^\\d{4}-(0[1-9]|1[0-2])")
                && !endDate.matches("^\\d{4}")){
            throw new IllegalArgumentException("Invalid End Date Format");
        }

        // check if start date lies after end date
        // if true then throw exception
        if(startDate!=null && endDate!=null){
            try {
                Date start;
                Date end;
                if(startDate.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")){
                    start = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
                } else if (startDate.matches("^\\d{4}-(0[1-9]|1[0-2])")){
                    start = new SimpleDateFormat("yyyy-MM").parse(startDate);
                } else {
                    start = new SimpleDateFormat("yyyy").parse(startDate);
                }

                if(endDate.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")){
                    end = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
                } else if (endDate.matches("^\\d{4}-(0[1-9]|1[0-2])")){
                    end = new SimpleDateFormat("yyyy-MM").parse(endDate);
                } else {
                    end = new SimpleDateFormat("yyyy").parse(endDate);
                }
                if(start.after(end)){
                    throw new IllegalArgumentException("Start Date occurs after End Date");
                }
            } catch (ParseException pe){
            }
        }

        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet resultSet = null;
        boolean exceptionOccurred = false;

        int dateCondition = 0;

        // build query to find media by tag and provided dates
        StringBuilder GET_MEDIA_BY_LOCATION = new StringBuilder("with table1 as ( " +
                "select md.media_id, md.file_location, mat.attribute_type, ma.attribute_value " +
                "from media_details md " +
                "inner join media_attributes ma on md.media_id=ma.media_id " +
                "inner join media_attributes_types mat on ma.attribute_id=mat.attribute_id " +
                "where mat.attribute_type='location' and ma.attribute_value LIKE ? " +
                "), table2 as ( " +
                "select md.media_id, md.file_location, mat.attribute_type, " +
                "STR_TO_DATE(ma.attribute_value,'%Y-%m-%d') as date_created " +
                "from media_details md " +
                "inner join media_attributes ma on md.media_id=ma.media_id " +
                "inner join media_attributes_types mat on ma.attribute_id=mat.attribute_id " +
                "where mat.attribute_type='date' " +
                "order by date_created " +
                "), table3 as ( " +
                "select t1.media_id,t1.file_location,t1.attribute_value as location, " +
                "t2.date_created from table1 t1 inner join table2 t2 " +
                "on t1.media_id=t2.media_id " +
                "where t2.date_created ");

        // if neither start nor end date is provided then change query
        if(startDate==null && endDate==null){
            GET_MEDIA_BY_LOCATION = new StringBuilder("select md.media_id, md.file_location, mat.attribute_type, ma.attribute_value " +
                    "from media_details md " +
                    "inner join media_attributes ma on md.media_id=ma.media_id " +
                    "inner join media_attributes_types mat on ma.attribute_id=mat.attribute_id " +
                    "where mat.attribute_type='location' and ma.attribute_value LIKE ? ");
            dateCondition=NO_DATES_PROVIDED;
        } else if(startDate!=null && endDate!=null){ // condition to add if both start and end dates are provided
            GET_MEDIA_BY_LOCATION.append(" between str_to_date(?,'%Y-%m-%d') and str_to_date(?,'%Y-%m-%d')) " +
            "select * from table3 ");
            dateCondition=BOTH_DATES_PROVIDED;
        } else if(endDate==null){ // condition to add if only start date is provided
            GET_MEDIA_BY_LOCATION.append(" >= str_to_date(?,'%Y-%m-%d')) " +
                    "select * from table3 t3");
            dateCondition=START_DATE_PROVIDED;
        } else{ // condition to add if only end date is provided
            GET_MEDIA_BY_LOCATION.append(" <= str_to_date(?,'%Y-%m-%d')) " +
                    "select * from table3 t3");
            dateCondition=END_DATE_PROVIDED;
        }

        Set<FileIdentifier> fileIdentifierSet=new LinkedHashSet<>();

        try {
            conn = DBConnection.getConnection();
            pStmt = conn.prepareStatement(GET_MEDIA_BY_LOCATION.toString());

            pStmt.setString(1,"%"+location+"%");

            // set parameters based on the provided start and end dates
            switch (dateCondition){
                case BOTH_DATES_PROVIDED:
                    pStmt.setString(2, startDate);
                    pStmt.setString(3, endDate);
                    break;
                case START_DATE_PROVIDED:
                    pStmt.setString(2, startDate);
                    break;
                case END_DATE_PROVIDED:
                    pStmt.setString(2, endDate);
                    break;
            }

            resultSet = pStmt.executeQuery();

            // add found media files to set
            while(resultSet.next()){
                int mediaId=resultSet.getInt("media_id");
                String fileLocation=resultSet.getString("file_location");
                FileIdentifier fileIdentifier = new FileIdentifier(mediaId,fileLocation);

                fileIdentifierSet.add(fileIdentifier);
            }

        } catch (SQLException sqe){
            exceptionOccurred=true;
        } finally {
            if(pStmt!=null){
                try {
                    pStmt.close();
                } catch (SQLException e) {
                }
            }
            if(resultSet!=null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }

        if(exceptionOccurred){
            return null;
        }

        return fileIdentifierSet;
    }

    /**
     * Fetches the set of media files that include the specified person’s immediate children.
     * Ordering of files in is ascending chronological order (breaking ties by the ascending order of the
     * file names). The files with no dates are at the end of the list.
     * @param person PersonIdentity object of individual
     * @return List of FileIdentifier objects that matches above criteria
     */
    List<FileIdentifier> findBiologicalFamilyMedia(PersonIdentity person){
        // input validation
        if(person==null){
            throw new IllegalArgumentException("person object cannot be null");
        }
        if(person.getPersonId()<1){
            throw new IllegalArgumentException("invalid person object");
        }
        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet resultSet = null;
        boolean exceptionOccurred = false;

        // query to find immediate children and their images
        String GET_FAMILY_MEDIA = "with table1 as( " +
                "with recursive descendants (child_id, gen) as ( " +
                "select child_id, 1 from parent_child where parent_id=? " +
                "union all " +
                "select pc.child_id, d.gen+1 from descendants d " +
                "inner join parent_child pc on pc.parent_id=d.child_id where d.gen<1 " +
                ") " +
                "select md.media_id, md.file_location, mat.attribute_type, " +
                "STR_TO_DATE(ma.attribute_value,'%Y-%m-%d') as date_created " +
                "from descendants d " +
                "inner join person_details pd on d.child_id=pd.person_id " +
                "inner join person_media pm on pd.person_id=pm.person_id " +
                "inner join media_details md on pm.media_id=md.media_id " +
                "left join media_attributes ma on pm.media_id=ma.media_id " +
                "left join media_attributes_types mat on ma.attribute_id=mat.attribute_id " +
                "order by date_created " +
                "), table2 as ( " +
                "select media_id, file_location, date_created from table1 where attribute_type='date' " +
                "), table3 as ( " +
                "select media_id, file_location, null as date_created from (select t1.media_id, t1.file_location, " +
                "sum(case when attribute_type='date' then 1 else 0 end) as recordsWithDate " +
                "from table1 t1 group by t1.media_id) as table4 where table4.recordsWithDate=0) " +
                "select * from table2 " +
                "union " +
                "select * from table3 order by -date_created DESC, file_location ASC;";

        List<FileIdentifier> fileIdentifierList=new ArrayList<>();

        try {
            conn=DBConnection.getConnection();
            pStmt = conn.prepareStatement(GET_FAMILY_MEDIA);

            pStmt.setInt(1,person.getPersonId());

            boolean result =pStmt.execute();

            if(!result){
                throw new SQLException();
            } else {
                resultSet=pStmt.getResultSet();

                // add media files to list
                while(resultSet.next()){
                    int mediaId = resultSet.getInt("media_id");
                    String fileLocation = resultSet.getString("file_location");
                    FileIdentifier fileIdentifier = new FileIdentifier(mediaId,fileLocation);
                    fileIdentifierList.add(fileIdentifier);
                }
            }

        } catch (SQLException sqe){
            exceptionOccurred=true;
        } finally {
            if(pStmt!=null){
                try {
                    pStmt.close();
                } catch (SQLException e) {
                }
            }
            if(resultSet!=null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }

        if(exceptionOccurred){
            return null;
        }

        return fileIdentifierList;
    }

    /**
     * Fetches the set of media files that include any of individuals given in the list of people whose
     * dates fall within the date range. Null values for the dates indicate no restrictions on the dates.
     * Ordering of files in is ascending chronological order (breaking ties by the ascending order of the
     * file names). The files with no dates are at the end of the list.
     * @param people PersonIdentity object of individual
     * @param startDate
     * @param endDate
     * @return List of FileIdentifier objects that matches above criteria
     */
    List<FileIdentifier> findIndividualsMedia(Set<PersonIdentity> people, String startDate, String
            endDate) {
        // input validation
        if(people==null){
            throw new IllegalArgumentException("set of people cannot be null");
        }

        List<FileIdentifier> fileIdentifierList=new ArrayList<>();
        // empty set is provided then return empty media file list
        if(people.size()==0){
            return fileIdentifierList;
        }

        if(startDate!=null && startDate.trim().isEmpty()){
            throw new IllegalArgumentException("start date cannot be an empty string");
        }
        if(endDate!=null && endDate.trim().isEmpty()){
            throw new IllegalArgumentException("end date cannot be an empty string");
        }

        // regex to match date of types: yyyy-MM-dd, yyyy-MM, yyyy
        // if date does not match any of the above formats then throw exception
        if(startDate!=null && !startDate.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")
                && !startDate.matches("^\\d{4}-(0[1-9]|1[0-2])")
                && !startDate.matches("^\\d{4}")){
            throw new IllegalArgumentException("Invalid Start Date Format");
        }
        if(endDate!=null && !endDate.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")
                && !endDate.matches("^\\d{4}-(0[1-9]|1[0-2])")
                && !endDate.matches("^\\d{4}")){
            throw new IllegalArgumentException("Invalid End Date Format");
        }

        // check if start date lies after end date
        // if true then throw exception
        if(startDate!=null && endDate!=null){
            try {
                Date start;
                Date end;
                if(startDate.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")){
                    start = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
                } else if (startDate.matches("^\\d{4}-(0[1-9]|1[0-2])")){
                    start = new SimpleDateFormat("yyyy-MM").parse(startDate);
                } else {
                    start = new SimpleDateFormat("yyyy").parse(startDate);
                }

                if(endDate.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")){
                    end = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
                } else if (endDate.matches("^\\d{4}-(0[1-9]|1[0-2])")){
                    end = new SimpleDateFormat("yyyy-MM").parse(endDate);
                } else {
                    end = new SimpleDateFormat("yyyy").parse(endDate);
                }
                if(start.after(end)){
                    throw new IllegalArgumentException("Start Date occurs after End Date");
                }
            } catch (ParseException pe){
            }
        }

        // build query to find media files for the provided set of people
        StringBuilder GET_INDIVIDUALS_MEDIA = new StringBuilder("with table1 as( " +
                "select md.media_id, md.file_location, mat.attribute_type, " +
                "STR_TO_DATE(ma.attribute_value,'%Y-%m-%d') as date_created " +
                "from person_details pd " +
                "inner join person_media pm on pd.person_id=pm.person_id " +
                "inner join media_details md on pm.media_id=md.media_id " +
                "left join media_attributes ma on pm.media_id=ma.media_id " +
                "left join media_attributes_types mat on ma.attribute_id=mat.attribute_id " +
                "where pd.person_id IN ");
        int i=0;
        int dateCondition=0;

        for(PersonIdentity person: people){
            if(person==null){
                throw new IllegalArgumentException("person object cannot be null");
            }
            if(person.getPersonId()<1){
                throw new IllegalArgumentException("invalid person object");
            }
            if(i==0){
                GET_INDIVIDUALS_MEDIA.append("( ");
            }
            GET_INDIVIDUALS_MEDIA.append("? ");
            if(i!=(people.size()-1)){
                GET_INDIVIDUALS_MEDIA.append(", ");
            } else {
                GET_INDIVIDUALS_MEDIA.append(") ");
            }
            i++;
        }

        GET_INDIVIDUALS_MEDIA.append(" order by date_created " +
                "), table2 as ( " +
                "select media_id, file_location, date_created from table1 where attribute_type='date' ");

        // if no dates are provided then no condition on date required
        if(startDate==null && endDate==null){
            GET_INDIVIDUALS_MEDIA.append("");
            dateCondition=NO_DATES_PROVIDED;
        } else if(startDate!=null && endDate!=null){ // condition to add if both start and end dates are provided
            GET_INDIVIDUALS_MEDIA.append(" and date_created between str_to_date(?,'%Y-%m-%d') and str_to_date(?,'%Y-%m-%d') ");
            dateCondition=BOTH_DATES_PROVIDED;
        } else if(endDate==null){ // condition to add if only start date is provided
            GET_INDIVIDUALS_MEDIA.append(" and date_created >= str_to_date(?,'%Y-%m-%d') ");
            dateCondition=START_DATE_PROVIDED;
        } else{ // condition to add if only end date is provided
            GET_INDIVIDUALS_MEDIA.append(" and date_created <= str_to_date(?,'%Y-%m-%d') ");
            dateCondition=END_DATE_PROVIDED;
        }

        GET_INDIVIDUALS_MEDIA.append(" ),table3 as ( " +
                "select media_id, file_location, null as date_created from (select t1.media_id, t1.file_location, " +
                "sum(case when attribute_type='date' then 1 else 0 end) as recordsWithDate " +
                "from table1 t1 group by t1.media_id) as table4 where table4.recordsWithDate=0) " +
                "select * from table2 " +
                "union " +
                "select * from table3 order by -date_created DESC, file_location ASC");

        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet resultSet = null;
        boolean exceptionOccurred = false;

        try {
            conn = DBConnection.getConnection();
            pStmt = conn.prepareStatement(GET_INDIVIDUALS_MEDIA.toString());

            int index=1;
            for(PersonIdentity person:people){
                pStmt.setInt(index++,person.getPersonId());
            }

            // set parameters based on the provided start and end dates
            switch (dateCondition){
                case BOTH_DATES_PROVIDED:
                    pStmt.setString(index++, startDate);
                    pStmt.setString(index++, endDate);
                    break;
                case START_DATE_PROVIDED:
                    pStmt.setString(index++, startDate);
                    break;
                case END_DATE_PROVIDED:
                    pStmt.setString(index++, endDate);
                    break;
            }

            boolean result=pStmt.execute();

            if(result){
                resultSet= pStmt.getResultSet();

                // add media files to list
                while(resultSet.next()){
                    int mediaId=resultSet.getInt("media_id");
                    String fileLocation=resultSet.getString("file_location");
                    FileIdentifier fileIdentifier = new FileIdentifier(mediaId,fileLocation);

                    fileIdentifierList.add(fileIdentifier);
                }
            } else {
                throw new SQLException();
            }


        } catch (SQLException sqe){
            // set flag to true if exception occurred
            exceptionOccurred=true;
        } finally { // close Connection, PreparedStatement and ResultSet object if used
            if(pStmt!=null){
                try {
                    pStmt.close();
                } catch (SQLException e) {
                }
            }
            if(resultSet!=null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }

        if(exceptionOccurred){
            return null;
        }

        return fileIdentifierList;
    }

}
