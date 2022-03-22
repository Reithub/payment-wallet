package com.waleed.java.training;

import java.sql.DriverManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import java.sql.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

//Integrated JdbcTemplate
@Repository("jdbcDao")
public class JdbcDao{
	
	JdbcTemplate jdbcTemplate;
	DataSource dataSource;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void createAccount(String name, String mobNo, Integer balance) {
		jdbcTemplate.update("insert into wallet values (?, ?, ?)", new Object[] {name, mobNo, balance});
	}

	public Integer getBalanceByName(String name) {
		return jdbcTemplate.queryForObject("select balance from wallet where name = ?", Integer.class, new Object[] {name});
	}

	public void fundTransfer(String sender, String receiver, Integer tamount) {

		int isamount = getBalanceByName(sender);
		int iramount = getBalanceByName(receiver);
		if(tamount <= isamount) {
			int fsamount = isamount - tamount;
			int framount = iramount + tamount;
			jdbcTemplate.update("update wallet set balance = ? where name = ?", new Object[] {fsamount, sender});
			System.out.println("\n++++++++++Transaction details++++++++++");
			System.out.println("Rs." + tamount + " transferred to " + receiver);
			System.out.println(sender + " your balance is Rs." + fsamount);
	
		
			jdbcTemplate.update("update wallet set balance = ? where name = ?", new Object[] {framount, receiver});
			System.out.println( receiver + " received Rs." + tamount);
			System.out.println("+++++++++++++++++++++++++++++++++++++++\n");
		}
		else {
			System.out.println("Insufficient fund to transfer\nBalance is Rs." + isamount);
		}
	}
	
	public void deposit(String dname, Integer damount) {
		int famount = damount + getBalanceByName(dname);
		jdbcTemplate.update("UPDATE wallet SET balance = ? WHERE name = ?", new Object[] {famount, dname});
		System.out.println("Rs." + damount + " deposited in " + dname);
		System.out.println("Balance is Rs." + famount);
	}
	
	public void withdrawl(String wname, Integer wamount) {
		int iamount = getBalanceByName(wname);
		int famount = iamount-wamount;
		if(famount>=0) {
			jdbcTemplate.update("UPDATE wallet SET balance = ? WHERE name = ?", new Object[] {famount, wname});
		}
		else {
			System.out.println("Insufficient balance of Rs." + iamount);
			System.out.println("Transaction declined...");
		}
	}

	List<Customer> display() {
		
		return jdbcTemplate.query("select *from wallet", new CustomerMapper());
		
	}
	
	class CustomerMapper implements RowMapper<Customer>{
		
		public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			Customer theCustomer = new Customer(
					rs.getString("name"),
					rs.getString("mobNo"),
					rs.getInt("balance")
					);
			return theCustomer;
		}
		
	}

	
}

//Conventional way
/*
@Repository("jdbcDao")
public class JdbcDao {

	Connection dbCon;

	void connectToDb() {
		
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			dbCon = DriverManager.getConnection("jdbc:mysql://localhost:3306/march9_ibm", "root", "");
			System.out.println("Connected to Db...");
			
		} catch (ClassNotFoundException e) {
			System.out.println("Issues while connecting to Db: "+e.getMessage());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	void createAccount(String name, String mobNo, Integer balance) throws SQLException {
		
		String qry = "INSERT INTO `wallet`(`name`, `mobNo`, `balance`) VALUES (?,?,?)";
		PreparedStatement pstmt = dbCon.prepareStatement(qry);
		pstmt.setString(1, name);
		pstmt.setString(2, mobNo);
		pstmt.setInt(3, balance);
		if(pstmt.executeUpdate() > 0)
			System.out.println("New account created...");
		
	}
	
	Integer getBalanceByName(String name) throws SQLException {
		
		int balance = 0;
		String qry = "select balance from wallet where name = ?";
		PreparedStatement pstmt = dbCon.prepareStatement(qry);
		pstmt.setString(1, name);
		ResultSet resultSet = pstmt.executeQuery();
		while(resultSet.next()) {
			balance = Integer.parseInt(resultSet.getString("balance"));
		}
		//System.out.println("Balance is Rs." + balance);
		return balance;
	
	}

	public void fundTransfer(String sender, String receiver, Integer tamount) throws SQLException {
		int isamount = getBalanceByName(sender);
		int iramount = getBalanceByName(receiver);
		if(tamount <= isamount) {
			int fsamount = isamount - tamount;
			int framount = iramount + tamount;
			
			String qry1 = "UPDATE wallet SET balance = ? WHERE name = ?";
			PreparedStatement pstmt = dbCon.prepareStatement(qry1);
			pstmt.setInt(1, fsamount);
			pstmt.setString(2, sender);
			if(pstmt.executeUpdate() > 0) {
				System.out.println("\n++++++++++Transaction details++++++++++");
				System.out.println("Rs." + tamount + " transferred to " + receiver);
				System.out.println(sender + " your balance is Rs." + fsamount);
			}
			
			String qry2 = "UPDATE wallet SET balance = ? WHERE name = ?";
			PreparedStatement pstmt1 = dbCon.prepareStatement(qry2);
			pstmt1.setInt(1, framount);
			pstmt1.setString(2, receiver);
			if(pstmt1.executeUpdate() > 0) {
				System.out.println( receiver + " received Rs." + tamount);
				System.out.println("+++++++++++++++++++++++++++++++++++++++\n");
			}
		}
		else {
			System.out.println("Insufficient fund to transfer\nBalance is Rs." + isamount);
		}
	}

	public void deposit(String dname, Integer damount) throws SQLException {
		
		int famount = damount + getBalanceByName(dname);
		String qry = "UPDATE wallet SET balance = ? WHERE name = ?";
		PreparedStatement pstmt = dbCon.prepareStatement(qry);
		pstmt.setInt(1, famount);
		pstmt.setString(2, dname);
		if(pstmt.executeUpdate() > 0)
			System.out.println("Rs." + damount + " deposited in " + dname);
			System.out.println("Balance is Rs." + famount);
		
	}

	public void withdraw(String wname, Integer wamount) throws SQLException {
		
		int iamount = getBalanceByName(wname);
		int famount = iamount-wamount;
		if(famount>=0) {
			String qry = "UPDATE wallet SET balance = ? WHERE name = ?";
			PreparedStatement pstmt = dbCon.prepareStatement(qry);
			pstmt.setInt(1, famount);
			pstmt.setString(2, wname);
			if(pstmt.executeUpdate() > 0) {
				System.out.println("Rs." + wamount + " withdrawn from " + wname);
				System.out.println("Balance is Rs." + famount);
			}
		}
		else {
			System.out.println("Insufficient balance of Rs." + iamount);
			System.out.println("Transaction declined...");
		}
	}

	public void display() throws SQLException {
		String qry = "select * from wallet";
		Statement theStmt = dbCon.createStatement();
		ResultSet resultSet = theStmt.executeQuery(qry);
		while(resultSet.next()) {
			System.out.print(resultSet.getString("name"));
			System.out.print("        |       " + resultSet.getString("mobNo"));
			System.out.println("      |        Rs." + resultSet.getInt("balance"));
			
		}
	}
}
*/