package backend.DataLayer.protocol.Widget;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WidgetShortcutDAO extends JpaRepository<WidgetShortcutEntity, Integer> {

    java.util.List<WidgetShortcutEntity> findByFolderId(Integer folderId);
}
