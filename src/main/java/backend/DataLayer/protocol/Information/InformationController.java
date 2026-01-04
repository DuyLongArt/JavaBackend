package backend.DataLayer.protocol.Information;

import backend.DataLayer.protocol.Person.PersonDAO;
import backend.DataLayer.protocol.Person.PersonEntity;
import jakarta.transaction.Transactional;
import kotlin.ParameterName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/backend/information/")

public class InformationController
{
    @Autowired
    private InformationDAO informationDAO;

    //    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    @Transactional
    @GetMapping("details")
    public ResponseEntity<InformationEntity> getInformation(@AuthenticationPrincipal UserDetails userDetails)
    {

        String currentUsername = userDetails.getUsername();

        System.out.println("currentUsername : " + currentUsername);
        InformationEntity information = informationDAO.findInformationByUserName(currentUsername);
        System.out.println("information : " + information);
//        return ResponseEntity.ok(person);
        if(information == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(information);

    }

    @Transactional
    @PostMapping("edit")
    public ResponseEntity<InformationEntity> edit(
            @RequestParam String university, // Changed ParameterName to RequestParam
            @RequestParam String location,
    @AuthenticationPrincipal UserDetails userDetails
    )
    {

        String currentUsername = userDetails.getUsername();

        // 1. Perform the update
        informationDAO.updateInformation(university, location, currentUsername);

        // 2. Fetch the updated entity to return to the frontend
        InformationEntity updatedInfo = informationDAO.findInformationByUserName(currentUsername);

        return ResponseEntity.ok(updatedInfo);
    }

}
