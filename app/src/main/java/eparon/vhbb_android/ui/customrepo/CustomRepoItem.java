package eparon.vhbb_android.ui.customrepo;

public class CustomRepoItem {

    private String Name;
    private String Filename, DataFilename;
    private String Version;
    private String Author;
    private String Description;
    private String Url, DataUrl;
    private String Date;

    public CustomRepoItem (String name, String filename, String dataFilename, String version, String author, String desc, String url, String dataUrl, String date) {
        this.Name = name;
        this.Filename = filename;
        this.DataFilename = dataFilename;
        this.Version = version;
        this.Author = author;
        this.Description = desc;
        this.Url = url;
        this.DataUrl = dataUrl;
        this.Date = date;
    }

    public String getName () {
        return Name;
    }

    public String getFilename () {
        return Filename;
    }

    public String getDataFilename () {
        return DataFilename;
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

    public String getDataUrl () {
        return DataUrl;
    }

    public String getDate () {
        return Date;
    }

}
