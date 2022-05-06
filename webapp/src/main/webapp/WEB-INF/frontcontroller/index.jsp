<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<!DOCTYPE html>
<html>
<%@ include file="Head.jsp" %>
<body class="min-h-screen flex flex-col max-w-[100vw]">
<!-- Header -->
<%@ include file="../components/navbar.jsp" %>
<!-- Hero -->
<div class="container px-4 py-8 flex flex-col-reverse lg:flex-row gap-4 lg:gap-12 grow justify-center items-center overflow-hidden md:overflow-visible">
    <!-- Message and buttons -->
    <div class="flex flex-1 flex-col items-center lg:items-start lg:mt-[-7rem]">
        <h2 class="text-3xl mid:text-4 lg:text-5xl text-center lg:text-left mb-6 font-semibold pt-12">
            <spring:message code="index.discover"/>
        </h2>
        <p class="text-bookmark-grey text-lg text-center lg:text-left mb-6">
            <spring:message code="index.become"/>
        </p>
        <div class="flex justify-center flex-wrap gap-6">
            <a href="<c:url value="/sell" />">
            <button type="button" class="shadow-md px-6 rounded-md transition duration-300 bg-cyan-600 hover:bg-cyan-800 text-white hover:shadow-xl h-12">
                <spring:message code="index.create"/>
            </button>
            </a>
            <a href="<c:url value="/explore" />">
            <button type="button" class="shadow-md px-6 rounded-md transition duration-300 border-2 border-cyan-600 hover:border-cyan-800 hover:bg-cyan-800 text-cyan-600 hover:text-white hover:shadow-xl h-12">
                <spring:message code="index.explore"/>
            </button>
            </a>
        </div>
    </div>
    <!-- Image and circles -->
    <div class="relative flex flex-items-center justify-center flex-1 md:mb-16 lg:mb-0 w-full max-w-lg lg:mt-[-7rem]">
        <div class="absolute -top-4 -left-8 w-80 h-80 bg-cyan-300 rounded-full mix-blend-multiply filter blur-xl opacity-75 animate-blob z-0"></div>
        <div class="absolute -top-4 -right-8 w-80 h-80 bg-yellow-300 rounded-full mix-blend-multiply filter blur-xl opacity-75 animate-blob delay-2000 z-0"></div>
        <div class="absolute top-8 w-80 h-80 bg-red-300 rounded-full mix-blend-multiply filter blur-xl opacity-75 animate-blob delay-4000 z-0"></div>
        <img class="z-10 h-80 w-68" src="<c:url value="/resources/nft_index.jpg" />" alt="Nft image"/>
    </div>
</div>
</body>
</html>
