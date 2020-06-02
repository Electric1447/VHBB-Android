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

import eparon.vhbb_android.Constants.VHBBAndroid;
import eparon.vhbb_android.R;

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
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, VHBBAndroid.PLUGIN_LIST_JSON_URL, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject item = response.getJSONObject(i);

                            String name = item.getString(VHBBAndroid.JSON_NAME);
                            String filename = item.getString(VHBBAndroid.JSON_FILENAME);
                            String version = item.getString(VHBBAndroid.JSON_VERSION);
                            String author = item.getString(VHBBAndroid.JSON_AUTHOR);
                            String description = item.getString(VHBBAndroid.JSON_DESCRIPTION);
                            String url = item.getString(VHBBAndroid.JSON_URL);

                            mPluginsList.add(new PluginsItem(name, filename, version, author, description, url));
                        }

                        mPluginsAdapter = new PluginsAdapter(requireActivity(), mPluginsList);
                        mRecyclerView.setAdapter(mPluginsAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        mQueue.add(request);
    }

}