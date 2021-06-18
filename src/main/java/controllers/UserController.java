package controllers;

import com.google.gson.Gson;
import entities.AssetEntity;
import entities.PortfolioEntity;
import entities.StockEntity;
import entities.UserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import requests.SignupRequest;
import services.StockService;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private StockService stockService;

    @RequestMapping(value = "/signup")
    public String getSignupForm(){
        return "signup";
    }


    @PostMapping(value = "/createUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createUser(HttpServletRequest request){
        SessionFactory factory = new Configuration()
                .addAnnotatedClass(AssetEntity.class)
                .addAnnotatedClass(PortfolioEntity.class)
                .addAnnotatedClass(StockEntity.class)
                .addAnnotatedClass(UserEntity.class)
                .buildSessionFactory();
        Session session = factory.getCurrentSession();

        Gson gson = new Gson();
        StringBuffer jb = new StringBuffer();
        String line;

        System.out.println("UserController: /createUser");

        try{
            session.getTransaction().begin();

            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);

            String jsonString = jb.toString();

            SignupRequest signupRequest = gson.fromJson(jsonString, SignupRequest.class);
            System.out.println("UserController: /createUser signupRequest: " + signupRequest);

            UserEntity user = new UserEntity();
            user.setUsername(signupRequest.getUsername());
            user.setPassword(signupRequest.getPassword());

            if(signupRequest.getUsername().equals("admin"))
                user.setRole("ADMIN");
            else
                user.setRole("USER");

            System.out.println("UserController: /createUser user: " + user);

            PortfolioEntity portfolio = new PortfolioEntity();

            AssetEntity cashAsset = new AssetEntity();
            cashAsset.setQuantity(1000000);
            cashAsset.setStock(stockService.getStockByName("Cash"));
            cashAsset.setBuyPrice(1);
            user.setPortfolio(portfolio);

            List<AssetEntity> assets = new ArrayList<>();
            assets.add(cashAsset);
            portfolio.setAssets(assets);
            cashAsset.setPortfolioEntity(portfolio);
            session.save(cashAsset);
            session.save(portfolio);
            session.save(user);
            session.getTransaction().commit();
            session.close();

            return ResponseEntity.status(HttpStatus.OK)
                    .body("User Created");
        }
        catch (IOException exception){
            System.out.println("User controller, createUser received POST, exception: " + exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("User creation error");
        }
        finally {
            factory.close();
        }
    }
}
