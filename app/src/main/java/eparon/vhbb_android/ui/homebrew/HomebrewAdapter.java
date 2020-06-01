package eparon.vhbb_android.ui.homebrew;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import eparon.vhbb_android.MainActivity;
import eparon.vhbb_android.R;

public class HomebrewAdapter extends RecyclerView.Adapter<HomebrewAdapter.ViewHolder> {

    private ArrayList<HomebrewItem> mHomebrewList;

    public HomebrewAdapter (ArrayList<HomebrewItem> homebrewList) {
        mHomebrewList = homebrewList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_homebrew, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder (@NonNull ViewHolder holder, int position) {
        HomebrewItem currentItem = mHomebrewList.get(position);

        String nameID = currentItem.getName();
        String iconID = currentItem.getIconUrl();
        String versionID = currentItem.getVersion();
        String authorID = currentItem.getAuthor();
        String descriptionID = currentItem.getDescription();
        String dateID = currentItem.getDateString();
        int downloadsID = currentItem.getDownloads();
        String urlID = currentItem.getUrl();

        holder.mTitle.setText(String.format("%s %s", nameID, versionID));
        holder.mAuthor.setText(authorID);
        holder.mDescription.setText(descriptionID);
        holder.mDate.setText(String.format("(%s)", dateID));
        holder.mDownloads.setText(String.format(Locale.getDefault(), "%dDLs", downloadsID));
        Picasso.get().load(iconID).fit().centerInside().into(holder.mIcon);

        holder.mDownload.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(v.getContext(), "Storage permission not granted", Toast.LENGTH_LONG).show();
                return;
            }

            DownloadManager downloadmanager = (DownloadManager)v.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(urlID);

            String filename = nameID + ".vpk";

            DownloadManager.Request request = new DownloadManager.Request(uri)
                    .setTitle(v.getContext().getString(R.string.app_name))
                    .setDescription(String.format("Downloading %s", filename))
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                    .setVisibleInDownloadsUi(true)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);

            assert downloadmanager != null;
            downloadmanager.enqueue(request);
        });
    }

    @Override
    public int getItemCount () {
        return mHomebrewList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle, mAuthor, mDescription, mDate, mDownloads;
        public ImageButton mDownload;
        public ImageView mIcon;

        public ViewHolder (View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.textview_name);
            mAuthor = itemView.findViewById(R.id.textview_author);
            mDescription = itemView.findViewById(R.id.textview_desc);
            mDate = itemView.findViewById(R.id.textview_date);
            mDownloads = itemView.findViewById(R.id.textview_downloads);
            mDownload = itemView.findViewById(R.id.download);
            mIcon = itemView.findViewById(R.id.image);
        }
    }

}
