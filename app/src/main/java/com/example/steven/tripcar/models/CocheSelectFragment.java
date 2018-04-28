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
import com.example.steven.tripcar.services.cochesService;
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
import java.util.Calendar;
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
    private  cochesService cochesService;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText fInicial,hinicial,fFinal,hFinal;
    private Button reservar;

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

                    final DatabaseReference ref = database.getReference(FirebaseReferences.RESERVAS_REFERENCE);
                    ref.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Reserva reserva = new Reserva(fInicial.getText() + " " + hinicial.getText(), fFinal.getText() + " " + hFinal.getText(), u, obj.getMatricula(), "100", UUID.randomUUID().toString() );
                            Toast toast2 = Toast.makeText(getActivity().getApplicationContext(), "Rserva realizada", Toast.LENGTH_LONG);
                            toast2.show();
                            ref.push().setValue(reserva);


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

                    if(year< yy || dayOfMonth<dd || monthOfYear<mm){
                        Toast toast2 = Toast.makeText(getActivity().getApplicationContext(), "Seleccione una fecha valida" , Toast.LENGTH_SHORT);
                        toast2.show();
                    }
                   else{

                        String fecha = String.valueOf(year) +"-"+String.valueOf(monthOfYear+1)
                                +"-"+String.valueOf(dayOfMonth);
                        fInicial.setText(fecha);
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
                    String hora = hourOfDay + ":" + minute;
                    hinicial.setText(hora);
                }
            },hora,minutos,true);
            timePickerDialog.show();
        }



        if(v==fFinal){




            DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    if(year< yy || dayOfMonth<dd || monthOfYear<mm ){

                        Toast toast2 = Toast.makeText(getActivity().getApplicationContext(), "Seleccione una fecha valida" , Toast.LENGTH_SHORT);
                        toast2.show();
                    }
                    else{

                        String fecha = String.valueOf(year) +"-"+String.valueOf(monthOfYear+1)
                                +"-"+String.valueOf(dayOfMonth);
                        Toast toast2 = Toast.makeText(getActivity().getApplicationContext(), "fechas"+fecha+"   "+fInicial.getText() , Toast.LENGTH_SHORT);
                        toast2.show();
                        if(fecha.equals(fInicial.getText().toString())){
                            fFinal.setText(fecha);
                        }

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
                    String hora = hourOfDay + ":" + minute;

                        hFinal.setText(hora);


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
