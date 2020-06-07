package eparon.vhbb_android.ui.homebrew;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import eparon.vhbb_android.Constants.VitaDB;
import eparon.vhbb_android.R;
import eparon.vhbb_android.Utils.NetworkUtils;
import eparon.vhbb_android.Utils.PermissionUtils;

public class HomebrewAdapter extends RecyclerView.Adapter<HomebrewAdapter.ViewHolder> {

    private Activity mActivity;
    private ArrayList<HomebrewItem> mHomebrewList;
    private ArrayList<HomebrewItem> mHomebrewListFull;

    public HomebrewAdapter (Activity activity, ArrayList<HomebrewItem> homebrewList) {
        this.mActivity = activity;
        this.mHomebrewList = homebrewList;
        this.mHomebrewListFull = new ArrayList<>(mHomebrewList);
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
        String dataUrlID = currentItem.getDataUrl();

        holder.mTitle.setText(String.format("%s %s", nameID, versionID));
        holder.mAuthor.setText(authorID);
        holder.mDescription.setText(descriptionID);
        holder.mDate.setText(String.format("(%s)", dateID));
        holder.mDownloads.setText(String.format(Locale.getDefault(), "%dDLs", downloadsID));
        Picasso.get().load(iconID).fit().centerInside().into(holder.mIcon);

        holder.mDownload.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                PermissionUtils.requestStoragePermission(mActivity);

            if (!NetworkUtils.isNetworkAvailable(v.getContext())) {
                Toast.makeText(v.getContext(), "Network not available", Toast.LENGTH_SHORT).show();
                return;
            }

            DownloadManager downloadmanager = (DownloadManager)v.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(urlID);

            String filename = nameID + ".vpk";

            DownloadManager.Request request = new DownloadManager.Request(uri)
                    .setTitle(filename)
                    .setDescription("Downloading...")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setVisibleInDownloadsUi(true)
                    .addRequestHeader(VitaDB.UA_REQUEST_HEADER, VitaDB.UA_REQUEST_VALUE) // Set a valid user-agent for the requests.
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);

            assert downloadmanager != null;
            downloadmanager.enqueue(request);
        });

        holder.mDownloadData.setVisibility(!dataUrlID.equals("") ? View.VISIBLE : View.GONE);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)holder.mDescription.getLayoutParams();
        lp.setMargins(0,
                (int)mActivity.getResources().getDimension(R.dimen.homebrew_padding_small),
                (int)mActivity.getResources().getDimension(!dataUrlID.equals("") ? R.dimen.homebrew_desc_margin_sec : R.dimen.homebrew_desc_margin_def),
                0);
        holder.mDescription.setLayoutParams(lp);

        if (!dataUrlID.equals("")) holder.mDownloadData.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                PermissionUtils.requestStoragePermission(mActivity);

            if (!NetworkUtils.isNetworkAvailable(v.getContext())) {
                Toast.makeText(v.getContext(), "Network not available", Toast.LENGTH_SHORT).show();
                return;
            }

            DownloadManager downloadmanager = (DownloadManager)v.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(dataUrlID);

            String filename = dataUrlID.substring(dataUrlID.lastIndexOf("/") + 1);

            DownloadManager.Request request = new DownloadManager.Request(uri)
                    .setTitle(filename)
                    .setDescription("Downloading...")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setVisibleInDownloadsUi(true)
                    .addRequestHeader(VitaDB.UA_REQUEST_HEADER, VitaDB.UA_REQUEST_VALUE) // Set a valid user-agent for the requests.
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
        public ImageButton mDownload, mDownloadData;
        public ImageView mIcon;

        public ViewHolder (View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.textview_name);
            mAuthor = itemView.findViewById(R.id.textview_author);
            mDescription = itemView.findViewById(R.id.textview_desc);
            mDate = itemView.findViewById(R.id.textview_date);
            mDownloads = itemView.findViewById(R.id.textview_downloads);
            mDownload = itemView.findViewById(R.id.download);
            mDownloadData = itemView.findViewById(R.id.downloadData);
            mIcon = itemView.findViewById(R.id.image);
        }
    }

    //region Filter

    public Filter getFilter () {
        return mFilter;
    }

    private Filter mFilter = new Filter() {
        @Override
        protected FilterResults performFiltering (CharSequence constraint) {
            ArrayList<HomebrewItem> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mHomebrewListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (HomebrewItem item : mHomebrewListFull)
                    if (item.getName().toLowerCase().contains(filterPattern))
                        filteredList.add(item);
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults (CharSequence constraint, FilterResults results) {
            mHomebrewList.clear();
            //noinspection unchecked
            mHomebrewList.addAll((ArrayList<HomebrewItem>)results.values);
            notifyDataSetChanged();
        }
    };

    //endregion

}
