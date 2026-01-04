package backend.DataLayer.protocol.Url;

public interface UrlStructure
{
    int getId();

    String getUrlValue();

    String getHost();
    String getPath();

    void setPath(String path);

    void setHost(String host);

    void setUrlValue(String urlValue);
}
