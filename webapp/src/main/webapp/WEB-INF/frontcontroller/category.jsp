<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="Head.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<!DOCTYPE HTML>
<html>
<head>
    <title>Category | Skyplace</title>
  <style>
    input::-webkit-outer-spin-button,
    input::-webkit-inner-spin-button {
      -webkit-appearance: none;
      margin: 0;
    }
  </style>
</head>
<body>
<div class="h-screen flex flex-col h-full">
  <div class="w-full h-16 bg-gray-400">navbar</div>
  <div class="grow flex">
    <div class="flex flex-col w-72 min-w-[250px] items-center">
      <span class="text-4xl"><c:out value="${category}" /></span>
      <span>258 resultados</span>

      <!-- Filter -->
      <div class="border-r-2 border-slate-300 grow w-full">
        <!-- Close filter btn -->
        <div class="w-full h-12">

        </div>

        <!-- Filter section -->
        <div class="flex flex-col w-full px-4">
          <!-- Header -->
          <div class="flex space-evenly">
            <!-- img filter -->
            <span class="text-2xl">Filter</span>
            <!-- img close filter -->
          </div>

          <!-- Filter content -->
          <div class="px-4 flex flex-col gap-4">
            <!-- Status -->
            <div class="flex justify-between">
              <label class="text-xl">Status
                <select name="status" id="statusFilter" class="border-2 rounded-xl w-40 cursor-pointer">
                  <option value="none" selected></option>
                  <option value="buynow">Buy now</option>
                  <option value="onauction">On auction</option>
                  <option value="new">New</option>
                  <option value="hasoffers">Has offers</option>
                </select>
              </label>
            </div>

            <!-- Price -->
            <div class="flex justify-between">
              <label class="text-xl">Price
                <div>
                  <input type="number" placeholder="Min" min="0"
                         class="border-2 rounded-xl py-1 px-2 text-sm w-20"/>
                  <input type="number" placeholder="Max" min="0"
                         class="border-2 rounded-xl py-1 px-2 text-sm w-20"/>
                </div>
              </label>
            </div>

            <!-- Chain -->
            <div class="flex justify-between">
              <label class="text-xl">Chain
                <select name="chain" id="chainFilter" class="border-2 rounded-xl w-40 cursor-pointer">
                  <option value="none" selected></option>
                  <option value="ethereum">Ethereum Network</option>
                  <option value="polygon">Polygon</option>
                  <option value="bsc">Binance Smart Chain</option>
                  <option value="solana">Solana</option>
                  <option value="testnet">Rinkeby</option>
                </select>
              </label>
            </div>

            <!-- Discount -->
            <div class="flex justify-between">
              <label class="text-xl">Discount
                <input type="number" placeholder="Discount" min="0"
                       class="border-2 rounded-xl w-32 py-1 px-2 text-sm"/>
              </label>
            </div>
          </div>

          <!-- Sort by section -->
          <div class="flex flex-col w-full">
            <!-- Header -->
            <div class="flex space-evenly">
              <!-- img sort -->
              <span class="text-2xl">Sort by</span>
              <!-- img close sort -->
            </div>

            <div class="flex flex-col px-4">
              <div>
                <label class="text-xl"><input type="radio" />Recently listed</label>
              </div>
              <div>
                <label class="text-xl"><input type="radio" />Recently sold</label>
              </div>
              <div>
                <label class="text-xl"><input type="radio" />Price: Low to High</label>
              </div>
              <div>
                <label class="text-xl"><input type="radio" />Price: High to Low</label>
              </div>
              <div>
                <label class="text-xl"><input type="radio" />Ending Soon</label>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- all nfts -->
    <div class="w-[80%] min-w-[500px] grow flex flex-col">
      <!-- header -->
      <div class="flex justify-between h-16">
        <!-- pages -->
        <div class="flex text-2xl pt-4">
          <!-- icon previous -->
          <span class=" text-gray-400 cursor-pointer mr-4">Previous</span>
          <label>
            <input type="number" min="1" value="1"
                   class="w-10 border-2 border-slate-300 rounded-lg bg-slate-300 px-1 mx-1 h-10" />
          </label>
          <span class="ml-4"> of 145</span>
          <span class="text-cyan-400 cursor-pointer ml-4">Next</span>
        </div>
        <div class="pt-4 text-2xl mr-8">
          <label for="npages" class="">Show</label>
          <select name="npages" id="npages" class="border-2 rounded-xl w-16 cursor-pointer">
            <option value="12p">12</option>
            <option value="24p">24</option>
            <option value="48p">48</option>
          </select>
        </div>
      </div>

      <div class="grid grid-cols-3 overflow-y-scroll h-[calc(100vh-8rem)]">
        <c:forEach var="i" begin="0" end="11">
          <div class="flex justify-center items-center">
            <%@ include file="../components/card.jsp" %>
          </div>
        </c:forEach>
      </div>
    </div>
  </div>
</div>
</body>
</html>
