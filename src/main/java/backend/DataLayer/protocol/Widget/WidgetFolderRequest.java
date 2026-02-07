package backend.DataLayer.protocol.Widget;

public class WidgetFolderRequest
{
    private Integer id;
    private String folderName;

    public Integer getId()
    {
        return id;
    }
    public void setId(Integer id)
    {
        this.id = id;
    }
    public String getFolderName()
    {
        return folderName;
    }
    public void setFolderName(String folderName)
    {
        this.folderName = folderName;
    }
}
