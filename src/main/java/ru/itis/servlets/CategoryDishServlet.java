package ru.itis.servlets;

import org.springframework.context.ApplicationContext;
import ru.itis.dao.categoryDishDao.CategoryDishDao;
import ru.itis.dao.categoryDishDao.CategoryDishJdbcTemlateImpl;
import ru.itis.models.CategoryDish;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CategoryDishServlet extends HttpServlet {
    private CategoryDishDao categoryDishDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ApplicationContext context = (ApplicationContext)config.getServletContext().getAttribute("context");
        categoryDishDao = context.getBean(CategoryDishDao.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<CategoryDish> categoryDishes = categoryDishDao.findAll();
        req.setAttribute("categoryDishes", categoryDishes);
        req.getRequestDispatcher("/WEB-INF/jsp/categoryDishes.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CategoryDish categoryDish = CategoryDish.builder()
                .name(req.getParameter("name"))
                .build();
        categoryDishDao.save(categoryDish);

        req.setAttribute("categoryDishes", categoryDishDao.findAll());
        req.getRequestDispatcher("/WEB-INF/jsp/categoryDishes.jsp").forward(req, resp);
    }
}
