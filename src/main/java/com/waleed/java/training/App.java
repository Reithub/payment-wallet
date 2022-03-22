package com.waleed.java.training;

import java.sql.SQLException;
import java.util.Scanner;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.waleed.java.training.JdbcDao.CustomerMapper;

//Payment wallet
public class App{
	
	public static void main(String[] args) {
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("config.xml");    	
    	JdbcDao dao = context.getBean("jdbcDao", JdbcDao.class);
    	//dao.setDataSource();
    	
    	//UsingNamedParameterJdbcTemplate namedDao = context.getBean("namedParamDao", UsingNamedParameterJdbcTemplate.class);

    	
    	Scanner sc = new Scanner(System.in);
    	int choice = 0;
    	
    	while(choice!=7) {
    		System.out.println("------------------Banking options-----------------");
			  System.out.println("1.Create Account\n2.Display balance\n3.Fund transfer"
			  		+ "\n4.Deposit\n5.Withdrawl\n6.Display all customers\n7.Exit\n"); 
			  System.out.print("Enter choice: "); choice = sc.nextInt();
    		
    		switch (choice) {
    		
        	case 1:
        		System.out.print("Enter name: "); 
        		String name = sc.next();
        		System.out.print("Enter mobile: "); 
        		String mobNo = sc.next();
        		System.out.print("Enter balance: "); 
        		Integer balance = sc.nextInt();
        		dao.createAccount(name, mobNo, balance); 
        		break;
        	
        	case 2:
        		System.out.print("Enter name: ");
        		String name1 = sc.next();
        		System.out.println("Balance is Rs." + dao.getBalanceByName(name1));
        		break;
        		
        	case 3:
        		System.out.print("Enter sender's name: "); 
        		String sender = sc.next();
        		System.out.print("Enter receiver's name: "); 
        		String receiver = sc.next();
        		System.out.print(sender+" enter amount to transfer to " + receiver + ": ");
        		Integer tamount = sc.nextInt(); 
        		dao.fundTransfer(sender, receiver, tamount);
        		break;
        		
        	case 4:
        		System.out.print("Enter name: "); 
        		String dname = sc.next(); 
        		System.out.print(dname+" enter amount to deposit: ");
        		Integer damount = sc.nextInt(); 
        		dao.deposit(dname, damount);
        		break;
        		
        	case 5:
        		System.out.print("Enter name: "); 
        		String wname = sc.next(); 
        		System.out.print(wname+" enter amount to deposit: ");
        		Integer wamount = sc.nextInt(); 
        		dao.deposit(wname, wamount);
        		break;
        		
        	case 6:
        		System.out.println("\n-------------------Customer Details-----------------------");
  			  	for(Customer theCustomer : dao.display())
  			  		System.out.println(theCustomer);
        		break;
        		
    		default:
    			if(choice!=7)
    				System.out.println("Invalid choice :(");
    			break;
    		}
    		
    	}
    	
    	System.out.println("Terminated...");
	}
}