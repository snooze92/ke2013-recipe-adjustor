package keadjustor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubstitutionsMap {
	private Map<String, List<Substitution>> substitutions;
	private long size;
	
	public SubstitutionsMap() {
		substitutions = new HashMap<String, List<Substitution>>();
		size = 0;
	}
	
	public void put(String key, Substitution val) {
		List<Substitution> list = substitutions.get(key);
		if (list == null) {
			list = new ArrayList<Substitution>();
			substitutions.put(key, list);
		}
		list.add(val);
		++size;
	}
	
	public List<Substitution> get(String key) {
		return substitutions.get(key);
	}
	
	public long size() {
		return size;
	}
}
