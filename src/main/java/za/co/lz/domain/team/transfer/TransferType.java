package za.co.lz.domain.team.transfer;

public enum TransferType {
    PERMANENT("Permanent", "Outright transfer of registration"),
    LOAN("Loan", "Temporary transfer, player returns to selling team"),
    LOAN_WITH_OPTION("Loan with option to buy", "Loan with an agreed permanent fee if triggered"),
    FREE("Free transfer", "No fee, typically an out-of-contract player");

    private final String displayName;
    private final String description;

    TransferType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
