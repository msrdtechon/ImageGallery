package com.example.imagegallery;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.imagegallery.Adapter.ImgListAdapter;
import com.example.imagegallery.Model.PhotoKeyDetailsModel;
import com.example.imagegallery.ViewModel.ImgListViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    RecyclerView recyclerView;
    ImgListAdapter adapter;
    List<PhotoKeyDetailsModel> list;
    List<String> images;
    ImgListViewModel viewModel;
    Toolbar toolbar;
    private RecyclerView.LayoutManager layoutManager;
    private DrawerLayout drawer;
    public static BottomNavigationView bottomNavigationMenu;
    private NavigationView navigationView;
    TextView tvToolbarTitle;
    private TextView textView_appDevlopBy;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        images = new ArrayList<String>();
        recyclerView = findViewById(R.id.imageList);
        LinearLayoutManager layoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        viewModel = ViewModelProviders.of(this).get(ImgListViewModel.class);
        viewModel.getImageListObserver().observe(this, new Observer<List<PhotoKeyDetailsModel>>() {
            @Override
            public void onChanged(List<PhotoKeyDetailsModel> photos) {

                if(photos != null){
                    list = photos;
                    for (PhotoKeyDetailsModel p: photos){
                        images.add(p.getUrlS());
                        Log.d("TAG",p.getUrlS());
                    }
                    adapter = new ImgListAdapter(MainActivity.this, list, new ImgListAdapter.PhotoListener() {
                        @Override
                        public void onPhotoClick(String path) {
                            Intent intent = new Intent(MainActivity.this, DownloadImgActivity.class);
                            intent.putExtra("path", path);
                            intent.putStringArrayListExtra("images", (ArrayList<String>) images);
                            startActivity(intent);
                        }
                    });
                    recyclerView.setAdapter(adapter);
                }
            }
        });
        viewModel.makeAPICall();


        toolbar =(androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_main);
        tvToolbarTitle = (TextView) findViewById(R.id.tvToolbarTitle);
        textView_appDevlopBy = (TextView) findViewById(R.id.textView_app_developed_by);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);
        tvToolbarTitle.setText("Home");


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_hamburger));

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_logout) {

            final AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);

            alertDialog.setTitle("Confirm Exit");
            alertDialog.setIcon(R.drawable.ic_logout);
            alertDialog.setMessage("Are you sure you want to exit ?");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                }
            });
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alertDialog.show();

        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {

            final AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);

            alertDialog.setTitle("Confirm Exit");
            alertDialog.setIcon(R.drawable.ic_logout);
            alertDialog.setMessage("Are you sure you want to exit ?");
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                }
            });
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alertDialog.show();

        }
    }

}
