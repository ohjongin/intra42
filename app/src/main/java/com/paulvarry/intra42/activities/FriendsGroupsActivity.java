package com.paulvarry.intra42.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.paulvarry.intra42.R;
import com.paulvarry.intra42.adapters.BaseListAdapterSlug;
import com.paulvarry.intra42.api.ApiService42Tools;
import com.paulvarry.intra42.api.tools42.Group;
import com.paulvarry.intra42.ui.BasicThreadActivity;
import com.paulvarry.intra42.utils.Tools;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class FriendsGroupsActivity extends BasicThreadActivity implements BasicThreadActivity.GetDataOnThread, AdapterView.OnItemClickListener, BasicThreadActivity.GetDataOnMain, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private List<Group> groups;
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_friends_groups);
        setActionBarToggle(ActionBarToggle.ARROW);

        registerGetDataOnOtherThread(this);
        registerGetDataOnMainTread(this);

        listView = findViewById(R.id.listView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        listView.setOnItemClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);

        fabBaseActivity.setVisibility(View.VISIBLE);
        fabBaseActivity.setOnClickListener(this);

        super.onCreateFinished();
    }

    @Nullable
    @Override
    public String getUrlIntra() {
        return null;
    }

    @Override
    public String getToolbarName() {
        return getString(R.string.title_activity_friends_groups);
    }

    @Override
    protected void setViewContent() {
        swipeRefreshLayout.setRefreshing(false);
        BaseListAdapterSlug<Group> adapter = new BaseListAdapterSlug<>(this, groups);
        listView.setAdapter(adapter);
    }

    @Override
    public String getEmptyText() {
        return null;
    }

    @Override
    public void getDataOnOtherThread() throws IOException, RuntimeException {

        ApiService42Tools api = app.getApiService42Tools();

        Call<List<Group>> call = api.getFriendsGroups();
        Response<List<Group>> ret = call.execute();
        if (Tools.apiIsSuccessful(ret))
            groups = ret.body();
        else
            groups = null;
    }

    @Override
    public final Object onRetainCustomNonConfigurationInstance() {
        return groups;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (groups.size() > position)
            startActivityForResult(FriendsGroupsEditActivity.getIntent(this, groups.get(position)), 0);
    }

    @Override
    public ThreadStatusCode getDataOnMainThread() {
        Object o = getLastCustomNonConfigurationInstance();

        if (o instanceof List)
            groups = (List<Group>) o;
        if (groups != null)
            return ThreadStatusCode.FINISH;
        return ThreadStatusCode.CONTINUE;
    }

    @Override
    public void onClick(View v) {
        if (v == fabBaseActivity)
            startActivityForResult(FriendsGroupsEditActivity.getIntent(this), 0);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        refresh();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            });
            groups = null;
            refresh();
        }
    }
}
