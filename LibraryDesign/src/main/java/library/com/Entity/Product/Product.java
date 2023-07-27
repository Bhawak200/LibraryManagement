package library.com.Entity.Product;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Product {

    /* --- General fields  --- */
    // item id
    protected String id;
    // item type like audio book, textbook, newspaper...
    protected String type;
    // item name
    protected String name;
    //item count
    protected int copies;

    public final int limit=15;

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getCopies(){
        return copies;
    }






}
