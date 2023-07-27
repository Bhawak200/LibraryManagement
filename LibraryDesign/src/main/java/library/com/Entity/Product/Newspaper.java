package library.com.Entity.Product;

import library.com.Entity.Library;
import org.jetbrains.annotations.NotNull;

public class Newspaper  extends Product{
    protected String timeStamp;

    public String getTimeStamp() {
        return timeStamp;
    }
    public  Newspaper(){

    }
    public Newspaper(@NotNull String name,@NotNull String timeStamp,@NotNull int copies){
        this.name =name;
        this.timeStamp = timeStamp;
        this.type = "NEWSPAPER";
        this.copies=copies;
        this.id = name + "$" + timeStamp + "NEWSPAPER";
    };

}
