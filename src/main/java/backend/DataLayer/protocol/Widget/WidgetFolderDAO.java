package backend.DataLayer.protocol.Widget;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WidgetFolderDAO extends JpaRepository<WidgetFolderEntity, Integer>
{
    // Spring automatically creates Save, Delete, FindByID, etc.

    // You can add custom queries here:

}



