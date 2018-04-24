package com.example.steven.tripcar.models;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.steven.tripcar.R;
import com.example.steven.tripcar.services.usuariosService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    private String  baseUrl= "http://10.111.60.105/SWTRIPCAR/";
    usuariosService usuariosService;
    private String  URL= "http://192.168.1.38/ServicioRestTripCar/Api/Usuarios/Usuario/";
    private String URL2 = "http://10.111.60.105/ServicioRestTripCar/Api/Usuarios/Usuario/";
    private EditText txtUserEmail;
    private String contrasenia;
    private String email;
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

        /*Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        usuariosService = retrofit.create(usuariosService.class);
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Usuario usuario = new Usuario();

                usuario.setEmail(txtUserEmail.getText().toString());
                usuario.setContrasenia(txtUserContrasenia.getText().toString());

                obtenerUsuario(usuario);


            }
        });*/

        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Usuario usuario = new Usuario();

                usuario.setEmail(txtUserEmail.getText().toString());
                usuario.setContrasenia(txtUserContrasenia.getText().toString());

                Logearse(usuario);


            }
        });
        Button btnRegister = (Button)view.findViewById(R.id.registro);
        btnRegister.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                SharedPreferences prefe=getActivity().getSharedPreferences("UsuarioEmail", Context.MODE_PRIVATE);
                String d=prefe.getString("Email", "");
                if (d.length()==0) {
                    RegistroFragment fragment  = new RegistroFragment();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_main,fragment).addToBackStack(null).commit();
                }
                else {

                   BienvenidoFragment fragment = new BienvenidoFragment();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_main,fragment).addToBackStack(null).commit();
                }



            }
        });



        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void Logearse(final Usuario usuario){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final DatabaseReference ref = database.getReference(FirebaseReferences.USUARIOS_REFERENCE);
        final StorageReference refImag = storage.getReference(FirebaseReferences.IMAGENES_USUARIOS_REFERENCE);

        ref.orderByChild("email").equalTo(usuario.email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean existe = dataSnapshot.exists();

                if(existe){

                    NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                    View headerView = navigationView.getHeaderView(0);
                    TextView email  = (TextView)headerView.findViewById(R.id.emailLog);
                    email.setText(usuario.email);
                    LinearLayout imagenUsuario = (LinearLayout) headerView.findViewById(R.id.side_nav);
                    InputStream srt = null;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Usuario post =postSnapshot.getValue(Usuario.class);
                        Usuario btChildDetails = new Usuario(post.getEmail(),post.getContrasenia(),post.getDNI(),post.getImagenUri());
                        if(btChildDetails.getContrasenia().toString().equals(usuario.contrasenia) ){

                            Toast toast1 = Toast.makeText(getActivity().getApplicationContext(), "Bienvenido, "+btChildDetails.email, Toast.LENGTH_LONG);
                            toast1.show();

                            Bitmap bitmap = null;

                            try {

                                srt = new java.net.URL(btChildDetails.imagenUri).openStream();
                                bitmap = BitmapFactory.decodeStream(srt);
                            } catch (IOException e) {
                                Log.e("aaa", e.getMessage());
                            }
                            int dimensionInPixel = 200;


                            Drawable d = new BitmapDrawable(getResources(), bitmap);
                            imagenUsuario.setBackground(d);
                            navigationView.getMenu().findItem(R.id.nav_exit).setVisible(true);
                            navigationView.getMenu().findItem(R.id.nav_gestion).setVisible(true);
                            SharedPreferences preferencias=getActivity().getSharedPreferences("UsuarioEmail", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=preferencias.edit();
                            editor.putString("Email",usuario.email);
                            editor.commit();
                            BienvenidoFragment fragment  = new BienvenidoFragment();
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.content_main,fragment).addToBackStack(null).commit();

                        }else{
                            Toast toast1 = Toast.makeText(getActivity().getApplicationContext(), "El usuario no existe o los datos son incorrectos", Toast.LENGTH_LONG);
                            toast1.show();
                        }


                        //Picasso.with(getActivity().getApplicationContext()).load(btChildDetails.imagenUri).into(imagenUsuario);
                    }




                }




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    /*
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
*/
   /*
    public void obtenerUsuario(final Usuario usuario) {

        final ProgressDialog dialog = new ProgressDialog(getActivity());

        dialog.setMessage("Ingresando");
        dialog.show();
        Call<Usuario> u = usuariosService.obtenerusario(usuario.getEmail(),usuario.getContrasenia());
        u.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if(response.isSuccessful()){

                    String usuarioEmail = usuario.getEmail();
                    Toast toast1 =Toast.makeText(getActivity().getApplicationContext(),"Datos correctos,"+usuarioEmail, Toast.LENGTH_SHORT);
                    toast1.show();
                    NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                    View headerView = navigationView.getHeaderView(0);
                    TextView email  = (TextView)headerView.findViewById(R.id.emailLog);
                    email.setText(usuarioEmail);
                    TextView nombre  = (TextView)headerView.findViewById(R.id.nombreLog);
                    nombre.setText("");
                    navigationView.getMenu().findItem(R.id.nav_exit).setVisible(true);
                    navigationView.getMenu().findItem(R.id.nav_gestion).setVisible(true);

                    SharedPreferences preferencias=getActivity().getSharedPreferences("UsuarioEmail", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferencias.edit();
                    editor.putString("Email",usuarioEmail);
                    editor.commit();
                    BienvenidoFragment fragment  = new BienvenidoFragment();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_main,fragment).addToBackStack(null).commit();
                }
                if(dialog.isShowing()){
                    dialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast toast1 = Toast.makeText(getActivity().getApplicationContext(),"Datos incorrectos", Toast.LENGTH_SHORT);
                toast1.show();
                if (dialog.isShowing()) {

                    dialog.dismiss();
                }
            }
        });
    }
*/

   //login firebase



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
