package com.shehab.imagetofirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {

    // imageView-imgview
    //chooseImageButton - btnchoose
    //btnupload

    Button ch, up;
    ImageView img;
    StorageReference mStorageRef;
    public Uri imguri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");

        ch = (Button) findViewById(R.id.chooseImageButton);
        up = (Button) findViewById(R.id.btnUpload);
        img = (ImageView) findViewById(R.id.imageView);

        ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser();
            }
        });

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fileuploader();
            }
        });
    }

    private String getExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }
    // giving files a unique name
    private void Fileuploader() {
        StorageReference Ref = mStorageRef.child(System.currentTimeMillis()+"."+getExtension(imguri));

        // code from firebase
        Ref.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(MainActivity.this, "Image Uploaded Successfully",
                                Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }

    private void FileChooser() {
        Intent intent = new Intent();
        intent.setType("image/'");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imguri = data.getData();
            img.setImageURI(imguri);
        }
    }
}
