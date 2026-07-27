package za.co.lz.factory.finances;

import za.co.lz.domain.Name;
import za.co.lz.domain.team.finances.Owner;
import za.co.lz.domain.team.finances.OwnerStrategy;
import za.co.lz.domain.team.finances.OwnershipType;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Creates Owner entities for the different controlling-entity types described
 * by the ownership rule: individual, consortium, company, municipality, or
 * supporters' trust (plus the education/government variants already modeled
 * in OwnershipType).
 */
public class OwnerFactory {

    public static Owner createIndividualOwner(Name ownerName, String nationality, BigDecimal netWorth) {
        return baseBuilder(OwnershipType.PRIVATE_OWNER, netWorth)
                .setOwnerName(ownerName)
                .setOwnerNationality(nationality)
                .setStrategy(OwnerStrategy.BALANCED_APPROACH)
                .build();
    }

    public static Owner createConsortiumOwner(Name representativeName, BigDecimal netWorth) {
        return baseBuilder(OwnershipType.CONSORTIUM, netWorth)
                .setOwnerName(representativeName)
                .setStrategy(OwnerStrategy.MERGERS_AND_ACQUISITIONS)
                .build();
    }

    public static Owner createCompanyOwner(Name representativeName, BigDecimal netWorth) {
        return baseBuilder(OwnershipType.COMPANY, netWorth)
                .setOwnerName(representativeName)
                .setStrategy(OwnerStrategy.BRAND_EXPANSION)
                .build();
    }

    public static Owner createMunicipalOwner(Name representativeName, BigDecimal netWorth) {
        return baseBuilder(OwnershipType.MUNICIPALITY, netWorth)
                .setOwnerName(representativeName)
                .setStrategy(OwnerStrategy.COMMUNITY_ENGAGEMENT)
                .build();
    }

    public static Owner createSupportersTrustOwner(Name representativeName, BigDecimal netWorth) {
        return baseBuilder(OwnershipType.SUPPORTERS_TRUST, netWorth)
                .setOwnerName(representativeName)
                .setStrategy(OwnerStrategy.SUSTAINABILITY_FOCUS)
                .build();
    }

    private static Owner.Builder baseBuilder(OwnershipType ownershipType, BigDecimal netWorth) {
        return new Owner.Builder()
                .setOwnerId(UUID.randomUUID())
                .setOwnershipType(ownershipType)
                .setNetWorth(netWorth)
                .setAvailableFunds(netWorth)
                .setInvestmentBudget(BigDecimal.ZERO)
                .setReputation(50);
    }
}
