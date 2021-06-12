package services;

import entities.AssetEntity;
import entities.PortfolioEntity;
import entities.StockEntity;
import entities.UserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private  SessionFactory factory = new Configuration()
            .addAnnotatedClass(AssetEntity.class)
            .addAnnotatedClass(PortfolioEntity.class)
            .addAnnotatedClass(StockEntity.class)
            .addAnnotatedClass(UserEntity.class)
            .buildSessionFactory();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Session session = factory.getCurrentSession();

        try{
            session.getTransaction().begin();
            List<UserEntity> users = session.createQuery(" from UserEntity").getResultList();
            for(UserEntity user : users){
                if(user.getUsername().equals(username)){
                    return user;
                }
            }
            session.close();
            throw new UsernameNotFoundException("loadUserByUsername exception: username not found\n");
        }
        finally{
            factory.close();
        }
    }

    public void deleteUser(int id){
        Session session = factory.getCurrentSession();

        try{
            UserEntity user = session.get(UserEntity.class, id);
            PortfolioEntity portfolio = user.getPortfolio();
            List<AssetEntity> assets = portfolio.getAssets();

            for(AssetEntity assetEntity : assets){
                assetEntity.getStock().getAssets().remove(assetEntity);
                session.update(assetEntity.getStock());
                session.delete(assetEntity);
            }

            session.delete(portfolio);
            session.delete(user);
            session.close();
        }
        finally {
            factory.close();
        }
    }
}
