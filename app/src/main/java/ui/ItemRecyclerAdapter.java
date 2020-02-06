package ui;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.binli.homechef.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import model.Post;

public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Post> postList;

    public ItemRecyclerAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public ItemRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_row, viewGroup, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemRecyclerAdapter.ViewHolder viewHolder, int position) {
        final Post post = postList.get(position);
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String imageUrl;

        viewHolder.title.setText(post.getTitle());
        viewHolder.thoughts.setText(post.getThought());
        viewHolder.name.setText(post.getUserName());
        imageUrl = post.getImageUrl();
        //journal.getTimeAdded();

        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(post
                .getTimeAdded()
                .getSeconds()*1000);

        viewHolder.dateAdded.setText(timeAgo);

        /*
        use picasso library to download and show image
         */

        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.chef_hat)
                .fit()
                .into(viewHolder.image);

//        isLikes(post.getUserId(), viewHolder.like);
//        nrLikes(viewHolder.likes, post.getUserId());
//
//        viewHolder.like.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(viewHolder.like.getTag().equals("like")){
//                    FirebaseDatabase.getInstance().getReference().child("likes").child(post.getUserId())
//                        .child(firebaseUser.getUid()).setValue(true);
//                }else{
//                    FirebaseDatabase.getInstance().getReference().child("likes").child(post.getUserId())
//                            .child(firebaseUser.getUid()).removeValue();
//                }
//            }
//        });



    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView
                title,
                thoughts,
                dateAdded,
                name;
        public ImageView image;
        public ImageButton likeButton;
//        public TextView likes;
//        public ImageView like;
        String userId;
        String username;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);

            context = ctx;

            title = itemView.findViewById(R.id.item_title_list);
            thoughts = itemView.findViewById(R.id.item_thought_list);
            dateAdded = itemView.findViewById(R.id.item_timestamp_list);
            image = itemView.findViewById(R.id.item_image_list);
            name = itemView.findViewById(R.id.item_row_username);
//
//            likeButton = itemView.findViewById(R.id.like);
//            likeButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //context.startActivity();
//                }
//            });

        }
    }

//    private void isLikes(String postid, final ImageView imageView){
//
//        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
//                .child("Likes")
//                .child(postid);
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.child(firebaseUser.getUid()).exists()){
//                    imageView.setImageResource(R.drawable.ic_liked);
//                    imageView.setTag("liked");
//
//                }else{
//                    imageView.setImageResource(R.drawable.ic_like);
//                    imageView.setTag("like");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    private void nrLikes(final TextView likes, String postid){
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes")
//                .child(postid);
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                likes.setText(dataSnapshot.getChildrenCount()+" likes");
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
}
