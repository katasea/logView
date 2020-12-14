package cn.katasea.util;

import java.io.StringWriter;
import java.net.URLEncoder;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author katasea
 * 2019/7/24 14:27
 */
public class XmlUtil {
		public XmlUtil() {
		}

		public static Object readXml(String xml, String rootName, String rowName) {
			XStream xs = new XStream(new DomDriver());
			xs.alias(rootName, Map.class);
			xs.registerConverter(new XmlUtil.MapEntryConverter(rowName));
			Object object = xs.fromXML(xml);
			return object;
		}

		public static String writeXml(Object object, String rootName, String rowName) {
			Map<String, Object> map = (Map)object;
			XStream xs = new XStream(new DomDriver());
			xs.alias(rootName, Map.class);
			xs.alias(rootName, LinkedHashMap.class);
			xs.alias(rootName, TreeMap.class);
			xs.alias(rootName, Hashtable.class);
			xs.alias(rootName, SortedMap.class);
			xs.registerConverter(new XmlUtil.MapEntryConverter(rowName));
			StringWriter sw = new StringWriter();
			xs.marshal(map, new CompactWriter(sw));
			return "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + sw.toString();
		}

		public static String packageForm(String channelNo, String url, String amt, String backurl, String channel) {
			StringBuffer sb = new StringBuffer();
			sb.append("<form name='payment' id='payment' action='" + url + "' method='post' target='_blank'>");
			sb.append("<input type='hidden' name='channelNo' value='" + channelNo + "' />");
			sb.append("<input type='hidden' name='amt' value='" + amt + "' />");
			sb.append("<input type='hidden' name='backurl' value='" + backurl + "' />");
			sb.append("<input type='hidden' name='channel' value='" + channel + "' />");
			sb.append("</form>");
			return sb.toString();
		}


		public static String createMessageByDigest(String message, String digest) {
			String newMessage = "";
			String signContent;
			String sign;
			if (message.indexOf("<sign>") != -1) {
				signContent = message.substring(message.indexOf("<sign>"), message.indexOf("</sign>") + "</sign>".length());
				sign = "<sign>" + digest + "</sign>";
				newMessage = message.replaceAll(signContent, sign);
			} else {
				signContent = message.substring(0, message.indexOf("</resp>"));
				sign = "<sign>" + digest + "</sign>";
				newMessage = signContent + sign + "</resp>";
			}

			return newMessage;
		}


		public static String createRequestMessage(String encryptMessage, Map<String, String> parma) throws Exception {
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			sb.append("<reqParam>");
			if (parma != null) {
				Iterator i$ = parma.keySet().iterator();

				while(i$.hasNext()) {
					String element = (String)i$.next();
					sb.append("<").append(element).append(">").append((String)parma.get(element)).append("</").append(element).append(">");
				}
			}

			sb.append("<reqXmlParam>").append(encryptMessage).append("</reqXmlParam>");
			sb.append("</reqParam>");

			try {
				String result = URLEncoder.encode(sb.toString(), "UTF-8");
				return result;
			} catch (Exception var5) {
				throw new Exception("创建xml返回报文失败");
			}
		}

		public static String getValue(String message, String key) {
			if (message != null && !"".equals(message)) {
				String itemStart = "<" + key + ">";
				String itemEnd = "</" + key + ">";
				if (message.indexOf(itemStart) != -1) {
					int start = message.indexOf(itemStart) + itemStart.length();
					int end = message.indexOf(itemEnd);
					return message.substring(start, end);
				} else {
					return null;
				}
			} else {
				return null;
			}
		}

		public static class MapEntryConverter implements Converter {
			private String rowName;

			public MapEntryConverter(String rowName) {
				this.rowName = rowName;
			}

			public boolean canConvert(Class clazz) {
				return Map.class.isAssignableFrom(clazz) || LinkedHashMap.class.isAssignableFrom(clazz);
			}

			public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
				this._marshal(value, writer, context);
			}

			private void _marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
				Iterator i$;
				Object object;
				if (value instanceof Map) {
					Map map = (Map)value;

					for(i$ = map.entrySet().iterator(); i$.hasNext(); writer.endNode()) {
						object = i$.next();
						Map.Entry entry = (Map.Entry)object;
						Object _key = entry.getKey();
						Object _value = entry.getValue();
						writer.startNode(entry.getKey().toString());
						if (_value instanceof Map) {
							this._marshal(_value, writer, context);
						} else if (_value instanceof List) {
							this._marshal(_value, writer, context);
						} else {
							writer.setValue(entry.getValue().toString());
						}
					}
				} else if (value instanceof List) {
					List list = (List)value;

					for(i$ = list.iterator(); i$.hasNext(); writer.endNode()) {
						object = i$.next();
						writer.startNode(this.rowName);
						if (!(object instanceof Map) && !(object instanceof List)) {
							writer.setValue(object.toString());
						} else {
							this._marshal(object, writer, context);
						}
					}
				}

			}

			public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
				return this._unmarshal(reader, context);
			}

			public Object _unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
				Map map = new HashMap();
				List list = new ArrayList();

				boolean isList;
				for(isList = false; reader.hasMoreChildren(); reader.moveUp()) {
					reader.moveDown();
					String nodeName = reader.getNodeName();
					if (reader.hasMoreChildren()) {
						if (isList) {
							list.add(this._unmarshal(reader, context));
						} else if (map.containsKey(nodeName)) {
							isList = true;
							list.add(map.remove(nodeName));
							list.add(this._unmarshal(reader, context));
						} else if (this.rowName.equals(nodeName)) {
							isList = true;
							list.add(this._unmarshal(reader, context));
						} else {
							map.put(nodeName, this._unmarshal(reader, context));
						}
					} else {
						String value = reader.getValue();
						if (isList) {
							list.add(value);
						} else if (map.containsKey(nodeName)) {
							isList = true;
							list.add(map.remove(nodeName));
							list.add(value);
						} else if (this.rowName.equals(nodeName)) {
							isList = true;
							list.add(value);
						} else {
							map.put(nodeName, value);
						}
					}
				}

				return isList ? list : map;
			}
		}
}
