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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pojos.StockDataDigest;
import requests.BuyStockRequest;
import requests.SellStockRequest;
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
        //return authentication.getName();

        /*
        the Spring Security principal can only be retrieved as an Object and needs to be cast to the correct UserDetails instance:
        */

        UserEntity loggedUser = (UserEntity) authentication.getPrincipal();

        try {
            session.getTransaction().begin();
            UserEntity user = session.get(UserEntity.class, loggedUser.getId());

            PortfolioResponse response = new PortfolioResponse();
            response.setUsername(user.getUsername());
            response.setId(user.getId());
            response.setPortfolio(user.getPortfolio());

            String json = gson.toJson(response);
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

        try {
            session.getTransaction().begin();
            UserEntity user = session.get(UserEntity.class, id);

            PortfolioResponse response = new PortfolioResponse();
            response.setUsername(user.getUsername());
            response.setId(user.getId());
            response.setPortfolio(user.getPortfolio());

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
        List<StockEntity> stocks = new ArrayList<>();

        try{
            session.getTransaction().begin();
            stocks = session.createQuery(" from StockEntity").getResultList();

            for(StockEntity stock : stocks){
                response.add(new StockDataDigest(stock));
            }

            String json = gson.toJson(response);
            session.getTransaction().commit();

            return ResponseEntity.status(HttpStatus.OK)
                    .body(json);
        }
        finally {
            factory.close();
        }
    }

    @GetMapping(value = "/stock/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStockInfo(@PathVariable int id){
        Session session = factory.getCurrentSession();
        Gson gson = new Gson();

        try{
            session.getTransaction().begin();
            StockEntity stock = session.get(StockEntity.class, id);
            String json = gson.toJson(stock);
            session.getTransaction().commit();

            return ResponseEntity.status(HttpStatus.OK)
                    .body(json);
        }
        finally {
            factory.close();
        }
    }

    @PostMapping(value = "/stock/{id}/sell", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sellStock(@PathVariable int id, HttpServletRequest request, Authentication authentication){
        Session session = factory.getCurrentSession();
        UserEntity loggedUser = (UserEntity) authentication.getPrincipal();

        Gson gson = new Gson();
        StringBuffer jb = new StringBuffer();
        String line = null;

        try{
            session.getTransaction().begin();
            UserEntity user = session.get(UserEntity.class, loggedUser.getId());
            PortfolioEntity portfolio = user.getPortfolio();
            List<AssetEntity> assets = portfolio.getAssets();
            StockEntity stock = session.get(StockEntity.class, id);

            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);

            String jsonString = jb.toString();

            SellStockRequest sellStockRequest = gson.fromJson(jsonString, SellStockRequest.class);
            int price = sellStockRequest.getQuantity() * sellStockRequest.getSellingPrice();

            for(AssetEntity asset : assets){
                if(asset.getStock().getName().equals(stock.getName())){
                    if(asset.getQuantity() > sellStockRequest.getQuantity()){
                        asset.setQuantity(asset.getQuantity() - sellStockRequest.getQuantity());
                        session.update(asset);
                    }
                    else if(asset.getStock().getName().equals("Cash")){
                        asset.setQuantity(asset.getQuantity() + price);
                        session.update(asset);
                    }
                }
            }
            session.getTransaction().commit();
            session.close();

            return ResponseEntity.status(HttpStatus.OK)
                    .body("");
        }
        catch(IOException e){
            System.out.println("User controller, buy stock received POST, exception: " + e.getMessage());
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

        try{
            session.getTransaction().begin();

            UserEntity user = session.get(UserEntity.class, loggedUser.getId());
            PortfolioEntity portfolio = user.getPortfolio();
            StockEntity stock = session.get(StockEntity.class, id);
            int cash = AvailableCreditGetter.getAvailableCredit(portfolio);

            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);

            String jsonString = jb.toString();

            BuyStockRequest buyRequest = gson.fromJson(jsonString, BuyStockRequest.class);
            int price = buyRequest.getQuantity() * buyRequest.getBuyPrice();

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
                    AssetEntity assetToBuy = assetOperationService.getAssetById(portfolio, buyRequest.getStockId());
                    assetToBuy.setQuantity(assetToBuy.getQuantity() + buyRequest.getQuantity());
                }
                else{
                    //TODO: A lot of potential problems

                    StockEntity boughtStock =  stockService.getStockById(buyRequest.getStockId());
                    AssetEntity boughtAsset = new AssetEntity();
                    boughtAsset.setQuantity(buyRequest.getQuantity());
                    boughtAsset.setStock(boughtStock);
                    boughtAsset.setBuyPrice(boughtStock.getCurrentPrice());

                    portfolio.getAssets().add(boughtAsset);
                    session.update(portfolio);          //TODO: test
                    session.getTransaction().commit();

                    return ResponseEntity.status(HttpStatus.OK)
                            .body("");
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
                    .body("");
        }
        finally {
            factory.close();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("");
    }

    @PostMapping(value = "/createUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createUser(HttpServletRequest request){
        Session session = factory.getCurrentSession();

        Gson gson = new Gson();
        StringBuffer jb = new StringBuffer();
        String line = null;

        try{
            session.getTransaction().begin();

            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);

            String jsonString = jb.toString();

            UserEntity user = gson.fromJson(jsonString, UserEntity.class);

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
                    .body("");
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
}
