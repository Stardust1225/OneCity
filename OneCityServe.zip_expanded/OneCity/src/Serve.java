import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Serve extends HttpServlet 
{
	 private CustomerManager customermanager;
	 private TaskManager taskmanager;
	 public void doGet(HttpServletRequest request,HttpServletResponse response)
     throws ServletException{
		 try{
			 
			 customermanager=CustomerManager.getInstance();
			 taskmanager=TaskManager.getInstance();
			 
			 String returnValue=null;
			 Integer flag=Integer.valueOf(request.getParameter("rq"));
			 
			 switch(flag.intValue()){
			 	case 0:returnValue=CreateCustomer(request);break;
			 	case 1:returnValue=ConfirmCustomer(request);break;	
			 	case 2:returnValue=AddTask(request);break;
			 	case 3:returnValue=RequestNumber(request);break;
			 	case 4:returnValue=ThroughTask(request);break;
			 	case 5:returnValue=AcceptTask(request);break;
			 	case 6:returnValue=ThroughDoneTask(request);break;
			 	case 7:returnValue=ThroughPutTask(request);break;
			 	case 8:returnValue=GetRelevantTask(request);break;
			 }
			 response.setHeader("Content-type","text/html;charset=UTF-8");
			 response.setCharacterEncoding("UTF-8");
			 response.getWriter().println(returnValue);
			 
		 }catch(Exception e){}
	 }
	 
	 public void doPost(HttpServletRequest request,HttpServletResponse response){
		 try {
			doGet(request,response);
		} catch (ServletException e) {}
		 
	 }
	 
	 private String CreateCustomer(HttpServletRequest request){
		 Customer customer=new Customer(request.getParameter("a"),request.getParameter("p"),request.getParameter("h"));
		 int i=customermanager.AddCustomer(customer);
		 return(String.valueOf(i));
	 }
	 
	 private String ConfirmCustomer(HttpServletRequest request){
		 Customer customer=new Customer(request.getParameter("a"),request.getParameter("p"),"1");
		 return(customermanager.ConfirmPassword(customer));
	 }
	 
	 private String AddTask(HttpServletRequest request){		 
		 Task task=new Task(request.getParameter("a"),request.getParameter("tit"),request.getParameter("con"),
				 			request.getParameter("loc"),request.getParameter("pay"),"true");
		 int i=taskmanager.addTask(task);
		 String put=customermanager.SearchPut(request.getParameter("a"));
		 String done=customermanager.SearchDone(request.getParameter("a"));
		 
		 if(put.equals(null))
			 put="&"+String.valueOf(i);
		 else{
			 StringBuffer putcopy=new StringBuffer(put);
			 putcopy.append("#"+String.valueOf(i));
			 put=putcopy.toString();
		 }
		 
		 int j=customermanager.SearchNumberOfPut(request.getParameter("a"))+1;
		 int k=customermanager.SearchNumberOfDone(request.getParameter("a"));
		 Customer customer=new Customer(request.getParameter("a"),request.getParameter("p"),request.getParameter("h"),String.valueOf(j),String.valueOf(k),put,done);
		
		 customermanager.Update(customer);
		 return String.valueOf(i);
	 }
	 
	 private String RequestNumber(HttpServletRequest request){		 
		 return String.valueOf(customermanager.SearchNumberOfPut(request.getParameter("a")))+"\n"
				 				+String.valueOf(customermanager.SearchNumberOfDone(request.getParameter("a")));
	 }
	 
	 private String ThroughTask(HttpServletRequest request){
		 String s=taskmanager.throughTask(request.getParameter("a"));
		 return s;
	 }
	 
	 private String AcceptTask(HttpServletRequest request){
		 String s=taskmanager.doneTask(Integer.parseInt(request.getParameter("num") ));
		 return s;
	 }
	 
	 private String ThroughDoneTask(HttpServletRequest request){
		 String s=taskmanager.throughDoneTask(request.getParameter("a"));
		 return s;
	 }
	 
	 private String ThroughPutTask(HttpServletRequest request){
		 String s=taskmanager.throughPutTask(request.getParameter("a"));
		 return s;
	 }
	 
	 private String GetRelevantTask(HttpServletRequest request){
		 String s=taskmanager.getRelevantTask(request.getParameter("num"));
		 return s;
	 }
	 

}
