package com.cxense.rxjava2adapter

import com.cxense.cxensesdk.CxenseConfiguration
import com.cxense.cxensesdk.CxenseSdk
import com.cxense.cxensesdk.LoadCallback
import com.cxense.cxensesdk.model.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Tracks a click for the given click-url
 *
 * @param url the click-url
 * @return a {@link Completable} that is complete when the function finished successfully
 */
fun trackClick(url: String): Completable =
    createCompletable { loadCallback ->
        CxenseSdk.getInstance().trackClick(url, loadCallback)
    }

/**
 * Tracks an url click for the given item
 *
 * @param item the item that contains the click-url
 * @return a {@link Completable} that is complete when the function finished successfully
 */
fun trackClick(item: WidgetItem): Completable =
    createCompletable { loadCallback ->
        CxenseSdk.getInstance().trackClick(item, loadCallback)
    }

/**
 * @param widgetId      the widget id
 * @param widgetContext the WidgetContext
 * @param user          custom user
 * @return a {@link Single} that emits list of widgets
 */
fun loadWidgetRecommendations(
    widgetId: String,
    widgetContext: WidgetContext,
    user: ContentUser? = null,
    tag: String? = null,
    prnd: String? = null
): Single<List<WidgetItem>> =
    createSingle { loadCallback ->
        CxenseSdk.getInstance()
            .loadWidgetRecommendations(widgetId, widgetContext, user, tag, prnd, loadCallback)
    }

/**
 * Retrieves the user id used by this SDK.
 *
 * @return a {@link Single} that emits current user id
 */
fun getUserId(): Single<String> =
    Single.fromCallable {
        CxenseSdk.getInstance().userId
    }

/**
 * Sets the user id used by this SDK. Must be at least 16 characters long.
 * Allowed characters are: A-Z, a-z, 0-9, "_", "-", "+" and ".".
 *
 * @param id new user id
 * @return a {@link Completable} that is complete when the function finished successfully
 */
fun setUserId(id: String): Completable =
    Completable.fromAction {
        CxenseSdk.getInstance().userId = id
    }

/**
 * Retrieves the default user id for SDK.
 *
 * @return a {@link Single} that emits advertising ID (if available)
 */
fun getDefaultUserId(): Single<String> =
    Single.fromCallable {
        CxenseSdk.getInstance().defaultUserId
            ?: throw IllegalStateException("Advertising ID is not available at this moment")
    }

/**
 * Retrieves whether the user has limit ad tracking enabled or not.
 *
 * @return a {@link Single} that emits true if the user has limit ad tracking enabled or false else.
 */
fun isLimitAdTrackingEnabled(): Single<Boolean> =
    Single.fromCallable {
        CxenseSdk.getInstance().limitAdTrackingEnabled
    }

/**
 * Gets Cxense SDK configuration
 *
 * @return a {@link Single} that emits sdk configuration
 */
fun getConfiguration(): Single<CxenseConfiguration> =
    Single.fromCallable {
        CxenseSdk.getInstance().configuration
    }

/**
 * Asynchronously retrieves a list of all segments where the specified user is a member
 *
 * @param identities   a collection of user identifiers for a single user to retrieve segments for
 * @param siteGroupIds the collection of site groups to retrieve segments for
 * @return a {@link Single} that emits list of user segment ids
 */
