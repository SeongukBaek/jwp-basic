package next.web;

import core.db.DataBase;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

@WebServlet(value = {"/user/updateForm", "/user/update"})
public class UpdateUserServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(UpdateUserServlet.class);
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        User user = DataBase.findUserById(userId);

        log.debug("userId : {}", userId);

        HttpSession httpSession = req.getSession();
        Object value = httpSession.getAttribute("user");

        if (value == null) {
            return;
        }

        User sessionUser = (User) value;

        if (!sessionUser.getUserId().equals(userId)) {
            return;
        }

        req.setAttribute("user", user);
        RequestDispatcher rd = req.getRequestDispatcher("/user/updateForm.jsp");
        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = DataBase.findUserById(req.getParameter("userId"));

        User updateUser = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"),
                req.getParameter("email"));

        log.debug("Update User : {}", updateUser);

        user.update(updateUser);
        resp.sendRedirect("/user/list");
    }
}
