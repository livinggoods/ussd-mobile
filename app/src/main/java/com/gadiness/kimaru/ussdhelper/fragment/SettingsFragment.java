package com.gadiness.kimaru.ussdhelper.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gadiness.kimaru.ussdhelper.R;
import com.gadiness.kimaru.ussdhelper.activity.MainActivity;
import com.gadiness.kimaru.ussdhelper.other.AppPreferences;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    AppPreferences appPreferences;
    RelativeLayout cloudRView, appUssdCode, appUssdEndpoint, appPhoneEndpoint, appPhoneJson, appApiView;
    TextView appUrl, baseUssdCode, baseUssdEndpoint, basePhoneEndpoint, basePhoneJsonRoot, baseApiVersion;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        appPreferences = new AppPreferences(getContext());
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        appUrl = (TextView) view.findViewById(R.id.appUrl);
        baseUssdCode = (TextView) view.findViewById(R.id.baseUssdCode);
        baseUssdEndpoint = (TextView) view.findViewById(R.id.baseUssdEndpoint);
        basePhoneEndpoint = (TextView) view.findViewById(R.id.basePhoneEndpoint);
        basePhoneJsonRoot = (TextView) view.findViewById(R.id.basePhoneJsonRoot);
        baseApiVersion = (TextView) view.findViewById(R.id.baseApiVersion);
        cloudRView = (RelativeLayout) view.findViewById(R.id.appCloudUrl);
        appUssdCode = (RelativeLayout) view.findViewById(R.id.appUssdCode);
        appUssdEndpoint = (RelativeLayout) view.findViewById(R.id.appUssdEndpoint);
        appPhoneEndpoint = (RelativeLayout) view.findViewById(R.id.appPhoneEndpoint);
        appPhoneJson = (RelativeLayout) view.findViewById(R.id.appPhoneJson);
        appApiView = (RelativeLayout) view.findViewById(R.id.appApiView);


        appUrl.setText(appPreferences.getApiServer());
        baseApiVersion.setText(appPreferences.getApiVersion());
        baseApiVersion.setText(appPreferences.getApiVersion());
        baseUssdCode.setText(appPreferences.getUssdCode());
        baseUssdEndpoint.setText(appPreferences.getUssdEndpoint());
        basePhoneEndpoint.setText(appPreferences.getPhoneQueueEndpoint());
        basePhoneJsonRoot.setText(appPreferences.getPhoneJsonRoot());


        cloudRView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Cloud URL");
                final EditText urlText = new EditText(getContext());
                urlText.setHint("Url to the Cloud");
                urlText.setText(appPreferences.getApiServer());
                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.addView(urlText);
                builder.setView(layout);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String cloudUrl = urlText.getText().toString();
                        //holder.iconText.setText(registration.getName().substring(0,1));

                        if (!cloudUrl.trim().equals("")){
                            if (!cloudUrl.substring(0,1).equalsIgnoreCase("http") ||
                                    !cloudUrl.substring(0,1).equalsIgnoreCase("https")){
                                // cloudUrl = "https://"+cloudUrl;
                                Log.d("USSD", cloudUrl);
                            }
                            appPreferences.saveApiServer(cloudUrl);
                            appUrl.setText(appPreferences.getApiServer());
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
        appUssdCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("USSD Code");
                final EditText ussdText = new EditText(getContext());
                ussdText.setHint("USSD");
                ussdText.setText(appPreferences.getUssdCode());
                LinearLayout ussdLayout = new LinearLayout(getContext());
                ussdLayout.setOrientation(LinearLayout.VERTICAL);
                ussdLayout.addView(ussdText);
                builder.setView(ussdLayout);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String ussdCode = ussdText.getText().toString();
                        if (!ussdCode.trim().equals("")){
                            appPreferences.saveUssdCode(ussdCode);
                            baseUssdCode.setText(appPreferences.getUssdCode());
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
        appUssdEndpoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("USSD Endpoint");
                final EditText ussdEndpoint = new EditText(getContext());
                ussdEndpoint.setHint("USSD endpoint");
                ussdEndpoint.setText(appPreferences.getUssdEndpoint());
                LinearLayout ussdEndpointLayout = new LinearLayout(getContext());
                ussdEndpointLayout.setOrientation(LinearLayout.VERTICAL);
                ussdEndpointLayout.addView(ussdEndpoint);
                builder.setView(ussdEndpointLayout);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String ussdCode = ussdEndpoint.getText().toString();
                        if (!ussdCode.trim().equals("")){
                            appPreferences.saveUssdEndpoint(ussdCode);
                            baseUssdEndpoint.setText(appPreferences.getUssdEndpoint());
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        appPhoneEndpoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Phone Endpoint");
                final EditText phoneEndpoint = new EditText(getContext());
                phoneEndpoint.setHint("Phone endpoint");
                phoneEndpoint.setText(appPreferences.getPhoneQueueEndpoint());
                LinearLayout ussdEndpointLayout = new LinearLayout(getContext());
                ussdEndpointLayout.setOrientation(LinearLayout.VERTICAL);
                ussdEndpointLayout.addView(phoneEndpoint);
                builder.setView(ussdEndpointLayout);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String phoneEndApi = phoneEndpoint.getText().toString();
                        if (!phoneEndApi.trim().equals("")){
                            appPreferences.savePhoneQueueEndpoint(phoneEndApi);
                            basePhoneEndpoint.setText(appPreferences.getPhoneQueueEndpoint());
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        appPhoneJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Phone Json Root");
                final EditText phoneJsonRoot = new EditText(getContext());
                phoneJsonRoot.setHint("json root");
                phoneJsonRoot.setText(appPreferences.getPhoneJsonRoot());
                LinearLayout ussdEndpointLayout = new LinearLayout(getContext());
                ussdEndpointLayout.setOrientation(LinearLayout.VERTICAL);
                ussdEndpointLayout.addView(phoneJsonRoot);
                builder.setView(ussdEndpointLayout);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String phoneRoot = phoneJsonRoot.getText().toString();
                        if (!phoneRoot.trim().equals("")){
                            appPreferences.savePhoneJsonRoot(phoneRoot);
                            basePhoneJsonRoot.setText(appPreferences.getPhoneJsonRoot());
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        appApiView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("API Version");
                final EditText phoneApiV = new EditText(getContext());
                phoneApiV.setHint("API Version");
                phoneApiV.setText(appPreferences.getApiVersion());
                LinearLayout apiLayout = new LinearLayout(getContext());
                apiLayout.setOrientation(LinearLayout.VERTICAL);
                apiLayout.addView(phoneApiV);
                builder.setView(apiLayout);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String version = phoneApiV.getText().toString();
                        if (!version.trim().equals("")){
                            appPreferences.saveApiVersion(version);
                            baseApiVersion.setText(appPreferences.getPhoneJsonRoot());
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
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
}
