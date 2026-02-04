package backend.DataLayer.protocol.Widget;

import backend.DataLayer.protocol.Account.AccountDAO;
import backend.DataLayer.protocol.Account.AccountEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/backend/widgets")
public class WidgetController {
    @Autowired
    private WidgetFolderDAO widgetDAO;

    @Autowired
    private WidgetShortcutDAO widgetShortcutDAO;

    @Autowired
    private AccountDAO accountDAO;

    @GetMapping("/folders")
    public ResponseEntity<Iterable<WidgetFolderEntity>> getFolders() {
        return ResponseEntity.ok(widgetDAO.findAll());
    }

    @GetMapping("/folder/{folderId}/shortcuts")
    public ResponseEntity<Iterable<WidgetShortcutEntity>> getShortcuts(@PathVariable Integer folderId) {
        return ResponseEntity.ok(widgetShortcutDAO.findByFolderId(folderId));
    }

    @PostMapping("/widget/folder/add")
    public ResponseEntity<WidgetFolderEntity> addFolderWidget(
            @RequestBody WidgetFolderEntity widgetFolder,
            Authentication authentication) {
        try {
            if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
                String username = ((UserDetails) authentication.getPrincipal()).getUsername();
                AccountEntity account = accountDAO.findAccountEntityByUsername(username);
                widgetFolder.setIdentity(account);
            }
            WidgetFolderEntity savedFolder = widgetDAO.save(widgetFolder);
            return ResponseEntity.ok().body(savedFolder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/widget/folder/delete/{id}")
    public ResponseEntity<String> deleteFolderWidget(@PathVariable Integer id) {
        try {
            widgetDAO.deleteById(id);
            return ResponseEntity.ok("Folder deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/widget/folder/edit/{id}")
    public ResponseEntity<String> updateFolderWidget(@PathVariable Integer id,
            @RequestBody WidgetFolderEntity updatedFolder) {
        try {
            WidgetFolderEntity existingFolder = widgetDAO.findById(id).orElse(null);
            if (existingFolder == null) {
                return ResponseEntity.notFound().build();
            }
            existingFolder.setFolderName(updatedFolder.getFolderName());
            widgetDAO.save(existingFolder);
            return ResponseEntity.ok("Folder updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/widget/shortcut/add")
    public ResponseEntity<WidgetShortcutEntity> addShortcutWidget(
            @RequestBody WidgetShortcutEntity shortcut,
            Authentication authentication) {
        try {
            WidgetShortcutEntity savedShortcut = widgetShortcutDAO.save(shortcut);
            return ResponseEntity.ok().body(savedShortcut);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/widget/shortcut/update/{id}")
    public ResponseEntity<WidgetShortcutEntity> updateShortcut(
            @PathVariable Integer id,
            @RequestBody WidgetShortcutEntity updatedDetails) {

        return widgetShortcutDAO.findById(id).map(shortcut -> {
            shortcut.setShortcutName(updatedDetails.getShortcutName());
            shortcut.setShortcutUrl(updatedDetails.getShortcutUrl());

            if (updatedDetails.getFolder() != null) {
                shortcut.setFolder(updatedDetails.getFolder());
            }

            WidgetShortcutEntity saved = widgetShortcutDAO.save(shortcut);
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/widget/shortcut/delete/{id}")
    public ResponseEntity<Void> deleteShortcut(@PathVariable Integer id) {
        try {
            if (widgetShortcutDAO.existsById(id)) {
                widgetShortcutDAO.deleteById(id);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
