import jdk.swing.interop.SwingInterOpUtils;
import library.com.Entity.Helper.BorrowedItem;
import library.com.Entity.Library;
import library.com.Entity.Product.AudioBook;
import library.com.Entity.Product.Newspaper;
import library.com.Entity.Product.Product;
import library.com.Entity.Product.TextBook;
import library.com.Entity.User.User;
//import library.com.Entity.LibraryItems.BooksEntity.Books;

//import library.com.Entity.User.User;

import javax.naming.StringRefAddr;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class mainApp {

    public static void main(String[] args) {
        /* Creating current date according to pattern */
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate currentDate = LocalDate.parse(LocalDate.now().toString(),formatter);

        /* installing books in database */

        //First of All I will create Library Class which consist of list of products and customers
        // userToProduct and productToUser relationship

        Library library = new Library();

        // First two lines after product creation shows that
         // if we add same product at different interval in library than it copies will get update if the product is already present

        AudioBook audioBook= new AudioBook("Peter theil","zero to one",5,25,"startup");
        library.save(audioBook);
        library.save(audioBook);
        AudioBook audioBook1 = new AudioBook("Chetan Bhagat","The Half Girlfriend",6,20,"fiction");
        library.save(audioBook1);


        TextBook textBook = new TextBook("Nitesh","Startup",6,144,"startup");
        TextBook textBook1 = new TextBook("Chetan Bhagat","The Half Girlfriend",6,220,"fiction");
        library.save(textBook);
        library.save(textBook);
        library.save(textBook1);

        Newspaper newspaper = new Newspaper("The Indian Express",currentDate.toString(),5);
        Newspaper newspaper1 = new Newspaper("The Hindu",currentDate.toString(),6);
        library.save(newspaper);
        library.save(newspaper);
        library.save(newspaper1);


        System.out.println("--------- Library get all items --------");
        library.getAllItems("ALLTYPE");
        System.out.println();

        System.out.println("------ Library items on the basis of genre ------");

        System.out.println();


        /* --- User start trading -- */
        User user = new User("Bhawak", "bhawakanand1996@gmail.com");
        library.saveUser(user);
        library.trading("BORROW","The Hindu",currentDate.toString(),5,user);

        // Upcoming two lines show that user borrow same type of book in different interval of time
        // in library
          /* 1. no of copies of product decreases
             2. in productToUser list customerid of customer is added in the list of product
             3. in userToProduct list borrowedItem(timestamp,productBorrowId,copies) is being added in the list of customer
          */
        library.trading("BORROW","Nitesh","Startup","startup",5,144,user);
        library.trading("BORROW","Nitesh","Startup","startup",5,144,user);

        /* Upcoming line show user return the item he borrowed */

        // here user come to return 6 no copies of product he borrowed from library but at different time interval
        // in class logic is there, we traverse productList of customerId and check productId matches with given productId
        // and if no copies of product is greater than required copies than no of copies of product decreases and required copies is zero we exit the loop
        // and if no copies of product is greater than required copies
        // than that particular borrowed item removed from the list of userToProduct customer list and required copies decreases and goes on

        // Than I will check if customer has that product with him if product is not there than I update my productToCustomerList
        // means that product is no longer in relation with that customer

        // After all this I also update my product copies in library

        library.trading("RETURN","Nitesh","Startup","startup",6,144,user);

        //library.customerProductRelationship();
        library.getAllItems("ALLTYPE");
        library.getAllUser();

        System.out.println(library.getAllItemsBasisOfGenre());

    }
}
