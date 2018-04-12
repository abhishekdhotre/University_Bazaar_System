package com.example.cse6324.university_bazaar_system;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SellLendFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SellLendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellLendFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PICK_IMAGE_REQUEST = 71;

    private Uri filePath;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mType;

    private FirebaseStorage storage;

    private OnFragmentInteractionListener mListener;

    public SellLendFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SellLendFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SellLendFragment newInstance(String param1, String param2) {
        SellLendFragment fragment = new SellLendFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storage = FirebaseStorage.getInstance();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View myFragmentView = inflater.inflate(R.layout.fragment_sell_lend, container, false);
        Button btnChoose = myFragmentView.findViewById(R.id.btnChooseImage);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        }) ;

        final EditText etDuration = myFragmentView.findViewById(R.id.etMerchandiseDuration);
        final RadioGroup rdButton = myFragmentView.findViewById(R.id.rgSellLend);
        rdButton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_Sell) {
                    etDuration.setEnabled(false);
                    mType = "Sell";
                } else {
                    etDuration.setEnabled(true);
                    mType = "Lend";
                }
            }
        });

        Button btnPost = myFragmentView.findViewById(R.id.btnPost);
        final EditText etName = myFragmentView.findViewById(R.id.etMerchandiseName);
        final EditText etDesc = myFragmentView.findViewById(R.id.etMerchandiseDesc);
        final EditText etPrice = myFragmentView.findViewById(R.id.etMerchandisePrice);
        final EditText etLocation = myFragmentView.findViewById(R.id.etMerchandiseLocation);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                final String str = sdf.format(Calendar.getInstance().getTime()).toString();
                StorageReference storageRef = storage.getReference();
                StorageReference riversRef = storageRef.child("images/"+ str + ".jpg");
                UploadTask uploadTask = riversRef.putFile(filePath);

                // Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        Map<String, Object> item = new HashMap<>();
                        item.put("etName", etName.getText().toString());
                        item.put("etDesc", etDesc.getText().toString());
                        item.put("etPrice", etPrice.getText().toString());
                        item.put("etDuration", etDuration.getText().toString());
                        item.put("etLocation", etLocation.getText().toString());
                        item.put("etExchangeCriteria", "");
                        item.put("imgPath",str + ".jpg");
                        item.put("etType", mType);

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("items").document(str).set(item);

                        Toast.makeText(getContext(), "Merchandise listed for " + mType, Toast.LENGTH_SHORT).show();
                    }
                });
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null )
        {
            filePath = data.getData();
            /*try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }*/
        }
    }



   /* @Override
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
    }*/

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
