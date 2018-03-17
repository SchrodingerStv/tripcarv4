package com.example.steven.tripcar.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.steven.tripcar.R;
import com.example.steven.tripcar.services.cochesService;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import com.example.steven.tripcar.services.cochesService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CocheSelectFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CocheSelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CocheSelectFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String  baseUrl= "http://192.168.1.38/SWTRIPCAR/";
    private  cochesService cochesService;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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

        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();




        SharedPreferences prefe=getActivity().getSharedPreferences("Matricula", Context.MODE_PRIVATE);
        String d=prefe.getString("Matricula", "");
        Gson gson = new Gson();

        final Coche  obj = gson.fromJson(d, Coche.class);
        cochesService = retrofit.create(cochesService.class);
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        formatSymbols.setDecimalSeparator(',');
        final DecimalFormat df = new DecimalFormat("####,####.##", formatSymbols);
        Call<Coche> u = cochesService.obtenerCoche(obj.getMatricula());
        u.enqueue(new Callback<Coche>() {
            @Override
            public void onResponse(Call<Coche> call, Response<Coche> response) {
                if(response.isSuccessful()) {

                    TextView txtMatricula = (TextView) getActivity().findViewById(R.id.matriculaSelect);
                    TextView txtTamanio = (TextView) getActivity().findViewById(R.id.tamanioSelect);
                    TextView txtPrecio = (TextView) getActivity().findViewById(R.id.precioSelect);
                    TextView txtMarca = (TextView) getActivity().findViewById(R.id.marcaModeloSelect);
                    txtMatricula.setText(obj.getMatricula());
                    txtMarca.setText(obj.getMarcaModelo());
                    txtPrecio.setText(df.format(obj.getPrecioDia())+"€");
                    txtTamanio.setText(obj.getTamanio());
                    InputStream srt = null;
                    try {
                        srt = new java.net.URL(obj.getImagen()).openStream();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bitmap bit = BitmapFactory.decodeStream(srt);

                    ImageView img = (ImageView) getActivity().findViewById(R.id.imagenCoche) ;

                    img.setImageBitmap(bit);
                }

            }

            @Override
            public void onFailure(Call<Coche> call, Throwable t) {

            }
        });




        return  view;
    }
    private class TareaObtenerCoche extends AsyncTask<String,Integer,Boolean> {


        private String matricula;
        private String imagen;
        private Bitmap bitmap;
        private InputStream srt = null;
        private String marcamodelo;
        private String  tamanio;
        private Double decimal;

        protected Boolean doInBackground(String... params) {

            boolean result = true;

            HttpClient httpClient = new DefaultHttpClient();

            //String matricula = params[0];

            SharedPreferences prefe=getActivity().getSharedPreferences("Matricula", Context.MODE_PRIVATE);
            String d=prefe.getString("Matricula", "");
            HttpGet del = new HttpGet("http://192.168.1.38/ServicioRestTripCar/Api/Coches/Coche/" +d);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);

                marcamodelo = respJSON.getString("MarcaModelo");

                tamanio = respJSON.getString("Tamanio");

                imagen  = respJSON.getString("Imagen");

                matricula  = respJSON.getString("Matricula");

                decimal  = respJSON.getDouble("PrecioDia");


                try {
                    srt = new java.net.URL(imagen).openStream();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                bitmap = BitmapFactory.decodeStream(srt);


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

                TextView txtMatricula = (TextView) getActivity().findViewById(R.id.matriculaSelect);
                TextView txtTamanio = (TextView) getActivity().findViewById(R.id.tamanioSelect);
                TextView txtPrecio = (TextView) getActivity().findViewById(R.id.marcaModeloSelect);
                TextView txtMarca = (TextView) getActivity().findViewById(R.id.precioSelect);
                txtMatricula.setText(matricula);
                txtMarca.setText(marcamodelo);
                txtPrecio.setText(decimal.toString());
                txtTamanio.setText(tamanio);
                ImageView img = (ImageView) getActivity().findViewById(R.id.imagenCoche) ;
                img.setImageBitmap(bitmap);

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
