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
        <h1 class="text-8xl text-cyan-500 pr-5 text-bold">404</h1>
        <div class="flex flex-col justify-start pl-5 gap-y-1">
            <h2 class="text-5xl text-bold font-bold">Page not found</h2>
            <p class="text-xl text-bold font-light">Please check the URL in the address bar and try again</p>
            <a href="<c:url value="/" />">
                <button type="button" class="w-fit shadow-md mt-3 py-2 px-6 rounded-md transition duration-300 bg-cyan-600 text-white">Go back</button>
            </a>
        </div>
    </div>
</div>
</body>
</html>