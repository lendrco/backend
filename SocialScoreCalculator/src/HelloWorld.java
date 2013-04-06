import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
 
import java.io.IOException;
 
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
 
public class HelloWorld extends AbstractHandler
{
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) 
        throws IOException, ServletException
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        response.getWriter().println("<h1>Hello World</h1>");
    }
 
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8080);
        server.setHandler(new HelloWorld());
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try{
           tx = session.beginTransaction();
           List employees = session.createQuery("FROM Employee").list(); 
           for (Iterator iterator = employees.iterator(); iterator.hasNext();){
              Employee employee = (Employee) iterator.next(); 
              System.out.print("First Name: " + employee.getFirstName()); 
              System.out.print("  Last Name: " + employee.getLastName()); 
              System.out.println("  Salary: " + employee.getSalary()); 
           }
           tx.commit();
        }catch (Hibernate e) {
           if (tx!=null) tx.rollback();
           e.printStackTrace(); 
        }finally {
           session.close(); 
        }
        
        server.start();
        server.join();
    }
}