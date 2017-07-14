import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;

public class TaskManager {
	
	private static HashMap<Integer,Task> taskhashmap=null;
	private static TaskManager taskmanager=null;
	private static Integer number=0;
	
	public synchronized static TaskManager getInstance(){
		if(taskmanager==null)
			taskmanager=new TaskManager();
		return taskmanager;
	}
	
	public TaskManager(){
		taskhashmap=new HashMap<Integer,Task>();
		ReadFromFile();
	}
	
	public synchronized int addTask(Task task){
		taskhashmap.put(number, task);
		WriteToFile();
		number++;
		return number.intValue();
	}
	
	public Task getTask(Integer getNumber){
		Task task=taskhashmap.get(getNumber);
		return task;
	}
	
	public synchronized String doneTask(Integer getNumber){
		Task task=taskhashmap.get(getNumber);
		if(task.getStatue().equals("true")){
			taskhashmap.remove(getNumber);
			task.changeStatue();
			taskhashmap.put(getNumber, task);
			WriteToFile();
			return "1";
		}
		else
			return "0";
	}
	
	
	public String throughTask(String account){
		int i=0;
		StringBuffer buffer=new StringBuffer();
		for(Integer key: taskhashmap.keySet()){
			Task task=taskhashmap.get(key);
			if(task.getStatue().equals("true")&&(!task.getAccount().equals(account))){
				i++;
				buffer.append(task.getAccount()+"&"+task.getTitle()+"&"+task.getContent()+"&"+task.getLocation()
							+"&"+task.getPay()+"&"+String.valueOf(key)+"&"+"\n");
				if(i>=9)
					break;
			}
		}
		return buffer.toString();
	}
	
	public String throughDoneTask(String account){
		int i=0;
		StringBuffer buffer=new StringBuffer();
		for(Integer key: taskhashmap.keySet()){
			Task task=taskhashmap.get(key);
			if(task.getStatue().equals("Done")&&(task.getAccount().equals(account))){
				i++;
				buffer.append(task.getAccount()+"&"+task.getTitle()+"&"+task.getContent()+"&"+task.getLocation()
							+"&"+task.getPay()+"&"+String.valueOf(key)+"&"+"\n");
			}
		}
		return buffer.toString();
	}
	
	public String throughPutTask(String account){
		int i=0;
		StringBuffer buffer=new StringBuffer();
		for(Integer key: taskhashmap.keySet()){
			Task task=taskhashmap.get(key);
			if(task.getAccount().equals(account)){
				i++;
				buffer.append(task.getAccount()+"&"+task.getTitle()+"&"+task.getContent()+"&"+task.getLocation()
							+"&"+task.getPay()+"&"+String.valueOf(key)+"&"+"\n");
			}
		}
		return buffer.toString();
	}
	
	public String getRelevantTask(String number){
		int i=0,j=0;
		StringBuffer buffer=new StringBuffer();
		
		while(i<number.length()&&j<number.length()&&j>=0){
			i=j;j=number.indexOf("?", i+1);
			if(j>=0){
				Task task=taskhashmap.get(Integer.parseInt(number.substring(i+1, j)));
				buffer.append(task.getAccount()+"&"+task.getTitle()+"&"+task.getContent()+"&"+task.getLocation()
				+"&"+task.getPay()+"&"+"\n");
			}
		}
		
		return buffer.toString();
	}
	
	
	
	private synchronized void WriteToFile(){
		try{
			File file=new File("D:\\taskcontent.txt");
			if(!file.exists())
				file.createNewFile();
			
			FileWriter fw=new FileWriter(file);
			BufferedWriter writer=new BufferedWriter(fw);
			
			for(Integer key: taskhashmap.keySet()){
				Task task=taskhashmap.get(key);
				writer.append(key.toString()+"&"+task.getAccount()+"&"+task.getTitle()+"&"+task.getContent()+"&"+task.getLocation()
							+"&"+task.getPay()+"&"+task.getStatue());		
				writer.newLine();
			}
			writer.flush();
			writer.close();
			fw.close();
		}catch(Exception e){}
	}
	
	public synchronized void ReadFromFile(){
		try{
			BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\taskcontent.txt")));
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
				taskhashmap.put(Integer.valueOf(content[0]), new Task(content[1],content[2],content[3],content[4],content[5],content[6]));
				number++;
			}
			bf.close();
		}catch(Exception e){}
	}
}
