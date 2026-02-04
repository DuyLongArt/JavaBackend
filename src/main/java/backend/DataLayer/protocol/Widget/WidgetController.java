package backend.DataLayer.protocol.Widget;

import backend.DataLayer.protocol.Person.PersonDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/widgets")
public class WidgetController
{
    @Autowired
    private  WidgetFolderDAO widgetDAO;
    @Autowired
    private  PersonDAO personDAO;

    @Autowired
    private  WidgetShortcutDAO widgetShortcutDAO;
    public WidgetController(WidgetFolderDAO widgetDAO)
    {
        this.widgetDAO = widgetDAO;

    }


    @PostMapping("/widget/folder/add")
    public ResponseEntity<WidgetFolderEntity> addFolderWidget(  @RequestBody WidgetFolderEntity widgetFolder)
    {
        WidgetFolderDAO widgetDAO = this.widgetDAO;
        try
        {
            WidgetFolderEntity savedFolder = widgetDAO.save(widgetFolder);
            return ResponseEntity.ok().body(savedFolder);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @DeleteMapping("/widgets/folder/delete/{id}")
    public ResponseEntity<String> deleteFolderWidget(@PathVariable Integer id)
    {
        try{
            widgetDAO.deleteById(id);
            return ResponseEntity.ok("Folder deleted successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("/widget/folder/edit/{id}")
    public ResponseEntity<String> updateFolderWidget(@PathVariable Integer id, @RequestBody WidgetFolderEntity updatedFolder)
    {
        try{
            WidgetFolderEntity existingFolder = widgetDAO.findById(id).orElse(null);
            if(existingFolder == null){
                return ResponseEntity.notFound().build();
            }
            existingFolder.setFolderName(updatedFolder.getFolderName());
            widgetDAO.save(existingFolder);
            return ResponseEntity.ok("Folder updated successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/widget/shortcut/add")
    public ResponseEntity<WidgetShortcutEntity> addShortcutWidget(  @RequestBody WidgetShortcutEntity shortcut)
    {
//        widgetDAO.
        try{
            WidgetShortcutEntity savedShortcut = widgetShortcutDAO.save(shortcut);
            return ResponseEntity.ok().body(savedShortcut);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
//        return ResponseEntity.ok().build();/
    }


    @PutMapping("/widgget/shortcut/update/{id}")
    public ResponseEntity<WidgetShortcutEntity> updateShortcut(
            @PathVariable Integer id,
            @RequestBody WidgetShortcutEntity updatedDetails) {

        return widgetShortcutDAO.findById(id).map(shortcut -> {
            shortcut.setShortcutName(updatedDetails.getShortcutName());
            shortcut.setShortcutUrl(updatedDetails.getShortcutUrl());

            // Handle folder update if provided
            if (updatedDetails.getFolder() != null) {
                shortcut.setFolder(updatedDetails.getFolder());
            }

            WidgetShortcutEntity saved = widgetShortcutDAO.save(shortcut);
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE: Remove a shortcut by ID
    @DeleteMapping("/widgget/shortcut/delete/{id}")
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
