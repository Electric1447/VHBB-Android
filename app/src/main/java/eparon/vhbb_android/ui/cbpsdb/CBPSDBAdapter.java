package eparon.vhbb_android.ui.cbpsdb;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import eparon.vhbb_android.Constants.CBPSDB;
import eparon.vhbb_android.Constants.VHBBAndroid;
import eparon.vhbb_android.R;
import eparon.vhbb_android.Utils.DownloadUtils;
import eparon.vhbb_android.Utils.NetworkUtils;
import eparon.vhbb_android.Utils.PermissionUtils;

public class CBPSDBAdapter extends RecyclerView.Adapter<CBPSDBAdapter.ViewHolder> {

    private Activity mActivity;
    private ArrayList<CBPSDBItem> mCBPSDBList;
    private ArrayList<CBPSDBItem> mCBPSDBListFull;

    public CBPSDBAdapter (Activity activity, ArrayList<CBPSDBItem> cbpsdbList) {
        this.mActivity = activity;
        this.mCBPSDBList = cbpsdbList;
        this.mCBPSDBListFull = new ArrayList<>(mCBPSDBList);
    }

    @NonNull
    @Override
    public CBPSDBAdapter.ViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cbpsdb, viewGroup, false);
        return new CBPSDBAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder (@NonNull CBPSDBAdapter.ViewHolder holder, int position) {
        CBPSDBItem currentItem = mCBPSDBList.get(position);

        String idID = currentItem.getID();
        String nameID = currentItem.getName();
        String authorID = currentItem.getAuthor();
        String iconID = currentItem.getIcon0();
        String urlID = currentItem.getUrl();
        String optionsID = currentItem.getOptions();
        String typeID = currentItem.getType();
        String dataUrlID = currentItem.getDataUrl();

        holder.mTitle.setText(nameID);
        holder.mAuthor.setText(authorID);
        holder.mType.setText(String.format("(%s)", currentItem.getTypeString()));

        if (iconID.equals("None")) {
            switch (typeID) {
                case CBPSDB.TYPE_VPK:
                case CBPSDB.TYPE_DATA:
                    iconID = VHBBAndroid.DEFAULT_ICON_URL;
                    holder.mIcon.setVisibility(View.VISIBLE);
                    break;
                case CBPSDB.TYPE_PLUGIN:
                    holder.mIcon.setVisibility(View.GONE);
                    break;
            }
        } else {
            holder.mIcon.setVisibility(View.VISIBLE);
        }

        Picasso.get().load(iconID).fit().centerInside().into(holder.mIcon);

        holder.mDownload.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                PermissionUtils.requestStoragePermission(mActivity);

            if (!NetworkUtils.isNetworkAvailable(v.getContext())) {
                Toast.makeText(v.getContext(), "Network not available", Toast.LENGTH_SHORT).show();
                return;
            }

            String filename = urlID.substring(urlID.lastIndexOf("/") + 1);
            String filenameExtension = filename.substring(filename.lastIndexOf(".")).toLowerCase();

            // If URL does not contain the filename
            if (!(filenameExtension.equals(".vpk") || filenameExtension.equals(".zip") || filenameExtension.equals(".suprx") || filenameExtension.equals(".skprx"))) {
                if (typeID.equals(CBPSDB.TYPE_PLUGIN)) {
                    if (optionsID.equals(CBPSDB.OPTIONS_KERNEL))
                        filename = idID + ".skprx"; // Kernel plugin
                    else
                        filename = idID + ".suprx"; // User plugin
                } else {
                    filename = idID;
                    Toast.makeText(v.getContext(), "Error while parsing file extension", Toast.LENGTH_SHORT).show();
                }
            }

            DownloadUtils.VHBBDownloadManager(v.getContext(), Uri.parse(urlID), filename);
        });

        holder.mDownloadData.setVisibility(!dataUrlID.equals("None") ? View.VISIBLE : View.GONE);

        if (!dataUrlID.equals("None")) holder.mDownloadData.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                PermissionUtils.requestStoragePermission(mActivity);

            if (!NetworkUtils.isNetworkAvailable(v.getContext())) {
                Toast.makeText(v.getContext(), "Network not available", Toast.LENGTH_SHORT).show();
                return;
            }

            String filename = urlID.substring(urlID.lastIndexOf("/") + 1);
            filename = filename.substring(0, filename.lastIndexOf(".")) + "-data.zip";

            DownloadUtils.VHBBDownloadManager(v.getContext(), Uri.parse(dataUrlID), filename);
        });
    }

    @Override
    public int getItemCount () {
        return mCBPSDBList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle, mAuthor, mType;
        public ImageButton mDownload, mDownloadData;
        public ImageView mIcon;

        public ViewHolder (View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.textview_name);
            mAuthor = itemView.findViewById(R.id.textview_author);
            mType = itemView.findViewById(R.id.textview_type);
            mDownload = itemView.findViewById(R.id.download);
            mDownloadData = itemView.findViewById(R.id.downloadData);
            mIcon = itemView.findViewById(R.id.image);
        }
    }

    //region Filters

    public Filter getSearchFilter () {
        return mSearchFilter;
    }

    private Filter mSearchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering (CharSequence constraint) {
            ArrayList<CBPSDBItem> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mCBPSDBListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (CBPSDBItem item : mCBPSDBListFull)
                    if (item.getName().toLowerCase().contains(filterPattern))
                        filteredList.add(item);
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults (CharSequence constraint, FilterResults results) {
            mCBPSDBList.clear();
            //noinspection unchecked
            mCBPSDBList.addAll((ArrayList<CBPSDBItem>)results.values);
            notifyDataSetChanged();
        }
    };

    public Filter getTypeFilter () {
        return mTypeFilter;
    }

    private Filter mTypeFilter = new Filter() {
        @Override
        protected FilterResults performFiltering (CharSequence constraint) {
            ArrayList<CBPSDBItem> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0 || constraint.equals(CBPSDB.TYPE_ALL)) {
                filteredList.addAll(mCBPSDBListFull);
            } else {
                for (CBPSDBItem item : mCBPSDBListFull)
                    if (item.getType().contentEquals(constraint))
                        filteredList.add(item);
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults (CharSequence constraint, FilterResults results) {
            mCBPSDBList.clear();
            //noinspection unchecked
            mCBPSDBList.addAll((ArrayList<CBPSDBItem>)results.values);
            notifyDataSetChanged();
        }
    };

    //endregion

}