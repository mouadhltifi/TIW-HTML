package tiw.polimi.it.controllers;

import org.thymeleaf.context.WebContext;
import tiw.polimi.it.beans.OnSale;
import tiw.polimi.it.beans.User;
import tiw.polimi.it.dao.OnSaleDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;

@WebServlet("/searchItems")
public class SearchItems extends HttpServletDBConnected{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext webContext = new WebContext(req,resp,getServletContext());
        ResourceBundle lang = findLanguage(req);
        String keySearch = req.getParameter("key");
        String resultPage = "risultati";
        boolean firstVersionPage = true;
        boolean secondVersionPage = false;

        User user = (User) req.getSession().getAttribute("user");
        OnSale onSale;

        OnSaleDAO onSaleDAO = new OnSaleDAO(conn,lang.getLocale().getLanguage(),lang.getLocale().getCountry());




        //check if the key search is correct
        if(keySearch == null) {
            resp.sendRedirect("/error?errorMessage=dbInvalidSearch");
            return;
        }

        try {

            //itemList = itemDAO.findItemsByKey(keySearch);

            onSale = onSaleDAO.findElementsByKey(keySearch);


            System.out.println(onSale.getIds_inVendita());


            webContext.setVariable("user",user);
            webContext.setVariable("itemList",onSale.getItems());
            webContext.setVariable("prices",onSale.getPrices());
            if(onSale.getIds_inVendita().isEmpty()) {
                webContext.setVariable("noItemFound",true);
            }else {
                webContext.setVariable("noItemFound",false);
            }
            webContext.setVariable("id_in_vendita",onSale.getIds_inVendita());
            webContext.setVariable("firstVersionPage",firstVersionPage);
            webContext.setVariable("secondVersionPage",secondVersionPage);
            webContext.setVariable("lang",lang);
            thymeleaf.process(resultPage,webContext,resp.getWriter());


        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            resp.sendRedirect("/error?code=500");
        }


    }
}
