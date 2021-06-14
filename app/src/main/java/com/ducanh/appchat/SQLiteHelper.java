package com.ducanh.appchat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.ducanh.appchat.model.Noti;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="Notification.db";
    private static final int DATABSE_VERSION=1;


    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME,null,DATABSE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="CREATE TABLE Noti(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "time TEXT," +
                "title TEXT)";
        db.execSQL(sql);
//      addNoti(new Noti(1,"00/30/00/04/06/2021","Làm bài tập"));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    public void addNoti(Noti noti){

        String sql="INSERT INTO Noti(time,title) VALUES (?,?)";
        String[] args={noti.getTime(),noti.getTitle()};
        SQLiteDatabase statement =getWritableDatabase();
        statement.execSQL(sql,args);
    }
    public List<Noti> getNoti() throws ParseException {
//        addNoti(new Noti(1,"00/30/00/04/06/2021","Làm bài tập"));
        List<Noti> list=new ArrayList<>();
        list.add(new Noti(1,"00/30/00/04/06/2021","Làm bài tập"));
        SQLiteDatabase statement=getReadableDatabase();
//        addNoti(new Noti(0,"00:30:12:12:2020","test"));
        Cursor rs=statement.query("Noti",null,
                null,null,null,
                null,null);
        while((rs!=null && rs.moveToNext())){
            int id=rs.getInt(0);

            String time=rs.getString(1);
            String title=rs.getString(2);
            Noti noti=new Noti(0,time,title);

            list.add(noti);
        }
       return list;
    }
//    public List<LichThi> searchCourse(String clause) throws ParseException {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//        List<LichThi> cours = new ArrayList<>();
//        String whereClause = "ten LIKE ?";
//        String[] whereArgs = {"%" + clause + "%"};
//        SQLiteDatabase statement = getReadableDatabase();
//        Cursor rs = statement.query("LichThi", null, whereClause, whereArgs, null, null, null);
//        while (rs != null && rs.moveToNext()) {
//            int id=rs.getInt(0);
//            String name=rs.getString(1);
//            String date=rs.getString(2);
//            String check=rs.getString(3);
//            boolean active;
//            if (check.equals("1")) active=true;
//            else active=false;
//            cours.add(new LichThi(id,name,date,active));
//        }
//        rs.close();
//        return cours;
//    }
//
//    public int update(LichThi lichThi){
//        ContentValues values = new ContentValues();
//        values.put("ten", lichThi.getName());
//        values.put("ngay", lichThi.getNgay());
//
//        if (lichThi.isThiViet())
//            values.put("viet",  "1");
//        else values.put("viet",  "0");
//
//        String whereClause = "id = ?";
//        String[] whereArgs = {String.valueOf(lichThi.getId())};
//        SQLiteDatabase statement = getWritableDatabase();
//        System.out.println("da chay qua");
//        return statement.update("LichThi", values, whereClause, whereArgs);
//    }
//
//
//    public int deleteById(int id){
//        String whereClause = "id = ?";
//        String[] whereArgs = {String.valueOf(id)};
//        SQLiteDatabase statement = getWritableDatabase();
//        return statement.delete("LichThi", whereClause, whereArgs);
//    }

}
