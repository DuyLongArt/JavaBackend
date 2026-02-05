package backend.DataLayer.protocol.Widget;

import backend.DataLayer.protocol.Account.AccountEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface WidgetFolderDAO extends JpaRepository<WidgetFolderEntity, Integer>
{
    List<WidgetFolderEntity> findWidgetFolderEntitiesByIdentity(AccountEntity identity);
    // Spring automatically creates Save, Delete, FindByID, etc.

    // You can add custom queries here:
//    @Query("SELECT wf FROM WidgetFolderEntity wf WHERE wf.identity = ?1")
//    List<WidgetFolderEntity> findWidgetFolderByEntityId(@Param("entityId") Integer entityId);
//
//    Iterable<WidgetFolderEntity> findWidgetFolderEntitiesByIdentity(AccountEntity identity);
}



