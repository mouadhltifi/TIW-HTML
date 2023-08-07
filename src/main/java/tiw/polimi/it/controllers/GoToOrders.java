package tiw.polimi.it.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.thymeleaf.context.WebContext;
import tiw.polimi.it.beans.Order;
import tiw.polimi.it.beans.User;
import tiw.polimi.it.dao.OrderDAO;
import tiw.polimi.it.dao.UserDAO;
import tiw.polimi.it.stuffToDelete.MyColors;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

@WebServlet("/GotoOrders")
public class GoToOrders extends HttpServletDBConnected {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResourceBundle lang = findLanguage(req);
        ServletContext servletContext = getServletContext();
        WebContext webContext = new WebContext(req, resp, servletContext);
        String path = getServletContext().getContextPath();
        String page = "ordersPage";
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        UserDAO userDAO = new UserDAO(conn);
        OrderDAO orderDAO = new OrderDAO(conn);
        User user = (User) req.getSession().getAttribute("user");
        List<Order> orderList;

        System.out.println(MyColors.ANSI_RED + "dentro a : " + getClass().getName() +MyColors.ANSI_RESET);
        System.out.println("_____________________________________________________________");
        System.out.println(MyColors.ANSI_RED + "_____________________________________________________________" +MyColors.ANSI_RESET);



        try {
            orderList = orderDAO.getOrdersByUserID(user.getUserId());

            System.out.println(orderList);

            webContext.setVariable("user", user);
            webContext.setVariable("orderList", orderList);
            webContext.setVariable("lang",lang);
            thymeleaf.process(page, webContext, resp.getWriter());
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
