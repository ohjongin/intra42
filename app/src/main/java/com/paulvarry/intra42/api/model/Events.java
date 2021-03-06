package com.paulvarry.intra42.api.model;

import android.content.Context;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.google.gson.annotations.SerializedName;
import com.paulvarry.intra42.R;
import com.paulvarry.intra42.api.IBaseItemSmall;

import java.util.Date;
import java.util.List;

public class Events implements IBaseItemSmall {

    private static final String API_ID = "id";
    private static final String API_NAME = "name";
    private static final String API_DESCRIPTION = "description";
    private static final String API_LOCATION = "location";
    private static final String API_KIND = "kind";
    private static final String API_MAX_PEOPLE = "max_people";
    private static final String API_NBR_SUBSCRIBERS = "nbr_subscribers";
    private static final String API_BEGIN_AT = "begin_at";
    private static final String API_END_AT = "end_at";
    private static final String API_CAMPUS_IDS = "campus_ids";
    private static final String API_CURSUS_IDS = "cursus_ids";
    private static final String API_PROHIBITION_OF_CANCELLATION = "prohibition_of_cancellation";
    private static final String API_WAITLIST = "waitlist";
    private static final String API_THEMES = "themes";

    @SerializedName(API_ID)
    public int id;
    @SerializedName(API_NAME)
    public String name;
    @SerializedName(API_DESCRIPTION)
    public String description;
    @SerializedName(API_LOCATION)
    public String location;
    @Nullable
    @SerializedName(API_KIND)
    public EventKind kind;
    @SerializedName(API_MAX_PEOPLE)
    @Nullable
    public Integer maxPeople;
    @SerializedName(API_NBR_SUBSCRIBERS)
    public int nbrSubscribers;
    @SerializedName(API_BEGIN_AT)
    public Date beginAt;
    @SerializedName(API_END_AT)
    public Date endAt;
    @SerializedName(API_CAMPUS_IDS)
    public List<Integer> campus;
    @SerializedName(API_CURSUS_IDS)
    public List<Integer> cursus;
    /**
     * Number of hours before the event while a subscription can't be canceled
     */
    @SerializedName(API_PROHIBITION_OF_CANCELLATION)
    public Integer prohibitionOfCancellation;

    @Override
    public String getName(Context context) {
        return name;
    }

    @Override
    public String getSub(Context context) {
        return location;
    }

    @Override
    public boolean openIt(Context context) {
        return false;
    }

    @Override
    public int getId() {
        return id;
    }

    public Date getCancellationLimit() {
        if (prohibitionOfCancellation == null)
            return null;

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(beginAt);
        calendar.add(java.util.Calendar.HOUR, prohibitionOfCancellation * -1);

        return calendar.getTime();
    }

    public enum EventKind {
        @SerializedName("conference") CONFERENCE(R.string.event_kind_conf, R.color.tag_event_meet),
        @SerializedName("meet_up") MEET_UP(R.string.event_kind_meet_up, R.color.tag_event_meet),
        @SerializedName("extern") EXTERN(R.string.event_kind_extern, R.color.tag_event_default),
        @SerializedName("hackathon") HACKATHON(R.string.event_kind_hackathon, R.color.tag_event_speed_working),
        @SerializedName("workshop") WORKSHOP(R.string.event_kind_workshop, R.color.tag_event_speed_working),
        @SerializedName("event") EVENT(R.string.event_kind_event, R.color.tag_event_meet),
        @SerializedName("atelier") ATELIER(R.string.event_kind_atelier, R.color.tag_event_atelier),
        @SerializedName("other") OTHER(R.string.event_kind_other, R.color.tag_event_default),
        @SerializedName("association") ASSOCIATION(R.string.event_kind_association, R.color.tag_event_association),
        @SerializedName("partnership") PARTNERSHIP(R.string.event_kind_partnership, R.color.tag_event_speed_working),
        @SerializedName("challenge") CHALLENGE(R.string.event_kind_challenge, R.color.tag_event_speed_working),
        @SerializedName("speed_working") SPEED_WORKING(R.string.event_kind_speed_working, R.color.tag_event_speed_working);

        @StringRes
        private final int name;
        @ColorRes
        private final int color;

        EventKind(@StringRes int name, @ColorRes int color) {
            this.name = name;
            this.color = color;
        }

        @StringRes
        public int getName() {
            return name;
        }

        @ColorRes
        public int getColorRes() {
            return color;
        }

        @ColorInt
        public int getColorInt(Context context) {
            return context.getResources().getColor(color);
        }

        public String getString(Context context) {
            return context.getString(name);
        }
    }

}
