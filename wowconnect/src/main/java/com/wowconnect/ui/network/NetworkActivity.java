package com.wowconnect.ui.network;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.wowconnect.NetworkHelper;
import com.wowconnect.NewDataHolder;
import com.wowconnect.R;
import com.wowconnect.ui.customUtils.VerticalSpaceItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NetworkActivity extends AppCompatActivity {
    @BindView(R.id.recycler_network)
    RecyclerView networkRecycler;
    @BindView(R.id.layout_no_users)
    RelativeLayout noUsersLayout;
    private NetworkHelper networkHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        networkRecycler.setLayoutManager(layoutManager);
        networkRecycler.addItemDecoration(new VerticalSpaceItemDecoration(5, 1, true));
        networkHelper = new NetworkHelper(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        networkHelper.getNetworkUsers(new NetworkHelper.NetworkListener() {
            @Override
            public void onFinish() {
                if (NewDataHolder.getInstance(NetworkActivity.this).getNetworkUsers() != null &&
                        NewDataHolder.getInstance(NetworkActivity.this).getNetworkUsers().size() > 0)
                    networkRecycler.setAdapter(new NetworkAdapter(getApplicationContext(),
                            NewDataHolder.getInstance(NetworkActivity.this).getNetworkUsers()));
                else {
                    noUsersLayout.setVisibility(View.VISIBLE);
                    networkRecycler.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (networkHelper != null) {
            networkHelper.removeNetworkListener();
        }
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
