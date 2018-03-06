package com.example.steven.tripcar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String  URL= "http://192.168.1.38/ServicioRestTripCar/Api/Usuarios/Usuario/";
    private String URL2 = "http://10.111.60.105/ServicioRestTripCar/Api/Usuarios/Usuario/";
    private EditText txtUserEmail;

    private EditText txtUserContrasenia;
    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        final View view = inflater.inflate(R.layout.fragment_login,container,false);
        Button btnLogin = (Button)view.findViewById(R.id.ingresar);
        txtUserEmail = (EditText) view.findViewById(R.id.Email);
        txtUserContrasenia = (EditText) view.findViewById(R.id.Contrasenia);
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                TareaComprobarUsuario tarea = new TareaComprobarUsuario();
                tarea.execute(
                        txtUserEmail.getText().toString(),
                        txtUserContrasenia.getText().toString());



            }
        });
        Button btnRegister = (Button)view.findViewById(R.id.registro);
        btnRegister.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {

                RegistroFragment fragment  = new RegistroFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content_main,fragment).addToBackStack(null).commit();


            }
        });
        return view;
    }
    private class TareaComprobarUsuario extends AsyncTask<String,Integer,Boolean> {


            private String usuarioEmail;
            private String usuarioNombre;


        protected Boolean doInBackground(String... params) {

            boolean result = true;

            HttpClient httpClient = new DefaultHttpClient();

            String email = params[0];
            String conrasenia = params[1];

            HttpGet del = new HttpGet(URL +email+"/"+conrasenia);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);

                usuarioEmail = respJSON.getString("Email");
                usuarioNombre = respJSON.getString("Nombre");


            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                result = false;
            }

            return result;
        }
        protected void onPostExecute(Boolean result) {

            if (result) {
                Toast toast1 =Toast.makeText(getActivity().getApplicationContext(),"Datos correctos,"+usuarioEmail, Toast.LENGTH_SHORT);
                toast1.show();
                NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                View headerView = navigationView.getHeaderView(0);
                TextView email  = (TextView)headerView.findViewById(R.id.emailLog);
                email.setText(usuarioEmail);
                TextView nombre  = (TextView)headerView.findViewById(R.id.nombreLog);
                nombre.setText(usuarioNombre);
                navigationView.getMenu().findItem(R.id.nav_exit).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_gestion).setVisible(true);

                SharedPreferences preferencias=getActivity().getSharedPreferences("UsuarioEmail", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=preferencias.edit();
                editor.putString("Email",usuarioEmail);
                editor.commit();
                BienvenidoFragment fragment  = new BienvenidoFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content_main,fragment).addToBackStack(null).commit();


            } else {

                Toast toast1 = Toast.makeText(getActivity().getApplicationContext(),"Datos incorrectos", Toast.LENGTH_SHORT);
                toast1.show();

            }
        }
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
