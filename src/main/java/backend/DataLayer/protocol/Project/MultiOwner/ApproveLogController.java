//package backend.DataLayer.protocol.Project.MultiOwner;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/backend/iot")
//public class ApproveLogController
//{
//
//    @Autowired
//    private ApproveProcessServices approvalService;
//
//    // POST /api/approval/5/action?step=2&status=APPROVE
//    @PostMapping("/{logid}/action")
//    public ResponseEntity<?> action(
//            @PathVariable Long logid,
//            @RequestParam int step,
//            @RequestParam String status // "APPROVE" or "REJECT"
//    )
//    {
//        try
//        {
//            boolean isApproved = "APPROVE".equalsIgnoreCase(status);
//            ApprovalRequest updated = approvalService.processApproval(logid, step, isApproved);
//            return ResponseEntity.ok(updated);
//        } catch (Exception e)
//        {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//}