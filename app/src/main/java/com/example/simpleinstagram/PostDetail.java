package com.example.simpleinstagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.parceler.Parcels;

public class PostDetail extends AppCompatActivity {

    private Post post;
    private TextView tvUsername;
    private ImageView ivImage;
    private TextView tvCaption;
    private TextView tvTimestamp;

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

        // Set objects' text and image
        tvUsername.setText(post.getUser().getUsername());
        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(PostDetail.this).load(image.getUrl()).into(ivImage);
        }
        tvCaption.setText(post.getDescription());
        tvTimestamp.setText(post.getRelativeTimeAgo(post.getCreatedAt().toString()));
    }
}