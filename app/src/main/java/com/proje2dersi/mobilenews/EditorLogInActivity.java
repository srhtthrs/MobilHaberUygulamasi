package com.proje2dersi.mobilenews;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.proje2dersi.mobilenews.databinding.ActivityEditorLogInBinding;
import com.proje2dersi.mobilenews.databinding.ActivityMainBinding;

import org.checkerframework.checker.nullness.qual.NonNull;

public class EditorLogInActivity extends AppCompatActivity {
    private ActivityEditorLogInBinding binding;
    private FirebaseAuth mAuth;
    ActionBar actionBar;
    String eMail, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditorLogInBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        actionBar = getSupportActionBar();
        actionBar.setTitle("LOG IN");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(0, 0, 0 )));
        binding.buttonLogIn.setBackgroundColor(Color.rgb(0, 0, 0));
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user= mAuth.getCurrentUser();
        if(user!=null) {
            Intent intent = new Intent(EditorLogInActivity.this, EditorHaberGirisActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void logIn(View view){
        eMail=binding.EditTextEmail.getText().toString();
        password=binding.editTextPassword.getText().toString();


        if(eMail.equals("")|password.equals("")) {
        Toast.makeText(EditorLogInActivity.this,"E-posta ve Parola Giriniz",Toast.LENGTH_LONG).show();
        }
        else{

            mAuth.signInWithEmailAndPassword(eMail,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                    Intent intentToTedMain=new Intent(EditorLogInActivity.this,EditorHaberGirisActivity.class);
                    intentToTedMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentToTedMain);


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditorLogInActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            });

        }











    }

    @Override
    public boolean onCreateOptionsMenu(@androidx.annotation.NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menueditorloginmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@androidx.annotation.NonNull MenuItem item) {
        if (item.getItemId() == R.id.passres) {
            Toast.makeText(EditorLogInActivity.this,"Şifremi Unuttum",Toast.LENGTH_LONG).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("E-Mail adresinizi giriniz");
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            builder.setPositiveButton("Gonder", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (input.getText().toString().equals("")) {
                        Toast.makeText(EditorLogInActivity.this, "E-posta Adresinizi Giriniz.", Toast.LENGTH_LONG).show();
                    } else {
                        String e_posta = input.getText().toString();
                        mAuth.sendPasswordResetEmail(e_posta)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(EditorLogInActivity.this, "Parola Sifirlama Icin E-posta Gonderildi. Kontrol Ediniz...", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(EditorLogInActivity.this, "Hata Olustu.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                }
            });
            builder.setNegativeButton("Geri", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }

      return super.onOptionsItemSelected(item);
    }

}