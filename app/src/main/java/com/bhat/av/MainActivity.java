package com.bhat.av;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    List<AppData> appDataList= new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private RecyclerView appListRecycler;
    private AppListAdapterClass appListAdapterObj;
    private LinearLayout noAppFound;
    private ShimmerFrameLayout shimmerFrameLayout;

    TextView tv;

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
    }

    private void initialise() {

        appListRecycler = findViewById(R.id.appListRecycler);
        noAppFound = findViewById(R.id.noAppFound);
        shimmerFrameLayout =findViewById(R.id.shimmer_view_container);
        //Start the shimmer
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        tv=findViewById(R.id.xxl);

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

                appDataList.add(data);
            }
        }

        appListAdapterObj = new AppListAdapterClass(MainActivity.this,MainActivity.this,appDataList);
        appListRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this,RecyclerView.VERTICAL,false));
        appListRecycler.setAdapter(appListAdapterObj);
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
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
}