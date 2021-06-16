package entities;


import com.sun.istack.NotNull;
import helpers.PortfolioValueCalculator;

import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.sound.sampled.Port;
import java.util.Arrays;
import java.util.Collection;

@Entity
@Table(name = "user", schema = "FantasyInvestor")
public class UserEntity implements UserDetails, Comparable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    @Basic
    @Column(name = "username")
    private String username;

    @Basic
    @Column(name = "password")
    private String password;

    @OneToOne(targetEntity = PortfolioEntity.class, cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "portfolio_id")
    private PortfolioEntity portfolio;

    @Basic
    @Column(name = "role")
    private String role;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public PortfolioEntity getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(PortfolioEntity portfolio) {
        this.portfolio = portfolio;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.username.equals("admin")){
            return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        else{
            return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    @Override
    public int compareTo(@NotNull Object o) {
        UserEntity user = (UserEntity)o;
        return Double.compare(PortfolioValueCalculator.getPortfolioValue(this.portfolio), PortfolioValueCalculator.getPortfolioValue(user.getPortfolio()));
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
