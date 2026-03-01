package backend.DataLayer.protocol.Account;

import backend.DataLayer.protocol.Person.PersonDAO;
import backend.DataLayer.protocol.Person.PersonEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/backend/webhook/supabase")
public class SupabaseWebhookController {

    private final PersonDAO personDAO;

    public SupabaseWebhookController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @PostMapping("/sync-person")
    public ResponseEntity<?> syncPerson(@RequestBody SupabaseWebhookPayload payload) {
        if (payload == null || payload.getRecord() == null) {
            return ResponseEntity.badRequest().body("Invalid webhook payload");
        }

        Map<String, Object> record = payload.getRecord();
        String idAlias = (String) record.get("id"); // Supabase UUID
        if (idAlias == null || idAlias.isBlank()) {
            return ResponseEntity.badRequest().body("Record ID missing");
        }

        // Extract fields from public.persons
        String firstName = (String) record.get("first_name");
        String lastName = (String) record.get("last_name");
        String profileImageUrl = (String) record.get("profile_image_url");

        // Use findByAlias properly
        PersonEntity person = personDAO.findByAlias(idAlias);

        if (person == null) {
            // New person created from Supabase (e.g. from OAuth if /sync hasn't fired yet)
            person = new PersonEntity();
            person.setAlias(idAlias);
            person.setIsActive(true);
        }

        if (firstName != null) person.setFirstName(firstName);
        if (lastName != null) person.setLastName(lastName);
        if (profileImageUrl != null) person.setProfileImageUrl(profileImageUrl);

        personDAO.saveAndFlush(person);

        return ResponseEntity.ok("Successfully synced person " + idAlias);
    }
}
