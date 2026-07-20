import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Ex08 {

    public int unique(List<Integer> list) {
        Set<Integer> uniqueSet = new HashSet<>();
        for(int i = 0; i < list.size(); i++){
            uniqueSet.add(list.get(i));
        }
        return uniqueSet.size();
    }

    public boolean hasDuplicate(List<Integer> list) {
        Set<Integer> set = new HashSet<>();
        for(int i = 0; i < list.size(); i++){
            if(set.contains(list.get(i))){
                return true;
            }else{
                set.add(list.get(i));
            }
        }
        return false;
        
    }

    public Set<Integer> intersection(Set<Integer> a, Set<Integer> b) {
        Set<Integer> result = new HashSet<>();
        for(Integer num : a){
            if(b.contains(num)){
                result.add(num);
            }
        }
        return result;
    }

    public Set<Integer> difference(Set<Integer> a, Set<Integer> b) {
        Set<Integer> result = new HashSet<>();
        for(Integer n : a){
            if(!b.contains(n)){
                result.add(n);
            }
        }
        return result;
    }
}