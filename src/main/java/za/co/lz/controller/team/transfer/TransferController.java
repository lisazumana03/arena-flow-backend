package za.co.lz.controller.team.transfer;

import java.util.List;
import java.util.Optional;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import za.co.lz.domain.team.transfer.Transfer;
import za.co.lz.domain.team.transfer.TransferStatus;
import za.co.lz.service.team.transfer.ITransferService;

@RestController
@RequestMapping("/api/transfer")
public class TransferController {

    @Autowired
    private ITransferService transferService;

    @PostMapping("/create")
    public ResponseEntity<Transfer> createTransfer(@RequestBody Transfer transfer) {
        return ResponseEntity.ok(transferService.create(transfer));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Transfer>> getAllTransfers() {
        return ResponseEntity.ok(transferService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Transfer>> getTransferById(@PathVariable UUID id) {
        return ResponseEntity.ok(transferService.findById(id));
    }

    @GetMapping("/window/{windowId}")
    public ResponseEntity<List<Transfer>> getTransfersByWindow(@PathVariable UUID windowId) {
        return ResponseEntity.ok(transferService.findByWindow(windowId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Transfer>> getTransfersByStatus(@PathVariable TransferStatus status) {
        return ResponseEntity.ok(transferService.findByStatus(status));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Transfer> updateTransfer(@PathVariable UUID id, @RequestBody Transfer transfer) {
        return ResponseEntity.ok(transferService.update(transfer, id));
    }

    // Advance to the next reliability tier, or DEAL_COLLAPSED. Rejects skipped tiers.
    @PutMapping("/{id}/status")
    public ResponseEntity<Transfer> advanceStatus(@PathVariable UUID id, @RequestParam TransferStatus next) {
        return ResponseEntity.ok(transferService.advanceStatus(id, next));
    }

    // Runs the finalize saga: window check, budget spend, squad swap, player re-point.
    @PostMapping("/{id}/finalize")
    public ResponseEntity<Transfer> finalizeTransfer(
            @PathVariable UUID id,
            @RequestParam UUID seasonId,
            @RequestParam UUID buyingTeamBudgetId,
            @RequestParam int newKitNumber) {
        return ResponseEntity.ok(transferService.finalizeTransfer(id, seasonId, buyingTeamBudgetId, newKitNumber));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTransfer(@PathVariable UUID id) {
        transferService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
