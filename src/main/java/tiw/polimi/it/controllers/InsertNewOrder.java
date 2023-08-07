//package tiw.polimi.it.controllers;
//
//import org.thymeleaf.context.WebContext;
//import tiw.polimi.it.beans.Order;
//import tiw.polimi.it.beans.ShoppingCart;
//import tiw.polimi.it.beans.User;
//import tiw.polimi.it.dao.OrderDAO;
//import tiw.polimi.it.stuffToDelete.MyColors;
//
//import javax.servlet.RequestDispatcher;
//import javax.servlet.ServletContext;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.ResourceBundle;
//
//@WebServlet("/insertNewOrder")
//public class InsertNewOrder extends HttpServletDBConnected {
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        ResourceBundle lang = findLanguage(req);
//        ServletContext servletContext = getServletContext();
//        WebContext webContext = new WebContext(req, resp, servletContext);
//        User user = (User) req.getSession().getAttribute("user");
//        String page = "GotoOrders";
//        ShoppingCart purchasedCart;
//        Order newOrder;
//        RequestDispatcher dispatcher = req.getRequestDispatcher(page);
//
//        List<ShoppingCart> cartList = (List<ShoppingCart>) req.getSession(false).getAttribute("cart");
//        List<ShoppingCart> oldCartList = (List<ShoppingCart>) req.getSession(false).getAttribute("oldCart");
//
//        String sellerIdString = req.getParameter("sellerdID");
//        //String cartString = req.getParameter("cartId");
//        String givenHashCodeString = req.getParameter("hashCode");
//
//
//        System.out.println(MyColors.ANSI_RED + "dentro a : " + getClass().getName() +MyColors.ANSI_RESET);
//        System.out.println("_____________________________________________________________");
//        System.out.println("sellerID : " + sellerIdString + "   hashCode : " + Integer.parseInt(givenHashCodeString) + oldCartList);
//        System.out.println(MyColors.ANSI_RED + "_____________________________________________________________" +MyColors.ANSI_RESET);
//
//
//
//
//        OrderDAO orderDAO = new OrderDAO(conn);
//
//        int sellerdId = -1;
//        int hashCode = -1;
//
//
//        if (sellerIdString != null && !sellerIdString.isEmpty() &&
//                givenHashCodeString != null && !givenHashCodeString.isEmpty()) {
//
//            try {
//                sellerdId = Integer.parseInt(sellerIdString);
//                hashCode = Integer.parseInt(givenHashCodeString);
//
//
//                    //purchasedCart = removeCartFromCartList(cartList, sellerdId);
//
//
//                    //occhio al possibile manual commit
//                    if (orderDAO.insertNewOrder(purchasedCart,user)) {
//                        //insert ok
//                        dispatcher.forward(req, resp);
//
//                        webContext.setVariable("lang", lang);
//                        webContext.setVariable("user", user);
//                        webContext.setVariable("cartList", cartList);
//
//                        thymeleaf.process(page, webContext, resp.getWriter());
//
//                    } else {
//                        //insert ko
//
//                        //restore previous conditions
//                        cartList.add(purchasedCart);
//
//
//                        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error purchasing new order");
//
//                    }
//
//
//
//            } catch (NumberFormatException | ServletException | IOException e) {
//                e.printStackTrace();
//                resp.sendRedirect("/error?code=500");
//            }
//        }
//    }
//
//
//    private ShoppingCart removeCartFromCartList(List<ShoppingCart> cartList, int sellerdId) {
//        //cartList.removeIf(cart -> cart.getSeller().getSellerId() == sellerdId);
//
//
//        for (ShoppingCart cart : cartList
//        ) {
//            if (cart.getSeller().getSellerId() == sellerdId) {
//                cartList.remove(cart);
//                return cart;
//            }
//        }
//
//        throw new IllegalArgumentException("no item found to remove");
//
//    }
//}
