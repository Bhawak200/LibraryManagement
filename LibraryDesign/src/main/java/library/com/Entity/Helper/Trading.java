package library.com.Entity.Helper;

import library.com.Entity.Library;
import org.jetbrains.annotations.NotNull;

public class Trading {

    public void trading(@NotNull String tradeType,@NotNull int copies,@NotNull Library library,@NotNull String customerId,@NotNull String productId){
        double dueAmount = library.findDueAmountOfCustomer(customerId);
        int leftOverLimit = library.checkBorrowingLimitOfCustomer(customerId);
        if(leftOverLimit<copies && tradeType == "BORROW"){
            System.out.println("Sorry you demanding more than leftOverLimit");
            System.out.println("Your leftOverLimit is -:" + leftOverLimit);
            return;
        }

         /* First it will check due amount */
        if(dueAmount>0){
            System.out.println("Pay your due amount -:" + dueAmount);
            System.out.println("If you want to continue trading....");
            System.out.println("");
            return;
        }

        if(tradeType == "BORROW"){

            // productAvailability gives the count of copies of product in library
            int count = library.productAvailability(productId);
            if(count == 0){
                System.out.println("----- Sorry product you asking for is Out of Stock -----");
                System.out.println();
                return;
            };
            if(count<copies){
                System.out.println("Sorry no of copies of product you are asking for is not availabale, Copies avialble -:" + count);
                System.out.println("");
            };
            library.borrowBook(productId,customerId,copies);
        }else if(tradeType == "RETURN"){
            library.returnBook(productId,customerId,copies);
        }else{
            System.out.println("You enter wrong tradeType ....");
        }

    }
}
