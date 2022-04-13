<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="Head.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<!DOCTYPE HTML>
<html>
<head>
    <title>Explore | Skyplace</title>
</head>
<body>
<div class="h-screen flex flex-col">
    <%@ include file="../components/navbar.jsp" %>
    <div class="flex grow justify-center relative items-center">
        <!-- Background circles -->
        <div class="absolute mt-16 h-80 w-1/2">
            <div
                class="absolute -top-4 md:-top-6 -left-16 w-48 h-48 md:w-72 md:h-72 bg-cyan-300 rounded-full mix-blend-multiply filter blur-xl opacity-75 animate-blob">
            </div>
            <div
                class="absolute -top-4 md:-top-6 -right-16 w-48 h-48 md:w-72 md:h-72 bg-red-300 rounded-full mix-blend-multiply filter blur-xl opacity-75 animate-blob delay-2000">
            </div>
            <div
                class="absolute -bottom-8 md:-bottom-16 left-[2%] md:left-1/4 w-56 h-56 md:w-80 md:h-80 bg-yellow-300 rounded-full mix-blend-multiply filter blur-xl opacity-75 animate-blob delay-4000">
            </div>
        </div>
        <div class="flex flex-col overflow-clip mt-[-7rem]">
            <h1 class="text-4xl z-10 text-center">Explore NFTs</h1>
            <div class="relative mt-12 w-5/12 min-w-[300px] md:min-w-[550px]">
                <div
                        class="grid w-full h-80 grid-cols-2 md:grid-cols-3 grid-rows-3 md:grid-rows-2 rounded-xl bg-white ">
                    <div
                            class="transition duration-200 border-2 border-slate-300 rounded-tl-lg hover:bg-cyan-100 bg-white z-10">
                        <a href="<c:url value="/category/all" />" class="w-full h-full cursor-pointer flex justify-center items-center flex-col gap-1">
                            <img class="w-14 h-14" src="<c:url value='/resources/all_icon.svg' />" alt="all_icon" />
                            <span class="text-xl md:text-2xl">All</span>
                        </a>
                    </div>
                    <div
                            class="transition duration-200 border-slate-300 border-2 border-l-0 md:border-r-0 rounded-tr-lg md:rounded-none cursor-pointer hover:bg-cyan-100 bg-white z-10">
                        <a href="<c:url value="/category/collections" />" class="w-full h-full flex justify-center items-center flex-col gap-1">
                            <img class="w-14 h-14" src="<c:url value='/resources/categories_icon.svg' />" alt="categories_icon" />
                            <span class="text-xl md:text-2xl">Collections</span>
                        </a>
                    </div>
                    <div
                            class="transition duration-200 border-2 border-slate-300 border-t-0 md:border-t-2 border-l-2 md:rounded-tr-lg cursor-pointer hover:bg-cyan-100 bg-white z-10">
                        <a href="<c:url value="/category/art" />" class="w-full h-full flex justify-center items-center flex-col gap-1">
                        <img class="w-14 h-14" src="<c:url value='/resources/art_icon.svg' />" alt="art_icon" />
                        <span class="text-xl md:text-2xl">Art</span>
                        </a>
                    </div>

                    <div
                            class="transition duration-200 border-slate-300 border-2 border-t-0 md:border-r-2 border-l-0 md:border-l-2 border-b-2 md:rounded-bl-lg cursor-pointer hover:bg-cyan-100 bg-white z-10">
                        <a href="<c:url value="/category/utility" />" class="w-full h-full flex justify-center items-center flex-col gap-1">
                        <img class="w-14 h-14" src="<c:url value='/resources/utility_icon.svg' />" alt="utility_icon" />
                        <span class="text-xl md:text-2xl">Utility</span>
                        </a>
                    </div>
                    <div
                            class="transition duration-200 border-slate-300 border-2 border-t-0 rounded-bl-lg md:border-0 md:border-b-2 md:rounded-none cursor-pointer hover:bg-cyan-100 bg-white z-10">
                        <a href="<c:url value="/category/photography" />" class="w-full h-full flex justify-center items-center flex-col gap-1">
                        <img class="w-14 h-14" src="<c:url value='/resources/photography_icon.svg' />" alt="photography_icon" />
                        <span class="text-xl md:text-2xl">Photography</span>
                        </a>
                    </div>
                    <div
                            class="transition duration-200 border-slate-300 border-2 border-l-0 md:border-l-2 border-t-0 rounded-br-lg cursor-pointer hover:bg-cyan-100 bg-white z-10">
                        <a href="<c:url value="/category/other" />" class="w-full h-full flex justify-center items-center flex-col gap-1">
                        <img class="w-14 h-14" src="<c:url value='/resources/other_icon.svg' />" alt="other_icon" />
                        <span class="text-xl md:text-2xl">Other</span>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
