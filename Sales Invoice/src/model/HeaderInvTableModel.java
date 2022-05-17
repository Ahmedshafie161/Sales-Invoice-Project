
package model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.table.AbstractTableModel;


public class HeaderInvTableModel extends AbstractTableModel {

    private List<HeaderInv> invoicesList;
    private DateFormat dataFormat = new SimpleDateFormat("dd-MM-yyyy");
    
    public HeaderInvTableModel(List<HeaderInv> invoicesList) {
        this.invoicesList = invoicesList;
    }

    public List<HeaderInv> getInvoicesList() {
        return invoicesList;
    }
    
    
    @Override
    public int getRowCount() {
        return invoicesList.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Invoice Num";
            case 1:
                return "Customer Name";
            case 2:
                return "Invoice Date";
            case 3:
                return "Invoice Total";
            default:
                return "";
        }
    }

   

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
     @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Integer.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            case 3:
                return Double.class;
            default:
                return Object.class;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        HeaderInv row = invoicesList.get(rowIndex);
        
        switch (columnIndex) {
            case 0:
                return row.getNumInv();
            case 1:
                return row.getCustomerName();
            case 2:
                return dataFormat.format(row.getInvDate());
            case 3:
                return row.getTotaIInv();
            default:
                return "";
        }
        
    }
    
}
