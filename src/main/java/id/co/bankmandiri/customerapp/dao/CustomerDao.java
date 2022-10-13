package id.co.bankmandiri.customerapp.dao;

import id.co.bankmandiri.customerapp.domain.Customer;
import id.co.bankmandiri.customerapp.domain.CustomerException;
import id.co.bankmandiri.customerapp.service.CustomerService;
import id.co.bankmandiri.customerapp.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao implements CustomerService {

//    private final String dburl = "jdbc:mysql://localhost:3306/belajar_java";
//    private final String username = "root";
//    private final String password = "";
    private Connection connection;
    private final String query = "SELECT * FROM customers";
    private final String queryFindCustomerById = "SELECT * FROM customers " +
                                                 "WHERE customerid = ?";
    private final String editCustomer = "UPDATE customers " +
                                        "SET firstname = ?, lastname = ?, dateofbirth = ? " +
                                        "WHERE customerid =?";
    private final String queryDeleteCustomer = "DELETE FROM customers" +
                                               " WHERE customerid = ?";
    private final String queryInsertCustomer = "INSERT INTO customers (firstname, lastname, dateofbirth) " +
            "VALUES (?, ?, ?)";

    private final String queryInsertCustomerWithID = "INSERT INTO customers (customerid, firstname, lastname, dateofbirth) " +
            "VALUES (?, ?, ?, ?)";

    public CustomerDao(){
        connection = DbUtil.getConnection();

    }
    public CustomerDao (Connection connection){
        this.connection=connection;
    }

    @Override
    public List<Customer> displayAllCustomers() {
       List<Customer>customers = new ArrayList<>();
        try (
            //    Connection connection = DriverManager.getConnection(dburl, username, password);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
        ) {
            while (resultSet.next()) {
                customers.add(new Customer(
                        resultSet.getInt("customerid"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getDate("dateofbirth").toLocalDate())
                );
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return customers;
    }

    @Override
    public void addCustomer(Customer customer) throws CustomerException {
        try (
                PreparedStatement ps = connection.prepareStatement(queryInsertCustomer);
                ){
            ps.setString(1, customer.getFirstName());
            ps.setString(2,customer.getLastName());
            ps.setDate(3,Date.valueOf(customer.getDateofBirth()));
            ps.executeUpdate();

        } catch (SQLException sqle) {
            throw new CustomerException("Add customer failed");
        }
    }

    @Override
    public void addCustomerWithID(Customer customer) throws CustomerException {
        try (
                PreparedStatement ps = connection.prepareStatement(queryInsertCustomerWithID);
        ){
            ps.setInt(1,customer.getCustomerId());
            ps.setString(2, customer.getFirstName());
            ps.setString(3,customer.getLastName());
            ps.setDate(4,Date.valueOf(customer.getDateofBirth()));
            ps.executeUpdate();

        } catch (SQLException sqle) {
            throw new CustomerException("Add customer failed");
        }
    }

    @Override
    public void editCustomer(Customer customer) throws CustomerException {
        try (
                PreparedStatement ps = connection.prepareStatement(editCustomer);
        ){
            ps.setString(1, customer.getFirstName());
            ps.setString(2,customer.getLastName());
            ps.setDate(3,Date.valueOf(customer.getDateofBirth()));
            ps.setInt(4,customer.getCustomerId());
            ps.executeUpdate();

        } catch (SQLException sqle) {
            throw new CustomerException("edit customer failed");
        }
    }

    @Override
    public Customer findCustomerById(int id) throws CustomerException {
        Customer customer = null;
        try (
                PreparedStatement ps = connection.prepareStatement(queryFindCustomerById);
        ) {
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                customer = new Customer(
                        resultSet.getInt("customerid"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getDate("dateofbirth").toLocalDate())
                ;
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            throw new CustomerException("Find customer by ID failed");
        }
        if (customer != null) {
            return customer;
        } else {
            throw new CustomerException("customer not found");
        }
    }

    @Override
    public void deleteCustomer(int id) throws CustomerException {
        try (
                PreparedStatement ps = connection.prepareStatement(queryDeleteCustomer);
        ){
            ps.setInt(1,id);
            ps.executeUpdate();

        } catch (SQLException sqle) {
            throw new CustomerException("delete customer failed");
        }
    }


}
