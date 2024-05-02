package Entities;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.ejb.Stateless;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Persistence;
import javax.validation.constraints.NotNull;

@Stateless
@Entity
public class Account {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	
	@NotNull
	@Column(unique = true)
	private String email;
	
	@NotNull
	@Column(unique = true)
	private String userName;
	
	@NotNull
	private String password;
	
	public Account(){}
	
	Account(String email ,String userName , String password)
	{
		this.email = email;
		this.userName = userName;
		this.password = password;
	}
	
	  public String getEmail() {
	        return email;
	    }
	    
	    public void setEmail(String email) {
	        this.email = email;
	    }
	
	public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password ;
    }
    

}

