<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<%@ include file="Head.jsp" %>
<body class="min-h-screen flex flex-col">
<%@ include file="../components/navbar.jsp" %>
    <div class="w-full max-w-5xl mx-auto p-4 grow flex flex-col justify-center py-12">
      <h1 class="text-3xl text-center">
        Sell an NFT
      </h1>
      <c:url value="/sell" var="postPath"/>
      <form:form modelAttribute="sellNftForm" action="${postPath}" method="post" class="grid grid-cols-2 gap-8 pt-8" enctype="multipart/form-data">
        <form:label path="name" class="flex flex-col gap-1">
          <span class="text-slate-600">Name</span>
          <form:input
            type="text"
            path="name"
            required="true"
            autoComplete="off"
            placeholder="Nft name"
            class="pl-3 sm:text-sm rounded-lg border-slate-300 focus:ring-cyan-800 focus:border-cyan-800 text-cyan-700 placeholder:text-slate-400 shadow-sm"
            name="name"
          />
            <form:errors path="name" element="p" cssStyle="color: tomato" />
        </form:label>
        <form:label path="nftId" class="flex flex-col gap-1">
          <span class="text-slate-600">Id</span>
          <form:input
                  type="number"
                  path="nftId"
                  min="0"
                  required="true"
                  autoComplete="off"
                  placeholder="Nft id"
                  class="pl-3 sm:text-sm rounded-lg border-slate-300 focus:ring-cyan-800 focus:border-cyan-800 text-cyan-700 placeholder:text-slate-400 shadow-sm"
          />
          <form:errors path="nftId" element="p" cssStyle="color: tomato" />
        </form:label>
        <form:label path="nftContract" class="flex flex-col gap-1">
          <span class="text-slate-600">Contract</span>
          <form:input
                  type="text"
                  path="nftContract"
                  required="true"
                  autoComplete="off"
                  placeholder="Nft contract"
                  class="pl-3 sm:text-sm rounded-lg border-slate-300 focus:ring-cyan-800 focus:border-cyan-800 text-cyan-700 placeholder:text-slate-400 shadow-sm"
          />
          <form:errors path="nftContract" element="p" cssStyle="color: tomato" />
        </form:label>
        <form:label path="chain" class="flex flex-col gap-1">
          <span class="text-slate-600">Blockchain</span>
          <form:select
                  path="chain"
                  required="true"
                  class="pl-3 sm:text-sm rounded-lg border-slate-300 focus:ring-cyan-800 focus:border-cyan-800 text-cyan-700 placeholder:text-slate-400 shadow-sm"
          >
            <c:forEach var="chain" items="${chains}">
              <form:option value="${chain}"><c:out value="${chain}" /></form:option>
            </c:forEach>
          </form:select>
          <form:errors path="chain" element="p" cssStyle="color: tomato" />
        </form:label>
        <form:label path="category" class="flex flex-col gap-1">
          <span class="text-slate-600">Category</span>
          <form:select
                  path="category"
                  required="true"
                  class="pl-3 sm:text-sm rounded-lg border-slate-300 focus:ring-cyan-800 focus:border-cyan-800 text-cyan-700 placeholder:text-slate-400 shadow-sm"
          >
            <c:forEach var="category" items="${categories}">
              <form:option value="${category}"><c:out value="${category}" /></form:option>
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
            required="true"
            placeholder="0.0"
            min="0"
            step="0.00000001"
            class="pl-3 sm:text-sm rounded-lg border-slate-300 focus:ring-cyan-800 focus:border-cyan-800 text-cyan-700 placeholder:text-slate-400 shadow-sm"
          />
          <form:errors path="price" element="p" cssStyle="color: tomato" />
        </form:label>
        <form:label path="description" class="flex flex-col gap-1">
          <span class="text-slate-600">Description</span>
          <form:textarea
            path="description"
            autoComplete="off"
            placeholder="Nft description"
            class="min-h-16 max-h-32 pl-3 sm:text-sm rounded-lg border-slate-300 focus:ring-cyan-800 focus:border-cyan-800 text-cyan-700 placeholder:text-slate-400 shadow-sm"
          />
        </form:label>
        <label class="flex flex-col gap-1">
          <span class="text-slate-600">Image</span>
          <form:input
              path="image"
              type="file"
              required="true"
              name="file"
              class="mt-3 block text-sm text-slate-500 file:mr-4 file:py-2 file:px-4
            file:rounded-lg file:border-0 file:text-sm file:font-semibold file:bg-cyan-50 file:text-cyan-700 hover:file:bg-cyan-100"
          />
            <form:errors path="image" element="p" cssStyle="color: tomato" />
        </label>
        <form:label path="email" class="flex flex-col gap-1">
          <span class="text-slate-600">Email</span>
          <form:input
            type="email"
            path="email"
            required="true"
            autoComplete="off"
            placeholder="Your email"
            class="pl-3 sm:text-sm rounded-lg border-slate-300 focus:ring-cyan-800 focus:border-cyan-800 text-cyan-700 placeholder:text-slate-400 shadow-sm"
          />
            <form:errors path="email" element="p" cssStyle="color: tomato" />
        </form:label>
          <input
            type="submit"
            value="Publish"
            class="p-1 font-bold rounded-lg shadow-sm cursor-pointer bg-cyan-100 text-cyan-700 hover:bg-cyan-200"
          />
          <form:errors path="publish" element="p" cssStyle="color: tomato" />
      </form:form>
    </div>
</body>
</html>
