package com.example.cxensesdk.kotlin

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cxense.cxensesdk.*
import com.cxense.cxensesdk.model.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val animals = listOf(
        "alligator", "ant", "bear", "bee", "bird", "camel", "cat", "cheetah", "chicken",
        "chimpanzee", "cow", "crocodile", "deer", "dog", "dolphin", "duck", "eagle", "elephant",
        "fish", "fly", "fox", "frog", "giraffe", "goat", "goldfish", "hamster", "hippopotamus",
        "horse", "kangaroo", "kitten", "lion", "lobster", "monkey", "octopus", "owl", "panda",
        "pig", "puppy", "rabbit", "rat", "scorpion", "seal", "shark", "sheep", "snail", "snake",
        "spider", "squirrel", "tiger", "turtle", "wolf", "zebra"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = MainAdapter(animals, this::onItemClick)

        CxenseSdk.getInstance().configuration.apply {
            dispatchPeriod(MIN_DISPATCH_PERIOD, TimeUnit.MILLISECONDS)
            credentialsProvider = object : CredentialsProvider {
                override fun getUsername(): String =
                    BuildConfig.USERNAME // load it from secured store

                override fun getApiKey(): String = BuildConfig.API_KEY // load it from secured store

                override fun getDmpPushPersistentId(): String = BuildConfig.PERSISTED_ID
            }
        }
    }

    override fun onPause() {
        CxenseSdk.getInstance().setDispatchEventsCallback(null)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        CxenseSdk.getInstance().setDispatchEventsCallback(object : CxenseSdk.DispatchEventsCallback {
            override fun onDispatch(statuses: List<EventStatus>) {
                val grouped = statuses.groupBy { it.isSent }
                showText("Sent: '${grouped[true]?.asString()}'\nNot sent: '${grouped[false]?.asString()}'")
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.run -> {
                runMethods()
                true
            }
            R.id.flush -> {
                CxenseSdk.getInstance().flushEventQueue()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onItemClick(item: String) {
        Intent(this, AnimalActivity::class.java)
            .putExtra(AnimalActivity.ITEM_KEY, item)
            .also { startActivity(it) }
    }

    private fun showText(str: String) {
        Snackbar.make(recyclerview, str, Snackbar.LENGTH_LONG).show()
    }

    private fun showError(t: Throwable) {
        Timber.e(t)
        showText(t.message ?: "")
    }

    private fun runMethods() {
        val id = "some_user_id"
        val type = "cxd"
        val segmentsPersistentId = "some_persistemt_id"
        val cxenseSdk = CxenseSdk.getInstance()
        val identity = UserIdentity(type, id)
        val identities = listOf(identity)
        cxenseSdk.executePersistedQuery(
            ENDPOINT_USER_SEGMENTS,
            segmentsPersistentId,
            UserSegmentRequest(identities, listOf()),
            object : LoadCallback<SegmentsResponse> {
                override fun onSuccess(data: SegmentsResponse) {
                    showText(TextUtils.join(" ", data.ids))
                }

                override fun onError(throwable: Throwable) {
                    showError(throwable)
                }
            })
        cxenseSdk.getUserSegmentIds(
            identities,
            listOf(BuildConfig.SITE_ID),
            object : LoadCallback<List<String>> {
                override fun onSuccess(data: List<String>) {
                    showText(TextUtils.join(" ", data))
                }

                override fun onError(throwable: Throwable) {
                    showError(throwable)
                }
            })
        cxenseSdk.getUser(identity, callback = object : LoadCallback<User> {
            override fun onSuccess(data: User) {
                showText(String.format(Locale.US, "User id = %s", data.id))
            }

            override fun onError(throwable: Throwable) {
                showError(throwable)
            }
        })

        // read external data for user
        cxenseSdk.getUserExternalData(
            type,
            id,
            callback = object : LoadCallback<List<UserExternalData>> {
                override fun onSuccess(data: List<UserExternalData>) {
                    showText(String.format(Locale.US, "We have %d items", data.size))
                }

                override fun onError(throwable: Throwable) {
                    showError(throwable)
                }
            })

        // read external data for all users with type
        cxenseSdk.getUserExternalData(
            type,
            callback = object : LoadCallback<List<UserExternalData>> {
                override fun onSuccess(data: List<UserExternalData>) {
                    showText(String.format(Locale.US, "We have %d items", data.size))
                }

                override fun onError(throwable: Throwable) {
                    showError(throwable)
                }
            })

        // delete external data for user
        cxenseSdk.deleteUserExternalData(UserIdentity(id, type), object : LoadCallback<Void> {
            override fun onSuccess(data: Void) {
                showText("Success")
            }

            override fun onError(throwable: Throwable) {
                showError(throwable)
            }
        })

        // update external data for user
        val userExternalData = UserExternalData.Builder(identity)
            .addExternalItems(
                ExternalItem("gender", "male"),
                ExternalItem("interests", "football"),
                ExternalItem("sports", "football")
            )
            .build()
        cxenseSdk.setUserExternalData(userExternalData, object : LoadCallback<Void> {
            override fun onSuccess(data: Void) {
                showText("Success")
            }

            override fun onError(throwable: Throwable) {
                showError(throwable)
            }
        })

        val builder =
            PerformanceEvent.Builder(
                BuildConfig.SITE_ID,
                "cxd-origin",
                "tap",
                mutableListOf(identity)
            )
                .prnd(UUID.randomUUID().toString())
                .addCustomParameters(
                    CustomParameter("cxd-interests", "TEST"),
                    CustomParameter("cxd-test", "TEST")

                )
        cxenseSdk.pushEvents(builder.build(), builder.build())
    }
}
