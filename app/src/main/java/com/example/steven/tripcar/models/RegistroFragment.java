package com.example.steven.tripcar.models;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.steven.tripcar.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.steven.tripcar.services.usuariosService;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegistroFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegistroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistroFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String  baseUrl= "http://192.168.1.38/SWTRIPCAR/";
    private EditText txtEmail;
    private EditText txtNombre;
    private EditText txtDNI;
    private EditText txtContrasenia;
    Comunicator comunicator;
    usuariosService usuariosService;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RegistroFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistroFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistroFragment newInstance(String param1, String param2) {
        RegistroFragment fragment = new RegistroFragment();
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_registro, container, false);

        Button btnRegister = (Button) view.findViewById(R.id.registrarse);
        Button btnLogin = (Button)view.findViewById(R.id.logearse);
        txtEmail = (EditText) view.findViewById(R.id.email);
        txtNombre = (EditText) view.findViewById(R.id.nombre);
        txtDNI = (EditText) view.findViewById(R.id.dni);
        txtContrasenia = (EditText) view.findViewById(R.id.contrasenia);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        usuariosService = retrofit.create(usuariosService.class);

        btnRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                    Usuario usuario = new Usuario();
                    usuario.setNombre( txtNombre.getText().toString());
                    usuario.setDNI(Integer.parseInt(txtDNI.getText().toString()));
                    usuario.setEmail( txtEmail.getText().toString() );
                    usuario.setContrasenia(txtContrasenia.getText().toString());
                    insertarUsuario(usuario);


            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                LoginFragment fragment  = new LoginFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content_main,fragment).addToBackStack(null).commit();
            }
        });
        return view;
    }
/*
    private class TareaInsertarUsuario extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean result = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new HttpPost(URL);
            post.setHeader("content-type", "application/json");

            try {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();

                //dato.put("Id", Integer.parseInt(txtId.getText().toString()));
                dato.put("email", params[0]);
                dato.put("nombre", params[1]);
                dato.put("contrasenia", params[2]);
                dato.put("dni", Integer.parseInt(params[3]));

                StringEntity entity = new StringEntity(dato.toString());
                post.setEntity(entity);

                HttpResponse resp = httpClient.execute(post);
                String respStr = EntityUtils.toString(resp.getEntity());

                if (!respStr.equals("true"))
                    result = false;
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                result = false;
            }

            return result;
        }
        protected void onPostExecute(Boolean result) {

            if (result) {
                Toast toast1 =Toast.makeText(getActivity().getApplicationContext(),"Registro con exito", Toast.LENGTH_SHORT);
                toast1.show();
                LoginFragment fragment  = new LoginFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content_main,fragment).addToBackStack(null).commit();


            } else {
                Toast toast1 = Toast.makeText(getActivity().getApplicationContext(),"Error al registrarse", Toast.LENGTH_SHORT);
                toast1.show();

            }
        }
    }
*/

    public void insertarUsuario(Usuario usuario) {

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Registrando");
        dialog.show();
        Call<Usuario> u = usuariosService.insertrUsuario(usuario);
        u.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if(response.isSuccessful()){
                    //Usuario pacienteResponse = response.body();
                    Toast toast1 =Toast.makeText(getActivity().getApplicationContext(),"Registro con exito", Toast.LENGTH_SHORT);
                    toast1.show();
                    LoginFragment fragment  = new LoginFragment();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_main,fragment).addToBackStack(null).commit();

                }
                if(dialog.isShowing()){
                    dialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    Toast toast1 = Toast.makeText(getActivity().getApplicationContext(),"Error al registrarse", Toast.LENGTH_SHORT);
                    toast1.show();
                }
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
            //comunicator = (Comunicator) context;
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
    public interface Comunicator{
        void insertarUsuario(Usuario usario);
    }
}
