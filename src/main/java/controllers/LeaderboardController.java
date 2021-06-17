package controllers;

import com.google.gson.Gson;
import entities.AssetEntity;
import entities.PortfolioEntity;
import entities.StockEntity;
import entities.UserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pojos.LeaderboardDigest;
import responses.LeaderboardResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/leaderboard")
public class LeaderboardController {

    @RequestMapping
    public String getPage(){
        return "leaderboard";
    }

    @GetMapping(value = "/getLeaderboard" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getLeaderboard(){
        SessionFactory factory = new Configuration()
                .addAnnotatedClass(AssetEntity.class)
                .addAnnotatedClass(PortfolioEntity.class)
                .addAnnotatedClass(StockEntity.class)
                .addAnnotatedClass(UserEntity.class)
                .buildSessionFactory();

        System.out.println("LeaderboardController got request");

        Session session = factory.getCurrentSession();
        Gson gson = new Gson();

        try{
            session.getTransaction().begin();

            List<UserEntity> users = session.createQuery(" from UserEntity").getResultList();
            Collections.sort(users, Collections.reverseOrder());
            LeaderboardResponse response = new LeaderboardResponse();

            for(int i = 0; i < users.size(); i++){
                response.add(new LeaderboardDigest(users.get(i)));
            }

            String json = gson.toJson(response);
            session.getTransaction().commit();
            session.close();

            System.out.println("LeaderboardController produced response: " + response);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(json);
        }
        finally {
            factory.close();
        }
    }
}
