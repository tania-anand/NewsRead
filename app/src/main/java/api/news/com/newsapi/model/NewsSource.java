package api.news.com.newsapi.model;

/**
 * Created by taniaanand on 04/02/19.
 */

public class NewsSource {

    private String description;
    private String category;
    private String language;
    private String urlContent;
    private String id;
    private String country;
    private String name;


    @Override
    public String toString() {
        return "NewsSource{" +
                "description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", language='" + language + '\'' +
                ", urlContent='" + urlContent + '\'' +
                ", id='" + id + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
