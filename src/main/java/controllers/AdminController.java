package controllers;

import com.google.gson.Gson;
import entities.AssetEntity;
import entities.PortfolioEntity;
import entities.StockEntity;
import entities.UserEntity;
import exceptions.AccessToNonExistentResource;
import helpers.StockNameGenerator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import services.UserService;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;


    @RequestMapping
    private String getPage(){
        return "admin";
    }

    @DeleteMapping(value = "/deleteUser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id){
        System.out.println("AdminController /deleteUser");

        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK)
                    .body("User Deleted");
    }

    @DeleteMapping(value = "/deleteStock/{id}")
    public ResponseEntity<String> deleteStock(@PathVariable int id){
        SessionFactory factory = new Configuration()
                .addAnnotatedClass(AssetEntity.class)
                .addAnnotatedClass(PortfolioEntity.class)
                .addAnnotatedClass(StockEntity.class)
                .addAnnotatedClass(UserEntity.class)
                .buildSessionFactory();

        try (factory) {
            Session session = factory.openSession();
            session.getTransaction().begin();
            StockEntity stock = session.get(StockEntity.class, id);
            session.delete(stock);
            System.out.println("AdminController Stock: " + stock.toString() + " deleted.");
            session.getTransaction().commit();
            session.close();

            return ResponseEntity.status(HttpStatus.OK)
                    .body("Stock Deleted");
        }
        catch(NullPointerException nullPointerException){
            throw new AccessToNonExistentResource();
        }
    }

    @PostMapping(value = "/createStock", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createStock(HttpServletRequest request){
        SessionFactory factory = new Configuration()
                .addAnnotatedClass(AssetEntity.class)
                .addAnnotatedClass(PortfolioEntity.class)
                .addAnnotatedClass(StockEntity.class)
                .addAnnotatedClass(UserEntity.class)
                .buildSessionFactory();

        try (factory) {
            Session session = factory.openSession();
            Gson gson = new Gson();
            StringBuffer jb = new StringBuffer();
            String line = null;

            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);

            String jsonString = jb.toString();

            StockEntity stock = gson.fromJson(jsonString, StockEntity.class);

            System.out.println("AdminController /createStock stock: " + stock);

            session.getTransaction().begin();
            session.save(stock);
            session.getTransaction().commit();
            session.close();

            return ResponseEntity.status(HttpStatus.OK)
                    .body("Stock Created");
        } catch (Exception ex) {
            System.out.println("Dashboard servlet received POST, exception: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Stock Creation failed");
        }
    }

    @RequestMapping(value = "/forcePriceChanges")
    public ResponseEntity<String> forceUpdate(){
        SessionFactory factory = new Configuration()
                .addAnnotatedClass(AssetEntity.class)
                .addAnnotatedClass(PortfolioEntity.class)
                .addAnnotatedClass(StockEntity.class)
                .addAnnotatedClass(UserEntity.class)
                .buildSessionFactory();

        try (factory) {
            Session session = factory.openSession();
            List<StockEntity> stocks;
            System.out.println("AdminController /forcePriceChanges stock: ");
            session.getTransaction().begin();
            stocks = session.createQuery(" from StockEntity").getResultList();

            Random r = new Random();

            //exclude cash
            for (StockEntity stock : stocks) {
                if (!stock.getName().equals("Cash")) {
                    double percentChange = r.nextDouble() / 10;
                    boolean isRaising = r.nextBoolean();

                    if (isRaising) {
                        stock.setCurrentPrice(stock.getCurrentPrice() * (1 + percentChange));
                    } else {
                        stock.setCurrentPrice(stock.getCurrentPrice() * (1 - percentChange));
                    }
                    session.update(stock);
                }
            }
            session.getTransaction().commit();
            session.close();

            return ResponseEntity.status(HttpStatus.OK)
                    .body("Price update successful");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Price update failed");
        }
    }

    @RequestMapping(value = "/generateFakeStocks")
    public ResponseEntity<String> generateStocks(){
        SessionFactory factory = new Configuration()
                .addAnnotatedClass(AssetEntity.class)
                .addAnnotatedClass(PortfolioEntity.class)
                .addAnnotatedClass(StockEntity.class)
                .addAnnotatedClass(UserEntity.class)
                .buildSessionFactory();

        try (factory) {
            Session session = factory.openSession();
            Random random = new Random();
            session.getTransaction().begin();
            for (int i = 0; i < 30; i++) {
                StockEntity stock = new StockEntity();
                stock.setName(StockNameGenerator.generateName());
                stock.setCurrentPrice(random.nextInt(2500) + 1);
                session.save(stock);
            }
            session.getTransaction().commit();
            session.close();

            return ResponseEntity.status(HttpStatus.OK)
                    .body("Stock generation OK");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Stock generation failed");
        }
    }
}
