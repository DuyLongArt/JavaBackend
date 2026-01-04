//package backend.DataLayer.protocol.Project.MultiOwner;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Objects;
//
//@Service
//public class ApproveProcessServices {
//
//    @Autowired
//    private ApprovalDAO approvalDAO;
//
//    @Transactional
//    public ApprovalRequest processNextStep(Long requestId, boolean isApproved, String currentUserAlias) {
//
//        // 1. Fetch the request
//        ApprovalRequest request = approvalDAO.findById(requestId)
//                .orElseThrow(() -> new RuntimeException("Request not found"));
//
//        List<Integer> steps = request.getSteps();
//        List<String> assignedAliases = request.getAssignedAliases(); // Assuming this field exists
//
//        int targetIndex = -1;
//
//        // 2. Find the active step
//        for (int i = 0; i < steps.size(); i++) {
//            int status = steps.get(i);
//
//            if (status == 3) {
//                throw new IllegalStateException("Process terminated. Step " + (i + 1) + " was rejected.");
//            }
//            if (status == 1) {
//                targetIndex = i;
//                break;
//            }
//        }
//
//        if (targetIndex == -1) {
//            throw new IllegalStateException("Process is already fully completed.");
//        }
//
//        // --- NEW: SECURITY CHECK ---
//
//        // Get the person assigned to this specific step
//        String assignedUser = assignedAliases.get(targetIndex);
//
//        // Compare with the person currently logged in
//        if (!Objects.equals(assignedUser, currentUserAlias)) {
//            throw new SecurityException("Permission Denied: Step " + (targetIndex + 1) +
//                    " is assigned to '" + assignedUser + "', but you are '" + currentUserAlias + "'.");
//        }
//
//        // --- UPDATE ---
//
//        int newStatus = isApproved ? 2 : 3;
//        steps.set(targetIndex, newStatus);
//
//        request.setSteps(steps);
//        return approvalDAO.save(request);
//    }
//}