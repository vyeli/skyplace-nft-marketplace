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
    <div class="w-full h-16">navbar</div>
    <div class="flex flex-col items-center mt-12 w-full grow overflow-clip">
        <h1 class="text-4xl">Explore NFTs</h1>
        <div class="relative mt-12 w-5/12 min-w-[300px] md:min-w-[550px]">
            <!-- Background circles -->
            <div class="absolute w-full h-80 absolute z-0">
                <div
                        class="absolute -top-4 md:-top-12 -left-16 w-56 h-56 md:w-80 md:h-80 bg-cyan-300 rounded-full mix-blend-multiply filter blur-xl opacity-75 animate-blob">
                </div>
                <div
                        class="absolute -top-4 md:-top-12 -right-16 w-56 h-56 md:w-80 md:h-80 bg-red-300 rounded-full mix-blend-multiply filter blur-xl opacity-75 animate-blob delay-2000">
                </div>
                <div
                        class="absolute -bottom-8 md:-bottom-16 left-[2%] md:left-[15%] w-72 h-72 md:w-96 md:h-96 bg-yellow-300 rounded-full mix-blend-multiply filter blur-xl opacity-75 animate-blob delay-4000">
                </div>
            </div>
            <div
                    class="grid w-full h-80 grid-cols-2 md:grid-cols-3 grid-rows-3 md:grid-rows-2 rounded-xl gap-0  bg-white ">
                <div
                        class="border-2 border-slate-300 cursor-pointer rounded-tl-lg hover:bg-cyan-400 bg-white z-10">
                    <!-- imagen -->
                    <a href="category/all" class="w-full h-full flex justify-center items-center flex-col">
                        <span class="text-xl md:text-2xl">All</span>
                    </a>
                </div>
                <div
                        class="border-slate-300 border-2 border-l-0 md:border-r-0 rounded-tr-lg md:rounded-none cursor-pointer hover:bg-cyan-400 bg-white z-10">
                    <!-- imagen -->
                    <a href="category/collections" class="w-full h-full flex justify-center items-center flex-col">
                    <span class="text-xl md:text-2xl">Collections</span>
                    </a>
                </div>
                <div
                        class="border-2 border-slate-300 border-t-0 md:border-t-2 border-l-2 md:rounded-tr-lg cursor-pointer hover:bg-cyan-400 bg-white z-10">
                    <!-- imagen -->
                    <a href="category/art" class="w-full h-full flex justify-center items-center flex-col">
                    <span class="text-xl md:text-2xl">Art</span>
                    </a>
                </div>

                <div
                        class="border-slate-300 border-2 border-t-0 md:border-r-2 border-l-0 md:border-l-2 border-b-2 md:rounded-bl-lg cursor-pointer hover:bg-cyan-400 bg-white z-10">
                    <!-- imagen -->
                    <a href="category/utility" class="w-full h-full flex justify-center items-center flex-col">
                    <span class="text-xl md:text-2xl">Utility</span>
                    </a>
                </div>
                <div
                        class="border-slate-300 border-2 border-t-0 rounded-bl-lg md:border-0 md:border-b-2 md:rounded-none cursor-pointer hover:bg-cyan-400 bg-white z-10">
                    <!-- imagen -->
                    <a href="category/photography" class="w-full h-full flex justify-center items-center flex-col">
                    <span class="text-xl md:text-2xl">Photography</span>
                    </a>
                </div>
                <div
                        class="border-slate-300 border-2 border-l-0 md:border-l-2 border-t-0 rounded-br-lg cursor-pointer hover:bg-cyan-400 bg-white z-10">
                    <!-- imagen -->
                    <a href="category/other" class="w-full h-full flex justify-center items-center flex-col">
                    <span class="text-xl md:text-2xl">Other</span>
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
