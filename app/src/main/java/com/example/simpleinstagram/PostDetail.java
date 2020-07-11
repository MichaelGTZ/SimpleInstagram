package com.example.simpleinstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class PostDetail extends AppCompatActivity {

    private Post post;
    private TextView tvUsername;
    private ImageView ivImage;
    private TextView tvCaption;
    private TextView tvTimestamp;
    private TextView tvUsername2;
    private ImageView ivLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        // Unwrap the post passed in via intent
        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));
        // Resolve new objects
        tvUsername = findViewById(R.id.tvUsername);
        ivImage = findViewById(R.id.ivImage);
        tvCaption = findViewById(R.id.tvCaption);
        tvTimestamp = findViewById(R.id.tvTimestamp);
        tvUsername2 = findViewById(R.id.tvUsername2);
        ivLike = findViewById(R.id.ivLike);
        // Cannot get heart icon to turn into filled in heart when clicked
//        ivLike.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ParseRelation<ParseUser> usersLikedRelation = post.getRelation("usersLiked");
//                ParseQuery<ParseUser> usersLikedQuery = usersLikedRelation.getQuery();
//                usersLikedQuery.findInBackground(new FindCallback<ParseUser>() {
//                    @Override
//                    public void done(List<ParseUser> objects, ParseException e) {
//                        if (e != null) {
//                            ivLike.setImageDrawable(getResources().getDrawable(R.drawable.ufi_heart_active));
//                        } else {
//                            ivLike.setImageDrawable(getResources().getDrawable(R.drawable.ufi_heart));
//                        }
//                    }
//                });
//            }
//        });

        // Set objects' text and image
        tvUsername.setText(post.getUser().getUsername());
        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(PostDetail.this).load(image.getUrl()).into(ivImage);
        }
        tvCaption.setText(post.getDescription());
        tvTimestamp.setText(post.getRelativeTimeAgo(post.getCreatedAt().toString()));
        tvUsername2.setText(post.getUser().getUsername());
    }
}