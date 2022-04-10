<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<body>

    <div class="">
        <a href="<c:url value="/product/${param.id_product}" />" class="flex flex-col rounded-xl border-2 w-72 h-96 border-gray-600 m-5">
            <div class="w-full h-96 border-b-2 border-gray-600">
                <img alt="nft image" src="data:image/jpg;base64,${param.img}"/>
            </div>
            <div class="flex justify-between px-2 mb-1">
                <div class="flex flex-col">
                    <span class="font-bold text-lg">${param.name}</span>
                    <span class="text-sm text-gray-600">${param.seller_email}</span>
                </div>
                <div class="flex flex-col items-end">
                    <span class="text-sm text-gray-600 ">Price</span>
                    <div>
                        <!-- foto eth -->
                        <span class="text-lg">${param.price}</span>
                    </div>
                </div>
            </div>
            <div class="border-t-2 border-gray-200 w-[90%] self-center">
                <!-- img corazon-->
                <span class="text-lg text-gray-500">${param.score}</span>
            </div>
        </a>
    </div>
</body>
</html>
