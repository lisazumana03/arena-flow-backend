package za.co.lz.controller.team.finances;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import za.co.lz.domain.team.finances.Owner;
import za.co.lz.service.team.finances.IOwnerService;

@RestController
@RequestMapping("/api/owner")
public class OwnerController {
    @Autowired
    private IOwnerService ownerService;
    
    @PostMapping("/create")
    public ResponseEntity<Owner> createOwner(@RequestBody Owner owner) {
        Owner created = ownerService.create(owner);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Owner>> getAllOwners() {
        List<Owner> owners = ownerService.findAll();
        return ResponseEntity.ok(owners);
    }

    @PutMapping("/update")
    public ResponseEntity<Owner> updateOwner(@RequestBody Owner owner) {
        Owner updated = ownerService.update(owner, owner.getOwnerId());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{ownerId}")
    public ResponseEntity<Void> deleteOwner(@PathVariable UUID ownerId) {
        ownerService.delete(ownerId);
        return ResponseEntity.noContent().build();
    }

    //Owner Logic
    // Additional endpoints for buying/ selling a club, changing strategy, and updating reputation can be added here as needed.
    
}
