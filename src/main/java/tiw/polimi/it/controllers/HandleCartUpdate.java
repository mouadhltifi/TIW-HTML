package tiw.polimi.it.controllers;

import org.thymeleaf.context.WebContext;
import tiw.polimi.it.beans.ShoppingCart;
import tiw.polimi.it.beans.User;
import tiw.polimi.it.dao.OrderDAO;
import tiw.polimi.it.stuffToDelete.MyColors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

@WebServlet("/handleCartUpdate")
public class HandleCartUpdate extends HttpServletDBConnected {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResourceBundle lang = findLanguage(req);
        ServletContext servletContext = getServletContext();
        WebContext webContext = new WebContext(req, resp, servletContext);
        User user = (User) req.getSession().getAttribute("user");
        String page = "GotoOrders";
        List<ShoppingCart> cartList = (List<ShoppingCart>) req.getSession(false).getAttribute("cart");

        String sellerIdString = req.getParameter("sellerdID");
        //String cartString = req.getParameter("cartId"); //forse non serve
        String givenHashCodeString = req.getParameter("hashCode");
        OrderDAO orderDAO = new OrderDAO(conn);

        int sellerdId = -1;
        int hashCode = -1;
        ShoppingCart purchasedCart;

        boolean dataCorrupted = false;

        System.out.println(MyColors.ANSI_RED + "dentro a : " + getClass().getName() +MyColors.ANSI_RESET);
        System.out.println("_____________________________________________________________");
        System.out.println("sellerID : " + sellerIdString + "   hashCode : " + Integer.parseInt(givenHashCodeString) + cartList);
        System.out.println(MyColors.ANSI_RED + "_____________________________________________________________" +MyColors.ANSI_RESET);



        //check input
        if(sellerIdString!= null && !sellerIdString.isEmpty()&&
                givenHashCodeString!= null && !givenHashCodeString.isEmpty()) {

            try {
                sellerdId = Integer.parseInt(sellerIdString);
                hashCode = Integer.parseInt(givenHashCodeString);

                if(cartList!= null && hashCode==(cartList.hashCode())) {
                    //info not corrupted
                    System.out.println(MyColors.ANSI_GREEN + "tutto ok hash conforme" +MyColors.ANSI_RESET);

                    //removeCartFromCartList(cartList,sellerdId);


                    purchasedCart = removeCartFromCartList(cartList, sellerdId);


                    //occhio al possibile manual commit
                    if (orderDAO.insertNewOrder(purchasedCart,user)) {
                        //insert ok

                        System.out.println(MyColors.ANSI_GREEN + "ordine inserito correttamente" +MyColors.ANSI_RESET);

                        RequestDispatcher dispatcher = req.getRequestDispatcher(page);
                        dispatcher.forward(req, resp);


                    } else {
                        //insert ko

                        //restore previous conditions
                        cartList.add(purchasedCart);


                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error purchasing new order");

                    }
                }
                else {

                    System.out.println(MyColors.ANSI_BLUE + "non conforme" +MyColors.ANSI_RESET);

                    dataCorrupted = true;
                    System.out.println("data errore \n");
                    resp.sendRedirect("/error?code=500");
                }
            } catch (NumberFormatException | ServletException | IOException | SQLException e) {
                e.printStackTrace();
                resp.sendRedirect("/error?code=500");
            }
        }else {
            System.out.println(MyColors.ANSI_RED +"qualche input a null !!" +MyColors.ANSI_RESET);

            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,"something wrong with the input in HandleCartUpdate");
        }



    }


    private ShoppingCart removeCartFromCartList(List<ShoppingCart> cartList, int sellerdId) {
        //cartList.removeIf(cart -> cart.getSeller().getSellerId() == sellerdId);


        for (ShoppingCart cart : cartList
        ) {
            if (cart.getSeller().getSellerId() == sellerdId) {
                cartList.remove(cart);
                return cart;
            }
        }

        throw new IllegalArgumentException("no item found to remove");

    }
}
