package com.example.steven.tripcar.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class MyAdapter extends ArrayAdapter<Coche>  {
    Double[] precio;
    String[] marcamodelo;
    String[] tamanio;
    String[] matricula;
    Bitmap[] imagenes;
    Context mContext;
    List<Coche> lista;
    public MyAdapter(Context context,List<Coche> lista) {
        super(context,0,lista);


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Coche auto = getItem(position);
        InputStream srt = null;
        try {
             srt = new java.net.URL(auto.getUriImagen()).openStream();

        } catch (IOException e) {
            e.printStackTrace();
        }
       Bitmap bit = BitmapFactory.decodeStream(srt);
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        formatSymbols.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("####,####.##", formatSymbols);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
            viewHolder.mName = (TextView) convertView.findViewById(R.id.marcaModelo);
            viewHolder.mtamanio = (TextView) convertView.findViewById(R.id.tamanio);
            viewHolder.mPrecio = (TextView) convertView.findViewById(R.id.precio);
            viewHolder.matricula = (TextView)convertView.findViewById(R.id.matricula);
            viewHolder.mImagen = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.mSleccionar = (Button) convertView.findViewById(R.id.seleccionarCoche); 
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.mName.setText(auto.getMarcaModelo());
        viewHolder.matricula.setText(auto.getMatricula());
        viewHolder.mPrecio.setText(df.format(auto.getPrecioDia())+"€");
        viewHolder.mtamanio.setText(auto.getTamanio());
        viewHolder.mImagen.setImageBitmap(bit);
        viewHolder.mSleccionar.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                
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
                    Gson gson = new Gson();
                    String json = gson.toJson(auto);

                    SharedPreferences.Editor editor=preferencias.edit();
                    editor.putString("Matricula",json);
                    editor.commit();
                    ft.replace(R.id.content_main,coche).addToBackStack(null).commit();

                }

            }
        });
        // Return the completed view to render on screen
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
