package eparon.vhbb_android.ui.cbpsdb;

import eparon.vhbb_android.Constants.CBPSDB;

public class CBPSDBItem {

    private final String ID;
    private final String Name;
    private final String Author;
    private final String Icon0;
    private final String Url;
    private final String Options;
    private final String Type;
    private String DataUrl = "None";

    public CBPSDBItem (String id, String name, String author, String icon0, String url, String options, String type) {
        this.ID = id;
        this.Name = name;
        this.Author = author;
        this.Icon0 = icon0;
        this.Url = url;
        this.Type = type;

        if (options.equals("None"))
            this.Options = CBPSDB.OPTIONS_NONE;
        else if (options.contains("*ALL"))
            this.Options = CBPSDB.OPTIONS_ALL;
        else if (options.contains("*main"))
            this.Options = CBPSDB.OPTIONS_MAIN;
        else if (options.contains("*KERNEL"))
            this.Options = CBPSDB.OPTIONS_KERNEL;
        else
            this.Options = CBPSDB.OPTIONS_UNKNOWN;
    }

    public String getID () {
        return ID;
    }

    public String getName () {
        return Name;
    }

    public String getAuthor () {
        return Author;
    }

    public String getIcon0() {
        return Icon0;
    }

    public String getUrl () {
        return Url;
    }

    public String getOptions () {
        return Options;
    }

    public String getType () {
        return Type;
    }

    public String getTypeString () {
        switch (this.Type) {
            case CBPSDB.TYPE_VPK:
                return "Homebrew";
            case CBPSDB.TYPE_PLUGIN:
                return "Plugin";
            case CBPSDB.TYPE_DATA:
                return "Data";
            default:
                return "Unknown";
        }
    }

    public String getDataUrl () {
        return DataUrl;
    }

    public void setDataUrl (String dataUrl) {
        DataUrl = dataUrl;
    }

}
