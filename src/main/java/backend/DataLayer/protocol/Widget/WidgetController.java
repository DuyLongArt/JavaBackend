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
    private  WidgetDAO widgetDAO;
    @Autowired
    private  PersonDAO personDAO;

    public WidgetController(WidgetDAO widgetDAO)
    {
        this.widgetDAO = widgetDAO;

    }

    @GetMapping("/shortcut/all")
    public ResponseEntity<ShortcutEntity> viewWidget(    @AuthenticationPrincipal UserDetails userDetails)
    {


        Integer identity= personDAO.findPersonEntityByUserName(userDetails.getUsername()).getId();

        System.out.println("Person ID: "+identity);

       ShortcutEntity result= widgetDAO.getAllShortcut(identity);
        if(result == null){
            return ResponseEntity.notFound().build();
    }
        return ResponseEntity.ok(result);
    }
    @PostMapping("/shortcut/add")
    public ResponseEntity<ShortcutEntity> addWidget(  @RequestBody ShortcutEntity shortcut,
    Authentication authentication)
    {

        String currentUsername = authentication.getName();
        Integer identity= personDAO.findPersonEntityByUserName(currentUsername).getId();
        shortcut.setIdentity(personDAO.findPersonEntityByUserName(currentUsername));
        ShortcutEntity savedShortcut = widgetDAO.save(shortcut);
        return ResponseEntity.ok(savedShortcut);
    }

}
