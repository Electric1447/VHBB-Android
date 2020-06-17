package eparon.vhbb_android.ui.homebrew;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import eparon.vhbb_android.Constants.VitaDB;
import eparon.vhbb_android.R;

public class HomebrewFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private HomebrewAdapter mHomebrewAdapter;
    private ArrayList<HomebrewItem> mHomebrewList;
    private RequestQueue mQueue;
    private BottomNavigationView mBottomNav;

    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_homebrew, container, false);
        setHasOptionsMenu(true);

        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mHomebrewList = new ArrayList<>();
        mQueue = Volley.newRequestQueue(requireContext());
        jsonParse();

        mBottomNav = rootView.findViewById(R.id.bottom_nav);
        mBottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bnav_all:
                    mHomebrewAdapter.getTypeFilter().filter(String.valueOf(VitaDB.TYPE_ALL));
                    return true;
                case R.id.bnav_original_games:
                    mHomebrewAdapter.getTypeFilter().filter(String.valueOf(VitaDB.TYPE_ORIGINAL_GAMES));
                    return true;
                case R.id.bnav_game_ports:
                    mHomebrewAdapter.getTypeFilter().filter(String.valueOf(VitaDB.TYPE_GAME_PORTS));
                    return true;
                case R.id.bnav_utilities:
                    mHomebrewAdapter.getTypeFilter().filter(String.valueOf(VitaDB.TYPE_UTILISES));
                    return true;
                case R.id.bnav_emulators:
                    mHomebrewAdapter.getTypeFilter().filter(String.valueOf(VitaDB.TYPE_EMULATORS));
                    return true;
            }
            return false;
        });

        return rootView;
    }

    private void jsonParse () {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, VitaDB.HOMEBREW_LIST_JSON_URL, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject item = response.getJSONObject(i);

                            String name             = item.optString(VitaDB.JSON_NAME, "");
                            String iconUrl          = item.optString(VitaDB.JSON_ICON, "");
                            String version          = item.optString(VitaDB.JSON_VERSION, "");
                            String author           = item.optString(VitaDB.JSON_AUTHOR, "");
                            String description      = item.optString(VitaDB.JSON_DESCRIPTION, "");
                            String longDescription  = item.optString(VitaDB.JSON_LONG_DESCRIPTION, "");
                            String date             = item.optString(VitaDB.JSON_DATE, "");
                            String sourceUrl        = item.optString(VitaDB.JSON_SOURCE, "");
                            String releaseUrl       = item.optString(VitaDB.JSON_RELEASE_PAGE, "");
                            String url              = item.optString(VitaDB.JSON_URL, "");
                            String dataUrl          = item.optString(VitaDB.JSON_DATA, "");
                            int type                = item.optInt(VitaDB.JSON_TYPE, 4);
                            int id                  = item.optInt(VitaDB.JSON_ID, 0);
                            int downloads           = item.optInt(VitaDB.JSON_DOWNLOADS, 0);
                            long size               = item.optLong(VitaDB.JSON_SIZE, 0);
                            long dataSize           = item.optLong(VitaDB.JSON_DATA_SIZE, 0);

                            mHomebrewList.add(new HomebrewItem(name, iconUrl, version, author, description, longDescription, date, sourceUrl, releaseUrl, url, dataUrl, type, id, downloads, size, dataSize));
                        }

                        mHomebrewAdapter = new HomebrewAdapter(requireActivity(), mHomebrewList);
                        mRecyclerView.setAdapter(mHomebrewAdapter);
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
                mHomebrewAdapter.getSearchFilter().filter(newText);
                mBottomNav.setSelectedItemId(R.id.bnav_all);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

}