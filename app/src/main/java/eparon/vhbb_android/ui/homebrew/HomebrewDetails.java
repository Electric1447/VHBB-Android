package eparon.vhbb_android.ui.homebrew;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import eparon.vhbb_android.R;
import eparon.vhbb_android.Utils.DownloadUtils;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class HomebrewDetails extends AppCompatActivity {

    HomebrewItem cItem;

    String Name, IconUrl, Version, Author, LongDescription, SourceUrl, ReleaseUrl, Date, Url, DataUrl;
    String[] ScreenshotsUrl;

    TextView mTitle, mAuthor, mDate, mDescription;
    ImageView mIcon, mScreenshot;
    Button mSourceBtn, mReleaseBtn;
    ImageButton mDownload, mDownloadData;

    Handler cycleHandler = new Handler();
    Runnable cycleRunnable = new Runnable() {
        @Override
        public void run () {
            cycleScreenshot();
            cycleHandler.postDelayed(this, 5000);
        }
    };
    int sc_index = 0;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_homebrew);

        cItem = (HomebrewItem)getIntent().getSerializableExtra("ITEM");
        assert cItem != null;

        Name = cItem.getName();
        IconUrl = cItem.getIconUrl();
        Version = cItem.getVersion();
        Author = cItem.getAuthor();
        LongDescription = cItem.getLongDescription();
        SourceUrl = cItem.getSourceUrl();
        ReleaseUrl = cItem.getReleaseUrl();
        Date = cItem.getDateString();
        Url = cItem.getUrl();
        DataUrl = cItem.getDataUrl();
        ScreenshotsUrl = cItem.getScreenshotsUrl();

        mTitle = findViewById(R.id.textview_title);
        mDate = findViewById(R.id.textview_date);
        mAuthor = findViewById(R.id.textview_author);
        mDescription = findViewById(R.id.textview_desc);
        mIcon = findViewById(R.id.image);
        mScreenshot = findViewById(R.id.screenshot);
        mSourceBtn = findViewById(R.id.button_source);
        mReleaseBtn = findViewById(R.id.button_release);
        mDownload = findViewById(R.id.download);
        mDownloadData = findViewById(R.id.downloadData);

        mTitle.setText(String.format("%s %s", Name, Version));
        mDate.setText(Date);
        mAuthor.setText(Author);
        mDescription.setText(LongDescription);
        Picasso.get().load(IconUrl).fit().centerInside().transform(new CropCircleTransformation()).memoryPolicy(MemoryPolicy.NO_CACHE).into(mIcon);

        findViewById(R.id.ll_pages).setVisibility(!(SourceUrl.equals("") && ReleaseUrl.equals("")) ? View.VISIBLE : View.GONE);
        findViewById(R.id.line3).setVisibility(!(SourceUrl.equals("") && ReleaseUrl.equals("")) ? View.VISIBLE : View.GONE);
        mSourceBtn.setVisibility(!SourceUrl.equals("") ? View.VISIBLE : View.GONE);
        mReleaseBtn.setVisibility(!ReleaseUrl.equals("") ? View.VISIBLE : View.GONE);

        mSourceBtn.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(SourceUrl))));
        mReleaseBtn.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ReleaseUrl))));

        mDownload.setOnClickListener(v -> {
            String filename = Name + ".vpk";
            DownloadUtils.VHBBDownloadManager(this, Uri.parse(Url), filename);
        });

        mDownloadData.setVisibility(!DataUrl.equals("") ? View.VISIBLE : View.GONE);
        if (!DataUrl.equals("")) mDownloadData.setOnClickListener(v -> {
            String filename = DataUrl.substring(DataUrl.lastIndexOf("/") + 1);
            DownloadUtils.VHBBDownloadManager(this, Uri.parse(Url), filename);
        });

        if (ScreenshotsUrl != null)
            if (ScreenshotsUrl.length == 1) Picasso.get().load(ScreenshotsUrl[0]).fit().centerInside().memoryPolicy(MemoryPolicy.NO_CACHE).into(mScreenshot);
            else cycleHandler.postDelayed(cycleRunnable, 0);
        else mScreenshot.setVisibility(View.GONE);
    }

    private void cycleScreenshot () {
        if (sc_index >= ScreenshotsUrl.length)
            sc_index = 0;

        Picasso.get().load(ScreenshotsUrl[sc_index]).fit().centerInside().memoryPolicy(MemoryPolicy.NO_CACHE).into(mScreenshot);
        sc_index++;
    }

}
