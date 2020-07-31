package eparon.vhbb_android.ui.homebrew;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import eparon.vhbb_android.BaseItem;
import eparon.vhbb_android.Constants.VitaDB;

/**
 * Homebrew item
 */
public class HomebrewItem extends BaseItem {

    private String IconUrl;
    private String LongDescription;
    private String SourceUrl, ReleaseUrl;
    private String DataUrl;
    private int Type;
    private int ID;
    private int Downloads;
    private long Size, DataSize;
    private Date Date;
    private String[] ScreenshotsUrl;

    public HomebrewItem (String name, String iconUrl, String version, String author, String desc, String longDesc, String date, String srcUrl, String relUrl, String url, String dataUrl, String screenshots, int type, int id, int downloads, long size, long dataSize) {
        super(name, "", version, author, desc, url);

        this.IconUrl = String.format("%s%s", VitaDB.ICONS_PARENT_URL, iconUrl);
        this.LongDescription = longDesc;
        this.SourceUrl = srcUrl;
        this.ReleaseUrl = relUrl;
        this.DataUrl = dataUrl;
        this.Type = type;
        this.ID = id;
        this.Downloads = downloads;
        this.Size = size;
        this.DataSize = dataSize;
        this.setDate(date);

        if (!screenshots.equals("")) {
            String[] scArray = screenshots.split(";");
            for (int i = 0; i < scArray.length; i++)
                scArray[i] = String.format("%s/%s", VitaDB.PARENT_URL, scArray[i]);
            this.ScreenshotsUrl = scArray;
        }
    }

    public String getIconUrl () {
        return IconUrl;
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

    public String getDataUrl () {
        return DataUrl;
    }

    public int getType () {
        return Type;
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

    public String[] getScreenshotsUrl () {
        return ScreenshotsUrl;
    }

}
