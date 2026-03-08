package backend.DataLayer.protocol.Health;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyMetricsId implements Serializable {
    private LocalDate date;
    private Integer identityId;
}
