/**
 * @author : ALE_IS_TER
 * Project Name: Layered Architecture
 * Date        : 5/31/2022
 * Time        : 9:18 AM
 * @Since : 0.1.0
 */

package lk.ijse.pos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import lk.ijse.pos.bo.BOFactory;
import lk.ijse.pos.bo.custom.ManageBO;
import lk.ijse.pos.dto.CustomerDTO;
import lk.ijse.pos.dto.ItemDTO;
import lk.ijse.pos.dto.OrderDTO;
import lk.ijse.pos.dto.OrderDetailDTO;
import lk.ijse.pos.view.util.Util;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ManageFormController {
    public JFXTextField txtCustomerName;
    public AnchorPane WorkingContex;
    public JFXTextField txtCustomerNIC;
    public JFXTextField txtCustomerPhoneNumber;
    public JFXButton btnAddCustomer;

    public TableView<CustomerDTO> tblCustomer;
    public TableColumn colCusID;
    public TableColumn colCusName;
    public TableColumn colCusAddress;
    public TableColumn colCusNIC;
    public TableColumn colCusPhoneNumber;
    public JFXTextField txtCustomerAddress;
    public TableView <OrderDetailDTO>tblOrder;
    public TableColumn colOrderID;
    public TableColumn colOrderQtyOnHand;
    public JFXButton btnModifyOrder;
    public TableColumn colItemCode;
    public TableColumn colOrderUnitPrice;
    public TableColumn colTotal;

    public JFXTextField txtOrderID;
    public JFXTextField txtUnitPrice;
    public JFXTextField txtTotal;
    public JFXComboBox cmbItemCode;
    public JFXTextField txtQtyOnHands;
    private CustomerDTO newValue1;
    private OrderDetailDTO orderDetailDTONewValue;
    private final ManageBO manageBO = (ManageBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.MANAGECUSTOMER);

    public JFXTextField txtCustomerID;

    private int qty;

    public void initialize() throws SQLException, ClassNotFoundException {
        colCusID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCusName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCusAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCusNIC.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colCusPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("PhoneNumber"));


        colOrderID.setCellValueFactory(new PropertyValueFactory<>("oid"));
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colOrderQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colOrderUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));


        txtQtyOnHands.textProperty().addListener((observable, oldValue, newValue) -> {
            checkValidate(txtQtyOnHands,"^[0-9]*$",btnModifyOrder);


        });

        txtTotal.textProperty().addListener((observable, oldValue, newValue) -> {


            if (orderDetailDTONewValue!=null){
                if (newValue.equals(orderDetailDTONewValue.getTotal() + "")){

                    btnModifyOrder.setText("Delete Order");
                }else if (newValue.equals("000.00")){
                    btnModifyOrder.setText("Delete Order");
                }
                else {
                    btnModifyOrder.setText("Modify Order");

                }

            }



        });

        txtQtyOnHands.textProperty().addListener((observable, oldValue, newValue) -> {

        });


        loadAllTable();

        txtQtyOnHands.setDisable(true);
        txtCustomerID.setDisable(true);
        txtCustomerName.setDisable(true);
        txtCustomerAddress.setDisable(true);
        txtCustomerNIC.setDisable(true);
        txtCustomerPhoneNumber.setDisable(true);
        btnAddCustomer.setDisable(true);


        tblCustomer.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
             newValue1 = newValue;

            btnAddCustomer.setText(newValue != null ? "Delete Customer" : "Add Customer");

            if (newValue != null) {
                txtCustomerID.setText(newValue.getId());
                txtCustomerName.setText(newValue.getName());
                txtCustomerAddress.setText(newValue.getAddress());
                txtCustomerNIC.setText(newValue.getNic());
                txtCustomerPhoneNumber.setText(newValue.getPhoneNumber());

                txtCustomerID.setDisable(false);
                txtCustomerName.setDisable(false);
                txtCustomerAddress.setDisable(false);
                txtCustomerNIC.setDisable(false);
                txtCustomerPhoneNumber.setDisable(false);
            }
        });

        tblOrder.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
             orderDetailDTONewValue = newValue;


            btnModifyOrder.setText(newValue != null ? "Delete Order" : "Modify Order");

            if (newValue != null) {
                qty=newValue.getQty();
                txtQtyOnHands.setDisable(false);
                txtOrderID.setText(newValue.getOid());
                txtUnitPrice.setText(String.valueOf(newValue.getQty()));
                txtTotal.setText(String.valueOf(newValue.getTotal()));
                txtQtyOnHands.setText(String.valueOf(newValue.getUnitPrice()));
                cmbItemCode.setValue(newValue.getItemCode());
            }
        });

        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!=null){
                if (newValue.equals(orderDetailDTONewValue.getItemCode())){
                    btnModifyOrder.setText("Delete Order");
                }else {
                    btnModifyOrder.setText("Modify Order");
                }
                try {
                    ItemDTO itemDTO = manageBO.searchItem(String.valueOf(newValue));
                    txtUnitPrice.setText(String.valueOf(itemDTO.getUnitPrice()));
                    if (txtQtyOnHands.getText().matches("^[0-9]*$") && !txtQtyOnHands.getText().equals("")){



                        int qty = Integer.parseInt(txtQtyOnHands.getText());
                        double unitPrice = Double.parseDouble(txtUnitPrice.getText());

                        double total = qty *unitPrice;

                        txtTotal.setText(String.valueOf(total));
                    }
                    if (txtQtyOnHands.getText().equals("")){
                        txtTotal.setText("000.00");
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }



        });

        checkValidate(txtCustomerName,"^[A-Z][a-z]*[ ][A-Z][a-z]*$",btnAddCustomer);
        checkValidate(txtCustomerNIC,"^([0-9]{9}[V]|[0-9]{12})$",btnAddCustomer);
        checkValidate(txtCustomerPhoneNumber,"^(\\+|0)(94|[1-9]{2,3})(-| |)([0-9]{7}|[0-9]{2} [0-9]{7})$",btnAddCustomer);
        checkValidate(txtCustomerAddress,"^[A-z ]+$",btnAddCustomer);


    }



    private void checkValidate(JFXTextField jfxTextField,String regex,JFXButton jfxButton) {
        jfxTextField.textProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue.matches(regex)){
                jfxButton.setDisable(false);
                jfxTextField.setFocusColor(Color.GREEN);
            }
            else {
                jfxButton.setDisable(true);
                jfxTextField.setFocusColor(Color.RED);
            }


        });
    }

    private void loadAllTable() throws SQLException, ClassNotFoundException {
        loadAllCustomer();
        loadAllOrder();
        loadAllItems();
    }

    private void loadAllItems() throws SQLException, ClassNotFoundException {
        ArrayList<ItemDTO> itemDTOS = manageBO.loadAllItem();

        for (ItemDTO itemDTO : itemDTOS
                ) {
            cmbItemCode.getItems().add(itemDTO.getCode());
        }

    }

    private void loadAllOrder() {
        tblOrder.getItems().clear();
        /*Get all customers*/
        try {

            //Loos Coupling
            ArrayList<OrderDetailDTO> allOrder = manageBO.loadAllOrderDetails();



            for (OrderDetailDTO orderDetailDTO : allOrder) {

                Double unitPrice =Double.parseDouble(String.valueOf(orderDetailDTO.getUnitPrice())) ;

                int qty =  orderDetailDTO.getQty();

                double total = unitPrice*qty;

                tblOrder.getItems().add(new OrderDetailDTO(orderDetailDTO.getOid(),orderDetailDTO.getItemCode(),orderDetailDTO.getQty(),orderDetailDTO.getUnitPrice(),total));


            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void loadAllCustomer() {
        tblCustomer.getItems().clear();
        /*Get all customers*/
        try {

            //Loos Coupling
            ArrayList<CustomerDTO> allCustomers = manageBO.loadAllCustomers();


            for (CustomerDTO customer : allCustomers) {
                tblCustomer.getItems().add(customer);


            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }





    public void addOnAction(MouseEvent mouseEvent) throws SQLException, ClassNotFoundException {

        newValue1=null;

        txtCustomerID.clear();
        txtCustomerID.setText(manageBO.generateNewId());
        txtCustomerName.clear();
        txtCustomerAddress.clear();
        txtCustomerNIC.clear();
        txtCustomerPhoneNumber.clear();
        btnAddCustomer.setText("Add Customer");

        txtCustomerID.setDisable(false);
        txtCustomerName.setDisable(false);
        txtCustomerAddress.setDisable(false);
        txtCustomerNIC.setDisable(false);
        txtCustomerPhoneNumber.setDisable(false);
    }

    public void txtCustomerNameOnKeyRelease(KeyEvent keyEvent) {
if (newValue1!=null){checkText(txtCustomerName,newValue1.getName());}


    }

    public void txtCustomerNicOnKeyRelease(KeyEvent keyEvent) {

        if (newValue1!=null){checkText(txtCustomerNIC,newValue1.getNic());}

    }

    private void checkText(JFXTextField jfxTextField,String value) {
        if (newValue1==null){

            btnAddCustomer.setText("Add Customer");
        }else if (jfxTextField.getText().equals(value)){

            btnAddCustomer.setText("Delete Customer");
        }else {

            btnAddCustomer.setText("Update Customer");
        }
    }

    public void txtCustomerPhoneNumberOnKeyRelease(KeyEvent keyEvent) {

        if (newValue1!=null){checkText(txtCustomerPhoneNumber,newValue1.getPhoneNumber());}

    }

    public void addCustomerOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (btnAddCustomer.getText().equals("Add Customer")){
            if ( manageBO.saveCustomer(new CustomerDTO(txtCustomerID.getText(),txtCustomerName.getText(),txtCustomerAddress.getText(),txtCustomerNIC.getText(),txtCustomerPhoneNumber.getText()))){

                Util.notifications("Customer has been saved successfully","SUCCESSFULLY");
                loadAllCustomer();
            }

        }
        else if (btnAddCustomer.getText().equals("Delete Customer")){

            if (manageBO.deleteCustomer(txtCustomerID.getText())){

                Util.notifications("Customer has been Deleted successfully","SUCCESSFULLY");
                loadAllCustomer();
            }
        }
        else {

            if (manageBO.UpdateCustomer(new CustomerDTO(txtCustomerID.getText(),txtCustomerName.getText(),txtCustomerAddress.getText(),txtCustomerNIC.getText(),txtCustomerPhoneNumber.getText()))){

                Util.notifications("Customer has been Updated successfully","SUCCESSFULLY");
                loadAllCustomer();
            }

        }

        txtCustomerID.clear();
        txtCustomerName.clear();
        txtCustomerAddress.clear();
        txtCustomerNIC.clear();
        txtCustomerPhoneNumber.clear();
        btnAddCustomer.setText("Add Customer");
    }

    public void txtCustomerAddressOnKeyRelease(KeyEvent keyEvent) {
        if (newValue1!=null){checkText(txtCustomerAddress,newValue1.getAddress());}



    }

    public void modifyOrderOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        if (btnModifyOrder.getText().equals("Delete Order")){
            if ( manageBO.deleteOrderDetail(new OrderDetailDTO(txtOrderID.getText(),(String) cmbItemCode.getValue()))){

                ItemDTO itemDTO = manageBO.searchItems((String) cmbItemCode.getValue());

                int itemQty = itemDTO.getQtyOnHand();
                int qtyInNow = Integer.parseInt(String.valueOf(orderDetailDTONewValue.getUnitPrice()));
                int ndUpdateQty = itemDTO.getQtyOnHand()+qtyInNow;

                manageBO.updateItems(new ItemDTO(orderDetailDTONewValue.getItemCode(),ndUpdateQty));


                loadAllOrder();

                Util.notifications("Order Detail Deleted SuccessFull","DELETED");

                List<OrderDTO> collect = tblOrder.getItems().stream().map(tm -> new OrderDTO(
                        tm.getOid(), tm.getTotal()
                )).collect(Collectors.toList());

                double total=0;

                for (OrderDTO order: collect
                ) {

                    if (txtOrderID.getText().equals(order.getOrderId())){
                        total=total+order.getTotal();
                    }




                }


                if (total == 0){
                    if ( manageBO.deleteOrder(txtOrderID.getText())){
                        Util.notifications("Order Deleted SuccessFull","DELETED");
                    }
                }


                txtTotal.clear();
                txtQtyOnHands.clear();
                txtOrderID.clear();
                txtUnitPrice.clear();
                cmbItemCode.setValue(null);
                txtQtyOnHands.setDisable(true);


            }

        }
        else {


            if ( manageBO.UpdateOrderDetail(new OrderDetailDTO(
                    txtOrderID.getText(),(String) cmbItemCode.getValue(),BigDecimal.valueOf(Double.parseDouble(txtUnitPrice.getText())),
                    Integer.parseInt(txtQtyOnHands.getText())), orderDetailDTONewValue.getItemCode())){
                Util.notifications("Order Detail Updated SuccessFull","UPDATED");

                ItemDTO itemDTO = manageBO.searchItems((String) cmbItemCode.getValue());

                int itemQty = itemDTO.getQtyOnHand();

                if (cmbItemCode.getValue().equals(orderDetailDTONewValue.getItemCode())){

                    int modifiedQty = Integer.parseInt(txtQtyOnHands.getText());
                    int qtyInNow = Integer.parseInt(String.valueOf(orderDetailDTONewValue.getUnitPrice()));




                    if (qtyInNow<modifiedQty){

                        int updatedQty = itemQty-(modifiedQty-qtyInNow);

                        manageBO.updateItems(new ItemDTO((String) cmbItemCode.getValue(),updatedQty));



                    }else if (qtyInNow>modifiedQty){

                        int updatedQty = itemQty+(qtyInNow-modifiedQty);

                        manageBO.updateItems(new ItemDTO((String) cmbItemCode.getValue(),updatedQty));

                    }
                }else {

                    int modifiedQty = Integer.parseInt(txtQtyOnHands.getText());
                    int qtyInNow = Integer.parseInt(String.valueOf(orderDetailDTONewValue.getUnitPrice()));

                    int updatedQty = itemQty-modifiedQty;



                    manageBO.updateItems(new ItemDTO((String) cmbItemCode.getValue(),updatedQty));

                    ItemDTO itemDTOin = manageBO.searchItems(orderDetailDTONewValue.getItemCode());

                    int ndUpdateQty = itemDTOin.getQtyOnHand()+qtyInNow;

                    manageBO.updateItems(new ItemDTO(orderDetailDTONewValue.getItemCode(),ndUpdateQty));

                }






                loadAllOrder();
            }
            List<OrderDTO> collect = tblOrder.getItems().stream().map(tm -> new OrderDTO(
                   tm.getOid(), tm.getTotal()
            )).collect(Collectors.toList());

            double total=0;

            for (OrderDTO order: collect
            ) {

                if (txtOrderID.getText().equals(order.getOrderId())){
                    total=total+order.getTotal();
                }




            }


            if (total == 0){
                if ( manageBO.deleteOrder(txtOrderID.getText())){
                    Util.notifications("Order Deleted SuccessFull","DELETED");
                }
            }

            if ( manageBO.UpdateOrder(new OrderDTO(txtOrderID.getText(),total))){
                Util.notifications("Order Updated SuccessFull","UPDATED");



            }



            txtOrderID.clear();
            txtTotal.clear();
            txtQtyOnHands.clear();
            txtUnitPrice.clear();
            cmbItemCode.setValue(null);
            txtQtyOnHands.setDisable(true);

        }

    }

    public void txtQtyOnKeyRelease(KeyEvent keyEvent) {



        if (txtQtyOnHands.getText().matches("^[0-9]*$") && !txtQtyOnHands.getText().equals("")){



            int qty = Integer.parseInt(txtQtyOnHands.getText());
            double unitPrice = Double.parseDouble(txtUnitPrice.getText());

            double total = qty *unitPrice;

            txtTotal.setText(String.valueOf(total));
        }
        if (txtQtyOnHands.getText().equals("")){
            txtTotal.setText("000.00");
        }
    }
}
