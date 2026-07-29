package za.co.lz.repository.team.transfer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.lz.domain.team.transfer.Transfer;
import za.co.lz.domain.team.transfer.TransferStatus;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, UUID> {

    List<Transfer> findByWindow_WindowId(UUID windowId);

    List<Transfer> findByStatus(TransferStatus status);

    List<Transfer> findByBuyingTeam_TeamId(UUID teamId);

    List<Transfer> findBySellingTeam_TeamId(UUID teamId);

    List<Transfer> findByPlayer_PlayerId(UUID playerId);
}
