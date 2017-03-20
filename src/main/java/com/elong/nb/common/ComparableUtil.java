/**   
 * @(#)comparable.java	2016年6月1日	下午4:34:56	   
 *     
 * Copyrights (C) 2016艺龙旅行网保留所有权利
 */
package com.elong.nb.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * (类型功能说明描述)
 * 对比两个集合，求交集、并集
 * <p>
 * 修改历史:											<br>  
 * 修改日期    		修改人员   	版本	 		修改内容<br>  
 * -------------------------------------------------<br>  
 * 2016年6月1日 下午4:34:56   Administrator     1.0    	初始化创建<br>
 * </p> 
 *
 * @author		Administrator  
 * @version		1.0  
 * @since		JDK1.7
 */
public class ComparableUtil {
	public static List<String> union(List<String>arr1,List<String>arr2){
		List<String> aList=new ArrayList<String>();
		for (String str : arr1) {
			aList.add(str);
		}
		for (String str : arr2) {
			aList.add(str);
		}
		return aList;
	}
	
	public static List<String> intersect(List<String> arr1,List<String> arr2){
		Map<String, Boolean>map=new HashMap<String,Boolean>();
		List<String>list=new ArrayList<String>();
		for (String str : arr1) {
			if(!map.containsKey(str)){
				map.put(str, Boolean.FALSE);
			}
		}
		for (String str : arr2) {
			if(map.containsKey(str)){
				map.put(str, Boolean.TRUE);
			}
		}
		for(Iterator<Entry<String, Boolean>>iterator=map.entrySet().iterator();iterator.hasNext();){
			Entry<String, Boolean>entry=(Entry<String, Boolean>)iterator.next();
			if(entry.getValue().equals(Boolean.TRUE)){
				list.add(entry.getKey());
			}
		}
		return list;
	}
    //求两个数组的交集   
    public static String[] intersect(String[] arr1, String[] arr2) {   
        Map<String, Boolean> map = new HashMap<String, Boolean>();   
        LinkedList<String> list = new LinkedList<String>();   
        for (String str : arr1) {   
            if (!map.containsKey(str)) {   
                map.put(str, Boolean.FALSE);   
            }   
        }   
        for (String str : arr2) {   
            if (map.containsKey(str)) {   
                map.put(str, Boolean.TRUE);   
            }   
        }   
  
        for (Entry<String, Boolean> e : map.entrySet()) {   
            if (e.getValue().equals(Boolean.TRUE)) {   
                list.add(e.getKey());   
            }   
        }   
  
        String[] result = {};   
        return list.toArray(result);   
    } 
    //去重
    public static List<String> convertDistinctList(List<String> list){
	    	List<String> distinctList= new ArrayList<String>();  
	        for(String i:list){  
	            if(!distinctList.contains(i)){  
	            		distinctList.add(i);  
	            }  
	        }  
	    return distinctList;
    }
}
