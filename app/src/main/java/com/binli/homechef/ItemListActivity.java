package com.binli.homechef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import model.Post;
import ui.ItemRecyclerAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.binli.homechef.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemListActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;

    private List<Post> itemList;
    private RecyclerView recyclerView;
    private ItemRecyclerAdapter itemRecyclerAdapter;

    private CollectionReference collectionReference = db.collection("Item");
    private TextView noItemEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        setContentView(R.layout.activity_item_list);

        firebaseAuth = firebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        noItemEntry = findViewById(R.id.list_no_thoughts);

        itemList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_add:
                //take user ti add item
                if(user != null && firebaseAuth != null){
                    startActivity(new Intent(ItemListActivity.this,
                            PostItemActivity.class));
                    //finish();
                }
                break;
            case R.id.action_signout:
                //take user to login page

                if(user != null && firebaseAuth != null){
                    firebaseAuth.signOut();

                    startActivity(new Intent(ItemListActivity.this,
                            MainActivity.class));

                    //finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

                    /*
            .get will get all objects in journal
            would not have to do this in homerestaurant app the
             */


//.whereEqualTo("userId", ItemApi.getInstance()
//                .getUserId())


        collectionReference
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){
                            for (QueryDocumentSnapshot items : queryDocumentSnapshots) {
                                Post post = items.toObject(Post.class);
                                itemList.add(post);


                            }

                            itemRecyclerAdapter = new ItemRecyclerAdapter(ItemListActivity.this,
                                    itemList);
                            recyclerView.setAdapter(itemRecyclerAdapter);
                            itemRecyclerAdapter.notifyDataSetChanged();

                        } else {
                            noItemEntry.setVisibility(View.VISIBLE);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }
}
