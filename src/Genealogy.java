import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class Genealogy {

    static final int MARRIAGE_DB_KEY = 1;
    static final int DIVORCE_DB_KEY = 2;
    static final int NO_DATES_PROVIDED = 1;
    static final int BOTH_DATES_PROVIDED = 2;
    static final int START_DATE_PROVIDED = 3;
    static final int END_DATE_PROVIDED = 4;


    public PersonIdentity addPerson(String name){
        if(name==null){
            throw new IllegalArgumentException("Name cannot be null");
        }
        if(name.trim().isEmpty()){
           throw new IllegalArgumentException("Name cannot be an empty string");
        }
        String SQL = "INSERT INTO person_details(name) VALUES(?)";
        Connection conn = null;
        PreparedStatement pStmt = null;
        PersonIdentity person = null;
        ResultSet generatedKeys = null;
        int result = 0;
        boolean exceptionOccurred = false;

        try {
            conn = DBConnection.getConnection();
            pStmt = conn.prepareStatement(SQL, new String[] {"person_id"});
            pStmt.setString(1,name);
            result = pStmt.executeUpdate();
            generatedKeys = pStmt.getGeneratedKeys();
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
                person = new PersonIdentity(insertedId, name);
            }
        } catch(SQLException e){
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
            return null;
        }
        return person;
    }

    public Boolean recordReference(PersonIdentity person, String reference){
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
        String SQL = "INSERT INTO person_references (reference, person_id) VALUES(?, ?)";
        int result = 0;
        int index = 1;
        boolean exceptionOccurred = false;

        try{
            conn = DBConnection.getConnection();
            pStmt = conn.prepareStatement(SQL);
            pStmt.setString(index++, reference);
            pStmt.setInt(index, person.getPersonId());
            result = pStmt.executeUpdate();
        } catch (SQLException e){
            exceptionOccurred = true;
        } finally {
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

        return result!=0;
    }

    public Boolean recordNote(PersonIdentity person, String note){
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
        String SQL = "INSERT INTO person_notes (note, person_id) VALUES(?, ?)";
        int result = 0;
        int index = 1;
        boolean exceptionOccurred = false;

        try{
            conn = DBConnection.getConnection();
            pStmt = conn.prepareStatement(SQL);
            pStmt.setString(index++, note);
            pStmt.setInt(index, person.getPersonId());
            result = pStmt.executeUpdate();
        } catch (SQLException e){
            exceptionOccurred = true;
        } finally {
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

        return result!=0;
    }

    public PersonIdentity findPerson(String name){
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

        String SQL = "SELECT * FROM person_details WHERE name=?";

        try {
                conn = DBConnection.getConnection();
                pStmt = conn.prepareStatement(SQL);
                pStmt.setString(1, name);
                resultSet = pStmt.executeQuery();
                int personId = 0;
                int rowCount = 0;
                while (resultSet.next()){
                    rowCount++;
                    personId = resultSet.getInt("person_id");
                }

                if(rowCount>1){
                    throw new RuntimeException();
                }

                if(personId != 0){
                    person = new PersonIdentity(personId, name);
                }

            } catch (SQLException e) {
                exceptionOccurred = true;
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
                try {
                    if(conn!=null){
                        conn.close();
                    }
                } catch (SQLException e) {
                }
            }
        if(exceptionOccurred){
            return null;
        }
        return person;
    }

    String findName(PersonIdentity person) {
        if(person == null){
            throw new IllegalArgumentException("person object cannot be null");
        }
        if(person.getPersonId()<1){
            throw new IllegalArgumentException("invalid person object");
        }
        return person.getName();
    }

    FileIdentifier addMediaFile(String fileLocation) {
        if(fileLocation==null){
            throw new IllegalArgumentException("file location cannot be null");
        }
        if(fileLocation.trim().isEmpty()){
            throw new IllegalArgumentException("file location cannot be an empty string");
        }
        String CHECK_EXISTING_FILE = "SELECT * FROM media_details WHERE file_location=?";
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
            conn = DBConnection.getConnection();
            pStmt=conn.prepareStatement(CHECK_EXISTING_FILE);

            pStmt.setString(1,fileLocation);

            resultSet = pStmt.executeQuery();

            while(resultSet.next()){
                existingMediaId=resultSet.getInt("media_id");
            }

            if(existingMediaId!=0){
                throw new IllegalArgumentException("Media file already exists");
            }

            pStmt.close();
            pStmt = conn.prepareStatement(SQL, new String[] {"media_id"});
            pStmt.setString(1,fileLocation);
            result = pStmt.executeUpdate();
            generatedKeys = pStmt.getGeneratedKeys();
            if (result==0 || !generatedKeys.isBeforeFirst()){
                throw new SQLException();
            } else {
                int insertedId = 0;
                while (generatedKeys.next()){
                    insertedId = generatedKeys.getInt(1);
                }
                if(insertedId==0){
                    throw new SQLException();
                }
                fileIdentifier = new FileIdentifier(insertedId, fileLocation);
            }
        } catch(SQLException e){
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
        }
        if(exceptionOccurred){
            return null;
        }
        return fileIdentifier;
    }

    FileIdentifier findMediaFile(String fileLocation){
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
        String SQL = "SELECT * FROM media_details WHERE file_location=?";;
        try {
            conn = DBConnection.getConnection();
            pStmt = conn.prepareStatement(SQL);
            pStmt.setString(1, fileLocation);
            resultSet = pStmt.executeQuery();

            int media_id = 0;
            int rowCount=0;

            while (resultSet.next()){
                rowCount++;
                media_id = resultSet.getInt("media_id");
            }

            if(rowCount>1){
                throw new RuntimeException();
            }

            if(media_id != 0){
                fileIdentifier = new FileIdentifier(media_id, fileLocation);
            }

        } catch (SQLException e) {
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
            try {
                if(conn!=null){
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }
        if(exceptionOccurred){
            return null;
        }
        return fileIdentifier;
    }

    String findFileLocation(FileIdentifier file) {
        if(file==null){
            throw new IllegalArgumentException("file object cannot be null");
        }
        if(file.getMediaId()<1){
            throw new IllegalArgumentException("invalid file object");
        }
        return file.getFileLocation();
    }

    Boolean peopleInMedia(FileIdentifier fileIdentifier, List<PersonIdentity> people){
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
        SQL.append("AS new ON DUPLICATE KEY UPDATE person_id=new.person_id, media_id=new.media_id");

        Connection conn = null;
        PreparedStatement pStmt = null;
        int result = 0;
        boolean exceptionOccurred = false;

        try{
            conn = DBConnection.getConnection();

            int index=1;
            pStmt = conn.prepareStatement(SQL.toString());
            for(PersonIdentity person : people) {
                pStmt.setInt(index++, person.getPersonId());
                pStmt.setInt(index++, fileIdentifier.getMediaId());
            }
            result = pStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
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
        return result!=0;
    }

    Boolean tagMedia(FileIdentifier fileIdentifier, String tag) {
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
        String CHECK_EXISTING_TAG = "SELECT * FROM media_tags_types WHERE tag_name=?";
        String CHECK_EXISTING_RECORD = "SELECT * FROM media_tags WHERE media_id=? AND tag_id=?";
        String INSERT_NEW_TAG = "INSERT INTO media_tags_types(tag_name) VALUES(?)";
        String RECORD_TAG = "INSERT INTO media_tags (tag_id, media_id) VALUES(?, ?)";
        int result = 0;
        int existingRecord = 0;
        boolean exceptionOccurred = false;

        int existingTagId=0;

        try{
            conn = DBConnection.getConnection();
            pStmt = conn.prepareStatement(CHECK_EXISTING_TAG);
            pStmt.setString(1, tag);
            resultSet = pStmt.executeQuery();

            while (resultSet.next()){
                existingTagId=resultSet.getInt("tag_id");
            }

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
            } else {
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
            e.printStackTrace();
            exceptionOccurred = true;
        } finally {
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

        return true;
    }

    Boolean recordAttributes(PersonIdentity person, Map<String, String> attributes) {
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
        String GET_ATTRIBUTE_KEYS = "SELECT * FROM person_attributes_types";
        StringBuilder INSERT_ATTRIBUTES_SQL = new StringBuilder("INSERT INTO person_attributes VALUES ");
        for(int i=0;i<attributeCount;i++){
            if(i<(attributeCount-1)){
                INSERT_ATTRIBUTES_SQL.append("(?,?,?), ");
            } else {
                INSERT_ATTRIBUTES_SQL.append("(?,?,?) ");
            }
        }

        INSERT_ATTRIBUTES_SQL.append("AS new ON DUPLICATE KEY UPDATE attribute_value=new.attribute_value");

        try {
            conn = DBConnection.getConnection();
            pStmt = conn.prepareStatement(GET_ATTRIBUTE_KEYS);

            resultSet = pStmt.executeQuery();

            if(!resultSet.isBeforeFirst()){
                throw new SQLException("No attribute types are defined in the database");
            }

            while (resultSet.next()){
                attributeTypes.put(resultSet.getString("attribute_type"), resultSet.getInt("attribute_id"));
            }

            pStmt.close();
            pStmt = conn.prepareStatement(INSERT_ATTRIBUTES_SQL.toString());

            int index=1;
            for(Map.Entry<String,String> attribute: attributes.entrySet()){
                int attributeId = 0;
                if(!attributeTypes.containsKey(attribute.getKey())){
                    attributeId = addNewAttributeType(attribute.getKey(), "person_attributes_types", conn);
                } else {
                    if(attribute.getKey().contains("date")){
                        if(!attribute.getValue().matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")
                                && !attribute.getValue().matches("^\\d{4}-(0[1-9]|1[0-2])")
                                && !attribute.getValue().matches("^\\d{4}")){
                            throw new IllegalArgumentException("Invalid Date Format");
                        }
                    }
                    attributeId=attributeTypes.get(attribute.getKey());
                }
                if(attributeId==0){
                    throw new SQLException("Cannot find attribute type id");
                }
                pStmt.setInt(index++,person.getPersonId());
                pStmt.setInt(index++, attributeId);
                pStmt.setString(index++, attribute.getValue());
            }

            result = pStmt.executeUpdate();

            if(result==0){
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

        String INSERT_ATTRIBUTE_TYPE = "INSERT INTO "+tableName+" (attribute_type) VALUES(?)";

        try {
            pStmt = conn.prepareStatement(INSERT_ATTRIBUTE_TYPE,new String[]{"attribute_id"});

            pStmt.setString(1,attributeType);

            result=pStmt.executeUpdate();
            generatedKeys=pStmt.getGeneratedKeys();

            if(result==0 || generatedKeys==null){
                throw new Exception();
            }

            while(generatedKeys.next()){
                newKey = generatedKeys.getInt(1);
            }

            if(newKey==0){
                throw new Exception();
            }

        } catch(SQLException sqe){
            sqe.printStackTrace();
            exceptionOccurred = true;
        } catch (Exception e){
            exceptionOccurred=true;
        }finally {
            if(pStmt!=null){
                try {
                    pStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        if(exceptionOccurred){
            return 0;
        }
        return newKey;
    }

    Boolean recordMediaAttributes(FileIdentifier fileIdentifier, Map<String, String> attributes) {
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
        String GET_ATTRIBUTE_KEYS = "SELECT * FROM media_attributes_types";
        StringBuilder INSERT_ATTRIBUTES_SQL = new StringBuilder("INSERT INTO media_attributes VALUES ");
        for(int i=0;i<attributeCount;i++){
            if(i<(attributeCount-1)){
                INSERT_ATTRIBUTES_SQL.append("(?,?,?), ");
            } else {
                INSERT_ATTRIBUTES_SQL.append("(?,?,?) ");
            }
        }

        INSERT_ATTRIBUTES_SQL.append("AS new ON DUPLICATE KEY UPDATE attribute_value=new.attribute_value");

        try {
            conn = DBConnection.getConnection();
            pStmt = conn.prepareStatement(GET_ATTRIBUTE_KEYS);

            resultSet = pStmt.executeQuery();

            if(!resultSet.isBeforeFirst()){
                throw new SQLException("No attribute types are defined in the database");
            }

            while (resultSet.next()){
                attributeTypes.put(resultSet.getString("attribute_type"), resultSet.getInt("attribute_id"));
            }

            pStmt.close();
            pStmt = conn.prepareStatement(INSERT_ATTRIBUTES_SQL.toString());

            int index=1;
            for(Map.Entry<String,String> attribute: attributes.entrySet()){
                int attributeId = 0;
                if(!attributeTypes.containsKey(attribute.getKey())){
                    attributeId = addNewAttributeType(attribute.getKey(), "media_attributes_types", conn);
                } else {
                    if(attribute.getKey().contains("date")){
                        if(!attribute.getValue().matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")
                        && !attribute.getValue().matches("^\\d{4}-(0[1-9]|1[0-2])")
                        && !attribute.getValue().matches("^\\d{4}")){
                            throw new IllegalArgumentException("Invalid Date format");
                        }
                    }
                    attributeId=attributeTypes.get(attribute.getKey());
                }
                if(attributeId==0){
                    throw new SQLException("Cannot find attribute type id");
                }
                pStmt.setInt(index++,fileIdentifier.getMediaId());
                pStmt.setInt(index++, attributeId);
                pStmt.setString(index++, attribute.getValue());
            }

            result = pStmt.executeUpdate();

            if(result==0){
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

    Boolean recordChild(PersonIdentity parent, PersonIdentity child) {
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

        String CHECK_EXISTING_RELATION = "SELECT * FROM parent_child WHERE parent_id=? AND child_id=?";
        String FIND_PARENTS = "SELECT COUNT(parent_id) as noOfParents FROM parent_child WHERE child_id=?";
        String INSERT_NEW_CHILD = "INSERT INTO parent_child VALUES(?,?)";

        try {
            conn = DBConnection.getConnection();
            pStmt=conn.prepareStatement(CHECK_EXISTING_RELATION);
            pStmt.setInt(1,parent.getPersonId());
            pStmt.setInt(2,child.getPersonId());
            resultSet = pStmt.executeQuery();

            while(resultSet.next()){
                existingParentId=resultSet.getInt("parent_id");
                existingChildId=resultSet.getInt("child_id");
            }

            if(!(existingParentId>0 && existingChildId>0)){
                pStmt.close();
                resultSet.close();
                pStmt = conn.prepareStatement(FIND_PARENTS);
                pStmt.setInt(1,child.getPersonId());
                resultSet = pStmt.executeQuery();

                while(resultSet.next()){
                    noOfParents = resultSet.getInt("noOfParents");
                }

                if(noOfParents>=2){
                    throw new IllegalArgumentException("2 parents already exist for child");
                }

                pStmt.close();
                pStmt = conn.prepareStatement(INSERT_NEW_CHILD);
                pStmt.setInt(1,parent.getPersonId());
                pStmt.setInt(2,child.getPersonId());

                result = pStmt.executeUpdate();

                if(result==0){
                    throw new SQLException();
                }
            }

        } catch(SQLException e){
            exceptionOccurred = true;
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

    Set<PersonIdentity> descendents(PersonIdentity person, Integer generations){
        if(person==null){
            throw new IllegalArgumentException("person object cannot be null");
        }

        if(person.getPersonId()<1){
            throw new IllegalArgumentException("invalid person object");
        }

        if(generations<0){
            throw new IllegalArgumentException("generations cannot be a negative integer");
        }
        Set<PersonIdentity> descendants = new LinkedHashSet<>();
        if(generations==0){
            return descendants;
        }

        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet resultSet = null;
        boolean exceptionOccurred = false;

        int parentId = person.getPersonId();

        String FIND_DESCENDANTS_RECURSIVE = "with recursive descendants (child_id, gen) as "+
                "(select child_id, 1 from parent_child where parent_id=? " +
                "union all " +
                "select pc.child_id, d.gen+1 from descendants d " +
                "inner join parent_child pc on pc.parent_id=d.child_id where d.gen<?) " +
                "select pd.person_id, pd.name from descendants d inner join person_details pd on d.child_id=pd.person_id";

        try {
            conn = DBConnection.getConnection();
            pStmt = conn.prepareStatement(FIND_DESCENDANTS_RECURSIVE);

            pStmt.setInt(1, parentId);
            pStmt.setInt(2, generations);

            resultSet = pStmt.executeQuery();


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

        return descendants;
    }

    Set<PersonIdentity> ancestors(PersonIdentity person, Integer generations){
        if(person==null){
            throw new IllegalArgumentException("person object cannot be null");
        }

        if(person.getPersonId()<1){
            throw new IllegalArgumentException("invalid person object");
        }

        if(generations<0){
            throw new IllegalArgumentException("generations cannot be a negative integer");
        }

        Set<PersonIdentity> ancestors = new LinkedHashSet<>();
        if(generations==0){
            return ancestors;
        }

        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet resultSet = null;
        boolean exceptionOccurred = false;

        int childId = person.getPersonId();

        String FIND_ANCESTORS_RECURSIVE = "with recursive ancestors (parent_id, gen) as " +
                "(select parent_id, 1 from parent_child where child_id=? " +
                "union all " +
                "select pc.parent_id, a.gen+1 from parent_child pc " +
                "inner join ancestors a on pc.child_id=a.parent_id where a.gen<?) " +
                "select pd.person_id, pd.name from ancestors a inner join person_details pd on a.parent_id=pd.person_id";

        try {
            conn = DBConnection.getConnection();
            pStmt = conn.prepareStatement(FIND_ANCESTORS_RECURSIVE);

            pStmt.setInt(1, childId);
            pStmt.setInt(2, generations);

            resultSet = pStmt.executeQuery();


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

        return ancestors;
    }

    BiologicalRelation findRelation(PersonIdentity person1, PersonIdentity person2) {
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
            conn = DBConnection.getConnection();
            pStmt=conn.prepareStatement(FIND_LCA);

            pStmt.setInt(1,person1.getPersonId());
            pStmt.setInt(2,person1.getPersonId());
            pStmt.setInt(3,person2.getPersonId());
            pStmt.setInt(4,person2.getPersonId());

            resultSet = pStmt.executeQuery();

            while(resultSet.next()){
                LCA=resultSet.getInt("LCA");
                depth1=resultSet.getInt("depth1");
                depth2=resultSet.getInt("depth2");
            }

            if(LCA==-1 || depth1==-1 || depth2==-1){
                throw new SQLException();
            }

            int cousinship = Math.min(depth1, depth2)-1;

            biologicalRelation = new BiologicalRelation();
            biologicalRelation.setCousinship(cousinship);
            biologicalRelation.setRemoval(Math.abs(depth1-depth2));

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

        return biologicalRelation;
    }

    Boolean recordPartnering(PersonIdentity partner1, PersonIdentity partner2){
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

        String FIND_EXISTING_EVENT_TYPE = "SELECT event_type_id FROM person_events WHERE (person_id_1=? AND person_id_2=?) OR (person_id_2=? AND person_id_1=?) ORDER BY event_id DESC LIMIT 1";
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

    Boolean recordDissolution(PersonIdentity partner1, PersonIdentity partner2){
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

        String FIND_EXISTING_EVENT_TYPE = "SELECT event_type_id FROM person_events WHERE (person_id_1=? AND person_id_2=?) OR (person_id_2=? AND person_id_1=?) ORDER BY event_id DESC LIMIT 1";
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

    List<String> notesAndReferences(PersonIdentity person){
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

    Set<FileIdentifier> findMediaByTag(String tag, String startDate, String endDate){
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

        String CHECK_EXISTING_TAG = "SELECT * FROM media_tags_types WHERE tag_name=?";
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

        if(startDate==null && endDate==null){
            GET_MEDIA_BY_TAG = new StringBuilder("select md.media_id, md.file_location, mtt.tag_name " +
                    "from media_details md " +
                    "inner join media_tags mt on md.media_id=mt.media_id " +
                    "inner join media_tags_types mtt on mt.tag_id=mtt.tag_id " +
                    "where mtt.tag_id=?");
            dateCondition=NO_DATES_PROVIDED;
        } else if(startDate!=null && endDate!=null){
            GET_MEDIA_BY_TAG.append("between str_to_date(?,'%Y-%m-%d') and str_to_date(?,'%Y-%m-%d'))  " +
                    "select * from table2 " +
                    "order by date_created ASC, file_location ASC");
            dateCondition=BOTH_DATES_PROVIDED;
        } else if(endDate==null){
            GET_MEDIA_BY_TAG.append(">= str_to_date(?,'%Y-%m-%d'))  " +
                    "select * from table2 " +
                    "order by date_created ASC, file_location ASC");
            dateCondition=START_DATE_PROVIDED;
        } else{
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
                    while(resultSet.next()){
                        int mediaId=resultSet.getInt("media_id");
                        String fileLocation=resultSet.getString("file_location");
                        FileIdentifier fileIdentifier = new FileIdentifier(mediaId,fileLocation);

                        fileIdentifierSet.add(fileIdentifier);
                    }
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

        return fileIdentifierSet;
    }

    Set<FileIdentifier> findMediaByLocation(String location, String startDate, String endDate){
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

        if(startDate==null && endDate==null){
            GET_MEDIA_BY_LOCATION = new StringBuilder("select md.media_id, md.file_location, mat.attribute_type, ma.attribute_value " +
                    "from media_details md " +
                    "inner join media_attributes ma on md.media_id=ma.media_id " +
                    "inner join media_attributes_types mat on ma.attribute_id=mat.attribute_id " +
                    "where mat.attribute_type='location' and ma.attribute_value LIKE ? ");
            dateCondition=NO_DATES_PROVIDED;
        } else if(startDate!=null && endDate!=null){
            GET_MEDIA_BY_LOCATION.append(" between str_to_date(?,'%Y-%m-%d') and str_to_date(?,'%Y-%m-%d')) " +
            "select * from table3 ");
            dateCondition=BOTH_DATES_PROVIDED;
        } else if(endDate==null){
            GET_MEDIA_BY_LOCATION.append(" >= str_to_date(?,'%Y-%m-%d')) " +
                    "select * from table3 t3");
            dateCondition=START_DATE_PROVIDED;
        } else{
            GET_MEDIA_BY_LOCATION.append(" <= str_to_date(?,'%Y-%m-%d')) " +
                    "select * from table3 t3");
            dateCondition=END_DATE_PROVIDED;
        }

        Set<FileIdentifier> fileIdentifierSet=new LinkedHashSet<>();

        try {
            conn = DBConnection.getConnection();
            pStmt = conn.prepareStatement(GET_MEDIA_BY_LOCATION.toString());

            pStmt.setString(1,"%"+location+"%");

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

            while(resultSet.next()){
                int mediaId=resultSet.getInt("media_id");
                String fileLocation=resultSet.getString("file_location");
                FileIdentifier fileIdentifier = new FileIdentifier(mediaId,fileLocation);

                fileIdentifierSet.add(fileIdentifier);
            }

        } catch (SQLException sqe){
            sqe.printStackTrace();
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

    List<FileIdentifier> findBiologicalFamilyMedia(PersonIdentity person){
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

    List<FileIdentifier> findIndividualsMedia(Set<PersonIdentity> people, String startDate, String
            endDate) {

        if(people==null){
            throw new IllegalArgumentException("set of people cannot be null");
        }

        List<FileIdentifier> fileIdentifierList=new ArrayList<>();
        if(people.size()==0){
            return fileIdentifierList;
        }

        if(startDate!=null && startDate.trim().isEmpty()){
            throw new IllegalArgumentException("start date cannot be an empty string");
        }
        if(endDate!=null && endDate.trim().isEmpty()){
            throw new IllegalArgumentException("end date cannot be an empty string");
        }
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

        if(startDate==null && endDate==null){
            GET_INDIVIDUALS_MEDIA.append("");
            dateCondition=NO_DATES_PROVIDED;
        } else if(startDate!=null && endDate!=null){
            GET_INDIVIDUALS_MEDIA.append(" and date_created between str_to_date(?,'%Y-%m-%d') and str_to_date(?,'%Y-%m-%d') ");
            dateCondition=BOTH_DATES_PROVIDED;
        } else if(endDate==null){
            GET_INDIVIDUALS_MEDIA.append(" and date_created >= str_to_date(?,'%Y-%m-%d') ");
            dateCondition=START_DATE_PROVIDED;
        } else{
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
            sqe.printStackTrace();
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

}
