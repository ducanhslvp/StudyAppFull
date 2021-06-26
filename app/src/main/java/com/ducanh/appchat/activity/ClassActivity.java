package com.ducanh.appchat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ducanh.appchat.AddSubjectActivity;
import com.ducanh.appchat.R;
import com.ducanh.appchat.adapter.ClassNewFeedAdapter;
import com.ducanh.appchat.model.ClassFeed;
import com.ducanh.appchat.model.Subject;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

    ImageButton btnSubmit, btnPicture,btnSubject,btnAddVideo,btnAddFile;
    EditText textSend;
    List<ClassFeed> listClassFeed=new ArrayList<>();
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    StorageReference storageReference;
    private static final  int IMAGE_REQUEST=1;
    private final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;

    private final int PICK_VIDEO_REQUEST = 2;
    private Uri videoUri;

    private final int PICK_FILE_REQUEST = 3;
    private Uri fileUri;

    LayoutInflater inflater;

    private ImageButton btnConfirm;


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
        btnAddFile=findViewById(R.id.btn_fileAdd);
        btnConfirm=findViewById(R.id.fab_confirm);


        Intent intent=new Intent();
        intent=getIntent();
        className=intent.getStringExtra("class");


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        storageReference= FirebaseStorage.getInstance().getReference("images");
        getClassNewFeed();

        if (getRole()){
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(ClassActivity.this, ConfirmUserJoinClassActivity.class);
                    intent.putExtra("className",className);
                    startActivity(intent);

                }
            });
        }else btnConfirm.setVisibility(View.GONE);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textSend.getText().toString().indexOf("http")==-1){
                    submitFeed(textSend.getText().toString(),"text");
                }else{
                    submitFeed(textSend.getText().toString(),"link");
                }
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
                    chooseVideo();
            }
        });
        btnSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAlertDialog();


            }
        });
        btnAddFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
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
                classNewFeedAdapter=new ClassNewFeedAdapter(ClassActivity.this,listClassFeed,className);
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
    private void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO_REQUEST);
    }
    private void chooseFile() {
//        Intent intent = new Intent();
//        intent.setType("image/*|application/pdf|application/docx|audio/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_FILE_REQUEST);

        String[] mimeTypes =
                {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "application/zip"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent,"ChooseFile"), PICK_FILE_REQUEST);
    }
    private String getFileExtension(Uri uri){
        ContentResolver conentResolver=getBaseContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(conentResolver.getType(uri));
    }
    private void uploadImage(){
        final ProgressDialog pd=new ProgressDialog(ClassActivity.this);
        pd.setMessage("Đang tải lên");
        pd.show();

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
    private void uploadVideo(){
        final ProgressDialog pd=new ProgressDialog(ClassActivity.this);
        pd.setMessage("Đang tải lên");
        pd.show();
        storageReference= FirebaseStorage.getInstance().getReference("videos");
        if (videoUri !=null){
            final StorageReference fileReference=storageReference.child(System.currentTimeMillis()+
                    "."+getFileExtension(videoUri));
            uploadTask=fileReference.putFile(videoUri);
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

                        submitFeed(mUri,"video");

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
            Toast.makeText(getBaseContext(),"No video selected",Toast.LENGTH_SHORT).show();
        }
    }
    private void uploadFile(){
        storageReference= FirebaseStorage.getInstance().getReference("files");
        final ProgressDialog pd=new ProgressDialog(ClassActivity.this);
        pd.setMessage("Đang tải lên");
        pd.show();

        if (fileUri !=null){
            final StorageReference fileReference=storageReference.child(getFileName(fileUri));
            uploadTask=fileReference.putFile(fileUri);
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

                        submitFeed(mUri,getFileName(fileUri));

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
            Toast.makeText(getBaseContext(),"No File selected",Toast.LENGTH_SHORT).show();
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

        if(requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            videoUri=data.getData();
            uploadVideo();
        }
        if(requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            fileUri=data.getData();
            uploadFile();
        }
    }

    public void displayAlertDialog() {

        reference=FirebaseDatabase.getInstance().getReference("Subjects");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int d=1;
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    Subject subject=snapshot1.getValue(Subject.class);
                    d++;
                }
                String[] subjectName = new String[d];
                subjectName[0]="+ Thêm bài làm mới";
                d=1;
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    Subject subject=snapshot1.getValue(Subject.class);

                    subjectName[d]=subject.getName();
                    d++;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(ClassActivity.this);
                builder.setTitle("Chọn bài muốn thêm vào:");

                builder.setItems(subjectName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which==0){
                            Intent intent=new Intent(ClassActivity.this, AddSubjectActivity.class);
                            intent.putExtra("className",className);
                            startActivity(intent);
                        }else
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

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
    public boolean getRole(){
        SharedPreferences sharedPreferences= getSharedPreferences("roleApp", Context.MODE_PRIVATE);
        if(sharedPreferences!= null) {
            return sharedPreferences.getBoolean("role", false);
        }else return false;
    }
}