import java.util.ArrayList;
import java.util.List;

public class Ex06 {

public List<String> addAll(List<String> list,String...items){
    for(int i = 0; i < items.length; i++){
        list.add(items[i]);
        }
    return list;
    }

public List<String> removeShort(List<String> list, int minLen) {
    List<String> result = new ArrayList();
    for(int i = 0; i < list.size(); i++){
    if(list.get(i).length() >= minLen)
    result.add(list.get(i));
        }
    return result;
    }

public String join(List<String> list, String sep) {
    String result = "";
    for(int i = 0; i < list.size(); i++){
    if(i > 0){
        result = result + sep;
        }
    result = result + list.get(i);
    }
    return result;
    }

public int indexOf(List<String> list, String target) {
    for(int i = 0; i < list.size(); i++){
    if(list.get(i).equals(target) ){
        return i;
    }
    }
        return -1;
    }
} 