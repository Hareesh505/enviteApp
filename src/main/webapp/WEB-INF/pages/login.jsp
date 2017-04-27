<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%

    RequestDispatcher rd = request.getRequestDispatcher("/index");
    rd.forward(request, response);

%>