package za.co.lz.repository.team.transfer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.lz.domain.team.transfer.TransferWindow;
import za.co.lz.domain.team.transfer.TransferWindowType;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransferWindowRepository extends JpaRepository<TransferWindow, UUID> {

    List<TransferWindow> findByYear(int year);

    List<TransferWindow> findByType(TransferWindowType type);
}
