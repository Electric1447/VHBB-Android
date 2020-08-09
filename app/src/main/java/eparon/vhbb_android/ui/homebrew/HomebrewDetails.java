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

    String[] ScreenshotsUrl;
    ImageView mScreenshot;

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

        String SourceUrl = cItem.getSourceUrl();
        String ReleaseUrl = cItem.getReleaseUrl();
        String DataUrl = cItem.getDataUrl();
        ScreenshotsUrl = cItem.getScreenshotsUrl();

        Button mSourceBtn = findViewById(R.id.button_source);
        Button mReleaseBtn = findViewById(R.id.button_release);
        ImageButton mDownload = findViewById(R.id.download);
        ImageButton mDownloadData = findViewById(R.id.downloadData);
        mScreenshot = findViewById(R.id.screenshot);

        ((TextView)findViewById(R.id.textview_title)).setText(String.format("%s %s", cItem.getName(), cItem.getVersion()));
        ((TextView)findViewById(R.id.textview_date)).setText(cItem.getDateString());
        ((TextView)findViewById(R.id.textview_author)).setText(cItem.getAuthor());
        ((TextView)findViewById(R.id.textview_desc)).setText(cItem.getLongDescription());

        Picasso.get().load(cItem.getIconUrl()).fit().centerInside().transform(new CropCircleTransformation()).memoryPolicy(MemoryPolicy.NO_CACHE).into((ImageView)findViewById(R.id.image));

        int pagesVisibility = !(SourceUrl.equals("") && ReleaseUrl.equals("")) ? View.VISIBLE : View.GONE;
        findViewById(R.id.ll_pages).setVisibility(pagesVisibility);
        findViewById(R.id.line3).setVisibility(pagesVisibility);
        mSourceBtn.setVisibility(!SourceUrl.equals("") ? View.VISIBLE : View.GONE);
        mReleaseBtn.setVisibility(!ReleaseUrl.equals("") ? View.VISIBLE : View.GONE);

        mSourceBtn.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(SourceUrl))));
        mReleaseBtn.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ReleaseUrl))));

        mDownload.setOnClickListener(v -> DownloadUtils.VHBBDownloadManager(this, this, Uri.parse(cItem.getUrl()), cItem.getName() + ".vpk"));

        mDownloadData.setVisibility(!DataUrl.equals("") ? View.VISIBLE : View.GONE);
        if (!DataUrl.equals("")) mDownloadData.setOnClickListener(v -> DownloadUtils.VHBBDownloadManager(this, this, Uri.parse(DataUrl), DataUrl.substring(DataUrl.lastIndexOf("/") + 1)));

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
