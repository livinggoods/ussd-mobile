package com.gadiness.kimaru.ussdhelper.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SimpleDateFormat;
import android.util.Log;

import com.gadiness.kimaru.ussdhelper.mzigos.PhoneQueue;
import com.gadiness.kimaru.ussdhelper.mzigos.Queue;
import com.gadiness.kimaru.ussdhelper.mzigos.UssdMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.icu.lang.UProperty.MATH;

/**
 * Created by kimaru on 10/6/17.
 */

public class UssdDbHelper extends SQLiteOpenHelper{
    public static final String USSD_TABLE_NAME="ussd_messages";
    public static final String PHONE_QUEUE_TABLE_NAME="phone_queue";
    public static final String UPSTREAM_QUEUE_TABLE="queues";
    public static final String PHONE_JSON_ROOT="phones";
    public static final String UPSTREAM_QUEUE_JSON_ROOT="queue";
    public static final String USSD_JSON_ROOT="messages";
    public static final String DATABASE_NAME= "ussd.db";
    public static final int DATABASE_VERSION= 1;

    Context context;


    public static String varchar_field = " varchar(512) ";
    public static String real_field = " REAL ";
    public static String primary_field = " INTEGER PRIMARY KEY AUTOINCREMENT ";
    public static String integer_field = " integer default 0 ";
    public static String text_field = " text ";

    public static final String ID= "id";
    public static final String BRANCH_ID= "branch_id";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String STATUS = "status";
    public static final String COUNTRY = "country";
    public static final String ERROR_MESSAGE = "error_message";
    public static final String SENT = "sent";
    public static final String MESSAGE = "message";
    public static final String PHONE_ID = "phone_id";
    public static final String QUEUE_ID = "queue_id";
    public static final String BUNDLE_BALANCE = "bundle_balance";
    public static final String EXPIRY_DATETIME = "expiry_datetime";
    public static final String MESSAGE_TYPE_ID = "message_type_id";
    public static final String DATE_ADDED = "date_added";
    public static final String ACTIVE = "active";
    public static final String DELETED = "deleted";
    public static final String SYNCED = "synced";
    public static final String ASSIGNED_TO = "assigned_to";
    public static final String BRANCH_NAME = "branch_name";
    public static final String NAME = "name";
    public static final String COMPLETED = "completed";
    public static final String SELECTED = "selected";

    String [] ussdMessageColumns=new String[]{ID, PHONE_NUMBER, MESSAGE, BRANCH_ID, PHONE_ID,
            BUNDLE_BALANCE, EXPIRY_DATETIME, MESSAGE_TYPE_ID, COUNTRY, DATE_ADDED, ACTIVE, DELETED, QUEUE_ID};

    String [] phoneQueueColumns = new String[] {ID, BRANCH_ID, PHONE_NUMBER, STATUS,
            ERROR_MESSAGE, COUNTRY, SENT, DELETED, QUEUE_ID, ASSIGNED_TO, BRANCH_NAME, PHONE_ID, SYNCED};

    String [] upstreamQueueCols = new String []{ID, BRANCH_ID, BRANCH_NAME, NAME, STATUS, COUNTRY,
            DELETED, DATE_ADDED, COMPLETED, SELECTED};

    public static final String CREATE_QUEUE_TABLE="CREATE TABLE " + PHONE_QUEUE_TABLE_NAME + "("
            + ID + primary_field + ", "
            + BRANCH_ID + integer_field + ", "
            + BRANCH_NAME + varchar_field + ", "
            + PHONE_NUMBER + varchar_field + ", "
            + PHONE_ID + integer_field + ", "
            + STATUS + integer_field + ", "
            + ERROR_MESSAGE + text_field + ", "
            + COUNTRY + varchar_field + ", "
            + SENT + real_field + ", "
            + DELETED + real_field + ", "
            + QUEUE_ID + integer_field + ", "
            + ASSIGNED_TO + varchar_field + ", "
            + DATE_ADDED + real_field + ", "
            + SYNCED + integer_field + "); ";

    public static final String CREATE_UPSTREAM_QUEUE ="CREATE TABLE " + UPSTREAM_QUEUE_TABLE + "("
            + ID + primary_field + ", "
            + BRANCH_ID + integer_field + ", "
            + BRANCH_NAME + varchar_field + ", "
            + NAME + varchar_field + ", "
            + STATUS + integer_field + ", "
            + COUNTRY + integer_field + ", "
            + DELETED + real_field + ", "
            + DATE_ADDED + real_field + ", "
            + COMPLETED + real_field + ", "
            + SELECTED + integer_field + ", "
            + SYNCED + integer_field + "); ";

