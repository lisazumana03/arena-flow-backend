package za.co.lz.factory.team.transfer;

import za.co.lz.domain.team.Player;
import za.co.lz.domain.team.Team;
import za.co.lz.domain.team.transfer.Transfer;
import za.co.lz.domain.team.transfer.TransferType;
import za.co.lz.domain.team.transfer.TransferWindow;

import java.math.BigDecimal;

/**
 * Every Transfer must reference a player, a buying team, and a window - see
 * Transfer.Builder.build(). These factory methods mirror TeamFactory: they
 * take the required relations up front so a Transfer is never created
 * "half wired", and every deal starts life as a RUMOURED report.
 */
public class TransferFactory {

    public static Transfer reportPermanentMove(Player player, Team sellingTeam, Team buyingTeam,
                                                 TransferWindow window, BigDecimal reportedFee) {
        return new Transfer.Builder()
                .setPlayer(player)
                .setSellingTeam(sellingTeam)
                .setBuyingTeam(buyingTeam)
                .setWindow(window)
                .setType(TransferType.PERMANENT)
                .setAgreedFee(reportedFee)
                .build();
    }

    public static Transfer reportLoanMove(Player player, Team sellingTeam, Team buyingTeam, TransferWindow window) {
        return new Transfer.Builder()
                .setPlayer(player)
                .setSellingTeam(sellingTeam)
                .setBuyingTeam(buyingTeam)
                .setWindow(window)
                .setType(TransferType.LOAN)
                .build();
    }

    public static Transfer reportFreeTransfer(Player player, Team buyingTeam, TransferWindow window) {
        return new Transfer.Builder()
                .setPlayer(player)
                .setBuyingTeam(buyingTeam)
                .setWindow(window)
                .setType(TransferType.FREE)
                .build();
    }
}
