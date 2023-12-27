package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.controller.ControllerFactoryImpl;
import org.example.controller.FlightController;

import java.io.IOException;
import java.sql.SQLException;


@WebServlet(name = "FlightServlet",
        urlPatterns = { "/flight" })
public class FlightServlet extends HttpServlet {
    FlightController flightController;

    @Override
    public void init() {
        var factory = new ControllerFactoryImpl();
        try {
            flightController = factory.getFlightController();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("searchAction");
        if (action != null) {

            if (action.equals("searchByAirlineName")) {
                String airlineName = request.getParameter("airlineName");
                request.setAttribute("flights", flightController.findAllByAirline(airlineName));
                request.getRequestDispatcher("/jsp/flights.jsp").forward(request, response);
            } else {
                throw new ServletException("Missing or invalid action.");
            }
        }
    }
}
