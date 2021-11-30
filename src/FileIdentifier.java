import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class FileIdentifier {
    private int mediaId;
    private String fileLocation;
    private Date dateCreated;
    private String location;
    private Map<String,String> attributes = new HashMap<>();

    public FileIdentifier(int mediaId, String fileLocation) {
        this.mediaId = mediaId;
        this.fileLocation = fileLocation;
    }

    public int getMediaId() {
        return mediaId;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public String getLocation() {
        return location;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "FileIdentifier{" +
                "mediaId=" + mediaId +
                ", fileLocation='" + fileLocation + '\'' +
                ", dateCreated=" + dateCreated +
                ", location='" + location + '\'' +
                '}';
    }
}
