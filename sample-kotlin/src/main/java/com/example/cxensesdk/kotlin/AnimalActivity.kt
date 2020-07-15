package com.example.cxensesdk.kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cxense.cxensesdk.CxenseSdk
import com.cxense.cxensesdk.LoadCallback
import com.cxense.cxensesdk.model.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_animal.*
import timber.log.Timber

class AnimalActivity : AppCompatActivity() {
    private lateinit var item: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal)
        item = intent.getStringExtra(ITEM_KEY) ?: ""
        animalText.text = getString(R.string.item_text, item)
    }

    override fun onPause() {
        CxenseSdk.getInstance().trackActiveTime(item)
        CxenseSdk.getInstance().setDispatchEventsCallback(null)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        CxenseSdk.getInstance().setDispatchEventsCallback(object : CxenseSdk.DispatchEventsCallback {
            override fun onDispatch(statuses: List<EventStatus>) {
                val grouped = statuses.groupBy { it.isSent }
                val message = "Sent: '${grouped[true]?.asString()}'\nNot sent: '${grouped[false]?.asString()}'"
                Snackbar.make(animalText, message, Snackbar.LENGTH_LONG).show()
            }

        })
        CxenseSdk.getInstance().pushEvents(
            PageViewEvent.Builder(BuildConfig.SITE_ID)
                .contentId(item)
                .eventId(item)
                .addCustomParameters(CustomParameter("xyz-item", item))
                .build(),
            ConversionEvent.Builder(
                BuildConfig.SITE_ID,
                "0ab24abee9a85d869b29f46c837144",
                ConversionEvent.FUNNEL_TYPE_CONVERT_PRODUCT,
                mutableListOf(UserIdentity("123456", "cxd"))
            )
                .productPrice(12.25)
                .renewalFrequency("1wC")
                .build()
        )
        CxenseSdk.getInstance().loadWidgetRecommendations(
            "ffb1d2523b582f5f649df351d37928d2c108e715",
            WidgetContext.Builder("https://cxense.com").build(),
            callback = object : LoadCallback<List<WidgetItem>> {
                override fun onSuccess(data: List<WidgetItem>) {
                    data.mapNotNull { it.clickUrl }
                        .mapIndexed { index, url -> Impression(url, index) }
                        .takeIf { it.isNotEmpty() }
                        ?.let {
                            CxenseSdk.getInstance().reportWidgetVisibilities(
                                object : LoadCallback<Any> {
                                    override fun onSuccess(data: Any) {
                                        Timber.d("Success")
                                    }

                                    override fun onError(throwable: Throwable) {
                                        Timber.e(throwable)
                                    }
                                },
                                *it.toTypedArray()
                            )
                        }
                }

                override fun onError(throwable: Throwable) {
                    Timber.e(throwable)
                }
            })
    }

    companion object {
        const val ITEM_KEY = "item"
    }
}
