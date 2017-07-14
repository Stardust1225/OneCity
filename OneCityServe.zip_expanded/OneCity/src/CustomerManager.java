import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;

public class CustomerManager {
	
	private HashMap<String,Customer> customermanager;
	private static CustomerManager manager=null;
	
	public synchronized static CustomerManager getInstance(){
		if(manager==null)
			manager=new CustomerManager();
		return manager;
	}
	
	private CustomerManager(){
		customermanager=new HashMap<String, Customer>();
		ReadFromFile();
	}
	
	public synchronized int AddCustomer(Customer customer){
		if(customermanager.get(customer.getAccount())==null){
			customermanager.put(customer.getAccount(), customer);
			WriteToFile();
			return 1;
		}
		else 
			return 0;
	}
	
	public synchronized String ConfirmPassword(Customer customer1){
		Customer customer2=customermanager.get(customer1.getAccount());
		if(customer2.getPassword().equals(customer1.getPassword())){
			return String.valueOf(customer2.getHead());
		}
		else
			return String.valueOf(-1);
	}
	
	public synchronized void Update(Customer customer1){
		customermanager.remove(customer1.getAccount());
		customermanager.put(customer1.getAccount(), customer1);
		WriteToFile();
	}
	
	public String SearchPut(String account){
		return customermanager.get(account).getPut();
	}
	
	public String SearchDone(String account){
		return customermanager.get(account).getDone();
	}
	
	public int SearchNumberOfDone(String account){
		return customermanager.get(account).getNumOfdone();
	}
	
	public int SearchNumberOfPut(String account){
		return customermanager.get(account).getNumOfput();
	}
	
		
	public synchronized void WriteToFile(){
		try{
			File file=new File("D:\\customercontent.txt");
			if(!file.exists())
				file.createNewFile();
			
			FileWriter fw=new FileWriter(file);
			BufferedWriter writer=new BufferedWriter(fw);
			
			for(String key: customermanager.keySet()){
				Customer customer=customermanager.get(key);
				writer.write(customer.getAccount()+"&"+customer.getPassword()+"&"+
						String.valueOf(customer.getHead())+"&"+String.valueOf(customer.getNumOfput())+"&"+
						String.valueOf(customer.getNumOfdone())+"&"+customer.getPut()+"&"+customer.getDone());		
				writer.newLine();
			}
			writer.flush();
			writer.close();
			fw.close();
		}catch(Exception e){}
	}
	
	public synchronized void ReadFromFile(){
		try{
			BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\customercontent.txt")));
			String str=null;
			int i=0,j=-1,k=0;
			String[] content=new String[7];
			while((str=bf.readLine())!=null){
				i=0;j=-1;
				for(k=0;k<6;k++){
					i=j;
					j=str.indexOf("&", i+1);
					content[k]=str.substring(i+1, j);
				}
				content[6]=str.substring(j+1,str.length());
				customermanager.put(content[0], new Customer(content[0],content[1],content[2],content[3],content[4],content[5],content[6]));
			}
			bf.close();
		}catch(Exception e){}
	}
	
	
}
