package responses;

import pojos.LeaderboardDigest;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardResponse {
    private List<LeaderboardDigest> leaderboardData = new ArrayList<>();

    public void add(LeaderboardDigest digest){
        this.leaderboardData.add(digest);
    }
}
