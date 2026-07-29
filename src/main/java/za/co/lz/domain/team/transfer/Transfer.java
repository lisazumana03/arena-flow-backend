package za.co.lz.domain.team.transfer;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.Team;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Represents a single transfer deal for a Player between two Teams, tracked
 * through its reliability tiers (see TransferStatus) from first rumour to
 * OFFICIAL confirmation - or DEAL_COLLAPSED if it falls through.
 *
 * sellingTeam is null for free agents (TransferType.FREE).
 */
@Entity
public class Transfer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID transferId;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "selling_team_id")
    private Team sellingTeam;

    @ManyToOne
    @JoinColumn(name = "buying_team_id")
    private Team buyingTeam;

    @ManyToOne
    @JoinColumn(name = "transfer_window_id")
    private TransferWindow window;

    @Enumerated(EnumType.STRING)
    private TransferType type;

    @Enumerated(EnumType.STRING)
    private TransferStatus status;

    private BigDecimal agreedFee = BigDecimal.ZERO;
    private LocalDate reportedDate;
    private LocalDate confirmedDate;

    public Transfer() {}

    private Transfer(Builder builder) {
        this.transferId = builder.transferId;
        this.player = builder.player;
        this.sellingTeam = builder.sellingTeam;
        this.buyingTeam = builder.buyingTeam;
        this.window = builder.window;
        this.type = builder.type;
        this.status = builder.status;
        this.agreedFee = builder.agreedFee;
        this.reportedDate = builder.reportedDate;
        this.confirmedDate = builder.confirmedDate;
    }

    public UUID getTransferId() {
        return transferId;
    }

    public Player getPlayer() {
        return player;
    }

    public Team getSellingTeam() {
        return sellingTeam;
    }

    public Team getBuyingTeam() {
        return buyingTeam;
    }

    public TransferWindow getWindow() {
        return window;
    }

    public TransferType getType() {
        return type;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public BigDecimal getAgreedFee() {
        return agreedFee;
    }

    public LocalDate getReportedDate() {
        return reportedDate;
    }

    public LocalDate getConfirmedDate() {
        return confirmedDate;
    }

    public void setStatus(TransferStatus status) {
        this.status = status;
    }

    public void setAgreedFee(BigDecimal agreedFee) {
        this.agreedFee = agreedFee;
    }

    public void setConfirmedDate(LocalDate confirmedDate) {
        this.confirmedDate = confirmedDate;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "transferId=" + transferId +
                ", player=" + (player != null ? player.getPlayerId() : "null") +
                ", sellingTeam=" + (sellingTeam != null ? sellingTeam.getTeamName() : "free agent") +
                ", buyingTeam=" + (buyingTeam != null ? buyingTeam.getTeamName() : "null") +
                ", type=" + type +
                ", status=" + status +
                ", agreedFee=" + agreedFee +
                '}';
    }

    public static class Builder {
        private UUID transferId;
        private Player player;
        private Team sellingTeam;
        private Team buyingTeam;
        private TransferWindow window;
        private TransferType type;
        private TransferStatus status = TransferStatus.RUMOURED;
        private BigDecimal agreedFee = BigDecimal.ZERO;
        private LocalDate reportedDate = LocalDate.now();
        private LocalDate confirmedDate;

        public Builder setTransferId(UUID transferId) {
            this.transferId = transferId;
            return this;
        }

        public Builder setPlayer(Player player) {
            this.player = player;
            return this;
        }

        public Builder setSellingTeam(Team sellingTeam) {
            this.sellingTeam = sellingTeam;
            return this;
        }

        public Builder setBuyingTeam(Team buyingTeam) {
            this.buyingTeam = buyingTeam;
            return this;
        }

        public Builder setWindow(TransferWindow window) {
            this.window = window;
            return this;
        }

        public Builder setType(TransferType type) {
            this.type = type;
            return this;
        }

        public Builder setStatus(TransferStatus status) {
            this.status = status;
            return this;
        }

        public Builder setAgreedFee(BigDecimal agreedFee) {
            this.agreedFee = agreedFee;
            return this;
        }

        public Builder setReportedDate(LocalDate reportedDate) {
            this.reportedDate = reportedDate;
            return this;
        }

        public Builder setConfirmedDate(LocalDate confirmedDate) {
            this.confirmedDate = confirmedDate;
            return this;
        }

        public Builder copy(Transfer transfer) {
            this.transferId = transfer.transferId;
            this.player = transfer.player;
            this.sellingTeam = transfer.sellingTeam;
            this.buyingTeam = transfer.buyingTeam;
            this.window = transfer.window;
            this.type = transfer.type;
            this.status = transfer.status;
            this.agreedFee = transfer.agreedFee;
            this.reportedDate = transfer.reportedDate;
            this.confirmedDate = transfer.confirmedDate;
            return this;
        }

        public Transfer build() {
            if (player == null) throw new IllegalStateException("Transfer requires a player.");
            if (buyingTeam == null) throw new IllegalStateException("Transfer requires a buying team.");
            if (window == null) throw new IllegalStateException("Transfer requires a transfer window.");
            if (type == null) throw new IllegalStateException("Transfer requires a type.");
            if (type != TransferType.FREE && sellingTeam == null) {
                throw new IllegalStateException("Non-free transfers require a selling team.");
            }
            if (sellingTeam != null && buyingTeam.getTeamId() != null
                    && buyingTeam.getTeamId().equals(sellingTeam.getTeamId())) {
                throw new IllegalStateException("Buying team and selling team cannot be the same.");
            }
            if (status == null) status = TransferStatus.RUMOURED;
            return new Transfer(this);
        }
    }
}
