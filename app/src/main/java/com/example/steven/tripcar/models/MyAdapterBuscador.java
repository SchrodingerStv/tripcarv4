package com.example.steven.tripcar.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyAdapterBuscador extends ArrayAdapter<usuarioCocheReserva>  {


    public MyAdapterBuscador(Context context, List<usuarioCocheReserva> lista) {
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
            viewHolder.mFechaFinalizacion = (TextView)convertView.findViewById(R.id.fechaHoraFinalizacion);
            viewHolder.mFinalizar = (Button) convertView.findViewById(R.id.finalizar);


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
        viewHolder.mFechaFinalizacion.setText(usaRC.getfFinalizacion());
        viewHolder.mFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentActivity fa = (FragmentActivity)(getContext());
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference ref = database.getReference(FirebaseReferences.RESERVAS_REFERENCE);
                ref.orderByChild("idReserva").equalTo(usaRC.getIdReserva()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                         boolean finalizo = false;
                        for (DataSnapshot child: dataSnapshot.getChildren()) {

                            String fecha =  usaRC.getfInicio();
                            String ff = usaRC.getfFinal();
                            String fff= usaRC.getfFinalizacion();
                            String pattern = "dd/MM/yyyy HH:mm";
                            SimpleDateFormat format = new SimpleDateFormat(pattern);
                            DateFormat writeFormat = new SimpleDateFormat( pattern);
                            try {

                                String fechaActual = format.format(new Date());
                                Date ff2 = format.parse(ff);
                                Date fechaEliminar = format.parse(fecha);
                                Date actual = format.parse(fechaActual);
                                String fomafi = writeFormat.format(actual);
                                int x2 = (int) ((actual.getTime()-fechaEliminar.getTime())/1000)/3600;

                                Log.e("dato2: ", String.valueOf(x2));

                                if(actual.after(fechaEliminar) && x2>1 && fechaEliminar.before(ff2) && fff.equals("")){
                                    Toast toast2 = Toast.makeText(fa.getApplicationContext(), "Reserva finalizada", Toast.LENGTH_LONG);
                                    toast2.show();
                                   ref.child(child.getKey()).child("fFinalizacion").setValue(fomafi);
                                   finalizo=true;
                                }
                                else if(fechaEliminar.getTime()<=actual.getTime() || x2<1 || !fff.equals("")){
                                    Toast toast2 = Toast.makeText(fa.getApplicationContext(), "No puedes finalizar esta reserva", Toast.LENGTH_LONG);
                                    toast2.show();


                                }


                                Log.e("fechaI actual: ",actual.toString());
                                Log.e("fechaI select: ",fechaEliminar.toString());


                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                        if (finalizo==true){
                            GestionReservasFragment f = new GestionReservasFragment();
                            FragmentTransaction ft = fa.getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_main,f).addToBackStack(null).commit();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        viewHolder.mCancelar.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                
                final FragmentActivity fa = (FragmentActivity)(getContext());

                SharedPreferences prefe=fa.getSharedPreferences("UsuarioEmail", Context.MODE_PRIVATE);
                String d=prefe.getString("Email", "");

                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference ref = database.getReference(FirebaseReferences.RESERVAS_REFERENCE);
                    ref.orderByChild("idReserva").equalTo(usaRC.getIdReserva()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            boolean cancelo = false;
                            for (DataSnapshot child: dataSnapshot.getChildren()) {

                                String fecha =  usaRC.getfInicio();
                                String fff = usaRC.getfFinalizacion();
                                String pattern = "dd/MM/yyyy HH:mm";
                                SimpleDateFormat format = new SimpleDateFormat(pattern);
                                try {

                                    String fechaActual = format.format(new Date());
                                    Date fechaEliminar = format.parse(fecha);
                                    Date actual = format.parse(fechaActual);
                                    int x2 = (int) ((actual.getTime()-fechaEliminar.getTime())/1000)/3600;
                                    if(actual.before(fechaEliminar) && fff.equals("")){
                                        Toast toast2 = Toast.makeText(fa.getApplicationContext(), "Reserva cancelada", Toast.LENGTH_LONG);
                                        toast2.show();
                                        cancelo=true;
                                        ref.child(child.getKey()).removeValue();
                                    }
                                    else if(fechaEliminar.equals(actual)&& x2<1 && fff.equals("")){
                                        Toast toast2 = Toast.makeText(fa.getApplicationContext(), "Reserva cancelada", Toast.LENGTH_LONG);
                                        toast2.show();
                                        cancelo=true;
                                        ref.child(child.getKey()).removeValue();
                                    }
                                    else if(actual.after(fechaEliminar) || !fff.equals("")){
                                        Toast toast2 = Toast.makeText(fa.getApplicationContext(), "No puedes cancelar esta reserva", Toast.LENGTH_LONG);
                                        toast2.show();
                                    }

                                    Log.e("fechaI actual: ",actual.toString());
                                    Log.e("fechaI select: ",fechaEliminar.toString());


                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                            if(cancelo==true){
                                GestionReservasFragment f = new GestionReservasFragment();
                                FragmentTransaction ft = fa.getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_main,f).addToBackStack(null).commit();
                            }
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



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
        TextView mFechaFinalizacion;
        Button mFinalizar;
        Button mCancelar;

    }
}
