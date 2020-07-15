package com.cxense.coroutinesadapter

import com.cxense.cxensesdk.CxenseSdk
import com.cxense.cxensesdk.LoadCallback
import com.cxense.cxensesdk.model.ContentUser
import com.cxense.cxensesdk.model.EventStatus
import com.cxense.cxensesdk.model.User
import com.cxense.cxensesdk.model.UserExternalData
import com.cxense.cxensesdk.model.UserIdentity
import com.cxense.cxensesdk.model.WidgetContext
import com.cxense.cxensesdk.model.WidgetItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Tracks a click for the given click-url
 *
 * @param url the click-url
 */
suspend fun trackClick(url: String) {
    await<Void> { callback ->
        CxenseSdk.getInstance().trackClick(url, callback)
    }
}

/**
 * Tracks an url click for the given item
 *
 * @param item the item that contains the click-url
 */
suspend fun trackClick(item: WidgetItem) {
    await<Void> { callback ->
        CxenseSdk.getInstance().trackClick(item, callback)
    }
}

/**
 * @param widgetId      the widget id
 * @param widgetContext the WidgetContext
 * @param user          custom user
 * @return a list of widgets
 */
suspend fun loadWidgetRecommendations(
    widgetId: String,
    widgetContext: WidgetContext,
    user: ContentUser? = null,
    tag: String? = null,
    prnd: String? = null
): List<WidgetItem> = await { callback ->
    CxenseSdk.getInstance()
        .loadWidgetRecommendations(widgetId, widgetContext, user, tag, prnd, callback)
}

/**
 * Asynchronously retrieves a list of all segments where the specified user is a member
 *
 * @param identities   a collection of user identifiers for a single user to retrieve segments for
 * @param siteGroupIds the collection of site groups to retrieve segments for
 * @return a list of user segment ids
 */
suspend fun getUserSegmentIds(
    identities: List<UserIdentity>,
    siteGroupIds: List<String>
): List<String> = await { callback ->
    CxenseSdk.getInstance().getUserSegmentIds(identities, siteGroupIds, callback)
}

/**
 * Asynchronously retrieves a suitably authorized slice of a given user's interest profile
 *
 * @param identity      user identifier with type and id
 * @param groups        a collection of strings that specify profile item groups to keep in the returned profile.
 *                      If not specified, all groups available for the user will be returned
 * @param recent        flag whether to only return the most recent user profile information. This can be used to
 *                      return quickly if response time is important
 * @param identityTypes a collection of external customer identifier types. If an external customer identifier exists for
 *                      the user, it will be included in the response
 * @return an {@link User} object
 */
suspend fun getUser(
    identity: UserIdentity,
    groups: List<String>? = null,
    recent: Boolean? = null,
    identityTypes: List<String>? = null
): User = await { callback ->
    CxenseSdk.getInstance().getUser(identity, groups, recent, identityTypes, callback)
}

/**
 * Asynchronously retrieves the external data associated with a given user
 *
 * @param id   identifier for the user. Use 'null' if you want match all users of provided type.
 * @param type the customer identifier type
 * @param filter a traffic filter of type user-external with required group and optional item/items specified
 * @return a list of {@link UserExternalData} objects
 */
suspend fun getUserExternalData(
    type: String,
    id: String? = null,
    filter: String? = null
): List<UserExternalData> = await { callback ->
    CxenseSdk.getInstance().getUserExternalData(type, id, filter, callback)
}

/**
 * Asynchronously sets the external data associated with a given user
 *
 * @param userExternalData external data associated with a user
 */
suspend fun setUserExternalData(userExternalData: UserExternalData) {
    await<Void> { callback ->
        CxenseSdk.getInstance().setUserExternalData(userExternalData, callback)
    }
}

/**
 * Asynchronously deletes the external data associated with a given user
 *
 * @param identity user identifier with type and id
 */
suspend fun deleteUserExternalData(identity: UserIdentity) {
    await<Void> { callback ->
        CxenseSdk.getInstance().deleteUserExternalData(identity, callback)
    }
}

/**
 * Asynchronously retrieves a registered external identity mapping for a Cxense identifier
 *
 * @param cxenseId the Cxense identifier of the user.
 * @param type     the identity mapping type (customer identifier type) that contains the mapping.
 * @return an {@link UserIdentity} object
 */
suspend fun getUserExternalLink(cxenseId: String, type: String): UserIdentity = await { callback ->
    CxenseSdk.getInstance().getUserExternalLink(cxenseId, type, callback)
}

/**
 * Asynchronously register a new identity-mapping for the given user
 *
 * @param cxenseId the Cxense identifier to map this user to
 * @param identity user identifier with type and id
 * @return an {@link UserIdentity} object
 */
suspend fun addUserExternalLink(cxenseId: String, identity: UserIdentity): UserIdentity =
    await { callback ->
        CxenseSdk.getInstance().addUserExternalLink(cxenseId, identity, callback)
    }

/**
 * Listener for events sending statuses
 *
 * @return an {@link Observable} that emits statuses at each events sending
 */
@ExperimentalCoroutinesApi
suspend fun getDispatchEventsStatuses(): Flow<List<EventStatus>> =
    callbackFlow {
        val callback = object : CxenseSdk.DispatchEventsCallback {
            override fun onDispatch(statuses: List<EventStatus>) {
                offer(statuses)
            }
        }
        CxenseSdk.getInstance().setDispatchEventsCallback(callback)
        awaitClose {
            CxenseSdk.getInstance().setDispatchEventsCallback(null)
        }
    }

/**
 * Executes persisted query to Cxense API endpoint. You can find some popular endpoints in {@link CxenseConstants}
 *
 * @param url               API endpoint
 * @param persistentQueryId query id
 * @param data              data for sending as request body
 * @param <T>               response type
 * @return a result of query
 */
suspend fun <T : Any> getPersistedQuery(
    url: String,
    persistentQueryId: String,
    data: Any? = null
): T = await { callback ->
    CxenseSdk.getInstance().executePersistedQuery(url, persistentQueryId, data, callback)
}

private suspend fun <T : Any> await(block: (LoadCallback<T>) -> Unit): T =
    suspendCancellableCoroutine { cont ->
        block(object : LoadCallback<T> {
            override fun onError(throwable: Throwable) {
                cont.resumeWithException(throwable)
            }

            override fun onSuccess(data: T) {
                cont.resume(data)
            }
        })
    }