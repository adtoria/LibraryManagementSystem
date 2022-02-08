import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class Administration {
	
	public static void adminServices() {
		
		Scanner input = new Scanner(System.in);
		Connection connect = ConnectDB.connectDB();
		
		String query;
		String bookId, bookName, authorName, studentId, studentName;
		
		while(true) {
			try {
		        System.out.println("\nEnter a number to proceed further:");
		        System.out.println("1 : Add a book");
		        System.out.println("2 : Delete a book");
		        System.out.println("3 : Edit book info");
		        System.out.println("4 : Add student");
		        System.out.println("5 : Issue book");
		        System.out.println("6 : Return book");
		        System.out.println("7 : View book issue history of a student");
		        System.out.println("8 : Exit admin services");
		        
		        int option = input.nextInt();
		        
		        if(option == 1) {
		        	 System.out.print("Enter Book ID: ");
		             bookId = input.next();
		
		             System.out.print("Enter Book Name: ");
		             bookName = input.next();
		             
		             System.out.print("Enter Author: ");
		             authorName = input.next();

		             query = "INSERT INTO bookRecord VALUES(?,?,?,?,?)";
		             PreparedStatement st = connect.prepareStatement(query);

		             st.setString(1, bookId);
		             st.setString(2, bookName);
		             st.setString(3, authorName);
		             st.setInt(4, 0);
		             st.setString(5, "NULL");
		             st.executeUpdate();
		             st.close();

		             System.out.println("Book with Book Id: " + bookId + ", Book Name: " + bookName + ", Author: " + authorName + " has been added.");		          
		        }
		        
		        else if(option == 2) {
		        	 System.out.print("Enter Book ID: ");
		             bookId = input.next();

		             query = "DELETE FROM bookRecord WHERE bookId=?";
		             PreparedStatement st = connect.prepareStatement(query);

		             st.setString(1, bookId);
		             st.executeUpdate();
		             st.close();

		             System.out.println("Book with Book Id: " + bookId + " has been deleted.");		          
		        }
		        
		        else if(option == 3) {
		        	System.out.println("Enter the Id of the Book to be updated:");
                    bookId = input.next();
                    
                    System.out.println("Enter New Book Name:");
                    bookName = input.next();
                    
                    System.out.println("Enter New Author Name:");
                    authorName = input.next();
                    
                    query = "UPDATE bookRecord SET bookName=?, authorName=? WHERE bookId=?";
                    PreparedStatement st = connect.prepareStatement(query);
     
                    st.setString(1, bookName);
                    st.setString(2, authorName);
                    st.setString(3, bookId);
                    st.executeUpdate();
                    st.close();
                    
                    System.out.println("Book details updated.");
		        }
		        
		        else if(option == 4) {
		        	String count;
                    count = "0";
                    
                    System.out.println("Enter Student Id:");
                    studentId = input.next();
                    System.out.println("Enter Student Name:");
                    studentName = input.next();
                    
                    query = "INSERT INTO studentRecord VALUES(?,?,?)";
                    PreparedStatement st = connect.prepareStatement(query);
                    
                    st.setString(1, studentId);
                    st.setString(2, studentName);
                    st.setString(3, count);
                    st.executeUpdate();
                    st.close();
                    
                    System.out.println("Student added successfully.");
		        }
		        
		        else if(option == 5) {
		        	System.out.println("Enter Student Id:");
                    studentId = input.next();
                    
                    System.out.println("Enter Book Id:");
                    bookId = input.next();
                    
                    query = "SELECT issueStatus FROM bookRecord WHERE bookId=?";
                    PreparedStatement st;
                    
                    st = connect.prepareStatement(query);
                    st.setString(1, bookId);
                    
                    ResultSet rs = st.executeQuery();
                    if(rs.next()){
                    	int status  = rs.getInt("issueStatus");
                        if(status == 1) {
                        	System.out.println("Book issued already.");
                            continue;
                        }
                        else {     
                        	query = "SELECT bookCount FROM studentRecord WHERE studentId=?";
                        	st = connect.prepareStatement(query);
                        	
                        	st.setString(1, studentId);
                        	rs = st.executeQuery();
                        	rs.next();
                        	
                        	int count = rs.getInt("bookCount");
                        	if(count>3) {
                        		System.out.println("Student " + studentId + " has 4 books issued.");
                        		break;
                           }                            
                           
                           String temp = "1";
                           
                           // Updating issueStatus of book.
                           query = "UPDATE bookRecord SET issueStatus='"+temp+"' WHERE bookId='"+bookId+"'";
                           st = connect.prepareStatement(query);
                           st.execute();
                           
                           // Updating issueBy parameter.
                           query = "UPDATE bookRecord SET issuedBy='"+studentId+"' where bookId='"+bookId+"'";
                           st = connect.prepareStatement(query);
                           st.execute();
                           
                           // Updating bookCount of student.
                           count += 1;
                           query = "UPDATE studentRecord SET bookCount='"+count+"' where studentId='"+studentId+"'";
                           st = connect.prepareStatement(query);
                           st.execute();
                           
                           // Inserting transaction into transactionList.
                           LocalDate currentDate = LocalDate.now();
                           query = "INSERT INTO transactionList (studentId,bookId,issueDate,fine) VALUES ('"+studentId+"','"+bookId+"','"+currentDate+"',0)";
                           st = connect.prepareStatement(query);
                           st.execute();
                           
                           System.out.println("Book Issued.");
                        }
                      }
                      else {
                          System.out.println("Wrong Book Id.");
                      }
                      rs.close();
                      st.close(); 
		        }
		        
		        else if(option == 6) {
		        	System.out.println("Enter Student Id:");
                    studentId = input.next();
                    
                    System.out.println("Enter Book Id:");
                    bookId = input.next();
                    
                    System.out.println("Enter Transaction Record Id:");
                    String transId = input.next();
                     
                    query = "SELECT issuedBy FROM bookRecord WHERE bookId=?";
                    PreparedStatement st = connect.prepareStatement(query);
                    st.setString(1, bookId);
                    
                    ResultSet rs = st.executeQuery();                
                    if(!rs.next()) {
                    	System.out.println("Invalid Book Id.");
                        continue;
                    }
                    String issuedBy = rs.getString("issuedBy");
                    	if(!issuedBy.equals(studentId)) {
                    		System.out.println("This book is not issued by "+studentId+".");
                        }
                        else{
                             query = "SELECT issueDate FROM transactionList WHERE recordId=?";
                             st = connect.prepareStatement(query);
                             st.setString(1, transId);
                             rs = st.executeQuery();
                               
                             int fine = 0;
                             String issueDate = rs.getString("issueDate");
                             
                             if(rs.next()) {
                            	 System.out.println("Enter today's date:");
                                 String dateString = input.next(); 
                                                                     
                                 if(dateString.compareTo(issueDate) > 15) {
                                      fine += 5*(dateString.compareTo(issueDate));
                                 }
                             }
                             else {
                                 System.out.println("Wrong Transaction Record Id.");
                                 break;
                             }
                             
                             // Update issueStatus & issuedBy in bookRecord table.
                             String temp = "0";
                             query = "UPDATE bookRecord SET issueStatus='"+temp+"',issuedBy=NULL WHERE bookId='"+bookId+"'";
                             st = connect.prepareStatement(query);
                             st.execute(); 
                        
                             // Update bookCount in studentRecord table.
                             query = "SELECT bookCount FROM studentRecord WHERE studentId=?";
                             st = connect.prepareStatement(query);
                             st.setString(1, studentId);
                             
                             rs = st.executeQuery();
                             rs.next();
                             
                             int count = rs.getInt("bookCount");
                             count = count-1;
                             
                             query = "UPDATE studentRecord SET bookCount='"+count+"' WHERE studentid='"+studentId+"'";
                             st = connect.prepareStatement(query);
                             st.execute();
                               
                             query = "UPDATE transactionList SET fine='"+fine+"' WHERE id='"+transId+"'";
                             st=connect.prepareStatement(query);
                             st.execute();
                               
                             System.out.println("Student Id: "+studentId+" returned Book Id: "+bookId+" with fine of "+fine+".");
                          }
                    rs.close();
                	st.close();
		        }
		        
		        else if(option == 7) {
		        	System.out.println("Enter Student Id:");
                    studentId = input.next();
                    
                    System.out.println("Books issued by Student " + studentId + " are:");
                    query ="SELECT bookId,issueDate, FROM transactionList WHERE studentId=?";
                    PreparedStatement st = connect.prepareStatement(query);                         
                    st.setString(1, studentId);
                    
                    ResultSet rs = st.executeQuery();
                    while(rs.next()) {
                    	String bId  = rs.getString("bookId");
                        String iDate = rs.getString("issueDate");
                       
                        // Displaying book details.
                        System.out.print("Book ID: " + bId);
                        System.out.println(", Issue Date: " + iDate);                 
                    }
                    rs.close();
                    st.close();
		        }
		        
		        else if(option == 8) {
		        	System.out.println("Exiting admin menu.");
                    break;
		        }
		        
		        else {
		        	System.out.println("Wrong input.");
		        }
			}
			catch(Exception e) {
                System.out.println(e);
            }
		}
		input.close();
	}
}
