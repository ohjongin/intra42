package com.paulvarry.intra42.activities.home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.paulvarry.intra42.AppClass;
import com.paulvarry.intra42.R;
import com.paulvarry.intra42.adapters.ListAdapterEvents;
import com.paulvarry.intra42.api.ApiService;
import com.paulvarry.intra42.api.model.Events;
import com.paulvarry.intra42.bottomSheet.BottomSheetEventDialogFragment;
import com.paulvarry.intra42.ui.BasicFragmentCall;
import com.paulvarry.intra42.utils.AppSettings;
import com.paulvarry.intra42.utils.DateTool;
import retrofit2.Call;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeEventsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeEventsFragment extends BasicFragmentCall<Events, ListAdapterEvents> {

    private OnFragmentInteractionListener mListener;

    public HomeEventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeEventsFragment.
     */
    public static HomeEventsFragment newInstance() {
        return new HomeEventsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Nullable
    @Override
    public Call<List<Events>> getCall(ApiService apiService, int page) {
        AppClass app = (AppClass) getActivity().getApplication();
        int cursus = AppSettings.getAppCursus(app);
        int campus = AppSettings.getAppCampus(app);

        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, 2);
        String date = DateTool.getNowUTC() + "," + DateTool.getUTC(c.getTime());

        if (cursus != -1 && cursus != 0 && campus != -1 && campus != 0)
            return apiService.getEvent(campus, cursus, date, page);
        else if (cursus != -1 && cursus != 0)
            return apiService.getEventCursus(cursus, date, page);
        else if (campus != -1 && campus != 0)
            return apiService.getEventCampus(campus, date, page);
        else
            return apiService.getEvent(date, page);
    }

    @Override
    public void onItemClick(Events item) {
        BottomSheetEventDialogFragment bottomSheetDialogFragment = BottomSheetEventDialogFragment.newInstance(item);
        FragmentActivity activity = getActivity();
        if (activity != null)
            bottomSheetDialogFragment.show(activity.getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    @Override
    public ListAdapterEvents generateAdapter(List<Events> list) {
        Collections.sort(list, new Comparator<Events>() {
            @Override
            public int compare(Events o1, Events o2) {
                return o1.beginAt.after(o2.beginAt) ? 1 : -1;
            }
        });

        return new ListAdapterEvents(getContext(), list);
    }

    @Override
    public String getEmptyMessage() {
        return getString(R.string.event_nothing_found_for_your_campus_cursus);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
