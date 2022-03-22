package com.bhat.av;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AppListAdapterClass extends RecyclerView.Adapter<AppListAdapterClass.AppListViewAdapter> {

    private Context context;
    private Activity activity;
    private List<AppData> appDataList;//

    public AppListAdapterClass(Activity activity,Context context, List<AppData> appDataList) {
        this.context = context;
        this.activity=activity;
        this.appDataList = new ArrayList<AppData>(appDataList);
    }

    @NonNull
    @Override
    public AppListViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.app_name_single_item_card, parent, false);

        return new AppListAdapterClass.AppListViewAdapter(view);
    }
    @Override
    public void onBindViewHolder(@NonNull  AppListAdapterClass.AppListViewAdapter holder, int position) {

        final AppData appData= appDataList.get(position);
        holder.appNameTv.setText(appData.getAppName());
        holder.appIcon.setImageDrawable(appData.getLogo());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, appData.getAppName(), Toast.LENGTH_SHORT).show();
                Intent deleteAppIntent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
                deleteAppIntent.setData(Uri.parse("package:" + appData.getPackageName()));
                deleteAppIntent.putExtra(Intent.EXTRA_RETURN_RESULT,true);
                activity.startActivityForResult(deleteAppIntent,100);
            }
        });
        holder.deleteApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent deleteAppIntent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
                deleteAppIntent.setData(Uri.parse("package:" + appData.getPackageName()));
                deleteAppIntent.putExtra(Intent.EXTRA_RETURN_RESULT,true);
                activity.startActivityForResult(deleteAppIntent,100);
            }
        });


    }

    @Override
    public int getItemCount() {
        return appDataList.size();
    }


    public class AppListViewAdapter extends RecyclerView.ViewHolder {

        TextView appNameTv;
        ImageView appIcon, deleteApp;

        public AppListViewAdapter(@NonNull  View itemView) {
            super(itemView);
            appNameTv=itemView.findViewById(R.id.appName);
            appIcon=itemView.findViewById(R.id.appIcon);
            deleteApp=itemView.findViewById(R.id.deleteApp);
        }
    }
}