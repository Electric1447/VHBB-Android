package eparon.vhbb_android.ui.cbpsdb;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import eparon.vhbb_android.Constants.CBPSDB;
import eparon.vhbb_android.R;

public class CBPSDBFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private CBPSDBAdapter mCBPSDBAdapter;
    private ArrayList<CBPSDBItem> mCBPSDBList;
    private BottomNavigationView mBottomNav;

    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cbpsdb, container, false);
        setHasOptionsMenu(true);

        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mCBPSDBList = new ArrayList<>();

        DownloadFilesTask downloadFilesTask = new DownloadFilesTask();
        downloadFilesTask.execute();

        mBottomNav = rootView.findViewById(R.id.bottom_nav);
        mBottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bnav_all:
                    mCBPSDBAdapter.getTypeFilter().filter(CBPSDB.TYPE_ALL);
                    return true;
                case R.id.bnav_homebrew:
                    mCBPSDBAdapter.getTypeFilter().filter(CBPSDB.TYPE_VPK);
                    return true;
                case R.id.bnav_plugins:
                    mCBPSDBAdapter.getTypeFilter().filter(CBPSDB.TYPE_PLUGIN);
                    return true;
            }
            return false;
        });

        return rootView;
    }

    @SuppressLint("StaticFieldLeak")
    private class DownloadFilesTask extends AsyncTask<URL, Void, List<String[]>> {

        protected List<String[]> doInBackground (URL... urls) {
            return downloadRemoteTextFileContent();
        }

        protected void onPostExecute (List<String[]> result) {
            if (result != null)
                initializeAdapter(result);
        }

    }

    private List<String[]> downloadRemoteTextFileContent () {
        URL mUrl = null;
        List<String[]> csvLine = new ArrayList<>();
        String[] content;

        try {
            mUrl = new URL(CBPSDB.CVS_URL_RAW);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            assert mUrl != null;
            URLConnection connection = mUrl.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = br.readLine()) != null) {
                content = line.split(",");
                csvLine.add(content);
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return csvLine;
    }

    private void initializeAdapter (List<String[]> result) {
        List<String[]> dataList = new ArrayList<>();

        for (int i = 1; i < result.size(); i++) {
            String[] item = result.get(i);

            boolean isVisible = item[CBPSDB.CVS_VISIBLE].equals("True");

            if (isVisible) {
                mCBPSDBList.add(new CBPSDBItem(item[CBPSDB.CVS_ID], item[CBPSDB.CVS_TITLE], item[CBPSDB.CVS_CREDITS], item[CBPSDB.CVS_ICON0], item[CBPSDB.CVS_URL], item[CBPSDB.CVS_OPTIONS], item[CBPSDB.CVS_TYPE]));
            } else {
                dataList.add(new String[] {item[CBPSDB.CVS_TITLE].substring(0, item[CBPSDB.CVS_TITLE].length() - 11), item[CBPSDB.CVS_URL]});
            }
        }

        if (dataList.size() > 0)
            for (int i = 0; i < dataList.size(); i++)
                for (int j = 0; j < mCBPSDBList.size(); j++)
                    if (dataList.get(i)[0].equals(mCBPSDBList.get(j).getName()))
                        mCBPSDBList.get(j).setDataUrl(dataList.get(i)[1]);

        mCBPSDBAdapter = new CBPSDBAdapter(mCBPSDBList);
        mRecyclerView.setAdapter(mCBPSDBAdapter);
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
                mCBPSDBAdapter.getSearchFilter().filter(newText);
                mBottomNav.setSelectedItemId(R.id.bnav_all);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

}
