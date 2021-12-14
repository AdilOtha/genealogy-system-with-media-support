/**
 * Defines minimum data and methods that can uniquely identify a file in the database.
 */
public class FileIdentifier {
    // The primary key of the media file in the database
    private int mediaId;

    // the unique location of file
    private String fileLocation;

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

}
