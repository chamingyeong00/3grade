import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class HashMapDemo {  
	public static void main(String args[]) {  
  
		HashMap<String, Double> hm = new HashMap<String, Double>();  
      
		hm.put("John Doe", new Double(3434.34));  	 // Map에 (키, 값) 쌍 추가 
		hm.put("John Doe", 3434.34);
		hm.put("Tom Smith", new Double(123.22));  
		hm.put("Jane Baker", new Double(1378.00));  
		hm.put("Tod Hall", new Double(99.22));  
		hm.put("Ralph Smith", new Double(-19.08));  

		System.out.println("Map의 toString() 내용: " + hm);
		// Map에 저장된 쌍들을 모두 출력
		Set<Map.Entry<String, Double>> set = hm.entrySet();  
		for(Map.Entry<String, Double> me : set)
			System.out.println(me);
//			System.out.println(me.getKey() + ": " + me.getValue());  

		// John Doe의 계정에 1000원을 추가 
		double balance = hm.get("John Doe");  
		hm.put("John Doe", balance + 1000);  
		System.out.println("New balance: " +  hm.get("John Doe"));
	}  
}