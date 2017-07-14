
public class Customer {
	private String account,password;
	private int head;
	private int numOfput,numOfdone;
	private String put,done;
	
	public Customer(String account,String password,String head){
		this.account=account;
		this.password=password;
		this.head=Integer.valueOf(head).intValue();
		numOfput=0;
		numOfdone=0;
		put=null;
		done=null;
	}
	
	public Customer(String account,String password,String head,String numOfput,String numOfdone,String put, String done){
		this.account=account;
		this.password=password;
		this.head=Integer.parseInt(head);
		this.numOfdone=Integer.parseInt(numOfdone);
		this.numOfput=Integer.parseInt(numOfput);
		this.put=put;
		this.done=done;
	}
	
	public String getAccount(){
		return account;
	}
	
	public String getPassword(){
		return password;
	}
	
	public int getHead(){
		return head;
	}
	
	public int getNumOfput(){
		return numOfput;
	}
	
	public int getNumOfdone(){
		return numOfdone;
	}
	
	public String getPut(){
		return put;
	}
	
	public String getDone(){
		return done;
	}
	
}
