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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import responses.PortfolioResponse;

@Controller
public class PortfolioController {
    @RequestMapping(value="/portfolio")
    public String getPortfolioPage(){
        return "portfolio";
    }

    @GetMapping(value = "/getPortfolio", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPortfolioResponse(Authentication authentication){
        System.out.println("PortfolioController: getPortfolioResponse");

        SessionFactory factory = new Configuration()
                .addAnnotatedClass(AssetEntity.class)
                .addAnnotatedClass(PortfolioEntity.class)
                .addAnnotatedClass(StockEntity.class)
                .addAnnotatedClass(UserEntity.class)
                .buildSessionFactory();
        Gson gson = new Gson();
        Session session = factory.openSession();

        UserEntity loggedUser = (UserEntity) authentication.getPrincipal();
        System.out.println("PortfolioController: getPortfolioResponse user id: " + loggedUser.getId());
        try {
            session.getTransaction().begin();
            UserEntity user = session.get(UserEntity.class, loggedUser.getId());

            String username = user.getUsername();
            int userId = user.getId();
            PortfolioEntity portfolioEntity = user.getPortfolio();
            PortfolioResponse response = new PortfolioResponse(userId, username, portfolioEntity);

            System.out.println("PortfolioController: getPortfolioResponse user: " + user);

            String json = gson.toJson(response);

            System.out.println("PortfolioController: getPortfolioResponse portfolio response: " + response);

            session.getTransaction().commit();
            session.close();

            return ResponseEntity.status(HttpStatus.OK)
                    .body(json);
        }
        finally{
            factory.close();
        }
    }

    @GetMapping(value = "/getPortfolio/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String>  getPortfolio(@PathVariable int id){
        SessionFactory factory = new Configuration()
                .addAnnotatedClass(AssetEntity.class)
                .addAnnotatedClass(PortfolioEntity.class)
                .addAnnotatedClass(StockEntity.class)
                .addAnnotatedClass(UserEntity.class)
                .buildSessionFactory();

        try (factory) {
            Gson gson = new Gson();
            Session session = factory.openSession();
            System.out.println("PortfolioController: getPortfolio/{id}");
            session.getTransaction().begin();
            UserEntity user = session.get(UserEntity.class, id);

            System.out.println("PortfolioController: getPortfolio/{id} user: " + user);

            String username = user.getUsername();
            int userId = user.getId();
            PortfolioEntity portfolioEntity = user.getPortfolio();
            PortfolioResponse response = new PortfolioResponse(userId, username, portfolioEntity);

            String json = gson.toJson(response);
            session.getTransaction().commit();
            session.close();

            System.out.println("PortfolioController: getPortfolio/{id} JSON response: " + json);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(json);
        }
    }
}
