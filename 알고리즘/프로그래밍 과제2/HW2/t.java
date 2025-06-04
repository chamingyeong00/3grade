import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

class TreeMapDemo {  
	public static void main(String args[]) {  
  
		TreeMap<String, Double> tm = new TreeMap<String, Double>();  
	      
		tm.put("John Doe", new Double(3434.34));  	 // Map에 (키, 값) 쌍 추가 
		tm.put("Tom Smith", new Double(123.22));  
		tm.put("Jane Baker", new Double(1378.00));  
		tm.put("Tod Hall", new Double(99.22));  
		tm.put("Ralph Smith", new Double(-19.08));  

		 // Map에 저장된 쌍들을 모두 출력
		Set<Map.Entry<String, Double>> set = tm.entrySet();  
		for(Map.Entry<String, Double> me : set) 
  		System.out.println(me.getKey() + ": " + me.getValue());  

		// John Doe의 계정에 1000원을 추가 
		double balance = tm.get("John Doe");  
		tm.put("John Doe", balance + 1000);  
		System.out.println("New balance: " +  tm.get("John Doe"));
	}
}