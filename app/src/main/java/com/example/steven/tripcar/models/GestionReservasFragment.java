package com.example.steven.tripcar.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.steven.tripcar.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GestionReservasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GestionReservasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GestionReservasFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private List<usuarioCocheReserva> listaReservas = new ArrayList<>();
    private   ListView mListView;

    private MyAdapterReservas myAdapterReservas;

    public GestionReservasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GestionReservasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GestionReservasFragment newInstance(String param1, String param2) {
        GestionReservasFragment fragment = new GestionReservasFragment();
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
        final View view = inflater.inflate(R.layout.fragment_gestion_reservas, container, false);
        mListView = (ListView) view.findViewById(R.id.listReservasview);
        obtenerReservas();

        return view;
    }
    private void obtenerReservas(){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final DatabaseReference ref = database.getReference(FirebaseReferences.RESERVAS_REFERENCE);

        SharedPreferences prefe=getActivity().getSharedPreferences("Coche", Context.MODE_PRIVATE);
        SharedPreferences prefeU=getActivity().getSharedPreferences("UsuarioEmail", Context.MODE_PRIVATE);
        final String u=prefeU.getString("Email", "");
        String d=prefe.getString("Coche", "");
        Gson gson = new Gson();

        final Coche  obj = gson.fromJson(d, Coche.class);
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        formatSymbols.setDecimalSeparator(',');
        final DecimalFormat df = new DecimalFormat("####,####.##", formatSymbols);



        ref.orderByChild("usuario").equalTo(u).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final DatabaseReference refcoche = database.getReference(FirebaseReferences.COCHES_REFERENCE);


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    final usuarioCocheReserva post = postSnapshot.getValue(usuarioCocheReserva.class);
                    refcoche.orderByChild("Matricula").equalTo(post.getCoche()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                                final Coche coche = postSnapshot.getValue(Coche.class);
                                usuarioCocheReserva btChildDetails = new usuarioCocheReserva(post.getfInicio(),post.getfFinal(),post.getUsuario(),post.getCoche(),post.getPrecioTotal(),coche.getMarcaModelo(),
                                        coche.getTamanio(),coche.getPrecioHora(),coche.getUriImagen(),coche.getMatricula(),post.getIdReserva());
                                listaReservas.add(btChildDetails);

                                myAdapterReservas= new MyAdapterReservas(getActivity(),listaReservas);
                                mListView.setAdapter(myAdapterReservas);
                                myAdapterReservas.notifyDataSetChanged();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



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
