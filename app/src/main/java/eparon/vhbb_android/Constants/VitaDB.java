package eparon.vhbb_android.Constants;

/**
 * Constants relating to VitaDB
 */
public class VitaDB {

    //region JSON
    public static final String JSON_NAME                = "name";
    public static final String JSON_ICON                = "icon";
    public static final String JSON_VERSION             = "version";
    public static final String JSON_AUTHOR              = "author";
    public static final String JSON_TYPE                = "type";
    public static final String JSON_DESCRIPTION         = "description";
    public static final String JSON_ID                  = "id";
    public static final String JSON_DATE                = "date";
    public static final String JSON_LONG_DESCRIPTION    = "long_description";
    public static final String JSON_DOWNLOADS           = "downloads";
    public static final String JSON_SOURCE              = "source";
    public static final String JSON_RELEASE_PAGE        = "release_page";
    public static final String JSON_SIZE                = "size";
    public static final String JSON_DATA_SIZE           = "data_size";
    public static final String JSON_URL                 = "url";
    public static final String JSON_DATA                = "data";
    //endregion


    //region URL
    public static final String PARENT_URL = "https://rinnegatamante.it/vitadb";
    public static final String HOMEBREW_LIST_JSON_URL = String.format("%s/list_hbs_json.php", PARENT_URL);
    public static final String PLUGIN_LIST_JSON_URL = String.format("%s/list_plugins_json.php", PARENT_URL);
    public static final String INFO_PARENT_URL = String.format("%s/#/info/", PARENT_URL);
    public static final String ICONS_PARENT_URL = String.format("%s/icons/", PARENT_URL);
    //endregion

}
