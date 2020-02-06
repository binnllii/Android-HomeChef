package com.binli.homechef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import util.ItemApi;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.binli.homechef.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import javax.annotation.Nullable;

public class LoginActivity extends AppCompatActivity {

    private Button loginbutton;
    private Button createAcctbutton;


    private AutoCompleteTextView emailAddress;

    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private EditText password;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);

        emailAddress = findViewById(R.id.email);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.login_progress);

        firebaseAuth = FirebaseAuth.getInstance();

        loginbutton = findViewById(R.id.email_sign_in_button);
        createAcctbutton = findViewById(R.id.create_acct_button_login);

        createAcctbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
            }
        });

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(LoginActivity.this, "login got clicked", Toast.LENGTH_SHORT).show();
                loginEmailPasswordUser(emailAddress.getText().toString().trim(),
                        password.getText().toString().trim());
            }
        });
    }

    private void loginEmailPasswordUser(String email, String pwd) {

        progressBar.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(email)
                && !TextUtils.isEmpty(pwd)){
            firebaseAuth.signInWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            assert user != null;
                            final String currentUserId = user.getUid();


                            collectionReference
                                    .whereEqualTo("userId", currentUserId)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                                            @Nullable FirebaseFirestoreException e) {
                                            if(e != null){
                                                return;
                                            }
                                            progressBar.setVisibility(View.INVISIBLE);
                                            assert queryDocumentSnapshots != null;
                                            if(!queryDocumentSnapshots.isEmpty()){

                                                for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                                                    ItemApi itemApi = ItemApi.getInstance();
                                                    itemApi.setUsername(snapshot.getString("username"));
                                                    itemApi.setUserId(snapshot.getString("userId"));

                                                    //go to ListActivity
                                                    startActivity(new Intent(LoginActivity.this, PostItemActivity.class));
                                                }



                                            }

                                        }
                                    });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });

        }else{
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(LoginActivity.this, "Please enter Email and Password.",
                    Toast.LENGTH_LONG)
                    .show();

        }
    }


}
