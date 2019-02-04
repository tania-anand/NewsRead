package api.news.com.newsapi.model;

/**
 * Created by taniaanand on 03/02/19.
 */

public class News {

    private String tittle;
    private String description;
    private String date;
    private String urlImage;
    private String urlContent;
    private String id;
    private String name;

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getUrlContent() {
        return urlContent;
    }

    public void setUrlContent(String urlContent) {
        this.urlContent = urlContent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "\nNews{" +
                "tittle='" + tittle + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", urlImage='" + urlImage + '\'' +
                ", urlContent='" + urlContent + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
