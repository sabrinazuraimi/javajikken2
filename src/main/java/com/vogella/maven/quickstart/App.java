package com.vogella.maven.quickstart;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

class User{
	
	public String date_of_birth;
	public String full_name;
	public String nickname;
	
	public User(String date_of_birth, String full_name){
		//..
	}
	
	public User(String date_of_birth, String full_name, String nickname){
		//...
	}
}





public class App 
{
	public void configFirebase() throws FileNotFoundException {
	       /*Firebase SDKをinitializeするために*/
	      FirebaseOptions options = new FirebaseOptions.Builder()
	    		  .setServiceAccount(new FileInputStream("path/to/serviceAccountKey.json"))
	    		  .setDatabaseUrl("https://javajikken.firebaseio.com/")
	    		  .build();
	      
	      FirebaseApp.initializeApp(options);
	  
	      //As an admin, the app has access to read and write all data,regardless of Security Rules//
	      DatabaseReference ref = FirebaseDatabase.getInstance().getReference("restricted_acceess/secret_document");
	      ref.addListenerForSingleValueEvent(new ValueEventListener(){
	    	  public void onDataChange(DataSnapshot dataSnapshot){
	    		  Object document = dataSnapshot.getValue();
	    		  System.out.println(document);
	    	  }

			public void onCancelled(DatabaseError arg0) {
				// TODO 自動生成されたメソッド・スタブ
				
			}
	      });
	      
	      final FirebaseDatabase database = FirebaseDatabase.getInstance();
	      DatabaseReference ref1 = database.getReference("server/saving-data/fireblog");
	      DatabaseReference usersRef = ref1.child("users");
	      
	      Map<String,User> users = new HashMap<String, User>();
	      users.put("alanisawesome", new User("June 23,1912","Alan Turing"));
	      users.put("gracehop", new User("December 9,1906","Grace Hopper"));
	      
	      usersRef.setValue(users);

	      }
	
	
    public static void main( String[] args ) 
    {
         
      
    }
}
