import library.com.Entity.Helper.BorrowedItem;
import library.com.Entity.Library;
import library.com.Entity.Product.AudioBook;
import library.com.Entity.Product.Product;
import library.com.Entity.User.User;
import org.junit.gen5.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.gen5.api.Assertions.*;

public class LibrarayTest {
    Library library;
    @BeforeEach
    void libraryCreaton(){
        library = new Library();
    }
    @Test
    void createProduct(){
        AudioBook audioBook = new AudioBook("Peter Theil","Zero To One",5,25,"startup");
        library.save(audioBook);
        library.save(audioBook);
        Map<String, Product> items=  library.getAllItems("ALLTYPE");
        Assertions.assertEquals(1,library.getAllItems("ALLTYPE").size());
        String productId = audioBook.getId();
        assertEquals(10,items.get(productId).getCopies(),"The copies is -:" + items.get(productId).getCopies());
    };
    @Test
    void createUser(){
        User user=  new User("Bhawak20","bhawakanand@gmail.com");
        library.saveUser(user);
        assertEquals(1,library.getAllUser().size());
    }

    @Test
    void returnAndBorrowProduct(){
        AudioBook audioBook = new AudioBook("Peter Theil","Zero To One",5,25.0,"startup");
        String productId = audioBook.getId();
        library.save(audioBook);
        library.save(audioBook);
        User user = new User("Bhawak20", "bhawakanad1996@gmail.com");
        library.saveUser(user);
        library.trading("BORROW","Peter Theil","Zero To One","startup",8,25.0, user);
        library.trading("RETURN","Peter Theil","Zero To One","startup",3,25.0, user);
        Map<String, Product> items=  library.getAllItems("ALLTYPE");
        assertEquals(5,items.get(productId).getCopies(),"The copies is -:" + items.get(productId).getCopies());
        int val = library.getUserBorrowedItems(user.getCustomerId()).size();
        assertEquals(1,val);

    }
}
