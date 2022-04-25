<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<%@ include file="Head.jsp" %>
<body class="min-h-screen flex flex-col">
<%@ include file="../components/navbar.jsp" %>
    <div class="w-full max-w-5xl mx-auto p-4 grow flex flex-col justify-center py-12">
      <h1 class="text-3xl text-center mb-12">
        Update sell order
      </h1>
      <c:url value="/product/update/${order.id}" var="postPath"/>
      <form:form modelAttribute="UpdateSellOrderForm" action="${postPath}" method="post" class="grid grid-cols-2 gap-8 pt-8" enctype="multipart/form-data">
        <div class="flex flex-col gap-1">
          <p class="text-slate-600">
            <span class="font-bold">NFT id: </span> 
            ${order.id}
          </p>
        </div>
        <div class="flex flex-col gap-1">
          <p class="text-slate-600">
            <span class="font-bold">NFT contract: </span> 
            ${order.nftAddress}
          </p>
        </div>
        <form:label path="category" class="flex flex-col gap-1">
          <span class="text-slate-600">Category</span>
          <form:select
            path="category"
            required="true"
            class="pl-3 sm:text-sm rounded-lg border-slate-300 focus:ring-cyan-800 focus:border-cyan-800 text-cyan-700 placeholder:text-slate-400 shadow-sm"
          >
            <c:forEach var="category" items="${categories}">
              <c:choose>
                <c:when test="${category == order.category}">
                  <form:option selected="selected" value="${category}"><c:out value="${category}" /></form:option>
                </c:when>
                <c:otherwise>
                  <form:option value="${category}"><c:out value="${category}" /></form:option>
                </c:otherwise>
              </c:choose>
            </c:forEach>
          </form:select>
          <form:errors path="category" element="p" cssStyle="color: tomato" />
        </form:label>
        <form:label path="price" class="flex flex-col gap-1">
          <span class="text-slate-600">Price</span>
          <form:input
            type="number"
            path="price"
            autoComplete="off"
            value="${order.price}"
            required="true"
            placeholder="0.0"
            min="0"
            step="0.000000000000000001"
            class="pl-3 sm:text-sm rounded-lg border-slate-300 focus:ring-cyan-800 focus:border-cyan-800 text-cyan-700 placeholder:text-slate-400 shadow-sm"
          />
          <form:errors path="price" element="p" cssStyle="color: tomato" />
        </form:label>
        <form:label path="description" class="flex flex-col gap-1">
          <span class="text-slate-600">Description</span>
          <form:textarea
            id="description"
            path="description"
            autoComplete="off"
            placeholder="Nft description"
            class="min-h-16 max-h-32 pl-3 sm:text-sm rounded-lg border-slate-300 focus:ring-cyan-800 focus:border-cyan-800 text-cyan-700 placeholder:text-slate-400 shadow-sm"
          />
        </form:label>
        <input
          type="submit"
          value="Update"
          class="p-1 font-bold rounded-lg shadow-sm cursor-pointer bg-cyan-100 text-cyan-700 hover:bg-cyan-200"
        />
      </form:form>
    </div>

    <script>
      document.getElementById("description").defaultValue = "${order.description}"
    </script>
</body>
</html>
