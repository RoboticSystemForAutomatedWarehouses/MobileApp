package com.example.madara.awsms.utils;

import android.app.Activity;
import android.content.Intent;

import com.example.madara.awsms.models.User;


import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by madara on 2/27/18.
 */

public class Session {
    private static Session instance;
    private Realm realm;
    public static Session getInstance(){
        if(instance==null){
            instance = new Session();
        }
        return instance;
    }
    private Session(){
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(realmConfiguration);
    }
    public void startSession(final User user) {
        if ( realm.where(User.class).findFirst()==null){

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealm(user);
                }
            });
        }
        else {
            logout();
            startSession(user);
        }
    }
    public void logout(){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(User.class);
            }
        });
    }
    public boolean isUserLoggedIn(){
        if(realm.where(User.class).findFirst()==null){
            return false;
        }
        else {
            return true;
        }
    }
    public User getUser(){
        return realm.where(User.class).findFirst();
    }
    public void logoutAndGoToLogin(Activity activity) {
        logout();
        //activity.startActivity(new Intent(activity, LoginScreen.class));
        //activity.finish();
    }
    public void logoutsecurity(Activity activity){
        if(!Session.getInstance().isUserLoggedIn()){
           // Intent intent = new Intent(activity,LoginScreen.class);
            //activity.startActivity(intent);
            //activity.finish();
        }
    }
}
