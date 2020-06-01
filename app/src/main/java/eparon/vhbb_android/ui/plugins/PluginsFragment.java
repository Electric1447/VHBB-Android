package eparon.vhbb_android.ui.plugins;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import eparon.vhbb_android.R;
import eparon.vhbb_android.Constants.VitaDB;

public class PluginsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private PluginsAdapter mPluginsAdapter;
    private ArrayList<PluginsItem> mPluginsList;
    private RequestQueue mQueue;

    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_homebrew, container, false);

        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mPluginsList = new ArrayList<>();
        mQueue = Volley.newRequestQueue(requireContext());
        jsonParse();

        return rootView;
    }

    private void jsonParse () {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, VitaDB.PLUGIN_LIST_JSON_URL, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject item = response.getJSONObject(i);

                            String name = item.getString(VitaDB.JSON_NAME);
                            String version = item.getString(VitaDB.JSON_VERSION);
                            String author = item.getString(VitaDB.JSON_AUTHOR);
                            String description = item.getString(VitaDB.JSON_DESCRIPTION);
                            String longDescription = item.getString(VitaDB.JSON_LONG_DESCRIPTION);
                            String date = item.getString(VitaDB.JSON_DATE);
                            String sourceUrl = item.getString(VitaDB.JSON_SOURCE);
                            String releaseUrl = item.getString(VitaDB.JSON_RELEASE_PAGE);
                            String url = item.getString(VitaDB.JSON_URL);
                            int id = item.getInt(VitaDB.JSON_ID);
                            int downloads = item.getInt(VitaDB.JSON_DOWNLOADS);
                            long size = item.getLong(VitaDB.JSON_SIZE);

                            mPluginsList.add(new PluginsItem(name, version, author, description, longDescription, date, sourceUrl, releaseUrl, url, id, downloads, size));
                        }

                        mPluginsAdapter = new PluginsAdapter(mPluginsList);
                        mRecyclerView.setAdapter(mPluginsAdapter);;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        mQueue.add(request);
    }

}