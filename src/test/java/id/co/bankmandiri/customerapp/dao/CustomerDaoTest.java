package id.co.bankmandiri.customerapp.dao;

import id.co.bankmandiri.customerapp.domain.Customer;
import id.co.bankmandiri.customerapp.domain.CustomerException;
import id.co.bankmandiri.customerapp.util.DbUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CustomerDaoTest {
    private static CustomerDao dao;

    @BeforeEach
    void setUp() {
        Connection connection = DbUtil.getConnection();
        dao = new CustomerDao(connection);
    }

//    @AfterEach
//    void tearDown() {
//    }

    @Test
    void displayAllCustomers() {
    }

    @Test
    void addCustomer() {
        Customer customer = new Customer("Marco", "Polo", LocalDate.of(1979,6,5));
        try {
            dao.addCustomer(customer);
            Customer result = dao.findCustomerById(3);
            Assertions.assertEquals("Marco", result.getFirstName());
        } catch (CustomerException e){
            e.printStackTrace();
        }
    }

    @Test
    void editCustomer() {

        try {
            Customer customer = dao.findCustomerById(3);
            customer.setFirstName("Tom");
            customer.setLastName("Hanks");
            customer.setDateofBirth(LocalDate.now());
            dao.editCustomer(customer);
            Customer result = dao.findCustomerById(3);
            Assertions.assertEquals("Tom", result.getFirstName());
            Assertions.assertEquals("Hanks", result.getLastName());
            Assertions.assertEquals(LocalDate.now(), result.getDateofBirth());
        } catch (CustomerException e){
            e.printStackTrace();
        }
    }

    @Test
    void findCustomerById() {
            //ngetest kalo disini ada apakah exception berhasil atau tidak
            Exception e = Assertions.assertThrows(
                    CustomerException.class, ()-> dao.findCustomerById(100)
            );
            Assertions.assertEquals("customer not found", e.getMessage());
    }

    @Test
    void deleteCustomer() {
        try {
            dao.deleteCustomer(1);
            Exception e = Assertions.assertThrows(
                    CustomerException.class, ()-> dao.findCustomerById(1)
            );
            Assertions.assertEquals("customer not found", e.getMessage());
        } catch (CustomerException e){
            e.printStackTrace();
        }
    }

    @Test
    void addCustomerWithID() {
        Customer customer = new Customer(4,"Marco", "Polo", LocalDate.of(1979,6,5));
        try {
            dao.addCustomerWithID(customer);
            Customer result = dao.findCustomerById(4);
            Assertions.assertEquals("Marco", result.getFirstName());
        } catch (CustomerException e){
            e.printStackTrace();
        }
    }
}