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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AppListAdapterClass extends RecyclerView.Adapter<AppListAdapterClass.AppListViewAdapter> implements Filterable{

    private Context context;
    private Activity activity;
    private List<AppData> appDataList, filterAppliedList;//

    public AppListAdapterClass(Activity activity,Context context, List<AppData> list) {
        this.context = context;
        this.activity=activity;
        this.filterAppliedList = list;
        this.appDataList = new ArrayList<AppData>(list);
    }

    @NonNull
    @Override
    public AppListViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.app_name_single_item_card, parent, false);

        return new AppListAdapterClass.AppListViewAdapter(view);
    }
    @Override
    public void onBindViewHolder(@NonNull  AppListAdapterClass.AppListViewAdapter holder, int position) {

        final AppData appData= filterAppliedList.get(position);

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
        return filterAppliedList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<AppData> filteredList = new ArrayList<>();
            if(String.valueOf(constraint).isEmpty()){
                filteredList.addAll(appDataList);
            }else{
                for(AppData appData : appDataList){
                    if(appData.getAppName().toLowerCase().contains(String.valueOf(constraint).toLowerCase())){
                        filteredList.add(appData);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values= filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            filterAppliedList.clear();

            filterAppliedList.addAll((Collection<? extends AppData>) results.values);
            notifyDataSetChanged();

        }
    };

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