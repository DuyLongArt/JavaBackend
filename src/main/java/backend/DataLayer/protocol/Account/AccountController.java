package backend.DataLayer.protocol.Account;

import backend.DataLayer.protocol.Person.PersonEntity;
import backend.DataLayer.protocol.RoleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

//Router Layer
@RestController
@RequestMapping("/backend/account") // Đường dẫn cơ sở cho tất cả các API trong controller này
public class AccountController {

    private final AccountDAO accountDAO;

    @Autowired
    public AccountController(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    // SỬA LỖI 1: Sử dụng PathVariable để xác định tài nguyên cụ thể.
    // Đường dẫn đúng: /person/1

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    // SỬA LỖI 2: Tạo một endpoint riêng và rõ ràng hơn để lấy tên.
    // Đường dẫn đúng: /person/1/name
    @GetMapping("information")
    public ResponseEntity<AccountEntity> getInformation(@AuthenticationPrincipal UserDetails userDetails)
    {

        String currentUsername = userDetails.getUsername();

        // Now find the person based on the username/email instead of ID 1
//        PersonEntity person = personDAO.(currentUsername).orElse(null);
        AccountEntity account=accountDAO.findAccountEntityByUsername(currentUsername);
//        return ResponseEntity.ok(person);
        return ResponseEntity.ok(account);
    }

    @PostMapping("updaterole")
    public ResponseEntity<Void> editRole(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam RoleTypes role
    )
    {

        String currentUsername = userDetails.getUsername();

        // 1. Perform the update
        accountDAO.updateAccountRoleByUsername(currentUsername, role);

        // 2. Fetch the updated entity to return to the frontend
//        AccountEntity updatedAccount = accountDAO.findAccountEntityByUsername(currentUsername);

        return ResponseEntity.ok().build();
    }

}