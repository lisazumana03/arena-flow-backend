# Phase 3 - Takeover & Financial Distress System

## Overview
Phase 3 implements comprehensive financial monitoring, distress detection, and takeover mechanics. The system tracks club financials, evaluates financial health, and triggers intervention/takeover scenarios when needed.

## Financial Health Scoring

### Health Levels
The system evaluates financial health based on:
- **Operating Margin** = (Revenue - Expenses) / Revenue × 100%
- **Debt-to-Revenue Ratio** = Debt / Revenue

| Health Status | Operating Margin | Debt/Revenue | Budget Impact | Action |
|---|---|---|---|---|
| **EXCELLENT** | > 20% | < 1.0x | 1.0x multiplier | Growth phase |
| **HEALTHY** | > 10% | < 1.5x | 1.0x multiplier | Normal operations |
| **STABLE** | > 0% | < 2.5x | 0.9x multiplier | Monitor closely |
| **CAUTION** | > -10% | < 4.0x | 0.75x multiplier | Owner review |
| **AT_RISK** | > -20% | < 5.0x | 0.5x multiplier | Intervention needed |
| **CRITICAL** | > -30% | < 6.0x | 0.3x multiplier | Severe action required |
| **INSOLVENT** | ≤ -30% | > 6.0x | 0.0x multiplier | Forced sale/bankruptcy |

### Scoring Formula
```
If Operating Margin > 20% AND Debt/Revenue < 1.0x → EXCELLENT
Else if Operating Margin > 10% AND Debt/Revenue < 1.5x → HEALTHY
Else if Operating Margin > 0% AND Debt/Revenue < 2.5x → STABLE
Else if Operating Margin > -10% AND Debt/Revenue < 4.0x → CAUTION
Else if Operating Margin > -20% AND Debt/Revenue < 5.0x → AT_RISK
Else if Operating Margin > -30% AND Debt/Revenue < 6.0x → CRITICAL
Else → INSOLVENT
```

## Revenue Sources

```java
Total Revenue = 
  + Ticket Revenue (match attendance)
  + Sponsorship Revenue (commercial deals)
  + Merchandise Revenue (kit sales, products)
  + Media Rights (broadcast deals)
  + Other Revenue (academy sales, etc.)
```

## Expense Categories

```java
Total Expenses = 
  + Player Wages (salary payments)
  + Staff Costs (coaches, physios, admin)
  + Operating Costs (utilities, travel, etc.)
  + Depreciation Costs (asset depreciation)
  + Other Expenses (miscellaneous)
```

## Takeover Triggers

### Automatic Forced Sale (Mandatory)
Triggered when **ANY** of these occur:
1. **Owner has negative funds** (`availableFunds < 0`)
2. **Debt exceeds 3x annual revenue** (`debt > 3 × revenue`)
3. **3+ consecutive seasons of losses** (`consecutiveLosses >= 3`)

### Example: Forced Sale Scenario
```
Arsenal (owned by Owner X)
├─ Revenue: $300M
├─ Expenses: $380M (80M loss)
├─ Debt: $950M
├─ Owner available funds: -$50M

Debt/Revenue = 950M / 300M = 3.17x > 3.0x threshold ✗
→ FORCED SALE TRIGGERED
→ Team must be sold to another owner
→ Sale price significantly reduced (fire sale)
```

## Intervention Scenarios

### Type 1: Owner Injection Required
```
Owner must inject funds to:
- Reduce debt below critical levels
- Unfreeze budget for spending
- Prevent forced sale

Example:
Owner X injects $100M into Arsenal
├─ Arsenal debt: $950M → $850M
├─ Owner funds: -$50M → $50M
├─ Status: AT_RISK → CAUTION
```

### Type 2: Budget Reduction (Penalties)
```
Applied when:
- Team relegated
- FFP violations detected
- Excessive spending

Example:
Arsenal relegated, receives penalty:
├─ Next season budget: 200M → 150M (25% reduction)
├─ Transfer budget: 80M → 60M
├─ Wage budget: 90M → 67.5M
```

### Type 3: Emergency Takeover
```
Quick sale to stabilize league/competition:
- Owner must sell at reduced price
- New owner takes on debt obligations (negotiated)
- League may subsidize to prevent collapse
```

## Consecutive Loss Tracking

```
Season 1: Loss → consecutiveLosses = 1
Season 2: Loss → consecutiveLosses = 2
Season 3: Loss → consecutiveLosses = 3 → FORCED SALE TRIGGERED
Season 4 (after profit): Profit → consecutiveLosses = 0 (resets)
```

## Debt Management

### Recording Losses
```java
POST /api/financials/{financialId}/record-loss

Effects:
1. consecutiveLosses increment
2. Deficit amount added to debt
3. Health status re-evaluated
4. If debt > 3x revenue → marked for takeover
```

