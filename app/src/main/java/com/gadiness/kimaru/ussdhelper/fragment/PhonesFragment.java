package com.gadiness.kimaru.ussdhelper.fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gadiness.kimaru.ussdhelper.R;
import com.gadiness.kimaru.ussdhelper.activity.MainActivity;
import com.gadiness.kimaru.ussdhelper.data.UssdDbHelper;
import com.gadiness.kimaru.ussdhelper.listadapters.PhoneNumberListAdapter;
import com.gadiness.kimaru.ussdhelper.mzigos.PhoneQueue;
import com.gadiness.kimaru.ussdhelper.mzigos.Queue;
import com.gadiness.kimaru.ussdhelper.other.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhonesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhonesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhonesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView textshow;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<PhoneQueue> phoneQueues = new ArrayList<>();
    private RecyclerView recyclerView;
    private PhoneNumberListAdapter rAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActionMode actionMode;
    private ActionModeCallback actionModeCallback;

    public Queue queue = null;

    private OnFragmentInteractionListener mListener;

    public PhonesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhonesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhonesFragment newInstance(String param1, String param2) {
        PhonesFragment fragment = new PhonesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.backFragment = new QueuesFragment();
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_phones, container, false);
        textshow = (TextView) v.findViewById(R.id.textShow);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getExams();
            }
        });
        rAdapter = new PhoneNumberListAdapter(this.getContext(), phoneQueues, new PhoneNumberListAdapter.PhoneNumberListAdapterListener() {
            @Override
            public void onIconClicked(int position) {
                PhoneQueue phoneQueue = phoneQueues.get(position);
            }

            @Override
            public void onIconImportantClicked(int position) {
                PhoneQueue phoneQueue = phoneQueues.get(position);
            }

            @Override
            public void onMessageRowClicked(int position) {
                PhoneQueue phoneQueue = phoneQueues.get(position);
            }

            @Override
            public void onRowLongClicked(int position) {
                PhoneQueue phoneQueue = phoneQueues.get(position);
            }

        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(rAdapter);
        actionModeCallback = new ActionModeCallback();
        swipeRefreshLayout.post(
                new Runnable(){
                    @Override
                    public void run(){
                        getExams();
                    }
                }
        );

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            Toast.makeText(getContext(), "Values of Enabled", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(getContext(), "Values of Enabled", Toast.LENGTH_SHORT).show();
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        rAdapter.toggleSelection(position);
        int count = rAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }
    /*
    *  Choose a random
    *  Color
     */
    private int getRandomMaterialColor(String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = getResources().getIdentifier("mdcolor_" + typeColor, "array", getContext().getPackageName());

        if (arrayId != 0) {
            TypedArray colors = getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }


    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);

            // disable swipe refresh if action mode is enabled
            swipeRefreshLayout.setEnabled(false);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
//                case R.id.action_delete:
//                    // delete all the selected messages
//                    deleteMessages();
//                    mode.finish();
//                    return true;
//
//                default:
//                    return false;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            rAdapter.clearSelections();
            swipeRefreshLayout.setEnabled(true);
            actionMode = null;
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    rAdapter.resetAnimationIndex();
                    // mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    // deleting the messages from recycler view
    private void deleteMessages() {
        rAdapter.resetAnimationIndex();
        List<Integer> selectedItemPositions =
                rAdapter.getSelectedItems();
        for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
            rAdapter.removeData(selectedItemPositions.get(i));
        }
        rAdapter.notifyDataSetChanged();
    }
    private void getExams() {
        swipeRefreshLayout.setRefreshing(true);

        phoneQueues.clear();
        Toast.makeText(getContext(), "Refreshing phone list", Toast.LENGTH_SHORT).show();

        // clear the registrations
        try {
            // get the registrations
            UssdDbHelper ussdDbHelper = new UssdDbHelper(getContext());
            List<PhoneQueue> phoneQueueList = new ArrayList<>();

            if (queue != null) {
                phoneQueueList = ussdDbHelper.getPhoneQueueByQueueId(queue.getId());
            }else{
                phoneQueueList = ussdDbHelper.getPhoneQueus();
            }
            for (PhoneQueue phone:phoneQueueList){
                phone.setColor(getRandomMaterialColor("400"));
                phoneQueues.add(phone);
            }
            rAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        } catch (Exception error){
            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            textshow.setText( error.getMessage());

        }
        swipeRefreshLayout.setRefreshing(false);
    }
}
