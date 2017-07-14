
public class Task {
	
	private String title,content,location,pay,statue,account;
	
	public Task(String account,String title,String content,String location,String pay,String statue){
		this.title=title;
		this.content=content;
		this.location=location;
		this.pay=pay;
		this.statue=statue;
		this.account=account;
	}
	
	public String getAccount(){
		return account;
	}
	
	public String getTitle(){
		return title;
	}
	
	public String getContent(){
		return content;
	}
	
	public String getLocation(){
		return location;
	}
	
	public String getPay(){
		return pay;
	}
	
	public void changeStatue(){
		statue="Done";
	}

	public String getStatue() {
		return statue;
	}
	
}
