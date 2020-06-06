package eparon.vhbb_android;

/**
 * This class is the base for the different items in the app.
 */
public class BaseItem {

    private String Name;            // Item's name
    private String Filename;        // Item's filename with extension
    private String Version;         // Item's version string
    private String Author;          // Item's author
    private String Description;     // Short description about the item
    private String Url;             // Item's file direct download link

    public BaseItem (String name, String filename, String version, String author, String desc, String url) {
        this.Name = name;
        this.Filename = filename;
        this.Version = version;
        this.Author = author;
        this.Description = desc;
        this.Url = url;
    }

    public String getName () {
        return Name;
    }

    public String getFilename () {
        return Filename;
    }

    public String getVersion () {
        return Version;
    }

    public String getAuthor () {
        return Author;
    }

    public String getDescription() {
        return Description;
    }

    public String getUrl () {
        return Url;
    }

}
