package com.example.cxensesdk.java;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cxense.cxensesdk.CredentialsProvider;
import com.cxense.cxensesdk.CxenseConfiguration;
import com.cxense.cxensesdk.CxenseConstants;
import com.cxense.cxensesdk.CxenseSdk;
import com.cxense.cxensesdk.LoadCallback;
import com.cxense.cxensesdk.model.CustomParameter;
import com.cxense.cxensesdk.model.EventStatus;
import com.cxense.cxensesdk.model.ExternalItem;
import com.cxense.cxensesdk.model.PerformanceEvent;
import com.cxense.cxensesdk.model.SegmentsResponse;
import com.cxense.cxensesdk.model.User;
import com.cxense.cxensesdk.model.UserExternalData;
import com.cxense.cxensesdk.model.UserIdentity;
import com.cxense.cxensesdk.model.UserSegmentRequest;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements MainAdapter.ItemClickListener {
    public static final String TAG = MainActivity.class.getSimpleName();
    RecyclerView recyclerView;

    private String[] animals = {
            "alligator", "ant", "bear", "bee", "bird", "camel", "cat", "cheetah", "chicken",
            "chimpanzee", "cow", "crocodile", "deer", "dog", "dolphin", "duck", "eagle", "elephant",
            "fish", "fly", "fox", "frog", "giraffe", "goat", "goldfish", "hamster", "hippopotamus",
            "horse", "kangaroo", "kitten", "lion", "lobster", "monkey", "octopus", "owl", "panda",
            "pig", "puppy", "rabbit", "rat", "scorpion", "seal", "shark", "sheep", "snail", "snake",
            "spider", "squirrel", "tiger", "turtle", "wolf", "zebra"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MainAdapter adapter = new MainAdapter(animals, this);
        recyclerView.setAdapter(adapter);

        CxenseConfiguration config = CxenseSdk.getInstance().getConfiguration();
        config.dispatchPeriod(CxenseConstants.getMinDispatchPeriod(), TimeUnit.MILLISECONDS);
        config.setCredentialsProvider(new CredentialsProvider() {
            @Override
            @NotNull
            public String getUsername() {
                return BuildConfig.USERNAME; // load it from secured store
            }

            @Override
            @NotNull
            public String getApiKey() {
                return BuildConfig.API_KEY; // load it from secured store
            }

            @Override
            @NotNull
            public String getDmpPushPersistentId() {
                return BuildConfig.PERSISTED_ID;
            }
        });
    }

    @Override
    protected void onPause() {
        CxenseSdk.getInstance().setDispatchEventsCallback(null);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CxenseSdk.getInstance().setDispatchEventsCallback(statuses -> {
            List<String> sent = new ArrayList<>(), notSent = new ArrayList<>();
            for (EventStatus s : statuses) {
                if (s.isSent())
                    sent.add(s.getEventId());
                else notSent.add(s.getEventId());
            }
            showText(String.format(Locale.getDefault(), "Sent: '%s'\nNot sent: '%s'", TextUtils.join(", ", sent), TextUtils.join(", ", notSent)));
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.run:
                runMethods();
                return true;
            case R.id.flush:
                CxenseSdk.getInstance().flushEventQueue();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(String item) {
        Intent intent = new Intent(this, AnimalActivity.class);
        intent.putExtra(AnimalActivity.ITEM_KEY, item);
        startActivity(intent);
    }

    private void showText(String str) {
        Snackbar.make(recyclerView, str, Snackbar.LENGTH_LONG).show();
    }

    private void showError(Throwable t) {
        Timber.e(t);
        showText(t.getMessage());
    }

    private void runMethods() {
        String id = "some_user_id";
        String type = "cxd";
        String segmentsPersistentId = "some_persistent_id";
        CxenseSdk cxenseSdk = CxenseSdk.getInstance();
        UserIdentity identity = new UserIdentity(type, id);
        List<UserIdentity> identities = new ArrayList<>();
        identities.add(identity);
        cxenseSdk.executePersistedQuery(CxenseConstants.ENDPOINT_USER_SEGMENTS, segmentsPersistentId, new UserSegmentRequest(Collections.singletonList(new UserIdentity(id, type)), null), new LoadCallback<SegmentsResponse>() {
            @Override
            public void onSuccess(@NotNull SegmentsResponse segmentsResponse) {
                showText(TextUtils.join(" ", segmentsResponse.getIds()));
            }

            @Override
            public void onError(@NotNull Throwable throwable) {
                showError(throwable);
            }
        });
        cxenseSdk.getUserSegmentIds(identities, Collections.singletonList(BuildConfig.SITEGROUP_ID), new LoadCallback<List<String>>() {
            @Override
            public void onSuccess(@NotNull List<String> data) {
                showText(TextUtils.join(" ", data));
            }

            @Override
            public void onError(@NotNull Throwable throwable) {
                showError(throwable);
            }
        });
        cxenseSdk.getUser(identity, new LoadCallback<User>() {
            @Override
            public void onSuccess(@NotNull User data) {
                showText(String.format(Locale.US, "User id = %s", data.getId()));
            }

            @Override
            public void onError(@NotNull Throwable throwable) {
                showError(throwable);
            }
        });

        // read external data for user
        cxenseSdk.getUserExternalData(type, id, new LoadCallback<List<UserExternalData>>() {
            @Override
            public void onSuccess(@NotNull List<UserExternalData> data) {
                showText(String.format(Locale.US, "We have %d items", data.size()));
            }

            @Override
            public void onError(@NotNull Throwable throwable) {
                showError(throwable);
            }
        });

        // read external data for all users with type
        cxenseSdk.getUserExternalData(type, new LoadCallback<List<UserExternalData>>() {
            @Override
            public void onSuccess(@NotNull List<UserExternalData> data) {
                showText(String.format(Locale.US, "We have %d items", data.size()));
            }

            @Override
            public void onError(@NotNull Throwable throwable) {
                showError(throwable);
            }
        });

        // delete external data for user
        cxenseSdk.deleteUserExternalData(identity, new LoadCallback<Void>() {
            @Override
            public void onSuccess(@NotNull Void data) {
                showText("Success");
            }

            @Override
            public void onError(@NotNull Throwable throwable) {
                showError(throwable);
            }
        });

        // update external data for user
        UserExternalData userExternalData = new UserExternalData.Builder(identity)
                .addExternalItems(
                        new ExternalItem("gender", "male"),
                        new ExternalItem("interests", "football"),
                        new ExternalItem("sports", "football")
                )
                .build();
        cxenseSdk.setUserExternalData(userExternalData, new LoadCallback<Void>() {
            @Override
            public void onSuccess(@NotNull Void data) {
                showText("Success");
            }

            @Override
            public void onError(@NotNull Throwable throwable) {
                showError(throwable);
            }
        });

        PerformanceEvent.Builder builder = new PerformanceEvent.Builder(BuildConfig.SITE_ID, "cxd-origin", "tap", identities)
                .prnd(UUID.randomUUID().toString())
                .addCustomParameters(
                        new CustomParameter("cxd-interests", "TEST"),
                        new CustomParameter("cxd-test", "TEST")
                );
        cxenseSdk.pushEvents(builder.build(), builder.build());
    }

}
