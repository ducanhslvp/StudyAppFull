package com.ducanh.appchat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ducanh.appchat.R;
import com.ducanh.appchat.adapter.ClassNewFeedAdapter;
import com.ducanh.appchat.model.ClassFeed;
import com.ducanh.appchat.model.Subject;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClassActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    String className,subject;
    ClassNewFeedAdapter classNewFeedAdapter;

    ImageButton btnSubmit, btnPicture,btnSubject,btnAddVideo;
    EditText textSend;
    List<ClassFeed> listClassFeed=new ArrayList<>();
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    StorageReference storageReference;
    private static final  int IMAGE_REQUEST=1;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri imageUri;
    private StorageTask uploadTask;

    LayoutInflater inflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        recyclerView=findViewById(R.id.recycler_view);

        btnSubmit =findViewById(R.id.btn_submit);
        textSend=findViewById(R.id.text_send);
        btnPicture =findViewById(R.id.btn_imagePicture);
        btnSubject=findViewById(R.id.btn_imageAdd);
        btnAddVideo=findViewById(R.id.btn_videoAdd);


        Intent intent=new Intent();
        intent=getIntent();
        className=intent.getStringExtra("class");



        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        storageReference= FirebaseStorage.getInstance().getReference("images");
        getClassNewFeed();



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFeed(textSend.getText().toString(),"text");
                textSend.setText("");
            }
        });
        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chooseImage();
            }
        });
        btnAddVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFeed("https://raw.githubusercontent.com/o7planning/webexamples/master/_testdatas_/mov_bbb.mp4","video");
            }
        });
        btnSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAlertDialog();


            }
        });
    }
    private void submitFeed(String content,String type){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference classRef= FirebaseDatabase.getInstance().getReference("Class")
                .child(className).child(firebaseUser.getUid());

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("userID",firebaseUser.getUid());
        hashMap.put("content",content);
        hashMap.put("type",type);
        classRef.push().setValue(hashMap);

    }
    private void getClassNewFeed(){
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference=FirebaseDatabase.getInstance().getReference("Class").child(className).child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listClassFeed.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()) {
                    ClassFeed feed = snapshot1.getValue(ClassFeed.class);
                    listClassFeed.add(feed);
                }
                classNewFeedAdapter=new ClassNewFeedAdapter(getBaseContext(),listClassFeed);
                recyclerView.setAdapter(classNewFeedAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    private String getFileExtension(Uri uri){
        ContentResolver conentResolver=getBaseContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(conentResolver.getType(uri));
    }
    private void uploadImage(){
        final ProgressDialog pd=new ProgressDialog(getBaseContext());
//        pd.setMessage("Đang tải lên");
//        pd.show();

        if (imageUri !=null){
            final StorageReference fileReference=storageReference.child(System.currentTimeMillis()+
                    "."+getFileExtension(imageUri));
            uploadTask=fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull  Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw  task.getException();
                    }
                    return  fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri=task.getResult();
                        String mUri =downloadUri.toString();

                        submitFeed(mUri,"image");

                        pd.dismiss();
                    }else{
                        Toast.makeText(getBaseContext(),"Failed",Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull  Exception e) {
                    Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }else {
            Toast.makeText(getBaseContext(),"No image selected",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            imageUri=data.getData();
            uploadImage();

        }
    }

    public void displayAlertDialog() {

        reference=FirebaseDatabase.getInstance().getReference("Subjects");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int d=0;
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    Subject subject=snapshot1.getValue(Subject.class);
                    d++;
                }
                String[] subjectName = new String[d];
                d=0;
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    Subject subject=snapshot1.getValue(Subject.class);

                    subjectName[d]=subject.getName();
                    d++;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(ClassActivity.this);
                builder.setTitle("Chọn môn học muốn thêm vào:");

                builder.setItems(subjectName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        submitFeed(subjectName[which],"subject");
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}