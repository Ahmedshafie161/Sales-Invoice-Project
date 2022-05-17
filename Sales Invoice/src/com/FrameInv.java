
package com;

import model.HeaderInv;
import model.HeaderInvTableModel;
import model.LineInv;
import model.LinesInvTableModel;
import view.InvoiceHeaderDialog;
import view.InvoiceLineDialog;
import java.awt.HeadlessException;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;



public class FrameInv extends javax.swing.JFrame implements ActionListener, ListSelectionListener {
private javax.swing.JButton btnCreateInv;
    private javax.swing.JButton btnCreateLine;
    private javax.swing.JButton btnDeleteInv;
    private javax.swing.JButton btnDeleteLine;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelNumInv;
    private javax.swing.JLabel labelTotalInv;
    private javax.swing.JMenuItem menuItemLoad;
    private javax.swing.JMenuItem menuItemSave;
    private javax.swing.JTextField tFieldCustomerName;
    private javax.swing.JTextField tFieldDateInv;
    private javax.swing.JTable tableInvoices;
    private javax.swing.JTable tableLinesInv;
    private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private List<HeaderInv> listInv = new ArrayList<>();
    private HeaderInvTableModel headerInvTableModel;
    private LinesInvTableModel linesInvTableModel;
    private InvoiceHeaderDialog invoiceHeaderDialog;
    private InvoiceLineDialog invoiceLineDialog;
   
