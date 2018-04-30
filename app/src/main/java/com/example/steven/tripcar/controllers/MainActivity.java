package com.example.steven.tripcar.controllers;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.steven.tripcar.R;
import com.example.steven.tripcar.models.BienvenidoFragment;
import com.example.steven.tripcar.models.BuscadorFragment;
import com.example.steven.tripcar.models.CocheSelectFragment;
import com.example.steven.tripcar.models.CochesFragment;
import com.example.steven.tripcar.models.GestionReservasFragment;
import com.example.steven.tripcar.models.LoginFragment;
import com.example.steven.tripcar.models.RegistroFragment;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        RegistroFragment.OnFragmentInteractionListener,LoginFragment.OnFragmentInteractionListener,CochesFragment.OnFragmentInteractionListener,
        GestionReservasFragment.OnFragmentInteractionListener,BienvenidoFragment.OnFragmentInteractionListener,CocheSelectFragment.OnFragmentInteractionListener,
        BuscadorFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if(savedInstanceState == null) {
            BuscadorFragment fragment = new BuscadorFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.content_main,fragment).addToBackStack(null).commit();

        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navigationView.getMenu().findItem(R.id.nav_loing).setVisible(true);
        navigationView.getMenu().findItem(R.id.nav_exit).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_gestion).setVisible(false);
        TextView email  = (TextView)headerView.findViewById(R.id.emailLog);
        TextView nombre  = (TextView)headerView.findViewById(R.id.nombreLog);
        nombre.setText("Inicia sesión");
        email.setText("");
        navigationView.setNavigationItemSelectedListener(this);


        SharedPreferences prefe=getSharedPreferences("UsuarioEmail", Context.MODE_PRIVATE);
        String d=prefe.getString("Email", "");
        if (d.length()!=0) {
            SharedPreferences.Editor editor=prefe.edit();
            editor.clear();
            editor.commit();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        Fragment currentFragment  = null;
        boolean fragmentSeleccionado =  false;
        if (id == R.id.nav_loing) {
            SharedPreferences prefe=getSharedPreferences("UsuarioEmail", Context.MODE_PRIVATE);
            String d=prefe.getString("Email", "");
            if (d.length()==0) {
                currentFragment = new LoginFragment();
                fragmentSeleccionado = true;
            }
            else {

                currentFragment = new BienvenidoFragment();
                fragmentSeleccionado = true;

            }


        } else if (id == R.id.nav_coches) {
            currentFragment = new CochesFragment();
            fragmentSeleccionado = true;
        } else if (id == R.id.nav_gestion) {
            currentFragment = new GestionReservasFragment();
            fragmentSeleccionado = true;
        } else if(id==R.id.nav_buscar){
            currentFragment = new BuscadorFragment();
            fragmentSeleccionado = true;
        }else if (id == R.id.nav_exit) {

            SharedPreferences prefe=getSharedPreferences("UsuarioEmail", Context.MODE_PRIVATE);
            String d=prefe.getString("Email", "");
            if (d.length()==0) {
                currentFragment = new LoginFragment();
                fragmentSeleccionado = true;
            }
            else {

                SharedPreferences.Editor editor=prefe.edit();
                editor.putString("Email","");
                editor.commit();
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                View headerView = navigationView.getHeaderView(0);
                TextView email  = (TextView)headerView.findViewById(R.id.emailLog);
                TextView nombre  = (TextView)headerView.findViewById(R.id.nombreLog);
                nombre.setText("Inicia sesión");
                email.setText("");
                navigationView.getMenu().findItem(R.id.nav_exit).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_gestion).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_loing).setVisible(true);
                Uri path = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getResources().getResourcePackageName(R.drawable.ic_launcher) + '/' + getResources().getResourceTypeName(R.drawable.ic_launcher) + '/' + String.valueOf(R.drawable.ic_launcher ));
                de.hdodenhof.circleimageview.CircleImageView perfil = (de.hdodenhof.circleimageview.CircleImageView) headerView.findViewById(R.id.profile_image);

                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), path);
                    perfil.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Toast toast1 =Toast.makeText(getApplicationContext(),"Sesión cerrada", Toast.LENGTH_LONG);
                toast1.show();
                currentFragment = new LoginFragment();
                fragmentSeleccionado = true;

            }

        }
        if(fragmentSeleccionado){
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main,currentFragment).addToBackStack(null).commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public String getURLForResource (int resourceId) {
        return Uri.parse("android.resource://"+R.class.getPackage().getName()+"/" +resourceId).toString();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
