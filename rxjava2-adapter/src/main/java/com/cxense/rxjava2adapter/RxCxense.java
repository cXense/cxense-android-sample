package com.cxense.rxjava2adapter;

import androidx.annotation.NonNull;

import com.cxense.cxensesdk.CxenseConfiguration;
import com.cxense.cxensesdk.CxenseConstants;
import com.cxense.cxensesdk.CxenseSdk;
import com.cxense.cxensesdk.EventStatus;
import com.cxense.cxensesdk.LoadCallback;
import com.cxense.cxensesdk.QueueStatus;
import com.cxense.cxensesdk.model.ContentUser;
import com.cxense.cxensesdk.model.Event;
import com.cxense.cxensesdk.model.User;
import com.cxense.cxensesdk.model.UserExternalData;
import com.cxense.cxensesdk.model.UserIdentity;
import com.cxense.cxensesdk.model.WidgetContext;
import com.cxense.cxensesdk.model.WidgetItem;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class RxCxense {
    /**
     * Tracks a click for the given click-url
     *
     * @param url the click-url
     * @return a {@link Completable} that is complete when the function finished successfully
     */
    @SuppressWarnings({"UnusedDeclaration", "WeakerAccess"}) // Public API.
    @NonNull
    public static Completable trackClick(@NonNull String url) {
        return Completable.create(emitter -> CxenseSdk.getInstance().trackClick(url, new LoadCallback() {
            @Override
            public void onSuccess(Object data) {
                emitter.onComplete();
            }

            @Override
            public void onError(Throwable throwable) {
                emitter.onError(throwable);
            }
        }));
    }

    /**
     * Tracks an url click for the given item
     *
     * @param item the item that contains the click-url
     * @return a {@link Completable} that is complete when the function finished successfully
     */
    @SuppressWarnings({"UnusedDeclaration", "WeakerAccess"}) // Public API.
    @NonNull
    public static Completable trackClick(@NonNull WidgetItem item) {
        return Completable.create(emitter -> CxenseSdk.getInstance().trackClick(item, new LoadCallback() {
            @Override
            public void onSuccess(Object data) {
                emitter.onComplete();
            }

            @Override
            public void onError(Throwable throwable) {
                emitter.onError(throwable);
            }
        }));
    }

    /**
     * Fetch async a list of {@link com.cxense.cxensesdk.model.WidgetItem items} for the given {@link WidgetContext} and widget id
     *
     * @param widgetId      the widget id
     * @param widgetContext the WidgetContext
     * @return a {@link Single} that emits list of widgets
     */
    @SuppressWarnings({"UnusedDeclaration", "WeakerAccess"}) // Public API.
    @NonNull
    public static Single<List<WidgetItem>> loadWidgetRecommendations(final String widgetId, final WidgetContext widgetContext) {
        return Single.create(emitter -> CxenseSdk.getInstance().loadWidgetRecommendations(widgetId, widgetContext, new LoadCallback<List<WidgetItem>>() {
            @Override
            public void onSuccess(List<WidgetItem> data) {
                emitter.onSuccess(data);
            }

            @Override
            public void onError(Throwable throwable) {
                emitter.onError(throwable);
            }
        }));
    }

    /**
     * @param widgetId      the widget id
     * @param widgetContext the WidgetContext
     * @param user          custom user
     * @return a {@link Single} that emits list of widgets
     */
    @SuppressWarnings({"UnusedDeclaration", "WeakerAccess"}) // Public API.
    @NonNull
    public static Single<List<WidgetItem>> loadWidgetRecommendations(final String widgetId, final WidgetContext widgetContext, ContentUser user, String tag, String prnd) {
        return Single.create(emitter -> CxenseSdk.getInstance().loadWidgetRecommendations(widgetId, widgetContext, user, tag, prnd, new LoadCallback<List<WidgetItem>>() {
            @Override
            public void onSuccess(List<WidgetItem> data) {
                emitter.onSuccess(data);
            }

            @Override
            public void onError(Throwable throwable) {
                emitter.onError(throwable);
            }
        }));
    }

    /**
     * Retrieves the user id used by this SDK.
     *
     * @return a {@link Single} that emits current user id
     */
    @SuppressWarnings({"UnusedDeclaration", "WeakerAccess"}) // Public API.
    @NonNull
    public static Single<String> getUserId() {
        return Single.fromCallable(() -> CxenseSdk.getInstance().getUserId());
    }

    /**
     * Sets the user id used by this SDK. Must be at least 16 characters long.
     * Allowed characters are: A-Z, a-z, 0-9, "_", "-", "+" and ".".
     *
     * @param id new user id
     * @return a {@link Completable} that is complete when the function finished successfully
     */
    @SuppressWarnings({"UnusedDeclaration", "WeakerAccess"}) // Public API.
    @NonNull
    public static Completable setUserId(@NonNull String id) {
        return Completable.fromAction(() -> CxenseSdk.getInstance().setUserId(id));
    }

    /**
     * Retrieves the default user id for SDK.
     *
     * @return a {@link Single} that emits advertising ID (if available)
     */
    @SuppressWarnings({"UnusedDeclaration", "WeakerAccess"}) // Public API.
    @NonNull
    public static Single<String> getDefaultUserId() {
        return Single.fromCallable(() -> {
            String id = CxenseSdk.getInstance().getDefaultUserId();
            if (id == null)
                throw new IllegalStateException("Advertising ID is not available at this moment");
            return id;
        });
    }

    /**
     * Retrieves whether the user has limit ad tracking enabled or not.
     *
     * @return a {@link Single} that emits true if the user has limit ad tracking enabled or false else.
     */
    @SuppressWarnings({"UnusedDeclaration", "WeakerAccess"}) // Public API.
    @NonNull
    public static Single<Boolean> isLimitAdTrackingEnabled() {
        return Single.fromCallable(() -> CxenseSdk.getInstance().isLimitAdTrackingEnabled());
    }

    /**
     * Gets Cxense SDK configuration
     *
     * @return a {@link Single} that emits sdk configuration
     */
    @SuppressWarnings({"UnusedDeclaration", "WeakerAccess"}) // Public API.
    @NonNull
    public static Single<CxenseConfiguration> getConfiguration() {
        return Single.fromCallable(() -> CxenseSdk.getInstance().getConfiguration());
    }

    /**
     * Asynchronously retrieves a list of all segments where the specified user is a member
     *
     * @param identities   a list of user identifiers for a single user to retrieve segments for
     * @param siteGroupIds the list of site groups to retrieve segments for
     * @return a {@link Single} that emits list of user segment ids
     */
    @SuppressWarnings({"UnusedDeclaration", "WeakerAccess"}) // Public API.
    @NonNull
    public static Single<List<String>> getUserSegmentIds(@NonNull List<UserIdentity> identities, @NonNull List<String> siteGroupIds) {
        return Single.create(emitter -> CxenseSdk.getInstance().getUserSegmentIds(identities, siteGroupIds, new LoadCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> data) {
                emitter.onSuccess(data);
            }

            @Override
            public void onError(Throwable throwable) {
                emitter.onError(throwable);
            }
        }));
    }

    /**
     * Asynchronously retrieves a suitably authorized slice of a given user's interest profile
     *
     * @param identity user identifier with type and id
     * @return a {@link Single} that emits an {@link User} object
     */
    @SuppressWarnings({"UnusedDeclaration", "WeakerAccess"}) // Public API.
    @NonNull
    public static Single<User> getUser(@NonNull UserIdentity identity) {
        return Single.create(emitter -> CxenseSdk.getInstance().getUser(identity, new LoadCallback<User>() {
            @Override
            public void onSuccess(User data) {
                emitter.onSuccess(data);
            }

            @Override
            public void onError(Throwable throwable) {
                emitter.onError(throwable);
            }
        }));
    }

    /**
     * Asynchronously retrieves a suitably authorized slice of a given user's interest profile
     *
     * @param identity      user identifier with type and id
     * @param groups        a list of strings that specify profile item groups to keep in the returned profile.
     *                      If not specified, all groups available for the user will be returned
     * @param recent        flag whether to only return the most recent user profile information. This can be used to
     *                      return quickly if response time is important
     * @param identityTypes a list of external customer identifier types. If an external customer identifier exists for
     *                      the user, it will be included in the response
     * @return a {@link Single} that emits an {@link User} object
     */
    @SuppressWarnings({"UnusedDeclaration", "WeakerAccess", "SameParameterValue"}) // Public API.
    @NonNull
    public static Single<User> getUser(@NonNull UserIdentity identity, List<String> groups, Boolean recent, List<String> identityTypes) {
        return Single.create(emitter -> CxenseSdk.getInstance().getUser(identity, groups, recent, identityTypes, new LoadCallback<User>() {
            @Override
            public void onSuccess(User data) {
                emitter.onSuccess(data);
            }

            @Override
            public void onError(Throwable throwable) {
                emitter.onError(throwable);
            }
        }));
    }

    /**
     * Asynchronously retrieves the external data associated with a given user type
     *
     * @param type the customer identifier type
     * @return a {@link Single} that emits list of {@link UserExternalData} objects
     */
    @SuppressWarnings({"UnusedDeclaration", "WeakerAccess"}) // Public API.
    @NonNull
    public static Single<List<UserExternalData>> getUserExternalData(@NonNull String type) {
        return Single.create(emitter -> CxenseSdk.getInstance().getUserExternalData(type, new LoadCallback<List<UserExternalData>>() {
            @Override
            public void onSuccess(List<UserExternalData> data) {
                emitter.onSuccess(data);
            }

            @Override
            public void onError(Throwable throwable) {
                emitter.onError(throwable);
            }
        }));
    }

    /**
     * Asynchronously retrieves the external data associated with a given user
     *
     * @param id   identifier for the user. Use 'null' if you want match all users of provided type.
     * @param type the customer identifier type
     * @return a {@link Single} that emits list of {@link UserExternalData} objects
     */
    @SuppressWarnings({"UnusedDeclaration", "WeakerAccess", "SameParameterValue"}) // Public API.
    @NonNull
    public static Single<List<UserExternalData>> getUserExternalData(String id, @NonNull String type) {
        return Single.create(emitter -> CxenseSdk.getInstance().getUserExternalData(id, type, new LoadCallback<List<UserExternalData>>() {
            @Override
            public void onSuccess(List<UserExternalData> data) {
                emitter.onSuccess(data);
            }

            @Override
            public void onError(Throwable throwable) {
                emitter.onError(throwable);
            }
        }));
    }

    /**
     * Asynchronously sets the external data associated with a given user
     *
     * @param userExternalData external data associated with a user
     * @return a {@link Completable} that is complete when the function finished successfully
     */
    @SuppressWarnings({"UnusedDeclaration", "WeakerAccess", "SameParameterValue"}) // Public API.
    @NonNull
    public static Completable setUserExternalData(@NonNull UserExternalData userExternalData) {
        return Completable.create(emitter -> CxenseSdk.getInstance().setUserExternalData(userExternalData, new LoadCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                emitter.onComplete();
            }

            @Override
            public void onError(Throwable throwable) {
                emitter.onError(throwable);
            }
        }));
    }

    /**
     * Asynchronously deletes the external data associated with a given user
     *
     * @param identity user identifier with type and id
     * @return a {@link Completable} that is complete when the function finished successfully
     */
    @SuppressWarnings({"UnusedDeclaration", "WeakerAccess"}) // Public API.
    @NonNull
    public static Completable deleteUserExternalData(@NonNull UserIdentity identity) {
        return Completable.create(emitter -> CxenseSdk.getInstance().deleteUserExternalData(identity, new LoadCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                emitter.onComplete();
            }

            @Override
            public void onError(Throwable throwable) {
                emitter.onError(throwable);
            }
        }));
    }

    /**
     * Asynchronously retrieves a registered external identity mapping for a Cxense identifier
     *
     * @param cxenseId the Cxense identifier of the user.
     * @param type     the identity mapping type (customer identifier type) that contains the mapping.
     * @return a {@link Single} that emits an {@link UserIdentity} object
     */
    @SuppressWarnings({"UnusedDeclaration", "WeakerAccess"}) // Public API.
    @NonNull
    public static Single<UserIdentity> getUserExternalLink(@NonNull String cxenseId, @NonNull String type) {
        return Single.create(emitter -> CxenseSdk.getInstance().getUserExternalLink(cxenseId, type, new LoadCallback<UserIdentity>() {
            @Override
            public void onSuccess(UserIdentity data) {
                emitter.onSuccess(data);
            }

            @Override
            public void onError(Throwable throwable) {
                emitter.onError(throwable);
            }
        }));
    }

    /**
     * Asynchronously register a new identity-mapping for the given user
     *
     * @param cxenseId the Cxense identifier to map this user to
     * @param identity user identifier with type and id
     * @return a {@link Single} that emits an {@link UserIdentity} object
     */
    @SuppressWarnings({"UnusedDeclaration", "WeakerAccess"}) // Public API.
    @NonNull
    public static Single<UserIdentity> setUserExternalLink(@NonNull String cxenseId, @NonNull UserIdentity identity) {
        return Single.create(emitter -> CxenseSdk.getInstance().setUserExternalLink(cxenseId, identity, new LoadCallback<UserIdentity>() {
            @Override
            public void onSuccess(UserIdentity data) {
                emitter.onSuccess(data);
            }

            @Override
            public void onError(Throwable throwable) {
                emitter.onError(throwable);
            }
        }));
    }

    /**
     * Push events to sending queue.
     *
     * @param events the events that should be pushed.
     * @return a {@link Completable} that is complete when the function finished successfully
     */
    @SuppressWarnings({"UnusedDeclaration", "WeakerAccess"}) // Public API.
    @NonNull
    public static Completable pushEvents(@NonNull Event... events) {
        return Completable.fromAction(() -> CxenseSdk.getInstance().pushEvents(events));
    }

    /**
     * Tracks active time for the given page view event. The active time will be calculated
     * as the time between this call and the trackEvent call.
     *
     * @param eventId the event to report active time for.
     * @return a {@link Completable} that is complete when the function finished successfully
     */
    @SuppressWarnings({"UnusedDeclaration", "WeakerAccess"}) // Public API.
    @NonNull
    public static Completable trackActiveTime(String eventId) {
        return Completable.fromAction(() -> CxenseSdk.getInstance().trackActiveTime(eventId));
    }

    /**
     * Tracks active time for the given page view event.
     *
     * @param eventId    the event to report active time for.
     * @param activeTime the active time in seconds.
     * @return a {@link Completable} that is complete when the function finished successfully
     */
    @SuppressWarnings({"UnusedDeclaration", "WeakerAccess", "SameParameterValue"}) // Public API.
    @NonNull
    public static Completable trackActiveTime(final String eventId, final long activeTime) {
        return Completable.fromAction(() -> CxenseSdk.getInstance().trackActiveTime(eventId, activeTime));
    }

    /**
     * Returns the default user used by all widgets if the user hasn't been specifically set on a widget
     *
     * @return a {@link Single} that emits the default {@link ContentUser}
     */
    @SuppressWarnings({"UnusedDeclaration", "WeakerAccess"}) // Public API.
    @NonNull
    public static Single<ContentUser> getDefaultUser() {
        return Single.fromCallable(() -> CxenseSdk.getInstance().getDefaultUser());
    }

    /**
     * Forces sending events from queue to server.
     *
     * @return a {@link Completable} that is complete when the function finished successfully
     */
    @SuppressWarnings({"UnusedDeclaration", "WeakerAccess"}) // Public API.
    @NonNull
    public static Completable flushEventQueue() {
        return Completable.fromAction(() -> CxenseSdk.getInstance().flushEventQueue());
    }

    /**
     * Returns current event queue status
     *
     * @return a {@link Single} that emits a {@link QueueStatus} object
     */
    @SuppressWarnings({"UnusedDeclaration", "WeakerAccess"}) // Public API.
    @NonNull
    public static Single<QueueStatus> getQueueStatus() {
        return Single.fromCallable(() -> CxenseSdk.getInstance().getQueueStatus());
    }

    /**
     * Listener for events sending statuses
     *
     * @return an {@link Observable} that emits statuses at each events sending
     */
    @SuppressWarnings({"UnusedDeclaration", "WeakerAccess"}) // Public API.
    @NonNull
    public static Observable<List<EventStatus>> getDispatchEventsStatuses() {
        return Observable.create(emitter -> {
                    CxenseSdk.getInstance().setDispatchEventsCallback(emitter::onNext);
                    emitter.setCancellable(() -> CxenseSdk.getInstance().setDispatchEventsCallback(null));
                }
        );
    }

    /**
     * Executes persisted query to Cxense API endpoint. You can find some popular endpoints in {@link CxenseConstants}
     *
     * @param url               API endpoint
     * @param persistentQueryId query id
     * @param <T>               response type
     * @return a {@link Single} that emits result of query
     */
    @SuppressWarnings({"UnusedDeclaration", "WeakerAccess"}) // Public API.
    @NonNull
    public <T> Single<T> getPersistedQuerySingle(String url, String persistentQueryId) {
        return Single.create(emitter -> CxenseSdk.getInstance().executePersistedQuery(url, persistentQueryId, new LoadCallback<T>() {
            @Override
            public void onSuccess(T data) {
                emitter.onSuccess(data);
            }

            @Override
            public void onError(Throwable throwable) {
                emitter.onError(throwable);
            }
        }));
    }

    /**
     * Executes persisted query to Cxense API endpoint. You can find some popular endpoints in {@link CxenseConstants}
     *
     * @param url               API endpoint
     * @param persistentQueryId query id
     * @return a {@link Completable} that is complete when the function finished successfully
     */
    @SuppressWarnings({"UnusedDeclaration", "WeakerAccess"}) // Public API.
    @NonNull
    public Completable getPersistedQueryCompletable(String url, String persistentQueryId) {
        return Completable.create(emitter -> CxenseSdk.getInstance().executePersistedQuery(url, persistentQueryId, new LoadCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                emitter.onComplete();
            }

            @Override
            public void onError(Throwable throwable) {
                emitter.onError(throwable);
            }
        }));
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
    @SuppressWarnings({"UnusedDeclaration", "WeakerAccess"}) // Public API.
    @NonNull
    public <T> Single<T> getPersistedQuerySingle(String url, String persistentQueryId, Object data) {
        return Single.create(emitter -> CxenseSdk.getInstance().executePersistedQuery(url, persistentQueryId, data, new LoadCallback<T>() {
            @Override
            public void onSuccess(T data) {
                emitter.onSuccess(data);
            }

            @Override
            public void onError(Throwable throwable) {
                emitter.onError(throwable);
            }
        }));
    }

    /**
     * Executes persisted query to Cxense API endpoint. You can find some popular endpoints in {@link CxenseConstants}
     *
     * @param url               API endpoint
     * @param persistentQueryId query id
     * @param data              data for sending as request body
     * @return a {@link Completable} that is complete when the function finished successfully
     */
    @SuppressWarnings({"UnusedDeclaration", "WeakerAccess"}) // Public API.
    @NonNull
    public Completable getPersistedQueryCompletable(String url, String persistentQueryId, Object data) {
        return Completable.create(emitter -> CxenseSdk.getInstance().executePersistedQuery(url, persistentQueryId, data, new LoadCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                emitter.onComplete();
            }

            @Override
            public void onError(Throwable throwable) {
                emitter.onError(throwable);
            }
        }));
    }
}
