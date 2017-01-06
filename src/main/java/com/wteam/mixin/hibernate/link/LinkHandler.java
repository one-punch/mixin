package com.wteam.mixin.hibernate.link;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.wteam.mixin.define.IPersistentObject;

public class LinkHandler {
	

	public static <T extends IPersistentObject, S extends IPersistentObject> void oneToMany(T t, S s,
			ILinker<T, S> linker) {
		oneToMany(new ArrayList<>(Arrays.asList(t)), new ArrayList<>(Arrays.asList(s)), linker);
	}

	public static <T extends IPersistentObject, S extends IPersistentObject> void oneToMany(List<T> tList, S s,
			ILinker<T, S> linker) {
		oneToMany(tList, new ArrayList<>(Arrays.asList(s)), linker);
	}
	
	public static <T extends IPersistentObject, S extends IPersistentObject> void oneToMany(T t, List<S> sList,
			ILinker<T, S> linker) {
		oneToMany(new ArrayList<>(Arrays.asList(t)), sList, linker);
	}
	
	public static <T extends IPersistentObject, S extends IPersistentObject> void oneToMany(List<T> tList, List<S> sList,
			ILinker<T, S> linker) {
		tList.forEach(t -> sList.forEach(s -> linker.link(t, s)) );
	}
	
	public static <T extends IPersistentObject, S extends IPersistentObject> void oneToOne(T t, S s,
			ILinker<T, S> linker) {
		oneToOne(new ArrayList<>(Arrays.asList(t)), new ArrayList<>(Arrays.asList(s)), linker);
	}
	
	public static <T extends IPersistentObject, S extends IPersistentObject> void oneToOne(List<T> tList, List<S> sList,
			ILinker<T, S> linker) {
		int end = tList.size() < sList.size() ? tList.size() : sList.size();
		for (int i = 0; i < end; i++) {
			linker.link(tList.get(i), sList.get(i));
		}
	}

}
