package exchange.mr.vertx.api.model;

public class Submit {

	/*
	 * 
	 * {
  "id": 1,
  "command": "submit",
  "secret": "sn3nxiW7v8KXzPzAqzyHXbSSKNuN9",
  "tx_json": {
    "Flags": 0,
    "TransactionType": "AccountSet",
    "Account": "rf1BiGeXwwQoi8Z2ueFYTEXSwuJYfV2Jpn",
    "Fee": "10000"
  }
}
	 */
	
	public enum Command {
		submit
	}
	
	public int id; 
	public String command;
	public String secret;
	
	
	
}
