package library.com.Entity;

import library.com.Entity.Helper.BorrowedItem;
import library.com.Entity.Helper.Trading;
import library.com.Entity.Product.AudioBook;
import library.com.Entity.Product.Newspaper;
import library.com.Entity.Product.Product;
import library.com.Entity.Product.TextBook;
import library.com.Entity.User.User;
import org.jetbrains.annotations.NotNull;


import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class Library {
    private Map<String,Product> Items = new HashMap<>();
    private Map<String, User> customers = new HashMap<>();
    private Map<String,ArrayList<BorrowedItem>> userToProduct = new HashMap<>();
    private Map<String, ArrayList<String>> productToUser = new HashMap<>();

   /* It will print products in library */

    // it will take parameter String
    //if parameter is
    // "AUDIOBOOK" - print audiobook items
    // "TEXTBOOK" -  print textBook items
    // "NEWSPAPER" - print newspaper items
    // "ALLTYPE" -   print all products

    public Map<String,Product> getAllItems(@NotNull String Type){
        Map<String,Product> items = new HashMap<>();
        Items.forEach((key,item)->{

            if((item.getType() == Type)){
                if(Type == "AUDIOBOOK") printItem((AudioBook) item);
                else if(Type == "TEXTBOOK") printItem((TextBook) item);
                else printItem((Newspaper) item);
                items.put(item.getId(),item);
            }else if(Type == "ALLTYPE"){
                if(item instanceof AudioBook)printItem((AudioBook) item);
                else if(item instanceof TextBook) printItem((TextBook) item);
                else printItem((Newspaper) item);
                items.put(item.getId(),item);

            }

            System.out.println();

        });
         return items;
    }

    private void printItem(AudioBook item) {
        System.out.println("{ Name -:" + item.getName());
        System.out.println("AuthorName -:" + item.getAuthorName());
        System.out.println("copies :" + item.getCopies());
        System.out.println("duration -:" + item.getDuration());
        System.out.println("type -:" + item.getType() +" }");
    }
    private void printItem(Newspaper item){
        System.out.println("{ Name -:" + item.getName());
        System.out.println("TimeStamp -:" + item.getTimeStamp());
        System.out.println("copies :" + item.getCopies());
        System.out.println("type -:" + item.getType() + " }");


    }
    private void printItem(TextBook item){
        System.out.println("{ Name -:" + item.getName());
        System.out.println("AuthorName -:" + item.getAuthorName());
        System.out.println("copies :" + item.getCopies());
        System.out.println("pages -:" + item.getPages());
        System.out.println("genre -:" + item.getGenre());
        System.out.println("type -:" + item.getType()+ " }");
    }

    // It will check product is avilable in library by looking out its copies
    public int productAvailability(@NotNull String productId){
        if(Items.containsKey(productId)==false) return 0;
        Product product = Items.get(productId);
        return product.getCopies();
    }



     /* Finding dueAmount of a customer */
    // it will take customerId as parameter
    public double findDueAmountOfCustomer(@NotNull String customerId) {
        AtomicReference<Double> dueAmount = new AtomicReference<>(0.0);
        if(userToProduct.containsKey(customerId)){
            LocalDate currentDate = LocalDate.now();
            ArrayList<BorrowedItem> products = userToProduct.get(customerId) ;
            products.forEach(borrowedItem -> {
             int duration = (int)ChronoUnit.DAYS.between(currentDate,borrowedItem.timestamp);
             if(duration>90) dueAmount.set((duration - 90) * 30.0 * borrowedItem.copies);
            });
        }


        return dueAmount.get();
    }


    /* Updating product and customer list according to tradeType */

    public void borrowBook(@NotNull String productId,@NotNull String customerId,@NotNull int copies) {

        BorrowedItem borrowedItem = new BorrowedItem();
        borrowedItem.copies = copies;
        borrowedItem.productId= productId;
        borrowedItem.timestamp = LocalDate.now();

        // checking product is already being assigned to anyone or not
        // if yes simply add customerId in the list
        // no create productId and customerId value pair
        if(productToUser.containsKey(productId)==false){
            ArrayList<String> customer = new ArrayList<>();
            customer.add(customerId);
            productToUser.put(productId,customer);
        }else{
            ArrayList<String> customers = productToUser.get(productId);
            if(customers.contains(customerId)==false) productToUser.get(productId).add(customerId);
        };

        // checking user is already has any book or not
        // if yes simply add borrowedItem(productId,timestamp,copies) in the list
        // no create customerId and borrowedItem(productId,timestamp,copies) value pair
        if(userToProduct.containsKey(customerId)==false){
            ArrayList<BorrowedItem> product = new ArrayList<>();
            product.add(borrowedItem);
            userToProduct.put(customerId,product);

        }else{
            userToProduct.get(customerId).add(borrowedItem);
        };
        // Here product copies is being updating in the list
        Product product =Items.get(productId);
        int oldCopiesPresent = product.getCopies();

        int newCopies = oldCopiesPresent;
        newCopies -= copies;
        if(product instanceof AudioBook){
            Product audioBook = new AudioBook(((AudioBook) product).getAuthorName(), ((AudioBook)product).getName(), newCopies, ((AudioBook) product).getDuration(), ((AudioBook)product).getGenre());
            Items.put(productId,audioBook);
        }else if(product instanceof TextBook){
            Product textBook = new TextBook(((TextBook) product).getAuthorName(), ((TextBook)product).getName(), newCopies, ((TextBook) product).getPages(), ((TextBook)product).getGenre());
            Items.put(productId,textBook);
        }else{
            Product newspaper = new Newspaper(((Newspaper)product).getName(),((Newspaper)product).getTimeStamp(),newCopies);
            Items.put(productId,newspaper);
        }
    }

    public void returnBook(@NotNull String productId,@NotNull String customerId,@NotNull int copies){
        if(productAvailability(productId)<=0){
            System.out.println("Sorry product is not available---");
            return;
        }
        int tempCopies = copies;
        // check userlist consist of customer or not
        if(userToProduct.containsKey(customerId)==false){
            System.out.println("Sorry you are not user of this library...");
            return;
        }

        // check productList consist of product or not
        if(productToUser.containsKey(productId) == false){
            System.out.println("Sorry this product is not for library");
            return;
        };

        //checking user belongs to product  and product belongs to user
        if((checkProductBelongsToUser(customerId,productId) == false) || (checkUserBelongsToPoduct(customerId,productId) == false)){
            System.out.println("Sorry this product don't belongs to you");
            System.out.println(checkProductBelongsToUser(customerId,productId));
            return;
        }

        ArrayList<BorrowedItem> borrowedItems = userToProduct.get(customerId);
        // sorting the product list on tha basis of timestamp
        Collections.
                sort(borrowedItems,
                        (borrowItem1,borrowedItem2) ->borrowItem1.timestamp.compareTo(borrowedItem2.timestamp));

        // logic of updating copies of borrowed item
        Iterator<BorrowedItem> itemIterator =borrowedItems.iterator();
        while(itemIterator.hasNext()){
            BorrowedItem borrowe = itemIterator.next();
            if(borrowe.productId.equals(productId)){
                if(borrowe.copies>tempCopies){
                    borrowe.copies -= tempCopies;
                    tempCopies=0;
                }else if(tempCopies>=borrowe.copies){
                    tempCopies -= borrowe.copies;
                    itemIterator.remove();
                }

            };
        }

        if(tempCopies>0){
            System.out.println("You are giving extra -:" + tempCopies +"copies");
        }

        if(checkProductBelongsToUser(customerId,productId)==false){
            ArrayList<String> customers = productToUser.get(productId);
            customers.remove(customerId);
        }

        // Here product copies is being updating in the list
        Product product =Items.get(productId);
        int oldCopiesPresent = product.getCopies();

        int newCopies = oldCopiesPresent;

        newCopies += copies;
        if(product instanceof AudioBook){
            Product audioBook = new AudioBook(((AudioBook) product).getAuthorName(), ((AudioBook)product).getName(), newCopies, ((AudioBook) product).getDuration(), ((AudioBook)product).getGenre());
            Items.put(productId,audioBook);
        }else if(product instanceof TextBook){
            Product textBook = new TextBook(((TextBook) product).getAuthorName(), ((TextBook)product).getName(), newCopies, ((TextBook) product).getPages(), ((TextBook)product).getGenre());
            Items.put(productId,textBook);
        }else{
            Product newspaper = new Newspaper(((Newspaper)product).getName(),((Newspaper)product).getTimeStamp(),newCopies);
            Items.put(productId,newspaper);
        }

    }

    /* It will check that customer has borrow that product or not */
    private boolean checkUserBelongsToPoduct(@NotNull String customerId,@NotNull String productId) {
        return productToUser.get(productId).contains(customerId);
    }

    /* It will check that product belongs to customer or not */
    private boolean checkProductBelongsToUser(@NotNull String customerId,@NotNull String productId) {
         ArrayList<BorrowedItem> borrowedItems = userToProduct.get(customerId);
         if(borrowedItems.size() == 0) return false;
         for(BorrowedItem borrowedItem : borrowedItems){
             if(borrowedItem.productId.equals(productId)) return true;
         };
         return false;
    }

    /* return all items based on genre */
    public Map<String,ArrayList<Product>> getAllItemsBasisOfGenre(){
        Map<String,ArrayList<Product>> items = new HashMap<>();
        Items.forEach((productId,product)->{

           if( product instanceof AudioBook){
               if(items.containsKey(((AudioBook) product).getGenre())) items.get(((AudioBook) product).getGenre()).add(product);
               else items.put(((AudioBook) product).getGenre(),new ArrayList<Product>(Collections.singleton(product)));
           }else if(product instanceof TextBook){
               if(items.containsKey(((TextBook) product).getGenre())) items.get(((TextBook) product).getGenre()).add(product);
               else items.put(((TextBook) product).getGenre(),new ArrayList<Product>(Collections.singleton(product)));
            }
        });
       return  items;
    }

   // get all items related to genre
   // it will take genre as type
   public void getItemsRelatedToGenre(@NotNull String genre){
       Items.forEach((productId,product)->{
           if( product instanceof AudioBook && ((AudioBook) product).getGenre().compareTo(genre)==1){
              printItem((AudioBook) product);
           }else if(product instanceof TextBook  && ((TextBook) product).getGenre().compareTo(genre)==1){
               printItem((TextBook) product);
           }
       });
   }


    /* Print which customerId (customer) borrow which productId (product) with timeStamp and copies */
    public void customerProductRelationship(){
        userToProduct.forEach((customerId,borrowedItems) ->{

            borrowedItems.forEach(borrowedItem -> {
                System.out.println("CustomerId -:" + customerId);
                System.out.println("Product Id -:" + borrowedItem.productId);
                System.out.println("copies -:" + borrowedItem.copies);
                System.out.print("Date of borrow -:" + borrowedItem.timestamp);
            });

        });
    }


    public int checkBorrowingLimitOfCustomer(String customerId) {
        int haveCopies;
        Product product = new Product();
        int limit = product.limit;
        ArrayList<BorrowedItem> products = userToProduct.get(customerId);
        if(products==null) return limit;
        haveCopies = products.stream().mapToInt(borrowedItem -> borrowedItem.copies).sum();
        int leftOverLimit = limit - haveCopies;
        return leftOverLimit;
    }

    //Trading(BORROW OR RETURN) of AUDIOBOOK
    public void trading(@NotNull String tradeType,@NotNull  String authorName,@NotNull String name,@NotNull String genre,@NotNull int copies,@NotNull  double duration ,@NotNull User user){
        String id = authorName + "$" + name + "$" + genre + "AUDIOBOOK";
        String customerId = user.getCustomerId();
        Trading trading = new Trading();
        trading.trading(tradeType,copies,this,customerId,id);
    };


    //Trading(BORROW OR RETURN) of TEXTBOOK
    public  void trading(@NotNull String tradeType,@NotNull String authorName,@NotNull String name,@NotNull String genre,@NotNull int copies,@NotNull int pages,@NotNull User user){
        String id = authorName + "$" + name + "$" + genre + "TEXTBOOK";
        String customerId = user.getCustomerId();
        Trading trading = new Trading();
        trading.trading(tradeType,copies,this,customerId,id);
    }


    //Trading(BORROW OR RETURN) of NEWSPAPER
    public  void trading(@NotNull String tradeType,@NotNull String name,@NotNull String timestamp,@NotNull int copies,@NotNull User user){
        String id =    name + "$" + timestamp + "NEWSPAPER";
        String customerId = user.getCustomerId();
        Trading trading = new Trading();
        trading.trading(tradeType,copies,this,customerId,id);
    }



 /* Saving a product in library */
    public  void save(Product product){
        String productId="";
        if(product instanceof  AudioBook) {
            productId = ((AudioBook)product).getAuthorName() + "$" +
                        ((AudioBook)product).getName()+ "$" + ((AudioBook)product).getGenre()
                        + ((AudioBook)product).getType();

        }else if(product instanceof Newspaper){
            productId = ((Newspaper)product).getName() + "$" +
                    ((Newspaper)product).getTimeStamp()
                    + ((Newspaper)product).getType();
        }else if(product instanceof TextBook){
            productId = ((TextBook)product).getAuthorName() + "$" +
                    ((TextBook)product).getName()+ "$" + ((TextBook)product).getGenre()
                    + ((TextBook)product).getType();
        }else{
            System.out.println("This type of product is not in this library");
            return;
        }
        if(Items.containsKey(productId)){
            Product item = Items.get(productId);
            int newCopies = item.getCopies() + product.getCopies();
            if(item instanceof AudioBook) {
                product = new AudioBook( ((AudioBook)product).getAuthorName(),
                                         ((AudioBook)product).getName(),
                                           newCopies,
                                         ((AudioBook)product).getDuration(),
                                         ((AudioBook)product).getGenre()
                                       );
            }else if(item instanceof TextBook){
                product = new TextBook(   ((TextBook)product).getAuthorName(),
                                            ((TextBook)product).getName(),
                                            newCopies,
                                            ((TextBook)product).getPages(),
                                            ((TextBook)product).getGenre()
                                        );
            }else{
                product = new Newspaper(
                                          ((Newspaper)product).getName(),
                                          ((Newspaper)product).getTimeStamp(),
                                           newCopies
                                        );
            }

        }
        Items.put(productId,product);

    }

    public void saveUser(User user) {
        if(customers.containsKey(user.getCustomerId())){
            System.out.println("Username - :" + user.getUserName() + " already present");
            return;
        };
        customers.put(user.getCustomerId(),user);
    };
    public Map<String,User> getAllUser(){
        Map<String,User> users = new HashMap<>();
        customers.forEach((userId, user) -> {
            System.out.println("User name -: " + user.getUserName());
            System.out.println("User name-: " + user.getEmailId());
            ArrayList<BorrowedItem> products = userToProduct.get(user.getCustomerId());
            if(products==null) System.out.println("Borrowed Items -: " + 0);
            else{
                int copies=products.stream().mapToInt(borrowedItem -> borrowedItem.copies).sum();
                System.out.println("Borrowed Items -:" + copies);
            }

            users.put(userId,user);
        });
        return customers;
    }

    public Map<String,ArrayList<String>> getUserBorrowedItems(String customerId){
        Map<String,ArrayList<String>>  items = new HashMap<>();
        ArrayList<BorrowedItem> borrowedItems = userToProduct.get(customerId);
        System.out.println(borrowedItems.size());
        if(borrowedItems.size()==0) return items;
        for (BorrowedItem borrowedItem : borrowedItems) {
            if(items.containsKey(borrowedItem.productId)){
               int copies = Integer.valueOf(items.get(borrowedItem.productId).get(1));
               items.get(borrowedItem.productId).set(1,String.valueOf(copies + borrowedItem.copies));
            }else{
                ArrayList<String> productDetails = new ArrayList<>();
                productDetails.add(Items.get(borrowedItem.productId).getName());
                productDetails.add(String.valueOf(Items.get(borrowedItem.productId).getCopies()));
                items.put(borrowedItem.productId, productDetails);
                System.out.println(1);
            }
        }
        System.out.println(items);
        return items;
    }
}


