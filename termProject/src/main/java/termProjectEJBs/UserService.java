package termProjectEJBs;

import java.io.ObjectInputFilter.Status;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import Entities.Account;

@Stateless
@Path("")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class UserService
{
	String emailPattern = "@gmail.com";
    @PersistenceContext(unitName = "hello")
    private EntityManager EM;
 
	@POST
	@Path("/register")
	 public Response register( Account account) {
	  
	  String email = account.getEmail();
	  String userName = account.getUserName();
	  String password = account.getPassword();
        try {
        	account.setEmail(account.getEmail());
        	if(!account.getEmail().contains(emailPattern) ) 
        	{
        		return Response.status(Response.Status.BAD_REQUEST).entity("invalid email!").build();
        		
        	}
        	if(email.isEmpty() || userName.isEmpty())
        	{
        		return Response.status(Response.Status.BAD_REQUEST).entity("username or email can not be empty!").build();
        		
        	}
        	account.setUserName(userName);
        		
        	if(existedByUserName(userName) || existedByEmail(email) )
        	{
        		return Response.status(Response.Status.BAD_REQUEST).entity("username or email already existed!").build();
        		
        	}
            if(!validPssword(password))
            {
            	return Response.status(Response.Status.BAD_REQUEST).entity("Invalid password,"
            			+ " the password must have at least one uppercase letter and at least 8 characters").build();

            }
            account.setPassword(password);
            if(password.isEmpty())
            {
            	return Response.status(Response.Status.BAD_REQUEST).entity("Password can not be empty!").build();
            }
            EM.persist(account);
            
            return Response.ok("added!").build();
        }
       
         catch (Exception e) {
          return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error occurred!").build();
       }
    }
	
	
	public boolean existedByUserName(String userName )
	{
		List<Account> similarUserNames = EM.createQuery("SELECT a FROM Account a WHERE a.userName = :userName", Account.class)
                .setParameter("userName", userName).getResultList();
		if(!similarUserNames.isEmpty()) {return true;}
		return false;
	}
	public boolean existedByEmail(String email )
	{
		List<Account> similarEmails = EM.createQuery("SELECT a FROM Account a WHERE a.email = :email", Account.class)
                .setParameter("email", email).getResultList();
		if(!similarEmails.isEmpty()) {return true;}
		return false;
	}
	


	
	@GET
	@Path("/accounts")
	public List<Account> getAllAccounts() {
	        try {
	            return EM.createQuery("SELECT a FROM Account a", Account.class).getResultList();
	        } catch (Exception e) {
	            return null;
	        }
	    }
	

	@GET
	@Path("/loginByEmail")
	public Response loginWithEmail(@QueryParam("email") String email, @QueryParam("password") String password) {
	    try {
	       Account account = EM.createQuery("SELECT a FROM Account a WHERE a.email = :email", Account.class)
	                            .setParameter("email", email)
	                            .getSingleResult();
	
	        if (account != null &&account.getEmail().equals(email) && account.getPassword().equals(password)) {
	            return Response.ok("Valid login!").build();
	        } else {
	            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid email or password").build();
	        }
	    } catch (NoResultException e) {
	        return Response.status(Response.Status.NOT_FOUND).entity("Account not found").build();
	    } 
	}
	
	


     
	@GET
	@Path("/loginByUserName")
	public Response loginWithUserName(@QueryParam("userName") String userName , @QueryParam("password") String password) {
    try {
	       Account account = EM.createQuery("SELECT a FROM Account a WHERE a.userName = :userName", Account.class)
	                            .setParameter("userName", userName)
	                            .getSingleResult();

	        if (account != null &&account.getUserName().equals(userName) &&account.getPassword().equals(password))
	        {
	        	
	        		return Response.ok("valid login!").build();
	        } else {
	            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid email or password").build();
	        }
	    } catch (NoResultException e)
	    {
	        	return Response.status(Response.Status.NOT_FOUND).entity("Account not found").build();
	    }
	}
	
	
	@PUT
	@Path("/updateEmail")
	public Response updateEmail(
			@QueryParam("email") String email ,@QueryParam("updatedEmail") String updatedEmail
			,@QueryParam("password") String password)
	{
		try
		{
			Account account = EM.createQuery("SELECT a FROM Account a WHERE a.email = :email", Account.class)
                    .setParameter("email", email)
                    .getSingleResult();
			if (account != null &&account.getEmail().equals(email)&& account.getPassword().equals(password)) {
	            account.setEmail(updatedEmail);
	            if(!email.contains(emailPattern ))
	            {
	            	return Response.status(Response.Status.BAD_REQUEST).entity("Valid login ,updated email is Invalid ," +
	            "Try entering another email..").build();
	            }
	            if(existedByEmail(email))
	            {
	            	return Response.status(Response.Status.BAD_REQUEST).entity("Valid login ,updated email is already existed ," +
	        	            "Try entering another email..").build();
	            }
	            return Response.ok("Valid login "
	            		+ ",Your email has been updated to : " +updatedEmail).build();
	        } else {
	            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid email or password,email can't be changed ").build();
			
		}
		}
		catch (NoResultException e)
		    {
		        	return Response.status(Response.Status.NOT_FOUND).entity("Account not found").build();
		    }
		}
	
	
	@PUT
	@Path("/updateUserName")
	public Response updateUserName(
			@QueryParam("userName") String userName ,@QueryParam("updatedUserName") String updatedUserName
			,@QueryParam("password") String password)
	{
		try
		{
			 Account account = EM.createQuery("SELECT a FROM Account a WHERE a.userName = :userName", Account.class)
                     .setParameter("userName", userName)
                     .getSingleResult();

			 if (account != null &&account.getUserName().equals(userName) &&account.getPassword().equals(password))
			 {
				    if(existedByUserName(updatedUserName))
				    {
				    	return Response.status(Response.Status.BAD_REQUEST).entity("Valid login ,updated username is already existed ," +
		        	            "Try entering another username..").build();
				    }
 	                account.setUserName(updatedUserName);
				 	return Response.ok("valid login , your username is updated to : "+
 	                updatedUserName).build();
			 	} 
			else {
					return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid email or password").build();
				}
			} catch (NoResultException e)
				{
					return Response.status(Response.Status.NOT_FOUND).entity("Account not found").build();
					}
		}
	
	
	
	@PUT
	@Path("/updatePassword")
	public Response updatePassword(
			@QueryParam("email") String email ,@QueryParam("password") String password
			,@QueryParam("newPassword") String newPassword)
	{
					try {
						Account account = EM.createQuery("SELECT a FROM Account a WHERE a.email = :email", Account.class)
								.setParameter("email", email)
								.getSingleResult();

						if (account != null &&account.getEmail().equals(email) && account.getPassword().equals(password)) {
							if(!validPssword(newPassword))
							{
								return Response.status(Response.Status.BAD_REQUEST).entity("valid login! the new password you entered is invalid, "+
										"Try entering another password..").build();
								
							}
								account.setPassword(newPassword);
		   						return Response.ok("Valid login, your new password is :"+ newPassword).build();
						}
						else {
								return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid Login ,Invalid email or password!").build();
		   					}
				} catch (NoResultException e) {
						return Response.status(Response.Status.NOT_FOUND).entity("Account not found").build();
					} 
		}
	
	
	
	public boolean validPssword(String password)
	{
		boolean flag= false; 
		for(int i =0 ; i< password.length() ;i++)
		{
			char ch = password.charAt(i);
			if(Character.isUpperCase(ch))
			{
				flag = true;
				break;
			}
			continue;
		}
		if(password.length() < 8 || flag== false)
		  return false;
		return true;
	}
	
	
	
	@DELETE
    @Path("/deleteAccount")	
	public Response deleteAccount(
			@QueryParam("email") String email , @QueryParam("password") String password)
	{
		try {
			Account account = EM.createQuery("SELECT a FROM Account a WHERE a.email = :email", Account.class)
					.setParameter("email", email)
					.getSingleResult();
			if (account != null &&account.getEmail().equals(email) && account.getPassword().equals(password)) {
	            EM.remove(account);
	            return Response.ok("Account deleted successfully").build();
	        } else {
	            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid email or password").build();
	        }
	    } catch (NoResultException e) {
	        return Response.status(Response.Status.NOT_FOUND).entity("Account not found").build();
	    } 
		
			
		}
		
}
        
	
	        
	
































