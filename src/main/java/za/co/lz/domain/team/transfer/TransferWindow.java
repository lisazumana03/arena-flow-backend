package za.co.lz.domain.team.transfer;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Governs when Transfers are allowed to be finalized. Deals can be
 * negotiated (RUMOURED..MEDICAL_SCHEDULED) at any time, but a Transfer
 * can only reach OFFICIAL while its window is open on the current date.
 */
@Entity
public class TransferWindow implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID windowId;

    @Enumerated(EnumType.STRING)
    private TransferWindowType type;

    private int year;
    private LocalDate openDate;
    private LocalDate closeDate;

    public TransferWindow() {}

    private TransferWindow(Builder builder) {
        this.windowId = builder.windowId;
        this.type = builder.type;
        this.year = builder.year;
        this.openDate = builder.openDate;
        this.closeDate = builder.closeDate;
    }

    public UUID getWindowId() {
        return windowId;
    }

    public TransferWindowType getType() {
        return type;
    }

    public int getYear() {
        return year;
    }

    public LocalDate getOpenDate() {
        return openDate;
    }

    public LocalDate getCloseDate() {
        return closeDate;
    }

    public boolean isOpen(LocalDate date) {
        return !date.isBefore(openDate) && !date.isAfter(closeDate);
    }

    @Override
    public String toString() {
        return "TransferWindow{" +
                "windowId=" + windowId +
                ", type=" + type +
                ", year=" + year +
                ", openDate=" + openDate +
                ", closeDate=" + closeDate +
                '}';
    }

    public static class Builder {
        private UUID windowId;
        private TransferWindowType type;
        private int year;
        private LocalDate openDate;
        private LocalDate closeDate;

        public Builder setWindowId(UUID windowId) {
            this.windowId = windowId;
            return this;
        }

        public Builder setType(TransferWindowType type) {
            this.type = type;
            return this;
        }

        public Builder setYear(int year) {
            this.year = year;
            return this;
        }

        public Builder setOpenDate(LocalDate openDate) {
            this.openDate = openDate;
            return this;
        }

        public Builder setCloseDate(LocalDate closeDate) {
            this.closeDate = closeDate;
            return this;
        }

        public Builder copy(TransferWindow window) {
            this.windowId = window.windowId;
            this.type = window.type;
            this.year = window.year;
            this.openDate = window.openDate;
            this.closeDate = window.closeDate;
            return this;
        }

        public TransferWindow build() {
            if (type == null) throw new IllegalStateException("TransferWindow requires a type.");
            if (openDate == null) throw new IllegalStateException("TransferWindow requires an openDate.");
            if (closeDate == null) throw new IllegalStateException("TransferWindow requires a closeDate.");
            if (closeDate.isBefore(openDate)) throw new IllegalStateException("closeDate cannot be before openDate.");
            return new TransferWindow(this);
        }
    }
}
