package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @DeleteMapping(value = "/deleteUser/{id}")
    public String deleteUser(@PathVariable int id){

    }

    @DeleteMapping(value = "/deleteStock/{id}")
    public String deleteStock(@PathVariable int id){

    }

    @PostMapping(value = "/createStock")
    public String createStock(HttpServletRequest request){

    }

    @RequestMapping(value = "/forcePriceChanges")
    public String forceUpdate(){

    }
}
