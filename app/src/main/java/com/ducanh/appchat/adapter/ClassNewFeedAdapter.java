package com.ducanh.appchat.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ducanh.appchat.AddSubjectActivity;
import com.ducanh.appchat.MainActivity;
import com.ducanh.appchat.MessageActivity;
import com.ducanh.appchat.R;
import com.ducanh.appchat.TestActivity;
import com.ducanh.appchat.activity.ClassActivity;
import com.ducanh.appchat.activity.WebViewActivity;
import com.ducanh.appchat.model.ChatList;
import com.ducanh.appchat.model.Class;
import com.ducanh.appchat.model.ClassFeed;
import com.ducanh.appchat.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

public class ClassNewFeedAdapter extends RecyclerView.Adapter<ClassNewFeedAdapter.ViewHolder> {

    private Context context;
    private List<ClassFeed> listFeed;
    private String className;
    LayoutInflater inflater;

    FirebaseUser firebaseUser;
    ProgressDialog progressDialog;
    private boolean role;
    private String myUserID;

    public ClassNewFeedAdapter(Context context, List<ClassFeed> listFeed,String className) {
        this.context = context;
        this.listFeed = listFeed;
        sort();
        this.role=getRole();
        this.className=className;
        this.myUserID=FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public ClassNewFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_classnewfeed,parent,false);
        return new ClassNewFeedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassNewFeedAdapter.ViewHolder holder, int position) {
        ClassFeed feed=listFeed.get(position);
        setUser(holder.txtUsername,holder.profileImage,feed.getUserID());

        if (role)
        holder.btn_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialogActionAdmin(feed,feed.getUserID(),position);
            }
        });
        else {
            if (feed.getUserID().equals(myUserID)){
                holder.btn_action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openDialogActionMe(feed,myUserID);
                    }
                });

            }else{
                holder.btn_action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openDialogActionFriend(position,feed.getUserID());
                    }
                });

            }

        }

        if (feed.getType().equals("text")){
            holder.txtContent.setVisibility(View.VISIBLE);
            holder.imageContent.setVisibility(View.GONE);
            holder.videoView.setVisibility(View.GONE);
            holder.txtSubjectName.setVisibility(View.GONE);
            holder.btnViewTest.setVisibility(View.GONE);

            holder.txtContent.setText(feed.getContent());


        }else
        if (feed.getType().equals("image")){
            holder.txtContent.setVisibility(View.GONE);
            holder.videoView.setVisibility(View.GONE);
            holder.txtSubjectName.setVisibility(View.GONE);
            holder.btnViewTest.setVisibility(View.GONE);
            holder.imageContent.setVisibility(View.VISIBLE);

            Picasso.get().load(feed.getContent()).into(holder.imageContent);

            holder.imageContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    displayAlertDialog(feed.getContent());
                }
            });
        }else
        if (feed.getType().equals("video")){
            holder.txtContent.setVisibility(View.GONE);
            holder.imageContent.setVisibility(View.GONE);
            holder.txtSubjectName.setVisibility(View.GONE);
            holder.btnViewTest.setVisibility(View.GONE);
            holder.videoView.setVisibility(View.VISIBLE);

//            MediaController mc = new MediaController(context);
//            holder.videoView.setMediaController(mc);
//            holder.videoView.setVideoURI(Uri.parse(feed.getContent()));
//
            Uri uri = Uri.parse(feed.getContent());
            holder.videoView.setVideoURI(uri);
//            MediaController mc = new MediaController(context.getApplicationContext());
//            holder.videoView.setMediaController(mc);
//            mc.setAnchorView(holder.videoView);
            holder.videoView.start();

            holder.videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }else

        if (feed.getType().equals("subject")){
            holder.txtContent.setVisibility(View.GONE);
            holder.imageContent.setVisibility(View.GONE);
            holder.videoView.setVisibility(View.GONE);
            holder.txtSubjectName.setVisibility(View.VISIBLE);
            holder.btnViewTest.setVisibility(View.VISIBLE);

            holder.txtSubjectName.setText(feed.getContent());

            holder.btnViewTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context.getApplicationContext(), TestActivity.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("subjectName", feed.getContent());
                    context.getApplicationContext().startActivity(intent);
                }
            });
        }else
        if (feed.getType().equals("link")){
            holder.txtContent.setVisibility(View.VISIBLE);
            holder.imageContent.setVisibility(View.GONE);
            holder.videoView.setVisibility(View.GONE);
            holder.txtSubjectName.setVisibility(View.GONE);
            holder.btnViewTest.setVisibility(View.GONE);

            holder.txtContent.setText(feed.getContent());
            holder.txtContent.setTextColor(Color.BLUE);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, WebViewActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("link",feed.getContent() );
                    intent.putExtra("type","link" );
                    context.startActivity(intent);
                }
            });
        }
        else{
            holder.txtContent.setVisibility(View.VISIBLE);
            holder.imageContent.setVisibility(View.GONE);
            holder.videoView.setVisibility(View.GONE);
            holder.txtSubjectName.setVisibility(View.GONE);
            holder.btnViewTest.setVisibility(View.GONE);

            holder.txtContent.setText(feed.getType());

            holder.txtContent.setTextColor(Color.BLUE);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (feed.getType().indexOf(".pdf")!=-1) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("link", feed.getContent());
                        intent.putExtra("type", "pdf");
                        context.startActivity(intent);
                    }else {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(feed.getContent()));
                        browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(browserIntent);
                    }
                }
            });


        }

    }

    @Override
    public int getItemCount() {
        return listFeed.size();
    }

    public class  ViewHolder extends  RecyclerView.ViewHolder{
        public CircleImageView profileImage;
        public TextView txtUsername,txtContent,txtSubjectName;
        public ImageView imageContent;
        public VideoView videoView;
        public Button btnViewTest;
        public ImageButton btn_action;

        public ViewHolder(View itemView){
            super(itemView);
            profileImage=itemView.findViewById(R.id.profile_image);
            txtUsername=itemView.findViewById(R.id.txt_username);
            txtContent=itemView.findViewById(R.id.txt_content);
            imageContent=itemView.findViewById(R.id.image_content);
            videoView=itemView.findViewById(R.id.video_view);
            txtSubjectName=itemView.findViewById(R.id.txt_subjectName);
            btnViewTest=itemView.findViewById(R.id.btn_viewTest);
            btn_action=itemView.findViewById(R.id.btn_action);
        }
    }
    private void setUser(TextView username,CircleImageView profileImage,String userID){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=new User();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    user=snapshot1.getValue(User.class);
                    if (user.getId().equals(userID)){
                        username.setText(user.getUsername());
                        Glide.with(context).load(user.getImageURL()).into(profileImage);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public boolean getRole(){
        SharedPreferences sharedPreferences= context.getSharedPreferences("roleApp", Context.MODE_PRIVATE);
        if(sharedPreferences!= null) {
            return sharedPreferences.getBoolean("role", false);
        }else return false;
    }

    public void displayAlertDialog(String imageURL) {
        inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alertLayout = inflater.inflate(R.layout.imageview_dialog, null);
        final ImageView imageView = (ImageView) alertLayout.findViewById(R.id.imageViewMess);
        Picasso.get().load(imageURL).into(imageView);


        AlertDialog.Builder alert = new AlertDialog.Builder(context.getApplicationContext());
        alert.setTitle("Ảnh");
        alert.setView(alertLayout);
        alert.setCancelable(true);

        AlertDialog dialog = alert.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
    }
    private void openDialogActionAdmin(ClassFeed feed,String userID,int position)  {
//        LoginAccountDialog dialog_sucess = new LoginAccountDialog (this,LoginActivity.this) ;
//        dialog_sucess.show();
//        dialog_sucess.setCancelable(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("");
        String[] action={"Xóa","Ghim","Nhắn tin"};

        builder.setItems(action, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int d=0;
                switch (which){
                    case 0:{
                        deleteFeed(feed,userID);
                        break;
                    }
                    case 1:{
                        pinFeed(feed,position);
                        break;
                    }
                    case 2:{
                        Intent intent=new Intent(context, MessageActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("userID", userID);

                        context.startActivity(intent);

                        break;
                    }
                }

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void openDialogActionMe(ClassFeed feed,String userID)  {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("");
        String[] action={"Xóa","Sửa"};
        builder.setItems(action, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:{
                        deleteFeed(feed,userID);
                        break;
                    }
                    case 1:{

                        break;
                    }
                }

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void openDialogActionFriend(int position,String userID)  {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("");
        String[] action={"Nhắn tin"};
        builder.setItems(action, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:{
                        Intent intent=new Intent(context, MessageActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("userID", userID);
                        context.startActivity(intent);

                        break;
                    }
                }

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteFeed(ClassFeed feed,String userID){

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Class").child(className).child(userID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()) {
                    ClassFeed classFeed = snapshot1.getValue(ClassFeed.class);


                        if (classFeed.getContent().equals(feed.getContent())) {
                            snapshot1.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    new ClassNewFeedAdapter(context, listFeed, className);
                                }
                            });
                            break;
                        }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void sort(){
        List<ClassFeed> listFeed2=new ArrayList<>();
        for (int i=listFeed.size()-1;i>=0;i--)
            listFeed2.add(listFeed.get(i));
        listFeed=listFeed2;
    }
    private void pinFeed(ClassFeed feed,int position){
        System.out.println("chay toi pin feed======================");
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Class").child(className).child(feed.getUserID());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()) {
                    ClassFeed classFeed = snapshot1.getValue(ClassFeed.class);


                    if (classFeed.getContent().equals(feed.getContent())) {
                        System.out.println("======================"+listFeed.get(position).getContent());
//                        swap(listFeed.get(0),listFeed.get(position));

                        List<ClassFeed> listFeed2=new ArrayList<>();
                        listFeed2.add(feed);
                        for (int i=listFeed.size()-1;i>=0;i--)
                            if (i!=position)
                                listFeed2.add(listFeed.get(i));
                        System.out.println("======================"+listFeed2.get(0).getContent());
                        listFeed=listFeed2;
                        new ClassNewFeedAdapter(context, listFeed2, className);

                        break;
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void swap(ClassFeed feed1, ClassFeed feed2){
        ClassFeed feedTG=feed1;
        feed1=feed2;
        feed2=feedTG;
    }

}
