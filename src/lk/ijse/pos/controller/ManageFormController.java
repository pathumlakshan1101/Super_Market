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
import lk.ijse.pos.bo.custom.ManageCustomerBO;
import lk.ijse.pos.dto.CustomerDTO;
import lk.ijse.pos.view.Util;
import lk.ijse.pos.view.tdm.CustomerTM;

import java.sql.SQLException;
import java.util.ArrayList;

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
    public TableView tblOrder;
    public TableColumn colOrderID;
    public TableColumn colOrderDate;
    public TableColumn colOrderQtyOnHand;
    public TableColumn colOrderCusID;
    public TableColumn colOrderItemCode;
    public JFXTextField txtOrderDate;
    public JFXButton btnModifyOrder;
    public JFXComboBox cmbOrderID;
    public JFXTextField txtQtyOnHand;
    public JFXComboBox cmbItemCode;
    public JFXComboBox cmbOrderCustomerID;
    private CustomerDTO newValue1;
    private final ManageCustomerBO manageCustomerBO = (ManageCustomerBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.MANAGECUSTOMER);

    public JFXTextField txtCustomerID;

    public void initialize(){
        colCusID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCusName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCusAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCusNIC.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colCusPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("PhoneNumber"));
        loadAllTable();

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

    private void loadAllTable() {
        loadAllCustomer();
        loadAllOrder();
    }

    private void loadAllOrder() {

    }

    private void loadAllCustomer() {
        tblCustomer.getItems().clear();
        /*Get all customers*/
        try {

            //Loos Coupling
            ArrayList<CustomerDTO> allCustomers = manageCustomerBO.loadAllCustomers();


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
        txtCustomerID.setText(manageCustomerBO.generateNewId());
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
            if ( manageCustomerBO.saveCustomer(new CustomerDTO(txtCustomerID.getText(),txtCustomerName.getText(),txtCustomerAddress.getText(),txtCustomerNIC.getText(),txtCustomerPhoneNumber.getText()))){

                Util.notifications("Customer has been saved successfully","SUCCESSFULLY");
                loadAllCustomer();
            }

        }
        else if (btnAddCustomer.getText().equals("Delete Customer")){

            if (manageCustomerBO.deleteCustomer(txtCustomerID.getText())){
                new Alert(Alert.AlertType.INFORMATION, "Customer has been Deleted successfully").show();
                Util.notifications("Customer has been saved successfully","SUCCESSFULLY");
                loadAllCustomer();
            }
        }
        else {

            if (manageCustomerBO.UpdateCustomer(new CustomerDTO(txtCustomerID.getText(),txtCustomerName.getText(),txtCustomerAddress.getText(),txtCustomerNIC.getText(),txtCustomerPhoneNumber.getText()))){
                new Alert(Alert.AlertType.INFORMATION, "Customer has been Updated successfully").show();
                loadAllCustomer();
            }

        }
    }

    public void txtCustomerAddressOnKeyRelease(KeyEvent keyEvent) {
        if (newValue1!=null){checkText(txtCustomerAddress,newValue1.getAddress());}


    }

    public void modifyOrderOnAction(ActionEvent actionEvent) {

    }


}
