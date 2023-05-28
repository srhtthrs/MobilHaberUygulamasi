package com.proje2dersi.mobilenews;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.proje2dersi.mobilenews.databinding.ActivityEditorHaberGirisBinding;
import com.proje2dersi.mobilenews.databinding.ActivityEditorLogInBinding;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EditorHaberGirisActivity extends AppCompatActivity {

    private ActivityEditorHaberGirisBinding binding;
    ActionBar actionBar;
    private FirebaseAuth mAuth;
    private String[] newsType;
    Uri imageData;
    String user;
    Bitmap selectedImage;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    MainActivity mainActivity;
    ArrayList<ModelEditorNews> modelEditorNewsArrayList;
    String newsTypeString, newsTitle, newsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditorHaberGirisBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mAuth = FirebaseAuth.getInstance();
        actionBar = getSupportActionBar();
        user=mAuth.getCurrentUser().getEmail().toString();
        modelEditorNewsArrayList=new ArrayList<>();
        String editor=mAuth.getCurrentUser().getEmail().toString();
        actionBar.setTitle(""+editor);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(0, 0, 0 )));
        binding.buttonAdd.setBackgroundColor(Color.rgb(0, 0, 0));
        newsType = new String[]{"General","Entertainment","Sport","Technology"};
        ArrayAdapter<String> adapterNewsType = new ArrayAdapter<String>(EditorHaberGirisActivity.this, android.R.layout.simple_spinner_item, newsType);
        binding.spinnerNewsType.setAdapter(adapterNewsType);
        registerLauncher();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menueditorhabergirismenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.gcikis) {

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Uyarı");
            alert.setMessage("Cikis Yapmak Istiyor Musun?");
            alert.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAuth.signOut();
                    Intent intentToMain=new Intent(EditorHaberGirisActivity.this, MainActivity.class);
                    intentToMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentToMain);
                }
            });
            alert.setNegativeButton("Hayir", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alert.show();



        }




        return super.onOptionsItemSelected(item);
    }

    public void AddClick(View view){
        newsTypeString=binding.spinnerNewsType.getSelectedItem().toString();
        newsText=binding.textMultilineTex.getText().toString();
        newsTitle=binding.textViewTitle.getText().toString().toUpperCase();
       if (imageData != null) {
            UUID uuid = UUID.randomUUID();
            final String imageName = "images/" + uuid + ".jpg";
            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    StorageReference newReference = FirebaseStorage.getInstance().getReference(imageName);
                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUrl = uri.toString();
                            HashMap<String, Object> postData = new HashMap<>();
                            postData.put("newstype",newsTypeString);
                            postData.put("newstext",newsText);
                            postData.put("newstitle",newsTitle);
                            postData.put("downloadurl",downloadUrl);
                            postData.put("date", FieldValue.serverTimestamp());
                            postData.put("user",mAuth.getCurrentUser().getEmail().toString());
                            firebaseFirestore.collection("newsdetails").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(EditorHaberGirisActivity.this,"Successful",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(EditorHaberGirisActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EditorHaberGirisActivity.this,e.getLocalizedMessage().toString()+"Something Wrong.",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditorHaberGirisActivity.this,e.getLocalizedMessage().toString()+"Something Wrong.",Toast.LENGTH_LONG).show();
                }
            });

        }
       else{Toast.makeText(EditorHaberGirisActivity.this,"No image selected.",Toast.LENGTH_LONG).show();}
        Toast.makeText(EditorHaberGirisActivity.this,"The registration process may take some time.",Toast.LENGTH_LONG).show();
    }

    public void SelectImage(View view){

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(view,"Galeri için izin gerekli", Snackbar.LENGTH_INDEFINITE).setAction("İzin Ver", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();
            } else {
                permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        } else {
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intentToGallery);

        }



    }

    public void registerLauncher() {
       activityResultLauncher = registerForActivityResult(
               new ActivityResultContracts.StartActivityForResult(),
               new ActivityResultCallback<ActivityResult>() {
                   @Override
                   public void onActivityResult(ActivityResult result) {
                       if (result.getResultCode() == Activity.RESULT_OK) {
                           Intent intentFromResult = result.getData();
                           if (intentFromResult != null) {
                               imageData = intentFromResult.getData();
                               try {

                                   if (Build.VERSION.SDK_INT >= 28) {
                                       ImageDecoder.Source source = ImageDecoder.createSource(EditorHaberGirisActivity.this.getContentResolver(), imageData);
                                       selectedImage = ImageDecoder.decodeBitmap(source);
                                       binding.imageViewAdd.setImageBitmap(selectedImage);

                                   } else {
                                       selectedImage = MediaStore.Images.Media.getBitmap(EditorHaberGirisActivity.this.getContentResolver(), imageData);
                                       binding.imageViewAdd.setImageBitmap(selectedImage);
                                   }

                               } catch (IOException e) {
                                   e.printStackTrace();
                               }
                           }

                       }
                   }
               });


       permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
           @Override
           public void onActivityResult(Boolean result) {
               if (result) {
                   //permission granted
                   Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                   activityResultLauncher.launch(intentToGallery);

               } else {
                   //permission denied
                   Toast.makeText(EditorHaberGirisActivity.this, "İzin Gereklidir!", Toast.LENGTH_LONG).show();
               }
           }

       });
   }

}


