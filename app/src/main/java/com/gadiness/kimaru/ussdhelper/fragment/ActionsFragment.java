package com.gadiness.kimaru.ussdhelper.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.gadiness.kimaru.ussdhelper.R;
import com.gadiness.kimaru.ussdhelper.activity.MainActivity;
import com.gadiness.kimaru.ussdhelper.data.UssdDbHelper;
import com.gadiness.kimaru.ussdhelper.other.ApiSyncHelper;
import com.gadiness.kimaru.ussdhelper.other.AppPreferences;
import com.gadiness.kimaru.ussdhelper.other.WriteToLog;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ActionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ActionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActionsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    WriteToLog writeToLog = new MainActivity().myLog;
    String TAG = "ACTIONS ";

    Button buttonGetPhones, buttonRunUssd, buttonUpload;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ActionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ActionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActionsFragment newInstance(String param1, String param2) {
        ActionsFragment fragment = new ActionsFragment();
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
        // Inflate the layout for this fragment
        MainActivity.backFragment = new HomeFragment();
        View view  = inflater.inflate(R.layout.fragment_actions, container, false);
        Button buttonGetPhones = (Button) view.findViewById(R.id.buttonGetPhones);
        Button buttonRunUssd = (Button) view.findViewById(R.id.buttonRunUssd);
        Button buttonUpload = (Button) view.findViewById(R.id.buttonUpload);
        Button buttonGetQueues = (Button) view.findViewById(R.id.buttonGetQueues);

        //Listeners
        buttonGetPhones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ApiSyncHelper(getContext()).schedulePhoneQueueTask();
                Toast.makeText(getContext(), "Get Phones from the URL ", Toast.LENGTH_SHORT).show();
            }
        });

        buttonRunUssd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add action to run tehe USSDs
                Toast.makeText(getContext(), "Starting the USSD task", Toast.LENGTH_SHORT).show();
                final Handler handler = new Handler(Looper.getMainLooper());
                Timer timer = new Timer();
                TimerTask getPhoneTask = new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (new UssdDbHelper(getContext()).getPhoneQueus().size() > 0){
                                    android.util.Log.d(MainActivity.TAG, "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                                    dialNumber(new AppPreferences(getContext()).getUssdCode());
                                    android.util.Log.d(MainActivity.TAG, "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                                }


                            }
                        });
                    }
                };
                timer.schedule(getPhoneTask, 0, 60*1000 / 3); //every 20 seconds
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("USSD", "Uploading Messages to the Cloud");
                writeToLog.log(TAG + " - Starting backgound job to Upload messages " );
                new ApiSyncHelper(getContext()).scheduleUssdUploadTask();
                Toast.makeText(getContext(), "Uploading Messages to the Cloud", Toast.LENGTH_SHORT).show();
            }
        });



        buttonGetQueues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ApiSyncHelper(getContext()).getQueuesTask();
            }
        });

        return view;
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

    public void dialNumber(String ussd){
        if (ussd.endsWith("#")){
            ussd = ussd.substring(0, ussd.length() - 1);
        }
        String ussdCode = ussd + Uri.encode("#");
        startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + ussdCode)));
    }
}
