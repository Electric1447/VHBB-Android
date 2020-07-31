package eparon.vhbb_android.ui.extras;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import eparon.vhbb_android.R;
import eparon.vhbb_android.Utils.DownloadUtils;

public class ExtrasAdapter extends RecyclerView.Adapter<ExtrasAdapter.ViewHolder> {

    private Activity mActivity;
    private ArrayList<ExtrasItem> mExtrasList;
    private ArrayList<ExtrasItem> mExtrasListFull;

    public ExtrasAdapter (Activity activity, ArrayList<ExtrasItem> extrasList) {
        this.mActivity = activity;
        this.mExtrasList = extrasList;
        this.mExtrasListFull = new ArrayList<>(mExtrasList);
    }

    @NonNull
    @Override
    public ExtrasAdapter.ViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_extras, viewGroup, false);
        return new ExtrasAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder (@NonNull ExtrasAdapter.ViewHolder holder, int position) {
        ExtrasItem currentItem = mExtrasList.get(position);

        String dataUrlID = currentItem.getDataUrl();
        String dateID = currentItem.getDate();

        holder.mTitle.setText(String.format("%s %s", currentItem.getName(), currentItem.getVersion()));
        holder.mAuthor.setText(currentItem.getAuthor());
        holder.mDescription.setText(currentItem.getDescription());
        Picasso.get().load(currentItem.getIcon()).fit().centerInside().into(holder.mIcon);

        holder.mDate.setText(String.format("(%s)", dateID));
        holder.mDate.setVisibility(!dateID.equals("") ? View.VISIBLE : View.GONE);

        holder.mDownload.setOnClickListener(v -> DownloadUtils.VHBBDownloadManager(v.getContext(), Uri.parse(currentItem.getUrl()), currentItem.getFilename()));

        holder.mDownloadData.setVisibility(!dataUrlID.equals("") ? View.VISIBLE : View.GONE);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)holder.mDescription.getLayoutParams();
        lp.setMargins(0,
                (int)mActivity.getResources().getDimension(R.dimen.homebrew_padding_small),
                (int)mActivity.getResources().getDimension(!dataUrlID.equals("") ? R.dimen.homebrew_desc_margin_sec : R.dimen.homebrew_desc_margin_def),
                0);
        holder.mDescription.setLayoutParams(lp);

        if (!dataUrlID.equals("")) holder.mDownloadData.setOnClickListener(v -> DownloadUtils.VHBBDownloadManager(v.getContext(), Uri.parse(dataUrlID), currentItem.getDataFilename()));
    }

    @Override
    public int getItemCount () {
        return mExtrasList.size();
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
            ArrayList<ExtrasItem> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mExtrasListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ExtrasItem item : mExtrasListFull)
                    if (item.getName().toLowerCase().contains(filterPattern))
                        filteredList.add(item);
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults (CharSequence constraint, FilterResults results) {
            mExtrasList.clear();
            //noinspection unchecked
            mExtrasList.addAll((ArrayList<ExtrasItem>)results.values);
            notifyDataSetChanged();
        }
    };

    //endregion

}

