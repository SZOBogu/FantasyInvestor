package controllers;

import com.google.gson.Gson;
import entities.AssetEntity;
import entities.PortfolioEntity;
import entities.StockEntity;
import entities.UserEntity;
import exceptions.AccessToNonExistentResource;
import exceptions.InsufficientFundsException;
import exceptions.NotEnoughStocksToSellException;
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
import responses.StockListResponse;
import services.AssetOperationService;
import services.StockService;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@Controller
public class StocksController {
    @Autowired
    private AssetOperationService assetOperationService;

    @Autowired
    private StockService stockService;

    @RequestMapping(value="/stocks")
    public String getStockPage(){
        return "stocks";
    }

    @GetMapping(value = "/stockList", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getStockList(){
        SessionFactory factory = new Configuration()
                .addAnnotatedClass(AssetEntity.class)
                .addAnnotatedClass(PortfolioEntity.class)
                .addAnnotatedClass(StockEntity.class)
                .addAnnotatedClass(UserEntity.class)
                .buildSessionFactory();
        Session session = factory.getCurrentSession();
        Gson gson = new Gson();
        StockListResponse response = new StockListResponse();
        List<StockEntity> stocks;

        System.out.println("StocksController: /stocks ");

        try{
            session.getTransaction().begin();
            stocks = session.createQuery(" from StockEntity").getResultList();

            for(StockEntity stock : stocks){
                response.add(new StockDataDigest(stock));
            }

            String json = gson.toJson(response);

            System.out.println("StocksController: /stocks response: " + response);

            session.getTransaction().commit();
            session.close();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(json);
        }
        finally {
            factory.close();
        }
    }

    @GetMapping(value = "/stock/{id}")
    public String getStockInfo(@ModelAttribute("stock") StockEntity stockEntity, Model model, @PathVariable int id){
        SessionFactory factory = new Configuration()
                .addAnnotatedClass(AssetEntity.class)
                .addAnnotatedClass(PortfolioEntity.class)
                .addAnnotatedClass(StockEntity.class)
                .addAnnotatedClass(UserEntity.class)
                .buildSessionFactory();
        Session session = factory.getCurrentSession();
        System.out.println("StocksController: /stock/{id} ");
        try{
            session.getTransaction().begin();
            StockEntity stock = session.get(StockEntity.class, id);
            model.addAttribute("stock", stock);

            System.out.println("StocksController: /stock/{id} stock: " + stock);

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
        SessionFactory factory = new Configuration()
                .addAnnotatedClass(AssetEntity.class)
                .addAnnotatedClass(PortfolioEntity.class)
                .addAnnotatedClass(StockEntity.class)
                .addAnnotatedClass(UserEntity.class)
                .buildSessionFactory();
        Session session = factory.getCurrentSession();
        UserEntity loggedUser = (UserEntity) authentication.getPrincipal();

        System.out.println("StocksController: /stock/{id}/sell loggedUser: " + loggedUser);

        Gson gson = new Gson();
        StringBuffer jb = new StringBuffer();
        String line;

        try{
            session.getTransaction().begin();
            UserEntity user = session.get(UserEntity.class, loggedUser.getId());
            PortfolioEntity portfolio = user.getPortfolio();
            List<AssetEntity> assets = portfolio.getAssets();
            StockEntity stock = session.get(StockEntity.class, id);
            AssetEntity cashAssetEntity = null;

            System.out.println("StocksController: /stock/{id}/sell user: " + user);
            System.out.println("StocksController: /stock/{id}/sell stock: " + stock);

            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);

            String jsonString = jb.toString();

            SellStockRequest sellStockRequest = gson.fromJson(jsonString, SellStockRequest.class);

            int price = (int)(sellStockRequest.getQuantity() * stock.getCurrentPrice());

            for(AssetEntity asset : assets) {
                if (asset.getStock().getName().equals(stock.getName())) {
                    if (asset.getQuantity() > sellStockRequest.getQuantity()) {
                        asset.setQuantity(asset.getQuantity() - sellStockRequest.getQuantity());
                        session.update(asset);

                    }
                    else if(asset.getQuantity() == sellStockRequest.getQuantity())
                        session.delete(asset);

                    else
                        throw new NotEnoughStocksToSellException();
                }
                if(asset.getStock().getName().equals("Cash")) {
                    cashAssetEntity = asset;
                }
            }

            cashAssetEntity.setQuantity(cashAssetEntity.getQuantity() + price);
            session.update(cashAssetEntity);
            session.update(portfolio);

            session.getTransaction().commit();
            session.close();

            return ResponseEntity.status(HttpStatus.OK)
                    .body("Stock sold");
        }
        catch(IOException e){
            System.out.println("StocksController, sell stock received POST, exception: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("");
        }
        catch(NullPointerException nullPointerException){
            throw new AccessToNonExistentResource();
        }
        finally {
            factory.close();
        }
    }

    @PostMapping(value = "/stock/{id}/buy", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> buyStock(@PathVariable int id, HttpServletRequest request, Authentication authentication){
        SessionFactory factory = new Configuration()
                .addAnnotatedClass(AssetEntity.class)
                .addAnnotatedClass(PortfolioEntity.class)
                .addAnnotatedClass(StockEntity.class)
                .addAnnotatedClass(UserEntity.class)
                .buildSessionFactory();
        Session session = factory.getCurrentSession();
        UserEntity loggedUser = (UserEntity) authentication.getPrincipal();

        Gson gson = new Gson();
        StringBuffer jb = new StringBuffer();
        String line = null;

        System.out.println("StocksController: /stock/{id}/buy loggedUser: " + loggedUser);

        try{
            session.getTransaction().begin();

            UserEntity user = session.get(UserEntity.class, loggedUser.getId());
            PortfolioEntity portfolio = user.getPortfolio();
            StockEntity stock = session.get(StockEntity.class, id);
            int cash = AvailableCreditGetter.getAvailableCredit(portfolio);

            System.out.println("StocksController: /stock/{id}/buy user: " + user);
            System.out.println("StocksController: /stock/{id}/buy stock: " + stock);

            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);

            String jsonString = jb.toString();

            BuyStockRequest buyStockRequest = gson.fromJson(jsonString, BuyStockRequest.class);
            int price = (int)(buyStockRequest.getQuantity() * stock.getCurrentPrice());

            System.out.println("StocksController: /stock/{id}/buy buyRequest: " + buyStockRequest);

            if(cash > price){
                AssetEntity cashAsset = portfolio.getCash();
                cashAsset.setQuantity(cashAsset.getQuantity() - price);
                session.save(cashAsset);

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
                    assetToBuy.setBuyPrice(assetToBuy.getStock().getCurrentPrice());

                    session.save(assetToBuy);

                    session.getTransaction().commit();
                    session.close();

                    return ResponseEntity.status(HttpStatus.OK)
                            .body("Stock bought");
                }
                else{
                    StockEntity boughtStock =  stockService.getStockById(buyStockRequest.getStockId());

                    AssetEntity boughtAsset = new AssetEntity();

                    boughtAsset.setQuantity(buyStockRequest.getQuantity());
                    boughtAsset.setStock(boughtStock);
                    boughtAsset.setBuyPrice(boughtStock.getCurrentPrice());
                    boughtAsset.setPortfolioEntity(portfolio);

                    portfolio.getAssets().add(boughtAsset);
                    session.save(boughtAsset);
                    session.update(portfolio);

                    session.getTransaction().commit();
                    session.close();

                    return ResponseEntity.status(HttpStatus.OK)
                            .body("Stock bought");
                }
            }

            else{
                throw new InsufficientFundsException();
            }
        }
        catch(IOException ioException){
            System.out.println("StocksController, sell stock received POST, exception: " + ioException.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("IOException");
        }
        catch(NullPointerException nullPointerException){
            throw new AccessToNonExistentResource();
        }
        finally {
            factory.close();
        }
    }
}