fun getUserSegmentIds(
    identities: List<UserIdentity>,
    siteGroupIds: List<String>
): Single<List<String>> =
    createSingle { loadCallback ->
        CxenseSdk.getInstance().getUserSegmentIds(identities, siteGroupIds, loadCallback)
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
 * @return a {@link Single} that emits an {@link User} object
 */
fun getUser(
    identity: UserIdentity,
    groups: List<String>? = null,
    recent: Boolean? = null,
    identityTypes: List<String>? = null
): Single<User> =
    createSingle { loadCallback ->
        CxenseSdk.getInstance().getUser(identity, groups, recent, identityTypes, loadCallback)
    }

/**
 * Asynchronously retrieves the external data associated with a given user
 *
 * @param id   identifier for the user. Use 'null' if you want match all users of provided type.
 * @param type the customer identifier type
 * @param filter a traffic filter of type user-external with required group and optional item/items specified
 * @return a {@link Single} that emits list of {@link UserExternalData} objects
 */
fun getUserExternalData(
    type: String,
    id: String? = null,
    filter: String? = null
): Single<List<UserExternalData>> =
    createSingle { loadCallback ->
        CxenseSdk.getInstance().getUserExternalData(type, id, filter, loadCallback)
    }

/**
 * Asynchronously sets the external data associated with a given user
 *
 * @param userExternalData external data associated with a user
 * @return a {@link Completable} that is complete when the function finished successfully
 */
fun setUserExternalData(userExternalData: UserExternalData): Completable =
    createCompletable { loadCallback ->
        CxenseSdk.getInstance().setUserExternalData(userExternalData, loadCallback)
    }

/**
 * Asynchronously deletes the external data associated with a given user
 *
 * @param identity user identifier with type and id
 * @return a {@link Completable} that is complete when the function finished successfully
 */
fun deleteUserExternalData(identity: UserIdentity): Completable =
    createCompletable { loadCallback ->
        CxenseSdk.getInstance().deleteUserExternalData(identity, loadCallback)
    }

/**
 * Asynchronously retrieves a registered external identity mapping for a Cxense identifier
 *
 * @param cxenseId the Cxense identifier of the user.
 * @param type     the identity mapping type (customer identifier type) that contains the mapping.
 * @return a {@link Single} that emits an {@link UserIdentity} object
 */
fun getUserExternalLink(cxenseId: String, type: String): Single<UserIdentity> =
    createSingle { loadCallback ->
        CxenseSdk.getInstance().getUserExternalLink(cxenseId, type, loadCallback)
    }

/**
 * Asynchronously register a new identity-mapping for the given user
 *
 * @param cxenseId the Cxense identifier to map this user to
 * @param identity user identifier with type and id
 * @return a {@link Single} that emits an {@link UserIdentity} object
 */
fun addUserExternalLink(cxenseId: String, identity: UserIdentity): Single<UserIdentity> =
    createSingle { loadCallback ->
        CxenseSdk.getInstance().addUserExternalLink(cxenseId, identity, loadCallback)
    }

/**
 * Push events to sending queue.
 *
 * @param events the events that should be pushed.
 * @return a {@link Completable} that is complete when the function finished successfully
 */
fun pushEvents(vararg events: Event): Completable =
    Completable.fromAction {
        CxenseSdk.getInstance().pushEvents(*events)
    }

/**
 * Tracks active time for the given page view event. The active time will be calculated
 * as the time between this call and the trackEvent call.
 *
 * @param eventId the event to report active time for.
 * @return a {@link Completable} that is complete when the function finished successfully
 */
fun trackActiveTime(eventId: String): Completable =
    Completable.fromAction {
        CxenseSdk.getInstance().trackActiveTime(eventId)
    }

/**
 * Tracks active time for the given page view event.
 *
 * @param eventId    the event to report active time for.
 * @param activeTime the active time in seconds.
 * @return a {@link Completable} that is complete when the function finished successfully
 */
fun trackActiveTime(eventId: String, activeTime: Long): Completable =
    Completable.fromAction {
        CxenseSdk.getInstance().trackActiveTime(eventId, activeTime)
    }

/**
 * Returns the default user used by all widgets if the user hasn't been specifically set on a widget
 *
 * @return a {@link Single} that emits the default {@link ContentUser}
 */
fun getDefaultContentUser(): Single<ContentUser> =
    Single.fromCallable {
        CxenseSdk.getInstance().defaultContentUser
    }

/**
 * Forces sending events from queue to server.
 *
 * @return a {@link Completable} that is complete when the function finished successfully
 */
fun flushEventQueue(): Completable =
    Completable.fromAction {
        CxenseSdk.getInstance().flushEventQueue()
    }

/**
 * Returns current event queue status
 *
 * @return a {@link Single} that emits a {@link QueueStatus} object
 */
fun getQueueStatus(): Single<QueueStatus> =
    Single.fromCallable {
        CxenseSdk.getInstance().queueStatus
    }

/**
 * Listener for events sending statuses
 *
 * @return an {@link Observable} that emits statuses at each events sending
 */
fun getDispatchEventsStatuses(): Observable<List<EventStatus>> =
    Observable.create { emitter ->
        CxenseSdk.getInstance()
            .setDispatchEventsCallback(object : CxenseSdk.DispatchEventsCallback {
                override fun onDispatch(statuses: List<EventStatus>) = emitter.onNext(statuses)
            })
        emitter.setCancellable { CxenseSdk.getInstance().setDispatchEventsCallback(null) }
    }

/**
 * Executes persisted query to Cxense API endpoint. You can find some popular endpoints in {@link CxenseConstants}
 *
 * @param url               API endpoint
 * @param persistentQueryId query id
 * @param data              data for sending as request body
 * @param <T>               response type
 * @return a {@link Single} that emits result of query
 */
fun <T : Any> getPersistedQuerySingle(
    url: String,
    persistentQueryId: String,
    data: Any? = null
): Single<T> =
    createSingle { loadCallback ->
        CxenseSdk.getInstance().executePersistedQuery(url, persistentQueryId, data, loadCallback)
    }

/**
 * Executes persisted query to Cxense API endpoint. You can find some popular endpoints in {@link CxenseConstants}
 *
 * @param url               API endpoint
 * @param persistentQueryId query id
 * @param data              data for sending as request body
 * @return a {@link Completable} that is complete when the function finished successfully
 */
fun getPersistedQueryCompletable(
    url: String,
    persistentQueryId: String,
    data: Any? = null
): Completable =
    createCompletable { loadCallback ->
        CxenseSdk.getInstance().executePersistedQuery(url, persistentQueryId, data, loadCallback)
    }

private fun <T : Any> createSingle(func: (LoadCallback<T>) -> Unit): Single<T> =
    Single.create { emitter ->
        func(object : LoadCallback<T> {
            override fun onSuccess(data: T) {
                emitter.onSuccess(data)
            }

            override fun onError(throwable: Throwable) {
                emitter.tryOnError(throwable)
            }
        })
    }

private fun createCompletable(func: (LoadCallback<Void>) -> Unit): Completable =
    Completable.create { emitter ->
        func(object : LoadCallback<Void> {
            override fun onSuccess(data: Void) {
                emitter.onComplete()
            }

            override fun onError(throwable: Throwable) {
                emitter.tryOnError(throwable)
            }
        })
    }
