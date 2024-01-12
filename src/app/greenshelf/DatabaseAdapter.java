package app.greenshelf;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DatabaseAdapter {
    private String url;
    private Connection connection;
    
    public void generateSqlDatabase() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS javafxdb;");
            statement.executeUpdate("USE javafxdb;");
            String query = "CREATE TABLE IF NOT EXISTS user (id INT PRIMARY KEY AUTO_INCREMENT, " +
                                                                        "username VARCHAR(50), " +
                                                                        "email VARCHAR(50), " +
                                                                        "phone VARCHAR(50), " +
                                                                        "address VARCHAR(50), " +
                                                                        "name VARCHAR(50)," + 
                                                                        "surname VARCHAR(50), " +
                                                                        "password VARCHAR(50), " +
                                                                        "profilePicture LONGBLOB, " +
                                                                        "userType VARCHAR(50), " +
                                                                        "UNIQUE(username), " +
                                                                        "UNIQUE(email), " +
                                                                        "UNIQUE(phone)" + 
                                                                        ");";
            statement.executeUpdate(query);

            String queryProducts = "CREATE TABLE IF NOT EXISTS products (id INT PRIMARY KEY AUTO_INCREMENT, " +
                                                                        "name VARCHAR(50), " +
                                                                        "stock DOUBLE, " +
                                                                        "image LONGBLOB, " +
                                                                        "price DOUBLE, " +
                                                                        "threshold DOUBLE, " +
                                                                        "type VARCHAR(50), " +
                                                                        "unit BOOLEAN, " + 
                                                                        "isRemoved BOOLEAN " +
                                                                        ");";
            statement.executeUpdate(queryProducts);

            String queryOrders = "CREATE TABLE IF NOT EXISTS orders (id INT PRIMARY KEY AUTO_INCREMENT, " +
                                                                        "orderid VARCHAR(16)," +
                                                                        "userid VARCHAR(16), " +
                                                                        "productid INT, " +
                                                                        "amount DOUBLE, " +
                                                                        "date VARCHAR(50), " +
                                                                        "price DOUBLE, " +
                                                                        "status VARCHAR(50), " + 
                                                                        "deliveryDate VARCHAR(50), " +
                                                                        "carrierUsername VARCHAR(50) " +
                                                                        ");";
            statement.executeUpdate(queryOrders);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void registerUserSql(Customer customer) {
        try{
            String url = "jdbc:mysql://localhost:3306/javafxdb";
            zorunlu user = new zorunlu();
            Connection connection = DriverManager.getConnection(url, user.name, user.pass);
            String query = "INSERT INTO user (username, email, phone, name, surname, password, profilePicture, userType, address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, customer.getUsername());
            preparedStatement.setString(2, customer.getEmail());
            preparedStatement.setString(3, customer.getPhone());
            preparedStatement.setString(4, customer.getName());
            preparedStatement.setString(5, customer.getSurname());
            preparedStatement.setString(6, customer.getPassword());
            preparedStatement.setBytes(7, customer.getProfilePicture().getBytes());
            preparedStatement.setString(8, customer.getUserType());
            preparedStatement.setString(9, customer.getAddress());
            preparedStatement.executeUpdate();
            System.out.println("Customer added successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

    public void registerUserSql(Carrier carrier) {
        try{
            String url = "jdbc:mysql://localhost:3306/javafxdb";
            zorunlu user = new zorunlu();
            Connection connection = DriverManager.getConnection(url, user.name, user.pass);
            String query = "INSERT INTO user (username, email, phone, name, surname, password, profilePicture, userType, address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, carrier.getUsername());
            preparedStatement.setString(2, carrier.getEmail());
            preparedStatement.setString(3, carrier.getPhone());
            preparedStatement.setString(4, carrier.getName());
            preparedStatement.setString(5, carrier.getSurname());
            preparedStatement.setString(6, carrier.getPassword());
            preparedStatement.setBytes(7, carrier.getProfilePicture().getBytes());
            preparedStatement.setString(8, carrier.getUserType());
            preparedStatement.setString(9, carrier.getAddress());
            preparedStatement.executeUpdate();
            System.out.println("Carrier added successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getUserIDFromUsername(String username){
        int id = 0;
        try{
            String url = "jdbc:mysql://localhost:3306/javafxdb";
            zorunlu user = new zorunlu();
            Connection connection = DriverManager.getConnection(url, user.name, user.pass);
            String query = "SELECT id FROM user WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                id = resultSet.getInt("id");
            }
        } catch (SQLException e) {
        }
        return id;
    }


    public List<String> loginUserSql(String username) {
        try {
            if (connection == null) {
                throw new SQLException("Connection not established.");
            }
    
            try (PreparedStatement useStatement = connection.prepareStatement("USE javafxdb;")) {
                useStatement.execute();
            }
    
            String queryUsernameExists = "SELECT COUNT(*) FROM user WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryUsernameExists)) {
                preparedStatement.setString(1, username);
                ResultSet resultSet2 = preparedStatement.executeQuery();
    
                if (resultSet2.next()) {
                    if (resultSet2.getInt(1) == 0) {
                        System.out.println("Username does not exist!");
                        return null;
                    }
                }
            }
    
            String query = "SELECT * FROM user WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    List<String> list = List.of(resultSet.getString("username"), resultSet.getString("email"), resultSet.getString("phone"), resultSet.getString("address"), resultSet.getString("name"), resultSet.getString("surname"), resultSet.getString("password"), resultSet.getString("profilePicture"), resultSet.getString("userType"));
                    
                    return list;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }




    /* REGISTER CONTROL PAGE FUNCTIONS */

    public Boolean checkUsernameSql(String username) {
        
        try {
            if (connection == null) {
                throw new SQLException("Connection not established.");
            }
    
            try (PreparedStatement useStatement = connection.prepareStatement("USE javafxdb;")) {
                useStatement.execute();
            }
    
            String queryUsernameExists = "SELECT COUNT(*) FROM user WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryUsernameExists)) {
                preparedStatement.setString(1, username);
                ResultSet resultSet2 = preparedStatement.executeQuery();
    
                if (resultSet2.next()) {
                    if (resultSet2.getInt(1) == 0) {
                        return false;
                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public Boolean checkEmailSql(String email) {
        
        try {
            if (connection == null) {
                throw new SQLException("Connection not established.");
            }
    
            try (PreparedStatement useStatement = connection.prepareStatement("USE javafxdb;")) {
                useStatement.execute();
            }
    
            String queryUsernameExists = "SELECT COUNT(*) FROM user WHERE email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryUsernameExists)) {
                preparedStatement.setString(1, email);
                ResultSet resultSet2 = preparedStatement.executeQuery();
    
                if (resultSet2.next()) {
                    if (resultSet2.getInt(1) == 0) {
                        /*email doesn't exists */
                        return false;
                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public Boolean checkPhoneSql(String username,String phone)
    {
        try{
            if (connection == null) {
                throw new SQLException("Connection not established.");
            }

            try (PreparedStatement useStatement = connection.prepareStatement("USE javafxdb;")) {
                useStatement.execute();
            }

            String queryUsernameExists = "SELECT COUNT(*) FROM user WHERE username != ? and phone = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryUsernameExists)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, phone);
                ResultSet resultSet2 = preparedStatement.executeQuery();

                if (resultSet2.next()) {
                    if (resultSet2.getInt(1) == 0) {
                        /*phone doesn't exists */
                        return false;
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public Boolean checkEmailSql(String username,String email) {
        
        try {
            if (connection == null) {
                throw new SQLException("Connection not established.");
            }
    
            try (PreparedStatement useStatement = connection.prepareStatement("USE javafxdb;")) {
                useStatement.execute();
            }
    
            String queryUsernameExists = "SELECT COUNT(*) FROM user WHERE username != ? and email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryUsernameExists)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, email);
                ResultSet resultSet2 = preparedStatement.executeQuery();
    
                if (resultSet2.next()) {
                    if (resultSet2.getInt(1) == 0) {
                        /*email doesn't exists */
                        return false;
                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public Boolean checkPhoneSql(String phone) {
        
        try {
            if (connection == null) {
                throw new SQLException("Connection not established.");
            }
    
            try (PreparedStatement useStatement = connection.prepareStatement("USE javafxdb;")) {
                useStatement.execute();
            }
    
            String queryUsernameExists = "SELECT COUNT(*) FROM user WHERE phone = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryUsernameExists)) {
                preparedStatement.setString(1, phone);
                ResultSet resultSet2 = preparedStatement.executeQuery();
    
                if (resultSet2.next()) {
                    if (resultSet2.getInt(1) == 0) {
                        /*phone doesn't exists */
                        return false;
                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public boolean checkProductName(String productName){
        try {
            if (connection == null) {
                throw new SQLException("Connection not established.");
            }
    
            try (PreparedStatement useStatement = connection.prepareStatement("USE javafxdb;")) {
                useStatement.execute();
            }
    
            String queryUsernameExists = "SELECT COUNT(*) FROM products WHERE name = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryUsernameExists)) {
                preparedStatement.setString(1, productName);
                ResultSet resultSet2 = preparedStatement.executeQuery();
    
                if (resultSet2.next()) {
                    if (resultSet2.getInt(1) == 0) {
                        /*product doesn't exists */
                        return false;
                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public DatabaseAdapter() {
        url = "jdbc:mysql://localhost:3306";
        zorunlu user = new zorunlu();

        try {
            connection = DriverManager.getConnection(url, user.name, user.pass);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    

    public void updateInfo(String password, String email, String address, String phone, String profilePhoto, String username)
    {
        String url = "jdbc:mysql://localhost:3306/javafxdb";
        zorunlu user = new zorunlu();
        
        try (Connection connection = DriverManager.getConnection(url, user.name, user.pass)) 
        {
            String updateQuery = "UPDATE user SET password = ?, email = ?, address = ?, phone = ?, profilePicture = ? WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) 
        {
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, address);
            preparedStatement.setString(4, phone);
            preparedStatement.setBytes(5, profilePhoto.getBytes()); 
            preparedStatement.setString(6, username);
            preparedStatement.executeUpdate();
        }
            System.out.println("Data updated successfully!");
        } catch (SQLException e) 
        {
            e.printStackTrace();
        }

    }

    public void addProductToDb(Product product){
        try{
            String url = "jdbc:mysql://localhost:3306/javafxdb";
            zorunlu user = new zorunlu();
            Connection connection = DriverManager.getConnection(url, user.name, user.pass);
            String query = "INSERT INTO products (name, stock, image, price, threshold, type, unit,isRemoved) VALUES (?, ?, ?, ?, ?, ?, ?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getStock());
            preparedStatement.setBytes(3, product.getImage().getBytes());
            preparedStatement.setDouble(4, product.getPrice());
            preparedStatement.setDouble(5, product.getThreshold());
            preparedStatement.setString(6, product.getType());
            preparedStatement.setBoolean(7, product.getIsPiece());
            preparedStatement.setBoolean(8, product.getIsRemoved());
            preparedStatement.executeUpdate();
            System.out.println("Product added successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Product> getAllProducts(){
        List<Product> products = new ArrayList<>();
        try{
            String url = "jdbc:mysql://localhost:3306/javafxdb";
            zorunlu user = new zorunlu();
            Connection connection = DriverManager.getConnection(url, user.name, user.pass);
            String query = "SELECT * FROM products";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Product product = new Product(resultSet.getString("name"), resultSet.getDouble("stock"), resultSet.getString("image"), resultSet.getDouble("price"), resultSet.getDouble("threshold"), resultSet.getString("type"), resultSet.getInt("id"), resultSet.getBoolean("unit"),resultSet.getBoolean("isRemoved"));
                products.add(product);
            }
        } catch (SQLException e) {
        }
        return products;
    }

    public List<Carrier> getCarriers()
    {
        List<Carrier> carriers = new ArrayList<>();
        try{
            String url = "jdbc:mysql://localhost:3306/javafxdb";
            zorunlu user = new zorunlu();
            Connection connection = DriverManager.getConnection(url, user.name, user.pass);
            String query = "SELECT * FROM user WHERE userType = 'carrier'";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Carrier carrier = new Carrier(resultSet.getString("name"), resultSet.getString("surname"), resultSet.getString("password"), resultSet.getString("email"), resultSet.getString("phone"), resultSet.getString("username"), resultSet.getString("address"), resultSet.getString("profilePicture"));
                carriers.add(carrier);
            }
        } catch (SQLException e) {
        }
        return carriers;
    }

    public void updateCarrier(Carrier carrier) throws SQLException{        
        System.out.println("update carrier");
        String url = "jdbc:mysql://localhost:3306/javafxdb";
        zorunlu user = new zorunlu();
        Connection connection = DriverManager.getConnection(url, user.name, user.pass);
        String query = "UPDATE user SET name = ?, surname = ?, password = ?, email = ?, phone = ? WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, carrier.getName());
            preparedStatement.setString(2, carrier.getSurname());
            preparedStatement.setString(3, carrier.getPassword());
            preparedStatement.setString(4, carrier.getEmail());
            preparedStatement.setString(5, carrier.getPhone());
            preparedStatement.setString(6, carrier.getUsername());
            preparedStatement.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Carrier updated successfully");
    }


    public void deleteCarrier(String username) throws SQLException{        
        String url = "jdbc:mysql://localhost:3306/javafxdb";
        zorunlu user = new zorunlu();
        Connection connection = DriverManager.getConnection(url, user.name, user.pass);
        String query = "DELETE FROM user WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeOrderStatus(String orderID, String status) throws SQLException{        
        String url = "jdbc:mysql://localhost:3306/javafxdb";
        zorunlu user = new zorunlu();
        Connection connection = DriverManager.getConnection(url, user.name, user.pass);
        String query = "UPDATE orders SET status = ? WHERE orderid = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, status);
            preparedStatement.setString(2, orderID);
            preparedStatement.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getAddress(int id){
        String address = "";
        try{
            String url = "jdbc:mysql://localhost:3306/javafxdb";
            zorunlu user = new zorunlu();
            Connection connection = DriverManager.getConnection(url, user.name, user.pass);
            String query = "SELECT address FROM user WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                address = resultSet.getString("address");
            }
        } catch (SQLException e) {
        }
        return address;
    }

    public List<Order> getAllOrders(){
        List<Order> orders = new ArrayList<>();
        try{
            String url = "jdbc:mysql://localhost:3306/javafxdb";
            zorunlu user = new zorunlu();
            Connection connection = DriverManager.getConnection(url, user.name, user.pass);
            String query = "SELECT * FROM orders";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Order order = new Order(resultSet.getInt("userid"), resultSet.getString("orderid"), resultSet.getInt("productid"), resultSet.getDouble("amount"), resultSet.getString("date"), resultSet.getString("status"), resultSet.getDouble("price"),resultSet.getString("deliveryDate"),resultSet.getString("carrierUsername"));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
    
    public List<Order> getAllOrdersWaiting()
    {
        List<Order> orders = new ArrayList<>();
        try{
            String url = "jdbc:mysql://localhost:3306/javafxdb";
            zorunlu user = new zorunlu();

            Connection connection = DriverManager.getConnection(url, user.name, user.pass);
            String query = "SELECT * FROM orders WHERE status = 'waiting'";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Order order = new Order(resultSet.getInt("userid"), resultSet.getString("orderid"), resultSet.getInt("productid"), resultSet.getDouble("amount"), resultSet.getString("date"), resultSet.getString("status"), resultSet.getDouble("price"),resultSet.getString("deliveryDate"),resultSet.getString("carrierUsername"));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public void addOrdersql(Order order){
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("USE javafxdb;");
            String query = "INSERT INTO orders (orderid, userid, productid, amount, date, price, status) VALUES ('" +
                            order.getOrderID() + "', '" +
                            order.getId() + "', '" +
                            order.getProductID() + "', '" +
                            order.getAmount() + "', '" +
                            order.getDate() + "', '" +
                            order.getPrice() + "', '" +
                            order.getStatus() + "');";
            statement.executeUpdate(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updateOrder(Order order)
    {
        try{
            String url = "jdbc:mysql://localhost:3306/javafxdb";
            zorunlu user = new zorunlu();
            Connection connection = DriverManager.getConnection(url, user.name, user.pass);
            String query = "UPDATE orders SET amount = ? WHERE orderid = ? AND productid = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setDouble(1, order.getAmount());
                preparedStatement.setString(2, order.getOrderID());
                preparedStatement.setInt(3, order.getProductID());
                preparedStatement.executeUpdate();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Order updated successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateOrderStatus(Order order)
    {
        try{
            String url = "jdbc:mysql://localhost:3306/javafxdb";
            zorunlu user = new zorunlu();
            Connection connection = DriverManager.getConnection(url, user.name, user.pass);
            String query = "UPDATE orders SET status = ?, carrierUsername = ? WHERE orderid = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, order.getStatus());	
                preparedStatement.setString(2, order.getCarrierUsername());
                preparedStatement.setString(3, order.getOrderID());
                preparedStatement.executeUpdate();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Order updated successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateOrderStatusandDeliveryDate(Order order)
    {
        try{
            String url = "jdbc:mysql://localhost:3306/javafxdb";
            zorunlu user = new zorunlu();

            Connection connection = DriverManager.getConnection(url, user.name, user.pass);
            String query = "UPDATE orders SET status = ?, deliveryDate = ? WHERE orderid = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, order.getStatus());
                preparedStatement.setString(2, order.getDeliveryDate());
                preparedStatement.setString(3, order.getOrderID());
                preparedStatement.executeUpdate();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Order updated successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeOrderStatusAndDate(Order order)
    {
        try{
            String url = "jdbc:mysql://localhost:3306/javafxdb";
            zorunlu user = new zorunlu();

            Connection connection = DriverManager.getConnection(url, user.name, user.pass);
            String query = "UPDATE orders SET status = ?, date = ? WHERE orderid = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, order.getStatus());
                preparedStatement.setString(2, order.getDate());
                preparedStatement.setString(3, order.getOrderID());
                preparedStatement.executeUpdate();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Order updated successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Order> isInCart(int userid)
    {
        List<Order> orders = new ArrayList<>();
        try{
            String url = "jdbc:mysql://localhost:3306/javafxdb";
            zorunlu user = new zorunlu();

            Connection connection = DriverManager.getConnection(url, user.name, user.pass);
            String query = "SELECT * FROM orders WHERE userid = ? AND status = 'inCart'";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userid);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Order order = new Order(resultSet.getInt("id"), resultSet.getString("orderid"), resultSet.getInt("productid"), resultSet.getDouble("amount"), resultSet.getString("date"), resultSet.getString("status"), resultSet.getDouble("price"),resultSet.getString("deliveryDate"),resultSet.getString("carrierUsername"));
                if (orders == null)
                {
                    orders = new ArrayList<>();
                    
                }
                orders.add(order);
            }
        } catch (SQLException e) {
            e.getStackTrace();
        }
        System.out.println("Size BU: "+ orders.size());
        return orders;
    }

    public void deleteFromCart(Order order)
    {
        try
        {
            String url = "jdbc:mysql://localhost:3306/javafxdb";
            zorunlu user = new zorunlu();
            Connection connection = DriverManager.getConnection(url, user.name, user.pass);

            String query = "DELETE FROM orders WHERE orderid = ? AND productid = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, order.getOrderID());
                preparedStatement.setInt(2, order.getProductID());
                preparedStatement.executeUpdate();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public Order getOrderFromId(String orderID, int productID)
    {
        try
        {
            String url = "jdbc:mysql://localhost:3306/javafxdb";
            zorunlu user = new zorunlu();

            Connection connection = DriverManager.getConnection(url, user.name, user.pass);
            String query = "SELECT * FROM orders WHERE orderid = ? AND productid = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, orderID);
            preparedStatement.setInt(2, productID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Order order = new Order(resultSet.getInt("id"), resultSet.getString("orderid"), resultSet.getInt("productid"), resultSet.getDouble("amount"), resultSet.getString("date"), resultSet.getString("status"), resultSet.getDouble("price"),resultSet.getString("deliveryDate"),resultSet.getString("carrierUsername"));
                return order;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Customer getUserFromId(int id)
    {
        try
        {
            String url = "jdbc:mysql://localhost:3306/javafxdb";
            zorunlu user = new zorunlu();

            Connection connection = DriverManager.getConnection(url, user.name, user.pass);
            String query = "SELECT * FROM user WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Customer customer = new Customer(resultSet.getString("name"), resultSet.getString("surname"), resultSet.getString("password"), resultSet.getString("email"), resultSet.getString("phone"), resultSet.getString("username"), resultSet.getString("address"), resultSet.getString("profilePicture"));
                return customer;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateProduct(Product product){
        try{
            String url = "jdbc:mysql://localhost:3306/javafxdb";
            zorunlu user = new zorunlu();
            Connection connection = DriverManager.getConnection(url, user.name, user.pass);
            String query = "UPDATE products SET name = ?, stock = ?, price = ?, threshold = ?, type = ? WHERE id = ?";
            //print the id
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, product.getName());
                preparedStatement.setDouble(2, product.getStock());
                preparedStatement.setDouble(3, product.getPrice());
                preparedStatement.setDouble(4, product.getThreshold());
                preparedStatement.setString(5, product.getType());
                preparedStatement.setInt(6, product.getId());
                preparedStatement.executeUpdate();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Product updated successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProductFromName(Product product){
        try{
            String url = "jdbc:mysql://localhost:3306/javafxdb";
            zorunlu user = new zorunlu();
            Connection connection = DriverManager.getConnection(url, user.name, user.pass);
            String query = "UPDATE products SET stock = ?, price = ?, threshold = ?, isRemoved = ? WHERE name = ?";
            //print the id
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setDouble(1, product.getStock());
                preparedStatement.setDouble(2, product.getPrice());
                preparedStatement.setDouble(3, product.getThreshold());
                preparedStatement.setBoolean(4, product.getIsRemoved());
                preparedStatement.setString(5, product.getName());
                preparedStatement.executeUpdate();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Product updated successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeProduct(int id)
    {
        try{
            String url = "jdbc:mysql://localhost:3306/javafxdb";
            zorunlu user = new zorunlu();
            Connection connection = DriverManager.getConnection(url, user.name, user.pass);
            String query = "UPDATE products SET isRemoved = 1 WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Product deleted successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProductStock(int productId, double amount){
        try {
            if (connection == null) {
                throw new SQLException("Connection not established.");
            }
    
            try (PreparedStatement useStatement = connection.prepareStatement("USE javafxdb;")) {
                useStatement.execute();
            }
    
            String query = "UPDATE products SET stock = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setDouble(1, amount);
                preparedStatement.setInt(2, productId);
                preparedStatement.executeUpdate();
            }

            

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public void updateProductPrice(int productId, double price)
    {
        try{
            String urlString = "jdbc:mysql://localhost:3306/javafxdb";
            zorunlu user = new zorunlu();
            Connection connection = DriverManager.getConnection(urlString, user.name, user.pass);
            String query = "Update products set price = ? where id = ?;";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setDouble(1, price);
                preparedStatement.setInt(2, productId);
                preparedStatement.executeUpdate();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Product updated successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProductPriceWhenCancel(int productId, double amount)
    {
        try{
            String urlString = "jdbc:mysql://localhost:3306/javafxdb";
            zorunlu user = new zorunlu();
            Connection connection = DriverManager.getConnection(urlString, user.name, user.pass);
            String query = "Update products set price = IF((stock < threshold) AND (stock + ? > threshold), price / 2, price) where id = ?;";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setDouble(1, amount);
                preparedStatement.setInt(2, productId);
                preparedStatement.executeUpdate();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Product updated successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Product getProductFromId (int id)
    {
        try{
            String url = "jdbc:mysql://localhost:3306/javafxdb";
            zorunlu user = new zorunlu();
            Connection connection = DriverManager.getConnection(url, user.name, user.pass);
            String query = "SELECT * FROM products WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Product product = new Product(resultSet.getString("name"), resultSet.getDouble("stock"), resultSet.getString("image"), resultSet.getDouble("price"), resultSet.getDouble("threshold"), resultSet.getString("type"), resultSet.getInt("id"), resultSet.getBoolean("unit"),resultSet.getBoolean("isRemoved"));
                System.out.println("tutu6tu6t6utProduct name: " + product.getName() + " Product stock: " + product.getStock() + " Product price: " + product.getPrice() + " Product threshold: " + product.getThreshold() + " Product type: " + product.getType() + " Product id: " + product.getId() + " Product unit: " + product.getIsPiece());

                return product;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Product not found");
        return null;
    }

    public Double getAllOrdersRevenue()
    {
        Double revenue = 0.0;
        try{
            String url = "jdbc:mysql://localhost:3306/javafxdb";
            zorunlu user = new zorunlu();

            Connection connection = DriverManager.getConnection(url, user.name, user.pass);
            String query = "SELECT price, amount FROM orders WHERE status = 'completed'";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                System.out.println("Price: " + resultSet.getDouble("price") + " Amount: " + resultSet.getDouble("amount"));
                revenue += resultSet.getDouble("price") * resultSet.getDouble("amount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return revenue;
    }

    public int getAllOrdersDifferentCount(String status)
    {
        int count = 0;
        try{
            String url = "jdbc:mysql://localhost:3306/javafxdb";
            zorunlu user = new zorunlu();

            Connection connection = DriverManager.getConnection(url, user.name, user.pass);
            String query = "SELECT COUNT(DISTINCT orderid) FROM orders WHERE status = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, status);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                count = resultSet.getInt("COUNT(DISTINCT orderid)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public HashMap<String, Integer> getOrdersRevenueByDate()
    {
        HashMap<String, Integer> revenueByDate = new HashMap<>();
        try
        {
            String url = "jdbc:mysql://localhost:3306/javafxdb";
            zorunlu user = new zorunlu();

            Connection connection = DriverManager.getConnection(url, user.name, user.pass);
            String query = "SELECT deliveryDate, price, amount FROM orders WHERE status = 'completed'";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String date = resultSet.getString("deliveryDate");
                Double price = resultSet.getDouble("price");
                Double amount = resultSet.getDouble("amount");
                date = date.substring(0, date.indexOf(' '));
                if (revenueByDate.containsKey(date))
                {
                    revenueByDate.put(date, revenueByDate.get(date) + (int)(price * amount));
                }
                else
                {
                    revenueByDate.put(date, (int)(price * amount));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return revenueByDate;
    }
}
