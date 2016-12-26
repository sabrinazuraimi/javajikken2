package com.vogella.maven.quickstart;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JFrame;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.tasks.OnSuccessListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class User{
	
	public String date_of_birth;
	public String full_name;
	public String nickname;
	public String password;
	public String email;
	
	//a constructor with no arguments because later when I use the query thingy I need this..//
	public User(){}
	
	public User(String date_of_birth, String full_name){
		this.date_of_birth = date_of_birth;
		this.full_name = full_name;
	}
	
	public User(String date_of_birth, String full_name, String email){
	 this.date_of_birth = date_of_birth;
	 this.full_name = full_name;
	 this.email = email;
	}
}

class frame extends JFrame implements ActionListener{
	
	String usertext = new String("");
	
	JPanel panel = new JPanel();
	
	JTextField username = new JTextField(20);
	JPasswordField password = new JPasswordField(20);
	
	JLabel email = new JLabel("");
	JLabel pass = new JLabel("");
	
	JButton login = new JButton("Log In");
    JButton signup = new JButton("Sign Up");
    

	frame(){
		setTitle("Log In Page");
		login.addActionListener(this);
		signup.addActionListener(this);
        
		panel.setLayout(new FlowLayout());
		panel.add(email);
		panel.add(username);
		panel.add(pass);
		panel.add(password);
		panel.add(login);
		panel.add(signup);
		
		
		email.setText("Username:");
		pass.setText("Password:");
		getContentPane().add(panel);
		
		setSize(300,200);
		setVisible(true);
		
	}
    
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == login){
			
			System.out.println("Tring to Log In");
		    //Firebaseに元々入れたトークンを入れてみました
//			String sampletoken = "aHN9WZxWHrOJABL8Nues9SpoBfq2";
//			FirebaseAuth.getInstance().verifyIdToken(sampletoken).addOnSuccessListener(new OnSuccessListener<FirebaseToken>(){
//				public void onSuccess(FirebaseToken decodedToken) {
//					String uid = decodedToken.getUid();
//					System.out.println("Logged In!");
//				}
//			});
			
			usertext = username.getText();
			char[] passwordtext = password.getPassword();
			
			System.out.println("入力したユーザーは"+usertext);
			System.out.println("入力したパスワードは"+Arrays.toString(passwordtext));
			
			
			//Getting a reference//
			final FirebaseDatabase database = FirebaseDatabase.getInstance();
			final DatabaseReference ref = database.getReference("user");
			
	        ref.addValueEventListener(
	        		new ValueEventListener(){
	        			public void onDataChange(DataSnapshot dataSnapshot){
	        				Object user = dataSnapshot.getValue();
	        				System.out.println("Getting the data");
	        				System.out.println(user);
	        				System.out.println("Got data");
	        				
	        				//取得されたデータのクラスを確認する//
	        				System.out.println("The user class is"+user.getClass());
	        				
	        			    Iterator it = ((Map) user).entrySet().iterator();
	        			    while (it.hasNext()) {
	        			        Map.Entry pair = (Map.Entry)it.next();
	        			        System.out.println(pair.getKey() + " = " + pair.getValue());
	        			        String key = (String) pair.getKey();
	        			        String value = (String) pair.getValue();
	        			        System.out.println("Key is"+key);
	        			        System.out.println("Value is"+value);
	        			        
	        			        if (key.equals(usertext)){
	        			        	System.out.println("Username exists");
	        			        }
	        			        it.remove(); // avoids a ConcurrentModificationException
	        			    }
	        			}
	        			
	        			

						public void onCancelled(DatabaseError error) {
							System.out.println("Read error because of"+error);
						}
	        		});
		}
		else {
		}
		}
		

	}

public class App 
{
	public static void configFirebase() throws FileNotFoundException {
	       /*Firebase SDKをinitializeするために*/
	      FirebaseOptions options = new FirebaseOptions.Builder()
	    		  .setServiceAccount(new FileInputStream("serviceAccounKey.json"))
	    		  .setDatabaseUrl("https://javajikken.firebaseio.com")
	    		  .build();
	      
	      FirebaseApp.initializeApp(options);
	      System.out.println("App Initialized!");
	     
	      DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user");
	      //試しでユーザーのデータ作った
	      Map<String,User> users = new HashMap<String, User>();
	      users.put("Alan", new User("June 23,1912","Alan Turing","alanturing@outlook.com"));
	     //ここでデータをデータベースに書き込む//
	     ref.setValue(users, new DatabaseReference.CompletionListener(){
	  			public void onComplete(DatabaseError dataerror, DatabaseReference dataref) {
					if(dataerror != null){
						System.out.println("Data could not be saved" + dataerror);
					}
					else {
						System.out.println("Data saved successfully.");
					}
	      }
	      });
	
	     
	     //試しでユーザーのデータを取って来る
	   
	      }

	
	
    public static void main( String[] args ) throws FileNotFoundException 
    {
        configFirebase();
        new frame();
        
    }
}
