
package model;

public class LineInv {
    private String name;
    private double price;
    private int count;
    private HeaderInv header;

    

    public void setItemCount(int count) {
        this.count = count;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }
    
    public double getPrice() {
        return price;
    }

     public String getName() {
        return name;
    }

    public HeaderInv getHeader() {
        return header;
    }

    public void setHeader(HeaderInv header) {
        this.header = header;
    }

    @Override
    public String toString() {
        String result = "InvoiceLine{" + "itemName=" + name + ", itemprice=" + price + ", itemCount=" + count + '}';
        return result;
    }
    
    public double getLineTotal() {
        Double result =count * price;
        return result ;
    }
    
    public String getData() {
        String result = "" + getHeader().getNumInv() + "," + getName() + "," + getPrice() + "," + getCount();
        return result;
    }
    public LineInv(String name, double price, int count, HeaderInv header) {
        this.name = name;
        this.price = price;
        this.count = count;
        this.header = header;
    }
}
