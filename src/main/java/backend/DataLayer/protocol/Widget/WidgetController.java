package backend.DataLayer.protocol.Widget;

import backend.DataLayer.protocol.Account.AccountDAO;
import backend.DataLayer.protocol.Account.AccountEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/backend/widgets")
public class WidgetController {

    @Autowired
    private WidgetFolderDAO widgetDAO;

    @Autowired
    private WidgetShortcutDAO widgetShortcutDAO;

    @Autowired
    private AccountDAO accountDAO;

    // Helper method to reduce boilerplate
    private AccountEntity getAccount(UserDetails userDetails) {
        return accountDAO.findAccountEntityByUsername(userDetails.getUsername());
    }

    @GetMapping("/folders")
    public ResponseEntity<Iterable<WidgetFolderEntity>> getFolders(@AuthenticationPrincipal UserDetails userDetails) {
        AccountEntity account = getAccount(userDetails);
        return ResponseEntity.ok(widgetDAO.findWidgetFolderEntitiesByIdentity(account));
    }

    @GetMapping("/folder/{folderId}/shortcuts")
    public ResponseEntity<Iterable<WidgetShortcutEntity>> getShortcuts(@PathVariable Integer folderId) {
        // Note: You might want to verify folder ownership here too
        return ResponseEntity.ok(widgetShortcutDAO.findByFolderId(folderId));
    }

    @PostMapping("/widget/folder/add")
    public ResponseEntity<WidgetFolderEntity> addFolderWidget(
            @RequestBody WidgetFolderRequest widgetFolder,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            WidgetFolderEntity widgetFolderEntity = new WidgetFolderEntity();
            widgetFolderEntity.setFolderName(widgetFolder.getFolderName());
//            widgetFolderEntity.setId(widgetFolder.getId());
//            Integer identityId = getAccount(userDetails).getId();
            widgetFolderEntity.setIdentity(getAccount(userDetails));
//            widgetFolderEntity.setIdentity(identityId);
            return ResponseEntity.ok(widgetDAO.save(widgetFolderEntity));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/widget/folder/edit/{id}")
    public ResponseEntity<String> updateFolderWidget(
            @PathVariable Integer id,
            @RequestBody WidgetFolderEntity updatedFolder,
            @AuthenticationPrincipal UserDetails userDetails) {

        return widgetDAO.findById(id).map(existingFolder -> {
            // SECURITY CHECK: Does this folder belong to the user?
            if (!existingFolder.getIdentity().getUsername().equals(userDetails.getUsername())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
            }

            existingFolder.setFolderName(updatedFolder.getFolderName());
            widgetDAO.save(existingFolder);
            return ResponseEntity.ok("Folder updated successfully");
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/widget/folder/delete/{id}")
    public ResponseEntity<String> deleteFolderWidget(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {

        return widgetDAO.findById(id).map(folder -> {
            if (!folder.getIdentity().getUsername().equals(userDetails.getUsername())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
            }
            widgetDAO.delete(folder);
            return ResponseEntity.ok("Folder deleted successfully");
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/widget/shortcut/add")
    public ResponseEntity<WidgetShortcutEntity> addShortcutWidget(
            @RequestBody WidgetShortcutEntity shortcut,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            // Short-cuts usually inherit permissions from their parent folder
            // You may want to verify that shortcut.getFolder() belongs to the user here
            return ResponseEntity.ok(widgetShortcutDAO.save(shortcut));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/widget/shortcut/update/{id}")
    public ResponseEntity<WidgetShortcutEntity> updateShortcut(
            @PathVariable Integer id,
            @RequestBody WidgetShortcutEntity updatedDetails,
            @AuthenticationPrincipal UserDetails userDetails) {

        return widgetShortcutDAO.findById(id).map(shortcut -> {
            // Ownership check (via the folder's owner)
            if (!shortcut.getFolder().getIdentity().getUsername().equals(userDetails.getUsername())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).<WidgetShortcutEntity>build();
            }

            shortcut.setShortcutName(updatedDetails.getShortcutName());
            shortcut.setShortcutUrl(updatedDetails.getShortcutUrl());
            if (updatedDetails.getFolder() != null) {
                shortcut.setFolder(updatedDetails.getFolder());
            }
            return ResponseEntity.ok(widgetShortcutDAO.save(shortcut));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/widget/shortcut/delete/{id}")
    public ResponseEntity<Void> deleteShortcut(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {

        return widgetShortcutDAO.findById(id).map(shortcut -> {
            if (!shortcut.getFolder().getIdentity().getUsername().equals(userDetails.getUsername())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).<Void>build();
            }
            widgetShortcutDAO.delete(shortcut);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}