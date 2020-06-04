package eparon.vhbb_android.ui.customrepo;

import eparon.vhbb_android.BaseItem;

public class CustomRepoItem extends BaseItem {

    private String DataFilename;
    private String DataUrl;
    private String Date;

    public CustomRepoItem (String name, String filename, String dataFilename, String version, String author, String desc, String url, String dataUrl, String date) {
        super(name, filename, version, author, desc, url);
        this.DataFilename = dataFilename;
        this.DataUrl = dataUrl;
        this.Date = date;
    }

    public String getDataFilename () {
        return DataFilename;
    }

    public String getDataUrl () {
        return DataUrl;
    }

    public String getDate () {
        return Date;
    }

}
