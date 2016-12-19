package com.vogella.maven.quickstart;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.tasks.OnSuccessListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class frame extends JFrame implements ActionListener{
	JPanel panel = new JPanel();
	JTextField username = new JTextField(20);
	JTextField password = new JTextField(20);
	
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
		
		
		email.setText("E-mail:");
		pass.setText("Password:");
		getContentPane().add(panel);
		
		setSize(300,200);
		setVisible(true);
		
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == login){
			System.out.println("Tring to Log In");
		    //Firebaseに元々入れたトークンを入れてみました
			String sampletoken = "aHN9WZxWHrOJABL8Nues9SpoBfq2";
			FirebaseAuth.getInstance().verifyIdToken(sampletoken).addOnSuccessListener(new OnSuccessListener<FirebaseToken>(){

				public void onSuccess(FirebaseToken decodedToken) {
					String uid = decodedToken.getUid();
					System.out.println("Logged In!");
					
				}
				
			});
			
			}
		else {
		}
		}
		
	}


class User{
	
	public String date_of_birth;
	public String full_name;
	public String nickname;
	
	public User(String date_of_birth, String full_name){
		this.date_of_birth = date_of_birth;
		this.full_name = full_name;
	}
	
	public User(String date_of_birth, String full_name, String nickname){
		this.date_of_birth = date_of_birth;
		this.full_name = full_name;
		this.nickname = nickname;
		
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
	      
	      //As an admin, the app has access to read and write all data,regardless of Security Rules//
	      DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user");
	      ref.addListenerForSingleValueEvent(new ValueEventListener(){
	    	  public void onDataChange(DataSnapshot dataSnapshot){
	    		  Object document = dataSnapshot.getValue();
	    		  System.out.println(document);
	    	  }

			public void onCancelled(DatabaseError arg0) {
				// TODO 自動生成されたメソッド・スタブ
				
			}
	      });
	      
	    

	      Map<String,User> users = new HashMap<String, User>();
	      users.put("alanisawesome", new User("June 23,1912","Alan Turing"));
	      
	      
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
	
	      }

	
	
    public static void main( String[] args ) throws FileNotFoundException 
    {
        configFirebase();
        new frame();
        
    }
}
