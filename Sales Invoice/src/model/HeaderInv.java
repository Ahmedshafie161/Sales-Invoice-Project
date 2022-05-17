
package model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HeaderInv {
    private int numInv;
    private String nameCustomer;
    private ArrayList<LineInv> linesList; 
    private Date dateInv;


    

    public void setCustomerName(String customerName) {
        this.nameCustomer = customerName;
    }

    

    public int getNumInv() {
        return numInv;
    }

    public void setNumInv(int invNum) {
        this.numInv = invNum;
    }

    public String getCustomerName() {
        return nameCustomer;
    }
    public void setDateInv(Date invDate) {
        this.dateInv = invDate;
    }
    
     public Date getInvDate() {
        return dateInv;
    }
     public HeaderInv(int invNum, String customerName, Date invDate) {
        this.numInv = invNum;
        this.dateInv = invDate; 
        this.nameCustomer = customerName;

    }
     

   
    public void addLineInv(LineInv line) {
        getLinesList().add(line);
    }
   
    

    public void setLinesList(ArrayList<LineInv> lines) {
        this.linesList = lines;
    }
    public ArrayList<LineInv> getLinesList() {
        if (linesList == null)
            linesList = new ArrayList<>();  
        return linesList;
    }

    public double getTotaIInv() {
        double total = 0.0;
        for (LineInv line : getLinesList()) {
            total += line.getLineTotal();
        }
        return total;
    }
    
    
    
    public String getData() {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String result = "" + getNumInv() + "," + df.format(getInvDate()) + "," + getCustomerName();
        return result;
    }
    
}
