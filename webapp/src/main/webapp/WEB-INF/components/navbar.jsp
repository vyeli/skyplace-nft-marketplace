<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

    <header class="font-sans font-light">
        <nav class="container flex items-center py-4 mt-4">
            <a href="<c:url value=" /" />">
            <div class="cursor-pointer flex flex-row flex-start gap-2">
                <img src="<c:url value='../resources/logo.png' />" alt="logo" />
                <h1 class="font-semibold text-2xl">Skyplace</h1>
            </div>
            </a>
            <div class="w-1/2 relative flex flex-row flex-start p-1 mx-8 rounded border border-gray-300">
                <svg class="h-5 w-5 text-cyan-400 mr-1" width="24" height="24" viewBox="0 0 24 24" stroke-width="2"
                    stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round">
                    <path stroke="none" d="M0 0h24v24H0z"></path>
                    <circle cx="10" cy="10" r="7"></circle>
                    <line x1="21" y1="21" x2="15" y2="15"></line>
                </svg>
                <input class="outline-none w-full border-none p-0 focus:border-none focus:ring-0" type="text"
                    placeholder="Search items, collections and accounts">
                <!-- Search icon -->
            </div>
            <div class="hidden sm:flex flex-1 justify-end items-center gap-12 pr-4">
                <a href="<c:url value=" /explore" />">Explore</a>
                <a href="<c:url value=" /sell" />">Sell</a>
            </div>
        </nav>
    </header>