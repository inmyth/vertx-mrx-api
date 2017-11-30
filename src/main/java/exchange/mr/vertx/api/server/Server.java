package exchange.mr.vertx.api.server;

import java.util.Optional;

import com.jsoniter.JsonIterator;

import exchange.mr.vertx.api.model.Submit;
import exchange.mr.vertx.api.model.Submit.Command;
import exchange.mr.vertx.api.repo.Redis;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;

public class Server {
	
  // Convenience method so you can run it in your IDE
  public static void main(String[] args) throws Exception {
  	String redisHost = args[0];
  	Integer redisPort = Integer.parseInt(args[1]);
  	Redis redis = new Redis(redisHost, redisPort);
  	server(redis);
//    Runner.runExample(Server.class);
  }

//  @Override
//  public void start() throws Exception {   	
//  	
//  	
//  	server();
//  }
  
  private static void server(Redis redis){
  	// debug only !!
  	VertxOptions options = new VertxOptions();
  	options.setBlockedThreadCheckInterval(3601000);
  	Vertx vertx = Vertx.vertx(options);
  	
  	HttpServer server = vertx.createHttpServer();
  	server.requestHandler(request -> {
  	  if (request.path().equals("/")) {
  	    ServerWebSocket wsSer = request.upgrade();
  	    wsSer.writeFinalTextFrame("Vertx");
  	    
  	  	HttpClient client = vertx.createHttpClient(new HttpClientOptions()
  	  			.setDefaultHost("rippled.mr.exchange")
  	  			.setDefaultPort(443)
  	  			.setSsl(true)
  	  			);
  	  	client.websocket("/", wscli -> {
  	  		System.out.println("Connected to ripple");  	  	  
    	    wsSer.handler(buffer -> {
    	    	String data = buffer.getString(0, buffer.length());
    	    	System.out.println(data);
    	    	if (data.contains("command")){
       	    	Submit submit = JsonIterator.deserialize(data, Submit.class);
       	    	if (submit.command.equals(Command.submit.name())){
       	    		Optional<String> secret = redis.secretOf(submit.secret);
       	    		if (secret.isPresent()){
        	    		wsSer.writeFinalTextFrame("GOT YOUR SECRET : " + secret.get());

       	    		}
       	    		else{
       	    			wsSer.writeFinalTextFrame("secret key not found");
       	    		}
       	    		
       	    	}

      	    	wscli.writeFinalTextFrame(data);
      	    	wscli.handler(cliBuf -> {
      	    		wsSer.writeFinalTextFrame(cliBuf.getString(0, cliBuf.length()));
      	    		
      	    	});
    	    	}
    	    	else{
    	    		wsSer.writeFinalTextFrame("submit command only");

    	    	}
 
    	    });
  	  	});
  	  } else {
  	    request.response().setStatusCode(400).end();
  	  }
  	});
  	
  	server.listen(8080);
  }
  
//  private void testCli(){
//  	
//  	HttpClient client = vertx.createHttpClient(new HttpClientOptions()
//  			.setDefaultHost("rippled.mr.exchange")
//  			.setDefaultPort(443)
//  			.setSsl(true)
//  			);
//  	client.websocket("/", ws -> {
//  		System.out.println("Connected to ripple");
//
//  	});
//  }
}