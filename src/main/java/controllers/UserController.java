package controllers;

import com.google.gson.Gson;
import entities.AssetEntity;
import entities.PortfolioEntity;
import entities.StockEntity;
import entities.UserEntity;
import exceptions.InsufficientFundsException;
import helpers.AvailableCreditGetter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pojos.StockDataDigest;
import requests.BuyStockRequest;
import requests.SellStockRequest;
import requests.SignupRequest;
import responses.PortfolioResponse;
import responses.StockListResponse;
import services.AssetOperationService;
import services.StockService;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private AssetOperationService assetOperationService;

    @Autowired
    private StockService stockService;

    private final SessionFactory factory = new Configuration()
            .addAnnotatedClass(AssetEntity.class)
            .addAnnotatedClass(PortfolioEntity.class)
            .addAnnotatedClass(StockEntity.class)
            .addAnnotatedClass(UserEntity.class)
            .buildSessionFactory();

    @GetMapping(value = "/portfolio", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPortfolioResponse(Authentication authentication){

        Gson gson = new Gson();
        Session session = factory.getCurrentSession();

        UserEntity loggedUser = (UserEntity) authentication.getPrincipal();
        System.out.println("UserController: getPortfolioResponse user id: " + loggedUser.getId());
        try {
            session.getTransaction().begin();
            UserEntity user = session.get(UserEntity.class, loggedUser.getId());

            String username = user.getUsername();
            int userId = user.getId();
            PortfolioEntity portfolioEntity = user.getPortfolio();
            PortfolioResponse response = new PortfolioResponse(userId, username, portfolioEntity);

            System.out.println("UserController: getPortfolioResponse user: " + user);

            String json = gson.toJson(response);

            System.out.println("UserController: getPortfolioResponse portfolio response: " + response);

			session.close();

            return ResponseEntity.status(HttpStatus.OK)
                    .body(json);
        }
        finally{
    		factory.close();
        }
    }

    @GetMapping(value = "/portfolio/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String>  getPortfolio(@PathVariable int id){
        Gson gson = new Gson();
        Session session = factory.getCurrentSession();

        System.out.println("UserController: getPortfolio/{id}");
        try {
            session.getTransaction().begin();
            UserEntity user = session.get(UserEntity.class, id);

            System.out.println("UserController: getPortfolio/{id} user: " + user);

            String username = user.getUsername();
            int userId = user.getId();
            PortfolioEntity portfolioEntity = user.getPortfolio();
            PortfolioResponse response = new PortfolioResponse(userId, username, portfolioEntity);

            System.out.println("UserController: getPortfolio/{id} response: " + response);

            String json = gson.toJson(response);
            session.close();

            return ResponseEntity.status(HttpStatus.OK)
                    .body(json);
        }
        finally{
            factory.close();
        }
    }

    @GetMapping(value = "/stocks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStockList(){
        Session session = factory.getCurrentSession();
        Gson gson = new Gson();
        StockListResponse response = new StockListResponse();
        List<StockEntity> stocks;

        System.out.println("UserController: /stocks ");

        try{
            session.getTransaction().begin();
            stocks = session.createQuery(" from StockEntity").getResultList();

            for(StockEntity stock : stocks){
                response.add(new StockDataDigest(stock));
            }

            String json = gson.toJson(response);

            System.out.println("UserController: /stocks response: " + response);

            session.getTransaction().commit();

            return ResponseEntity.status(HttpStatus.OK)
                    .body(json);
        }
        finally {
            factory.close();
        }
    }

    @GetMapping(value = "/stock/{id}")
    public String getStockInfo(@ModelAttribute("stock") StockEntity stockEntity, Model model, @PathVariable int id){
        Session session = factory.getCurrentSession();
        System.out.println("UserController: /stock/{id} ");
        try{
            session.getTransaction().begin();
            StockEntity stock = session.get(StockEntity.class, id);
            model.addAttribute("stock", stock);

            System.out.println("UserController: /stock/{id} stock: " + stock);

            session.getTransaction().commit();
            session.close();
        }
        finally {
            factory.close();
        }
        return "stock";
    }

    @PostMapping(value = "/stock/{id}/sell", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sellStock(@PathVariable int id, HttpServletRequest request, Authentication authentication){
        Session session = factory.getCurrentSession();
        UserEntity loggedUser = (UserEntity) authentication.getPrincipal();

        System.out.println("UserController: /stock/{id}/sell loggedUser: " + loggedUser);

        Gson gson = new Gson();
        StringBuffer jb = new StringBuffer();
        String line;

        try{
            session.getTransaction().begin();
            UserEntity user = session.get(UserEntity.class, loggedUser.getId());
            PortfolioEntity portfolio = user.getPortfolio();
            List<AssetEntity> assets = portfolio.getAssets();
            StockEntity stock = session.get(StockEntity.class, id);

            System.out.println("UserController: /stock/{id}/sell user: " + user);
            System.out.println("UserController: /stock/{id}/sell stock: " + stock);

            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);

            String jsonString = jb.toString();

            SellStockRequest sellStockRequest = gson.fromJson(jsonString, SellStockRequest.class);
            int price = sellStockRequest.getQuantity() * sellStockRequest.getSellingPrice();

            System.out.println("UserController: /stock/{id}/sell sellStockRequest: " + sellStockRequest);

            for(AssetEntity asset : assets){
                if(asset.getStock().getName().equals(stock.getName())){
                    if(asset.getQuantity() > sellStockRequest.getQuantity()){
                        asset.setQuantity(asset.getQuantity() - sellStockRequest.getQuantity());
                        session.update(asset);
                    }
                    //TODO: fix, subtracting cash only after deleting stock, handle too much stocks to sell
                    else if(asset.getStock().getName().equals("Cash")){
                        asset.setQuantity(asset.getQuantity() + price);
                        session.update(asset);
                    }
                }
            }
            session.getTransaction().commit();
            session.close();

            return ResponseEntity.status(HttpStatus.OK)
                    .body("Stock sold");
        }
        catch(IOException e){
            System.out.println("User controller, sell stock received POST, exception: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("");
        }
        finally {
            factory.close();
        }
    }

    @PostMapping(value = "/stock/{id}/buy", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> buyStock(@PathVariable int id, HttpServletRequest request, Authentication authentication){
        Session session = factory.getCurrentSession();
        UserEntity loggedUser = (UserEntity) authentication.getPrincipal();

        Gson gson = new Gson();
        StringBuffer jb = new StringBuffer();
        String line = null;

        System.out.println("UserController: /stock/{id}/buy loggedUser: " + loggedUser);

        try{
            session.getTransaction().begin();

            UserEntity user = session.get(UserEntity.class, loggedUser.getId());
            PortfolioEntity portfolio = user.getPortfolio();
            StockEntity stock = session.get(StockEntity.class, id);
            int cash = AvailableCreditGetter.getAvailableCredit(portfolio);

            System.out.println("UserController: /stock/{id}/buy user: " + user);
            System.out.println("UserController: /stock/{id}/buy stock: " + stock);

            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);

            String jsonString = jb.toString();

            BuyStockRequest buyStockRequest = gson.fromJson(jsonString, BuyStockRequest.class);
            int price = buyStockRequest.getQuantity() * buyStockRequest.getBuyPrice();

            System.out.println("UserController: /stock/{id}/sell buyRequest: " + buyStockRequest);

            if(cash > price){
                AssetEntity cashAsset = portfolio.getCash();
                cashAsset.setQuantity(cashAsset.getQuantity() - price);
                boolean isStockPresentAlready = false;
                for(AssetEntity asset : portfolio.getAssets()){
                    if(asset.getStock().equals(stock)){
                        isStockPresentAlready = true;
                        break;
                    }
                }

                if(isStockPresentAlready){
                    AssetEntity assetToBuy = assetOperationService.getAssetById(portfolio, buyStockRequest.getStockId());
                    assetToBuy.setQuantity(assetToBuy.getQuantity() + buyStockRequest.getQuantity());
                }
                else{
                    //TODO: A lot of potential problems

                    StockEntity boughtStock =  stockService.getStockById(buyStockRequest.getStockId());
                    AssetEntity boughtAsset = new AssetEntity();
                    boughtAsset.setQuantity(buyStockRequest.getQuantity());
                    boughtAsset.setStock(boughtStock);
                    boughtAsset.setBuyPrice(boughtStock.getCurrentPrice());

                    portfolio.getAssets().add(boughtAsset);
                    session.update(portfolio);          //TODO: test
                    session.getTransaction().commit();

                    return ResponseEntity.status(HttpStatus.OK)
                            .body("Stock bought");
                }
//                portfolio.getAssets().add
            }

            else{
                throw new InsufficientFundsException();
            }
        }
        catch(IOException ioException){
            System.out.println("User controller, sell stock received POST, exception: " + ioException.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("IOException");
        }
        finally {
            factory.close();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Stock buying failed");
    }

    @PostMapping(value = "/createUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createUser(HttpServletRequest request){
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
            portfolio.getAssets().add(cashAsset);
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
                    .body("");
        }
        finally {
            factory.close();
        }

    }

    @RequestMapping(value = "/signup")
    public String getSignupForm(){
        return "signup";
    }
}
