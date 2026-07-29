# Phase 2 - Budget Management System

## Overview
The Budget Management system allows owners to allocate and control team finances based on their selected strategy. Budgets are automatically distributed across five categories based on the owner's `OwnerStrategy`.

## Architecture

### Budget Allocation Formula

When an owner creates a budget with a total amount, the system allocates funds based on **strategy multipliers**:

```
Each category gets: Total Budget × (Strategy Multiplier / Total Multipliers Sum)
```

**Example: AGGRESSIVE_SPENDING Strategy**
- Transfer: 1.5x
- Wage: 1.5x
- Academy: 0.8x
- Operating: 1.0x (default)
- Infrastructure: 1.0x
- **Total: 5.8x**

With $100M budget:
- Transfer: $100M × (1.5/5.8) = **$25.9M**
- Wage: $100M × (1.5/5.8) = **$25.9M**
- Academy: $100M × (0.8/5.8) = **$13.8M**
- Operating: $100M × (1.0/5.8) = **$17.2M**
- Infrastructure: $100M × (1.0/5.8) = **$17.2M**

### Strategy Impact Examples

| Strategy | Transfer | Wage | Academy | Use Case |
|----------|----------|------|---------|----------|
| AGGRESSIVE_SPENDING | 1.5x | 1.5x | 0.8x | Win now, buy stars |
| YOUTH_DEVELOPMENT | 0.6x | 0.7x | 2.0x | Long-term growth |
| PROFIT_FOCUS | 0.5x | 0.6x | 0.5x | Revenue maximization |
| BALANCED_APPROACH | 1.0x | 1.0x | 1.0x | Sustainable growth |
| SURVIVAL_MODE | 0.4x | 0.5x | 0.3x | Financial distress |

## Budget Categories

### 1. **Transfer Budget**
- Used for player purchases
- Strategy influences spending power
- Can be frozen if FFP violations detected

### 2. **Wage Budget**
- Player salary payments
- Manager/coach contracts
- Staff wages
- Strategy affects spending flexibility

### 3. **Youth Academy Budget**
- Young player development
- Coaching staff for academy
- Training facilities for youth
- Critical for long-term sustainability

### 4. **Operating Budget**
- Stadium maintenance
- Equipment/supplies
- Administrative costs
- Essential fixed costs

### 5. **Infrastructure Budget**
- Stadium expansion
- Training ground upgrades
- Youth academy facilities
- Long-term investment

## Spending Controls

### Budget Validation
```java
// Before spending, system checks:
✓ Available funds > requested amount
✓ Budget status == ACTIVE (not FROZEN or SUSPENDED)
✓ Owner has sufficient funds
✓ No FFP violations
```

### Spending Operations
```java
// API endpoints for spending
POST /api/budgets/{budgetId}/spend-transfer?amount=5000000
POST /api/budgets/{budgetId}/spend-wages?amount=3000000
POST /api/budgets/{budgetId}/spend-academy?amount=500000

// Each call:
// 1. Validates amount
// 2. Deducts from allocated budget
// 3. Updates spent tracking
// 4. Checks budget health
// 5. May trigger restrictions if over budget
```

## Budget Health Monitoring

### Utilization Percentage
```
Used = Total Spent / Total Budget × 100%

80-100%: CAUTION (running tight)
100%+:   EXCEEDED (overspent - requires intervention)
< 50%:   COMFORTABLE (plenty of room)
```

### Freeze/Unfreeze
Budgets can be frozen by:
- Owner decision (financial discipline)
- System (FFP violations, excessive losses)
- Creditors/regulators

```
FROZEN budget → No spending allowed until unfrozen
```

## API Usage Examples

### 1. Create Budget for Team
```bash
POST /api/budgets/create
{
  "teamId": "uuid-here",
  "ownerId": "uuid-here",
  "budgetYear": "2024-01-01",
  "totalBudget": 100000000
}

Response: TeamBudget with allocated amounts
```

### 2. Check Available Budgets
```bash
GET /api/budgets/owner/{ownerId}
→ List all budgets for owner's teams

GET /api/budgets/team/{teamId}
→ All budgets in team's history

GET /api/budgets/team/{teamId}/year/2024
→ Specific year's budget
```

### 3. Spend From Budget
```bash
# Spend on player transfers
POST /api/budgets/{budgetId}/spend-transfer?amount=10000000

# Spend on wages
POST /api/budgets/{budgetId}/spend-wages?amount=3000000

# Invest in youth academy
POST /api/budgets/{budgetId}/spend-academy?amount=500000
```

### 4. Monitor Budget Health
```bash
GET /api/budgets/{budgetId}/health

Response:
{
  "healthy": true,
  "utilizationPercentage": 65.5
}
```

## Integration with Other Systems

### With OwnerStrategy
- Changing strategy affects budget allocation
- New strategy takes effect on next budget cycle
- Strategy multipliers scale all category allocations

### With Owner Finances
- Budget spending deducts from `availableFunds`
- Exceeded budgets may freeze owner's funds
- Budget health affects owner reputation

### With Objectives
- Objectives track progress (e.g., "Win League")
- Budget allocation supports objectives
- Youth academy budget supports "Develop Young Players" objective

## Business Rules

1. **Every team must have a budget** for each financial year
2. **Budget cannot be negative** - spends are validated
3. **Strategy-based allocation** - totals depend on owner strategy
4. **Freeze protection** - prevents reckless spending
5. **Annual cycles** - new budget each year/season
6. **Multi-team support** - owner can have different budgets per team

## Future Extensions

- Revenue generation (ticket sales, sponsorships)
- Budget forecasting (next year predictions)
- FFP calculation (spending limits based on revenue)
- Loan mechanics (borrow against future revenue)
- Budget negotiations (owner vs league restrictions)
