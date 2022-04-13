<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<body>

    <div class="">
        <a href="<c:url value="/product/${param.id_product}" />" class="flex flex-col rounded-xl overflow-hidden border-2 h-96 max-w-sm border-gray-600">
            <div class="w-full grow border-b-2 border-gray-600">
                <img alt="nft image" src="data:image/jpg;base64,${param.img}" class="w-full h-full object-cover" />
            </div>
            <div class="flex justify-between p-2">
                <div class="flex flex-col w-[66%]">
                    <span class="font-bold text-lg truncate">${param.name}</span>
                    <span class="text-sm text-gray-600 truncate">${param.seller_email}</span>
                </div>
                <div class="flex flex-col items-end">
                    <span class="text-sm text-gray-600">Price</span>
                    <div class="flex">
                        <img class="w-8" src="<c:url value="/resources/eth_logo.svg" />" alt="eth logo">
                        <span class="text-lg  overflow-hidden">${param.price}</span>
                    </div>
                </div>
            </div>
            <div class="py-1 pl-2 border-t-2 border-gray-200 flex">
                <img class="w-6 mr-1" src="<c:url value="/resources/heart.svg" />" alt="heart logo">
                <span class="text-lg text-gray-500">${param.score}</span>
            </div>
        </a>
    </div>
</body>
</html>
