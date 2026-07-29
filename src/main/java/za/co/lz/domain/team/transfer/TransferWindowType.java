package za.co.lz.domain.team.transfer;

public enum TransferWindowType {
    SUMMER("Summer window"),
    WINTER("Winter window");

    private final String displayName;

    TransferWindowType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
