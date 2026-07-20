import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ex07 {

    public Map<String, Integer> countWords(List<String> words) {
        Map<String, Integer> countMap = new HashMap();
        for(int i = 0; i < words.size(); i++){ 
        if(countMap.containsKey(words.get(i))){
            countMap.put(words.get(i),countMap.get(words.get(i)) + 1);
        }else{
            countMap.put(words.get(i),1);
        }
        }
        return countMap;
    }

    public int getOrZero(Map<String, Integer> map, String key) {
        if(map.containsKey(key)){
            return map.get(key);
        }else{
            return 0;
        }
    }

    public Map<String, Integer> merge(Map<String, Integer> a, Map<String, Integer> b) {
        Map<String, Integer> mergeMap = new HashMap<>();
        for(Map.Entry<String, Integer> e : a.entrySet()) {
            mergeMap.put(e.getKey(), e.getValue());
            }
        for(Map.Entry<String, Integer> e : b.entrySet()) {
            if(mergeMap.containsKey(e.getKey())){
                mergeMap.put(e.getKey(),mergeMap.get(e.getKey()) + e.getValue() );
            }else{
                mergeMap.put(e.getKey(),e.getValue());          
            }
        }
            return mergeMap;
    }

    public String maxKey(Map<String, Integer> map) {
        int maxValue = 0;
        String maxKey = null;
        for(Map.Entry<String, Integer> e : map.entrySet()){
            if(e.getValue() > maxValue){
                maxValue = e.getValue();
                maxKey = e.getKey();
            }
        }
        return maxKey;
    }
}