package com.example.cse6324.university_bazaar_system;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MerchandiseDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MerchandiseDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MerchandiseDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    @SuppressWarnings("unchecked")
    private Map<String, Object> mParam1;

    private ArrayList<Map<String, Object>> items;
    private Map<String, byte[]> images;

    private OnFragmentInteractionListener mListener;

    public MerchandiseDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment MerchandiseDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MerchandiseDetailFragment newInstance(Bundle param1) {
        MerchandiseDetailFragment fragment = new MerchandiseDetailFragment();
        Bundle args = new Bundle();
        args.putBundle(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle b = (Bundle)getArguments().get(ARG_PARAM1);
            mParam1 = new Gson().fromJson(b.getString("item"), new TypeToken<Map<String, Object>>(){}.getType());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View myFragmentView = inflater.inflate(R.layout.fragment_merchandise_detail, container, false);

        ImageView imgview = myFragmentView.findViewById(R.id.imageMerchandiseDetail);
        TextView name = myFragmentView.findViewById(R.id.nameMerchandiseDetail);
        TextView desc = myFragmentView.findViewById(R.id.descriptionMerchandiseDetail);
        TextView type = myFragmentView.findViewById(R.id.typeExchangeMerchandise);
        TextView price = myFragmentView.findViewById(R.id.priceMerchandiseDetail);
        TextView dur = myFragmentView.findViewById(R.id.durationMerchandiseDetail);
        TextView exch = myFragmentView.findViewById(R.id.exchangeCriteriaMerchandiseDetail);
        Button bt = myFragmentView.findViewById(R.id.interestedButton);

        items = ((EntryScreenActivity)getActivity()).items;
        images = ((EntryScreenActivity)getActivity()).images;

        Map<String, Object> i = mParam1;
        byte[] img = images.get(i.get("imgPath"));
        imgview.setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length));
        name.setText(i.get("etName").toString());
        desc.setText(i.get("etDesc").toString());
        type.setText(i.get("etType").toString());
        price.setText("$" + i.get("etPrice").toString());
        if (i.get("etType").toString().equals("Lend")){
            dur.setText(i.get("etDuration").toString());
        }
        if (i.get("etType").toString().equals("Exchange")){
            exch.setText(i.get("etExchangeCriteria").toString());
        }

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Message sent", Toast.LENGTH_SHORT).show();
            }
        });


        return myFragmentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

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
