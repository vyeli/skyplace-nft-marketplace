<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<%@ include file="Head.jsp" %>
<body class="h-full">
<%@ include file="../components/navbar.jsp" %>
<div class="min-h-full flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-md w-full space-y-8">
        <div>
            <img class="mx-auto h-12 w-auto" src="<c:url value='/resources/logo.svg'/>" alt="logo">
            <h2 class="mt-6 text-center text-3xl font-extrabold text-gray-900">Create your account</h2>
            <p class="text-center pt-2">Create an account to buy and sell nfts</p>
        </div>

        <c:url value="/create" var="postPath"/>
        <form:form modelAttribute="userForm" action="${postPath}" method="post" class="mt-8 space-y-6">
            <div class="rounded-md shadow-sm -space-y-px">
                <div>
                    <form:label path="email">Email</form:label>
                    <form:input path="email" type="email" autocomplete="email" required="true"
                                  class="appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-cyan-500 focus:border-cyan-500 focus:z-10 sm:text-sm"/>
                    <form:errors path="email" element="p" cssStyle="color: tomato" />
                </div>
                <div class="pt-4">
                    <form:label  path="walletAddress">Wallet address</form:label>
                    <form:input path="walletAddress" type="text" required="true"
                                class="appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-cyan-500 focus:border-cyan-500 focus:z-10 sm:text-sm"/>
                    <form:errors path="walletAddress" element="p" cssStyle="color: tomato" />
                </div>
                <div class="pt-4">
                    <form:label path="username">Username</form:label>
                    <form:input path="username" type="text" autocomplete="name" required="true"
                                class="appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-cyan-500 focus:border-cyan-500 focus:z-10 sm:text-sm"/>
                    <form:errors path="username" element="p" cssStyle="color: tomato" />
                </div>
                <div class="pt-4">
                    <form:label path="password">Password</form:label>
                    <form:input path="password" type="password" autocomplete="current-password" required="true"
                           class="appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-cyan-500 focus:border-cyan-500 focus:z-10 sm:text-sm"/>
                    <form:errors path="password" element="p" cssStyle="color: tomato"/>
                </div>
            </div>

            <div>
                <button type="submit"
                        class="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-bold rounded-md text-white bg-cyan-600 hover:bg-cyan-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-cyan-500">
                    Create your account
                </button>
                <p class="text-center py-4">Already have an account ? <a href="<c:url value="/login"/>" class="text-cyan-400">&nbsp;&nbsp;Log in</a></p>
            </div>
        </form:form>

    </div>
</div>
</body>
</html>