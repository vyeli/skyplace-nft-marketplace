<%--
  Created by IntelliJ IDEA.
  User: Administrador
  Date: 17/4/2022
  Time: 18:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<!DOCTYPE html>
<html>
<%@ include file="Head.jsp" %>
<body class="mx-5">
<div class="h-screen flex flex-col">
    <!-- Header -->
    <%@ include file="/WEB-INF/components/navbar.jsp" %>
    <!-- Text -->
    <div class="flex flex-col sm:flex-row justify-center items-center flex-grow sm:divide-x h-">
        <h1 class="text-8xl text-cyan-500 pr-5 text-bold">500</h1>
        <div class="flex flex-col justify-start pl-5 gap-y-1">
            <h2 class="text-5xl text-bol font-bold">Internal server error</h2>
            <p class="text-xl text-bol font-light">Oops! something went wrong. Try again or contact the administrators if the problem persists</p>
            <a href="<c:url value="/" />">
                <button type="button" class="w-fit shadow-md mt-3 py-2 px-6 rounded-md transition duration-300 bg-cyan-600 text-white">Go back</button>
            </a>
        </div>
    </div>
</div>
</body>
</html>