package edu.utd.cs.bdma.synset.validator.shared;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class SynsetDeserializer implements JsonDeserializer<Synset>{

	@Override
	public Synset deserialize(JsonElement je, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
		// TODO Auto-generated method stub
		JsonElement content = je.getAsJsonObject().get("sets");
		 
		 return new Gson().fromJson(content, Synset.class);
	}

}
