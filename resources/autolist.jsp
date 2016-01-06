
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %> 
<HTML>
<HEAD>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=windows-1252">
<TITLE>List of Autos</TITLE>
<link rel="stylesheet" type="text/css" href="styles.css"> 


</style>
</HEAD>
<BODY>
<jsp:useBean id="Autos" class="java.util.ArrayList" scope="session" />
<h3>Sample Application - Used Autos</h3>
<img src="images/autosalesman.png"/><br/>
<table class="gridtable">
     <tr>
     <td>ID</td>
     <td>Make</td>
     <td>Model</td>
     <td>Colour</td>
     <td>Cost</td>
     <td>&nbsp;</td>
     <td>&nbsp;</td>
  </tr>
   
   <c:forEach var="plist" items="${Autos}" >
     <tr>
     <td><a href="lookup?id=${plist.id}">${plist.id}</td>
	 <td>${plist.make}</td>
     <td>${plist.model}</td>
     <td>${plist.colour}</td>
     <td>${plist.formattedCost}</td>
     <td><a href="lookup?id=${plist.id}">Edit</a></td>
     <td><a href="delete?id=${plist.id}"  onclick="return confirm('Are you sure you want to delete this Autot?');">Delete</a></td>
  </tr>
  </c:forEach>
</table>
<br/><a href="lookup?id=-1">New Auto</a> | <a href="list">Refresh</a>

</BODY>
</HTML>