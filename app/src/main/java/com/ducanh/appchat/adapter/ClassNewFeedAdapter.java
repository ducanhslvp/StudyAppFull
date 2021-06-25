package com.ducanh.appchat.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.ducanh.appchat.R;
import com.ducanh.appchat.TestActivity;
import com.ducanh.appchat.activity.ClassActivity;
import com.ducanh.appchat.activity.WebViewActivity;
import com.ducanh.appchat.model.ChatList;
import com.ducanh.appchat.model.ClassFeed;
import com.ducanh.appchat.model.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

public class ClassNewFeedAdapter extends RecyclerView.Adapter<ClassNewFeedAdapter.ViewHolder> {

    private Context context;
    private List<ClassFeed> listFeed;
    LayoutInflater inflater;

    FirebaseUser firebaseUser;
    ProgressDialog progressDialog;
    private boolean role;

    public ClassNewFeedAdapter(Context context, List<ClassFeed> listFeed) {
        this.context = context;
        this.listFeed = listFeed;
        this.role=getRole();
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

            }
        });
        else holder.btn_action.setVisibility(View.GONE);

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

}
