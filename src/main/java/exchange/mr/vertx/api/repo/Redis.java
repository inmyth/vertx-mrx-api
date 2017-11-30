package exchange.mr.vertx.api.repo;

import java.util.Optional;

import redis.clients.jedis.Jedis;

public class Redis {
	private static final String fakeAPIKEY = "ak123456";
	final String host;
	final Integer port;
	
	public Redis(String host, Integer port) {
		this.host = host;
		this.port = port;
	}
	
	
	private Jedis getJedis(){
//		return new Jedis("54.205.99.208",6380);
		return new Jedis(host, port);
	}
	
	public Optional<String> secretOf(String apikey){
		Jedis jedis = getJedis();
		String secret = jedis.get(apikey);
		if (secret != null && !secret.isEmpty()){
			return Optional.of(secret); 
		}
		return Optional.empty();
	}

}
