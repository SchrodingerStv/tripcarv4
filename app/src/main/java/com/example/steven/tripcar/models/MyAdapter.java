package com.example.steven.tripcar.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.steven.tripcar.R;

public class MyAdapter extends ArrayAdapter<String>  {
    Double[] precio;
    String[] marcamodelo;
    String[] tamanio;
    String[] matricula;
    Bitmap[] imagenes;
    Context mContext;
    private String URL = "http://192.168.1.38/ServicioRestTripCar/Api/Coches";
    public MyAdapter(Context context, String[] marcamodelo,String[] tamanio,String[] matricula, Double[] precio, Bitmap[] bitmaps) {
        super(context, R.layout.listview_item);
        this.matricula = matricula;
        this.tamanio = tamanio;
        this.marcamodelo = marcamodelo;
        this.imagenes = bitmaps;
        this.mContext = context;
        this.precio = precio;
    }

    @Override
    public int getCount() {
        return marcamodelo.length;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.listview_item, parent, false);

            mViewHolder.mPrecio = (TextView) convertView.findViewById(R.id.precio);
            mViewHolder.mImagen = (ImageView) convertView.findViewById(R.id.imageView);
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.marcaModelo);
            mViewHolder.mtamanio = (TextView)convertView.findViewById(R.id.tamanio);
            mViewHolder.matricula = (TextView) convertView.findViewById(R.id.matricula);
            mViewHolder.mSleccionar = (Button)convertView.findViewById((R.id.seleccionarCoche));
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mPrecio.setText(precio[position].toString()+"€");
        mViewHolder.mImagen.setImageBitmap(imagenes[position]);
        mViewHolder.mName.setText(marcamodelo[position]);
        mViewHolder.mtamanio.setText(tamanio[position]);
        mViewHolder.matricula.setText(matricula[position]);
        Button btnSeleccionar = (Button)convertView.findViewById(R.id.seleccionarCoche);
        btnSeleccionar.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                int pos = position;
                FragmentActivity fa = (FragmentActivity)(getContext());

                SharedPreferences prefe=fa.getSharedPreferences("UsuarioEmail", Context.MODE_PRIVATE);
                String d=prefe.getString("Email", "");
                if (d.length()==0) {
                    LoginFragment f = new LoginFragment();
                    FragmentTransaction ft = fa.getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_main,f).commit();
                }
                else {

                    CocheSelectFragment coche =  new CocheSelectFragment();
                    FragmentTransaction ft = fa.getSupportFragmentManager().beginTransaction();
                    SharedPreferences preferencias= fa.getSharedPreferences("Matricula", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferencias.edit();
                    editor.putString("Matricula",matricula[pos]);
                    editor.commit();
                    ft.replace(R.id.content_main,coche).addToBackStack(null).commit();

                }

            }
        });
        return convertView;
    }


    static class ViewHolder {
        ImageView mImagen;
        TextView mName;
        TextView mtamanio;
        TextView matricula;
        TextView mPrecio;
        Button mSleccionar;

    }
}
