package eparon.vhbb_android.ui.plugins;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

import eparon.vhbb_android.R;
import eparon.vhbb_android.Utils.NetworkUtils;

public class PluginsAdapter extends RecyclerView.Adapter<PluginsAdapter.ViewHolder> {

    private ArrayList<PluginsItem> mPluginsList;

    public PluginsAdapter (ArrayList<PluginsItem> pluginsList) {
        mPluginsList = pluginsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_plugins, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder (@NonNull ViewHolder holder, int position) {
        PluginsItem currentItem = mPluginsList.get(position);

        String nameID = currentItem.getName();
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

        holder.mDownload.setOnClickListener(v -> Toast.makeText(v.getContext(), "Currently disabled :(", Toast.LENGTH_SHORT).show());


        holder.mDownload.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(v.getContext(), "Storage permission not granted", Toast.LENGTH_LONG).show();
                return;
            }
            if (!NetworkUtils.isNetworkAvailable(v.getContext())) {
                Toast.makeText(v.getContext(), "Network not available", Toast.LENGTH_SHORT).show();
                return;
            }

            DownloadManager downloadmanager = (DownloadManager)v.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(urlID);

            String[] pluginFileExtensions = new String[] {".skprx", ".suprx", ".zip"};

            String filename = "";

            for (String fileEx : pluginFileExtensions) {
                if (NetworkUtils.doesExists("https://dl.coolatoms.org/vitadb/" + nameID + fileEx))
                    filename = nameID + fileEx;
            }

            if (filename.equals("")) {
                Toast.makeText(v.getContext(), "Error while downloading plugin", Toast.LENGTH_LONG).show();
                return;
            }

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
        return mPluginsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle, mAuthor, mDescription, mDate, mDownloads;
        public ImageButton mDownload;

        public ViewHolder (View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.textview_name);
            mAuthor = itemView.findViewById(R.id.textview_author);
            mDescription = itemView.findViewById(R.id.textview_desc);
            mDate = itemView.findViewById(R.id.textview_date);
            mDownloads = itemView.findViewById(R.id.textview_downloads);
            mDownload = itemView.findViewById(R.id.download);
            mDate = itemView.findViewById(R.id.textview_date);
        }
    }

}
