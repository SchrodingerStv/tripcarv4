package com.example.steven.tripcar.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.steven.tripcar.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CochesBuscadorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CochesBuscadorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CochesBuscadorFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "fechaInicial";
    private static final String ARG_PARAM2 = "fechaFinal";
    private static final String ARG_PARAM3 = "tamanio";
    private List<Coche> listaCochesFiltro = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;
    private ListView mListView;
    private MyAdapterBuscador myAdapter;
    private OnFragmentInteractionListener mListener;
    private boolean dato;
    private List<String>matricules = new ArrayList<>();
    private List<String>matriculesOK = new ArrayList<>();
    public CochesBuscadorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CochesBuscadorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CochesBuscadorFragment newInstance(String param1, String param2,String param3 ) {
        CochesBuscadorFragment fragment = new CochesBuscadorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM3, param3);
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
            mParam3 = getArguments().getString(ARG_PARAM3);
            Log.e("comprobar", mParam1+mParam2+mParam3);
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_coches_buscador, container, false);

        mListView = (ListView) view.findViewById(R.id.listviewFiltro);
        boolean hacer= comprobarFechas();



        return view;



    }

    private void obtenerCoches(boolean dato, final List<String> matriculas) {


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final DatabaseReference ref = database.getReference(FirebaseReferences.COCHES_REFERENCE);

        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        formatSymbols.setDecimalSeparator(',');
        final DecimalFormat df = new DecimalFormat("####,####.##", formatSymbols);

                String pattern = "dd/MM/yyyy HH:mm";
                SimpleDateFormat format = new SimpleDateFormat(pattern);
                DateFormat writeFormat = new SimpleDateFormat(pattern);




                if(dato && mParam3.equals("Seleccionar tamaño")){
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            int i = 0;
                            for (DataSnapshot postSnapshot2 : dataSnapshot.getChildren()) {

                                Coche coche = postSnapshot2.getValue(Coche.class);

                                Coche btChildDetails = new Coche(coche.getMarcaModelo(), coche.getTamanio(), coche.getPrecioHora(), coche.getUriImagen(), coche.getMatricula());
                                listaCochesFiltro.add(btChildDetails);

                                myAdapter = new MyAdapterBuscador(getActivity(), listaCochesFiltro);
                                mListView.setAdapter(myAdapter);
                                myAdapter.notifyDataSetChanged();


                            }
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else if(dato && !mParam3.equals("Seleccionar tamaño")){
                    ref.orderByChild("Tamanio").equalTo(mParam3).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            int i = 0;
                            for (DataSnapshot postSnapshot2 : dataSnapshot.getChildren()) {

                                Coche coche = postSnapshot2.getValue(Coche.class);

                                Coche btChildDetails = new Coche(coche.getMarcaModelo(), coche.getTamanio(), coche.getPrecioHora(), coche.getUriImagen(), coche.getMatricula());
                                listaCochesFiltro.add(btChildDetails);

                                myAdapter = new MyAdapterBuscador(getActivity(), listaCochesFiltro);
                                mListView.setAdapter(myAdapter);
                                myAdapter.notifyDataSetChanged();


                            }
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else if(!dato && mParam3.equals("Seleccionar tamaño")){
                    Log.e("comprobaras", String.valueOf(dato));
                    Log.e("comprobaras", String.valueOf(matriculas));
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot postSnapshot2 : dataSnapshot.getChildren()) {

                                Coche coche = postSnapshot2.getValue(Coche.class);
                                matricules.add(coche.getMatricula());


                            }
                           matricules.removeAll(matriculas);


                            for (String j : matricules){
                                Log.e("comprobaras", String.valueOf(j));
                                ref.orderByChild("Matricula").equalTo(j).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        for (DataSnapshot postSnapshot2 : dataSnapshot.getChildren()) {

                                            Coche coche = postSnapshot2.getValue(Coche.class);
                                            Coche btChildDetails = new Coche(coche.getMarcaModelo(), coche.getTamanio(), coche.getPrecioHora(), coche.getUriImagen(), coche.getMatricula());

                                            listaCochesFiltro.add(btChildDetails);
                                        }



                                        myAdapter = new MyAdapterBuscador(getActivity(), listaCochesFiltro);
                                        mListView.setAdapter(myAdapter);
                                        myAdapter.notifyDataSetChanged();


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
                 else if(!dato &&  !mParam3.equals("Seleccionar tamaño")){

                    Log.e("comprobar", String.valueOf(dato));
                    Log.e("comprobar", String.valueOf(matriculas));

                    ref.orderByChild("Tamanio").equalTo(mParam3).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot postSnapshot2 : dataSnapshot.getChildren()) {

                                Coche coche = postSnapshot2.getValue(Coche.class);
                                Coche btChildDetails = new Coche(coche.getMarcaModelo(), coche.getTamanio(), coche.getPrecioHora(), coche.getUriImagen(), coche.getMatricula());

                                matricules.add(coche.getMatricula());

                            }
                            matricules.removeAll(matriculas);

                            for (String j : matricules){
                                ref.orderByChild("Matricula").equalTo(j).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        for (DataSnapshot postSnapshot2 : dataSnapshot.getChildren()) {

                                            Coche coche = postSnapshot2.getValue(Coche.class);
                                            Coche btChildDetails = new Coche(coche.getMarcaModelo(), coche.getTamanio(), coche.getPrecioHora(), coche.getUriImagen(), coche.getMatricula());

                                            listaCochesFiltro.add(btChildDetails);
                                        }



                                        myAdapter = new MyAdapterBuscador(getActivity(), listaCochesFiltro);
                                        mListView.setAdapter(myAdapter);
                                        myAdapter.notifyDataSetChanged();


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



    }

    private boolean comprobarFechas(){
        final boolean existe = false;
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference(FirebaseReferences.RESERVAS_REFERENCE);

        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        formatSymbols.setDecimalSeparator(',');
        final DecimalFormat df = new DecimalFormat("####,####.##", formatSymbols);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String pattern = "dd/MM/yyyy HH:mm";
                SimpleDateFormat format = new SimpleDateFormat(pattern);
                DateFormat writeFormat = new SimpleDateFormat( pattern);
                boolean busqueda = false;
                List<String> matriculas = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    final Reserva post = postSnapshot.getValue(Reserva.class);

                    try {
                        Date dateI = format.parse(mParam1);
                        Date dateF = format.parse(mParam2);
                        Date dateIH = format.parse(post.getfInicio());
                        Date dateFH = format.parse(post.getfFinal());

                        if(dateIH.equals(dateF)||dateFH.equals(dateI)||dateI.equals(dateIH) || dateF.equals(dateFH ) ){
                            Log.e("fechas no validas", "soy 1");
                            busqueda=false;
                            String matricula = post.getCoche();
                            matriculas.add(matricula);

                        }

                        else if(dateI.getTime()>=(dateIH.getTime()) && dateF.getTime()<=(dateFH.getTime())){
                            Log.e("fechas no validas", "soy 2");
                            busqueda=false;
                            String matricula = post.getCoche();
                            matriculas.add(matricula);


                        }

                        else if(dateI.getTime()<=(dateFH.getTime()) && dateF.getTime()>=(dateFH.getTime())){
                            Log.e("fechas no validas", "soy 3");
                            busqueda=false;
                            String matricula = post.getCoche();
                            matriculas.add(matricula);


                        }
                        else if(dateI.getTime()<=(dateIH.getTime()) && dateF.getTime()<=(dateFH.getTime()) &&  dateF.getTime()>=(dateIH.getTime())){
                            Log.e("fechas no validas", "soy 4");
                            busqueda=false;
                            String matricula = post.getCoche();
                            matriculas.add(matricula);
                        }
                        else if(dateI.getTime()>(dateIH.getTime()) && dateF.getTime()>(dateFH.getTime()) ){
                            Log.e("fechas validas", "soy 5");
                            busqueda=true;
                            String matricula = "";
                            matriculas.add(matricula);


                        }



                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }
                obtenerCoches( busqueda,matriculas);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return existe;
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
