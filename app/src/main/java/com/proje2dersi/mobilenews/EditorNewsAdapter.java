package com.proje2dersi.mobilenews;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.proje2dersi.mobilenews.databinding.RecyclerRowAnaHaberBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class EditorNewsAdapter extends RecyclerView.Adapter<EditorNewsAdapter.EditorNewsModelHoler> {

    private ArrayList<ModelEditorNews>modelEditorNews;
   // String url;

    public EditorNewsAdapter(ArrayList<ModelEditorNews> modelEditorNews) {
        this.modelEditorNews = modelEditorNews;
    }

    @NonNull
    @Override
    public EditorNewsModelHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       RecyclerRowAnaHaberBinding recyclerRowAnaHaberBinding= RecyclerRowAnaHaberBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
       return new EditorNewsModelHoler(recyclerRowAnaHaberBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull EditorNewsModelHoler holder, @SuppressLint("RecyclerView") int position) {



        Picasso.get().load(modelEditorNews.get(position).downloadURL).into(holder.recyclerRowAnaHaberBinding.image);

        holder.recyclerRowAnaHaberBinding.title.setText(modelEditorNews.get(position).newsTitle);
        holder.recyclerRowAnaHaberBinding.name.setText(modelEditorNews.get(position).name );
        holder.recyclerRowAnaHaberBinding.date.setText(modelEditorNews.get(position).date);



        holder.recyclerRowAnaHaberBinding.description.setText(modelEditorNews.get(position).description);


        //url = modelEditorNews.get(position).url;



         holder.recyclerRowAnaHaberBinding.image.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {


                 if(modelEditorNews.get(position).url.equals("bos")){

                     Intent intentBosURL = new Intent(holder.itemView.getContext(), EditorHaberView.class);

                     intentBosURL.putExtra("date",modelEditorNews.get(position).date);
                     intentBosURL.putExtra("title",modelEditorNews.get(position).newsTitle);
                     intentBosURL.putExtra("image",modelEditorNews.get(position).downloadURL);
                     intentBosURL.putExtra("text",modelEditorNews.get(position).newsText);

                     holder.itemView.getContext().startActivity(intentBosURL);

                 }


                 else{
                     Intent intent = new Intent(Intent.ACTION_VIEW);
                     intent.setData(Uri.parse(modelEditorNews.get(position).url));
                     view.getContext().startActivity(intent);}



             }
         });

    }




    @Override
    public int getItemCount() {
        return modelEditorNews.size();
    }

    class EditorNewsModelHoler extends RecyclerView.ViewHolder{

        RecyclerRowAnaHaberBinding recyclerRowAnaHaberBinding;

        public EditorNewsModelHoler(RecyclerRowAnaHaberBinding recyclerRowAnaHaberBinding) {
            super(recyclerRowAnaHaberBinding.getRoot());
            this.recyclerRowAnaHaberBinding=recyclerRowAnaHaberBinding;
        }
    }

}
