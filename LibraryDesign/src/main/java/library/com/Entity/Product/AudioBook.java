package library.com.Entity.Product;

import library.com.Entity.Library;
import org.jetbrains.annotations.NotNull;

public class AudioBook extends Product{
    protected String authorName;
    protected double duration;
    protected String genre;



    public String getAuthorName() {
        return authorName;
    }

    public double getDuration() {
        return duration;
    }

    public String getGenre() {
        return genre;
    }

    public AudioBook(){

    }

    public AudioBook(@NotNull String authorName,@NotNull String name,@NotNull int copies,@NotNull double duration,@NotNull String genre ) {
        this.authorName = authorName;
        this.copies = copies;
        this.name = name;
        this.duration=duration;
        this.type = "AUDIOBOOK";
        this.genre =genre;
        this.id = authorName + "$" + name + "$" + genre + "AUDIOBOOK";
    };




}
