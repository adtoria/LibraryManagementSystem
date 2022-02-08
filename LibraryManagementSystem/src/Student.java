import java.sql.*;
import java.util.Scanner;

public class Student {
	
	public static void studentServices() {
		
		Scanner input = new Scanner(System.in);
		
        Connection connect = ConnectDB.connectDB();
        ResultSet rs;
        PreparedStatement st;

        String query, studentId;
        
        System.out.println("Enter Student Id:");
        studentId = input.next();
        
        try{
            query = "SELECT studentId FROM students WHERE studentId=?";
            st = connect.prepareStatement(query);
            st.setString(1, studentId);
            rs = st.executeQuery();
            
            if(rs.next()) {
                System.out.println("Student Menu:");
            }
            else {
                System.out.println("Wrong Student Id.");
                input.close();
                return;
            }  
            rs.close();
            st.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }
        
        while(true){
        	try{
        		System.out.println("\nEnter a number to proceed further:");
                System.out.println("1 : Search Book");
                System.out.println("2 : Check history of books issued");
                System.out.println("3 : Check books currently issued");
                System.out.println("4 : Exit");
                
                int option = input.nextInt();
                
                if(option == 1) {
                	System.out.println("Enter Book Id:");
                    String bookId= input.next();
                    
                    query = "SELECT bookName,authorName,issueStatus FROM bookRecord WHERE bookId=?";
                    st = connect.prepareStatement(query);
                    st.setString(1, bookId);
                    rs = st.executeQuery();
                    
                    if(rs.next()) {
                    	int issueStatus  = rs.getInt("issueStatus");
                        String authorName = rs.getString("authorName");
                        String bookName = rs.getString("bookName");

                        System.out.print("Book ID: " + bookId);
                        System.out.print(", Book Name: " + bookName);
                        System.out.print(", Book author: " + authorName);
                        if (issueStatus == 1) {
                        	System.out.println(" is already issued!\n");
                        }
                        else {
                        	System.out.println(" is available!\n");                                                                                                                                                      
                        }     
                    }
                    else {
                    	System.out.println("Invalid Book Id.");
                    }        
                }	
                
                else if (option == 2){
                	System.out.println("Books issued by Student: "+studentId+" are:");
                	
                    query = "Select book_id,issue_date from issuelist where student_id= ?";
                    st = connect.prepareStatement(query);
                    st.setString(1, studentId);
                    rs = st.executeQuery();
                        
                    while(rs.next()){
                           //Retrieve by column name
                           
                           String bkid  = rs.getString("book_id");
                           String issuedate = rs.getString("issue_date");
                          
                           //Display values
                           System.out.print("BookID: " + bkid);
                           System.out.println(", Issue Date " + issuedate);
                      
                       }
                       
                   }
                else if(option == 3) {
                	query = "SELECT bookId,bookName,authorName FROM bookRecord WHERE issuedBy=? AND issueStatus=?";
                    
                	st = connect.prepareStatement(query);
                    st.setString(1, studentId);
                    st.setString(2, "1");
                    rs=st.executeQuery();
                    
                    System.out.println("Books issued by Student: "+studentId+"are: ");
                    
                    while(rs.next()) {
                    	String bookId = rs.getString("bookId");
                        String bookName = rs.getString("bookName");
                        String authorName = rs.getString("author");
                        
                        System.out.print("Book Id: " + bookId);
                        System.out.print(", Book Name: " + bookName);
                        System.out.println(", Book Author: " + authorName);
                    }
                }
                else if(option == 4) {
                	System.out.println("Exiting student menu.");
                	break;
                }
                else {
                	System.out.println("Wrong Input.");
                	continue;
                }                
        	}
        	catch(Exception e) {
        		System.out.println(e);
        	}
        }
        input.close();
	}
}