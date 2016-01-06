<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head><title>Error</title>
<link href="styles/styles.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#FFFFFF">

<h3>MedIT - Sample MVC Application.</h3>

<p>
<% 
  ca.ubc.med.mvc.MVCException E = (ca.ubc.med.mvc.MVCException)request.getAttribute("error");
  String action = (String)request.getParameter("action");
  if (action == null) action = ""; else action = "Error performing action: " + action;
  if (E != null) { 
	  if (E.getLocalizedMessage() != null) 
		  out.println(E.getLocalizedMessage() + "<br/>" );
	  else {
           out.println(E.getMessage() + "<br/>" + action);
		   out.println("<br/><i>Please report this error to the Systems Administrator.</i>");
	  }
     }
  else {
    out.println("No error message available.<br/>" + action);
	out.println("<br/><i>Please report this error to the Systems Administrator.</i>");
    }   
  %>
</p>

</body>
</html>
