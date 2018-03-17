package com.example.steven.tripcar.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.steven.tripcar.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BienvenidoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BienvenidoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CochesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String URL = "http://192.168.1.38/ServicioRestTripCar/Api/Coches";
    private String URL2 = "http://10.111.60.105/ServicioRestTripCar/Api/Coches";
    private OnFragmentInteractionListener mListener;
    private ListView mListView;

    public CochesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BienvenidoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CochesFragment newInstance(String param1, String param2) {
        CochesFragment fragment = new CochesFragment();
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
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_coches, container, false);
        mListView =(ListView)view.findViewById(R.id.listview);
        TareaObtenerCoches tarea = new TareaObtenerCoches();
        tarea.execute();

        return view;
    }
    private class TareaObtenerCoches extends AsyncTask<String,Integer,Boolean> {

        private String[] coches;
        private Bitmap[] bits;
        private String[] img;
        private String [] marcamodelo;
        private String [] matricula;
        private String [] tamanio;
        private Double[] decimal;
        private InputStream srt = null;
        private Bitmap[] imagen = null;

        protected Boolean doInBackground(String... params) {

            boolean result = true;


            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =
                    new HttpGet(URL);

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                coches = new String[respJSON.length()];
                img = new String[respJSON.length()];
                bits = new Bitmap[respJSON.length()];
                imagen  =  new Bitmap[respJSON.length()];
                marcamodelo = new String[respJSON.length()];
                tamanio = new String[respJSON.length()];
                matricula = new String[respJSON.length()];
                decimal = new Double[respJSON.length()];
                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);
                    String marca = obj.getString("MarcaModelo");
                    marcamodelo[i] = marca;
                    String tamanyo = obj.getString("Tamanio");
                    tamanio[i] = tamanyo;
                    String imagenes  = obj.getString("Imagen");
                    img[i] = imagenes;
                    String matric  = obj.getString("Matricula");
                    matricula[i]=matric;
                    Double decim  = obj.getDouble("PrecioDia");
                    decimal[i]=decim;

                    try {
                        srt = new java.net.URL(img[i]).openStream();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    bits[i] = BitmapFactory.decodeStream(srt);


                    imagen[i] = bits[i];
                }
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                result = false;
            }

            return result;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {

                //ImageView image = (ImageView)getActivity().findViewById(R.id.imagenView);
                //image.setImageBitmap(imagen[1]);
                MyAdapter myAdapter = new MyAdapter(getActivity(), marcamodelo,tamanio,matricula,decimal, imagen);
                mListView.setAdapter(myAdapter);



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
