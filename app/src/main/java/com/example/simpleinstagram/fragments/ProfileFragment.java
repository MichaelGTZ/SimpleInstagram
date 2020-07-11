package com.example.simpleinstagram.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.simpleinstagram.BitmapScalar;
import com.example.simpleinstagram.LoginActivity;
import com.example.simpleinstagram.Post;
import com.example.simpleinstagram.PostsAdapter;
import com.example.simpleinstagram.R;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {

    public static final String TAG = "Profile Fragment";

    private RecyclerView rvPosts;
    private PostsAdapter adapter;
    private List<Post> allPosts;
    private Button btnLogout;
    private ImageView ivProfilePic;
    private Bitmap resizedProfilePicBitmap;

    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1134;
    public String profilePhotoFileName = "profile_%d.jpg";
    File profilePicFile;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPosts = view.findViewById(R.id.rvPosts);

        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(getContext(), allPosts);
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick log out button");
                logoutUser();
            }
        });

        ivProfilePic = view.findViewById(R.id.ivprofilePic);
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser.has("profilePicture")) {
            ParseFile profilePicFile = (ParseFile) currentUser.get("profilePicture");
            try {
                File profilePic = profilePicFile.getFile();
                Bitmap profilePicBitmap = BitmapFactory.decodeFile(profilePic.getAbsolutePath());
                ivProfilePic.setImageBitmap(profilePicBitmap);
            } catch (ParseException e) {
                Log.e(TAG, "onViewCreated: Failed to load profile pic", e);
            }
        } else {
            ivProfilePic.setImageDrawable(getResources().getDrawable(R.drawable.instagram_user_outline_24));
        }
        ivProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera(view);
            }
        });

        queryPosts();
    }

    public void launchCamera(View view) {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access using current timestamp
        profilePicFile = getPhotoFileUri(String.format(profilePhotoFileName, System.currentTimeMillis()));

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", profilePicFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE); // Calls onActivityResult() upon completion
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                try {
                    saveProfilePic(currentUser, profilePicFile);
                } catch (IOException e) {
                    Log.e(TAG, "Save Post failed from button click listener", e);
                }
                Bitmap rawTakenImage = BitmapFactory.decodeFile(profilePicFile.getAbsolutePath());
                // Load the taken image into a preview
                ivProfilePic.setImageBitmap(rawTakenImage);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    private void saveProfilePic(ParseUser currentUser, File profilePicFile) throws IOException {
        // See code above
        Uri takenPhotoUri = Uri.fromFile(getPhotoFileUri(profilePicFile.getName()));
        // by this point we have the camera photo on disk
        Bitmap rawTakenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
        // See BitmapScaler.java: https://gist.github.com/nesquena/3885707fd3773c09f1bb
        resizedProfilePicBitmap = BitmapScalar.scaleToFitWidth(rawTakenImage, 1080);
        // Configure byte output stream
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        // Compress the image further
        resizedProfilePicBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
        // Create a new file for the resized bitmap (`getPhotoFileUri` defined above)
        File resizedFile = getPhotoFileUri("resized_" + profilePicFile.getName());
        resizedFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(resizedFile);
        // Write the bytes of the bitmap to file
        fos.write(bytes.toByteArray());
        fos.close();

        currentUser.put("profilePicture", new ParseFile(resizedFile));

        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(getContext(), "Error while saving", Toast.LENGTH_SHORT);
                }
                Log.i(TAG, "Post save was successful");
                ivProfilePic.setImageBitmap(resizedProfilePicBitmap);
            }
        });
    }

    private void logoutUser() {
        ParseUser.logOut();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        finishActivity();
    }

    private void finishActivity() {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    private void queryPosts() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // Include the user of the post in the query result
        query.include(Post.KEY_USER);
        // Get posts only for the currently signed in user
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        // Limit the number of posts to retrieve
        query.setLimit(20);
        // Show most recent posts first
        query.addDescendingOrder(Post.KEY_CREATED_AT);

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                }
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }
}