<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<HTML>
<HEAD>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=windows-1252">
<TITLE>MVC Sample</TITLE>
<LINK REL="stylesheet" HREF="styles.css" TYPE="text/css">
</HEAD>
<BODY>

<jsp:useBean id="Auto" class="ca.bob.autos.data.Auto" scope="request" />

<FORM action="update">
  <input type="hidden" name="id" value="${Auto.id}"/>

<h2>Edit Auto Info</h2>
<img src="images/autosalesman.png"/><br/>
<table class="gridtable">
<tr>
   <td>ID:</td><td>${Auto.id}</td>
</tr>

<tr>
   <td>Make:</td><td><input type="text" name="make" value="${Auto.make}" /></td>
</tr>
<tr>
   <td>Model:</td><td><input type="text" name="model" value="${Auto.model}" /></td>
</tr>
<tr>
   <td>Colour:</td><td><input type="text" name="colour" value="${Auto.colour}" /></td>
</tr>
<tr>
   <td>Cost:</td><td><input type="text" name="cost" value="${Auto.cost}" /></td>
</tr>
</table>
<input type="submit" name="submit" value="Save" />
</form>
<br/><a href="list">Cancel | <a href="list">List</a></a>
</BODY>
</HTML>
