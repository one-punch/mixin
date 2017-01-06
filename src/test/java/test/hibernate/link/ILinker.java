package test.hibernate.link;

import com.wteam.mixin.define.IPersistentObject;

public interface ILinker<T extends IPersistentObject,S extends IPersistentObject> {
	
	void link(T t,S s);
	
}
