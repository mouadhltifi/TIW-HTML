package tiw.polimi.it.controllers;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.thymeleaf.context.WebContext;
import tiw.polimi.it.beans.*;
import tiw.polimi.it.dao.ItemDAO;
import tiw.polimi.it.dao.OnSaleDAO;
import tiw.polimi.it.dao.UserDAO;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

@WebServlet("/addToCart")
public class AddToCart extends HttpServletDBConnected{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ResourceBundle lang = findLanguage(req);
        ServletContext servletContext = getServletContext();
        WebContext webContext = new WebContext(req, resp, servletContext);
        String path = getServletContext().getContextPath();
        String page = "cartPage";
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        User user = (User) req.getSession().getAttribute("user");
        ItemDAO itemDAO = new ItemDAO(conn);
        OnSaleDAO onSaleDAO = new OnSaleDAO(conn);
        OnSale onSale;
        Seller seller;
        Item itemAdded;
        System.out.println("eccoci in addtocart id in vendita : "
                + req.getParameter("id_in_vendita")+ " qty : "
                + req.getParameter("qty") + "itemId : "
                + req.getParameter("itemId"));

        boolean dataError = false;
        String quantityString = req.getParameter("qty");
        //identifica l'istanza di un articolo messo in vendita da un determinato fornitore
        String idInVenditaString = req.getParameter("id_in_vendita");
        String itemIdString = req.getParameter("itemId");
        List<ShoppingCart> cartList = (List<ShoppingCart>) req.getSession(false).getAttribute("cart");
        int quantity = 0;



        //ShoppingCart shoppingCart = new ShoppingCart();

        if(quantityString != null && idInVenditaString !=null && itemIdString!= null){
            try {

                System.out.println("prima di conversione");


                onSale = onSaleDAO.getFromIdOnSale(Integer.parseInt(idInVenditaString));
                System.out.println(onSale);
                itemAdded = onSale.getItems().get(0);
                seller = onSale.getSellers().get(0);
                quantity = Integer.parseInt(quantityString);

                if(onSale.getItems()!=null && quantity >0){
                    //itemId is not corrupted
                    if(cartList == null) {
                        System.out.println("carrello vuoto");
                        //cart is empty
                        cartList = new LinkedList<>();
                        addToCartList(cartList,itemAdded,onSale,quantity,seller);
                    }
                    else {
                        System.out.println("cartList not null : " + cartList);
                        findFromCartList(cartList,quantity,onSale);

                    }


                    //store into session
                    req.getSession().setAttribute("cart",cartList);

                    //staff to delte
                    System.out.println("cartList : " + cartList);


                    webContext.setVariable("lang", lang);
                    webContext.setVariable("user", user);
                    webContext.setVariable("cartList",cartList);
                    webContext.setVariable("fromAddToCart",true);
                    thymeleaf.process(page, webContext, resp.getWriter());
                    return;


                    //prepare WebContext

                }else {
                    //data corrupted
                    System.out.println("errore nei file di input");
                    dataError = true;
                    resp.sendRedirect("/error?code=500");

                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
                resp.sendRedirect("/error?code=500");
            }
        }else {
            System.out.println("qualcosa è a null : "+ quantityString + idInVenditaString + itemIdString);

            //send redirect
        }


    }




    private void findFromCartList(List<ShoppingCart> cartList, int quantity, OnSale onSale) {

        for (ShoppingCart cart : cartList
        ) {
            if(cart.getSeller().getSellerId() == onSale.getSellers().get(0).getSellerId()) {
                //buying other items from the same seller
                System.out.println("compro da stesso venditore");
                //la lista di carrelli è divisa in carrelli dei singoli venditori
                //ogni carrello di venditore ha la lista dei suoi oggetti
                cart.updateSellerCart(onSale.getSellers().get(0),quantity,onSale.getItems().get(0),onSale.getPrices().get(0));
                return;
        }
        }
            //buying for the first time to this seller
            System.out.println("acquisto da nuovo venditore");

            addToCartList(cartList,onSale.getItems().get(0),onSale,quantity,onSale.getSellers().get(0));

    }


    private void addToCartList(List<ShoppingCart> cartList, Item itemAdded, OnSale onSale, int quantity, Seller seller) {
        List<LightItem> tempList = new LinkedList<>();
        tempList.add(new LightItem(itemAdded.getName(),itemAdded.getId_item(),quantity));
        cartList.add(new ShoppingCart(
                tempList,
                onSale.getPrices().get(0)*quantity,
                quantity,
                seller
                ));
    }


}
