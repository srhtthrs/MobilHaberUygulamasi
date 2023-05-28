package com.proje2dersi.mobilenews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;



import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.proje2dersi.mobilenews.databinding.ActivityMainBinding;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    ActionBar actionBar;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<ModelEditorNews> modelEditorNewsArrayList;
    EditorNewsAdapter editorNewsAdapter;
    String haberTuru="";
    private Retrofit retrofit;
    private String baseURL="https://newsapi.org/v2/";
    int total;
    Timer timer;
    TimerTask timerTask;

    private String urlAl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        Gson gson = new GsonBuilder().setLenient().create();
        retrofit=new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        modelEditorNewsArrayList=new ArrayList<ModelEditorNews>();
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(0, 0, 0 )));
        auth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Intent intent = getIntent();
        urlAl = intent.getStringExtra("urlGonder");





        getNewsAPIturkey();

    }

    public void getData(){
        firebaseFirestore.collection("newsdetails").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(MainActivity.this, error.getLocalizedMessage() + "Hata Var", Toast.LENGTH_LONG).show();
                }
                if (value != null) {

                   modelEditorNewsArrayList.clear();

                    total=value.getDocuments().size();
                    if(total>98){total=98;}
                    else{total=total;}
                    actionBar.setTitle("Editor's News");

                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();


                        String newstext = (String) data.get("newstext");
                        String newsTitle = (String) data.get("newstitle");
                        String newsType= (String) data.get("newstype");
                        String user=(String) data.get("user");
                        String downloadURL= (String) data.get("downloadurl");

                        Timestamp date = (Timestamp) data.get("date"); //burada bir dönüşüm yapılacak
                        String dateee=null;
                        try{Date datee=date.toDate();
                            dateee= DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(datee).toString();}
                        catch(Exception e){e.getLocalizedMessage();}


                        ModelEditorNews modelEditorNews= new ModelEditorNews(dateee,newstext,newsTitle,newsType,user,downloadURL,"bos","Editor News","");

                        modelEditorNewsArrayList.add(modelEditorNews);


                    }
                    editorNewsAdapter=new EditorNewsAdapter(modelEditorNewsArrayList);
                    binding.recyclerView.setAdapter(editorNewsAdapter);

                    editorNewsAdapter.notifyDataSetChanged();


                }

            }
        });
    }

    @Override

    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuanamenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

         /*

     business
     entertainment
     general
     health
     science
     technology
     sports

    */

        if (item.getItemId() == R.id.editors) {
           // Toast.makeText(MainActivity.this, "Editör Girişi", Toast.LENGTH_LONG).show();

            Intent intentToTedMain=new Intent(MainActivity.this,EditorLogInActivity.class);
            intentToTedMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentToTedMain);
        }

        else if (item.getItemId() == R.id.editorsnews) {

            getData();
        }

        else if (item.getItemId() == R.id.newsturkey) {

            getNewsAPIturkey();
        }

        else if (item.getItemId() == R.id.newsandroid) {

            getNewsAPIandroid();
        }

        else if (item.getItemId() == R.id.entertainment) {

            getNewsAPIentertainment();
        }


        else if (item.getItemId() == R.id.sport) {


            getNewsAPIsport();
        }

        return super.onOptionsItemSelected(item);
    }

    private void getNewsAPIturkey() {



        try {
            modelEditorNewsArrayList.clear();
            HaberAPI haberAPI = retrofit.create(HaberAPI.class);
            Call<HaberAPIModel> call = haberAPI.getAPIturkey();
            call.enqueue(new Callback<HaberAPIModel>() {
                @Override
                public void onResponse(Call<HaberAPIModel> call, Response<HaberAPIModel> response) {
                    if (response.isSuccessful()) {
                        //    Toast.makeText(MainActivity.this,"cevap var",Toast.LENGTH_LONG).show();

                        HaberAPIModel haberAPIModel = response.body();

                        Toast.makeText(MainActivity.this,"status: "+haberAPIModel.status.toString(),Toast.LENGTH_LONG).show();


                        total = Integer.parseInt(haberAPIModel.getTotalResults());
                        if(total>98){total=98;}
                        else{total=total;}
                        actionBar.setTitle("News");
                        try {
                            for (int i = 0; i <= total; i++) {
                            String baslik = haberAPIModel.getArticles().get(i).title;
                            String imaj = haberAPIModel.getArticles().get(i).urlToImage;
                              String tarih = haberAPIModel.getArticles().get(i).publishedAt.split("T")[0];

                              String description = haberAPIModel.getArticles().get(i).description;
                              int index= description.indexOf("<");
                                if(index!=-1){

                                    description=baslik;
                              }

                              String source = haberAPIModel.getArticles().get(i).source.name;
                              String url = haberAPIModel.getArticles().get(i).url;
                                ModelEditorNews modelEditorNews = new ModelEditorNews(tarih, "", "" + baslik, "diger", "1190606904@nku.edu.tr", imaj, url, source, description);
                                modelEditorNewsArrayList.add(modelEditorNews);
                            }
                            editorNewsAdapter = new EditorNewsAdapter(modelEditorNewsArrayList);
                            binding.recyclerView.setAdapter(editorNewsAdapter);

                        } catch (Exception e) {
                            System.out.println(e.getLocalizedMessage());
                            Toast.makeText(MainActivity.this,"Please Refresh",Toast.LENGTH_LONG).show();

                        }

                    }
                }

                @Override
                public void onFailure(Call<HaberAPIModel> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "not loading" + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                }
            });


        }catch(Exception e){System.out.println(e.getLocalizedMessage());}

    }

    private void getNewsAPIandroid() {


        try {
            modelEditorNewsArrayList.clear();
            HaberAPI haberAPI = retrofit.create(HaberAPI.class);
            Call<HaberAPIModel> call = haberAPI.getAPIandroid();
            call.enqueue(new Callback<HaberAPIModel>() {
                @Override
                public void onResponse(Call<HaberAPIModel> call, Response<HaberAPIModel> response) {
                    if (response.isSuccessful()) {
                        //    Toast.makeText(MainActivity.this,"cevap var",Toast.LENGTH_LONG).show();

                        HaberAPIModel haberAPIModel = response.body();

                        total = Integer.parseInt(haberAPIModel.getTotalResults());
                        if(total>98){total=98;}
                        else{total=total;}
                        actionBar.setTitle("Technology");
                        try {
                            for (int i = 0; i <= total; i++) {
                                String baslik = haberAPIModel.getArticles().get(i).title;
                                String imaj = haberAPIModel.getArticles().get(i).urlToImage;
                                String tarih = haberAPIModel.getArticles().get(i).publishedAt.split("T")[0];
                                String description = haberAPIModel.getArticles().get(i).description;

                                int index= description.indexOf("<");
                                if(index!=-1){

                                    description=baslik;
                                }
                                String source = haberAPIModel.getArticles().get(i).source.name;
                                String url = haberAPIModel.getArticles().get(i).url;
                                ModelEditorNews modelEditorNews = new ModelEditorNews(tarih, "", "" + baslik, "diger", "1190606904@nku.edu.tr", imaj, url, source, description);
                                modelEditorNewsArrayList.add(modelEditorNews);
                            }
                            editorNewsAdapter = new EditorNewsAdapter(modelEditorNewsArrayList);
                            binding.recyclerView.setAdapter(editorNewsAdapter);

                        } catch (Exception e) {
                            System.out.println(e.getLocalizedMessage());
                            Toast.makeText(MainActivity.this,"Please Refresh",Toast.LENGTH_LONG).show();

                        }

                    }
                }

                @Override
                public void onFailure(Call<HaberAPIModel> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "not loading" + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                }
            });


        }catch(Exception e){System.out.println(e.getLocalizedMessage());}

    }

    private void getNewsAPIsport() {


        try {
            modelEditorNewsArrayList.clear();
            HaberAPI haberAPI = retrofit.create(HaberAPI.class);
            Call<HaberAPIModel> call = haberAPI.getAPIsport();
            call.enqueue(new Callback<HaberAPIModel>() {
                @Override
                public void onResponse(Call<HaberAPIModel> call, Response<HaberAPIModel> response) {
                    if (response.isSuccessful()) {
                        //    Toast.makeText(MainActivity.this,"cevap var",Toast.LENGTH_LONG).show();

                        HaberAPIModel haberAPIModel = response.body();

                        total = Integer.parseInt(haberAPIModel.getTotalResults());
                        if(total>98){total=98;}
                        else{total=total;}
                        actionBar.setTitle("Sport");
                        try {
                            for (int i = 0; i <= total; i++) {
                                String baslik = haberAPIModel.getArticles().get(i).title;
                                String imaj = haberAPIModel.getArticles().get(i).urlToImage;
                                String tarih = haberAPIModel.getArticles().get(i).publishedAt.split("T")[0];
                                String description = haberAPIModel.getArticles().get(i).description;

                                int index= description.indexOf("<");
                                if(index!=-1){

                                    description=baslik;
                                }
                                String source = haberAPIModel.getArticles().get(i).source.name;
                                String url = haberAPIModel.getArticles().get(i).url;
                                ModelEditorNews modelEditorNews = new ModelEditorNews(tarih, "", "" + baslik, "diger", "1190606904@nku.edu.tr", imaj, url, source, description);
                                modelEditorNewsArrayList.add(modelEditorNews);
                            }
                            editorNewsAdapter = new EditorNewsAdapter(modelEditorNewsArrayList);
                            binding.recyclerView.setAdapter(editorNewsAdapter);

                        } catch (Exception e) {
                            System.out.println(e.getLocalizedMessage());
                            Toast.makeText(MainActivity.this,"Please Refresh",Toast.LENGTH_LONG).show();

                        }

                    }
                }

                @Override
                public void onFailure(Call<HaberAPIModel> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "not loading" + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                }
            });


        }catch(Exception e){System.out.println(e.getLocalizedMessage());}

    }

    private void getNewsAPIentertainment() {





        try {
            modelEditorNewsArrayList.clear();
            HaberAPI haberAPI = retrofit.create(HaberAPI.class);

            Call<HaberAPIModel> call = haberAPI.getAPIentertainment();
            call.enqueue(new Callback<HaberAPIModel>() {
                @Override
                public void onResponse(Call<HaberAPIModel> call, Response<HaberAPIModel> response) {
                    if (response.isSuccessful()) {
                        //    Toast.makeText(MainActivity.this,"cevap var",Toast.LENGTH_LONG).show();

                        HaberAPIModel haberAPIModel = response.body();

                        total = Integer.parseInt(haberAPIModel.getTotalResults());
                        if(total>98){total=98;}
                        else{total=total;}
                        actionBar.setTitle("Entertainment");
                        try {
                            for (int i = 0; i <= total; i++) {
                                String baslik = haberAPIModel.getArticles().get(i).title;
                                String imaj = haberAPIModel.getArticles().get(i).urlToImage;
                                String tarih = haberAPIModel.getArticles().get(i).publishedAt.split("T")[0];
                                String description = haberAPIModel.getArticles().get(i).description;

                                int index= description.indexOf("<");
                                if(index!=-1){

                                    description=baslik;
                                }
                                String source = haberAPIModel.getArticles().get(i).source.name;
                                String url = haberAPIModel.getArticles().get(i).url;
                                ModelEditorNews modelEditorNews = new ModelEditorNews(tarih, "", "" + baslik, "diger", "1190606904@nku.edu.tr", imaj, url, source, description);
                                modelEditorNewsArrayList.add(modelEditorNews);
                            }
                            editorNewsAdapter = new EditorNewsAdapter(modelEditorNewsArrayList);
                            binding.recyclerView.setAdapter(editorNewsAdapter);

                        } catch (Exception e) {
                            System.out.println(e.getLocalizedMessage());
                            Toast.makeText(MainActivity.this,"Please Refresh",Toast.LENGTH_LONG).show();

                        }

                    }
                }

                @Override
                public void onFailure(Call<HaberAPIModel> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "not loading" + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                }
            });


        }catch(Exception e){System.out.println(e.getLocalizedMessage());}

    }




}






