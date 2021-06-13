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

import java.util.Locale;

@Service
public class StockService {
    private final SessionFactory factory = new Configuration()
            .addAnnotatedClass(AssetEntity.class)
            .addAnnotatedClass(PortfolioEntity.class)
            .addAnnotatedClass(StockEntity.class)
            .addAnnotatedClass(UserEntity.class)
            .buildSessionFactory();

    public StockEntity getStockById(int id){
        Session session = factory.getCurrentSession();

        try {
            session.getTransaction().begin();
            StockEntity stockEntity = session.get(StockEntity.class, id);
            return stockEntity;
        }
        finally {
            session.close();
            factory.close();
        }
    }

    public StockEntity getStockByName(String name){
        Session session = factory.getCurrentSession();

        try {
            session.getTransaction().begin();

            Query query= factory.getCurrentSession().
                    createQuery("from StockEntity where name=:name");
            query.setParameter("name", name);

            return (StockEntity) query.uniqueResult();
        }
        finally {
            session.close();
            factory.close();
        }
    }
}
