package za.co.lz.domain.team.transfer;

/**
 * Reliability tiers a Transfer moves through, mirroring the escalating
 * certainty scale used by transfer journalists (Ornstein/Romano style):
 * a deal starts as speculation and becomes progressively more confirmed.
 *
 * Transitions are constrained (see canAdvanceTo): a Transfer can only move
 * one tier forward at a time, or collapse from any non-terminal tier.
 * OFFICIAL and DEAL_COLLAPSED are terminal - nothing moves out of them.
 */
public enum TransferStatus {
    RUMOURED("Rumoured", "Media speculation only, nothing agreed"),
    IN_TALKS("In talks", "Clubs are negotiating a fee"),
    AGREEMENT_REACHED("Agreement reached", "Fee agreed between clubs"),
    HERE_WE_GO("Here we go", "Personal terms agreed, deal all but done"),
    MEDICAL_SCHEDULED("Medical scheduled", "Awaiting medical clearance"),
    OFFICIAL("Official", "Deal confirmed, player registered"),
    DEAL_COLLAPSED("Deal collapsed", "Transfer will not proceed");

    private final String displayName;
    private final String description;

    TransferStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isTerminal() {
        return this == OFFICIAL || this == DEAL_COLLAPSED;
    }

    /**
     * A Transfer may only progress one tier at a time, or collapse outright
     * from any non-terminal tier. It can never skip tiers (e.g. RUMOURED
     * straight to OFFICIAL) and nothing leaves OFFICIAL or DEAL_COLLAPSED.
     */
    public boolean canAdvanceTo(TransferStatus next) {
        if (this.isTerminal()) {
            return false;
        }
        if (next == DEAL_COLLAPSED) {
            return true;
        }
        return next.ordinal() == this.ordinal() + 1;
    }
}
