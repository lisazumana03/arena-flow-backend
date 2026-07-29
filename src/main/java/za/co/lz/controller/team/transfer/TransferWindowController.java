package za.co.lz.controller.team.transfer;

import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RestController;
import za.co.lz.domain.team.transfer.TransferWindow;
import za.co.lz.service.team.transfer.ITransferWindowService;

@RestController
@RequestMapping("/api/transfer-window")
public class TransferWindowController {

    @Autowired
    private ITransferWindowService transferWindowService;

    @PostMapping("/create")
    public ResponseEntity<TransferWindow> createWindow(@RequestBody TransferWindow window) {
        return ResponseEntity.ok(transferWindowService.create(window));
    }

    @GetMapping("/all")
    public ResponseEntity<List<TransferWindow>> getAllWindows() {
        return ResponseEntity.ok(transferWindowService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<TransferWindow>> getWindowById(@PathVariable UUID id) {
        return ResponseEntity.ok(transferWindowService.findById(id));
    }

    // The board polls this to know whether to allow finalize actions right now.
    @GetMapping("/open")
    public ResponseEntity<List<TransferWindow>> getOpenWindows() {
        return ResponseEntity.ok(transferWindowService.findOpenWindows(LocalDate.now()));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TransferWindow> updateWindow(@PathVariable UUID id, @RequestBody TransferWindow window) {
        return ResponseEntity.ok(transferWindowService.update(window, id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteWindow(@PathVariable UUID id) {
        transferWindowService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
