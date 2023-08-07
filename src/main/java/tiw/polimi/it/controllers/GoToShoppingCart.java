package tiw.polimi.it.controllers;

import org.thymeleaf.context.WebContext;
import tiw.polimi.it.beans.ShoppingCart;
import tiw.polimi.it.beans.User;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

@WebServlet("/GoToShoppingCart")
public class GoToShoppingCart extends HttpServletDBConnected {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResourceBundle lang = findLanguage(req);
        ServletContext servletContext = getServletContext();
        WebContext webContext = new WebContext(req, resp, servletContext);
        User user = (User) req.getSession().getAttribute("user");
        String page = "cartPage";
        List<ShoppingCart> cartList = (List<ShoppingCart>) req.getSession(false).getAttribute("cart");
        boolean emptyCart = cartList == null;


        webContext.setVariable("lang", lang);
        webContext.setVariable("user", user);

        if (!emptyCart) {
            int hashCode = cartList.hashCode();
            System.out.println("cartList : " + cartList);
            webContext.setVariable("hashCode", hashCode);
            webContext.setVariable("emptyCart", false);
        } else {
            webContext.setVariable("emptyCart", true);
        }
        webContext.setVariable("cartList", cartList);
//        webContext.setVariable("fromAddToCart",true);
        thymeleaf.process(page, webContext, resp.getWriter());
        return;

        //resp.sendRedirect(path + "/index");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
