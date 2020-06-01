package eparon.vhbb_android.ui.homebrew;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import eparon.vhbb_android.Constants.VitaDB;

public class HomebrewItem {

    private String Name;
    private String IconUrl;
    private String Version;
    private String Author;
    private String Description, LongDescription;
    private String SourceUrl, ReleaseUrl;
    private String Url, DataUrl;
    private int ID;
    private int Downloads;
    private long Size, DataSize;
    private Date Date;

    public HomebrewItem (String name, String iconUrl, String version, String author, String desc, String longDesc, String date, String srcUrl, String relUrl, String url, String dataUrl, int id, int downloads, long size, long dataSize) {
        this.Name = name;
        this.IconUrl = String.format("%s%s", VitaDB.ICONS_PARENT_URL, iconUrl);
        this.Version = version;
        this.Author = author;
        this.Description = desc;
        this.LongDescription = longDesc;
        this.SourceUrl = srcUrl;
        this.ReleaseUrl = relUrl;
        this.Url = url;
        this.DataUrl = dataUrl;
        this.ID = id;
        this.Downloads = downloads;
        this.Size = size;
        this.DataSize = dataSize;
        this.setDate(date);
    }

    public String getName () {
        return Name;
    }

    public String getIconUrl () {
        return IconUrl;
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

    public String getDataUrl () {
        return DataUrl;
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

    public long getDataSize () {
        return DataSize;
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
