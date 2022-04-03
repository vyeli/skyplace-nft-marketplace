<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<!DOCTYPE html>
<html>
<%@ include file="Head.jsp" %>
<body>
<div class="mb-20"></div> <!-- Navbar -->
<div class="container flex flex-row m-12">
    <!-- Container -->
    <div class="border-2 w-80 mt-8 rounded-md">
        <!-- Description box -->
        <p class="text-2xl font-bold m-4 ">Description</p>
        <hr>
        <p class="m-3">Created by&nbsp;<span class="text-cyan-500">Larva Labs</span>
            <br>
            <br>
            The Cryptopunks are
            one of the earliest examples
            of a "Non-Fungible Token"
            on Ethereum, and were inspiration
            for the ERC-721 standard that
            powers most digital art and collectibles.
        </p>
    </div> <!-- Description box -->
    <div>
        <!-- center img -->
        <img class="object-contain max-w-xl m-8 rounded-md" src="https://s3.amazonaws.com/businessinsider.mx/wp-content/uploads/2021/07/16190820/image-25.jpeg" alt="Alien Cryptopunk">
    </div> <!-- center img -->

    <div class="flex flex-col">
        <!-- Pricing box-->
        <h1 class="py-8 font-bold text-2xl">Alien CryptoPunk #7523</h1>
        <div class="border-2 p-2 rounded-md h-64">
            <div class="flex mb-2">
                <!-- Price -->
                <img class="h-7 w-7 my-auto" src="http://assets.stickpng.com/images/5a7593fc64538c292dec1bbf.png" alt="eth">
                <p class="my-auto text-sm"><span class="font-bold pl-4 text-lg">0.6</span>&nbsp;($2,090.48)</p>
            </div> <!-- Price -->
            <hr>
            <div class="mt-2 py-2 h-52 grid grid-flow-row ">
                <!-- Specification -->
                <div class="flex justify-between">
                    <p>Contract address</p>
                    <p>0x4a8c...62af</p>
                </div>
                <div class="flex justify-between">
                    <p>Token ID</p>
                    <p>7370</p>
                </div>
                <div class="flex justify-between">
                    <p>Token Standard</p>
                    <p>ERC-721</p>
                </div>
                <div class="flex justify-between">
                    <p>Blockchain</p>
                    <p>Ethereum</p>
                </div>
            </div> <!-- Specification -->
            <div class="p-4">
                <button class="w-40 bg-cyan-600 p-2 text-white rounded-md">
                    Place bid
                </button>
            </div>
        </div>
    </div> <!-- Pricing box-->
</div>
</body>
</html>
