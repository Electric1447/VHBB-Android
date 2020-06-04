package eparon.vhbb_android.ui.customrepo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
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

public class CustomRepoFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private CustomRepoAdapter mCustomRepoAdapter;
    private ArrayList<CustomRepoItem> mCustomRepoList;
    private RequestQueue mQueue;

    CardView mCardView;

    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_customrepo, container, false);
        setHasOptionsMenu(true);

        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mCustomRepoList = new ArrayList<>();
        mQueue = Volley.newRequestQueue(requireContext());

        mCardView = rootView.findViewById(R.id.refresh_cardview);
        Button mRefreshButton = rootView.findViewById(R.id.refresh_button);
        mRefreshButton.setOnClickListener(v -> showDialog());

        showDialog();

        return rootView;
    }

    private void showDialog () {
        mCardView.setVisibility(View.GONE);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.repoAlertDialogStyle);
        builder.setTitle("Repository URL:");

        final EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> jsonParse(input.getText().toString()));
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
            mCardView.setVisibility(View.VISIBLE);
        });

        builder.setOnCancelListener(dialog -> mCardView.setVisibility(View.VISIBLE));

        builder.show();
    }

    private void jsonParse (String repoUrl) {
        if (repoUrl.length() > 4) if (!repoUrl.substring(0, 4).equalsIgnoreCase("http"))
            repoUrl = "https://" + repoUrl;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, repoUrl, null,
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

                            mCustomRepoList.add(new CustomRepoItem(name, filename, dataFilename, version, author, description, url, dataUrl, date));
                        }

                        mCustomRepoAdapter = new CustomRepoAdapter(requireActivity(), mCustomRepoList);
                        mRecyclerView.setAdapter(mCustomRepoAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            mCardView.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "Invalid Repo URL", Toast.LENGTH_SHORT).show();
        });
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
                mCustomRepoAdapter.getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

}
