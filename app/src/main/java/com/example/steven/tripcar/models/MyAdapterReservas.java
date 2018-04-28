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
import android.widget.Toast;

import com.example.steven.tripcar.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class MyAdapterReservas extends ArrayAdapter<usuarioCocheReserva>  {


    public MyAdapterReservas(Context context, List<usuarioCocheReserva> lista) {
        super(context,0,lista);


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final usuarioCocheReserva usaRC = getItem(position);
        InputStream srt = null;
        try {
             srt = new java.net.URL(usaRC.getUriImagen()).openStream();

        } catch (IOException e) {
            e.printStackTrace();
        }
       Bitmap bit = BitmapFactory.decodeStream(srt);
        double precioHora = Double.parseDouble(usaRC.getPrecioTotal());
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        formatSymbols.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("####,####.##", formatSymbols);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listview_item_reservas, parent, false);
            viewHolder.mName = (TextView) convertView.findViewById(R.id.marcaModeloR);
            viewHolder.mtamanio = (TextView) convertView.findViewById(R.id.tamanioR);
            viewHolder.mPrecio = (TextView) convertView.findViewById(R.id.precioR);
            viewHolder.matricula = (TextView)convertView.findViewById(R.id.matriculaR);
            viewHolder.mImagen = (ImageView) convertView.findViewById(R.id.imageViewR);
            viewHolder.mCancelar = (Button) convertView.findViewById(R.id.cancelar);
            viewHolder.mFechaFinal = (TextView) convertView.findViewById(R.id.fechaHoraFinal);
            viewHolder.mFechaInicio = (TextView) convertView.findViewById(R.id.fecaHoraInicial);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.mName.setText(usaRC.getMarcaModelo());
        viewHolder.matricula.setText(usaRC.getMatricula());
        viewHolder.mPrecio.setText(df.format(precioHora) + " €");
        viewHolder.mtamanio.setText(usaRC.getTamanio());
        viewHolder.mImagen.setImageBitmap(bit);
        viewHolder.mFechaInicio.setText(usaRC.getfInicio());
        viewHolder.mFechaFinal.setText(usaRC.getfFinal());
        viewHolder.mCancelar.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                
                final FragmentActivity fa = (FragmentActivity)(getContext());

                SharedPreferences prefe=fa.getSharedPreferences("UsuarioEmail", Context.MODE_PRIVATE);
                String d=prefe.getString("Email", "");
                if (d.length()==0) {
                    LoginFragment f = new LoginFragment();
                    FragmentTransaction ft = fa.getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_main,f).commit();
                }
                else {
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference ref = database.getReference(FirebaseReferences.RESERVAS_REFERENCE);
                    ref.orderByChild("idReserva").equalTo(usaRC.getIdReserva()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot child: dataSnapshot.getChildren()) {
                                ref.child(child.getKey()).removeValue();
                                Toast toast2 = Toast.makeText(fa.getApplicationContext(), "Reserva cancelada", Toast.LENGTH_LONG);
                                toast2.show();
                            }
                            GestionReservasFragment f = new GestionReservasFragment();
                            FragmentTransaction ft = fa.getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_main,f).commit();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



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
        TextView mFechaInicio;
        TextView mFechaFinal;
        Button mCancelar;

    }
}