### Recording Profits
```java
POST /api/financials/{financialId}/record-profit

Effects:
1. consecutiveLosses reset to 0
2. Profit amount reduces debt (cannot go below 0)
3. Health status improves
4. Intervention flag may be cleared
```

### Relegation Penalties
```java
POST /api/financials/{financialId}/apply-relegation-penalty?amount=50000000

Effects:
1. Penalty amount added to debt
2. Team automatically requires intervention
3. Next budget cycle reduced
4. May trigger takeover if debt becomes extreme
```

## API Endpoints

### Financial Monitoring
```bash
# Create annual financials
POST /api/financials/create
{ "teamId": "uuid", "ownerId": "uuid", "year": "2024-01-01" }

# Check financial health
GET /api/financials/{financialId}/health
→ Returns: { status, budgetMultiplier, interventionNeeded, takeoverRisk }

# Record financial events
POST /api/financials/{financialId}/record-loss
POST /api/financials/{financialId}/record-profit
POST /api/financials/{financialId}/add-debt?amount=5000000
POST /api/financials/{financialId}/apply-relegation-penalty?amount=20000000

# Find problematic clubs
GET /api/financials/takeovers      # Teams in forced sale
GET /api/financials/intervention   # Teams needing intervention
```

### Takeover Operations
```bash
# Evaluate if forced sale is needed
POST /api/financials/takeover/evaluate/{teamId}
→ Returns: { forcedSaleNeeded, interventionNeeded }

# Execute forced sale
POST /api/financials/takeover/execute
{ "teamId": "uuid", "buyerId": "uuid", "price": 50000000 }
→ Owner X loses team, Owner Y gains team

# Owner injects funds
POST /api/financials/owner/{ownerId}/inject-funds?teamId=uuid&amount=50000000
→ Reduces debt, improves health status
```

## Business Rules

1. **Every team must have owner** - Ownership cannot be null
2. **Financial year is immutable** - Cannot change historical records
3. **Forced sales bypass owner agreement** - League can force if criteria met
4. **Debt cascades to owner** - Owner responsibility for club debt
5. **Budget multiplier applies automatically** - Health status affects next budget allocation
6. **Consecutive losses are seasonal** - Reset when profit is achieved
7. **Takeover is permanent** - Cannot reverse (use historical financials to show ownership)

## Integration Flow

```
Match Results
    ↓
Revenue/Expenses Generated
    ↓
FinancialService.recordLoss() or recordProfit()
    ↓
Health Status Evaluated
    ↓
Check Intervention Thresholds
    ↓
If health is CRITICAL/INSOLVENT:
  ├─ Mark as intervention needed
  ├─ Mark as takeover candidate
  ├─ Freeze next budget
  └─ Alert owner/league
    ↓
If forced sale criteria met:
  ├─ Evaluate buyers
  ├─ Execute takeover
  └─ Transfer ownership
    ↓
If owner bankruptcy:
  ├─ Seize teams
  ├─ Assign to league/neutral party
  └─ Liquidate assets
```

## Scenarios

### Scenario 1: Successful Turnaround
```
Year 1: Loss (-50M) → Debt: 100M → Health: AT_RISK
Year 2: Profit (+30M) → Debt: 70M → Health: CAUTION
Year 3: Profit (+40M) → Debt: 30M → Health: STABLE
Year 4: Profit (+50M) → Debt: 0 → Health: HEALTHY
```

### Scenario 2: Death Spiral
```
Year 1: Loss (-50M) → consecutiveLosses: 1, Health: AT_RISK
Year 2: Loss (-60M) → consecutiveLosses: 2, Health: CRITICAL
Year 3: Loss (-70M) → consecutiveLosses: 3 → FORCED SALE TRIGGERED
Year 4: New Owner → Fresh start
```

### Scenario 3: Multi-Club Bankruptcy
```
Owner X owns 3 teams:
├─ Arsenal: Health INSOLVENT, Debt: 2B
├─ Chelsea: Health INSOLVENT, Debt: 1.5B
└─ Liverpool: Health CRITICAL, Debt: 800M

Owner X runs out of funds trying to save them
→ isOwnerBankrupt() = true
→ All teams seized by league
→ Auctioned to new owners
```

## Future Extensions

- **Loan mechanics** - Owner borrows against future revenue
- **Financial Fair Play (FFP)** - Spending limits tied to revenue
- **Stock system** - Owners can go public, trade shares
- **Stadium sales** - Asset-backed financing
- **Player sales** - Capital gains from academy/youth promotions
- **Broadcasting deals** - Long-term revenue contracts
- **Sponsorship negotiations** - Dynamic commercial revenue
