package library.com.Entity.Product;

import library.com.Entity.Library;
import org.jetbrains.annotations.NotNull;

public class TextBook extends Product{
    protected String authorName;
    protected int pages;
    protected String genre;

    public TextBook() {

    }

    public String getAuthorName() {
        return authorName;
    }

    public int getPages() {
        return pages;
    }

    public String getGenre() {
        return genre;
    }

    public TextBook(@NotNull String authorName,@NotNull String name,@NotNull int copies,@NotNull int pages,@NotNull String genre) {
        this.authorName = authorName;
        this.copies = copies;
        this.name = name;
        this.pages = pages;
        this.type = "TEXTBOOK";
        this.genre =genre;
        this.id = authorName + "$" + name + "$" + genre + "TEXTBOOK";
    };

}
