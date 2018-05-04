package com.example.steven.tripcar.models;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.steven.tripcar.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;



public class RegistroFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EditText txtEmail;
    private EditText txtNombre;
    private EditText txtDNI;
    private EditText txtContrasenia;
    private Button mQuitarImagen;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //imagen
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private de.hdodenhof.circleimageview.CircleImageView mImageView;
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
        mQuitarImagen = (Button) view.findViewById(R.id.quitarImagen);
        mQuitarImagen.setVisibility(View.INVISIBLE);
        mQuitarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageUri=null;
                mImageView.setImageBitmap(null);
                mQuitarImagen.setVisibility(View.INVISIBLE);
            }
        });

        txtDNI = (EditText) view.findViewById(R.id.dni);
        txtContrasenia = (EditText) view.findViewById(R.id.contrasenia);
        mImageView = (de.hdodenhof.circleimageview.CircleImageView) view.findViewById(R.id.imagenUsuario);


        //imagen
        mButtonImagen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            2000);
                }
                else {
                    captureImage();
                }
            }


        });

        //firebase
        btnRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (txtDNI.getText().toString().equals("") || txtEmail.getText().toString().equals("") ||  txtContrasenia.getText().toString().equals("")){
                    Toast toast1 =Toast.makeText(getActivity().getApplicationContext(),"Rellene todos los datos", Toast.LENGTH_LONG);
                    toast1.show();
                }
                else{
                    Usuario usuario = new Usuario();
                    if(!validarEmail(txtEmail.getText().toString())){
                        txtEmail.setError("Email no válido");
                    }

                    else{

                        usuario.setDNI(Integer.parseInt(txtDNI.getText().toString()));
                        usuario.setEmail( txtEmail.getText().toString() );
                        usuario.setContrasenia(txtContrasenia.getText().toString());
                        registrar(usuario);
                    }



                }



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
    private void captureImage() {
        Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);


        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, PICK_IMAGE_REQUEST);
        }
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(resultCode == RESULT_OK) {
                if(requestCode == PICK_IMAGE_REQUEST){
                     mImageUri = data.getData();
                    Bitmap bitmapImage = null;
                    try {
                        bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mImageUri);
                        mImageView.setImageBitmap(bitmapImage);
                        mQuitarImagen.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
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

                    Toast toast1 =Toast.makeText(getActivity().getApplicationContext(),"Error al registrarse el usuario ya existe", Toast.LENGTH_LONG);
                    toast1.show();

                }
                else{
                    if(mImageUri!=null){
                        refImag.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                String url = taskSnapshot.getDownloadUrl().toString();
                                usuario.setImagenUri(url);
                                ref.push().setValue(usuario);
                                Toast toast1 = Toast.makeText(getActivity().getApplicationContext(), "Registro con exito", Toast.LENGTH_LONG);
                                toast1.show();
                                LoginFragment fragment  = new LoginFragment();
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.content_main,fragment).addToBackStack(null).commit();
                            }
                        });
                    }
                    else {
                        usuario.setImagenUri("");
                        ref.push().setValue(usuario);
                        Toast toast1 = Toast.makeText(getActivity().getApplicationContext(), "Registro con exito", Toast.LENGTH_LONG);
                        toast1.show();
                        LoginFragment fragment  = new LoginFragment();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.content_main,fragment).addToBackStack(null).commit();
                    }


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


}
