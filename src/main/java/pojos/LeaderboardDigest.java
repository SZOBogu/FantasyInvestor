package pojos;

import entities.UserEntity;
import helpers.PortfolioValueCalculator;

public class LeaderboardDigest {
    private int userId;
    private String username;
    private double portfolioValue;

    public LeaderboardDigest(UserEntity user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.portfolioValue = PortfolioValueCalculator.getPortfolioValue(user.getPortfolio());
    }
}