    public static final String CREATE_MESSAGE_TABLE="CREATE TABLE " + USSD_TABLE_NAME + "("
            + ID + primary_field + ", "
            + PHONE_NUMBER + varchar_field + ", "
            + MESSAGE + text_field + ", "
            + BRANCH_ID + integer_field + ", "
            + PHONE_ID + integer_field + ", "
            + BUNDLE_BALANCE + real_field + ", "
            + EXPIRY_DATETIME + real_field + ", "
            + MESSAGE_TYPE_ID + integer_field + ", "
            + COUNTRY + varchar_field + ", "
            + DATE_ADDED + real_field + ", "
            + QUEUE_ID + real_field + ", "
            + ACTIVE + real_field + ", "
            + DELETED + real_field + ", "
            + SYNCED + integer_field + "); ";

    public static final String USSD_DROP="DROP TABLE IF EXISTS" + USSD_TABLE_NAME;
    public static final String PHONE_DROP="DROP TABLE IF EXISTS" + PHONE_QUEUE_TABLE_NAME;

    public UssdDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        if (!isTableExists(UPSTREAM_QUEUE_TABLE)){
            createUpstreamQueueTable();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MESSAGE_TABLE);
        db.execSQL(CREATE_QUEUE_TABLE);
        db.execSQL(CREATE_UPSTREAM_QUEUE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createUpstreamQueueTable(){
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(CREATE_UPSTREAM_QUEUE);
    }


    public long addUssdMessage(UssdMessage message) {

        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        // cv.put(ID, message.getId());
        cv.put(PHONE_NUMBER, message.getPhoneNumber());
        cv.put(MESSAGE, message.getMessage());
        cv.put(BRANCH_ID, message.getBranchId());
        cv.put(PHONE_ID, message.getPhoneId());
        cv.put(BUNDLE_BALANCE, message.getBundleBalance());
        cv.put(EXPIRY_DATETIME, message.getExpiryDateTime());
        cv.put(MESSAGE_TYPE_ID, message.getMessageTypeId());
        cv.put(COUNTRY, message.getCountry());
        cv.put(DATE_ADDED, message.getDateAdded());
        cv.put(ACTIVE, message.isActive());
        cv.put(DELETED, message.isDeleted());
        cv.put(SYNCED, message.isSynced());
        cv.put(QUEUE_ID, message.getQueueId());
        long id;
        id = db.insert(USSD_TABLE_NAME, null, cv);
        db.close();
        return id;
    }

    public long deletePhoneQueue(PhoneQueue phoneQueue) {
        SQLiteDatabase db = getReadableDatabase();
        //db.delete(DATABASE_TABLE, KEY_NAME + "=" + name, null) > 0;

        String whereClause = ID+" = ?";
        String[] whereArgs = new String[] {
                String.valueOf(phoneQueue.getId()),
        };
        int status=db.delete(PHONE_QUEUE_TABLE_NAME,whereClause,whereArgs);
        return status;
    }


    public long deleteUssdMessage(UssdMessage message) {
        SQLiteDatabase db = getReadableDatabase();
        //db.delete(DATABASE_TABLE, KEY_NAME + "=" + name, null) > 0;

        String whereClause = ID+" = ?";
        String[] whereArgs = new String[] {
                String.valueOf(message.getId()),
        };
        int status=db.delete(USSD_TABLE_NAME,whereClause,whereArgs);
        return status;
    }

    public List<UssdMessage> getUssdMessages() {
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.query(USSD_TABLE_NAME,ussdMessageColumns,null,null,null,null,null,null);
        List<UssdMessage> ussdMessages=new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            UssdMessage message=new UssdMessage();
            message.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            message.setMessage(cursor.getString(cursor.getColumnIndex(MESSAGE)));
            message.setPhoneNumber(cursor.getString(cursor.getColumnIndex(PHONE_NUMBER)));
            message.setBranchId(cursor.getInt(cursor.getColumnIndex(BRANCH_ID)));
            message.setPhoneId(cursor.getInt(cursor.getColumnIndex(PHONE_ID)));
            message.setBundleBalance(cursor.getInt(cursor.getColumnIndex(BUNDLE_BALANCE)));
            message.setExpiryDateTime(cursor.getLong(cursor.getColumnIndex(EXPIRY_DATETIME)));
            message.setMessageTypeId(cursor.getInt(cursor.getColumnIndex(MESSAGE_TYPE_ID)));
            message.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
            message.setDateAdded(cursor.getLong(cursor.getColumnIndex(DATE_ADDED)));
            message.setActive(cursor.getInt(cursor.getColumnIndex(ACTIVE))==1);
            message.setDeleted(cursor.getInt(cursor.getColumnIndex(DELETED))==1);
            message.setSynced(cursor.getInt(cursor.getColumnIndex(SYNCED))==1);
            ussdMessages.add(message);
        }
        db.close();
        return ussdMessages;
    }

