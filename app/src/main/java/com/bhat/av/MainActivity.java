package com.bhat.av;

import java.util.ArrayList;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import androidx.appcompat.widget.SearchView;

public class MainActivity extends AppCompatActivity {

    List<AppData> appDataList= new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private RecyclerView appListRecycler;
    private AppListAdapterClass appListAdapterObj;
    private LinearLayout noAppFound;
    private ShimmerFrameLayout shimmerFrameLayout;
    private ArrayList<String>  levelOne, levelTwo, levelThree;
    private FloatingActionButton floatingActionButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //For back arrow in nav bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //setting Nav bar title
        getSupportActionBar().setTitle("Your Apps");

        initialise();
        getAndSetAppData();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AppListFilterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initialise() {

        floatingActionButton= findViewById(R.id.foab);
        appListRecycler = findViewById(R.id.appListRecycler);
        noAppFound = findViewById(R.id.noAppFound);
        shimmerFrameLayout =findViewById(R.id.shimmer_view_container);
        //Start the shimmer
        shimmerFrameLayout.setVisibility(View.VISIBLE);

        String[] levelOneStr = getResources().getStringArray(R.array.levelOne);
        String[] levelTwoStr = getResources().getStringArray(R.array.levelTwo);
        String[] levelThreeStr = getResources().getStringArray(R.array.levelThree);
        //Initialise threat app list
        levelOne = new ArrayList<String>(Arrays.asList(levelOneStr));
        levelTwo = new ArrayList<String>(Arrays.asList(levelTwoStr));
        levelThree = new ArrayList<String>(Arrays.asList(levelThreeStr));

    }

    private void getAndSetAppData() {

//        Intent intent = new Intent(Intent.ACTION_MAIN,null);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        List<ResolveInfo> resolveInfos = getPackageManager().queryIntentActivities(intent, 0);
//
//        for (ResolveInfo info : resolveInfos){
//
//            appNameList.add(
//                     info.activityInfo.loadLabel(getPackageManager()).toString()
//
//            );
//
//        }

        //Getting filter choice from intent
        Intent intent= getIntent();
        String filter = intent.getStringExtra("filter");

        //Initializing the package manager object
        PackageManager packageManager = getPackageManager();

        //Initializing a list for storing installed app info
        List<ApplicationInfo> applicationInfo= packageManager.getInstalledApplications(
                PackageManager.GET_META_DATA
        );


        //Extract useful data from the applicationInfo and store it in appData
        for(ApplicationInfo info : applicationInfo){
            //If condition to only extract installed externals apps data
            if((info.flags & ApplicationInfo.FLAG_SYSTEM)==0){
                AppData data = new AppData();
                data.setAppName(info.loadLabel(packageManager).toString());
                data.setPackageName(info.packageName);
                data.setLogo(info.loadIcon(packageManager));
                if(filter.equals("0"))
                    appDataList.add(data);
                else if(filter.equals("1") && levelOne.contains(data.getAppName()))
                    appDataList.add(data);
                else if (filter.equals("2") && levelTwo.contains(data.getAppName()))
                    appDataList.add(data);
                else if (filter.equals("3") && levelThree.contains(data.getAppName()))
                    appDataList.add(data);
                else
                    System.out.println("nice");
            }
        }
        if(!appDataList.isEmpty()) {
            appListRecycler.setVisibility(View.VISIBLE);
            appListAdapterObj = new AppListAdapterClass(MainActivity.this, MainActivity.this, appDataList);
            appListRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false));
            appListRecycler.setAdapter(appListAdapterObj);
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
        }else {
            appListRecycler.setVisibility(View.GONE);
            shimmerFrameLayout.setVisibility(View.GONE);
            noAppFound.setVisibility(View.VISIBLE);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && resultCode == RESULT_OK){
            Toast.makeText(this, "Uninstall: Success", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Uninstall: Failure", Toast.LENGTH_SHORT).show();
        }
    }

    //To implement search
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_for_search,menu);

        MenuItem item= menu.findItem(R.id.action_search);

        SearchView searchView= (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                //newText contains the search string.
                appListAdapterObj.getFilter().filter(newText);

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}