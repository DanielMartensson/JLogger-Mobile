package se.danielmartensson.views.messages;

import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Simple status message 
 * @author Daniel Mårtensson
 *
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleMessageStatus {
	private String message; // Can also include a Json string
	private int statuscode;
	
	public <T> void setJsonMessage(T object) {
		Gson gson = new Gson();
		Type type = new TypeToken<T>() {}.getType();
		message = gson.toJson(object, type);
	}
	/**
	 * This is a special converter that be used several times
	 * @param json Our Json string
	 * @param classType Class type, normally SimpleMessageStatus.class
	 * @return
	 */
	public SimpleMessageStatus getJsonMessage(String json){
		Gson gson = new Gson();
		Type type = new TypeToken<SimpleMessageStatus>() {}.getType();
		return gson.fromJson(json, type);
	}
}
