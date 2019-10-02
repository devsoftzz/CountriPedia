package com.devsoft.countryapi_recyclersearch;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;

import java.util.ArrayList;
import java.util.List;

public class myAdapter extends RecyclerView.Adapter<myAdapter.myViewHolder> implements Filterable {

    ArrayList<Country> data;
    ArrayList<Country> fulldata;
    Activity context;
    RequestBuilder<PictureDrawable> requestBuilder;

    public myAdapter(ArrayList<Country> data,Activity context) {
        this.data = data;
        this.context = context;
        fulldata = new ArrayList<>(data);
        requestBuilder = GlideToVectorYou.init().with(context).getRequestBuilder();
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item,viewGroup,false);
        final myViewHolder v = new myViewHolder(view);
        v.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View x) {
                final Country c = data.get(v.getAdapterPosition());
                final Dialog dialog = new Dialog(context,android.R.style.Theme_Material_Light_LightStatusBar);
                dialog.setContentView(R.layout.pop);
                dialog.setTitle(c.name);

                ImageView view1 = dialog.findViewById(R.id.flag_pop);
                TextView textView = dialog.findViewById(R.id.name);
                TextView textView1 = dialog.findViewById(R.id.capital);
                TextView alpha2 = dialog.findViewById(R.id.alpha2);
                TextView region = dialog.findViewById(R.id.region);
                TextView population = dialog.findViewById(R.id.population);
                TextView listView = dialog.findViewById(R.id.timezone);
                TextView area = dialog.findViewById(R.id.area);
                Button map = dialog.findViewById(R.id.map);
                String timedata="";
                for(int i=0;i<c.timezones.size();i++){
                    timedata = timedata + c.timezones.get(i) + "\n";
                }
                timedata=timedata.substring(0,timedata.length()-1);
                TextView cur = dialog.findViewById(R.id.cur);
                String curdata = "";
                for(int j=0;j<c.currencies.size();j++){
                    curdata = curdata + c.currencies.get(j).code + " - " + c.currencies.get(j).name + "\n";
                }
                curdata=curdata.substring(0,curdata.length()-1);
                cur.setText(curdata);

                map.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://maps.google.com/?q="+c.name));
                        context.startActivity(intent);
                    }
                });

                requestBuilder.load(c.flag).transition(DrawableTransitionOptions.withCrossFade()).apply(new RequestOptions().fitCenter()).into(view1);
                textView.setText(c.name);
                textView1.setText(c.capital);
                alpha2.setText(c.alpha2code);
                region.setText(c.region);
                population.setText(String.valueOf(c.population));
                listView.setText(timedata);
                area.setText(String.valueOf(c.area));
                final Button info = dialog.findViewById(R.id.info);
                info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context,info.class);
                        intent.putExtra("URL","https://en.wikipedia.org/wiki/"+c.name);
                        context.startActivity(intent);
                    }
                });

                dialog.show();
                //Toast.makeText(viewGroup.getContext(),String.valueOf(v.getAdapterPosition()), Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder myViewHolder, int i) {
        requestBuilder.load(data.get(i).flag).transition(DrawableTransitionOptions.withCrossFade()).apply(new RequestOptions().override(100,75).fitCenter()).into(myViewHolder.flag);
        myViewHolder.country.setText(data.get(i).name);
        myViewHolder.capital.setText(data.get(i).capital);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }



    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView country,capital;
        ImageView flag;
        LinearLayout ll;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            country = itemView.findViewById(R.id.country);
            capital = itemView.findViewById(R.id.capital);
            flag = itemView.findViewById(R.id.flag);
            ll = itemView.findViewById(R.id.ll);
        }
    }
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Country> filteredData = new ArrayList<>();
            if(constraint==null || constraint.length()==0){
                filteredData.addAll(fulldata);
            }else {
                String search = constraint.toString().toLowerCase().trim();

                for(Country item : fulldata){
                    if(item.name.toLowerCase().startsWith(search)){
                        filteredData.add(item);
                    }
                }
                for(Country item1 : fulldata){
                    if(item1.name.toLowerCase().contains(search) && !item1.name.toLowerCase().startsWith(search)){
                        filteredData.add(item1);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredData;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            data.clear();
            data.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
}


