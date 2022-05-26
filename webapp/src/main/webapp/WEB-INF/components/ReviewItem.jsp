<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="flex flex-col pt-4">
    <div class="flex flex-row items-center justify-between">
        <div class="flex items-center space-x-4">
            <img class="w-14 h-14 rounded-full" src="<c:url value='/resources/profile_picture.png'/>" alt="">
            <div class="space-y-1 font-medium">
                <p class="text-lg">${param.reviewerName}</p>
            </div>
        </div>
        <div class="flex flex-row">
            <c:forEach var="i" begin="1" end="${param.score}">
                <img class="h-9 w-9" src="<c:url value='/resources/filled_star.svg'/>" alt=""/>
            </c:forEach>
            <c:forEach var="j" begin="${param.score}" end="4">
                <img class="h-9 w-9" src="<c:url value='/resources/empty_star.svg'/>" alt=""/>
            </c:forEach>
        </div>
    </div>
    <h3 class="font-semibold text-gray-500 mt-4">${param.title}</h3>
    <p class="mb-2 mt-1 font-light text-gray-500">${param.description}</p>
</div>
