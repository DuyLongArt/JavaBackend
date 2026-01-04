package backend.DataLayer.protocol.Project.MultiOwner;

import jakarta.persistence.*;
import java.util.Arrays;
import java.util.List;

@Entity
public class ApprovalRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Stores the state like [1, 1, 1, 1]
    // Using a List or Array of integers
    @ElementCollection
    private List<Integer> steps = Arrays.asList(1, 1, 1, 1);

    public List<Integer> getSteps() {
        return steps;
    }

    public void setSteps(List<Integer> steps) {
        this.steps = steps;
    }
}