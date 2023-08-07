package tiw.polimi.it.controllers;

import com.google.gson.Gson;
import org.thymeleaf.context.WebContext;
import tiw.polimi.it.beans.*;
import tiw.polimi.it.dao.ItemDAO;
import tiw.polimi.it.dao.OnSaleDAO;
import tiw.polimi.it.dao.SellerDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

@WebServlet("/ItemDetails")
public class GetItemDetails extends HttpServletDBConnected {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        /*
        TODO: implementare ... il numero degli articoli
         e valore totale degli articoli di quel fornitore che l’utente ha già messo nel carrello.
         */




        ResourceBundle lang = findLanguage(req);
        OnSaleDAO onSaleDAO = new OnSaleDAO(conn, lang.getLocale().getLanguage(), lang.getLocale().getCountry());

        User user = (User) req.getSession().getAttribute("user");
        int userItemId;
        OnSale onSale;
        WebContext webContext = new WebContext(req, resp, getServletContext());
        boolean firstVersionPage = false;
        boolean secondVersionPage = true;
        String page = "risultati";
        Gson gson = new Gson();
        LinkedList<Item> itemList = null;
        LinkedList<Double> itemIdsFromCookies = null;

        /*
        L’utente può selezionare mediante un click un elemento dell'elenco e visualizzare nella
        stessa pagina i dati completi e l’elenco dei fornitori che lo vendono (questa azione rende
        l’articolo “visualizzato”).


         Per ogni fornitore in tale elenco compaiono: nome, valutazione, prezzo unitario, fasce di spesa
         di spedizione, importo minimo della spedizione gratuita e il numero degli articoli
         e valore totale degli articoli di quel fornitore che l’utente ha già messo nel carrello.

         
         Accanto all’offerta di ciascun fornitore compare un campo di input intero (quantità)
         e un bottone METTI NEL CARRELLO. */

        Cookie[] cookies = req.getCookies();
        for (Cookie c : cookies
        ) {
            if (c.getName().equalsIgnoreCase("itemList")) {

                String jsonCookie = URLDecoder.decode(c.getValue(), StandardCharsets.UTF_8);
                itemIdsFromCookies = gson.fromJson(jsonCookie, (Type) LinkedList.class);

                /*
                if itemdIds is null ==> cookies corrupted or not present
                 */
                if (itemIdsFromCookies == null) {

                    System.out.println("itemId  null");
                    throw new IllegalArgumentException(lang.getString("errorCookies"));
                    /*TODO
                        create a new Random cookie item id list
                     */
                }
            }

        }

        //get the itemId
        try {
            userItemId = Integer.parseInt(req.getParameter("itemId"));

            if (userItemId < 0) {

                System.out.println("userItemId < 0");
                resp.sendRedirect("/error?code=500");
            }

            //segnalo elemento come visualizzato
            if (itemIdsFromCookies == null) {
                ItemDAO itemDAO = new ItemDAO(conn);
                itemIdsFromCookies = itemDAO.getRandomItemsIds();
            }
            if (!itemIdsFromCookies.contains(userItemId - 0.0)) {
                itemIdsFromCookies.poll();
                itemIdsFromCookies.addLast(userItemId - 0.0);
            }

            String jsonString = gson.toJson(itemIdsFromCookies, LinkedList.class);
            System.out.println(jsonString);
            Cookie myCookie = new Cookie("itemList", URLEncoder.encode(jsonString, StandardCharsets.UTF_8));
            System.out.println(myCookie);
            resp.addCookie(myCookie);

            onSale = onSaleDAO.getInfoByItemId(userItemId);
            for (Seller s:onSale.getSellers()
                 ) {
                System.out.println(s.getsPolicy());
            }
            System.out.println(onSale.getIds_inVendita());

            List<ShoppingCart> cartList = (List<ShoppingCart>) req.getSession(false).getAttribute("cart");

            HashMap<Integer,List<Double>> pricesOnCart = prepareHashMap_SellerId_TotalPrice(cartList,onSale.getSellers());




            webContext.setVariable("lang", lang);
            webContext.setVariable("user", user);
            webContext.setVariable("selectedItem", onSale.getItems().get(0));
            webContext.setVariable("sellers", onSale.getSellers());
            webContext.setVariable("prices", onSale.getPrices());
            webContext.setVariable("pricesOnCart", pricesOnCart);
            webContext.setVariable("hashCode",cartList== null ? null : cartList.hashCode());
            webContext.setVariable("id_in_vendita",onSale.getIds_inVendita());
            webContext.setVariable("firstVersionPage", firstVersionPage);
            webContext.setVariable("secondVersionPage", secondVersionPage);
            thymeleaf.process(page, webContext, resp.getWriter());

        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
            resp.sendRedirect("/error?code=500");
            return;
        }catch (Exception e) {
            e.printStackTrace();
            return;
        }


    }

    private HashMap<Integer, List<Double>> prepareHashMap_SellerId_TotalPrice(List<ShoppingCart> cartList, List<Seller> sellers) {
        HashMap<Integer,List<Double>> result = new HashMap<>();

        List<Double> qty_totalPrice = new ArrayList<>();
        double truncatedDouble;


        System.out.println("creo HaskMap \n " + cartList +"\n" + sellers );
        if(cartList== null || cartList.isEmpty()) return null;
        for (ShoppingCart cart:cartList
        ) {
            for (Seller seller: sellers
            ) {
                if(seller.getSellerId()==cart.getSeller().getSellerId()) {
                    System.out.println("trovato da stesso venditore");

                    truncatedDouble = BigDecimal.valueOf(cart.getTotalPrice())
                            .setScale(3, RoundingMode.HALF_UP)
                            .doubleValue();

                    qty_totalPrice.add(truncatedDouble);
                    qty_totalPrice.add(cart.getQuantity()-0.0);
                    result.put(seller.getSellerId(),qty_totalPrice);
                    break;
                }
            }
        }
        System.out.println("resultSet : "+result);
        return result;
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
