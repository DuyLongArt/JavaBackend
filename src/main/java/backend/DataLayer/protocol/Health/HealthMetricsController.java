package backend.DataLayer.protocol.Health;

import backend.DataLayer.protocol.Person.PersonDAO;
import backend.DataLayer.protocol.Person.PersonEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/backend/health")
public class HealthMetricsController {

    @Autowired
    private DailyMetricsDAO dailyMetricsDAO;

    @Autowired
    private PersonDAO personDAO;

    @GetMapping("/metrics/daily")
    public ResponseEntity<DailyMetricsEntity> getDailyMetrics(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String date) {
        
        if (userDetails == null) return ResponseEntity.status(401).build();
        
        PersonEntity person = personDAO.findPersonEntityByUserName(userDetails.getUsername());
        if (person == null) return ResponseEntity.notFound().build();

        LocalDate targetDate = (date != null) ? LocalDate.parse(date) : LocalDate.now();
        
        return dailyMetricsDAO.findByIdentityIdAndDate(person.getId(), targetDate)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping("/metrics/hydration/add")
    public ResponseEntity<DailyMetricsEntity> addHydration(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Integer amountMl) {
        
        if (userDetails == null) return ResponseEntity.status(401).build();
        
        PersonEntity person = personDAO.findPersonEntityByUserName(userDetails.getUsername());
        if (person == null) return ResponseEntity.notFound().build();

        LocalDate today = LocalDate.now();
        DailyMetricsEntity metrics = dailyMetricsDAO.findByIdentityIdAndDate(person.getId(), today)
                .orElseGet(() -> new DailyMetricsEntity(person, today));

        metrics.setWaterMl(metrics.getWaterMl() + amountMl);
        dailyMetricsDAO.save(metrics);
        
        return ResponseEntity.ok(metrics);
    }
}
