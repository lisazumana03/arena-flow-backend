package za.co.lz.service.team.transfer;

import za.co.lz.domain.team.transfer.TransferWindow;
import za.co.lz.service.IService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ITransferWindowService extends IService<TransferWindow, UUID> {

    // Whichever window (if any) is open on the given date
    List<TransferWindow> findOpenWindows(LocalDate date);
}
