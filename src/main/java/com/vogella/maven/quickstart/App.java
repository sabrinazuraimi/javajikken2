package com.vogella.maven.quickstart;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	public String year_of_birth;
	public String full_name;
	public String nickname;
	public String password;
	public String email;
	
	//a constructor with no arguments because later when I use the query thingy I need this..//
	public User(){}
	
	public User(String year_of_birth, String full_name){
		this.year_of_birth = year_of_birth;
		this.full_name = full_name;
	}
	
	public User(String year_of_birth, String full_name, String email,String password){
	 this.year_of_birth = year_of_birth;
	 this.full_name = full_name;
	 this.email = email;
	 this.password = password;
	}
}


class frame extends JFrame implements ActionListener{

	String usertext = new String("");
	
	JPanel panel = new JPanel();
	
	JTextField username = new JTextField(20);
	JPasswordField password = new JPasswordField(20);
	JTextField full = new JTextField(20);
	JTextField birth = new JTextField(20);
	JTextField email = new JTextField(20);
	
	JLabel userlabel = new JLabel("Username:");
	JLabel pass = new JLabel("Password:");
	JLabel dob = new JLabel("Year of Birth(e.g:1992):");
	JLabel fullname = new JLabel("Full Name:");
	JLabel emailadd = new JLabel("Email Address:");
	
	
	
	JButton login = new JButton("Log In");
    JButton signup = new JButton("Sign Up");
	JButton register = new JButton("Register");




	frame(){
		setTitle("Log In Page");
		login.addActionListener(this);
		signup.addActionListener(this);
        
		panel.setLayout(new FlowLayout());
		panel.add(userlabel);
		panel.add(username);
		panel.add(pass);
		panel.add(password);
		panel.add(login);
		panel.add(signup);
		
		getContentPane().add(panel);
		
		setSize(300,200);
		setVisible(true);
		
	}
	
	//Tableの関数//
	void newframe(Object[][] Object){
		
	 String[] columns = new String[]{"Name"};
		
	 JTable table = new JTable(Object,columns);
		
	 setTitle("User List");
		
     this.add(new JScrollPane(table));
     this.pack();
     this.setVisible(true);
		
	}
    
	
	void signuppage(){
		
		setTitle("Sign Up Page");
		
		panel.setLayout(new FlowLayout());
		panel.add(userlabel);
		panel.add(username);
		panel.add(pass);
		panel.add(password);
		panel.add(fullname);
		panel.add(full);
		panel.add(dob);
		panel.add(birth);
		panel.add(emailadd);
		panel.add(email);
		
		panel.add(register);
		panel.setVisible(true);
		
		register.addActionListener(this);
		getContentPane().add(panel);
		
		setSize(300,330);
		setVisible(true);

	}
	
	public void actionPerformed(ActionEvent e) {
		//Getting a reference//
		final FirebaseDatabase database = FirebaseDatabase.getInstance();
		final DatabaseReference ref = database.getReference("user");
		
		if(e.getSource() == login){
			
			System.out.println("Trying to Log In");

			
			getData(ref);
		}
		if(e.getSource() == signup){
	            panel.removeAll();
	        	panel.setVisible(false);
               
	        	signuppage();
		}
		
		if(e.getSource() == register){
		  usertext = username.getText();
		  char[] passwordtext = password.getPassword();
		  
		  final String stringpass = String.valueOf(passwordtext);
          
		  String fullname = full.getText();
		  String year = birth.getText();
		  String emailadd = email.getText();
		  
		  registerUser(usertext, year, fullname,emailadd, stringpass);
		  getData(ref);
		   
		}
		
		else {
		}
		}

    //Stringを比較するための関数//
   public boolean containsIgnoreCase(String haystack,String needle) {
     if(needle.equals(""))
	 return true;
     if(haystack == null|| needle == null || haystack.equals("") )
	 return false;

    Pattern p = Pattern.compile(needle, Pattern.CASE_INSENSITIVE+Pattern.LITERAL);
    Matcher m = p.matcher(haystack);
   return m.find();
  }

  //データベースからデーターを取る関数//
  public void getData(DatabaseReference ref){
	  
	usertext = username.getText();
	char[] passwordtext = password.getPassword();
		
	System.out.println("入力したユーザーは"+usertext);
	System.out.println("入力したパスワードは"+Arrays.toString(passwordtext));
    
	final String stringpass = String.valueOf(passwordtext);
	
		//データをデータベースから取る//
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
     			        

     			        System.out.println("Key is"+key);
     			        
     			        Object value = user.toString();
     			        System.out.println(value);
     			        

     			        if (containsIgnoreCase((String) value,stringpass)){
     			        	System.out.println("Login Success");
     			            panel.removeAll();
     			        	panel.setVisible(false);
     			        	setTitle("Logging In..");
     			        	
     			        	Object[][] display = new Object[][] {{key},{key},{key}};
     			        	
     			        	newframe(display);
     			        	
     			        }
     			        else {
     			        	
     			        }
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
  
 //ユーザーをサインアップする関数//
 public void registerUser(String username, String dateofbirth, String fullname, String email, String password){
	 
     DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user");
     //試しでユーザーのデータ作った
     Map<String, Object> users = new HashMap<String, Object>();
     users.put(username, new User(dateofbirth,fullname,email,password));

     ref.updateChildren(users);

     System.out.println("New data saved");

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
	     
	      }

	
	
    public static void main( String[] args ) throws FileNotFoundException 
    {
        configFirebase();
        new frame();
    }
}
