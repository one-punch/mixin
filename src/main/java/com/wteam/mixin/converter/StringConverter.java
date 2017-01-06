package com.wteam.mixin.converter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class StringConverter implements Converter{
	
	private static final String pre = "<![CDATA[";
	private static final String post = "]]>";

	@Override
	public boolean canConvert(Class type) {
		return String.class == type;
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		String value = (String) source;
		writer.setValue(pre+value+post);
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		
		String value = reader.getValue();
		int start = value.indexOf(pre)+pre.length();
		int end = value.indexOf(post);
		
		return value.substring(start, end);
	}
}
