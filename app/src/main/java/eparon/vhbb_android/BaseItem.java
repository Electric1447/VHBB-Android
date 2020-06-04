package eparon.vhbb_android;

public class BaseItem {

    private String Name;
    private String Filename;
    private String Version;
    private String Author;
    private String Description;
    private String Url;

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
