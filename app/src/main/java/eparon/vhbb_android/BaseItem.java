package eparon.vhbb_android;

import java.io.Serializable;

/**
 * This class is the base for the different items in the app.
 */
public class BaseItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String Name;            // Item's name
    private final String Filename;        // Item's filename with extension
    private final String Version;         // Item's version string
    private final String Author;          // Item's author
    private final String Description;     // Short description about the item
    private final String Url;             // Item's file direct download link

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
