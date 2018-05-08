package com.example.steven.tripcar.models;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.steven.tripcar.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CocheSelectFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CocheSelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CocheSelectFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText fInicial,hinicial,fFinal,hFinal;
    private EditText pTotal;
    private Button reservar;
    private float coste;

    private int dia,mes,ano,hora,minutos;

    private OnFragmentInteractionListener mListener;

    public CocheSelectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CocheSelectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CocheSelectFragment newInstance(String param1, String param2) {
        CocheSelectFragment fragment = new CocheSelectFragment();
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
        final View view = inflater.inflate(R.layout.fragment_coche_select, container, false);



        SharedPreferences prefe=getActivity().getSharedPreferences("Coche", Context.MODE_PRIVATE);
        SharedPreferences prefeU=getActivity().getSharedPreferences("UsuarioEmail", Context.MODE_PRIVATE);
        final String u=prefeU.getString("Email", "");
        String d=prefe.getString("Coche", "");
        Gson gson = new Gson();

        final Coche  obj = gson.fromJson(d, Coche.class);
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        formatSymbols.setDecimalSeparator(',');
        final DecimalFormat df = new DecimalFormat("####,####.##", formatSymbols);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final DatabaseReference ref = database.getReference(FirebaseReferences.COCHES_REFERENCE);
        final TextView txtMatricula = (TextView) view.findViewById(R.id.matriculaSelect);
        final TextView txtTamanio = (TextView) view.findViewById(R.id.tamanioSelect);
        final TextView txtPrecio = (TextView) view.findViewById(R.id.precioSelect);
        final TextView txtMarca = (TextView) view.findViewById(R.id.marcaModeloSelect);
        final ImageView img = (ImageView) view.findViewById(R.id.imagenCoche) ;
        pTotal = (EditText) view.findViewById(R.id.precioTotal);
        coste = Float.parseFloat(obj.getPrecioHora());

        ref.orderByChild("Matricula").equalTo(obj.getMatricula()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean exist = dataSnapshot.exists();
                if(exist){
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Coche post =postSnapshot.getValue(Coche.class);
                        Coche btChildDetails = new Coche(post.getMarcaModelo(),post.getTamanio(),post.getPrecioHora(),post.getUriImagen(),post.getMatricula());

                        txtMatricula.setText(btChildDetails.getMatricula());
                        txtMarca.setText(btChildDetails.getMarcaModelo());
                        txtPrecio.setText(df.format(Double.parseDouble(btChildDetails.getPrecioHora()))+"€");
                        txtTamanio.setText(btChildDetails.getTamanio());
                        InputStream srt = null;
                        try {
                            srt = new java.net.URL(btChildDetails.getUriImagen()).openStream();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Bitmap bit = BitmapFactory.decodeStream(srt);
                        img.setImageBitmap(bit);

                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        reservar = (Button) view.findViewById(R.id.Reservar);

        fInicial = (EditText) view.findViewById(R.id.fechaInicial);
        hinicial = (EditText) view.findViewById(R.id.horaInicial);

        fFinal = (EditText) view.findViewById(R.id.fechaFin);
        hFinal = (EditText) view.findViewById(R.id.horaFin);

        fInicial.setOnClickListener(this);

        hinicial.setOnClickListener(this);

        fFinal.setOnClickListener(this);

        hFinal.setOnClickListener(this);



        reservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                     SharedPreferences prefe=getActivity().getSharedPreferences("UsuarioEmail", Context.MODE_PRIVATE);
                     String d=prefe.getString("Email", "");
                    final DatabaseReference ref = database.getReference(FirebaseReferences.RESERVAS_REFERENCE);

                    ref.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            SharedPreferences prefe=getActivity().getSharedPreferences("UsuarioEmail", Context.MODE_PRIVATE);
                            String d=prefe.getString("Email", "");
                            boolean existe = dataSnapshot.exists();
                            if(existe && d.length()!=0){



                                for(final DataSnapshot data : dataSnapshot.getChildren()){

                                    Reserva reserva = data.getValue(Reserva.class);
                                    String fecha1 = fInicial.getText() + " " + hinicial.getText();
                                    String fecha2 = fFinal.getText() + " " + hFinal.getText();
                                    final String fechaI = reserva.getfInicio();
                                    final String fechaF = reserva.getfFinal();


                                    String pattern = "dd/MM/yyyy HH:mm";
                                    final SimpleDateFormat format = new SimpleDateFormat(pattern);
                                    if(d.equals(reserva.getUsuario())){

                                        try {
                                            final Date dateI = format.parse(fecha1);
                                            final Date dateF = format.parse(fecha2);


                                            if(obj.getMatricula().equals(reserva.getCoche())) {


                                                ref.orderByChild("coche").equalTo(obj.getMatricula()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        boolean existe = dataSnapshot.exists();
                                                        if(existe) {

                                                            try {

                                                                Date dateIH = format.parse(fechaI);
                                                                Date dateFH = format.parse(fechaF);


                                                                if( dateIH.equals(dateF)||dateFH.equals(dateI)||dateI.equals(dateIH) || dateF.equals(dateFH )  ){
                                                                    Toast toast1 = Toast.makeText(getActivity().getApplicationContext(), "No puedes reservar con esas fechas", Toast.LENGTH_LONG);
                                                                    toast1.show();

                                                                }

                                                                else if(dateI.getTime()>=(dateIH.getTime()) && dateF.getTime()<=(dateFH.getTime())){
                                                                    Toast toast1 = Toast.makeText(getActivity().getApplicationContext(), "No puedes reservar con esas fechas", Toast.LENGTH_LONG);
                                                                    toast1.show();
                                                                }
                                                                else if(dateI.getTime()<=(dateFH.getTime()) && dateF.getTime()>=(dateFH.getTime())){
                                                                    Toast toast1 = Toast.makeText(getActivity().getApplicationContext(), "No puedes reservar con esas fechas", Toast.LENGTH_LONG);
                                                                    toast1.show();

                                                                }
                                                                else if(dateI.getTime()<(dateIH.getTime()) && dateF.getTime()<(dateIH.getTime()) ){
                                                                    float x = (float) ((dateF.getTime()-dateI.getTime())/1000)/3600;
                                                                    float total = x*coste;
                                                                    Log.i("Horas ", String.valueOf(total));
                                                                    Reserva reservadata = new Reserva(fInicial.getText() + " " + hinicial.getText(), fFinal.getText()
                                                                            + " " + hFinal.getText(), u, obj.getMatricula(), String.valueOf(total), UUID.randomUUID().toString());
                                                                    Toast toast2 = Toast.makeText(getActivity().getApplicationContext(), "Reserva realizada", Toast.LENGTH_LONG);
                                                                    toast2.show();
                                                                    ref.push().setValue(reservadata);
                                                                }

                                                                else if(dateI.getTime()>(dateIH.getTime()) && dateF.getTime()>(dateFH.getTime()) ){
                                                                    float x = (float) ((dateF.getTime()-dateI.getTime())/1000)/3600;
                                                                    float total = x*coste;
                                                                    Log.i("Horas ", String.valueOf(total));
                                                                    Reserva reservadata = new Reserva(fInicial.getText() + " " + hinicial.getText(), fFinal.getText()
                                                                            + " " + hFinal.getText(), u, obj.getMatricula(), String.valueOf(total), UUID.randomUUID().toString());
                                                                    Toast toast2 = Toast.makeText(getActivity().getApplicationContext(), "Reserva realizada", Toast.LENGTH_LONG);
                                                                    toast2.show();
                                                                    ref.push().setValue(reservadata);
                                                                }


                                                            } catch (ParseException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });




                                            }
                                            else{

                                                ref.orderByChild("coche").equalTo(obj.getMatricula()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        boolean existe = dataSnapshot.exists();

                                                        if(existe) {

                                                            try {

                                                                Date dateIH = format.parse(fechaI);
                                                                Date dateFH = format.parse(fechaF);


                                                                if( dateIH.equals(dateF)||dateFH.equals(dateI)||dateI.equals(dateIH) || dateF.equals(dateFH )  ){
                                                                    Toast toast1 = Toast.makeText(getActivity().getApplicationContext(), "No puedes reservar con esas fechas", Toast.LENGTH_LONG);
                                                                    toast1.show();

                                                                }
                                                                else if(dateI.getTime()>=(dateIH.getTime()) && dateF.getTime()<=(dateFH.getTime())){
                                                                    Toast toast1 = Toast.makeText(getActivity().getApplicationContext(), "No puedes reservar con esas fechas", Toast.LENGTH_LONG);
                                                                    toast1.show();
                                                                }
                                                                else if(dateI.getTime()<=(dateFH.getTime()) && dateF.getTime()>=(dateFH.getTime())){
                                                                    Toast toast1 = Toast.makeText(getActivity().getApplicationContext(), "No puedes reservar con esas fechas", Toast.LENGTH_LONG);
                                                                    toast1.show();

                                                                }
                                                                else if(dateI.getTime()<(dateIH.getTime()) && dateF.getTime()<(dateIH.getTime()) ){
                                                                    float x = (float) ((dateF.getTime()-dateI.getTime())/1000)/3600;
                                                                    float total = x*coste;
                                                                    Log.i("Horas ", String.valueOf(total));
                                                                    Reserva reservadata = new Reserva(fInicial.getText() + " " + hinicial.getText(), fFinal.getText()
                                                                            + " " + hFinal.getText(), u, obj.getMatricula(), String.valueOf(total), UUID.randomUUID().toString());
                                                                    Toast toast2 = Toast.makeText(getActivity().getApplicationContext(), "Reserva realizada", Toast.LENGTH_LONG);
                                                                    toast2.show();
                                                                    ref.push().setValue(reservadata);
                                                                }

                                                                else if(dateI.getTime()>(dateIH.getTime()) && dateF.getTime()>(dateFH.getTime()) ){
                                                                    float x = (float) ((dateF.getTime()-dateI.getTime())/1000)/3600;
                                                                    float total = x*coste;
                                                                    Log.i("Horas ", String.valueOf(total));
                                                                    Reserva reservadata = new Reserva(fInicial.getText() + " " + hinicial.getText(), fFinal.getText()
                                                                            + " " + hFinal.getText(), u, obj.getMatricula(), String.valueOf(total), UUID.randomUUID().toString());
                                                                    Toast toast2 = Toast.makeText(getActivity().getApplicationContext(), "Reserva realizada", Toast.LENGTH_LONG);
                                                                    toast2.show();
                                                                    ref.push().setValue(reservadata);
                                                                }




                                                            } catch (ParseException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                        else{
                                                            float x = (float) ((dateF.getTime()-dateI.getTime())/1000)/3600;
                                                            float total = x*coste;
                                                            Log.i("Horas ", String.valueOf(total));
                                                            Reserva reservadata = new Reserva(fInicial.getText() + " " + hinicial.getText(), fFinal.getText()
                                                                    + " " + hFinal.getText(), u, obj.getMatricula(), String.valueOf(total), UUID.randomUUID().toString());
                                                            Toast toast2 = Toast.makeText(getActivity().getApplicationContext(), "Reserva realizada", Toast.LENGTH_LONG);
                                                            toast2.show();
                                                            ref.push().setValue(reservadata);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });

                                            }



                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                    }else{
                                        try {
                                            final Date dateI = format.parse(fecha1);
                                            final Date dateF = format.parse(fecha2);


                                            if(obj.getMatricula().equals(reserva.getCoche())) {


                                                ref.orderByChild("coche").equalTo(obj.getMatricula()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        boolean existe = dataSnapshot.exists();
                                                        if(existe) {

                                                            try {

                                                                Date dateIH = format.parse(fechaI);
                                                                Date dateFH = format.parse(fechaF);


                                                                if( dateIH.equals(dateF)||dateFH.equals(dateI)||dateI.equals(dateIH) || dateF.equals(dateFH )  ){
                                                                    Toast toast1 = Toast.makeText(getActivity().getApplicationContext(), "No puedes reservar con esas fechas", Toast.LENGTH_LONG);
                                                                    toast1.show();

                                                                }

                                                                else if(dateI.getTime()>=(dateIH.getTime()) && dateF.getTime()<=(dateFH.getTime())){
                                                                    Toast toast1 = Toast.makeText(getActivity().getApplicationContext(), "No puedes reservar con esas fechas", Toast.LENGTH_LONG);
                                                                    toast1.show();
                                                                }
                                                                else if(dateI.getTime()<=(dateFH.getTime()) && dateF.getTime()>=(dateFH.getTime())){
                                                                    Toast toast1 = Toast.makeText(getActivity().getApplicationContext(), "No puedes reservar con esas fechas", Toast.LENGTH_LONG);
                                                                    toast1.show();

                                                                }
                                                                else if(dateI.getTime()<(dateIH.getTime()) && dateF.getTime()<(dateIH.getTime()) ){
                                                                    float x = (float) ((dateF.getTime()-dateI.getTime())/1000)/3600;
                                                                    float total = x*coste;
                                                                    Log.i("Horas ", String.valueOf(total));
                                                                    Reserva reservadata = new Reserva(fInicial.getText() + " " + hinicial.getText(), fFinal.getText()
                                                                            + " " + hFinal.getText(), u, obj.getMatricula(), String.valueOf(total), UUID.randomUUID().toString());
                                                                    Toast toast2 = Toast.makeText(getActivity().getApplicationContext(), "Reserva realizada", Toast.LENGTH_LONG);
                                                                    toast2.show();
                                                                    ref.push().setValue(reservadata);
                                                                }
                                                                else if(dateI.getTime()>(dateIH.getTime()) && dateF.getTime()>(dateFH.getTime()) ){
                                                                    float x = (float) ((dateF.getTime()-dateI.getTime())/1000)/3600;
                                                                    float total = x*coste;
                                                                    Log.i("Horas ", String.valueOf(total));
                                                                    Reserva reservadata = new Reserva(fInicial.getText() + " " + hinicial.getText(), fFinal.getText()
                                                                            + " " + hFinal.getText(), u, obj.getMatricula(), String.valueOf(total), UUID.randomUUID().toString());
                                                                    Toast toast2 = Toast.makeText(getActivity().getApplicationContext(), "Reserva realizada", Toast.LENGTH_LONG);
                                                                    toast2.show();
                                                                    ref.push().setValue(reservadata);
                                                                }


                                                            } catch (ParseException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });




                                            }
                                            else{

                                                ref.orderByChild("coche").equalTo(obj.getMatricula()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        boolean existe = dataSnapshot.exists();

                                                        if(existe) {

                                                            try {

                                                                Date dateIH = format.parse(fechaI);
                                                                Date dateFH = format.parse(fechaF);


                                                                if( dateIH.equals(dateF)||dateFH.equals(dateI)||dateI.equals(dateIH) || dateF.equals(dateFH )  ){
                                                                    Toast toast1 = Toast.makeText(getActivity().getApplicationContext(), "No puedes reservar con esas fechas", Toast.LENGTH_LONG);
                                                                    toast1.show();

                                                                }
                                                                else if(dateI.getTime()>=(dateIH.getTime()) && dateF.getTime()<=(dateFH.getTime())){
                                                                    Toast toast1 = Toast.makeText(getActivity().getApplicationContext(), "No puedes reservar con esas fechas", Toast.LENGTH_LONG);
                                                                    toast1.show();
                                                                }
                                                                else if(dateI.getTime()<=(dateFH.getTime()) && dateF.getTime()>=(dateFH.getTime())){
                                                                    Toast toast1 = Toast.makeText(getActivity().getApplicationContext(), "No puedes reservar con esas fechas", Toast.LENGTH_LONG);
                                                                    toast1.show();

                                                                }
                                                                else if(dateI.getTime()<(dateIH.getTime()) && dateF.getTime()<(dateIH.getTime()) ){
                                                                    float x = (float) ((dateF.getTime()-dateI.getTime())/1000)/3600;
                                                                    float total = x*coste;
                                                                    Log.i("Horas ", String.valueOf(total));
                                                                    Reserva reservadata = new Reserva(fInicial.getText() + " " + hinicial.getText(), fFinal.getText()
                                                                            + " " + hFinal.getText(), u, obj.getMatricula(), String.valueOf(total), UUID.randomUUID().toString());
                                                                    Toast toast2 = Toast.makeText(getActivity().getApplicationContext(), "Reserva realizada", Toast.LENGTH_LONG);
                                                                    toast2.show();
                                                                    ref.push().setValue(reservadata);
                                                                }

                                                                else if(dateI.getTime()>(dateIH.getTime()) && dateF.getTime()>(dateFH.getTime()) ){
                                                                    float x = (float) ((dateF.getTime()-dateI.getTime())/1000)/3600;
                                                                    float total = x*coste;
                                                                    Log.i("Horas ", String.valueOf(total));
                                                                    Reserva reservadata = new Reserva(fInicial.getText() + " " + hinicial.getText(), fFinal.getText()
                                                                            + " " + hFinal.getText(), u, obj.getMatricula(), String.valueOf(total), UUID.randomUUID().toString());
                                                                    Toast toast2 = Toast.makeText(getActivity().getApplicationContext(), "Reserva realizada", Toast.LENGTH_LONG);
                                                                    toast2.show();
                                                                    ref.push().setValue(reservadata);
                                                                }




                                                            } catch (ParseException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                        else{
                                                            float x = (float) ((dateF.getTime()-dateI.getTime())/1000)/3600;
                                                            float total = x*coste;
                                                            Log.i("Horas ", String.valueOf(total));
                                                            Reserva reservadata = new Reserva(fInicial.getText() + " " + hinicial.getText(), fFinal.getText()
                                                                    + " " + hFinal.getText(), u, obj.getMatricula(), String.valueOf(total), UUID.randomUUID().toString());
                                                            Toast toast2 = Toast.makeText(getActivity().getApplicationContext(), "Reserva realizada", Toast.LENGTH_LONG);
                                                            toast2.show();
                                                            ref.push().setValue(reservadata);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });

                                            }



                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }




                                }



                            }
                            else if(existe && d.length()==0){

                                Toast toast1 = Toast.makeText(getActivity().getApplicationContext(), "Debes iniciar sesión para realizar cualquier reserva", Toast.LENGTH_LONG);
                                toast1.show();


                            }
                            else if(!existe && d.length()==0){

                                Toast toast1 = Toast.makeText(getActivity().getApplicationContext(), "Debes iniciar sesión para realizar cualquier reserva", Toast.LENGTH_LONG);
                                toast1.show();


                            }
                            else if(!existe && d.length()!=0){
                                String fecha1 = fInicial.getText() + " " + hinicial.getText();
                                String fecha2 = fFinal.getText() + " " + hFinal.getText();

                                String pattern = "dd/MM/yyyy HH:mm";
                                SimpleDateFormat format = new SimpleDateFormat(pattern);
                                try {
                                    Date dateI = format.parse(fecha1);
                                    Date dateF = format.parse(fecha2);

                                    float x = (float) ((dateF.getTime()-dateI.getTime())/1000)/3600;
                                    float total = x*coste;
                                    Log.i("Horas ", String.valueOf(total));
                                    Reserva reservadata = new Reserva(fInicial.getText() + " " + hinicial.getText(), fFinal.getText()
                                            + " " + hFinal.getText(), u, obj.getMatricula(), String.valueOf(total), UUID.randomUUID().toString() );
                                    Toast toast2 = Toast.makeText(getActivity().getApplicationContext(), "Reserva realizada", Toast.LENGTH_LONG);
                                    toast2.show();
                                    ref.push().setValue(reservadata);



                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }



                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


            }
        });



        return  view;
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

    @Override
    public void onClick(View v) {
        final Calendar calendario = Calendar.getInstance();
        final int yy = calendario.get(Calendar.YEAR);
        final int mm = calendario.get(Calendar.MONTH);
        final int dd = calendario.get(Calendar.DAY_OF_MONTH);
        final int hora = calendario.get(Calendar.HOUR_OF_DAY);
        final int minutos = calendario.get(Calendar.MINUTE);
        if (v == fInicial) {

            DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String fecha = "";
                    if(dayOfMonth<10 && monthOfYear+1>9){
                         fecha = "0"+String.valueOf(dayOfMonth) +"/"+String.valueOf(monthOfYear+1)
                                +"/"+String.valueOf(year);
                    }
                    else if(monthOfYear+1<10 && dayOfMonth>9){
                        fecha = String.valueOf(dayOfMonth) +"/0"+String.valueOf(monthOfYear+1)
                                +"/"+String.valueOf(year);
                    }
                    else if(monthOfYear+1<10 && dayOfMonth<10){
                        fecha = "0"+String.valueOf(dayOfMonth) +"/0"+String.valueOf(monthOfYear+1)
                                +"/"+String.valueOf(year);
                    }
                    else{
                        fecha = String.valueOf(dayOfMonth) +"/"+String.valueOf(monthOfYear+1)
                                +"/"+String.valueOf(year);
                    }

                    String pattern = "dd/MM/yyyy";
                    SimpleDateFormat format = new SimpleDateFormat(pattern);
                    try {

                        String fechaActual = format.format(new Date());
                        Date fechaSelect = format.parse(fecha);
                        Date actual = format.parse(fechaActual);

                        if(fechaSelect.equals(actual)){
                            Toast toast2 = Toast.makeText(getActivity().getApplicationContext(), "Seleccione una fecha valida" , Toast.LENGTH_SHORT);
                            toast2.show();
                        }
                        else if(fechaSelect.before(actual)){
                            Toast toast2 = Toast.makeText(getActivity().getApplicationContext(), "Seleccione una fecha valida" , Toast.LENGTH_SHORT);
                            toast2.show();
                        }
                        else if(fechaSelect.after(actual)){
                            fInicial.setText(fecha);
                            fFinal.setText("");
                            hFinal.setText("");
                        }

                        Log.e("fechaI actual: ",actual.toString());
                        Log.e("fechaI select: ",fechaSelect.toString());


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }



                }
            }, yy, mm, dd);

            datePicker.show();
        }
        if(v==hinicial){


            //final int segundos = calendario.get(Calendar.SECOND);
            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String hora = "";
                    if (minute < 10) {
                        hora = hourOfDay + ":0" + minute;
                    } else {
                        hora = hourOfDay + ":" + minute;
                    }

                    if(hora.equals("0:00")){
                        Toast toast2 = Toast.makeText(getActivity().getApplicationContext(), "Seleccione una hora valida" , Toast.LENGTH_SHORT);
                        toast2.show();
                    }else{
                        hinicial.setText(hora);
                        fFinal.setText("");
                        hFinal.setText("");
                    }

                }




            },hora,minutos,true);
            timePickerDialog.show();
        }



        if(v==fFinal){


            DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    String fecha = "";
                    if(dayOfMonth<10 && monthOfYear+1>9){
                        fecha = "0"+String.valueOf(dayOfMonth) +"/"+String.valueOf(monthOfYear+1)
                                +"/"+String.valueOf(year);
                    }
                    else if(monthOfYear+1<10 && dayOfMonth>9){
                        fecha = String.valueOf(dayOfMonth) +"/0"+String.valueOf(monthOfYear+1)
                                +"/"+String.valueOf(year);
                    }
                    else if(monthOfYear+1<10 && dayOfMonth<10){
                        fecha = "0"+String.valueOf(dayOfMonth) +"/0"+String.valueOf(monthOfYear+1)
                                +"/"+String.valueOf(year);
                    }
                    else{
                        fecha = String.valueOf(dayOfMonth) +"/"+String.valueOf(monthOfYear+1)
                                +"/"+String.valueOf(year);
                    }

                    String fechaInicial = "";
                    String pattern = "dd/MM/yyyy";
                    SimpleDateFormat format = new SimpleDateFormat(pattern);
                    if(!fInicial.getText().toString().equals("")){
                         fechaInicial = fInicial.getText().toString();
                    }


                    try {


                        Date fechaSelect = format.parse(fecha);

                        Date fechaInit = format.parse(fechaInicial);

                        if(fechaSelect.equals(fechaInit)){
                            Toast toast2 = Toast.makeText(getActivity().getApplicationContext(), "Seleccione una fecha valida" , Toast.LENGTH_SHORT);
                            toast2.show();
                        }
                        else if(fechaSelect.before(fechaInit)){
                            Toast toast2 = Toast.makeText(getActivity().getApplicationContext(), "Seleccione una fecha valida" , Toast.LENGTH_SHORT);
                            toast2.show();
                        }
                        else if(fechaSelect.after(fechaInit)){
                            fFinal.setText(fecha);

                        }

                        Log.e("fechaF actual: ",fechaInit.toString());
                        Log.e("fechaF select: ",fechaSelect.toString());


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }, yy, mm, dd);

            datePicker.show();
        }

        if(v==hFinal){


            //final int segundos = calendario.get(Calendar.SECOND);
            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String hora = "";
                    if(minute<10){
                        hora = hourOfDay + ":0" + minute;
                    }else{
                        hora = hourOfDay + ":" + minute;
                    }

                    hFinal.setText(hora);

                    if(fInicial.getText()!=null && hinicial.getText()!=null && fFinal.getText()!=null && hFinal!=null){
                        String fecha1 = fInicial.getText() + " " + hinicial.getText();
                        String fecha2 = fFinal.getText() + " " + hFinal.getText();



                        String pattern = "dd/MM/yyyy HH:mm";
                        final SimpleDateFormat format = new SimpleDateFormat(pattern);
                        try {
                            final Date dateI = format.parse(fecha1);
                            final Date dateF = format.parse(fecha2);
                            float x = (float) ((dateF.getTime()-dateI.getTime())/1000)/3600;
                            float total = x*coste;
                            DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.getDefault());
                            formatSymbols.setDecimalSeparator(',');
                            DecimalFormat df = new DecimalFormat("####,####.##", formatSymbols);

                            pTotal.setText(df.format(total) + " €");
                        }
                        catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }




                }
            },hora,minutos,true);
            timePickerDialog.show();
        }




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
