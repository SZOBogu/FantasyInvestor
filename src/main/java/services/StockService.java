package services;

import entities.AssetEntity;
import entities.PortfolioEntity;
import entities.StockEntity;
import entities.UserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.springframework.stereotype.Service;

@Service
public class StockService {
//    private final SessionFactory factory = new Configuration()
//            .addAnnotatedClass(AssetEntity.class)
//            .addAnnotatedClass(PortfolioEntity.class)
//            .addAnnotatedClass(StockEntity.class)
//            .addAnnotatedClass(UserEntity.class)
//            .buildSessionFactory();

    public StockEntity getStockById(int id){
        SessionFactory factory = new Configuration()
                .addAnnotatedClass(AssetEntity.class)
                .addAnnotatedClass(PortfolioEntity.class)
                .addAnnotatedClass(StockEntity.class)
                .addAnnotatedClass(UserEntity.class)
                .buildSessionFactory();
        Session session = factory.getCurrentSession();

        try {
            session.getTransaction().begin();
            StockEntity stockEntity = session.get(StockEntity.class, id);
            session.getTransaction().commit();
            System.out.println("Stock service, getStockById stock: " + stockEntity);
            return stockEntity;
        }
        finally {
            session.close();
            factory.close();
        }
    }

    public StockEntity getStockByName(String name){
        SessionFactory factory = new Configuration()
                .addAnnotatedClass(AssetEntity.class)
                .addAnnotatedClass(PortfolioEntity.class)
                .addAnnotatedClass(StockEntity.class)
                .addAnnotatedClass(UserEntity.class)
                .buildSessionFactory();
        Session session = factory.getCurrentSession();

        try {
            session.getTransaction().begin();

            Query query= factory.getCurrentSession().
                    createQuery("from StockEntity where name=:name");
            query.setParameter("name", name);

            StockEntity stock =  (StockEntity) query.uniqueResult();
            System.out.println("Stock service, getStockByName stock: " + stock);
            session.getTransaction().commit();
            return stock;
        }
        finally {
            session.close();
            factory.close();
        }
    }
}
