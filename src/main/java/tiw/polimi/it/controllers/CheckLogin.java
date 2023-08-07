package tiw.polimi.it.controllers;

import org.thymeleaf.context.WebContext;
import tiw.polimi.it.beans.User;
import tiw.polimi.it.dao.UserDAO;
import tiw.polimi.it.utils.Const;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;

@WebServlet("/checkLogin")
public class CheckLogin extends HttpServletDBConnected {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResourceBundle lang = findLanguage(req);
        ServletContext context = getServletContext();
        WebContext webContext = new WebContext(req,resp,context);
        HttpSession session;

        String loginPage = "loginPage";
        String homePage = getServletContext().getContextPath() + "/GoToHome";
        String indexPage = getServletContext().getContextPath() + "/index";
        String uMail = req.getParameter("email");
        String uPassword = req.getParameter("password");
        String login = req.getParameter("login");
        UserDAO userDAO = new UserDAO(conn,lang.getLocale().getLanguage(),lang.getLocale().getCountry());
        User user;
        boolean loginError = false , dataError = false;


        // request processed only if the parameters are sent correctly
        if(login != null && uMail != null && !uMail.isEmpty() && uPassword != null && !uPassword.isEmpty() ) {

            try {
                user = userDAO.findUser(uMail,uPassword);

                if(user != null) {
                    session = req.getSession(true);
                    session.setAttribute("user",user);
                } else {
                    loginError = true;
                    dataError = false;
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
                resp.sendRedirect("/error?code=500");
                return;

            }




            //if errors accured , user is sent back to its inital page
            if(loginError) {
                try {
                    webContext.setVariable("lang",lang);
                    webContext.setVariable("loginError",loginError);
                    thymeleaf.process(loginPage,webContext,resp.getWriter());
                }catch (Exception e) {
                    e.printStackTrace();
                }
            } else { //no errors
                resp.sendRedirect(homePage);
            }
        }else { //bad request
            resp.sendRedirect(indexPage);
        }
//
//                ResourceBundle lang = findLanguage(req);
//        ServletContext context = getServletContext();
//        WebContext webContext = new WebContext(req, resp, context);
//        HttpSession session;
//        User user;
//        UserDAO userDAO = new UserDAO(conn);
//        String path = getServletContext().getContextPath() + "/GoToHome";
//        String email = req.getParameter("email");
//        String password = req.getParameter("password");
//
//
//        // the request is processed only if the parameters sent are correct
//        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
//            //resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "dbAccountInvalidCredentials");
//            resp.sendRedirect("/error?code=500");
//            //throw new UnavailableException("problema leggendo input");
//            return;
//        }
//
//        try {
//            user = userDAO.findUser(email, password);
//
//            if (user != null) {
//                session = req.getSession(true);
//                session.setAttribute("user", user);
//
//            } else {
//                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "user not found");
//                return;
//            }
//        } catch (SQLException sqlException) {
//            sqlException.printStackTrace();
//            return;
//        }
//
//
//        resp.sendRedirect(path);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("/index");
    }
}
