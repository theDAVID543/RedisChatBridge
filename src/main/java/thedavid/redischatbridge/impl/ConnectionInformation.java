package thedavid.redischatbridge.impl;

public class ConnectionInformation{
	public ConnectionInformation(String ip, int port, String username, String password){
		this.ip = ip;
		this.port = port;
		this.username = username;
		this.password = password;
		if(username.isEmpty()){
			username = null;
		}
		if(password.isEmpty()){
			password = null;
		}
	}
	private final String ip;
	private final int port;
	private final String username;
	private final String password;
	public String getIp() {
		return ip;
	}
	public int getPort() {
		return port;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
}
