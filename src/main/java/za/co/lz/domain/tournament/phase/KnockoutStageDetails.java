package za.co.lz.domain.tournament.phase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import za.co.lz.domain.tournament.round.Round;

@Entity
public class KnockoutStageDetails {
    private UUID knockoutStageId;
    private Stage stage;
    private UUID championId;
    @OneToMany(mappedBy = "knockoutStageDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("roundNumber ASC")
    private List<Round> rounds = new ArrayList<>();
    
}
