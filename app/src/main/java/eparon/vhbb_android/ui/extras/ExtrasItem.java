package eparon.vhbb_android.ui.extras;

import eparon.vhbb_android.BaseItem;

/**
 * Extras item
 */
public class ExtrasItem extends BaseItem {

    private final String DataFilename;
    private final String DataUrl;
    private final String Date;
    private final String Icon;

    public ExtrasItem (String name, String filename, String version, String author, String desc, String url, String dataFilename, String dataUrl, String date, String icon) {
        super(name, filename, version, author, desc, url);
        this.DataFilename = dataFilename;
        this.DataUrl = dataUrl;
        this.Date = date;
        this.Icon = icon;
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

    public String getIcon () {
        return Icon;
    }

}
