package lk.ijse.pos.dao.custom.impl;

import lk.ijse.pos.dao.SQLUtil;
import lk.ijse.pos.dao.custom.OrderDetailsDAO;
import lk.ijse.pos.entity.OrderDetails;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailsDAOImpl implements OrderDetailsDAO {
    @Override
    public ArrayList<OrderDetails> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.executeQuery("SELECT * FROM orderDetail");
        ArrayList<OrderDetails> all = new ArrayList<>();
        while (rst.next()){
            all.add(new OrderDetails(rst.getString(1),rst.getString(2),rst.getInt(4),rst.getBigDecimal(3)));
        }

        return all;
    }

    @Override
    public boolean save(OrderDetails entity) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("INSERT INTO OrderDetail (OrderID, itemCode, OrderQty, unitPrice) VALUES (?,?,?,?)", entity.getOid(), entity.getItemCode(), entity.getQty(), entity.getUnitPrice());
    }

    @Override
    public boolean update(OrderDetails dto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean updateDetail(OrderDetails entity,String itemCode) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("UPDATE OrderDetail SET ItemCode=?, unitPrice=?, OrderQty=? WHERE orderId=? AND ItemCode=? ", entity.getItemCode(), entity.getUnitPrice(), entity.getQty(), entity.getOid(),itemCode);

    }

    @Override
    public OrderDetails search(String s) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean exist(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean deleteItem(OrderDetails orderDetails) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeUpdate("DELETE FROM OrderDetail WHERE OrderId=? and itemCode =?", orderDetails.getOid(),orderDetails.getItemCode());
    }

    @Override
    public String generateNewID() throws SQLException, ClassNotFoundException {
        String table ="ODderDetail";
        String incrementID=null;

        ResultSet maxId = SQLUtil.execute("SELECT CONCAT(MAX(0+SUBSTRING(OrderId,3))) FROM OrderDetail");

        String id=null;

        while (maxId.next()){
            id=maxId.getString(1);
        }

        if (id!=null){
            int nextID = Integer.parseInt(id);
            nextID++;
            String cptl =table.substring(1,2);
            char v=cptl.charAt(0);
            char second = Character.toUpperCase(v);
            String first =table.substring(0,1);
            incrementID = first+second+nextID;

        }else {
            String cptl =table.substring(1,2);
            char v=cptl.charAt(0);
            char second = Character.toUpperCase(v);
            String first =table.substring(0,1);
            incrementID = first+second+"1";


        }



        return incrementID;

    }

}
