package com.example.steven.tripcar.models;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
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
import static android.app.Activity.RESULT_OK;
import com.example.steven.tripcar.services.usuariosService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;


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
    private String  baseUrl= "http://10.111.60.105/SWTRIPCAR/";
    private EditText txtEmail;
    private EditText txtNombre;
    private EditText txtDNI;
    private EditText txtContrasenia;
    Comunicator comunicator;
    usuariosService usuariosService;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //imagen
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private ImageView mImageView;
    //private Button mButtonImagen;


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
        Button mButtonImagen = (Button) view.findViewById(R.id.elegir_imagen);
        Button btnRegister = (Button) view.findViewById(R.id.registrarse);
        Button btnLogin = (Button)view.findViewById(R.id.logearse);
        txtEmail = (EditText) view.findViewById(R.id.email);
       // txtNombre = (EditText) view.findViewById(R.id.nombre);
        txtDNI = (EditText) view.findViewById(R.id.dni);
        txtContrasenia = (EditText) view.findViewById(R.id.contrasenia);
        mImageView = (ImageView) view.findViewById(R.id.imagenUsuario);


        //imagen
        mButtonImagen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
        //firebase
        btnRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (txtDNI.getText().toString().equals("") ){
                    RegistroFragment fragment  = new RegistroFragment();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_main,fragment).addToBackStack(null).commit();
                }
                else{
                    Usuario usuario = new Usuario();
                    usuario.setDNI(Integer.parseInt(txtDNI.getText().toString()));
                    usuario.setEmail( txtEmail.getText().toString() );
                    usuario.setContrasenia(txtContrasenia.getText().toString());

                    registrar(usuario);
                }



            }
        });





       /* Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
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
        });*/
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                LoginFragment fragment  = new LoginFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content_main,fragment).addToBackStack(null).commit();
            }
        });


        return view;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //mImageView = (ImageView) getActivity().findViewById(R.id.imagenUsuario);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            try {
                Bitmap bitmap =  MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mImageUri);
                mImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


            //Picasso.with(getActivity().getApplication()).load(mImageUri).into(mImageView);

        }
    }

    private void registrar(final Usuario usuario){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final DatabaseReference ref = database.getReference(FirebaseReferences.USUARIOS_REFERENCE);
        final StorageReference refImag = storage.getReference(FirebaseReferences.IMAGENES_USUARIOS_REFERENCE);
        ref.orderByChild("email").equalTo(usuario.email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean existe = dataSnapshot.exists();
                if(existe){

                    Toast toast1 =Toast.makeText(getActivity().getApplicationContext(),"Error al registrarse el usuario ya existe", Toast.LENGTH_SHORT);
                    toast1.show();

                }
                else{
                    refImag.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String url = taskSnapshot.getDownloadUrl().toString();
                            usuario.setImagenUri(url);
                            ref.push().setValue(usuario);
                            Toast toast1 = Toast.makeText(getActivity().getApplicationContext(), "Registro con exito", Toast.LENGTH_SHORT);
                            toast1.show();
                            LoginFragment fragment  = new LoginFragment();
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.content_main,fragment).addToBackStack(null).commit();
                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



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
/*
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

                }
            }
        });
    }
*/

    //Registro con Firebase


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
