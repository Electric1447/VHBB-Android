package eparon.vhbb_android.ui.plugins;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PluginsItem {

    private String Name;
    private String Version;
    private String Author;
    private String Description, LongDescription;
    private String SourceUrl, ReleaseUrl;
    private String Url;
    private int ID;
    private int Downloads;
    private long Size;
    private Date Date;

    public PluginsItem (String name, String version, String author, String desc, String longDesc, String date, String srcUrl, String relUrl, String url, int id, int downloads, long size) {
        this.Name = name;
        this.Version = version;
        this.Author = author;
        this.Description = desc;
        this.LongDescription = longDesc;
        this.SourceUrl = srcUrl;
        this.ReleaseUrl = relUrl;
        this.Url = url;
        this.ID = id;
        this.Downloads = downloads;
        this.Size = size;
        this.setDate(date);
    }

    public String getName () {
        return Name;
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

    public String getLongDescription () {
        return LongDescription;
    }

    public String getSourceUrl () {
        return SourceUrl;
    }

    public String getReleaseUrl () {
        return ReleaseUrl;
    }

    public String getUrl () {
        return Url;
    }

    public int getID () {
        return ID;
    }

    public int getDownloads () {
        return Downloads;
    }

    public long getSize () {
        return Size;
    }

    public Date getDate () {
        return Date;
    }

    public String getDateString () {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.format(this.Date);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "1970-01-01";
    }

    private void setDate (String date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.Date = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
