<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<!DOCTYPE html>
<html lang="en">
<%@ include file="Head.jsp" %>
<body class="h-full">
<%@ include file="../components/navbar.jsp" %>
<div class="min-h-full flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-md w-full space-y-8">
        <div>
            <img class="mx-auto h-12 w-auto" src="<c:url value='/resources/logo.svg' />" alt="logo">
            <h2 class="mt-6 text-center text-3xl font-extrabold text-gray-900"><spring:message code="login.signin"/></h2>
        </div>
        <form class="mt-8 space-y-6" action="<c:url value='/login'/>" method="POST">
            <div class="rounded-md shadow-sm -space-y-px">
                <div>
                    <label for="email-address" class="sr-only"><spring:message code="login.email"/></label>
                    <input id="email-address" name="email" type="email" autocomplete="email" required class="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-t-md focus:outline-none focus:ring-cyan-500 focus:border-cyan-500 focus:z-10 sm:text-sm" placeholder="<spring:message code="login.email"/>">
                </div>
                <div>
                    <label for="password" class="sr-only"><spring:message code="login.password"/>
                    </label>
                    <input id="password" name="password" type="password" autocomplete="current-password" required class="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-b-md focus:outline-none focus:ring-cyan-500 focus:border-cyan-500 focus:z-10 sm:text-sm" placeholder="<spring:message code="login.password"/>">
                </div>
            </div>
            <c:if test="${param.error != null}">
            <div>
                <p class="text-red-600"><spring:message code="login.error"/></p>
            </div>
            </c:if>
            <div class="flex items-center justify-between">
                <div class="flex items-center">
                    <input id="rememberme" name="rememberme" type="checkbox" class="h-4 w-4 text-cyan-600 focus:ring-cyan-500 border-gray-300 rounded">
                    <label for="rememberme" class="ml-2 block text-sm text-gray-900"> <spring:message code="login.rememberme"/> </label>
                </div>
                <div class="text-sm">
                <a href="<c:url value="/register"/>" class="font-medium text-cyan-600 hover:text-cyan-500"> <spring:message code="login.noaccount"/> </a>
                </div>
            </div>

            <div>
                <button type="submit" class="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-cyan-600 hover:bg-cyan-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-cyan-500">
            <span class="absolute left-0 inset-y-0 flex items-center pl-3">
              <svg class="h-5 w-5 text-cyan-500 group-hover:text-cyan-400" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                <path fill-rule="evenodd" d="M5 9V7a5 5 0 0110 0v2a2 2 0 012 2v5a2 2 0 01-2 2H5a2 2 0 01-2-2v-5a2 2 0 012-2zm8-2v2H7V7a3 3 0 016 0z" clip-rule="evenodd"></path>
              </svg>
            </span>
                    <spring:message code="login.singInButton"/>
                </button>
            </div>
        </form>
    </div>
</div>
</body>
</html>