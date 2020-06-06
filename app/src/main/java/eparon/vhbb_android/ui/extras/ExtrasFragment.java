package eparon.vhbb_android.ui.extras;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
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

public class ExtrasFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ExtrasAdapter mExtrasAdapter;
    private ArrayList<ExtrasItem> mExtrasList;
    private RequestQueue mQueue;

    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_extras, container, false);
        setHasOptionsMenu(true);

        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mExtrasList = new ArrayList<>();
        mQueue = Volley.newRequestQueue(requireContext());
        jsonParse();

        return rootView;
    }

    private void jsonParse () {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, VHBBAndroid.EXTRAS_LIST_JSON_URL, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject item = response.getJSONObject(i);

                            String name             = item.optString(VHBBAndroid.JSON_NAME, "");
                            String filename         = item.optString(VHBBAndroid.JSON_FILENAME, "");
                            String dataFilename     = item.optString(VHBBAndroid.JSON_DATA_FILENAME, "");
                            String version          = item.optString(VHBBAndroid.JSON_VERSION, "");
                            String author           = item.optString(VHBBAndroid.JSON_AUTHOR, "");
                            String description      = item.optString(VHBBAndroid.JSON_DESCRIPTION, "");
                            String url              = item.optString(VHBBAndroid.JSON_URL, "");
                            String dataUrl          = item.optString(VHBBAndroid.JSON_DATA_URL, "");
                            String date             = item.optString(VHBBAndroid.JSON_DATE, "");
                            String icon             = item.optString(VHBBAndroid.JSON_ICON, "");

                            mExtrasList.add(new ExtrasItem(name, filename, version, author, description, url, dataFilename, dataUrl, date, icon));
                        }

                        mExtrasAdapter = new ExtrasAdapter(requireActivity(), mExtrasList);
                        mRecyclerView.setAdapter(mExtrasAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        mQueue.add(request);
    }

    @Override
    public void onCreateOptionsMenu (@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView)searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit (String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange (String newText) {
                mExtrasAdapter.getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

}
