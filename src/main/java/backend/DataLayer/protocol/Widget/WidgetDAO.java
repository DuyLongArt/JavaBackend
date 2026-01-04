package backend.DataLayer.protocol.Widget;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;



@Repository
public interface WidgetDAO extends JpaRepository<ShortcutEntity, Integer>
{
    // Spring automatically creates Save, Delete, FindByID, etc.

    // You can add custom queries here:
   @Query("SELECT s FROM ShortcutEntity s  WHERE  s.identity.id=:identity")
    ShortcutEntity getAllShortcut(Integer identity);
}