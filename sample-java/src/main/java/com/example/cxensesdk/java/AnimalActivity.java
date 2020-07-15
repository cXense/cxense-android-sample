package com.example.cxensesdk.java;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cxense.cxensesdk.CxenseSdk;
import com.cxense.cxensesdk.LoadCallback;
import com.cxense.cxensesdk.model.ConversionEvent;
import com.cxense.cxensesdk.model.CustomParameter;
import com.cxense.cxensesdk.model.EventStatus;
import com.cxense.cxensesdk.model.Impression;
import com.cxense.cxensesdk.model.PageViewEvent;
import com.cxense.cxensesdk.model.UserIdentity;
import com.cxense.cxensesdk.model.WidgetContext;
import com.cxense.cxensesdk.model.WidgetItem;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

public class AnimalActivity extends AppCompatActivity {
    public static final String ITEM_KEY = "item";
    private String item;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);
        this.item = getIntent().getStringExtra(ITEM_KEY);
        textView = findViewById(R.id.text);
        textView.setText(getString(R.string.item_text, item));
    }

    @Override
    protected void onPause() {
        CxenseSdk.getInstance().trackActiveTime(item);
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
            String message = String.format(Locale.getDefault(), "Sent: '%s'\nNot sent: '%s'", TextUtils.join(", ", sent), TextUtils.join(", ", notSent));
            Snackbar.make(textView, message, Snackbar.LENGTH_LONG).show();
        });
        CxenseSdk.getInstance().pushEvents(
                new PageViewEvent.Builder(BuildConfig.SITE_ID)
                        .contentId(item)
                        .eventId(item)
                        .addCustomParameters(new CustomParameter("xyz-item", item))
                        .build(),
                new ConversionEvent.Builder(BuildConfig.SITE_ID, "0ab24abee9a85d869b29f46c837144", ConversionEvent.FUNNEL_TYPE_CONVERT_PRODUCT, Collections.singletonList(new UserIdentity("cxd", "123456")))
                        .productPrice(12.25)
                        .renewalFrequency("1wC")
                        .build()
        );
        CxenseSdk.getInstance().loadWidgetRecommendations("ffb1d2523b582f5f649df351d37928d2c108e715", new WidgetContext.Builder("https://cxense.com").build(), new LoadCallback<List<WidgetItem>>() {
            @Override
            public void onSuccess(@NotNull List<WidgetItem> data) {
                int i = 1;
                List<Impression> impressions = new ArrayList<>();
                for (WidgetItem item: data) {
                    if (item.getClickUrl() != null)
                        impressions.add(new Impression(item.getClickUrl(), i++));
                }
                CxenseSdk.getInstance().reportWidgetVisibilities(
                        new LoadCallback<Void>() {
                            @Override
                            public void onSuccess(@NotNull Void data) {
                                Timber.d("Success");
                            }

                            @Override
                            public void onError(@NotNull Throwable throwable) {
                                Timber.e(throwable);
                            }
                        },
                        impressions.toArray(new Impression[0])
                );
            }

            @Override
            public void onError(@NotNull Throwable throwable) {
                Timber.e(throwable);
            }
        });

    }
}
