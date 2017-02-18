package flash.hippo.com.some;

/**
 * Created by stpl on 6/2/17.
 */

public class ListModel {

    private  String title="";
    private  String content="";
    private  String datetime="";

    /*********** Set Methods ******************/

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public void setDatetime(String datetime)
    {
        this.datetime = datetime;
    }

    /*********** Get Methods ****************/

    public String getTitle()
    {
        return this.title;
    }

    public String getContent()
    {
        return this.content;
    }

    public String getDatetime()
    {
        return this.datetime;
    }
}
