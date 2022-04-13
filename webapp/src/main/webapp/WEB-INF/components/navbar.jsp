<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<nav class="flex items-center w-full justify-around h-16 lg:h-20 shadow-md lg:shadow-none">
    <a href="<c:url value="/" />">
    <div class="cursor-pointer flex flex-row flex-start gap-2">
        <img src="<c:url value="/resources/logo.svg" />" alt="logo" class="w-10 md:w-14" />
        <h1 class="font-semibold text-xl md:text-2xl">Skyplace</h1>
    </div>
    </a>
    <div class="w-2/5 relative flex flex-row flex-start p-2 rounded border border-gray-300 text-cyan-800">
        <c:url value="/category/all" var="postPath"/>
        <form action="${postPath}" method="get" class="w-full flex m-0">
            <img src="<c:url value='/resources/icsearch.svg' />" alt="icsearch" class="w-5" />
        <input name="name" class="pl-2 outline-none w-full border-none p-0 focus:border-none focus:ring-0" type="text"
            placeholder="Search items, collections and accounts" />
        </form>
        <!-- Search icon -->
    </div>
    <div class="hidden sm:flex justify-end items-center gap-12 pr-4 text-lg font-normal">
        <a class="hover:underline decoration-2 decoration-cyan-500 underline-offset-4" href="<c:url value="/explore" />">Explore</a>
        <a class="hover:underline decoration-2 decoration-cyan-500 underline-offset-4" href="<c:url value="/sell" />">Sell</a>
    </div>
</nav>