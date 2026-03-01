package backend.DataLayer.protocol.Account;

import lombok.Data;
import java.util.Map;

@Data
public class SupabaseWebhookPayload {
    private String type;
    private String table;
    private String schema;
    private Map<String, Object> record;
    private Map<String, Object> old_record;
}
