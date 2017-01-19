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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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

//ユーザーに送るメッセージのclass宣言//
class message {
	public String sender;
	public String recipient;
	public String title;
	public String message;
	
	public message(String sender,String recipient, String title, String message){
		this.sender = sender;
		this.recipient = recipient;
		this.title = title;
		this.message = message;
	}
}


class frame extends JFrame implements ActionListener,MouseListener{

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
    JButton chat = new JButton("Chat");

   //連絡ページ用//
	JLabel name = new JLabel("To:");
	JLabel title = new JLabel("Title:");
	JLabel message = new JLabel("Message:");
	
	JTextField namefield = new JTextField(20);
	JTextField titlefield = new JTextField(20);
	
	JTextArea textArea = new JTextArea(10, 50);
	JButton send = new JButton("Send");

    JFrame frame = new JFrame("Chat Window" );

    //Login Pageのframe//
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
	
	//signupのページを作る関数//
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

	//Tableを作る関数//
	void newframe(Map map){
     
	 JTable table = new JTable(toTableModel((Map)map)){
		 public TableCellRenderer getCellRenderer(int row,int column){
			 return new ButtonRenderer();
		 }
	 };
	 table.setRowHeight(32);
     table.addMouseListener((MouseListener) this);
	 
	 setTitle("User List");
	 
     this.add(new JScrollPane(table));
     this.pack();
     this.setVisible(true);
		
	}
    
	//連絡ページを作る関数//
	private void chatwindow() {
        
	    frame.setSize(600,400);
	    frame.setLocationRelativeTo( null );
	    frame.setVisible( true );   
	   
	    JPanel panel2 = new JPanel();
	    
		panel2.setLayout(new FlowLayout());
		panel2.add(name);
		panel2.add(namefield);
		panel2.add(title);
		panel2.add(titlefield);
		panel2.add(message);
		panel2.add(textArea);
		panel2.add(send);
		
	
		send.addActionListener(this);
		frame.getContentPane().add(panel2);
		
	}


	//このクラスのaction listener//
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
		
		if(e.getSource() == send){
			System.out.println("Message sent");
			String name = namefield.getText();
			String title = titlefield.getText();
			String messagesend = textArea.getText();
			
			registerMessage(usertext,name,title,messagesend);
			JOptionPane.showMessageDialog(frame, "Message sent to database. We will soon forward it to the recipient","Sent",JOptionPane.PLAIN_MESSAGE);
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
		
	final String stringpass = String.valueOf(passwordtext);
	
		//データをデータベースから取る//
     ref.addValueEventListener(
     		new ValueEventListener(){
     		  public void onDataChange(DataSnapshot dataSnapshot){
     				Object user = dataSnapshot.getValue();
     				Iterator it = ((Map) user).entrySet().iterator();
     				
     			    int i = 0;
     			    
     			    while (it.hasNext()) {
     			        Map.Entry pair = (Map.Entry)it.next();
     			        System.out.println(pair.getKey() + " = " + pair.getValue());
     			        String key = (String) pair.getKey();
     			        
     			        System.out.println("Key is"+key);
     			        
     			        Object value = user.toString();
     			        System.out.println(value);
     			        
                        //security度が低いパスワードがデータベースにあるかどうかのチェック方法//
     			        //時間あれば、もっといい方法見つけます//
     			        if (containsIgnoreCase((String) value,stringpass) && i==0){
     			        	System.out.println("Login Success");
     			            panel.removeAll();
     			        	panel.setVisible(false);
     			        	setTitle("Logging In..");
     			        	
     			        	Object[][] display = new Object[][] {{key},{key},{key}};
     			        	
     			        	newframe((Map)user);
     			        	
     			        	//if I do not do this, the table would keep on changing//
     			        	i=1;
     			        	
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
 
//メッセージをデータベースに登録する//
public void registerMessage(String sender,String recipient, String title, String message){
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("message");
    //試しでユーザーのデータ作った
    Map<String, Object> messages = new HashMap<String, Object>();
    messages.put(recipient,new message(sender,recipient,title,message));

    ref.updateChildren(messages);

    System.out.println("New message saved");

}
 
 public static TableModel toTableModel(Map map) {
     DefaultTableModel model = new DefaultTableModel (
   new Object[] { "User" }, 0
  );
  for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
   Map.Entry entry = (Map.Entry)it.next();
   System.out.println("Entry is"+entry);
   model.addRow(new Object[] { entry.getKey()});
  }
  return model;
 }

public void mouseClicked(MouseEvent e) {
  System.out.println("Table Clicked");	
  chatwindow();
}

public void mousePressed(MouseEvent e) {
	// TODO 自動生成されたメソッド・スタブ
	
}

public void mouseReleased(MouseEvent e) {
	// TODO 自動生成されたメソッド・スタブ
	
}

public void mouseEntered(MouseEvent e) {
	// TODO 自動生成されたメソッド・スタブ
	
}

public void mouseExited(MouseEvent e) {
	// TODO 自動生成されたメソッド・スタブ
	
 }
}

//tableのbuttonのclass//
class ButtonRenderer extends JPanel implements TableCellRenderer,ActionListener {
	
	JButton chat = new JButton("Available(Click To Contact)");
	
  public Component getTableCellRendererComponent(
                      final JTable table, Object value,
                      boolean isSelected, boolean hasFocus,
                      int row, int column) {
  	JLabel label = new JLabel(value.toString());
      this.add(label);
      this.add(chat);
      chat.addActionListener(this);
      table.setEnabled(false);
      return this;
  }

	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == chat){
			System.out.println("Chat Button Clicked!");
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
	     
	      }

	
	
    public static void main( String[] args ) throws FileNotFoundException 
    {
        configFirebase();
        new frame();
    }
    
}
	
