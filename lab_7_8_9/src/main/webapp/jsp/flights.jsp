<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 12/9/2023
  Time: 8:31 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <link rel="stylesheet" href="../css/bootstrap.css"/>
    <script src="../js/bootstrap.js"></script>
    <title>Flights</title>
</head>

<body>
<div class="container">
    <h2>Flights</h2>
    <!--Search Form -->
    <form action="/flight" method="get" id="seachFlightForm" role="form">
        <input type="hidden" id="searchAction" name="searchAction" value="searchByAirlineName">
        <div class="form-group col-xs-5">
            <input type="text" name="airlineName" id="airlineName" class="form-control" required="true" placeholder="Type the Airline Name"/>
        </div>
        <button type="submit" class="btn btn-info">
            <span class="glyphicon glyphicon-search"></span> Search
        </button>
        <br></br>
        <br></br>
    </form>

    <!--Flights List-->
    <form action="/flight" method="post" id="flightForm" role="form" >

        <c:choose>
            <c:when test="${not empty flights}">
                <table  class="table table-striped">
                    <thead>
                    <tr>
                        <td>#</td>
                        <td>Flight Number</td>
                        <td>Departure City</td>
                        <td>Arrival City</td>
                        <td>Departure Time</td>
                        <td>Arrival Time</td>
                    </tr>
                    </thead>
                    <c:forEach var="flight" items="${flights}">
                        <c:set var="classSucess" value="classSucess"/>
                        <tr class="${classSucess}">
                            <td>${flight.flightNumber}</td>
                            <td>${flight.origin}</td>
                            <td>${flight.destination}</td>
                            <td>${flight.departureTime}</td>
                            <td>${flight.arrivalTime}</td>
                        </tr>
                    </c:forEach>
                </table>
            </c:when>
            <c:otherwise>
                <br>
                <div class="alert alert-info">
                    No flights found matching your search criteria
                </div>
            </c:otherwise>
        </c:choose>
    </form>

</div>
</body>
</html>
