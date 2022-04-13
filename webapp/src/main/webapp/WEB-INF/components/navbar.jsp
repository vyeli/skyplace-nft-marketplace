<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
    <header class="font-sans font-light h-20 flex items-center">
        <nav class="flex items-center w-full justify-evenly">
            <a href="<c:url value="/" />">
            <div class="cursor-pointer flex flex-row flex-start gap-2">
                <img src="<c:url value="/resources/logo.svg" />" alt="logo" class="w-14" />
                <h1 class="font-semibold text-2xl">Skyplace</h1>
            </div>
            </a>
            <div class="w-2/5 relative flex flex-row flex-start p-1 rounded border border-gray-300">
                <c:url value="/category/all" var="postPath"/>
                <form action="${postPath}" method="get" class="w-full flex m-0">
                    <img src="<c:url value='/resources/icsearch.svg' />" alt="icsearch" class="w-5" />
                <input name="name" class="pl-2 outline-none w-full border-none p-0 focus:border-none focus:ring-0" type="text"
                    placeholder="Search items, collections and accounts" />
                </form>
                <!-- Search icon -->
            </div>
            <div class="hidden sm:flex justify-end items-center gap-12 pr-4 text-lg font-normal">
                <a href="<c:url value="/explore" />">Explore</a>
                <a href="<c:url value="/sell" />">Sell</a>
            </div>
        </nav>
    </header>