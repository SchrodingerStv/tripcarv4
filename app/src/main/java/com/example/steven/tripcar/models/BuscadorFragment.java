package com.example.steven.tripcar.models;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.steven.tripcar.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BuscadorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BuscadorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuscadorFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText fBInicial, hBinicial, fBFinal, hBFinal;
    private Button buscar;
    private OnFragmentInteractionListener mListener;
    private Spinner tamanios;

    public BuscadorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BuscadorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BuscadorFragment newInstance(String param1, String param2) {
        BuscadorFragment fragment = new BuscadorFragment();
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
        final View view = inflater.inflate(R.layout.fragment_buscador, container, false);

        buscar = (Button) view.findViewById(R.id.Buscar);
        final String[] dato = new String[1];

        tamanios = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.tamanios, android.R.layout.simple_spinner_item);
        tamanios.setAdapter(adapter);

        tamanios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                dato[0] = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Bundle bundle = new Bundle();
                bundle.putString("fechaInicial", fBInicial.getText().toString() + " " + hBinicial.getText().toString());
                bundle.putString("fechaFinal", fBFinal.getText().toString() + " " + hBFinal.getText().toString());
                bundle.putString("tamanio", dato[0]);

                Fragment myObj = new CochesBuscadorFragment();
                myObj.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, myObj).addToBackStack(null).commit();
                // myObj.setArguments(bundle);
            }
        });

        fBInicial = (EditText) view.findViewById(R.id.fechaBInicial);
        hBinicial = (EditText) view.findViewById(R.id.horaBInicial);

        fBFinal = (EditText) view.findViewById(R.id.fechaBFin);
        hBFinal = (EditText) view.findViewById(R.id.horaBFin);

        fBInicial.setOnClickListener(this);

        hBinicial.setOnClickListener(this);

        fBFinal.setOnClickListener(this);

        hBFinal.setOnClickListener(this);

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
        if (v == fBInicial) {

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



                        fBInicial.setText(fecha);
                        fBFinal.setText("");
                        hBFinal.setText("");


                        Log.e("fechaI actual: ",actual.toString());
                        Log.e("fechaI select: ",fechaSelect.toString());


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }



                }
            }, yy, mm, dd);

            datePicker.show();
        }
        if(v==hBinicial){


            //final int segundos = calendario.get(Calendar.SECOND);
            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String horaI = "";
                    String comparar = "";
                    if (minute < 10) {
                        comparar = hora + ":0" + minutos;
                        horaI = hourOfDay + ":0" + minute;

                    } else {
                        horaI = hourOfDay + ":" + minute;
                        comparar = hora + ":" + minutos;

                    }

                    String fechaInicial = "";
                    String pattern = "dd/MM/yyyy HH:mm";
                    String pattern2 = "dd/MM/yyyy";
                    SimpleDateFormat format = new SimpleDateFormat(pattern);
                    SimpleDateFormat format2 = new SimpleDateFormat(pattern2);
                    Date date = new Date();
                    fechaInicial=  fBInicial.getText().toString();

                    try{
                        Date date1 = format.parse("00/00/0000 "+horaI);
                        Date date2 = format.parse("00/00/0000 "+comparar);
                        String dateact = format.format(date);
                        Date date4 =  format2.parse(dateact);
                        Date date3 = format2.parse(fechaInicial);

                        if(horaI.equals("0:00")){
                            Toast toast2 = Toast.makeText(getActivity().getApplicationContext(), "Seleccione una hora valida" , Toast.LENGTH_SHORT);
                            toast2.show();
                        }
                        else if(date1.getTime()<date2.getTime() && date3.getTime()==date4.getTime()){
                            Toast toast2 = Toast.makeText(getActivity().getApplicationContext(), "Seleccione una hora valida" , Toast.LENGTH_SHORT);
                            toast2.show();
                        }
                        else if(date3.getTime()>=date4.getTime()){
                            hBinicial.setText(horaI);
                            fBFinal.setText("");
                            hBFinal.setText("");
                        }
                        else{
                            hBinicial.setText(horaI);
                            fBFinal.setText("");
                            hBFinal.setText("");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }



                }


            },hora,minutos,true);
            timePickerDialog.show();
        }



        if(v==fBFinal){


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
                    if(!fBInicial.getText().toString().equals("")){
                        fechaInicial = fBInicial.getText().toString();
                    }


                    try {


                        Date fechaSelect = format.parse(fecha);

                        Date fechaInit = format.parse(fechaInicial);


                        if(fechaSelect.before(fechaInit)){
                            Toast toast2 = Toast.makeText(getActivity().getApplicationContext(), "Seleccione una fecha valida" , Toast.LENGTH_SHORT);
                            toast2.show();
                        }
                        else {
                            fBFinal.setText(fecha);

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

        if(v==hBFinal){


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
                    hBFinal.setText(hora);

                    String pattern = "dd/MM/yyyy HH:mm";
                    SimpleDateFormat format = new SimpleDateFormat(pattern);





                    if(!fBInicial.getText().toString().equals("") && !hBinicial.getText().toString().equals("") && !fBFinal.getText().toString().equals("") && !hBFinal.getText().toString().equals("")){

                        try {


                            String fecha1 = fBInicial.getText() + " " + hBinicial.getText();
                            String fecha2 = fBFinal.getText() + " " + hBFinal.getText();
                            final Date dateI = format.parse(fecha1);
                            final Date dateF = format.parse(fecha2);
                            Log.e("fecha2: ", fecha2);
                            if (dateF.getTime() < (dateI.getTime())) {
                                Log.e("fecha2: ", dateF.getTime()+"  "+dateI.getTime());
                                Toast toast2 = Toast.makeText(getActivity().getApplicationContext(), "Seleccione una hora valida", Toast.LENGTH_SHORT);
                                toast2.show();
                                hBFinal.setText("");
                            } else {
                                hBFinal.setText(hora);

                            }



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