    public UssdMessage getUssdMessageById(int id){
        SQLiteDatabase db = getReadableDatabase();

        String whereClause = ID+" = ?";
        String[] whereArgs = new String[] {
                String.valueOf(id),
        };
        Cursor cursor=db.query(USSD_TABLE_NAME,ussdMessageColumns,whereClause,whereArgs,null,null,null,null);
        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return null;
        }else{
            UssdMessage message=new UssdMessage();
            message.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            message.setMessage(cursor.getString(cursor.getColumnIndex(MESSAGE)));
            message.setPhoneNumber(cursor.getString(cursor.getColumnIndex(PHONE_NUMBER)));
            message.setBranchId(cursor.getInt(cursor.getColumnIndex(BRANCH_ID)));
            message.setPhoneId(cursor.getInt(cursor.getColumnIndex(PHONE_ID)));
            message.setBundleBalance(cursor.getInt(cursor.getColumnIndex(BUNDLE_BALANCE)));
            message.setExpiryDateTime(cursor.getLong(cursor.getColumnIndex(EXPIRY_DATETIME)));
            message.setMessageTypeId(cursor.getInt(cursor.getColumnIndex(MESSAGE_TYPE_ID)));
            message.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
            message.setDateAdded(cursor.getLong(cursor.getColumnIndex(DATE_ADDED)));
            message.setActive(cursor.getInt(cursor.getColumnIndex(ACTIVE))==1);
            message.setDeleted(cursor.getInt(cursor.getColumnIndex(DELETED))==1);
            message.setSynced(cursor.getInt(cursor.getColumnIndex(SYNCED))==1);
            return message;
        }
    }

    public void ussdMessageFromJson(JSONObject jsonObject){
        try{
            UssdMessage message=new UssdMessage();
            message.setId(jsonObject.getInt(ID));
            message.setMessage(jsonObject.getString(MESSAGE));
            message.setPhoneNumber(jsonObject.getString(PHONE_NUMBER));
            message.setBranchId(jsonObject.getInt(BRANCH_ID));
            message.setPhoneId(jsonObject.getInt(PHONE_ID));
            message.setBundleBalance(jsonObject.getInt(BUNDLE_BALANCE));
            message.setExpiryDateTime(jsonObject.getLong(EXPIRY_DATETIME));
            message.setMessageTypeId(jsonObject.getInt(MESSAGE_TYPE_ID));
            message.setCountry(jsonObject.getString(COUNTRY));
            message.setDateAdded(jsonObject.getLong(DATE_ADDED));
            message.setActive(jsonObject.getInt(ACTIVE)==1);
            message.setDeleted(jsonObject.getInt(DELETED)==1);
            message.setSynced(jsonObject.getInt(SYNCED)==1);
            this.addUssdMessage(message);
        }catch (Exception e){}
    }

    public JSONObject getMessagesToSyncJson() {
        SQLiteDatabase db=getReadableDatabase();
        String whereClause = SYNCED+" = ?";
        String[] whereArgs = new String[] {
                "0",
        };
        Cursor cursor=db.query(USSD_TABLE_NAME,ussdMessageColumns,whereClause,whereArgs,null,null,null,null);
        return cursorToJson(cursor, USSD_JSON_ROOT);
    }


    public long addPhoneQueue(PhoneQueue phone) {

        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put(ID, phone.getId());
        cv.put(BRANCH_ID, phone.getBranchId());
        cv.put(PHONE_NUMBER, phone.getPhoneNumber());
        cv.put(STATUS, phone.getStatus());
        cv.put(ERROR_MESSAGE, phone.getErrorMessage());
        cv.put(COUNTRY, phone.getCountry());
        cv.put(SENT, phone.isSent());
        cv.put(DELETED, phone.isDeleted());
        cv.put(SYNCED, phone.isSynced());

        long id;
        if (isExistPhone(phone)){
            id = db.update(PHONE_QUEUE_TABLE_NAME, cv, ID+"='"+phone.getId()+"'", null);
        }else{
            id = db.insertWithOnConflict(PHONE_QUEUE_TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        }
        db.close();
        return id;
    }

    public List<PhoneQueue> getPhoneQueus(){
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.query(PHONE_QUEUE_TABLE_NAME,phoneQueueColumns,null,null,null,null,null,null);
        List<PhoneQueue> phoneQueues=new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            PhoneQueue phoneQueue = new PhoneQueue();
            phoneQueue.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            phoneQueue.setBranchId(cursor.getInt(cursor.getColumnIndex(BRANCH_ID)));
            phoneQueue.setPhoneNumber(cursor.getString(cursor.getColumnIndex(PHONE_NUMBER)));
            phoneQueue.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
            phoneQueue.setErrorMessage(cursor.getString(cursor.getColumnIndex(ERROR_MESSAGE)));
            phoneQueue.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
            phoneQueue.setSent(cursor.getInt(cursor.getColumnIndex(SENT))==1);
            phoneQueue.setDeleted(cursor.getInt(cursor.getColumnIndex(DELETED))==1);
            phoneQueue.setSynced(cursor.getInt(cursor.getColumnIndex(SYNCED))==1);
        }
        return phoneQueues;
    }


    public void phoneFromJson(JSONObject jsonObject){
        try{
            PhoneQueue phone=new PhoneQueue();
            phone.setId(jsonObject.getInt(ID));
            phone.setBranchId(jsonObject.getInt(BRANCH_ID));
            phone.setPhoneNumber(jsonObject.getString(PHONE_NUMBER));
            phone.setStatus(jsonObject.getString(STATUS));
            phone.setErrorMessage(jsonObject.getString(ERROR_MESSAGE));
            phone.setCountry(jsonObject.getString(COUNTRY));
            phone.setSent(jsonObject.getBoolean(SENT));
            phone.setDeleted(jsonObject.getBoolean(DELETED));
            phone.setSynced(jsonObject.getBoolean(SYNCED));
            this.addPhoneQueue(phone);
        }catch (Exception e){}
    }

    public PhoneQueue getPhoneById(int id){
        SQLiteDatabase db = getReadableDatabase();

        String whereClause = ID+" = ?";
        String[] whereArgs = new String[] {
                String.valueOf(id),
        };
        Cursor cursor=db.query(PHONE_QUEUE_TABLE_NAME,phoneQueueColumns,whereClause,whereArgs,null,null,null,null);
        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return null;
        }else{
            PhoneQueue phoneQueue = new PhoneQueue();
            phoneQueue.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            phoneQueue.setBranchId(cursor.getInt(cursor.getColumnIndex(BRANCH_ID)));
            phoneQueue.setPhoneNumber(cursor.getString(cursor.getColumnIndex(PHONE_NUMBER)));
            phoneQueue.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
            phoneQueue.setErrorMessage(cursor.getString(cursor.getColumnIndex(ERROR_MESSAGE)));
            phoneQueue.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
            phoneQueue.setSent(cursor.getInt(cursor.getColumnIndex(SENT))==1);
            phoneQueue.setDeleted(cursor.getInt(cursor.getColumnIndex(DELETED))==1);
            phoneQueue.setSynced(cursor.getInt(cursor.getColumnIndex(SYNCED))==1);
            return phoneQueue;
        }

    }

    public PhoneQueue getNextPhone(){
        SQLiteDatabase db = getReadableDatabase();

        String whereClause = SYNCED+" = ?";
        String[] whereArgs = new String[] {
                String.valueOf(0),
        };
        String limit = String.valueOf(1);
        Cursor cursor=db.query(PHONE_QUEUE_TABLE_NAME,phoneQueueColumns,whereClause,whereArgs,null,null,null, limit);
        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return null;
        }else{
            PhoneQueue phoneQueue = new PhoneQueue();
            phoneQueue.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            phoneQueue.setBranchId(cursor.getInt(cursor.getColumnIndex(BRANCH_ID)));
            phoneQueue.setPhoneNumber(cursor.getString(cursor.getColumnIndex(PHONE_NUMBER)));
            phoneQueue.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
            phoneQueue.setErrorMessage(cursor.getString(cursor.getColumnIndex(ERROR_MESSAGE)));
            phoneQueue.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
            phoneQueue.setSent(cursor.getInt(cursor.getColumnIndex(SENT))==1);
            phoneQueue.setDeleted(cursor.getInt(cursor.getColumnIndex(DELETED))==1);
            phoneQueue.setSynced(cursor.getInt(cursor.getColumnIndex(SYNCED))==1);
            return phoneQueue;
        }

    }

    public boolean isExist(UssdMessage message) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT id FROM " + USSD_TABLE_NAME + " WHERE "+ID+" = '" + message.getId() + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        return exist;
    }
    public boolean isExistPhone(PhoneQueue phone) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT id FROM " + PHONE_QUEUE_TABLE_NAME + " WHERE "+ID+" = '" + phone.getId() + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        return exist;

    }

    public JSONObject cursorToJson(Cursor cursor, String jsonRoot) {
        SQLiteDatabase db=getReadableDatabase();
        JSONObject results = new JSONObject();
        JSONArray resultSet = new JSONArray();
        for (cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){
            int totalColumns = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for (int i =0; i < totalColumns; i++){
                if (cursor.getColumnName(i) != null){
                    try {
                        if (cursor.getString(i) != null){
                            rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                        }else{
                            rowObject.put(cursor.getColumnName(i), "");
                        }
                    }catch (Exception e){
                    }
                }
            }
            resultSet.put(rowObject);
            try {
                results.put(jsonRoot, resultSet);
            } catch (JSONException e) {

            }
        }
        cursor.close();
        return results;
    }


    private void upgradeVersion2(SQLiteDatabase db) {}

}
