package backend.DataLayer.protocol.System;

import backend.DataLayer.protocol.Person.PersonDAO;
import backend.DataLayer.protocol.Person.PersonEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/backend/system")
public class SystemScoringController {

    @Autowired
    private ScoreDAO scoreDAO;

    @Autowired
    private QuestDAO questDAO;

    @Autowired
    private PersonDAO personDAO;

    @GetMapping("/scores")
    public ResponseEntity<ScoreEntity> getScores(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) return ResponseEntity.status(401).build();
        
        PersonEntity person = personDAO.findPersonEntityByUserName(userDetails.getUsername());
        if (person == null) return ResponseEntity.notFound().build();

        return scoreDAO.findById(person.getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/quests")
    public ResponseEntity<List<QuestEntity>> getQuests(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) Boolean completed) {
        
        if (userDetails == null) return ResponseEntity.status(401).build();
        
        PersonEntity person = personDAO.findPersonEntityByUserName(userDetails.getUsername());
        if (person == null) return ResponseEntity.notFound().build();

        List<QuestEntity> quests;
        if (completed != null) {
            quests = questDAO.findByPersonIdAndIsCompleted(person.getId(), completed);
        } else {
            quests = questDAO.findByPersonId(person.getId());
        }
        
        return ResponseEntity.ok(quests);
    }
}
