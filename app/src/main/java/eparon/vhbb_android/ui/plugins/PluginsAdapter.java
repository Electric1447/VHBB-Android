package eparon.vhbb_android.ui.plugins;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import eparon.vhbb_android.R;
import eparon.vhbb_android.Utils.DownloadUtils;

public class PluginsAdapter extends RecyclerView.Adapter<PluginsAdapter.ViewHolder> {

    private ArrayList<PluginsItem> mPluginsList;
    private ArrayList<PluginsItem> mPluginsListFull;

    public PluginsAdapter (ArrayList<PluginsItem> pluginsList) {
        this.mPluginsList = pluginsList;
        this.mPluginsListFull = new ArrayList<>(mPluginsList);
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

        holder.mTitle.setText(String.format("%s %s", currentItem.getName(), currentItem.getVersion()));
        holder.mAuthor.setText(currentItem.getAuthor());
        holder.mDescription.setText(currentItem.getDescription());

        holder.mDownload.setOnClickListener(v -> DownloadUtils.VHBBDownloadManager(v.getContext(), Uri.parse(currentItem.getUrl()), currentItem.getFilename()));
    }

    @Override
    public int getItemCount () {
        return mPluginsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle, mAuthor, mDescription;
        public ImageButton mDownload;

        public ViewHolder (View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.textview_name);
            mAuthor = itemView.findViewById(R.id.textview_author);
            mDescription = itemView.findViewById(R.id.textview_desc);
            mDownload = itemView.findViewById(R.id.download);
        }
    }

    //region Filter

    public Filter getFilter () {
        return mFilter;
    }

    private Filter mFilter = new Filter() {
        @Override
        protected FilterResults performFiltering (CharSequence constraint) {
            ArrayList<PluginsItem> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mPluginsListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (PluginsItem item : mPluginsListFull)
                    if (item.getName().toLowerCase().contains(filterPattern))
                        filteredList.add(item);
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults (CharSequence constraint, FilterResults results) {
            mPluginsList.clear();
            //noinspection unchecked
            mPluginsList.addAll((ArrayList<PluginsItem>)results.values);
            notifyDataSetChanged();
        }
    };

    //endregion

}
