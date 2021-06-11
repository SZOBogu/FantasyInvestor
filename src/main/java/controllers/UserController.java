package controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {
    @GetMapping(value = "/portfolio")
    public String getPortfolio(Authentication authentication){
        //return authentication.getName();

        /*
        the Spring Security principal can only be retrieved as an Object and needs to be cast to the correct UserDetails instance:

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        System.out.println("User has authorities: " + userDetails.getAuthorities());
         */
    }

    @GetMapping(value = "/portfolio/{id}")
    public String getPortfolio(@PathVariable int id){

    }

    @GetMapping(value = "/stocks")
    public String getStockList(){

    }

    @GetMapping(value = "/stock/{id}")
    public String getStockInfo(@PathVariable int id){

    }

    @PostMapping(value = "/stock/{id}/buy")
    public String getStockInfo(@PathVariable int id, HttpServletRequest request){

    }

    @PostMapping(value = "/stock/{id}/sell")
    public String getStockInfo(@PathVariable int id, HttpServletRequest request){

    }
}
