<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<html>
<%@ include file="Head.jsp" %>
<body>
<!-- Header -->
    <div class="mb-20"></div> <!-- Navbar -->
    <div class="container rounded-lg text-2xl font-Montserrat m-auto flex flex-col">
        <c:if test="${emailSent == true}">
            <img class="w-52 self-center " src="../img/emailsent.svg" alt="">
            <p class="text-center">Email sent successfully</p>
            <p class="text-center">the seller is informed and will contact you shortly</p>
            <a href="<c:url value="/" />" class="self-center">
            <button type="button" class=" w-fit shadow-md m-3 py-2 px-6 rounded-md transition duration-300 bg-cyan-600 text-white">Go back to home</button>
            </a>
        </c:if>
        <c:if test="${emailSent == false}">
            <p class="text-center">Email sent successfully</p>
            <script>alert("Email could not be sent")</script>
        </c:if>
    </div>
</body>
</html>