    public FrameInv() {
        initComponents();
    }

    

    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrameInv().setVisible(true);
                
            }
        });
    }

    

    private void saveData() {
        String allheaders = "";
        String lines = "";
        for (HeaderInv header : listInv) {
            allheaders += header.getData();
            allheaders += "\n";
            for (LineInv line : header.getLinesList()) {
                lines += line.getData();
                lines += "\n";
            }
        }
        JOptionPane.showMessageDialog(this, " select file to save header !", "please", JOptionPane.WARNING_MESSAGE);
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fileChooser.getSelectedFile();
                try {
                FileWriter fileWriter = new FileWriter(headerFile);
                fileWriter.write(allheaders);
                fileWriter.flush();
                fileWriter.close();

                JOptionPane.showMessageDialog(this, ", select file to save lines !", "Please", JOptionPane.WARNING_MESSAGE);
                result = fileChooser.showSaveDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File linesFile = fileChooser.getSelectedFile();
                    try (FileWriter lFW = new FileWriter(linesFile)) {
                        lFW.write(lines);
                        lFW.flush();
                    }
                }
            } catch (HeadlessException | IOException ex) {
                JOptionPane.showMessageDialog(this, "err: " + ex.getMessage(), "err", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    private void cancelCreatingLine() {
        invoiceLineDialog.setVisible(false);
        invoiceLineDialog.dispose();
        invoiceLineDialog = null;
    }

    private void confirmCreatingLine() {
        String itemName = invoiceLineDialog.getItemNameField().getText();
        String itemCountStr = invoiceLineDialog.getItemCountField().getText();
        String itemPriceStr = invoiceLineDialog.getItemPriceField().getText();
        invoiceLineDialog.setVisible(false);
        invoiceLineDialog.dispose();
        invoiceLineDialog = null;
        int itemCount = Integer.parseInt(itemCountStr);
        double itemPrice = Double.parseDouble(itemPriceStr);
        int headerIndex = tableInvoices.getSelectedRow();
        HeaderInv invoice = headerInvTableModel.getInvoicesList().get(headerIndex);

        LineInv invoiceLine = new LineInv(itemName, itemPrice, itemCount, invoice);
        invoice.addLineInv(invoiceLine);
        linesInvTableModel.fireTableDataChanged();
        headerInvTableModel.fireTableDataChanged();
        labelTotalInv.setText("" + invoice.getTotaIInv());
    }

    private void deleteInv() {
        int invIndex = tableInvoices.getSelectedRow();
        HeaderInv header = headerInvTableModel.getInvoicesList().get(invIndex);
        headerInvTableModel.getInvoicesList().remove(invIndex);
        headerInvTableModel.fireTableDataChanged();
        linesInvTableModel = new LinesInvTableModel(new ArrayList<LineInv>());
        tableLinesInv.setModel(linesInvTableModel);
        linesInvTableModel.fireTableDataChanged();
        tFieldCustomerName.setText("");
        tFieldDateInv.setText("");
        labelNumInv.setText("");
        labelTotalInv.setText("");
    }

    private void deleteLine() {
        int lineIndex = tableLinesInv.getSelectedRow();
        LineInv line = linesInvTableModel.getInvoiceLines().get(lineIndex);
        linesInvTableModel.getInvoiceLines().remove(lineIndex);
        linesInvTableModel.fireTableDataChanged();
        headerInvTableModel.fireTableDataChanged();
        labelTotalInv.setText("" + line.getHeader().getTotaIInv());
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
           
            case "createInvOK":
                confirmCreatingInv();
                break;
            case "CreateNewLine":
                displayDialogNwLine();
                break;
            case "DeleteLine":
                deleteLine();
                break;
            case "LoadFile":
                loadFile();
                break;
            case "SaveFile":
                saveData();
                break;
            case "createInvCancel":
                cancelCreatingInv();
                break;
            case "createLineCancel":
                cancelCreatingLine();
                break;
            case "createLineOK":
                confirmCreatingLine();
                break;
            case "CreateNewInvoice":
                displayDialogNwInv();
                break;
            case "DeleteInvoice":
                deleteInv();
                break;
        }
    }

    private void loadFile() {
        JOptionPane.showMessageDialog(this, " select header file!", "Please", JOptionPane.WARNING_MESSAGE);
        JFileChooser file = new JFileChooser();
        int result = file.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File fileHeader = file.getSelectedFile();
            try {
                FileReader fileReader = new FileReader(fileHeader);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    String[] row = line.split(",");
                    String numberInvStr = row[0];
                    String dateInvStr = row[1];
                    String nameCustomerStr = row[2];

                    int numbInv = Integer.parseInt(numberInvStr);
                    Date dateInv = dateFormat.parse(dateInvStr);

                    HeaderInv invoice = new HeaderInv(numbInv, nameCustomerStr, dateInv);
                    listInv.add(invoice);
                }
                //reading lines ,assign line to correspondinig header , 

                JOptionPane.showMessageDialog(this, "Please, select lines file!", "Attension", JOptionPane.WARNING_MESSAGE);
                result = file.showOpenDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File linesFile = file.getSelectedFile();
                    BufferedReader linesBr = new BufferedReader(new FileReader(linesFile));
                    String linesLine = null;
                    while ((linesLine = linesBr.readLine()) != null) {
                        String[] lineParts = linesLine.split(",");
                        String invNumStr = lineParts[0];
                        String itemName = lineParts[1];
                        String itemPriceStr = lineParts[2];
                        String itemCountStr = lineParts[3];

                        int invNum = Integer.parseInt(invNumStr);
                        int itemCount = Integer.parseInt(itemCountStr);
                        HeaderInv header = getInvByNum(invNum);
                         double itemPrice = Double.parseDouble(itemPriceStr);

                        LineInv invLine = new LineInv(itemName, itemPrice, itemCount, header);
                        header.getLinesList().add(invLine);
                    }
                    headerInvTableModel = new HeaderInvTableModel(listInv);
                    tableInvoices.setModel(headerInvTableModel);
                    tableInvoices.validate();
                }
                System.out.println("Check");
            } catch (HeadlessException | IOException | NumberFormatException | ParseException ex) {
                ex.printStackTrace();
            }
        }
    }

    
    private HeaderInv getInvByNum(int numInv) {
        HeaderInv header = null;
        for (HeaderInv inv : listInv) {
            if (numInv == inv.getNumInv()) {
                header = inv;
                break;
            }
        }
        return header;
    }

    // event : user selected row from header list 
    @Override
    public void valueChanged(ListSelectionEvent e) {
        System.out.println("Invoice Selected!");
        invoicesTableRowSelected();
    }

    private void invoicesTableRowSelected() {
        int selectedRowIndex = tableInvoices.getSelectedRow();
        if (selectedRowIndex >= 0) {
            //get selected object row selected
            HeaderInv row = headerInvTableModel.getInvoicesList().get(selectedRowIndex);
            
            //change lables , textfield values
            tFieldCustomerName.setText(row.getCustomerName());
            tFieldDateInv.setText(dateFormat.format(row.getInvDate()));
            labelNumInv.setText("" + row.getNumInv());
            labelTotalInv.setText("" + row.getTotaIInv());
            
            //display lines table
            ArrayList<LineInv> lines = row.getLinesList();
            linesInvTableModel = new LinesInvTableModel(lines);
            tableLinesInv.setModel(linesInvTableModel);
            // notify listeners model changes
            linesInvTableModel.fireTableDataChanged();
        }
    }

    //user clicked new invoice button , show dialog for adding invoice 
    private void displayDialogNwInv() {
        invoiceHeaderDialog = new InvoiceHeaderDialog(this);
        invoiceHeaderDialog.setVisible(true);
    }

        //user clicked new line button , show dialog for adding line
    private void displayDialogNwLine() {
        invoiceLineDialog = new InvoiceLineDialog(this);
        invoiceLineDialog.setVisible(true);
    }

        //user clicked new invoice button then cancel button , hide dialog , delete dialog
    private void cancelCreatingInv() {
        invoiceHeaderDialog.setVisible(false);
        invoiceHeaderDialog.dispose();
        invoiceHeaderDialog = null;
    }
        //user clicked new invoice button then Confirm button , hide dialog , delete dialog

    private void confirmCreatingInv() {
        
        //get data from dialog
        String custName = invoiceHeaderDialog.getCustNameField().getText();
        String invDateStr = invoiceHeaderDialog.getInvDateField().getText();
        
        //hide ,delete dialog
        invoiceHeaderDialog.setVisible(false);
        invoiceHeaderDialog.dispose();
        invoiceHeaderDialog = null;
        try {
            Date invDate = dateFormat.parse(invDateStr);
            int invNum = getNextInvoiceNum();
            HeaderInv invoiceHeader = new HeaderInv(invNum, custName, invDate);
            listInv.add(invoiceHeader);
            //notify listeners , show addedd row in line table
            headerInvTableModel.fireTableDataChanged();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    private int getNextInvoiceNum() {
        int max = 0;
        for (HeaderInv header : listInv) {
            if (header.getNumInv() > max) {
                max = header.getNumInv();
            }
        }
        return max + 1;
        
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableInvoices = new javax.swing.JTable();
        tableInvoices.getSelectionModel().addListSelectionListener(this);
        btnCreateInv = new javax.swing.JButton();
        btnCreateInv.addActionListener(this);
        btnDeleteInv = new javax.swing.JButton();
        btnDeleteInv.addActionListener(this);
        tFieldCustomerName = new javax.swing.JTextField();
        tFieldDateInv = new javax.swing.JTextField();
        labelNumInv = new javax.swing.JLabel();
        labelTotalInv = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableLinesInv = new javax.swing.JTable();
        btnCreateLine = new javax.swing.JButton();
        btnCreateLine.addActionListener(this);
        btnDeleteLine = new javax.swing.JButton();
        btnDeleteLine.addActionListener(this);
        jMenuBar = new javax.swing.JMenuBar();
        jMenu = new javax.swing.JMenu();
        menuItemLoad = new javax.swing.JMenuItem();
        menuItemLoad.addActionListener(this);
        menuItemSave = new javax.swing.JMenuItem();
        menuItemSave.addActionListener(this);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tableInvoices.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tableInvoices);

        btnCreateInv.setText("Create New Invoice");
        btnCreateInv.setActionCommand("CreateNewInvoice");

        btnDeleteInv.setText("Delete Invoice");
        btnDeleteInv.setActionCommand("DeleteInvoice");

        jLabel1.setText("Invoice Number");

        jLabel2.setText("Invoide Date");

        jLabel3.setText("Customer name");

        jLabel4.setText("Invoice Total");

        tableLinesInv.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(tableLinesInv);

        btnCreateLine.setText("Create New Line");
        btnCreateLine.setActionCommand("CreateNewLine");

        btnDeleteLine.setText("Delete Line");
        btnDeleteLine.setActionCommand("DeleteLine");

        jMenu.setText("File");

        menuItemLoad.setText("Load File");
        menuItemLoad.setActionCommand("LoadFile");
        jMenu.add(menuItemLoad);

        menuItemSave.setText("Save File");
        menuItemSave.setActionCommand("SaveFile");
        jMenu.add(menuItemSave);

        jMenuBar.add(jMenu);

        setJMenuBar(jMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel3)
                                            .addComponent(jLabel1)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel4))
                                        .addGap(25, 25, 25)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(tFieldCustomerName)
                                            .addComponent(tFieldDateInv)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(labelNumInv)
                                                    .addComponent(labelTotalInv))
                                                .addGap(0, 0, Short.MAX_VALUE))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(85, 85, 85)
                                .addComponent(btnCreateLine)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnDeleteLine)
                                .addGap(120, 120, 120))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(btnCreateInv)
                        .addGap(95, 95, 95)
                        .addComponent(btnDeleteInv)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 548, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(labelNumInv))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(tFieldDateInv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(tFieldCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(labelTotalInv))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45,45, 45)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCreateLine)
                            .addComponent(btnDeleteLine))
                        .addGap(65, 65, 65)))
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDeleteInv)
                    .addComponent(btnCreateInv))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }
    

    

}
