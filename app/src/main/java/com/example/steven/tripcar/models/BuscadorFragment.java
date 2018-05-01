package com.example.steven.tripcar.models;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.steven.tripcar.R;

import java.util.Calendar;

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
    private EditText fBInicial,hBinicial,fBFinal,hBFinal;
    private Button buscar;
    private OnFragmentInteractionListener mListener;

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
        final View view= inflater.inflate(R.layout.fragment_buscador, container, false);

        buscar = (Button) view.findViewById(R.id.Buscar);
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Bundle bundle = new Bundle();
                bundle.putString("fechaInicial", fBInicial.getText().toString()+" "+hBinicial.getText().toString());
                bundle.putString("fechaFinal", fBFinal.getText().toString()+" "+hBFinal.getText().toString());

                Fragment myObj = new CochesBuscadorFragment();
                myObj.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main,myObj).addToBackStack(null).commit();

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

                    if(year< yy || (monthOfYear+1)<mm){
                        Toast toast2 = Toast.makeText(getActivity().getApplicationContext(), "Seleccione una fecha valida" , Toast.LENGTH_SHORT);
                        toast2.show();
                    }
                    else{

                        String fecha = String.valueOf(dayOfMonth) +"/"+String.valueOf(monthOfYear+1)
                                +"/"+String.valueOf(year);
                        fBInicial.setText(fecha);
                        fBFinal.setText("");
                        hBFinal.setText("");
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
                    String hora = hourOfDay + ":" + minute;
                    hBinicial.setText(hora);
                }
            },hora,minutos,true);
            timePickerDialog.show();
        }



        if(v==fBFinal){


            DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    if(year< yy ||  (monthOfYear+1)<mm ){

                        Toast toast2 = Toast.makeText(getActivity().getApplicationContext(), "Seleccione una fecha valida" , Toast.LENGTH_SHORT);
                        toast2.show();
                    }
                    else{

                        String fecha = String.valueOf(dayOfMonth) +"/"+String.valueOf(monthOfYear+1)
                                +"/"+String.valueOf(year);

                        if(!fecha.equals(fBInicial.getText().toString()) || fecha.equals(fBInicial.getText().toString())){
                            fBFinal.setText(fecha);
                        }

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
                    String hora = hourOfDay + ":" + minute;

                    hBFinal.setText(hora);


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
