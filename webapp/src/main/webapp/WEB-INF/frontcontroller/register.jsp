<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<!DOCTYPE html>
<html lang="en">
<%@ include file="Head.jsp" %>
<body class="h-full">
<%@ include file="../components/navbar.jsp" %>
<div class="min-h-full flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-md w-full space-y-8">
        <div>
            <img class="mx-auto h-12 w-auto" src="<c:url value='/resources/logo.svg'/>" alt="logo">
            <h2 class="mt-6 text-center text-3xl font-extrabold text-gray-900"><spring:message code="register.createAccount"/></h2>
            <p class="text-center pt-2"><spring:message code="register.createTo"/></p>
        </div>

        <c:url value="/register" var="postPath"/>
        <form:form modelAttribute="userForm" action="${postPath}" method="post" class="mt-8 space-y-6">
            <div class="rounded-md shadow-sm -space-y-px">
                <div>
                    <c:if test="${error == true}">
                        <p class="text-red-500 mb-4 text-center"><spring:message code="register.error"/></p>
                    </c:if>
                    <form:label path="email"><spring:message code="register.email"/></form:label>
                    <form:input path="email" type="email" autocomplete="email" required="true"
                                  class="appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-cyan-500 focus:border-cyan-500 focus:z-10 sm:text-sm"/>
                    <form:errors path="email" element="p" cssStyle="color: tomato" />
                </div>
                <div class="pt-4">
                    <form:label  path="walletAddress"><spring:message code="register.walletAddress"/></form:label>
                    <form:input path="walletAddress" type="text" required="true"
                                class="appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-cyan-500 focus:border-cyan-500 focus:z-10 sm:text-sm"/>
                    <form:errors path="walletAddress" element="p" cssStyle="color: tomato" />
                </div>
                <div class="pt-4 flex flex-col">
                    <form:label  path="walletChain"><spring:message code="register.walletChain"/></form:label>
                    <form:select
                            path="walletChain"
                            required="true"
                            class="pl-3 sm:text-sm rounded-lg border-slate-300 focus:ring-cyan-800 focus:border-cyan-800 text-cyan-700 placeholder:text-slate-400 shadow-sm"
                    >
                        <option disabled selected><spring:message code="register.blockchain"/></option>
                        <c:forEach var="chain" items="${chains}">
                            <form:option value="${chain}"><c:out value="${chain}" /></form:option>
                        </c:forEach>
                    </form:select>
                    <form:errors path="walletChain" element="p" cssStyle="color: tomato" />
                </div>
                <div class="pt-4">
                    <form:label path="username"><spring:message code="register.username"/></form:label>
                    <form:input path="username" type="text" autocomplete="name" required="true"
                                class="appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-cyan-500 focus:border-cyan-500 focus:z-10 sm:text-sm"/>
                    <form:errors path="username" element="p" cssStyle="color: tomato" />
                </div>
                <div class="pt-4">
                    <form:label path="password"><spring:message code="register.password"/></form:label>
                    <form:input path="password" type="password" autocomplete="current-password" required="true"
                           class="appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-cyan-500 focus:border-cyan-500 focus:z-10 sm:text-sm"/>
                    <form:errors path="password" element="p" cssStyle="color: tomato"/>
                </div>
            </div>

            <div>
                <button type="submit"
                        class="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-bold rounded-md text-white bg-cyan-600 hover:bg-cyan-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-cyan-500">
                    <spring:message code="register.createAccount"/>
                </button>
                <p class="text-center py-4"><spring:message code="register.haveAccount"/> <a href="<c:url value="/login"/>" class="text-cyan-400">&nbsp;&nbsp;<spring:message code="register.login"/></a></p>
            </div>
        </form:form>

    </div>
</div>
</body>
</html>