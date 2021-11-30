import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Genealogy {

    public PersonIdentity addPerson(String name){
        if(name==null){
            return null;
        }
        if(name.trim().isEmpty()){
           return null;
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
            if (result==0 || generatedKeys==null){
                exceptionOccurred = true;
            } else {
                int insertedId = 0;
                while (generatedKeys.next()){
                    insertedId = generatedKeys.getInt(1);
                }
                person = (insertedId != 0) ? new PersonIdentity(insertedId, name) : null;
            }
        } catch(SQLException e){
            e.printStackTrace();
            exceptionOccurred = true;
        } finally {
            if(pStmt!=null){
                try {
                    pStmt.close();
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
        return person;
    }

    public Boolean recordReference(PersonIdentity person, String reference){
        if(person==null){
            return false;
        }
        if(person.getPersonId()==0){
            return false;
        }
        if(reference==null){
            return false;
        }
        if(reference.trim().isEmpty()){
            return false;
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
            e.printStackTrace();
            exceptionOccurred = true;
        } finally {
            if (pStmt != null) {
                try {
                    pStmt.close();
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
            return false;
        }

        return result!=0;
    }

    public Boolean recordNote(PersonIdentity person, String note){
        if(person==null){
            return false;
        }
        if(person.getPersonId()==0){
            return false;
        }
        if(note==null){
            return false;
        }
        if(note.trim().isEmpty()){
            return false;
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
            e.printStackTrace();
            exceptionOccurred = true;
        } finally {
            if (pStmt != null) {
                try {
                    pStmt.close();
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
            return false;
        }

        return result!=0;
    }

    public PersonIdentity findPerson(String name){
        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet resultSet = null;
        PersonIdentity person = null;
        String SQL = "SELECT * FROM person_details WHERE name=? LIMIT 1";
        String SELECT_ATTRIBUTE_SQL = "select t3.attribute_id, t3.attribute_type, " +
                "t2.attribute_value from person_details t1 " +
                "inner join person_attributes t2 on t1.person_id=t2.person_id " +
                "inner join person_attributes_types t3 on t2.attribute_id=t3.attribute_id " +
                "where t1.person_id=?";
        boolean exceptionOccurred = false;

        try {
                conn = DBConnection.getConnection();
                pStmt = conn.prepareStatement(SQL);
                pStmt.setString(1, name);
                resultSet = pStmt.executeQuery();
                int personId = 0, genderId=0;
                Map<String, String> attributes = new HashMap<>();

                while (resultSet.next()){
                    personId = resultSet.getInt("person_id");
                    genderId = resultSet.getInt("gender_id");
                }

                if(personId == 0){
                    throw new SQLException();
                }

                person = new PersonIdentity(personId, name);

                person.setGenderId(genderId);

                pStmt = conn.prepareStatement(SELECT_ATTRIBUTE_SQL);
                pStmt.setInt(1, personId);

                resultSet = pStmt.executeQuery();

                while(resultSet.next()){
                    attributes.put(resultSet.getString("attribute_type"), resultSet.getString("attribute_value"));
                }

                if(attributes.size()>0){
                    person.setAttributes(attributes);
                }

            } catch (SQLException e) {
                e.printStackTrace();
                exceptionOccurred = true;
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
                try {
                    if(conn!=null){
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        if(exceptionOccurred){
            return null;
        }
        return person;
    }

    String findName(PersonIdentity person) {
        if(person == null){
            return null;
        }
        return person.getName();
    }

    FileIdentifier addMediaFile(String fileLocation) {
        if(fileLocation==null){
            return null;
        }
        if(fileLocation.trim().isEmpty()){
            return null;
        }
        String SQL = "INSERT INTO media_details(file_location) VALUES(?)";
        Connection conn = null;
        PreparedStatement pStmt = null;
        FileIdentifier file = null;
        ResultSet generatedKeys = null;
        int result = 0;
        boolean exceptionOccurred = false;

        try {
            conn = DBConnection.getConnection();
            pStmt = conn.prepareStatement(SQL, new String[] {"media_id"});
            pStmt.setString(1,fileLocation);
            result = pStmt.executeUpdate();
            generatedKeys = pStmt.getGeneratedKeys();
            if (result==0 || generatedKeys==null){
                exceptionOccurred = true;
            } else {
                int insertedId = 0;
                while (generatedKeys.next()){
                    insertedId = generatedKeys.getInt(1);
                }
                file = (insertedId != 0) ? new FileIdentifier(insertedId, fileLocation) : null;
            }
        } catch(SQLException e){
            e.printStackTrace();
            exceptionOccurred = true;
        } finally {
            if(pStmt!=null){
                try {
                    pStmt.close();
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
        return file;
    }

    FileIdentifier findMediaFile(String fileLocation){
        Connection conn = null;
        PreparedStatement pStmt = null;
        ResultSet resultSet = null;
        FileIdentifier file = null;
        String SQL = "SELECT * FROM media_details WHERE file_location=? LIMIT 1";;
        try {
            conn = DBConnection.getConnection();
            pStmt = conn.prepareStatement(SQL);
            pStmt.setString(1, fileLocation);
            resultSet = pStmt.executeQuery();

            int media_id = 0;
            String location = null;
            Date dateCreated = null;

            while (resultSet.next()){
                media_id = resultSet.getInt("media_id");
                location = resultSet.getString("location");
                dateCreated = resultSet.getDate("date_created");
            }

            if(media_id == 0){
                return null;
            }

            file = new FileIdentifier(media_id, fileLocation);

            if(dateCreated!=null){
                file.setDateCreated(dateCreated);
            }
            if(location!=null){
                file.setLocation(location);
            }

        } catch (SQLException e) {
            e.printStackTrace();
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
            try {
                if(conn!=null){
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    String findFileLocation(FileIdentifier file) {
        if(file==null){
            return null;
        }
        return file.getFileLocation();
    }

    Boolean peopleInMedia(FileIdentifier fileIdentifier, List<PersonIdentity> people){
        if(fileIdentifier==null){
            return false;
        }
        if(fileIdentifier.getMediaId()<1){
            return false;
        }
        if(people==null){
            return false;
        }
        if(people.isEmpty()){
            return true;
        }
        for(PersonIdentity person : people){
            if(person.getPersonId()<1){
                return false;
            }
        }

        String SQL = "INSERT INTO person_media(person_id, media_id) VALUES(?,?)";
        Connection conn = null;
        PreparedStatement pStmt = null;
        int result = 0;
        boolean exceptionOccurred = false;

        try{
            conn = DBConnection.getConnection();

            for(PersonIdentity person : people) {
                pStmt = conn.prepareStatement(SQL);
                pStmt.setInt(1, person.getPersonId());
                pStmt.setInt(2, fileIdentifier.getMediaId());
                result += pStmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            exceptionOccurred = true;
        } finally {
            if(pStmt!=null){
                try {
                    pStmt.close();
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
            return false;
        }
        return result!=0;
    }

    Boolean tagMedia(FileIdentifier fileIdentifier, String tag) {
        if(fileIdentifier==null){
            return false;
        }
        if(fileIdentifier.getMediaId()<1){
            return false;
        }
        if(tag==null){
            return false;
        }
        if(tag.trim().isEmpty()){
            return false;
        }
        Connection conn = null;
        PreparedStatement pStmt = null;
        String SQL = "INSERT INTO media_tags (tag_name, media_id) VALUES(?, ?)";
        int result = 0;
        int index = 1;
        boolean exceptionOccurred = false;

        try{
            conn = DBConnection.getConnection();
            pStmt = conn.prepareStatement(SQL);
            pStmt.setString(index++, tag);
            pStmt.setInt(index, fileIdentifier.getMediaId());
            result = pStmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            exceptionOccurred = true;
        } finally {
            if (pStmt != null) {
                try {
                    pStmt.close();
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
            return false;
        }

        return result!=0;
    }

    Boolean recordChild(PersonIdentity parent, PersonIdentity child){
        if(parent==null || child==null){
            return false;
        }
        if(parent.getPersonId()<1 || child.getPersonId()<1){
            return false;
        }
        String SELECT_PARENT_GENDER = "SELECT gender_id FROM person_details WHERE person_id=?";
        String INSERT_EVENT_SQL = "INSERT INTO events(event_type_id) VALUES(?)";
        String INSERT_RELATION_SQL = "INSERT INTO person_event_relations(person_id,event_id,relation_id) VALUES(?,?,?)";
        Connection conn = null;
        PreparedStatement pStmt = null;
        PersonIdentity person = null;
        ResultSet resultSet = null;
        int result = 0;
        boolean exceptionOccurred = false;
        int eventId=0;
        int genderCode = 0;
        int parentType = 0;

        try {
            conn = DBConnection.getConnection();
            pStmt = conn.prepareStatement(SELECT_PARENT_GENDER);
            pStmt.setInt(1,parent.getPersonId());
            resultSet = pStmt.executeQuery();

            while(resultSet.next()){
                genderCode = resultSet.getInt("gender_id");
            }

            if(genderCode == 1) { // MALE
                parentType = 1; // FATHER
            } else if (genderCode == 2){ // FEMALE
                parentType = 2; // MOTHER
            } else if (genderCode==0) {
                parentType = 6; // GENERAL PARENT
            } else {
                throw new SQLException();
            }

            pStmt = conn.prepareStatement(INSERT_EVENT_SQL, new String[]{"event_id"});
            pStmt.setInt(1,1);
            result = pStmt.executeUpdate();
            resultSet = pStmt.getGeneratedKeys();

            if(result==0 || resultSet==null){
                throw new SQLException();
            }

            while(resultSet.next()){
                eventId = resultSet.getInt(1);
            }

            if(eventId==0){
                throw new SQLException();
            }

            // ADD CHILD RELATION
            pStmt = conn.prepareStatement(INSERT_RELATION_SQL);
            pStmt.setInt(1, child.getPersonId());
            pStmt.setInt(2, eventId);
            pStmt.setInt(3, 3); // 3 for CHILD in relation_types table
            result = pStmt.executeUpdate();

            if(result<1){
                throw new SQLException();
            }

            // ADD PARENT RELATION
            pStmt = conn.prepareStatement(INSERT_RELATION_SQL);
            pStmt.setInt(1, parent.getPersonId());
            pStmt.setInt(2, eventId);
            pStmt.setInt(3, parentType);
            result = pStmt.executeUpdate();

            if(result<1){
                throw new SQLException();
            }

        } catch(SQLException e){
            e.printStackTrace();
            exceptionOccurred = true;
        } finally {
            if(pStmt!=null){
                try {
                    pStmt.close();
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
            return false;
        }
        return result!=0;
    }

}
