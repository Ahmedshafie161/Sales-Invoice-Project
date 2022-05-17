
package model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.table.AbstractTableModel;


public class LinesInvTableModel extends AbstractTableModel {

    private List<LineInv> invoiceLines;
    private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    
    public LinesInvTableModel(List<LineInv> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }

    public List<LineInv> getInvoiceLines() {
        return invoiceLines;
    }
    
    
    @Override
    public int getRowCount() {
        return invoiceLines.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Name";
            case 1:
                return "Price";
            case 2:
                return "Count";
            case 3:
                return "Total Line";
            default:
                return "";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return Double.class;
            case 2:
                return Integer.class;
            case 3:
                return Double.class;
            default:
                return Object.class;
        }
    }

   

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        LineInv row = invoiceLines.get(rowIndex);
        
        switch (columnIndex) {
            case 0:
                return row.getName();
            case 1:
                return row.getPrice();
            case 2:
                return row.getCount();
            case 3:
                return row.getLineTotal();
            default:
                return "";
        }
        
        
    }
     @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
}
